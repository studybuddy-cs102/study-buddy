package com.cs102.studybuddy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.core.Match;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.R;
import com.cs102.studybuddy.views.CourseListView;
import com.cs102.studybuddy.views.UserListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class SearchFragment extends Fragment {
    private StudyBuddy application;

    private CourseListView courseListView;
    private UserListView userListView;
    private View rootView;
    private Course course;

    public void onCourseClick(Course c) {
        // TODO: Join the user to the course automatically
        course = c;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        c.Enroll(application.currentUser);
        db.collection("users")
            .document(application.currentUser.getUsername())
            .set(application.currentUser, SetOptions.merge());
        db.collection("courses")
            .document(c.getCourseId())
            .set(c, SetOptions.merge());

        // TODO: Show the users after joining
        courseListView.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
        userListView.populateUserList(c);
        userListView.setUserClickListener(this::onUserClick);
    }

    public void onUserClick(User u){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Match match = new Match(
            course.getCourseId(), u.getUsername(),
            application.currentUser.getUsername(), true, "");
        db.collection("matches").add(match).addOnSuccessListener(docRef -> {
            match.setMatchId(docRef.getId());
            db.collection("matches").document(match.getMatchId()).set(match, SetOptions.merge());
        });

        userListView.removeUser(u);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        application = ((StudyBuddy) requireActivity().getApplication());
        this.courseListView = rootView.findViewById(R.id.courseListView);
        this.userListView = rootView.findViewById(R.id.MatchView);
        courseListView.setCourseClickListener(this::onCourseClick);
        courseListView.populateCourseList();
        return rootView;
    }
}