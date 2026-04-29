
//This class belongs to the ut.JAR.CPEN410 package
package ut.JAR.CPEN410;

//Import the java.sql package for managing the ResulSet objects
import java.sql.* ;

//Import hashing functions
import org.apache.commons.codec.*;

/******
	This class authenticate users using userName and passwords

*/
public class applicationDBAuthenticationGoodComplete{

	//myDBConn is an MySQLConnector object for accessing to the database
	private MySQLCompleteConnector myDBConn;
	
	/********
		Default constructor
		It creates a new MySQLConnector object and open a connection to the database
		@parameters:
		
	*/
	public applicationDBAuthenticationGoodComplete(){
		//Create the MySQLConnector object
		myDBConn = new MySQLCompleteConnector();
		
		//Open the connection to the database
		myDBConn.doConnection();
	}
	/*******
		authenticate method
			Authentication method
			@parameters:
			@returns:
				A ResultSet containing the userName and all roles assigned to her.
	*/
	public ResultSet authenticate(String userName, String userPass)	{
		
		//Declare function variables
		String fields, tables, whereClause, hashingVal;
		
		//Define the table where the selection is performed
		tables="usergood, roleusergood";
		//Define the list fields list to retrieve assigned roles to the user
		fields ="usergood.userName, roleusergood.roleId, usergood.Name";
		hashingVal = hashingSha256(userName + userPass);
		whereClause="usergood.userName = roleusergood.userName and usergood.userName='" +userName +"' and hashing='" + hashingVal + "'";
		whereClause += "and usergood.status='Active'";
		
		System.out.println("listing authenticate...");
		
		//Return the ResultSet containing all roles assigned to the user
		return myDBConn.doSelect(fields, tables, whereClause);
	}
	/*******
		menuElements method
			This method configures the necessary information to send to the MySQLCompleteConnector object, retrieves all menu options according their role.
			The result depends on the role and category specific to each role
			@parameters:
				userName: Username of the person the menu option will be retried 
				rolId: The roleId that controls which menu item the user will have
			@returns:
				A ResultSet containing the pageURL, title and pageTItle assigned to the user.
	*/
	public ResultSet menuElements(String userName, String rolId){
		//Declare function variables
		String fields, tables, whereClause, orderBy;
		
		//Define the table where the selection is performed
		tables="roleusergood, role, rolewebpagegood, menuElement,webpagegood ";
		//Define the list fields list to retrieve assigned roles to the user
		fields ="rolewebpagegood.pageURL, menuElement.title, webpagegood.pageTitle";
		//Define the whereClause list to obtain the specific desired information
		whereClause=" roleusergood.roleID=role.roleID and role.roleID=rolewebpagegood.roleId and menuElement.menuID = webpagegood.menuID";
		whereClause+=" and rolewebpagegood.pageURL=webpagegood.pageURL";
		whereClause+=" and userName='"+ userName +"'";
		whereClause+= " and roleusergood.RoleId = '"+rolId+"'";
		if("rol1".equals(rolId)){
			whereClause +=" and webpagegood.MenuID IN(1,2,4)";
		}
		else if("rol3".equals(rolId)){
			whereClause +=" and webpagegood.MenuID IN(2,4)";
		}
		else{
		}
		whereClause+=" order by menuElement.title, webpagegood.pageTitle";
		System.out.println("listing menuElement...");
		
		//Return the ResultSet containing all roles assigned to the user
		return myDBConn.doSelect(fields, tables, whereClause);
	}	
	
