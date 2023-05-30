package com.cs102.studybuddy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cs102.studybuddy.core.Course;
import com.cs102.studybuddy.core.Match;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.databinding.UserListLayoutBinding;
import com.cs102.studybuddy.listeners.UserClickListener;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.ui.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserListView extends ScrollView {
    private final StudyBuddy application;

    private final TableLayout userTable;
    private final HashMap<String, User> userList;

    private UserClickListener userClickListener;

    private Course course;

    public UserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        application = (StudyBuddy) ((MainActivity) context).getApplication();

        userList = new HashMap<>();

        userTable = new TableLayout(context);
        userTable.setStretchAllColumns(true);
        userTable.setLayoutParams(
            new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        );
        addView(userTable);
    }

    public void setUserClickListener(UserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    // Add a course to the list
    public void addUser(User user) {
        if (userList.containsKey(user.getUsername())) return;
        userList.put(user.getUsername(), user);
    }

    public void removeUser(User user) {
        userList.remove(user.getUsername());
        userTable.removeAllViews();
        for (User u : userList.values()) {
            createUserView(u);
        }
    }


//     Create course list from user's courses
    public void populateUserList(Course course) {
        this.course = course;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Map.Entry<String,Boolean> user : course.getWantsToStudy().entrySet()) {
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
        User user = task.getResult().toObject(User.class);
        if (user.getUsername().equals(application.currentUser.getUsername())) return;

        FirebaseFirestore.getInstance()
            .collection("matches")
            .get()
            .addOnCompleteListener(t -> {
                if (!t.isSuccessful()) return;

                boolean hasMatch = false;
                for (QueryDocumentSnapshot matchDoc : t.getResult()) {
                    Match m = matchDoc.toObject(Match.class);
                    if (!m.getCourseId().equals(course.getCourseId())) continue;

                    if (m.hasUser(user.getUsername())
                        && m.hasUser(application.currentUser.getUsername())
                    ) {
                        hasMatch = true;
                        break;
                    }
                }

                if (!hasMatch) {
                    addUser(user);
                    createUserView(user);
                }
            });
    }

    private void createUserView(User user) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        UserListLayoutBinding binding = UserListLayoutBinding.inflate(inflater);
        binding.setUser(user);
        binding.setCallback(userClickListener);

        userTable.addView(binding.getRoot());
    }
}
