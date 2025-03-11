package com.jidnivai.kobutor.models;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private int messegeCount;
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

    public int getMessegeCount() {
        return messegeCount;
    }

    public void setMessegeCount(int messegeCount) {
        this.messegeCount = messegeCount;
    }

    public Chat(JSONObject jsonObject) throws JSONException{
        if (jsonObject==null) return;
        if(jsonObject.has("id") && !jsonObject.isNull("id") ) this.id = jsonObject.getLong("id");
        if (jsonObject.has("name") && !jsonObject.isNull("name") && jsonObject.get("name") instanceof String) this.name = jsonObject.getString("name");
        if (jsonObject.has("group") && !jsonObject.isNull("group") && jsonObject.get("group") instanceof Boolean) this.isGroup = jsonObject.getBoolean("group");
        if (jsonObject.has("groupImage") && !jsonObject.isNull("groupImage") && jsonObject.get("groupImage") instanceof JSONObject) this.groupImage = new Image(jsonObject.getJSONObject("groupImage"));
        if (jsonObject.has("requestedMembers") && !jsonObject.isNull("requestedMembers") ){
            this.requestedMembers = new ArrayList<>();
            JSONArray requestedMembersArray = jsonObject.getJSONArray("requestedMembers");
            for (int i = 0; i < requestedMembersArray.length(); i++) {
                JSONObject memberObject = requestedMembersArray.getJSONObject(i);
                this.requestedMembers.add(new User(memberObject));
            }
        }
        if (jsonObject.has("members") && !jsonObject.isNull("members") ){
            this.members = new ArrayList<>();
            JSONArray membersArray = jsonObject.getJSONArray("members");
            for (int i = 0; i < membersArray.length(); i++) {
                JSONObject memberObject = membersArray.getJSONObject(i);
                this.members.add(new User(memberObject));
            }
        }
        if (jsonObject.has("creator") && !jsonObject.isNull("creator")) this.creator = new User(jsonObject.getJSONObject("creator"));
        if (jsonObject.has("lastMessage") && !jsonObject.isNull("lastMessage")) this.lastMessage = jsonObject.getString("lastMessage");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (jsonObject.has("lastMessageTime") && !jsonObject.isNull("lastMessageTime"))
                this.lastMessageTime = LocalDateTime.parse(jsonObject.getString("lastMessageTime"));
            if (jsonObject.has("createdAt") && !jsonObject.isNull("createdAt")){
                this.createdAt = LocalDateTime.parse(jsonObject.getString("createdAt"));
            }
            if (jsonObject.has("updatedAt") && !jsonObject.isNull("updatedAt")){
                this.updatedAt = LocalDateTime.parse(jsonObject.getString("updatedAt"));
            }
        }
        if (jsonObject.has("messegeCount") && !jsonObject.isNull("messegeCount")) this.messegeCount = jsonObject.getInt("messegeCount");


    }
}
