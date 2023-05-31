package com.cs102.studybuddy.core;

public class Match {
    private Boolean active;
    private String courseId;
    private String username1;
    private String username2;
    private String matchId;

    public Match() {}

    public Match(String courseId, String username1, String username2,
                 Boolean active, String matchId) {
        this.courseId = courseId;
        this.active = active;
        this.matchId = matchId;
        this.username1 = username1;
        this.username2 = username2;
    }

    public String getCourseId() { return courseId; }

    public Boolean getActive() { return active; }

    public String getUsername1() { return username1; }

    public String getUsername2() { return username2; }

    public String getMatchId() { return matchId; }

    public void setMatchId(String matchId) { this.matchId = matchId; }

    public boolean hasUser(String username) {
        return username1.equals(username) || username2.equals(username);
    }
}
