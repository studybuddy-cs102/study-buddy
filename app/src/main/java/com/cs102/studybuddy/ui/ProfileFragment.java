package com.cs102.studybuddy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cs102.studybuddy.core.Match;
import com.cs102.studybuddy.core.StudyBuddy;
import com.cs102.studybuddy.core.User;
import com.cs102.studybuddy.R;
import com.cs102.studybuddy.views.MatchListView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class ProfileFragment extends Fragment {
    Button btnLogout, btnforgotPass, btnChangeName, btnChangeSurname;
    TextView txtHello;
    User currentUser;

    EditText nameField;
    MatchListView matchListView;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();
        this.currentUser = ((StudyBuddy) requireActivity().getApplication()).currentUser;
        mAuth = FirebaseAuth.getInstance();

        nameField = rootView.findViewById(R.id.changeNameOrSur);
        txtHello = rootView.findViewById(R.id.textView1);
        btnLogout = rootView.findViewById(R.id.goButton);
        btnforgotPass = rootView.findViewById(R.id.resetPass);
        btnChangeName = rootView.findViewById(R.id.resetName);
        btnChangeSurname = rootView.findViewById(R.id.resetSurname);

        btnforgotPass.setOnClickListener(this::onForgotPassClick);
        btnLogout.setOnClickListener(this::onSignOut);
        btnChangeName.setOnClickListener(this::resetName);
        btnChangeSurname.setOnClickListener(this::resetSurname);


        matchListView = rootView.findViewById(R.id.matchListView);
        matchListView.setMatchClickListener(this::onMatchClick);
        matchListView.populateMatchList(currentUser);
        matchListView.setVisibility(View.VISIBLE);

        txtHello.setText("Hello "+currentUser.getName());


        return rootView;
    }


    private void goToLoginPage() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void resetName(View view) {
        currentUser.setName(nameField.getText().toString());
        db.collection("users").document(currentUser.getUsername()).set(currentUser, SetOptions.merge());
        txtHello.setText("Hello "+currentUser.getName()+" "+currentUser.getSurname());
    }

    private void resetSurname(View view) {
        currentUser.setSurname(nameField.getText().toString());
        db.collection("users").document(currentUser.getUsername()).set(currentUser, SetOptions.merge());
        txtHello.setText("Hello "+currentUser.getName()+" "+currentUser.getSurname());
    }

    private void onSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        goToLoginPage();
    }

    private void onForgotPassClick(View v) {
        String email;
        email = String.valueOf(currentUser.getEmail());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                getContext(),
                "Please enter the email",
                Toast.LENGTH_LONG
            ).show();
            return;
        }

        mAuth.sendPasswordResetEmail(currentUser.getEmail())
            .addOnCompleteListener(this::onSendPasswordResetLink);
    }

    private void onSendPasswordResetLink(Task<Void> task) {
        if (!task.isSuccessful()) return;
        Toast.makeText(
            getContext(),
            "Please check your email",
            Toast.LENGTH_LONG
        ).show();
        return;
    }

    private void onMatchClick(Match m) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        matchListView.removeMatch(m);
        db.collection("matches").document(m.getMatchId()).delete().addOnCompleteListener(task -> Toast.makeText(
            getContext(),
            "Successfully Ended the Match",
            Toast.LENGTH_LONG
        ).show());
    }
}