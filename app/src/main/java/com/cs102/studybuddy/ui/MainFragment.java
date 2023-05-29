package com.cs102.studybuddy.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cs102.studybuddy.R;
import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.core.Post;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.views.CourseDiscussionView;
import com.cs102.studybuddy.views.CourseListView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainFragment extends Fragment {
    CourseListView courseListView;
    CourseDiscussionView courseDiscussionView;
    LayoutInflater inflater;

    EditText editPostContent;
    User currentUser;
    Post createdPost;

    private void onCourseClick(Course c) {
        // TODO: Open course discussion
        ((MainActivity) requireActivity()).getSupportActionBar().setTitle(c.getName());

        courseDiscussionView.setLeaveCourseListener(this::onCourseLeave);
        courseDiscussionView.setChangeStudyPreferenceListener(this::onChangeStudyPreference);
        courseDiscussionView.setOnPostListener(this::onPost);
        courseDiscussionView.setOnBackListener(this::onBack);

        courseDiscussionView.populatePostList(c, currentUser.getUsername());

        courseListView.setVisibility(View.GONE);
        courseDiscussionView.setVisibility(View.VISIBLE);
    }

    private void onCourseLeave(Course c) {
        c.Leave(currentUser);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses").document(c.getCourseId()).set(c);
        db.collection("users").document(currentUser.getUsername()).set(currentUser);

        ((MainActivity) requireActivity()).getSupportActionBar().setTitle("My Courses");
        courseListView.removeCourse(c);
        goBack();
    }

    private void onChangeStudyPreference(Course c) {
        c.SwitchWantsToStudy(currentUser);
        FirebaseFirestore.getInstance()
            .collection("courses")
            .document(c.getCourseId()).set(c);
    }

    private void onPost(Course c) {
        editPostContent = courseDiscussionView.findViewById(R.id.editPostContent);
        String content = editPostContent.getText().toString();
        if (content.trim().length() == 0) { return; }

        createdPost = new Post(
            c.getCourseId(), currentUser.getUsername(),
            content, System.currentTimeMillis(), 0
        );
        FirebaseFirestore.getInstance()
            .collection("posts")
            .document().set(createdPost).addOnCompleteListener(this::onPostComplete);
        Log.d(StudyBuddy.TAG, content);
    }

    private void onPostComplete(Task<Void> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(getContext(),
                R.string.msg_failedToPost,
                Toast.LENGTH_LONG
            ).show();
            return;
        }

        courseDiscussionView.addPost(createdPost);
        editPostContent.setText("");
    }

    private void onBack(View v) { goBack(); }

    private void goBack() {
        courseDiscussionView.removeAllViews();
        courseListView.setVisibility(View.VISIBLE);
        courseDiscussionView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.currentUser = ((StudyBuddy) requireActivity().getApplication()).currentUser;

        this.inflater = inflater;
        this.courseListView = rootView.findViewById(R.id.courseListView);
        this.courseDiscussionView = rootView.findViewById(R.id.courseDiscussionView);

        courseListView.setCourseClickListener(this::onCourseClick);
        courseListView.populateCourseList(currentUser);

        return rootView;
    }

}