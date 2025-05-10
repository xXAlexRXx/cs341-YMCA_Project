package model;
import java.time.LocalDate;

public class Registration {
	 private Long registrationId;
	    private Long userId;
	    private Long programId;
	    private LocalDate registrationDate;
	    private double price;

	    // Default constructor
	    public Registration() {
	    }

	    // Full constructor
	    public Registration(Long registrationId, Long userId, Long programId, LocalDate registrationDate) {
	        this.registrationId = registrationId;
	        this.userId = userId;
	        this.programId = programId;
	        this.registrationDate = registrationDate;
	    }

	    // Getters and setters
	    public double getPrice() {
	    	return price;
	    }

	    public void setPrice(double price) {
	    	this.price = price;
	    }

	    public Long getRegistrationId() {
	        return registrationId;
	    }

	    public void setRegistrationId(Long registrationId) {
	        this.registrationId = registrationId;
	    }

	    public Long getUserId() {
	        return userId;
	    }

	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }

	    public Long getProgramId() {
	        return programId;
	    }

	    public void setProgramId(Long programId) {
	        this.programId = programId;
	    }

	    public LocalDate getRegistrationDate() {
	        return registrationDate;
	    }

	    public void setRegistrationDate(LocalDate registrationDate) {
	        this.registrationDate = registrationDate;
	    }

	    @Override
	    public String toString() {
	        return "Registration{" +
	               "registrationId=" + registrationId +
	               ", userId=" + userId +
	               ", programId=" + programId +
	               ", registrationDate=" + registrationDate +
	               '}';
	    }
}
