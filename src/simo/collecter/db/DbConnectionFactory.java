package simo.collecter.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that manages connections to the database
 * @author Thomas
 *
 */
public class DbConnectionFactory {

	private Connection connection = null;
	private static final String DB_SERVER = "jdbc:postgresql://localhost:6789/";
	public static String dbName = "";

	// Remember to add the jdbc.jar file into the projects build path
	public Connection create() {		
		//System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();		    
		}

		//System.out.println("PostgreSQL JDBC Driver Registered!");	 		 
		try {
			connection = DriverManager.getConnection(DB_SERVER + dbName,"postgres", "snaplab");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}

		if (connection != null) {
			//System.out.println("You made it, take control your database now!");
			//System.out.println();
		} else
			System.out.println("If you reach this line, please email me by telling how you do it?");
		
		return connection;
	} // -- END CREATEDBCONNECTION
}
