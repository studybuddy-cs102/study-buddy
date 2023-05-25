package com.cs102.studybuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    TableLayout courseTable;
    CourseListView courseListView;
    LayoutInflater inflater;

    public void onCourseClick(Course c) {
        // TODO: Open course discussion
        Log.d(StudyBuddy.TAG, String.format("Click %s from main", c.getCourseId()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.inflater = inflater;
        this.courseListView = rootView.findViewById(R.id.courseListView);

        this.courseTable = courseListView.getCourseTable();
        courseListView.setCourseClickListener(this::onCourseClick);
        courseListView.populateCourseList(
            ((StudyBuddy) requireActivity().getApplication()).currentUser
        );

        return rootView;
    }

}