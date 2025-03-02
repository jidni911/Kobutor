package com.jidnivai.kobutor.models;

// Message.java
public class Message {

    private String text;
    private String sender;
    private boolean isSent; // true if sent by the user, false if received

    public Message(String text, String sender, boolean isSent) {
        this.text = text;
        this.sender = sender;
        this.isSent = isSent;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public boolean isSent() {
        return isSent;
    }
}
