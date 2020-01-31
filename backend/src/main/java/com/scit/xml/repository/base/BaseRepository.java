package com.scit.xml.repository.base;

import static com.scit.xml.repository.base.utility.XUpdateTemplate.APPEND;
import static com.scit.xml.repository.base.utility.XUpdateTemplate.REMOVE;
import static com.scit.xml.repository.base.utility.XUpdateTemplate.UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.OutputKeys;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

import com.scit.xml.repository.base.utility.DatabaseConstants;
import com.scit.xml.repository.base.utility.ExistAuthenticationUtilities.ExistConnectionProperties;
import com.scit.xml.repository.base.utility.ExistConnection;
import com.scit.xml.repository.base.utility.RepositoryUtilities;
import com.scit.xml.repository.base.utility.XQueryUtilities;

public class BaseRepository {
	
	protected final ExistConnectionProperties connection;
	protected String collectionId;
	
	public BaseRepository(ExistConnection existConnection)  {
		this.connection = existConnection.getConnection();
		this.collectionId = DatabaseConstants.DATABASE_COLLECTION;
	}
	

	
	public XMLResource findDocumentById(UUID id) throws XMLDBException{
		Collection collection = null;
		XMLResource resource = null;

		try {    
			System.out.println("[INFO] Retrieving the collection: " + this.collectionId);
			collection = DatabaseManager.getCollection(this.connection.uri + collectionId);
			collection.setProperty(OutputKeys.INDENT, "yes");

    		System.out.println("[INFO] Retrieving the document: " + id);
    		resource = (XMLResource)collection.getResource(id.toString());

    		if(resource == null){
    			System.out.println("[WARNING] Document '" + id + "' can not be found!");
			} 
			else{
				System.out.println(resource.getContent().toString());
				return resource;

			}
	    } 
		finally {
        	RepositoryUtilities.cleanUpResource(resource);
        	RepositoryUtilities.cleanUpCollection(collection);
		}
		return null;
	}
	
	//if xml property sand 'property' with @ example: "@id", "@paperId"...
    public XMLResource findOne(String path, String property, String value) throws XMLDBException{
		Collection collection = null;
		Resource resource = null;
		
		collection = getCollection();
        XQueryService xQueryService = RepositoryUtilities.initializeXQueryService(collection);
        String xquery = XQueryUtilities.exactMatchQuery(path, property, value);
        System.out.print(xquery+"\n==================================\n");
        
        CompiledExpression compiledExpression = xQueryService.compile(xquery);
        ResourceSet resourceSet = xQueryService.execute(compiledExpression);
        ResourceIterator i = resourceSet.getIterator();

        if(i.hasMoreResources()) {
            try {
                resource = i.nextResource();
                XMLResource xmlResource = (XMLResource) resource;
                System.out.println("\n"+resource.getContent().toString());
				return xmlResource;
            } 
            finally {
            	RepositoryUtilities.cleanUpResource(resource);
            	RepositoryUtilities.cleanUpCollection(collection);
            }
        }
        return null;
    }
    
    public List<XMLResource> findAll(UUID id, String collectionPath) throws XMLDBException{
		Collection collection = null;
		Resource resource = null;
		
		collection = getCollection();
        XQueryService xQueryService = RepositoryUtilities.initializeXQueryService(collection);
        String xquery = XQueryUtilities.collectionMatchForUser(id, collectionPath);
        System.out.print(xquery+"\n==================================\n");
        
        CompiledExpression compiledExpression = xQueryService.compile(xquery);
        ResourceSet resourceSet = xQueryService.execute(compiledExpression);
        ResourceIterator i = resourceSet.getIterator();
        
        List<XMLResource> list = new ArrayList<XMLResource>();

        while(i.hasMoreResources()) {
            try {
                resource = i.nextResource();
                XMLResource xmlResource = (XMLResource) resource;
                System.out.println("\n"+resource.getContent().toString());
                list.add(xmlResource);
            } 
            finally {
            	RepositoryUtilities.cleanUpResource(resource);
            	RepositoryUtilities.cleanUpCollection(collection);
            }
        }
        return list;
    }
    
    
    //TARGET_NAMESPACE is http://www.ftn.uns.ac.rs/scit/xml/document
    public void removeById(UUID documentId, String context, UUID id) throws XMLDBException {
    	
    	String contextXPathPatern = "/document/"+ context +"[@id = '%s']";
    	String contextXPath = String.format(contextXPathPatern, id.toString());
    	
        System.out.println("\t- collection ID: " + collectionId);
        Collection collection = null; 
        try { 
        	collection = getCollection();  
            XUpdateQueryService xupdateService = RepositoryUtilities.initializeXUpdateQueryService(collection);

            System.out.println("[INFO] Removing " + contextXPath + " node.");
         	long mods = xupdateService.updateResource(documentId.toString(), String.format(REMOVE, contextXPath));
            System.out.println("[INFO] " + mods + " modifications processed.");
            
        } finally {
        	RepositoryUtilities.cleanUpCollection(collection);
        }
    }
    
