package com.scit.xml.repository.base.utility;

public class XQueryUtils {

	
	public static String buildQuery(String collectionId, String propertyName, String value) {
		
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append("for $object in collection(\""+collectionId+"\")\r\n");
		builder.append("where $object//"+ propertyName +" = \"" +value + "\" \n");
		builder.append("return $object\n");

		return builder.toString();
		
	}
	
	public static String buildQueryAll(String collectionId) {
		
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append("for $object in collection(\""+collectionId+"\")\r\n");
		builder.append("return $object\n");

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
