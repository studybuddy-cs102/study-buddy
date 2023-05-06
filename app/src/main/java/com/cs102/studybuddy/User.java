package com.cs102.studybuddy;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class User {
    private String username;
    private String email;
    private String name;
    private String surname;
    private int birthYear;
    private String gender;
    private HashMap<String, Integer> enrollments;

    public User(String username, String email, String name,
                String surname, int birthYear, String gender)
    {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.gender = gender;

        this.enrollments = new HashMap<>();
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getBirthYear() { return birthYear; }
    public String getGender() { return gender; }
    public HashMap<String, Integer> getEnrollments() { return enrollments; }
}
