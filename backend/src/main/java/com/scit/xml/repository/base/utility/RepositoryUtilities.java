package com.scit.xml.repository.base.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.exist.xmldb.EXistResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import com.scit.xml.repository.base.utility.ExistAuthenticationUtilities.ExistConnectionProperties;

public class RepositoryUtilities {

    public static Collection getOrCreateCollection(ExistConnectionProperties conn, String collectionUri) throws XMLDBException {
        return getOrCreateCollection(conn, collectionUri, 0);
    }

    public static Collection getOrCreateCollection(ExistConnectionProperties conn, String collectionUri, int pathSegmentOffset) throws XMLDBException {

        Collection col = DatabaseManager.getCollection(conn.uri + collectionUri, conn.user, conn.password);

        // create the collection if it does not exist
        if(col == null) {

            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }

            String pathSegments[] = collectionUri.split("/");

            if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();

                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/" + pathSegments[i]);
                }

                Collection startCol = DatabaseManager.getCollection(conn.uri + path, conn.user, conn.password);

                if (startCol == null) {

                    // child collection does not exist

                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parentCol = DatabaseManager.getCollection(conn.uri + parentPath, conn.user, conn.password);

                    CollectionManagementService mgt = (CollectionManagementService) parentCol.getService("CollectionManagementService", "1.0");

                    System.out.println("[INFO] Creating the collection: " + pathSegments[pathSegmentOffset]);
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);

                    col.close();
                    parentCol.close();

                } else {
                    startCol.close();
                }
            }
            return getOrCreateCollection(conn, collectionUri, ++pathSegmentOffset);
        } else {
            return col;
        }
    }
	
    
    public static void cleanUpResource(Resource resource){
    	if(resource != null) {
            try {
                ((EXistResource)resource).freeResources();
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }
    }
    
    public static void cleanUpResource(List<XMLResource> resources){
    	for(XMLResource resource : resources) {
	    	if(resource != null) {
	            try {
	                ((EXistResource)resource).freeResources();
	            } catch (XMLDBException xe) {
	                xe.printStackTrace();
	            }
	        }
    	}
    }
    
    
    public static void cleanUpCollection(Collection collection){
    	if(collection != null) {
            try {
            	collection.close();
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }
    }
    
    public static String loadFileContent(String path) throws IOException {
        byte[] allBytes = Files.readAllBytes(Paths.get(path));
        return new String(allBytes, StandardCharsets.UTF_8);
    }
    
    
    
    public static XQueryService initializeXQueryService(Collection collection) throws XMLDBException {
    	XQueryService xQueryService = (XQueryService) collection.getService("XQueryService", "1.0");
        xQueryService.setProperty("indent", "yes");
        return xQueryService;
    }
    
    public static XUpdateQueryService initializeXUpdateQueryService(Collection collection) throws XMLDBException {
        System.out.println("[INFO] Fetching XUpdate service for the collection.");
        XUpdateQueryService xupdateService = (XUpdateQueryService) collection.getService("XUpdateQueryService", "1.0");
        xupdateService.setProperty("indent", "yes");
        return xupdateService;
    }
    
    
      
}