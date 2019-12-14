package com.scit.xml.repository.base.utility;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import com.scit.xml.repository.base.utility.ExistAuthenticationUtilities.ExistConnectionProperties;

@Component
public class ExistConnection {

	private ExistConnectionProperties connection;
	private Database database;

	public ExistConnection() throws IOException, ClassNotFoundException, XMLDBException, InstantiationException, IllegalAccessException{
		this.connection = ExistAuthenticationUtilities.loadProperties(); 
        // initialize database driver
        Class<?> cl = Class.forName(this.connection.driver);
        // encapsulation of the database driver functionality
        database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // entry point for the API which enables you to get the Collection reference
        DatabaseManager.registerDatabase(database);
	}

	public ExistConnectionProperties getConnection() {
		return connection;
	}
	
}
