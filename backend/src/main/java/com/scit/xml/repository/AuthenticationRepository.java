package com.scit.xml.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import com.scit.xml.repository.base.BaseRepository;
import com.scit.xml.repository.base.utility.DatabaseConstants;
import com.scit.xml.repository.base.utility.ExistConnection;
import com.scit.xml.repository.base.utility.RepositoryUtilities;
import com.scit.xml.repository.base.utility.XQueryUtilities;


@Repository
public class AuthenticationRepository extends BaseRepository {

	@Autowired
	public AuthenticationRepository(ExistConnection existConnection) {
		super(existConnection);
	}

	public UUID saveNewDocument(String username, String password, String email, String role) throws XMLDBException {
		UUID id = UUID.randomUUID();
		
		String content = initialDocumentBuilder(id.toString(), username, password, email, role);
		
		System.out.println(content);
		System.out.println("\t- collection ID: " + collectionId);
    	System.out.println("\t- document ID: " + id);
		
        Collection collection = null;
        XMLResource resource = null;
        
        try { 
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
        	collection = RepositoryUtilities.getOrCreateCollection(connection, collectionId);
            
            System.out.println("[INFO] Inserting the document: " + id);
            resource = (XMLResource) collection.createResource(id.toString(), XMLResource.RESOURCE_TYPE);
            
            resource.setContent(content);
            System.out.println("[INFO] Storing the document: " + resource.getId());
            
            collection.storeResource(resource);
            System.out.println("[INFO] Done."); 
        } finally {
        	RepositoryUtilities.cleanUpResource(resource);
        	RepositoryUtilities.cleanUpCollection(collection);
        }
        return id;
	}
	
	public XMLResource signIn(String username, String password) throws XMLDBException {
		return this.checkUsernamePassword(username, password);
	}
	
	private XMLResource checkUsernamePassword(String username, String password) throws XMLDBException{
			Collection collection = null;
			Resource resource = null;
			
			collection = getCollection();
	        XQueryService xQueryService = RepositoryUtilities.initializeXQueryService(collection);
	        String xquery = XQueryUtilities.checkUsernameAndPasswordQuery(username, password);
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

	private static String initialDocumentBuilder(String id, String username, String password, String email, String role) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("<document xmlns=\"%s\" id = \"%s\" role =\"%s\">\n",DatabaseConstants.ROOT_NAMESPACE, id, role));
		builder.append(String.format("\t<username>%s</username>\n", username));
		builder.append(String.format("\t<password>%s</password>\n", password));
		builder.append(String.format("\t<email>%s</email>\n", email));
		builder.append("\t<papers></papers>\n");
		builder.append("\t<cover_letters></cover_letters>\n");
		builder.append("\t<reviews></reviews>\n");
		builder.append("</document>\n");
		return builder.toString();
	}
}
