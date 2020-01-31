package com.scit.xml.repository.base.utility;

import java.util.UUID;

public class XQueryUtilities {

	public static String exactMatchQuery(String path, String property, String value) {
		return exactMatch(DatabaseConstants.DATABASE_COLLECTION, path, property,  value);
	}
	private static String exactMatch(String collectionId, String path, String property, String value) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append(String.format("declare namespace doc = \"%s\";\n", DatabaseConstants.ROOT_NAMESPACE));

		builder.append("for $file in collection(\""+collectionId+"\")\r\n");
		if(path.equals("")){
	
			builder.append("where $file//" + ( property.contains("@") ? property : "doc:"+property ) + " eq \""+value+"\"\n");
			builder.append("return $file\n");
		}
		else {
			builder.append("where $file//doc:"+path+"/" + property + " eq \""+value+"\"\n");
			builder.append("return $file//doc:"+path+"\n");
		}
		return builder.toString();
	}
	
	
	public static String collectionMatchQuery(String collectionPath) {
		return collectionMatch(DatabaseConstants.DATABASE_COLLECTION, collectionPath);
	}
	private static String collectionMatch(String collectionId, String collectionPath) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append(String.format("declare namespace doc = \"%s\";\n", DatabaseConstants.ROOT_NAMESPACE));
		builder.append("for $file in collection(\""+collectionId+"\")\r\n");
		builder.append("for $element in $file//"+collectionPath+"\r\n");
		builder.append("return $element\n");
		return builder.toString();
	}
	
	public static String collectionMatchForUser(UUID id, String collectionPath) {
		return collectionMatchForUser(DatabaseConstants.DATABASE_COLLECTION, id, collectionPath);
	}
	private static String collectionMatchForUser(String collectionId, UUID id, String collectionPath) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append(String.format("declare namespace doc = \"%s\";\n", DatabaseConstants.ROOT_NAMESPACE));
		builder.append("for $file in collection(\""+collectionId+"\")\r\n");
		builder.append("where $file//@id eq \""+id+"\"\n");
		builder.append("for $element in $file//doc:"+"papers/doc:paper"+"\r\n");
		builder.append("return $element\n");
		return builder.toString();
	}
	
	public static String checkUsernameAndPasswordQuery(String username, String password) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append(String.format("declare namespace doc = \"%s\";\n", DatabaseConstants.ROOT_NAMESPACE));
		builder.append("for $file in collection(\""+DatabaseConstants.DATABASE_COLLECTION+"\")\r\n");
		builder.append("where $file//doc:username eq \""+username+"\"\n");
		builder.append("  and $file//doc:password eq \""+password+"\"\n");
		builder.append("return $file\n");
		return builder.toString();
	}

	
	
//	private static String buildCollectionNamespace(String collectionId) {
//        String[] holder = collectionId.split("/");
//        String collectionPath = DatabaseConstants.DATABASE_ROOT;
//    	for(int i = 3; i<holder.length; i++) {
//    		collectionPath = collectionPath+"/"+holder[i];
//    	}
//		return "\""+collectionPath+"\"";
//	}
	
	
	public static String test(String collectionId) {
	
		
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");

		builder.append("for $v in collection(\"/db/scit/users\")\r\n");
		builder.append("where $v//username = \"test2\"\n");
		builder.append("return $v\n");
		
		return builder.toString();
		
	}
	
	
	
}