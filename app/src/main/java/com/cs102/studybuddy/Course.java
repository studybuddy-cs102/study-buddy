package com.cs102.studybuddy;

import java.util.HashMap;

public class Course {
    private String courseId;
    private String name;
    private HashMap<String, Integer> members;
    private HashMap<String, Boolean> mentors;
    private HashMap<String, Boolean> wantsToStudy;

    public Course() {}

    public Course(
        String courseId, String name,
        HashMap<String, Integer> members,
        HashMap<String, Boolean> mentors,
        HashMap<String, Boolean> wantsToStudy
    ) {
        this.courseId = courseId;
        this.name = name;
        this.members = members;
        this.mentors = mentors;
        this.wantsToStudy = wantsToStudy;
    }

    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public HashMap<String, Integer> getMembers() { return members; }
    public HashMap<String, Boolean> getMentors() { return mentors; }
    public HashMap<String, Boolean> getWantsToStudy() { return wantsToStudy; }

    public void Enroll(User user) {
        this.members.put(user.getUsername(), 0);
    }
}
