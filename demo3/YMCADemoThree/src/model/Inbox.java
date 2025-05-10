package model;

import java.time.LocalDateTime;

/**
 * The Inbox class represents a single message sent to a user.
 * Each entry corresponds to a message stored in the user's inbox table.
 */
public class Inbox {
    private Long messageId;           // Unique ID of the message
    private Long userId;             // ID of the user receiving the message
    private String message;          // Message content
    private LocalDateTime dateSent;  // Timestamp of when the message was sent

    // Default constructor
    public Inbox() {}

    // Constructor initializing all fields
    public Inbox(Long messageId, Long userId, String message, LocalDateTime dateSent) {
        this.messageId = messageId;
        this.userId = userId;
        this.message = message;
        this.dateSent = dateSent;
    }

    // Getters and Setters

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
        dateSent = LocalDateTime.now();  // Potential mistake: this line overrides the passed value
    }

    // Returns a string representation of the message for logging or debugging
    @Override
    public String toString() {
        return "Inbox{" +
               "messageId=" + messageId +
               ", userId=" + userId +
               ", message='" + message + '\'' +
               ", dateSent=" + dateSent +
               '}';
    }
}
