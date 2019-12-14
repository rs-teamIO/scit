package com.scit.xml.repository.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;

import org.springframework.core.io.ClassPathResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.scit.xml.domain.IDomain;
import com.scit.xml.exception.UndefinedNamespacePrefixMapper;
import com.scit.xml.repository.base.utility.ExistAuthenticationUtilities.ExistConnectionProperties;
import com.scit.xml.repository.base.utility.ExistConnection;
import com.scit.xml.repository.base.utility.RepositoryUtils;

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

    protected void save(T object) throws XMLDBException, JAXBException, UndefinedNamespacePrefixMapper {
    
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

        } finally {
            RepositoryUtils.cleanUpResource(resource);
            RepositoryUtils.cleanUpCollection(collection);
        }
    }


    protected XMLResource getResource(String documentId) throws Exception {

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

    protected Collection getCollection() throws Exception {

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

