
//This class belongs to the ut.JAR.CPEN410 package
package ut.JAR.CPEN410;

//Import the java.sql package for managing the ResulSet objects
import java.sql.* ;

/******
	This class manage a connection to the Department database and it should be accessed from the front End. Therefore,
	this class must contain all needed methods for manipulating data without showing how to access the database

*/
public class applicationProductManager{

	//myDBConn is an MySQLConnector object for accessing to the database
	private MySQLConnector myDBConn;
	
	/********
		Default constructor
		It creates a new MySQLConnector object and open a connection to the database
		@parameters:
		
	*/
	public applicationProductManager(){
		//Create the MySQLConnector object
		myDBConn = new MySQLConnector();
		
		//Open the connection to the database
		myDBConn.doConnection();
	}
	
	
	/*******
		listAllDepartment method
			List all products in the database
			@parameters:
			@returns:
				A ResultSet containing all departments in the database
	*/
	public ResultSet listAllProducts()
	{
		
		//Declare function variables
		String fields, tables;
		
		//Define the table where the selection is performed
		tables="product";
		//Define the list fields list to retrieve from the table department
		fields ="*";
		
		
		System.out.println("listing...");
		
		//Return the ResultSet containing all departments in the database
		return myDBConn.doSelect(fields, tables);
		
		
	}
	
	/*********
		close method
			Close the connection to the database.
			This method must be called at the end of each page/object that instatiates a applicationDBManager object
			@parameters:
			@returns:
	*/
	public void close()
	{
		//Close the connection
		myDBConn.closeConnection();
	}

	

}