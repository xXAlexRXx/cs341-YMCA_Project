package model;

import java.time.LocalDate;

public class Registration {
    private Long registrationId;
    private Long programId;
    private Long registeredByUserId;
    private Long participantUserId;       // nullable
    private Long participantDependentId;  // nullable
    private LocalDate registrationDate;

    public Registration() {}

    public Registration(Long registrationId, Long programId, Long registeredByUserId,
                        Long participantUserId, Long participantDependentId, LocalDate registrationDate) {
        this.registrationId = registrationId;
        this.programId = programId;
        this.registeredByUserId = registeredByUserId;
        this.participantUserId = participantUserId;
        this.participantDependentId = participantDependentId;
        this.registrationDate = registrationDate;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getRegisteredByUserId() {
        return registeredByUserId;
    }

    public void setRegisteredByUserId(Long registeredByUserId) {
        this.registeredByUserId = registeredByUserId;
    }

    public Long getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(Long participantUserId) {
        this.participantUserId = participantUserId;
    }

    public Long getParticipantDependentId() {
        return participantDependentId;
    }

    public void setParticipantDependentId(Long participantDependentId) {
        this.participantDependentId = participantDependentId;
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
               ", programId=" + programId +
               ", registeredByUserId=" + registeredByUserId +
               ", participantUserId=" + participantUserId +
               ", participantDependentId=" + participantDependentId +
               ", registrationDate=" + registrationDate +
               '}';
    }
}

