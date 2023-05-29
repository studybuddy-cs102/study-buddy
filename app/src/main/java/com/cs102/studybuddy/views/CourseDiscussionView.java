package com.cs102.studybuddy.views;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.core.Post;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.databinding.CourseInteractionLayoutBinding;
import com.cs102.studybuddy.databinding.CreatePostLayoutBinding;
import com.cs102.studybuddy.databinding.PostLayoutBinding;
import com.cs102.studybuddy.listeners.ChangeStudyPreferenceListener;
import com.cs102.studybuddy.listeners.LeaveCourseListener;
import com.cs102.studybuddy.listeners.OnPostListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CourseDiscussionView extends LinearLayout {
    private ScrollView postsScroll;
    private LinearLayout postList;
    private LayoutInflater inflater;

    private LeaveCourseListener leaveCourseListener;
    private ChangeStudyPreferenceListener changeStudyPreferenceListener;
    private OnPostListener onPostListener;
    private View.OnClickListener onBackListener;

    public CourseDiscussionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(context);

        leaveCourseListener = (c) -> {};
        changeStudyPreferenceListener = (c) -> {};
        onPostListener = (c) -> {};
        onBackListener = (v) -> {};
    }

    public LinearLayout getPostList() { return postList; }

    public void setChangeStudyPreferenceListener(ChangeStudyPreferenceListener changeStudyPreferenceListener) {
        this.changeStudyPreferenceListener = changeStudyPreferenceListener;
    }

    public void setLeaveCourseListener(LeaveCourseListener leaveCourseListener) {
        this.leaveCourseListener = leaveCourseListener;
    }

    public void setOnPostListener(OnPostListener onPostListener) {
        this.onPostListener = onPostListener;
    }

    public void setOnBackListener(OnClickListener onBackListener) {
        this.onBackListener = (v) -> {
            postList.removeAllViews();
            postsScroll.removeAllViews();
            removeAllViews();

            onBackListener.onClick(v);
        };
    }

    public void populatePostList(Course course, String username) {
        createCourseInteractionLayout(course, username);
        createTable();
        createCreatePostLayout(course);

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("courseId", course.getCourseId())
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get().addOnCompleteListener(this::onGetPosts);
    }

    public void addPost(Post post) {
        Log.d(StudyBuddy.TAG, "Adding post: " + post.getContent());
        PostLayoutBinding binding = PostLayoutBinding.inflate(inflater);
        binding.setPost(post);
        binding.setRelativeDate(DateUtils.getRelativeTimeSpanString(
            post.getCreatedAt(), System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString());

        postList.addView(binding.getRoot());
        postsScroll.computeScroll();
        postsScroll.fullScroll(View.FOCUS_DOWN);
    }

    private void createCourseInteractionLayout(Course course, String username) {
        CourseInteractionLayoutBinding binding = CourseInteractionLayoutBinding.inflate(inflater);
        binding.setUsername(username);
        binding.setCourse(course);
        binding.setOnLeave(leaveCourseListener);
        binding.setOnChangeStudyPreference(changeStudyPreferenceListener);
        binding.setOnBack(onBackListener);

        View view = binding.getRoot();
        LayoutParams layoutParams = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(layoutParams);
        addView(view);
    }

    private void createTable() {
        postsScroll = new ScrollView(getContext());
        postList = new LinearLayout(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        );
        layoutParams.weight = 1f;

        postsScroll.setLayoutParams(layoutParams);
        postList.setOrientation(LinearLayout.VERTICAL);
//        postTable.setStretchAllColumns(true);

        postsScroll.addView(postList);
        addView(postsScroll);
    }

    private void createCreatePostLayout(Course course) {
        CreatePostLayoutBinding binding = CreatePostLayoutBinding.inflate(inflater);
        binding.setOnPost(onPostListener);
        binding.setCourse(course);
        View view = binding.getRoot();
        LayoutParams layoutParams = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(layoutParams);
        addView(view);
    }

    private void onGetPosts(@NonNull Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            Log.e(StudyBuddy.TAG, task.getException().getLocalizedMessage());
            Toast.makeText(
                getContext(),
                "Failed to fetch posts",
                Toast.LENGTH_LONG
            ).show();

            return;
        }

        for (DocumentSnapshot postDoc : task.getResult().getDocuments()) {
            addPost(postDoc.toObject(Post.class));
        }
    }
}
