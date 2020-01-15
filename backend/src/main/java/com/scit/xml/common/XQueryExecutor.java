package com.scit.xml.common;

import com.scit.xml.exception.InternalServerException;
import org.exist.interpreter.Compiled;
import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import javax.xml.transform.OutputKeys;

@Configuration
public class XQueryExecutor {

    private final String COLLECTION_ID = "/db/rs/scit";

    private String connectionUri = "xmldb:exist://%1$s:%2$s/exist/xmlrpc";

    @Value("${exist-db.host}")
    private String host;

    @Value("${exist-db.port}")
    private int port;

    @Value("${exist-db.username}")
    private String username;

    @Value("${exist-db.password}")
    private String password;

    @Value("${exist-db.driver}")
    private String driver;

    /**
     * Executes the given query and returns a result.
     * @param documentId unique identifier of the document resource
     * @param query query to be executed
     * @return {@link ResourceSet} instance
     */
    public ResourceSet execute(String documentId, String query) {
        Collection collection = null;
        XMLResource resource = null;

        try {
            // initialize database driver
            System.out.println("[INFO] Loading driver class: " + this.driver);
            Class<?> cl = Class.forName(this.driver);

            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");

            DatabaseManager.registerDatabase(database);

            // get the collection
            System.out.println("[INFO] Retrieving the collection: " + COLLECTION_ID);
            collection = DatabaseManager.getCollection(getCollectionUri());
            collection.setProperty(OutputKeys.INDENT, "yes");

            resource = (XMLResource) collection.getResource(documentId);
            if (resource == null) {
                throw new InternalServerException();
            }

            // get an instance of xquery service
            XQueryService xQueryService = (XQueryService) collection.getService("XQueryService", "1.0");
            xQueryService.setProperty(OutputKeys.INDENT, "yes");

            // compile and execute the expression
            CompiledExpression compiledXquery = xQueryService.compile(query);
            ResourceSet result = xQueryService.execute(compiledXquery);

            return result;
        } catch (Exception e) {
            throw new InternalServerException(e);
        } finally {
            if (resource != null) {
                try {
                    ((EXistResource) resource).freeResources();
                } catch (XMLDBException xe) {
                    throw new InternalServerException(xe);
                }
            }

            if (collection != null) {
                try {
                    collection.close();
                } catch (XMLDBException xe) {
                    throw new InternalServerException(xe);
                }
            }
        }
    }

    /**
     * Gets the URI where the {@link Collection} is loaded from
     * @return
     */
    private String getCollectionUri() {
        return String.format(this.connectionUri, this.host, this.port) + this.COLLECTION_ID;
    }
}
