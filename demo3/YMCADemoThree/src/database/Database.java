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

/**
 * This class handles all database interactions for the YMCA application.
 * It supports connecting to the database, running queries and updates,
 * and managing CRUD operations for users, programs, registrations,
 * dependents, family members, and inbox messages.
 */
public class Database {

    // JDBC URL for connecting to the MySQL database
    String url = "jdbc:mysql://173.24.68.203:3306/ymcafinal?user=admin&password=";
    private Connection connection;

    // Constructor that appends the password to the connection URL
    public Database() {
        String password = "CS341Group!"; // Change this value to match your actual password
        url = url + password;
    }

    // Establishes a connection to the database
    public void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Cannot connect!");
            System.out.println(e);
        }
    }

    // Closes the database connection
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Cannot disconnect!");
        }
    }

    // Returns the current database connection
    public Connection getConnection() {
        return connection;
    }

    // Runs a query and returns a ResultSet
    public ResultSet runQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    // Executes an update and returns number of rows affected
    public int runUpdate(String query) throws SQLException {
        if (connection == null) {
            throw new SQLException("No database connection established.");
        }

        Statement stmt = null;
        int rowsAffected = 0;
        try {
            stmt = connection.createStatement();
            rowsAffected = stmt.executeUpdate(query);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return rowsAffected;
    }

    // --- Query Generators ---
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
        return "SELECT P.* FROM Program P INNER JOIN Registration R ON P.program_id = R.program_id WHERE R.participant_user_id = " + userId;
    }

    public String getUsersForProgram(long programId) {
        return "SELECT U.* FROM User U INNER JOIN Registration R ON U.user_id = R.participant_user_id WHERE R.program_id = " + programId;
    }

    // Adds a new user to the database
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO User(username, password, email, user_type, balance, status) VALUES(?, ?, ?, ?,?,?)";
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

    // Checks for existing username or email
    public ResultSet getUsernameAndEmail(String username, String email) throws SQLException {
        String sql = "SELECT username, email FROM User WHERE username = ? OR email = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, email);
        return pstmt.executeQuery();
    }

    // Deletes a user by ID
    public void deleteUser(Long userId) throws SQLException {
        String sql = "DELETE FROM User WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, userId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Updates a user's information
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

    // Adds a new program
    public void addProgram(Program program) throws SQLException {
        String sql = "INSERT INTO Program (program_name, description, capacity, current_capacity, start_date, end_date, start_time, end_time, location, price, requirements, days, staff_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

	// Deletes a Program
	public void deleteProgram(Long programId) throws SQLException {
	    String sql = "DELETE FROM Program WHERE program_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, programId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	// Updates a program
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

	// Returns a program name given a program ID
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

	// Adds a registration to the database
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


	// Deletes a registration from the database
	public void deleteRegistration(Long registrationId) throws SQLException {
	    String sql = "DELETE FROM Registration WHERE registration_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, registrationId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	// Returns a User based on a username and password
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

	// Returns a user ID for a given username
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

	// Returns all programs for a given userID
	public ResultSet getProgramsForParticipant(long userId) throws SQLException {
	    String sql =
	      "SELECT p.program_name, p.start_date, p.days " +
	      "FROM Program p " +
	      "JOIN Registration r ON p.program_id = r.program_id " +
	      "WHERE r.participant_user_id = ? OR r.participant_dependent_id = ?";

	    PreparedStatement ps = connection.prepareStatement(sql);
	    ps.setLong(1, userId);
	    ps.setLong(2, userId); // handle both user and dependent registrations
	    return ps.executeQuery();
	}

	// Adds a family member
	public void addFamilyMember(Long userId, Long familyMemberId, String relationship) throws SQLException {
	    String sql = "INSERT INTO FamilyMember (user_id, family_member_id, relationship) VALUES (?, ?, ?)";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.setLong(2, familyMemberId);
	    pstmt.setString(3, relationship);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	// Returns all family members for a given userID
	public ResultSet getFamilyMembersForUser(Long userId) throws SQLException {
	    String sql = "SELECT * FROM FamilyMember WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    return pstmt.executeQuery();
	}


	// Deletes a family member
	public void deleteFamilyMember(Long userId, Long familyMemberId) throws SQLException {
	    String sql = "DELETE FROM FamilyMember WHERE user_id = ? AND family_member_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId);
	    pstmt.setLong(2, familyMemberId);
	    pstmt.executeUpdate();
	    pstmt.close();
	}

	// Returns a program for a given programID
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

	// Checks if a user is register for a given program
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


	// Checks for time conflicts
	private boolean checkTimeMatch(ResultSet rs, LocalTime programStartTime, LocalTime programEndTime) throws SQLException {
	    // Check if the time matches between the registered program and the new program
	    String registeredStartTimeStr = rs.getString("start_time");
	    String registeredEndTimeStr = rs.getString("end_time");

	    // Convert the registered times to LocalTime for comparison
	    LocalTime registeredStartTime = LocalTime.parse(registeredStartTimeStr);
	    LocalTime registeredEndTime = LocalTime.parse(registeredEndTimeStr);

	    return programStartTime.equals(registeredStartTime) && programEndTime.equals(registeredEndTime);
	}

	// Checks for day conflicts
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

	/**
	 * Checks if the given program start date matches the start date of a record in the ResultSet.
	 *
	 */
	private boolean checkDateMatch(ResultSet rs, LocalDate programStartDate) throws SQLException {
	    // Extract the start date from the ResultSet and convert it to LocalDate
	    LocalDate registeredStartDate = rs.getDate("start_date").toLocalDate();

	    // Return true if the start dates match exactly
	    return programStartDate.equals(registeredStartDate);
	}

	/**
	 * Checks whether a staff member has a schedule conflict when adding a new program.
	 * A conflict exists if another program is assigned to the same staff member
	 * with the same date, time, and overlapping days.
	 *
	 */
	@SuppressWarnings("unused")
	public boolean isStaffScheduleConflict(Program newProgram) throws SQLException {
	    String query = "SELECT * FROM Program WHERE staff_id = ? AND (start_date <= ? AND end_date >= ?)";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        // Set parameters for checking overlapping date range
	        stmt.setLong(1, newProgram.getStaffId());
	        stmt.setDate(2, java.sql.Date.valueOf(newProgram.getStartDate()));
	        stmt.setDate(3, java.sql.Date.valueOf(newProgram.getEndDate()));

	        ResultSet rs = stmt.executeQuery();

	        // Iterate through existing programs assigned to the same staff member
	        while (rs.next()) {
	            // Extract existing program times and days
				LocalTime existingStartTime = LocalTime.parse(rs.getString("start_time"));
				LocalTime existingEndTime = LocalTime.parse(rs.getString("end_time"));
	            String[] existingDays = rs.getString("days").split(",");
	            LocalDate existingStartDate = rs.getDate("start_date").toLocalDate();

	            // Check time, day, and date conflicts
	            boolean isTimeMatch = checkTimeMatch(rs, newProgram.getStartTime(), newProgram.getEndTime());
	            boolean isDayMatch = checkDayMatch(rs, newProgram.getDays());
	            boolean isDateMatch = checkDateMatch(rs, newProgram.getStartDate());

	            if (isTimeMatch && isDayMatch && isDateMatch) {
	                return true; // Conflict found
	            }
	        }
	    }

	    return false; // No conflicts found
	}

	/**
	 * Checks whether a program location is already booked for a conflicting time slot.
	 * It considers overlapping date ranges and overlapping time windows.
	 *
	 */
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

	    // Set date range overlap parameters
	    stmt.setDate(2, java.sql.Date.valueOf(program.getEndDate()));   // existing.start <= new.end
	    stmt.setDate(3, java.sql.Date.valueOf(program.getEndDate()));   // existing.end >= new.end
	    stmt.setDate(4, java.sql.Date.valueOf(program.getStartDate())); // existing.start <= new.start
	    stmt.setDate(5, java.sql.Date.valueOf(program.getStartDate())); // existing.end >= new.start
	    stmt.setDate(6, java.sql.Date.valueOf(program.getStartDate())); // existing.start >= new.start
	    stmt.setDate(7, java.sql.Date.valueOf(program.getEndDate()));   // existing.end <= new.end

	    // Set time overlap parameters
	    stmt.setTime(8, java.sql.Time.valueOf(program.getEndTime()));   // existing.start < new.end
	    stmt.setTime(9, java.sql.Time.valueOf(program.getStartTime())); // existing.end > new.start
	    stmt.setTime(10, java.sql.Time.valueOf(program.getStartTime())); // existing.start < new.start
	    stmt.setTime(11, java.sql.Time.valueOf(program.getEndTime()));   // existing.end > new.end
	    stmt.setTime(12, java.sql.Time.valueOf(program.getStartTime())); // existing.start >= new.start
	    stmt.setTime(13, java.sql.Time.valueOf(program.getEndTime()));   // existing.start < new.end

	    ResultSet rs = stmt.executeQuery();

	    boolean conflict = rs.next(); // True if any overlapping program exists

	    rs.close();
	    stmt.close();

	    return conflict;
	}



	// Inserts a message into a user's inbox with a timestamp
	public void sendMessageToInbox(Long userId, String message) throws SQLException {
	    String query = "INSERT INTO Inbox (user_id, message, date_sent) VALUES (?, ?, ?)";
	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setLong(1, userId); // recipient's user ID
	    stmt.setString(2, message); // message content
	    stmt.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now())); // current timestamp
	    stmt.executeUpdate();
	    stmt.close();
	}

	// Returns a query string to retrieve all programs assigned to a specific staff member
	public String getProgramsForStaff(long staffId) {
	    return "SELECT * FROM Program WHERE staff_id = " + staffId;
	}

	// Returns a query string to search participants in a specific program by username
	public String searchParticipantsForProgram(long programId, String searchTerm) {
	    return "SELECT u.username " +
	           "FROM Users u " +
	           "JOIN Registration r ON u.user_id = r.user_id " +
	           "WHERE r.program_id = ? AND u.username LIKE ?";
	}

	// Returns a query string to retrieve all programs a specific dependent is registered in
	public String getProgramsForDependent(long dependentId) {
	    return "SELECT P.* " +
	           "FROM Program P " +
	           "INNER JOIN Registration R ON P.program_id = R.program_id " +
	           "WHERE R.participant_dependent_id = " + dependentId;
	}

	// Queries and returns all dependents associated with a given user
	public ResultSet getDependentsForUser(long userId) throws SQLException {
	    String sql = "SELECT * FROM Dependent WHERE user_id = ?";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setLong(1, userId); // parent user's ID
	    return pstmt.executeQuery();
	}


	// Retrieves all participants (both Users and Dependents) registered for a given program.
	// Combines two SELECT queries using UNION to include both types.
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
	    pstmt.setLong(1, programId); // for User part
	    pstmt.setLong(2, programId); // for Dependent part
	    return pstmt.executeQuery();
	}

	// Retrieves all users who currently have 'active' status
	public ResultSet getActiveUsers() throws SQLException {
	    String sql = "SELECT * FROM User WHERE status = 'active'";
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    return pstmt.executeQuery();
	}

	// Increments the current_capacity of a program by 1
	public void incrementCurrentCapacity(long programId) throws SQLException {
	    String sql = "UPDATE Program SET current_capacity = current_capacity + 1 WHERE program_id = ?";
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setLong(1, programId);
	    stmt.executeUpdate();
	    stmt.close();
	}

	// Decrements the current_capacity of a program by 1 (only if it's above 0)
	public void decrementCurrentCapacity(long programId) throws SQLException {
	    String sql = "UPDATE Program SET current_capacity = current_capacity - 1 WHERE program_id = ? AND current_capacity > 0";
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setLong(1, programId);
	    stmt.executeUpdate();
	    stmt.close();
	}



}
