package com.cs102.studybuddy;

import java.util.HashMap;

public class Course {
    private String courseId;
    private String name;
    private HashMap<String, Integer> members;
    private HashMap<String, Boolean> mentors;

    public Course() {}

    public Course(
        String courseId, String name,
        HashMap<String, Integer> members,
        HashMap<String, Boolean> mentors
    ) {
        this.courseId = courseId;
        this.name = name;
        this.members = members;
        this.mentors = mentors;
    }

    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public HashMap<String, Integer> getMembers() { return members; }
    public HashMap<String, Boolean> getMentors() { return mentors; }

    public void Enroll(User user) {
        this.members.put(user.getUsername(), 10);
    }
}
