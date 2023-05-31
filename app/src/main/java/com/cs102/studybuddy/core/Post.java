package com.cs102.studybuddy.core;

public class Post {
    private String courseId;
    private String posterId;
    private String content;
    private long createdAt;

    public Post() {}

    public Post(
        String courseId, String posterId,
        String content, long createdAt
    ) {
        this.courseId = courseId;
        this.posterId = posterId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getCourseId() { return courseId; }
    public String getPosterId() { return posterId; }
    public String getContent() { return content; }
    public long getCreatedAt() { return createdAt; }
}
