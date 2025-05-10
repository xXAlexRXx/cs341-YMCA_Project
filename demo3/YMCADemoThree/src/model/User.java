package model;

/**
 * Represents a registered user in the YMCA system.
 * This class contains login credentials, role information, account status, and balance.
 */
public class User {

    // Unique identifier for the user
    private Long userId;

    // User's chosen username
    private String username;

    // Hashed password of the user
    private String password;

    // User's email address
    private String email;

    // User type: Admin, Staff, Member, or User
    private String userType;

    // Account balance for the user
    private double balance;

    // Status of the account (e.g., active, suspended)
    private String status;

    // Default constructor
    public User() {}

    // Full constructor with all fields
    public User(Long userId, String username, String password, String email, String userType, double balance, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.balance = balance;
        this.status = status;
    }

    // Getter and setter for userId
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for user type
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Getter and setter for account balance
    public double getUserBalance() {
        return balance;
    }
    public void setUserBalance(double balance) {
        this.balance = balance;
    }

    // Reset the balance to 0
    public void resetBalance() {
        this.balance = 0f;
    }

    // Getter and setter for account status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // String representation for debugging and logging
    @Override
    public String toString() {
        return "User{" +
               "userId=" + userId +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", userType='" + userType + '\'' +
               '}';
    }
}
