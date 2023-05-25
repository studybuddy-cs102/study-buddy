package com.cs102.studybuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    CourseListView courseListView;
    TableLayout courseTable;
    LayoutInflater inflater;

    public void onCourseClick(Course c) {
        // TODO: Join the user to the course automatically
        // TODO: Show the users after joining
        Log.d(StudyBuddy.TAG, String.format("Click %s from search", c.getCourseId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        this.courseListView = rootView.findViewById(R.id.courseListView);
        courseListView.setCourseClickListener(this::onCourseClick);
        courseListView.populateCourseList();

        return rootView;
    }
}