    //TARGET_NAMESPACE is http://ftn.uns.ac.rs/scit/xml/document
    public UUID insert(UUID documentId, String context, String content) throws XMLDBException {
    	
    	UUID id = UUID.randomUUID();
    	String[] holder = context.split("/");
    	String subcontext =  holder[holder.length-1];   	
    	content = tempBuilder(documentId.toString(), subcontext, content, id.toString());
    	//System.out.println(content);
    	
    	String contextXPath= "/document/"+holder[0];
    	
        System.out.println("\t- collection ID: " + collectionId);
        Collection collection = null; 
        try { 
        	collection = getCollection();  
            XUpdateQueryService xupdateService = RepositoryUtilities.initializeXUpdateQueryService(collection);
            
            System.out.println("[INFO] Appending fragments as last child of " + contextXPath + " node.");
            long mods = xupdateService.updateResource(documentId.toString(), String.format(APPEND, contextXPath, content));
            System.out.println("[INFO] " + mods + " modifications processed."); 
            
            return id;
        } 
        finally {
        	RepositoryUtilities.cleanUpCollection(collection);
        }    

    }
    
    //TARGET_NAMESPACE ishttp://ftn.uns.ac.rs/scit/xml/document
    public void update(UUID documentId, String context, String id, String property, String content) throws XMLDBException {
    	String xPath = context+String.format("[@id='%s']/@%s", id, property);
    	update(documentId, xPath, content);
    }
    
    public void update(UUID documentId, String context, String id, String content) throws XMLDBException {
    	String xPath = context+String.format("[@id='%s']", id);
    	update(documentId, xPath, content);
    }
    
    public void update(UUID documentId, String xPath, String content) throws XMLDBException {
    	System.out.println("\t- collection ID: " + collectionId);
        Collection collection = null; 
        try { 
        	collection = getCollection();  
            XUpdateQueryService xupdateService = RepositoryUtilities.initializeXUpdateQueryService(collection);

            System.out.println("[INFO] Removing " + xPath + " node.");
         	long mods = xupdateService.updateResource(documentId.toString(), String.format(UPDATE, xPath, content));
            System.out.println("[INFO] " + mods + " modifications processed.");
            
        } finally {
        	RepositoryUtilities.cleanUpCollection(collection);
        }
    }
    
    
    
    	
    protected Collection getCollection() throws XMLDBException {

        Collection collection = null;

        try {
            System.out.println("[INFO] Retrieving the collection: " + this.collectionId);
            collection = DatabaseManager.getCollection(this.connection.uri + this.collectionId, this.connection.user, this.connection.password);
            collection.setProperty("indent", "yes");
            return collection;
            
        } finally {
            RepositoryUtilities.cleanUpCollection(collection);
        }
    }
	


	
	//there should be builder for new docuent... params (String id, String username, String password, String email)
	//probably from shema
	

	
	private static String tempBuilder(String documentId, String context, String content, String id) {
        StringBuffer newString  = new StringBuffer(content); 
        
        String counter = "<" + context;

        newString.insert(counter.length(), String.format(" documentId=\"%s\" id = \"%s\" ", documentId, id)); 

        return newString.toString(); 
	}
	
//	private static String tempBuilder(String content, String id) {
//        StringBuffer newString  = new StringBuffer(content); 
//        
//        String counter = "<document>";
//        newString.insert(counter.length(), String.format("id = \"%s\" ", id)); 
//
//        return newString.toString(); 
//	}
	
	

}
