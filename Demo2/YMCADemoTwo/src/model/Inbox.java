package model;

import java.time.LocalDateTime;

public class Inbox {
    private Long messageId;
    private Long userId;
    private String message;
    private LocalDateTime dateSent;

    // Constructors
    public Inbox() {
    }

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
        dateSent = LocalDateTime.now();
    }

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
