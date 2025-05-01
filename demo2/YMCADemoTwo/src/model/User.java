package model;

public class User {
	private Long userId;
    private String username;
    private String password;
    private String email;
    private String userType;
    private double balance;

    // Default constructor
    public User() {
    }

    // Full constructor
    public User(Long userId, String username, String password, String email, String userType, double balance) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.balance = balance;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getUserBalance() {
    	return balance;
    }

    public void setUserBalance(double balance) {
    	this.balance = balance;
    }

    public void resetBalance() {
    	this.balance = 0f;
    }
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
