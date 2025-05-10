package model;

/**
 * The Dependent class represents a child or family member
 * associated with a primary User account in the YMCA system.
 */
public class Dependent {
    // Unique identifier for the dependent
    private Long dependentId;

    // ID of the parent User who owns this dependent
    private Long userId;

    // Name of the dependent
    private String name;

    // Relationship of the dependent to the parent user (e.g., child, spouse)
    private String relationship;

    // Birthdate of the dependent in string format (e.g., "2009-06-12")
    private String birthdate;

    // Default constructor
    public Dependent() {}

    // Full constructor with all fields
    public Dependent(Long dependentId, Long userId, String name, String relationship, String birthdate) {
        this.dependentId = dependentId;
        this.userId = userId;
        this.name = name;
        this.relationship = relationship;
        this.birthdate = birthdate;
    }

    // Getters and Setters

    public Long getDependentId() {
        return dependentId;
    }

    public void setDependentId(Long dependentId) {
        this.dependentId = dependentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
