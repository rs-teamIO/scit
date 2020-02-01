package com.scit.xml.repository;

import com.scit.xml.common.util.RegexExtractor;
import com.scit.xml.config.RdfQueryExecutor;
import com.scit.xml.rdf.RdfTriple;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * RDF Store data access layer.
 */
@Component
@RequiredArgsConstructor
public class RdfRepository {

    private static final String PREFIX = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n\n";
    private static final String INSERT_INTO_DEFAULT_GRAPH = PREFIX + "INSERT DATA { %s }";
    private static final String INSERT_INTO_GRAPH = PREFIX + "INSERT DATA { GRAPH <%s> { %s } }";
    private static final String DELETE_AND_INSERT = PREFIX + "DELETE { <%1$s> ?p ?o } WHERE { <%1$s> ?p ?o } ; INSERT DATA { %2$s }";
    private static final String DELETE_FROM_GRAPH = PREFIX + "DELETE WHERE { <%1$s> %2$s <%3$s> }";

    private static final String DELETE_BY_SUBJECT = PREFIX + "DELETE WHERE { <%1$s> ?p ?o }";
    private static final String DELETE_BY_OBJECT = PREFIX + "DELETE WHERE { ?s ?p <%1$s> }";

    private final RdfQueryExecutor rdfQueryExecutor;

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

    public boolean ask(String query) {
        return this.rdfQueryExecutor.ask(query);
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

    // TODO: Review implementation
    public void deleteTriple(String s, String p, String o) {
        String query = String.format(DELETE_FROM_GRAPH, s, p, o);
        this.rdfQueryExecutor.update(query);
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
}
