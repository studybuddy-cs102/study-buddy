package com.cs102.studybuddy.core;

public class Message {
    private String senderId;
    private String content;
    private long createdAt;

    public Message() {}

    public Message(String senderId, String content, long createdAt) {
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getSenderId() { return senderId; }

    public String getContent() { return content; }

    public long getCreatedAt() { return createdAt; }
}
