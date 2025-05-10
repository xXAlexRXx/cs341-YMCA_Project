package model;

/**
 * The Staff class represents a staff member in the YMCA system.
 * It contains the staff member's unique ID and name.
 */
public class Staff {

    // Unique identifier for the staff member
    private Long staffID;

    // Name of the staff member
    private String staffName;

    // Constructor to initialize both staff ID and name
    public Staff(Long staffID, String staffName) {
        super();
        this.staffID = staffID;
        this.staffName = staffName;
    }

    // Getter for staff ID
    public Long getStaffID() {
        return staffID;
    }

    // Setter for staff ID
    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    // Getter for staff name
    public String getStaffName() {
        return staffName;
    }

    // Setter for staff name
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
