package model;

public class Dependent {
    private Long dependentId;
    private Long userId;  // Parent account
    private String name;
    private String relationship;
    private String birthdate;

    public Dependent() {
    }

    public Dependent(Long dependentId, Long userId, String name, String relationship, String birthdate) {
        this.dependentId = dependentId;
        this.userId = userId;
        this.name = name;
        this.relationship = relationship;
        this.birthdate = birthdate;
    }

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

