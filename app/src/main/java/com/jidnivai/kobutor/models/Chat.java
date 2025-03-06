package com.jidnivai.kobutor.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Chat implements Serializable {
    private Long id;
    private String name;
    private boolean isGroup = false;
    private Image groupImage;
    private List<User> requestedMembers;  // Added this field
    private List<User> members;
    private User creator;

    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Chat() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isGroup() { return isGroup; }
    public void setGroup(boolean group) { isGroup = group; }

    public Image getGroupImage() { return groupImage; }
    public void setGroupImage(Image groupImage) { this.groupImage = groupImage; }

    public List<User> getRequestedMembers() { return requestedMembers; }
    public void setRequestedMembers(List<User> requestedMembers) { this.requestedMembers = requestedMembers; }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
