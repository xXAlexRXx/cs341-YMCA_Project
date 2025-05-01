package model;

public class FamilyMember {
    private long userId;
    private long familyMemberId;
    private String relationship;

    public FamilyMember(long userId, long familyMemberId, String relationship) {
        this.userId = userId;
        this.familyMemberId = familyMemberId;
        this.relationship = relationship;
    }

    // Getters
    public long getUserId() {
        return userId;
    }

    public long getFamilyMemberId() {
        return familyMemberId;
    }

    public String getRelationship() {
        return relationship;
    }

    // Setters
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setFamilyMemberId(long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
