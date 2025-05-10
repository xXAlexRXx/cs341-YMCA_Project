package model;

/**
 * The FamilyMember class represents a relationship between two users in the system:
 * a primary user and a family member (another registered user).
 * This allows multiple users to be linked under a family plan.
 */
public class FamilyMember {
    // ID of the user who is adding a family member
    private long userId;

    // ID of the family member being added (also a registered user)
    private long familyMemberId;

    // Description of the relationship (e.g., "spouse", "child", "sibling")
    private String relationship;

    // Constructor to initialize all fields
    public FamilyMember(long userId, long familyMemberId, String relationship) {
        this.userId = userId;
        this.familyMemberId = familyMemberId;
        this.relationship = relationship;
    }

    // Getter for userId
    public long getUserId() {
        return userId;
    }

    // Getter for familyMemberId
    public long getFamilyMemberId() {
        return familyMemberId;
    }

    // Getter for relationship
    public String getRelationship() {
        return relationship;
    }

    // Setter for userId
    public void setUserId(long userId) {
        this.userId = userId;
    }

    // Setter for familyMemberId
    public void setFamilyMemberId(long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    // Setter for relationship
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