	/*******
		verifyUser method
			This method configures the necessary information to send to the MySQLCompleteConnector object, retrieves the users information.
			The result depends on the role, current and previous webpage specific to each role
			@parameters:
				userName: Username of the person who will be verified.
				rolId: The roleId of the user to know what permission they have.
			@returns:
				A ResultSet containing the pageURL, title and pageTItle assigned to the user.
	*/
	public ResultSet verifyUser(String userName, String currentPage, String previousPage)	{
		
		//Declare function variables
		String fields, tables, whereClause, hashingVal;
		
		//Define the table where the selection is performed
		tables="roleusergood, role, rolewebpagegood, webpagegood, usergood, webpageprevious";
		//Define the list fields list to retrieve assigned roles to the user
		fields ="usergood.userName, roleusergood.roleId, usergood.Name ";
		//Define the whereClause list to obtain the specific desired information
		whereClause=" usergood.userName = roleusergood.userName and usergood.userName='" +userName +"' and role.roleId=roleusergood.roleId and ";
		whereClause+=" rolewebpagegood.roleId=role.roleId and rolewebpagegood.pageURL=webpagegood.pageURL and webpagegood.pageURL='" +currentPage+"' and ";
		whereClause+=" webpageprevious.previousPageURL='"+previousPage+"' and webpageprevious.currentPageURL=webpagegood.pageURL";
		whereClause += " and usergood.status='Active'";
		
		System.out.println("listing verifyUser...");
		
		//Return the ResultSet containing all roles assigned to the user
		return myDBConn.doSelect(fields, tables, whereClause);
	}
	/*******
		addUser method  
			This method configures the necessary information to send to the MySQLCompleteConnector object, inserts a new user into the usergood table.
			The password is hashed using SHA-256 before being inserted, and the user is set to Active status by default.
		@parameters:
			userName: Username of the new user.
			completeName: Full name of the new user.
			userPass: Raw password to be hashed.
			DateofBirth: The user birthdate .
			Gender: Gender of the user (Male, Female, Other).
			Street: User street address.
			Town: User town.
			State: User state.
			Country: User country.
			Degree: Academic degree of the user.
			School: User school.
			profilePath: Path to the user profile picture.
		@returns:
			A boolean valuevalue: true: the insert was ok, an error was generated
	*/
	public boolean addUser(String userName, String completeName, String userPass, String DateofBirth,String Gender,String Street,String Town,String State,String Country,String Degree,String School, String profilePath)
	{
		//Declare function variables
		boolean res;
		String table, values, hashingValue;
		//Creating hashed password using SHA256 at a hexadecimal number
		hashingValue=hashingSha256(userName + userPass);
		//Define the table where the insert is performed
		table="usergood";
		//Define the list values that will be inserted
		values="'"+userName+"', '" +hashingValue+"', '"+ completeName + "', '" + DateofBirth + "','"+ Gender + "','"+ Street + "','"+ Town + "','"+ State + "','"+ Country + "','"+ Degree + "','"+ School + "', 'Active', '" + profilePath + "'";
		res=myDBConn.doInsert(table, values);
		System.out.println("listing addUser..");
		//Return the boolean value: true: the insertion was ok, an error was generated.
		return res;
	}
	/*******
		addRoleUser method  
			This method configures the necessary information to send to the MySQLCompleteConnector object, inserts a default student role for new user.
			The current date is automatically used for the assigned date of the role.
		@parameters:
			userName: Username of the user to whom the student role will be assigned.
		@returns:
			A boolean value: true: the insertion was ok, an error was generated.
	*/
	public boolean addRoleUser(String userName)	{
		//Declare function variables
		boolean res;
		String table, values;
		//Define the table where the insert is performed
		table = "roleusergood";
		//Define the list values that will be inserted
		values = "'"+userName+"', 'rol3', CURDATE()";
		res = myDBConn.doInsert(table,values);
		System.out.println("listing addRoleUser..");
		//Return the boolean value: true: the insertion was ok, an error was generated.
		return res;
	}
	/*******
		addFriend method  
			This method configures the necessary information to send to the MySQLCompleteConnector object, inserts a new friendship between two users.
			The current date is automatically used for the friendship creation date.
		@parameters:
			userName: Username of the user sending the friend request.
			friendUserName: Username of the user receiving the friend request.
		@returns:
			A boolean value: true: the insertion was ok, an error was generated.
		*/
	public boolean addFriend(String userName, String friendUserName) {
		//Declare function variables
		boolean res;
		String table, values, filter;
		//Define the table where the insert is performed
		table = "friendship";
		//Define the list values that will be inserted
		values = "'"+userName+"', '"+ friendUserName+"', CURDATE()";
		//Define which specific attribute the data will be inserted in
		filter = "(User1, User2, FriendshipDate)";
		res = myDBConn.doInsert(table,filter,values);
		System.out.println("listing addFriend...");
		//Return the boolean value: true: the insertion was ok, an error was generated.
		return res;
	}
	
