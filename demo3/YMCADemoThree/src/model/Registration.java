package model;

import java.time.LocalDate;

/**
 * Represents a registration record for a user or dependent in a YMCA program.
 */
public class Registration {
    private Long registrationId;             // Unique identifier for the registration
    private Long programId;                  // ID of the registered program
    private Long registeredByUserId;         // ID of the user who performed the registration
    private Long participantUserId;          // ID of the registered user (nullable)
    private Long participantDependentId;     // ID of the registered dependent (nullable)
    private LocalDate registrationDate;      // Date the registration occurred

    // Default constructor
    public Registration() {}

    // Full constructor
    public Registration(Long registrationId, Long programId, Long registeredByUserId,
                        Long participantUserId, Long participantDependentId, LocalDate registrationDate) {
        this.registrationId = registrationId;
        this.programId = programId;
        this.registeredByUserId = registeredByUserId;
        this.participantUserId = participantUserId;
        this.participantDependentId = participantDependentId;
        this.registrationDate = registrationDate;
    }

    // Getter and setter for registrationId
    public Long getRegistrationId() {
        return registrationId;
    }
    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    // Getter and setter for programId
    public Long getProgramId() {
        return programId;
    }
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    // Getter and setter for registeredByUserId
    public Long getRegisteredByUserId() {
        return registeredByUserId;
    }
    public void setRegisteredByUserId(Long registeredByUserId) {
        this.registeredByUserId = registeredByUserId;
    }

    // Getter and setter for participantUserId
    public Long getParticipantUserId() {
        return participantUserId;
    }
    public void setParticipantUserId(Long participantUserId) {
        this.participantUserId = participantUserId;
    }

    // Getter and setter for participantDependentId
    public Long getParticipantDependentId() {
        return participantDependentId;
    }
    public void setParticipantDependentId(Long participantDependentId) {
        this.participantDependentId = participantDependentId;
    }

    // Getter and setter for registrationDate
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Returns a string representation of the registration
    @Override
    public String toString() {
        return "Registration{" +
               "registrationId=" + registrationId +
               ", programId=" + programId +
               ", registeredByUserId=" + registeredByUserId +
               ", participantUserId=" + participantUserId +
               ", participantDependentId=" + participantDependentId +
               ", registrationDate=" + registrationDate +
               '}';
    }
}
