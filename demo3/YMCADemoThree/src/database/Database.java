package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

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

	String url = "jdbc:mysql://173.24.68.203:3306/ymcafinal?user=admin&password=";


	
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
		return "SELECT * FROM User";
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
	           "WHERE R.participant_user_id = " + userId;
	}

	
	public String getUsersForProgram(long programId) {
	    return "SELECT U.* " +
	           "FROM User U " +
	           "INNER JOIN Registration R ON U.user_id = R.participant_user_id " +
	           "WHERE R.program_id = " + programId;
	}

	public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO User(username, password, email, user_type,balance. status) VALUES(?, ?, ?, ?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getUserType());
        pstmt.setDouble(5, user.getUserBalance());
        pstmt.setString(6, user.getStatus());
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
	    String sql = "UPDATE User SET username = ?, password = ?, email = ?, user_type = ?, balance = ?, status = ? WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setString(1, user.getUsername());
	    pstmt.setString(2, user.getPassword());
	    pstmt.setString(3, user.getEmail());
	    pstmt.setString(4, user.getUserType());
	    pstmt.setDouble(5, user.getUserBalance());
	    pstmt.setString(6, user.getStatus());
	    pstmt.setLong(7, user.getUserId());
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
	
	public String getProgramNameById(long programId) throws SQLException {
	    String sql = "SELECT program_name FROM Program WHERE program_id = ?";
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setLong(1, programId);
	    ResultSet rs = stmt.executeQuery();
	    String name = null;
	    if (rs.next()) {
	        name = rs.getString("program_name");
	    }
	    rs.close();
	    stmt.close();
	    return name;
	}

	
	public void addRegistration(Registration registration) throws SQLException {
	    String sql = "INSERT INTO Registration(user_id, participant_user_id, participant_dependent_id, program_id, registration_date) " +
	                 "VALUES (?, ?, ?, ?, ?)";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, registration.getRegisteredByUserId());
	    
	    if (registration.getParticipantUserId() != null) {
	        pstmt.setLong(2, registration.getParticipantUserId());
	    } else {
	        pstmt.setNull(2, java.sql.Types.BIGINT);
	    }

	    if (registration.getParticipantDependentId() != null) {
	        pstmt.setLong(3, registration.getParticipantDependentId());
	    } else {
	        pstmt.setNull(3, java.sql.Types.BIGINT);
	    }

	    pstmt.setLong(4, registration.getProgramId());
	    pstmt.setDate(5, java.sql.Date.valueOf(registration.getRegistrationDate()));
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
	        user.setStatus(rs.getString("status"));
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
	
	public ResultSet getProgramsForParticipant(long userId) throws SQLException {
	    String sql = 
	      "SELECT p.program_id, p.program_name\n" +
	      "  FROM Program p\n" +
	      "  JOIN Registration r ON p.program_id = r.program_id\n" +
	      " WHERE r.participant_user_id = ?";

	    PreparedStatement ps = connection.prepareStatement(sql);
	    ps.setLong(1, userId);
	    return ps.executeQuery();
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
	
	public boolean isUserRegisteredForProgram(long participantId, long programId) throws SQLException {
	    String sql = """
	        SELECT * FROM Registration 
	        WHERE program_id = ? 
	          AND (participant_user_id = ? OR participant_dependent_id = ?)
	    """;
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, programId);
	    pstmt.setLong(2, participantId);
	    pstmt.setLong(3, participantId);
	    ResultSet rs = pstmt.executeQuery();

	    // If a registration exists, return true — no need to check schedule here
	    boolean registered = rs.next();

	    rs.close();
	    pstmt.close();
	    return registered;
	}

	private boolean checkTimeMatch(ResultSet rs, LocalTime programStartTime, LocalTime programEndTime) throws SQLException {
	    // Check if the time matches between the registered program and the new program
	    String registeredStartTimeStr = rs.getString("start_time");
	    String registeredEndTimeStr = rs.getString("end_time");
	    
	    // Convert the registered times to LocalTime for comparison
	    LocalTime registeredStartTime = LocalTime.parse(registeredStartTimeStr);
	    LocalTime registeredEndTime = LocalTime.parse(registeredEndTimeStr);
	    
	    return programStartTime.equals(registeredStartTime) && programEndTime.equals(registeredEndTime);
	}

	private boolean checkDayMatch(ResultSet rs, String programDays) throws SQLException {
	    // Check if the days match (assuming it's stored as a comma-separated string)
	    String registeredDays = rs.getString("days");  // Get the stored days
	    String[] registeredDaysArray = registeredDays.split(",");
	    
	    for (String programDay : programDays.split(",")) {
	        boolean dayMatch = false;
	        for (String registeredDay : registeredDaysArray) {
	            if (programDay.trim().equalsIgnoreCase(registeredDay.trim())) {
	                dayMatch = true;
	                break;
	            }
	        }
	        if (!dayMatch) {
	            return false; // If any day doesn't match, return false
	        }
	    }
	    return true; // All days matched
	}

	private boolean checkDateMatch(ResultSet rs, LocalDate programStartDate) throws SQLException {
	    // Check if the start date matches
	    LocalDate registeredStartDate = rs.getDate("start_date").toLocalDate();  // Convert to LocalDate
	    return programStartDate.equals(registeredStartDate);
	}

	public boolean isStaffScheduleConflict(Program newProgram) throws SQLException {
	    String query = "SELECT * FROM program WHERE staff_id = ? AND (start_date <= ? AND end_date >= ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setLong(1, newProgram.getStaffId());
	        stmt.setDate(2, java.sql.Date.valueOf(newProgram.getStartDate()));  // Convert LocalDate to java.sql.Date
	        stmt.setDate(3, java.sql.Date.valueOf(newProgram.getEndDate()));    // Convert LocalDate to java.sql.Date
	        ResultSet rs = stmt.executeQuery();
	        
	        // Check if there is any conflicting program for the staff during the same time
	        while (rs.next()) {
	            String existingStartTimeStr = rs.getString("start_time");
	            String existingEndTimeStr = rs.getString("end_time");
	            rs.getString("days").split(",");
	            rs.getDate("start_date").toLocalDate();
	            
	            LocalTime.parse(existingStartTimeStr);
	            LocalTime.parse(existingEndTimeStr);
	            
	            // Compare the days, times, and date of the existing program with the new one
	            boolean isTimeMatch = checkTimeMatch(rs, newProgram.getStartTime(), newProgram.getEndTime());
	            boolean isDayMatch = checkDayMatch(rs, newProgram.getDays());
	            boolean isDateMatch = checkDateMatch(rs, newProgram.getStartDate());
	            
	            if (isTimeMatch && isDayMatch && isDateMatch) {
	                return true;  // Conflicting schedule found
	            }
	        }
	    }
	    return false; // No conflict found
	}
	
	public boolean isLocationScheduleConflict(Program program) throws SQLException {
	    String sql = """
	        SELECT * FROM Program
	        WHERE location = ?
	          AND (
	              (start_date <= ? AND end_date >= ?) OR
	              (start_date <= ? AND end_date >= ?) OR
	              (start_date >= ? AND end_date <= ?)
	          )
	          AND (
	              (start_time < ? AND end_time > ?) OR
	              (start_time < ? AND end_time > ?) OR
	              (start_time >= ? AND start_time < ?)
	          )
	    """;

	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setString(1, program.getLocation());

	    // Date overlap checks
	    stmt.setDate(2, java.sql.Date.valueOf(program.getEndDate()));   // existing.start <= new.end
	    stmt.setDate(3, java.sql.Date.valueOf(program.getEndDate()));   // existing.end >= new.end
	    stmt.setDate(4, java.sql.Date.valueOf(program.getStartDate())); // existing.start <= new.start
	    stmt.setDate(5, java.sql.Date.valueOf(program.getStartDate())); // existing.end >= new.start
	    stmt.setDate(6, java.sql.Date.valueOf(program.getStartDate())); // existing.start >= new.start
	    stmt.setDate(7, java.sql.Date.valueOf(program.getEndDate()));   // existing.end <= new.end

	    // Time overlap checks
	    stmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));   // existing.start < new.end
	    stmt.setTime(9, java.sql.Time.valueOf(program.getStartTime())); // existing.end > new.start
	    stmt.setTime(10, java.sql.Time.valueOf(program.getStartTime())); // existing.start < new.start
	    stmt.setTime(11, java.sql.Time.valueOf(program.getEndTime()));   // existing.end > new.end
	    stmt.setTime(12, java.sql.Time.valueOf(program.getStartTime())); // existing.start >= new.start
	    stmt.setTime(13, java.sql.Time.valueOf(program.getEndTime()));   // existing.start < new.end

	    ResultSet rs = stmt.executeQuery();
	    boolean conflict = rs.next(); // if we found any conflicting record

	    rs.close();
	    stmt.close();

	    return conflict;
	}

	
	public void sendMessageToInbox(Long userId, String message) throws SQLException {
	    String query = "INSERT INTO Inbox (user_id, message, date_sent) VALUES (?, ?, ?)";
	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setLong(1, userId);
	    stmt.setString(2, message);
	    stmt.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now())); // <-- ADD THIS LINE
	    stmt.executeUpdate();
	    stmt.close();
	}
	
	public String getProgramsForStaff(long staffId) {
        return "SELECT * FROM Program WHERE staff_id = " + staffId;
    }
	
	public String searchParticipantsForProgram(long programId, String searchTerm) {
	    return "SELECT u.username " +
	           "FROM Users u " +
	           "JOIN Registration r ON u.user_id = r.user_id " +
	           "WHERE r.program_id = ? AND u.username LIKE ?";
	}
	
	public String getProgramsForDependent(long dependentId) {
	    return "SELECT P.* " +
	           "FROM Program P " +
	           "INNER JOIN Registration R ON P.program_id = R.program_id " +
	           "WHERE R.participant_dependent_id = " + dependentId;
	}
	
	public ResultSet getDependentsForUser(long userId) throws SQLException {
	    String sql = "SELECT * FROM Dependent WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    return pstmt.executeQuery();
	}
	
	public ResultSet getAllParticipantsForProgram(long programId) throws SQLException {
	    String sql = 
	        "SELECT U.username AS participant_name, 'User' AS participant_type " +
	        "FROM Registration R " +
	        "JOIN User U ON R.participant_user_id = U.user_id " +
	        "WHERE R.program_id = ? " +
	        "UNION " +
	        "SELECT D.name AS participant_name, 'Dependent' AS participant_type " +
	        "FROM Registration R " +
	        "JOIN Dependent D ON R.participant_dependent_id = D.dependent_id " +
	        "WHERE R.program_id = ?";

	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, programId);
	    pstmt.setLong(2, programId);
	    return pstmt.executeQuery();
	}

	public ResultSet getActiveUsers() throws SQLException {
	    String sql = "SELECT * FROM User WHERE status = 'active'";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    return pstmt.executeQuery();
	}
	
	public void incrementCurrentCapacity(long programId) throws SQLException {
	    String sql = "UPDATE Program SET current_capacity = current_capacity + 1 WHERE program_id = ?";
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setLong(1, programId);
	    stmt.executeUpdate();
	    stmt.close();
	}
	
	public void decrementCurrentCapacity(long programId) throws SQLException {
	    String sql = "UPDATE Program SET current_capacity = current_capacity - 1 WHERE program_id = ? AND current_capacity > 0";
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setLong(1, programId);
	    stmt.executeUpdate();
	    stmt.close();
	}


}