	/*******
		addProfilePicture method  
			This method configures the necessary information to send to the MySQLCompleteConnector object, updates the profile picture path for a specific user.
			The update is applied to the userGood table using the username as the condition.
		@parameters:
			userName: Username whose profile picture will be updated.
			filePath: Path to the new profile picture.
		@returns:
			A boolean value: true: the insertion was ok, an error was generated.
	*/
	
	public boolean addProfilePicture(String userName, String filePath) {
		//Declare function variables
		boolean res;
		//Define the new information it is going to modify		
		String setClause = "profilePicture='" + filePath + "'";
		//Define which user is going to be updated		
		String whereClause = "userName='" + userName + "'";
		//Define the table where the insert is performed		
		String table = "userGood";
		System.out.println("listing addProfilePicture");
		//Return the boolean value: true: the insertion was ok, an error was generated.
		return res = myDBConn.doUpdate(setClause,table, whereClause);
	}		
	
/*******
	removeUser method  
		This method configures the necessary information to send to the MySQLCompleteConnector object.
		This is a soft delete that save the user's data in the database.
	@parameters:
		userName: Username of the user who will be deactivated.
	@returns:
		A boolean value: true: the insertion was ok, an error was generated.
*/
	
	public boolean removeUser(String userName){
		//Declare function variables
		boolean res;
		String table, setClause, whereClause;
		//Define the table where the insert is performed		
		table = "userGood";
		//Define the new information it is going to modify	
		setClause = "status = 'Inactive' ";
		//Define which user is going to be updated		
		whereClause = "userName = '" + userName + "'";
		System.out.println("listing removeUser");
		return res = myDBConn.doUpdate(setClause, table, whereClause);
	}
	
	/*******
		UpdateUser method  
			This method configures the necessary information to send to the MySQLCompleteConnector object.
			Only filled parameters are updated.
		@parameters:
			userName: Username of the user whose information will be updated.
			userPass: New password to be hashed and inserted.
			completeName: Full name of the user.
			DateOfBirth: Date of birth of the user.
			Gender: Gender of the user.
			Street: Street address of the user.
			Town: Town or city of the user.
			State: State or region of the user.
			Country: Country of the user.
			Degree: Academic degree of the user.
			School: School of the user.
		@returns:
			A boolean value: true: the insertion was ok, an error was generated.
	*/
	
