package com.cs102.studybuddy;

public class Post {
    private String courseId;
    private String posterId;
    private String content;
    private long createdAt;
    private long likes;

    public Post() {}

    public Post(
        String courseId, String posterId,
        String content, long createdAt, long likes
    ) {
        this.courseId = courseId;
        this.posterId = posterId;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
    }

    public String getCourseId() { return courseId; }
    public String getPosterId() { return posterId; }
    public String getContent() { return content; }
    public long getCreatedAt() { return createdAt; }
    public long getLikes() { return likes; }
}
