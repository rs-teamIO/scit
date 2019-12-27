package com.scit.xml.repository.base.utility;

public class XQueryUtilities {

	public static String exactMatchQuery(String path, String property, String value) {
		return exactMatch(DatabaseConstants.DATABASE_COLLECTION, path, property,  value);
	}
	
	private static String exactMatch(String collectionId, String path, String property, String value) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append("for $file in collection(\""+collectionId+"\")\r\n");
		if(path.equals("")){
			builder.append("where $file//" + property + " eq \""+value+"\"\n");
			builder.append("return $file\n");
		}
		else {
			builder.append("where $file//"+path+"/" + property + " eq \""+value+"\"\n");
			builder.append("return $file//"+path+"\n");
		}
		return builder.toString();
	}
	
	public static String checkUsernameAndPasswordQuery(String username, String password) {
		StringBuilder builder = new StringBuilder("xquery version \"3.1\";\n");
		builder.append("for $file in collection(\""+DatabaseConstants.DATABASE_COLLECTION+"\")\r\n");
		builder.append("where $file//username eq \""+username+"\"\n");
		builder.append("  and $file//password eq \""+password+"\"\n");
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