	public boolean UpdateUser(String userName, String userPass, String completeName, String DateOfBirth, String Gender, String Street, String Town, String State, String Country, String Degree, String School){
		//Declare function variables
		boolean res; 
		String table, values, whereClause, hashingValue;
		//Define the table where the insert is performed	
		table = "usergood";
		//Define the new information it is going to modify	
		values = "";
		//Verifies which field of the users information will be updated
		if (userPass != null && !userPass.isEmpty()) {
			hashingValue = hashingSha256(userName + userPass);
			values += " hashing='" + hashingValue + "',";
		}	
		if(completeName != null && !completeName.isEmpty()){
			values += " Name='"+completeName+"',";
		}
		if(DateOfBirth != null && !DateOfBirth.isEmpty()){
			values += " DateOfBirth='"+DateOfBirth+"',";
		}
		if(Gender != null && !Gender.isEmpty()){
			values += " Gender='"+Gender+"',";
		}
		if(Street != null && !Street.isEmpty()){
			values += " Street='"+Street+"',";
		}
		if(Town != null && !Town.isEmpty()){
			values += " Town='"+Town+"',";
		}
		if(State != null && !State.isEmpty()){
			values += " State='"+State+"',";
		}
		if(Country != null && !Country.isEmpty()){
			values += " Country='"+Country+"',";
		}
		if(Degree != null && !Degree.isEmpty()){
			values += " Degree='"+Degree+"',";
		}
		if(School != null && !School.isEmpty()){
			values += " School='"+School+"',";
		}
		if(values.endsWith(",")){
			values = values.substring(0, values.length() -1)+ " ";
		}
		//Define which user is going to be updated		
		whereClause = "userName='"+userName+"'";	
		System.out.println("listing UpdateUser");
		return res = myDBConn.doUpdate(values, table, whereClause);
	}
	/*******
		searchUserFriend method  
			This method configures the necessary information to send to the MySQLCompleteConnector object.
			Filters include town, state, country, gender, and age. Only active users are returned.
		@parameters:
			userName: Username of the user performing the search.
			Town: Town to filter friends by.
			State: State to filter friends by.
			Country: Country to filter friends by.
			Gender: Gender to filter friends by.
			Age: Age to filter friends by, calculated from DateOfBirth.
		@returns:
			A ResultSet containing the matching friends Username, name, town, state, country, gender, age, and profile picture.
	*/
	public ResultSet searchUserFriend(String userName, String Town, String State, String Country, String Gender, String Age) {
		//Declare function variables
		String fields, tables, whereClause;
		//Define the table where the selection is performed
		tables = "userGood";
		//Define the list fields to retrieve the userName, name, town, state, country, gender, age and profile picture of the user
		fields = "userName, name, town, state, country, gender, TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age, profilePicture"; //It calculats the time difference of the birtyear and current date to get the age
		//Define the whereClause list to obtain the specific desired information
		whereClause = "userName IN (SELECT IF(User1 = '" + userName + "', User2, User1) FROM friendship WHERE '" + userName + "' IN (User1, User2))"; //returns every user that is a friend
		whereClause += " AND status = 'Active'";
	
		if (Town != null && !Town.isEmpty()) {
		whereClause += " AND town = '" + Town + "'";
		}
		if (State != null && !State.isEmpty()) {
			whereClause += " AND state = '" + State + "'";
		}
		if (Country != null && !Country.isEmpty()) {
			whereClause += " AND country = '" + Country + "'";
		}
		if (Gender != null && !Gender.isEmpty()) {
			whereClause += " AND gender = '" + Gender + "'";
		}
		if (Age != null && !Age.isEmpty()) {
			whereClause += " AND TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) = " + Age;
		}
		whereClause += " AND userName != '" + userName + "'";
		
		System.out.println("listing searchUserFriend...");
		//Return the ResultSet containing the searched users information		
		return myDBConn.doSelect(fields, tables, whereClause);
	}
	/*******
		searchUserNotFriend method  
			This method configures the necessary information to send to the MySQLCompleteConnector object.
			Filters include town, state, country, gender, and age. Only active users are returned.
		@parameters:
			userName: Username of the user performing the search.
			Town: Town to filter friends by.
			State: State to filter friends by.
			Country: Country to filter friends by.
			Gender: Gender to filter friends by.
			Age: Age to filter friends by, calculated from DateOfBirth.
		@returns:
			A ResultSet containing the matching possible friends Username, name, town, state, country, gender, age, and profile picture.
	*/
	public ResultSet searchUserNotFriend(String userName, String Town, String State, String Country, String Gender, String Age) {
		//Declare function variables
		String fields, tables, whereClause;
		//Define the table where the selection is performed
		tables = "userGood";
		//Define the list fields to retrieve  the userName, name, town, state, country, gender, age and profile picture of the user
		fields = "userName, name, town, state, country, gender, TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age, profilePicture";//It calculats the time difference of the birtyear and current date to get the age 
		//Define the whereClause list to obtain the specific desired information
		whereClause = "userName NOT IN (SELECT IF(User1 = '" + userName + "', User2, User1) FROM friendship WHERE '" + userName + "' IN (User1, User2))";  //returns every user that is not a friend
		whereClause += " AND status = 'Active'";
	
		if (Town != null && !Town.isEmpty()) {
		whereClause += " AND town = '" + Town + "'";
		}
		if (State != null && !State.isEmpty()) {
			whereClause += " AND state = '" + State + "'";
		}
		if (Country != null && !Country.isEmpty()) {
			whereClause += " AND country = '" + Country + "'";
		}
		if (Gender != null && !Gender.isEmpty()) {
			whereClause += " AND gender = '" + Gender + "'";
		}
		if (Age != null && !Age.isEmpty()) {
			whereClause += " AND TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) = " + Age;
		}
		whereClause += " AND userName != '" + userName + "'";
		
		System.out.println("listing searchUserNotFriend...");
		//Return the ResultSet containing the searched users information
		return myDBConn.doSelect(fields, tables, whereClause);
	}
	
