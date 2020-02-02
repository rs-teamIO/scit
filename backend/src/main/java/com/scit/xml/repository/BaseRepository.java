package com.scit.xml.repository;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.common.util.DefaultNamespacePrefixMapper;
import com.scit.xml.common.util.RegexExtractor;
import com.scit.xml.common.util.SchemaValidationEventHandler;
import com.scit.xml.config.RdfQueryBuilder;
import com.scit.xml.config.RdfQueryExecutor;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InternalServerException;

import com.scit.xml.exception.handlers.SpecificExceptionHandler;
import com.scit.xml.rdf.RdfTriple;
import javafx.util.Pair;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Data access layer base class.
 * Encapsulates XML Database logic and RDF Store logic.
 */
@Component
@RequiredArgsConstructor
public abstract class BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

    @Value("classpath:xq/common/append.xml")
    protected Resource appendTemplate;

    @Value("classpath:xq/common/remove.xml")
    protected Resource removeTemplate;

    protected final XQueryBuilder xQueryBuilder;
    protected final XQueryExecutor xQueryExecutor;
    protected final String documentId;

    protected <T> String marshal(Class<T> clazz, T instance) {
        LOGGER.info(String.format("Marshalling %s instance...", clazz.getName()));
        try {
            final StringWriter stringWriter = new StringWriter();
            final JAXBContext context = JAXBContext.newInstance(clazz);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new DefaultNamespacePrefixMapper());
            marshaller.marshal(instance, stringWriter);
            LOGGER.info(String.format("Marshalling of %s instance successful.", clazz.getName()));
            return stringWriter.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("Marshalling of %s instance unsuccessful.", clazz.getName()));
            throw new InternalServerException(e);
        }
    }

    // =============================================================================

    private static final String PREFIX = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n\n";
    private static final String INSERT_INTO_DEFAULT_GRAPH = PREFIX + "INSERT DATA { %s }";
    private static final String DELETE_AND_INSERT = PREFIX + "DELETE { <%1$s> ?p ?o } WHERE { <%1$s> ?p ?o } ; INSERT DATA { %2$s }";
    private static final String DELETE_FROM_GRAPH = PREFIX + "DELETE WHERE { <%1$s> %2$s <%3$s> }";

    private static final String DELETE_BY_SUBJECT = PREFIX + "DELETE WHERE { <%1$s> ?p ?o }";
    private static final String DELETE_BY_OBJECT = PREFIX + "DELETE WHERE { ?s ?p <%1$s> }";
    private static final String DELETE_BY_PREDICATE_AND_OBJECT = PREFIX + "DELETE WHERE { ?s <%1$s> <%2$s> }";

    protected final RdfQueryBuilder rdfQueryBuilder;
    protected final RdfQueryExecutor rdfQueryExecutor;

    private RegexExtractor regexExtractor = new RegexExtractor("(http:\\/\\/www\\.scit\\.org\\/.*\\/[0-9a-z-]+)");

    /**
     * Returns a list of identifiers of subjects for given query.
     *
     * @param query SELECT query to be executed
     * @return list of unique identifiers
     */
    public List<String> selectSubjects(String query) {
        Pair<ResultSet, QueryExecution> pair = this.rdfQueryExecutor.select(query);
        ResultSet resultSet = pair.getKey();
        QueryExecution queryExecution = pair.getValue();

        List<String> subjectIds = new ArrayList<>();

        String extractedId;
        while (resultSet.hasNext()) {
            extractedId = this.regexExtractor.extract(resultSet.next().toString());
            subjectIds.add(extractedId);
        }
        queryExecution.close();

        return subjectIds;
    }

    /**
     * Executes a SPARQL SELECT query on the RDF store.
     *
     * @param query query to be executed
     * @return a pair of {@link ResultSet} and {@link QueryExecution} instances
     */
    public Pair<ResultSet, QueryExecution> selectTriples(String query) {
        Pair<ResultSet, QueryExecution> result = this.rdfQueryExecutor.select(query);
        return result;
    }

    /**
     * Executes a SPARQL ASK query. Returns true if query is satisfied, otherwise returns false.
     *
     * @param query ASK query to be executed
     * @return true if query is satisfied, otherwise returns false
     */
    public boolean ask(String query) {
        boolean result = this.rdfQueryExecutor.ask(query);
        return result;
    }

    /**
     * Executes an INSERT query on the RDF store.
     *
     * @param rdfTriples list of {@link RdfTriple} instances to be inserted
     */
    public void insertTriples(List<RdfTriple> rdfTriples) {
        String rdfTriplesString = this.convertRdfTriplesToString(rdfTriples);
        String query = String.format(INSERT_INTO_DEFAULT_GRAPH, rdfTriplesString);
        this.rdfQueryExecutor.insert(query);
    }

    /**
     * Executes an SPARQL UPDATE query on the RDF store.
     *
     * @param subjectId unique identifier of the subject
     * @param rdfTriples list of {@link RdfTriple} instances to be updated
     */
    public void updateTriples(String subjectId, List<RdfTriple> rdfTriples) {
        String rdfTriplesString = this.convertRdfTriplesToString(rdfTriples);
        String query = String.format(DELETE_AND_INSERT, subjectId, rdfTriplesString);
        this.rdfQueryExecutor.update(query);
    }

    /**
     * Converts given {@link RdfTriple} instances into string representation.
     *
     * @param rdfTriples list of {@link RdfTriple} instances to be converted
     * @return
     */
    private String convertRdfTriplesToString(List<RdfTriple> rdfTriples) {
        StringBuilder rdfTriplesStrings = new StringBuilder();
        rdfTriples.stream().forEach(rdfTriple -> {
            rdfTriplesStrings.append(rdfTriple.toString());
        });
        return rdfTriplesStrings.toString();
    }




    // TODO: Review implementation = OVDE DOLAZE UNWRAPPED VREDNOSTI A SAM QUERY IH WRAPPUJE PO PATTERNU

    public void deleteTriple(String s, String p, String o) {

        String query = String.format(DELETE_FROM_GRAPH, s, p, o);
        this.rdfQueryExecutor.delete(query);
    }

    public void deleteAllMetadata(String entityId) {
        String subjectQuery = String.format(DELETE_BY_SUBJECT, entityId);
        this.rdfQueryExecutor.delete(subjectQuery);
        String objectQuery = String.format(DELETE_BY_OBJECT, entityId);
        this.rdfQueryExecutor.delete(objectQuery);
    }

    public void deleteMetadataByObject(String objectId) {
        String objectQuery = String.format(DELETE_BY_OBJECT, objectId);
        this.rdfQueryExecutor.delete(objectQuery);
    }

    public void deleteMetadataByPredicateAndObject(String predicate, String objectId) {
        String predicateAndObjectQuery = String.format(DELETE_BY_PREDICATE_AND_OBJECT, predicate, objectId);
        this.rdfQueryExecutor.delete(predicateAndObjectQuery);
    }
}
