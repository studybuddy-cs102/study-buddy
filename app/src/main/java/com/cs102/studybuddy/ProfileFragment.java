package com.cs102.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    Button btnLogout;
    TextView txtHello;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        txtHello = rootView.findViewById(R.id.textView1);
        btnLogout = rootView.findViewById(R.id.goButton);
        btnLogout.setOnClickListener(this::onSignOut);

        User currentUser = ((StudyBuddy) requireActivity().getApplication()).currentUser;

        txtHello.setText(currentUser.getName());

        return rootView;
    }
    private void goToLoginPage() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void onSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        goToLoginPage();
    }
}