	/*******
		getUserProfile method  
			This method configures the necessary information to send to the MySQLCompleteConnector object.
		@parameters:
			userName: Username of the user whose profile information will be retrieved.
		@returns:
			A ResultSet containing all user profile fields from the usergood table for the given username.
	*/

	public ResultSet getUserProfile(String userName) {
		//Declare function variables
		String fields, tables, whereClause;
		//Define the table where the selection is performed
		tables = "usergood";
		//Define the list fields to retrieve all information the user
		fields = "*"; 
		//Define the whereClause list to obtain the specific desired information
		whereClause = "userName='" + userName + "'";
		System.out.println("listing getUserProfile...");
		//Return the ResultSet containing the user profile picture
		return myDBConn.doSelect(fields, tables, whereClause);
	}
	/*******
		searchAllUser method  
			This method configures the necessary information to send to the MySQLCompleteConnector object.
		@parameters:
			userName: Username of the user performing the search.
		@returns:
			A ResultSet containing all active users information except the one specified.
	*/
	
	public ResultSet searchAllUser(String userName) {
		//Declare function variables
		String fields, tables, whereClause;
		//Define the table where the selection is performed
		tables = "userGood";
		//Define the list fields to retrieve all information the user
		fields = "*";
		//Define the whereClause list to obtain the specific desired information
		whereClause = "userName != '" + userName + "'";
		whereClause += " AND status = 'Active'";
		System.out.println("listing searchAllUser...");
		//Return the ResultSet containing the searched users information
		return myDBConn.doSelect(fields, tables, whereClause);
	}	
	
/*******
	getFriend method  
		This method configures the necessary information to send to the MySQLCompleteConnector object.
		The method checks both sides of the friendship to determine the users connections.
	@parameters:
		userName: Username of the user whose friends will be retrieved.
	@returns:
		A ResultSet containing the friend's name, date of birth, gender, address details, academic info, and profile picture.
*/
	
	public ResultSet getFriend(String userName) {
		//Declare function variables
		String fields, tables, whereClause;
		//Define the table where the selection is performed
		tables = "userGood";
		//Define the whereClause list to obtain the specific desired information
		fields = "Name, DateOfBirth, Gender, Street, Town, State, Country, Degree, School, profilePicture";
		//Define the whereClause list to obtain the specific desired information
		whereClause = "UserName IN (SELECT IF(User1 = '"+userName+"', User2, User1) FROM friendship WHERE '"+userName+"' IN (User1, User2))"; //if the search is done by user1 it shows user2, otherwise it will return user1
		whereClause += " and status='Active'";
		System.out.println("listing getFriend...");
		//Return the ResultSet containing the friends information
		return myDBConn.doSelect(fields, tables, whereClause);
	}

	/*********
		hashingSha256 method
			Generates a hash value using the sha256 algorithm.
			@parameters: Plain text
			@returns: the hash string based on the plainText
	*/
	private String hashingSha256(String plainText)
	{
			String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(plainText); 
			return sha256hex;
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