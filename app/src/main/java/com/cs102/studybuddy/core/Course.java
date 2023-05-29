package com.cs102.studybuddy.core;

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

    public void SetWantsToStudy(User user, boolean value) {
        String username = user.getUsername();
        if (!wantsToStudy.containsKey(username)) {
            wantsToStudy.put(username, true);
            return;
        }

        wantsToStudy.put(username, value);
    }

    public void SwitchWantsToStudy(User user) {
        SetWantsToStudy(user, !wantsToStudy.get(user.getUsername()));
    }

    public void Enroll(User user) {
        user.Enroll(this);

        String username = user.getUsername();
        members.put(username, 0);
        wantsToStudy.put(username, true);
    }

    public void Leave(User user) {
        user.Leave(this);

        String username = user.getUsername();
        members.remove(username);
        mentors.remove(username);
        wantsToStudy.remove(username);
    }
}
