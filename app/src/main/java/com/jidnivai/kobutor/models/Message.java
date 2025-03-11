package com.jidnivai.kobutor.models;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;

// Message.java
public class Message implements Serializable {

    private Long id;

    private String message;
    private User sender;
    private boolean isRead = false;

    private boolean isDeleted = false;

    private boolean isSent = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Message() {
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

    public Message(JSONObject jsonObject) throws JSONException{
        if (jsonObject==null) return;
        if(jsonObject.has("id") && !jsonObject.isNull("id") ) this.id = jsonObject.getLong("id");
        if (jsonObject.has("message") && !jsonObject.isNull("message") && jsonObject.get("message") instanceof String) this.message = jsonObject.getString("message");
        if (jsonObject.has("sender") && !jsonObject.isNull("sender") && jsonObject.get("sender") instanceof JSONObject) this.sender = new User(jsonObject.getJSONObject("sender"));
        if (jsonObject.has("read") && !jsonObject.isNull("read") && jsonObject.get("read") instanceof Boolean) this.isRead = jsonObject.getBoolean("read");
        if (jsonObject.has("deleted") && !jsonObject.isNull("deleted") && jsonObject.get("deleted") instanceof Boolean) this.isDeleted = jsonObject.getBoolean("deleted");
        if (jsonObject.has("sent") && !jsonObject.isNull("sent") && jsonObject.get("sent") instanceof Boolean) this.isSent = jsonObject.getBoolean("sent");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (jsonObject.has("createdAt") && !jsonObject.isNull("createdAt") && jsonObject.get("createdAt") instanceof String)
                this.createdAt = LocalDateTime.parse(jsonObject.getString("createdAt"));
            if (jsonObject.has("updatedAt") && !jsonObject.isNull("updatedAt") && jsonObject.get("updatedAt") instanceof String)
                this.updatedAt = LocalDateTime.parse(jsonObject.getString("updatedAt"));
        }
    }
}
