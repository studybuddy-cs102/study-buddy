package com.cs102.studybuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class SearchFragment extends Fragment {
    CourseListView courseListView;
    UserListView userListView;
    TableLayout courseTable;
    LayoutInflater inflater;
    View rootView;
    Course course;
    HashMap<String, Boolean> matchMembers;
    User user;

    public void onCourseClick(Course c) {
        // TODO: Join the user to the course automatically
        course = c;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        c.Enroll(user);
        user.Enroll(c);
        db.collection("users").document(user.getUsername()).set(user, SetOptions.merge());
        db.collection("courses").document(c.getCourseId()).set(c, SetOptions.merge());
        // TODO: Show the users after joining
        courseListView.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
        userListView.populateCourseList(c);
        userListView.setUserClickListener(this::onUserClick);
        Log.d(StudyBuddy.TAG, String.format("Click %s from search", c.getCourseId()));
    }
    public void onUserClick(User u){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Match match = new Match(course.getCourseId(),false, new HashMap<>());
        match.addMember(u);
        match.addMember(((StudyBuddy) requireActivity().getApplication()).currentUser);
        db.collection("matches").add(match);
        Log.d(StudyBuddy.TAG, String.format("Click %s from search", u.getUsername()));
        // TODO: OPEN chat for the match and create its collection
        //TODO: Remove the current user from the list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        this.courseListView = rootView.findViewById(R.id.courseListView);
        this.userListView = rootView.findViewById(R.id.MatchView);
        courseListView.setCourseClickListener(this::onCourseClick);
        courseListView.populateCourseList();
        user = ((StudyBuddy) requireActivity().getApplication()).currentUser;
        return rootView;
    }
}