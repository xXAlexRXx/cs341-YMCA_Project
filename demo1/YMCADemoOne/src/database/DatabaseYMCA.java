package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Program;
import model.Registration;
import model.User;

public class DatabaseYMCA {

	/*
	 * load SQL driver (JDBC: Java Database Connector/ODBC)
	 * - add to build path
	 * 
	 * set up our database (script)
	 * 
	 * connect to the database
	 * 
	 * insert/modify/delete data (Java)
	 * 
	 * query data (Java)
	 * 
	 * disconnect from the database
	 * 
	 */

	String url = "jdbc:mysql://173.24.68.203:3306/ymcatest_demo1?user=admin&password=";


	
	private Connection connection;
	
	public DatabaseYMCA() {
		String password = "CS341Group!"; //TODO: set this to your password
		url = url + password;
	}
	
	public void connect() {							//Connect to database when given url
		try {
			connection = DriverManager.getConnection(url);
			System.out.println("Connection Successful");
		} catch (SQLException e) {
			System.out.println("Cannot connect!");
			System.out.println(e);
		}
	}
	
	public void disconnect() {					//disconnect from database
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Cannot disconnect!");
		}
	}
	
	public ResultSet runQuery(String query) throws SQLException {		//Runs a query in the form of a string and returns a result set
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}
	
	public int runUpdate(String query) throws SQLException {
	    // Ensure that a connection exists
	    if (connection == null) {
	        throw new SQLException("No database connection established.");
	    }
	    
	    Statement stmt = null;
	    int rowsAffected = 0;
	    try {
	        stmt = connection.createStatement();
	        rowsAffected = stmt.executeUpdate(query);
	    } finally {
	        // Ensure the Statement is closed even if an exception occurs.
	        if (stmt != null) {
	            stmt.close();
	        }
	    }
	    return rowsAffected;
	}

	
	public String getAllUsers() {
		return "SELECT * FROM Program";
	}
	
	public String getAllPrograms() {
		return "SELECT * FROM Program";
	}
	
	public String getAllRegistrations() {
		return "SELECT * FROM Registration";
	}
	
	public String getProgramByName(String name) {
        return "SELECT * FROM Program WHERE program_name = '" + name + "'";
    }
	
	public String getRegistrationsForUser(long userId) {
        return "SELECT * FROM Registration WHERE user_id = " + userId;
    }
	
	public String getProgramsForUser(long userId) {
        return "SELECT P.* " +
               "FROM Program P " +
               "INNER JOIN Registration R ON P.program_id = R.program_id " +
               "WHERE R.user_id = " + userId;
    }
	
	public String getUsersForProgram(long programId) {
        return "SELECT U.* " +
               "FROM Program U " +
               "INNER JOIN Registration R ON U.user_id = R.user_id " +
               "WHERE R.program_id = " + programId;
    }
	
	public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO User(username, password, email, user_type,balance) VALUES(?, ?, ?, ?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getUserType());
        pstmt.setDouble(5, user.getUserBalance());
        pstmt.executeUpdate();
        pstmt.close();
    }
	
	public void deleteUser(Long userId) throws SQLException {
	    String sql = "DELETE FROM User WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}
	
	public void updateUser(User user) throws SQLException {
	    String sql = "UPDATE User SET username = ?, password = ?, email = ?, user_type = ?, balance = ? WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, user.getUsername());
	    pstmt.setString(2, user.getPassword());
	    pstmt.setString(3, user.getEmail());
	    pstmt.setString(4, user.getUserType());
	    pstmt.setLong(5, user.getUserId());
	    pstmt.setDouble(6, user.getUserBalance());
	    pstmt.executeUpdate();
	    pstmt.close();
	}
	
	
	public void addProgram(Program program) throws SQLException {
	    String sql = "INSERT INTO Program (" 
	               + "program_name, description, capacity, current_capacity, "
	               + "start_date, end_date, start_time, end_time, "
	               + "location, price, requirements"
	               + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    
	    pstmt.setString(1, program.getProgramName());
	    pstmt.setString(2, program.getDescription());
	    pstmt.setInt(3, program.getCapacity());
	    pstmt.setInt(4, program.getCurrentCapacity());
	    
	    // Convert LocalDate to java.sql.Date
	    pstmt.setDate(5, java.sql.Date.valueOf(program.getStartDate()));
	    pstmt.setDate(6, java.sql.Date.valueOf(program.getEndDate()));
	    
	    // Convert LocalTime to java.sql.Time
	    pstmt.setTime(7, java.sql.Time.valueOf(program.getStartTime()));
	    pstmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));
	    
	    // Insert location and price
	    pstmt.setString(9, program.getLocation());
	    pstmt.setDouble(10, program.getPrice());
	    
	    // Requirements
	    pstmt.setInt(11, program.getRequirements());
	    
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	
	public void deleteProgram(Long programId) throws SQLException {
	    String sql = "DELETE FROM Program WHERE program_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, programId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}
	
	public void updateProgram(Program program) throws SQLException {
	    String sql = "UPDATE Program SET program_name = ?, description = ?, capacity = ?, current_capacity = ?, date = ?, start_time = ?, requirements = ? WHERE program_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, program.getProgramName());
	    pstmt.setString(2, program.getDescription());
	    pstmt.setInt(3, program.getCapacity());
	    pstmt.setInt(4, program.getCurrentCapacity());
	    pstmt.setDate(5, java.sql.Date.valueOf(program.getStartDate()));
	    pstmt.setDate(6, java.sql.Date.valueOf(program.getEndDate()));
	    pstmt.setTime(7, java.sql.Time.valueOf(program.getStartTime()));
	    pstmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));
	    pstmt.setInt(9, program.getRequirements());
	    pstmt.setLong(10, program.getProgramId());
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	
	public void addRegistration(Registration registration) throws SQLException {
	   
	        // Insert the registration record.
	        String sql = "INSERT INTO Registration(user_id, program_id, registration_date) VALUES(?, ?, ?)";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setLong(1, registration.getUserId());
	        pstmt.setLong(2, registration.getProgramId());
	        pstmt.setDate(3, java.sql.Date.valueOf(registration.getRegistrationDate()));
	        pstmt.executeUpdate();
	        pstmt.close();
	      	        
	     
	}

	
	public void deleteRegistration(Long registrationId) throws SQLException {
	    String sql = "DELETE FROM Registration WHERE registration_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, registrationId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}
	
	public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
	    String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, username);
	    pstmt.setString(2, password);
	    ResultSet rs = pstmt.executeQuery();
	    User user = null;
	    if(rs.next()) {
	        // Assuming your User model has these setters and a no-args constructor
	        user = new User();
	        user.setUserId(rs.getLong("user_id"));
	        user.setUsername(rs.getString("username"));
	        user.setPassword(rs.getString("password"));
	        user.setEmail(rs.getString("email"));
	        user.setUserType(rs.getString("user_type"));
	        user.setUserBalance(rs.getDouble("balance"));
	    }
	    rs.close();
	    pstmt.close();
	    return user;
	}

}
