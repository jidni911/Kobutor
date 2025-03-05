package com.jidnivai.kobutor.models;

import java.time.LocalDateTime;

// Message.java
public class Message {

    private Long id;

    private String message;
    private User sender;
    private User receiver;
    private boolean isRead = false;

    private boolean isDeleted = false;

    private boolean isSent = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Message() {
    }

    public Message(Long id, String message, User sender, User receiver, boolean isRead, boolean isDeleted, boolean isSent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
        this.isSent = isSent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
