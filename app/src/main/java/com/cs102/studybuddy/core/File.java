package com.cs102.studybuddy.core;

import android.widget.EditText;

import java.util.HashMap;

public class File {
    private String name;
//    private String path;
//    private int size;
//    private String firePath;
//    private String type;
    private String uploaderId, course;

    // This is here for deserialization from Firebase document
    public File() {}

    public File(String name, String uploaderId, String courseId)
    {
        this.name = name;
//        this.path = path;
//        this.size = size;
//        this.firePath = firePath;
//        this.type = type;
        this.uploaderId = uploaderId;
        this.course = courseId;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getUploaderId() {
        return uploaderId;
    }
}
