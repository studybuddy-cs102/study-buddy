package com.cs102.studybuddy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.databinding.CourseCardLayoutBinding;
import com.cs102.studybuddy.listeners.CourseClickListener;
import com.cs102.studybuddy.core.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CourseListView extends ScrollView {
    private final TableLayout courseTable;
    private final HashMap<String, Course> courseList;

    private CourseClickListener courseClickListener;

    public CourseListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        courseList = new HashMap<>();

        courseTable = new TableLayout(context);
        courseTable.setStretchAllColumns(true);
        courseTable.setLayoutParams(
            new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        );
        addView(courseTable);
    }

    public TableLayout getCourseTable() { return courseTable; }

    public void setCourseClickListener(CourseClickListener courseClickListener) {
        this.courseClickListener = courseClickListener;
    }

    public void removeCourse(Course c) {
        courseList.remove(c.getCourseId());
        courseTable.removeAllViews();
        for (Course course : courseList.values()) {
            createCourseView(course);
        }
    }

    // Add a course to the list
    public void addCourse(Course course) {
        if (courseList.containsKey(course.getCourseId())) return;

        courseList.put(course.getCourseId(), course);
    }

    public void createCourseView(Course course) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CourseCardLayoutBinding binding = CourseCardLayoutBinding.inflate(inflater);
        binding.setCourse(course);
        binding.setCallback(courseClickListener);

        View courseView = binding.getRoot();
        courseTable.addView(courseView);
    }

    // Create course list from all courses
    public void populateCourseList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses")
            .get().addOnCompleteListener(this::onGetCourses);
    }

    // Create course list from user's courses
    public void populateCourseList(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String courseId : user.getEnrollments().keySet()) {
            db.collection("courses")
                .document(courseId)
                .get().addOnCompleteListener(this::onGetCourse);
        }
    }

    // Callback when we got a single course data
    private void onGetCourse(@NonNull Task<DocumentSnapshot> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(
                getContext(),
                "Failed to fetch course",
                Toast.LENGTH_LONG
            ).show();

            return;
        }

        // When we got the course from Firebase,
        // create the view with the data bindings.
        Course course = task.getResult().toObject(Course.class);
        addCourse(course);
        createCourseView(course);
    }

    // Callback when we got courses collection
    private void onGetCourses(@NonNull Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(
                getContext(),
                "Failed to fetch course list",
                Toast.LENGTH_LONG
            ).show();

            return;
        }

        for (DocumentSnapshot courseDoc : task.getResult().getDocuments()) {
            Course course = courseDoc.toObject(Course.class);
            addCourse(course);
            createCourseView(course);
        }
    }
}
