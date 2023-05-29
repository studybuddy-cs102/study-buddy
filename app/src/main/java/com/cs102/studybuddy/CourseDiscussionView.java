package com.cs102.studybuddy;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cs102.studybuddy.databinding.PostLayoutBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CourseDiscussionView extends ScrollView {
    private final TableLayout postTable;
    private final ArrayList<Post> postList;

    private Course course;

    public CourseDiscussionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        postList = new ArrayList<>();

        postTable = new TableLayout(context);
        postTable.setStretchAllColumns(true);
        postTable.setLayoutParams(
            new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        );
        addView(postTable);
    }

    public TableLayout getPostTable() { return postTable; }

    public void populatePostList(Course course) {
        this.course = course;

        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("courseId", course.getCourseId())
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get().addOnCompleteListener(this::onGetPosts);
    }

    public void addPost(Post post) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        PostLayoutBinding binding = PostLayoutBinding.inflate(inflater);
        binding.setPost(post);
        binding.setRelativeDate(DateUtils.getRelativeTimeSpanString(
            post.getCreatedAt(), System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString());

        postTable.addView(binding.getRoot());
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
            postList.add(postDoc.toObject(Post.class));
        }

        for (int i = postList.size() - 1; i >= 0; i--) {
            addPost(postList.get(i));
        }
    }
}
