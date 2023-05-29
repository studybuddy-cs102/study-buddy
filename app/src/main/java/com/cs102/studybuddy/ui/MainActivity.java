package com.cs102.studybuddy.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;

    MainFragment mainFragment = new MainFragment();
    SearchFragment searchFragment = new SearchFragment();
    FilesFragment filesFragment = new FilesFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(this::onPageSelect);
        setSupportActionBar(bottomNavigationView);
        getSupportActionBar().setTitle("My Courses");
        progressBar = findViewById(R.id.progressBar);

        loadCurrentUser();
    }

    private void setSupportActionBar(BottomNavigationView bottomNavigationView) {
    }

    private boolean onPageSelect(@NonNull MenuItem item) {
        getSupportActionBar().show();
        if (item.getItemId() == R.id.home) {
            getSupportActionBar().setTitle("My Courses");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.search) {
            getSupportActionBar().setTitle("Search Courses");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.files) {
            getSupportActionBar().setTitle("Files");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, filesFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.profile) {
            getSupportActionBar().hide();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
            return true;
        }
        return false;
    }

    private void loadCurrentUser() {
        String email = Objects
            .requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
            .getEmail();
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", email)
            .get().addOnCompleteListener(this::onCurrentUserFetched);
    }

    private void onCurrentUserFetched(@NonNull Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_LONG)
                .show();

            return;
        }

        ((StudyBuddy) getApplication()).currentUser = task.getResult()
            .iterator().next()
            .toObject(User.class);

        /* TODO: Load anything that should be loaded before the user
        *   interacts with the application here
        */

        ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
    }
}