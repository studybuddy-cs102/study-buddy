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
import com.cs102.studybuddy.databinding.UserListLayoutBinding;
import com.cs102.studybuddy.listeners.UserClickListener;
import com.cs102.studybuddy.core.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserListView extends ScrollView {
    private final TableLayout courseTable;
    private final HashMap<String, User> courseList;

    private UserClickListener userClickListener;

    public UserListView(Context context, AttributeSet attrs) {
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

    public void setUserClickListener(UserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    // Add a course to the list
    public void addUser(User user) {
        if (courseList.containsKey(user.getUsername())) return;

        courseList.put(user.getUsername(), user);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        UserListLayoutBinding binding = UserListLayoutBinding.inflate(inflater);
        binding.setUser(user);
        binding.setCallback(userClickListener);

        View courseView = binding.getRoot();
        courseTable.addView(courseView);
    }


//     Create course list from user's courses
    public void populateUserList(Course c) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Map.Entry<String,Boolean> user : c.getWantsToStudy().entrySet()) {
            if (user.getValue()) {
                db.collection("users")
                    .document(user.getKey())
                    .get().addOnCompleteListener(this::onGetUser);
            }
        }
    }

    // Callback when we got a single course data
    private void onGetUser(@NonNull Task<DocumentSnapshot> task) {
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
        addUser(task.getResult().toObject(User.class));
    }

    // Callback when we got courses collection
}
