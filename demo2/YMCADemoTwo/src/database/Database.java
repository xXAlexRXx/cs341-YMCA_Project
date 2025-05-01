package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import model.Program;
import model.Registration;
import model.User;

public class Database {

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

	String url = "jdbc:mysql://173.24.68.203:3306/ymcatest_demo2?user=admin&password=";



	private Connection connection;

	public Database() {
		String password = "CS341Group!"; //TODO: set this to your password
		url = url + password;
	}

	public void connect() throws SQLException {							//Connect to database when given url
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

	public Connection getConnection() {
	    return connection;
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
	               + "location, price, requirements, days, staff_id"
	               + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    PreparedStatement pstmt = connection.prepareStatement(sql);

	    pstmt.setString(1, program.getProgramName());
	    pstmt.setString(2, program.getDescription());
	    pstmt.setInt(3, program.getCapacity());
	    pstmt.setInt(4, program.getCurrentCapacity());
	    pstmt.setDate(5, java.sql.Date.valueOf(program.getStartDate()));
	    pstmt.setDate(6, java.sql.Date.valueOf(program.getEndDate()));
	    pstmt.setTime(7, java.sql.Time.valueOf(program.getStartTime()));
	    pstmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));
	    pstmt.setString(9, program.getLocation());
	    pstmt.setDouble(10, program.getPrice());
	    pstmt.setInt(11, program.getRequirements());
	    pstmt.setString(12, program.getDays());
	    if (program.getStaffId() != null) {
	        pstmt.setLong(13, program.getStaffId());
	    } else {
	        pstmt.setNull(13, java.sql.Types.BIGINT);
	    }

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
	    String sql = "UPDATE Program SET "
	               + "program_name = ?, description = ?, capacity = ?, current_capacity = ?, "
	               + "start_date = ?, end_date = ?, start_time = ?, end_time = ?, "
	               + "location = ?, price = ?, requirements = ?, days = ?, staff_id = ? "
	               + "WHERE program_id = ?";

	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, program.getProgramName());
	    pstmt.setString(2, program.getDescription());
	    pstmt.setInt(3, program.getCapacity());
	    pstmt.setInt(4, program.getCurrentCapacity());
	    pstmt.setDate(5, java.sql.Date.valueOf(program.getStartDate()));
	    pstmt.setDate(6, java.sql.Date.valueOf(program.getEndDate()));
	    pstmt.setTime(7, java.sql.Time.valueOf(program.getStartTime()));
	    pstmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));
	    pstmt.setString(9, program.getLocation());
	    pstmt.setDouble(10, program.getPrice());
	    pstmt.setInt(11, program.getRequirements());
	    pstmt.setString(12, program.getDays());
	    if (program.getStaffId() != null) {
	        pstmt.setLong(13, program.getStaffId());
	    } else {
	        pstmt.setNull(13, java.sql.Types.BIGINT);
	    }
	    pstmt.setLong(14, program.getProgramId());

	    pstmt.executeUpdate();
	    pstmt.close();
	}


	public void addRegistration(Registration registration) throws SQLException {
	    String sql = "INSERT INTO Registration(user_id, participant_id, program_id, registration_date) VALUES (?, ?, ?, ?)";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, registration.getUserId());        // who is registering
	    pstmt.setLong(2, registration.getParticipantId()); // who is participating
	    pstmt.setLong(3, registration.getProgramId());
	    pstmt.setDate(4, java.sql.Date.valueOf(registration.getRegistrationDate()));
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

	public Long getUserIdByUsername(String username) throws SQLException {
	    String sql = "SELECT user_id FROM User WHERE username = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, username);
	    ResultSet rs = pstmt.executeQuery();
	    Long userId = null;
	    if (rs.next()) {
	        userId = rs.getLong("user_id");
	    }
	    rs.close();
	    pstmt.close();
	    return userId;
	}

	public String getProgramsForParticipant(long participantId) {
	    return "SELECT P.* " +
	           "FROM Program P " +
	           "INNER JOIN Registration R ON P.program_id = R.program_id " +
	           "WHERE R.participant_id = " + participantId;
	}

	public void addFamilyMember(Long userId, Long familyMemberId, String relationship) throws SQLException {
	    String sql = "INSERT INTO FamilyMember (user_id, family_member_id, relationship) VALUES (?, ?, ?)";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.setLong(2, familyMemberId);
	    pstmt.setString(3, relationship);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	public ResultSet getFamilyMembersForUser(Long userId) throws SQLException {
	    String sql = "SELECT * FROM FamilyMember WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    return pstmt.executeQuery();
	}

	public void deleteFamilyMember(Long userId, Long familyMemberId) throws SQLException {
	    String sql = "DELETE FROM FamilyMember WHERE user_id = ? AND family_member_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.setLong(2, familyMemberId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	public Program getProgramByID(long id) throws SQLException {
	    ResultSet rs;
	    Program p = new Program();
	    String sql = "SELECT * FROM Program WHERE program_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, id);
	    rs = pstmt.executeQuery();

	    if (rs.next()) {
	        p = new Program();
	        p.setProgramId(rs.getLong("program_id")); // <<< You missed setting the programId
	        p.setProgramName(rs.getString("program_name"));
	        p.setDescription(rs.getString("description"));
	        p.setCapacity(rs.getInt("capacity"));
	        p.setCurrentCapacity(rs.getInt("current_capacity"));
	        p.setStartDate(rs.getDate("start_date").toLocalDate());
	        p.setEndDate(rs.getDate("end_date").toLocalDate());
	        p.setStartTime(rs.getTime("start_time").toLocalTime());
	        p.setEndTime(rs.getTime("end_time").toLocalTime());
	        p.setLocation(rs.getString("location"));
	        p.setRequirements(rs.getInt("requirements"));
	        p.setDays(rs.getString("days"));
	        p.setPrice(rs.getDouble("price"));
	        long staffId = rs.getLong("staff_id");
	        if (!rs.wasNull()) {
	            p.setStaffId(staffId);
	        } else {
	            p.setStaffId(null);
	        }
	    }

	    rs.close();
	    pstmt.close();
	    return p;
	}

	public boolean isUserRegisteredForProgram(long userId, long programId) throws SQLException {
	    String sql = "SELECT * FROM Registration WHERE participant_id = ? AND program_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.setLong(2, programId);
	    ResultSet rs = pstmt.executeQuery();
	    boolean registered = rs.next();
	    rs.close();
	    pstmt.close();
	    return registered;
	}

	public boolean isStaffScheduleConflict(Program newProgram) throws SQLException {
	    String query = "SELECT * FROM program WHERE staff_id = ? AND (start_date <= ? AND end_date >= ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setLong(1, newProgram.getStaffId());
	        stmt.setDate(2, java.sql.Date.valueOf(newProgram.getStartDate()));
	        stmt.setDate(3, java.sql.Date.valueOf(newProgram.getEndDate()));
	        ResultSet rs = stmt.executeQuery();
	        return rs.next();  // If there is a conflicting schedule, return true
	    }
	}

	public boolean isLocationScheduleConflict(Program newProgram) throws SQLException {
	    String query = "SELECT * FROM program WHERE location = ? AND (start_date <= ? AND end_date >= ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, newProgram.getLocation());
	        stmt.setDate(2, java.sql.Date.valueOf(newProgram.getStartDate()));
	        stmt.setDate(3, java.sql.Date.valueOf(newProgram.getEndDate()));
	        ResultSet rs = stmt.executeQuery();
	        return rs.next();  // If there is a conflicting schedule, return true
	    }
	}

	public void sendMessageToInbox(Long userId, String message) throws SQLException {
	    String query = "INSERT INTO Inbox (user_id, message, date_sent) VALUES (?, ?, ?)";
	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setLong(1, userId);
	    stmt.setString(2, message);
	    stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now())); // <-- This sets the date/time!
	    stmt.executeUpdate();
	}




}
