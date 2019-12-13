package com.scit.xml.repository.base.utils;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;


public class NamespaceMapper extends NamespacePrefixMapper {

    private static final String USERS_URI = "http://ftn.uns.ac.rs/scit/users";
    private static final String USERS_PREFIX = "users";


    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        
    	if(USERS_URI.equals(namespaceUri)) {
            return USERS_PREFIX;
        }

        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] { USERS_URI };
    }
    
    
	
	public static boolean checkNamespacePrefixMapper(String collectionId) {
		
      
           return collectionId.endsWith(USERS_PREFIX); // || collectionId.endsWith(...)
	}
	
	

}
