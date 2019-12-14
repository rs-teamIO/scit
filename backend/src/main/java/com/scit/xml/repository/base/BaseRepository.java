package com.scit.xml.repository.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;

import org.springframework.core.io.ClassPathResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import com.scit.xml.domain.IDomain;
import com.scit.xml.exception.UndefinedNamespacePrefixMapper;
import com.scit.xml.repository.base.utility.ExistAuthenticationUtilities.ExistConnectionProperties;
import com.scit.xml.repository.base.utility.ExistConnection;
import com.scit.xml.repository.base.utility.RepositoryUtils;
import com.scit.xml.repository.base.utility.XQueryUtils;

public class BaseRepository<T extends IDomain> {

	private final ExistConnectionProperties connection;
	
	protected String collectionId;
	protected JAXBContext context;
	
	public BaseRepository(
			ExistConnection existConnection
			) throws IOException, JAXBException  {	
		
		this.connection = existConnection.getConnection();
		this.context = JAXBContext.newInstance("com.scit.xml.domain");
	}

    public UUID save(T object) throws XMLDBException, JAXBException, UndefinedNamespacePrefixMapper {
    
        // a collection of Resources stored within an XML database
        Collection collection = null;
        XMLResource resource = null;
        OutputStream outputstream = new ByteArrayOutputStream();

        try {
            System.out.println("[INFO] Retrieving the collection: " + this.collectionId);
            collection = RepositoryUtils.getOrCreateCollection(this.connection, this.collectionId);

            System.out.println("[INFO] Inserting the document: " + object.getId());
            resource = (XMLResource) collection.createResource(object.getId().toString(), XMLResource.RESOURCE_TYPE);

            System.out.println("[INFO] Unmarshalling XML document to an JAXB instance: ");

            Marshaller marshaller = this.context.createMarshaller();
            
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);        
             
//           if(NamespaceMapper.checkNamespacePrefixMapper(this.collectionId)) {
//                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespaceMapper());
//           }
//           else {
//               throw new UndefinedNamespacePrefixMapper("Undefined NamespacePrefixMapper for collection " + collectionId + "!");
//            }

            // marshal the contents to an output stream
            marshaller.marshal(object, outputstream);

            // link the stream to the XML resource
            resource.setContent(outputstream);
            
            System.out.println("[INFO] Storing the document: " + resource.getId());
            collection.storeResource(resource);
            
            System.out.println("[INFO] Done.");
            
            return object.getId();

        } finally {
            RepositoryUtils.cleanUpResource(resource);
            RepositoryUtils.cleanUpCollection(collection);
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<T> findAll() throws JAXBException, XMLDBException{
    	
    	List<T> list = new ArrayList<T>();
    	
    	Collection collection = this.getCollection();
        XQueryService xQueryService = RepositoryUtils.initializeXQueryService(collection);
        String xquery = XQueryUtils.buildQueryAll(this.collectionId);
        System.out.print(xquery);
        CompiledExpression compiledExpression = xQueryService.compile(xquery);
        ResourceSet resourceSet = xQueryService.execute(compiledExpression);
        ResourceIterator i = resourceSet.getIterator();

        Resource resource = null;
        while(i.hasMoreResources()) {
            try {
                resource = i.nextResource();
                XMLResource xmlResource = (XMLResource) resource;
                System.out.println("[INFO] Binding XML resouce to an JAXB instance: ");
                Unmarshaller unmarshaller = this.context.createUnmarshaller();
				T object = (T) unmarshaller.unmarshal(xmlResource.getContentAsDOM());
				System.out.println(object);
				list.add(object);
            } 
            finally {
                RepositoryUtils.cleanUpResource(resource);
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
	public T findById(UUID id) throws JAXBException, XMLDBException{
    	T object = null;
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

				Unmarshaller unmarshaller = this.context.createUnmarshaller();
					
				object = (T) unmarshaller.unmarshal(resource.getContentAsDOM());
				System.out.println("[INFO] Showing the document as JAXB instance: ");
				System.out.println(object);
    		}
        } 
		finally {
			RepositoryUtils.cleanUpResource(resource);
			RepositoryUtils.cleanUpCollection(collection);
		}
		return object;
    }
    
	@SuppressWarnings("unchecked")
    public T findByUniqueProperty(String propertyName, String value) throws XMLDBException, JAXBException{
		T object = null;
        Collection collection = this.getCollection();
        XQueryService xQueryService = RepositoryUtils.initializeXQueryService(collection);
        String xquery = XQueryUtils.buildQuery(this.collectionId, propertyName, value);
        System.out.print(xquery);
        CompiledExpression compiledExpression = xQueryService.compile(xquery);
        ResourceSet resourceSet = xQueryService.execute(compiledExpression);
        ResourceIterator i = resourceSet.getIterator();

        Resource resource = null;
        if(i.hasMoreResources()) {
            try {
                resource = i.nextResource();
                XMLResource xmlResource = (XMLResource) resource;
                System.out.println("[INFO] Binding XML resouce to an JAXB instance: ");
                Unmarshaller unmarshaller = this.context.createUnmarshaller();
				object = (T) unmarshaller.unmarshal(xmlResource.getContentAsDOM());
				System.out.println(object);
				return object;
            } 
            finally {
                RepositoryUtils.cleanUpResource(resource);
            }
        }
        return object;
    }

    protected XMLResource getResource(String documentId) throws XMLDBException {

        Collection collection = null;
        XMLResource resource = null;

        try {
            System.out.println("[INFO] Retrieving the collection: " + this.collectionId);
            collection = DatabaseManager.getCollection(this.connection.uri + this.collectionId, this.connection.user, this.connection.password);
            collection.setProperty(OutputKeys.INDENT, "yes");

            System.out.println("[INFO] Retrieving the document: " + documentId);
            resource = (XMLResource)collection.getResource(documentId);

            if(resource == null) {
                System.out.println("[WARNING] Document '" + documentId + "' can not be found!");
            }
            return resource;
            
        } finally {
            RepositoryUtils.cleanUpResource(resource);
            RepositoryUtils.cleanUpCollection(collection);
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
            RepositoryUtils.cleanUpCollection(collection);
        }
    }

    @SuppressWarnings("unchecked")
	public void initialize() throws XMLDBException, JAXBException, UndefinedNamespacePrefixMapper, IOException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();
        String[] holder = this.collectionId.split("/");
        String collectionPath = "instances/";
    	for(int i = 3; i<holder.length; i++) {
    		collectionPath = collectionPath+"/"+holder[i];
    	}
        System.out.println(holder[holder.length-1]);
        final File folder = new ClassPathResource(collectionPath).getFile();
        
        for (final File fileEntry : folder.listFiles()) {
        	System.out.println(fileEntry.getName());
        	T objects = (T) unmarshaller.unmarshal(new ClassPathResource( collectionPath +"/" + fileEntry.getName()).getFile());
            System.out.println(objects);
            this.save(objects);
        }
	}
    

}

