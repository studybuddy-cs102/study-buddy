package com.cs102.studybuddy.core;

import java.util.HashMap;

public class Match {
    private String courseId;
    private Boolean isGroup;
    private HashMap<String, Boolean> members;

    public Match() {}

    public Match( String courseId, boolean isGroup, HashMap<String, Boolean> members) {
        this.courseId = courseId;
        this.isGroup = isGroup;
        this.members = members;
    }

    public String getCourseId() {
        return courseId;
    }

    public HashMap<String, Boolean> getMembers() {
        return members;
    }
    public boolean getisGroup(){
        return isGroup;
    }
    public void addMember(User user) {
        this.members.put(user.getUsername(),true);
    }
}
