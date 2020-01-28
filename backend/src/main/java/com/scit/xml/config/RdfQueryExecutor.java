package com.scit.xml.config;

import javafx.util.Pair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Contains configuration parameters for RDF Store and implements methods
 * for querying and manipulating data in the RDF Store using SPARQL queries.
 */
@Configuration
public class RdfQueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RdfQueryExecutor.class);

    @Value("${jena-fuseki.endpoint}")
    private String endpoint;

    @Value("${jena-fuseki.username}")
    private String username;

    @Value("${jena-fuseki.password}")
    private String password;

    @Value("${jena-fuseki.dataset}")
    private String dataset;

    @Value("${jena-fuseki.query-prefix}")
    private String query;

    @Value("${jena-fuseki.update-prefix}")
    private String update;

    private String queryEndpoint;
    private String updateEndpoint;

    /**
     * Initializes the class fields and configures default HttpClient with authentication.
     * The default HttpClient is required to access the remote RDF Store.
     *
     * Executed immediately after a class instance has been constructed.
     */
    @PostConstruct
    public void init() {
        LOGGER.info("Executing PostConstruct of RdfQueryExecutor...");

        queryEndpoint = String.join("/", this.endpoint, this.dataset, this.query);
        updateEndpoint = String.join("/", this.endpoint, this.dataset, this.update);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        HttpOp.setDefaultHttpClient(httpclient);

        LOGGER.info("Executed PostConstruct of RdfQueryExecutor. Default HttpClient is configured.");
    }

    /**
     * Executes a SPARQL SELECT query.
     *
     * @param query query to be executed
     * @return a pair of {@link ResultSet} and {@link QueryExecution} instances
     */
    public Pair<ResultSet, QueryExecution> select(String query) {
        LOGGER.info("Executing SELECT query...");

        QueryExecution queryExecution = QueryExecutionFactory.sparqlService(queryEndpoint, query);
        ResultSet resultSet = queryExecution.execSelect();

        LOGGER.info("SELECT query executed.");
        return new Pair<>(resultSet, queryExecution);
    }

    /**
     * Executes an INSERT query.
     *
     * @param query query to be executed
     */
    public void insert(String query) {
        LOGGER.info("Executing INSERT query...");
        UpdateExecutionFactory.createRemote(UpdateFactory.create(query), updateEndpoint).execute();
        LOGGER.info("INSERT query executed.");
    }

    /**
     * Executes an SPARQL UPDATE query.
     *
     * @param query query to be executed
     */
    public void update(String query) {
        LOGGER.info("Executing UPDATE query...");
        UpdateExecutionFactory.createRemote(UpdateFactory.create(query), updateEndpoint).execute();
        LOGGER.info("UPDATE query executed.");
    }
}
