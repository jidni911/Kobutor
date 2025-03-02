package com.jidnivai.kobutor.activities.messaging;

// Chat.java
public class Chat {

    private int id;
    private String name;
    private String lastMessage;
    private String time;

    public Chat(int id, String name, String lastMessage, String time) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }
}
