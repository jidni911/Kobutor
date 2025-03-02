package com.jidnivai.kobutor.activities.additional;

// StatusModel.java
public class StatusModel {
    String userName;
    String mediaUrl;
    long timestamp;

    public StatusModel(String userName, String mediaUrl, long timestamp) {
        this.userName = userName;
        this.mediaUrl = mediaUrl;
        this.timestamp = timestamp;
    }

    public String getUserName() { return userName; }
    public String getMediaUrl() { return mediaUrl; }
    public long getTimestamp() { return timestamp; }
}
