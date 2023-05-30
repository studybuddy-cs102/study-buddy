package com.cs102.studybuddy.core;

import com.google.api.BackendProto;

import java.util.HashMap;

public class Match {
    private Boolean isActive;
    private String courseId;
    private Boolean isGroup;
    private HashMap<String, Boolean> members;
    private String docID;

    public Match() {}

    public Match( String courseId, boolean isGroup, HashMap<String, Boolean> members, Boolean isActive, String docID) {
        this.courseId = courseId;
        this.isGroup = isGroup;
        this.members = members;
        this.isActive = isActive;
        this.docID = docID;
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
    public Boolean getActive() {
        return isActive;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocID() { return docID; }
}
