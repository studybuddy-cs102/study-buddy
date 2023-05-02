package com.cs102.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainFragment extends Fragment {


    FirebaseAuth auth;
    FirebaseUser user;
    Button go;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        auth = FirebaseAuth.getInstance();
        textView = rootView.findViewById(R.id.textView1);
        go = rootView.findViewById(R.id.goButton);
        user = auth.getCurrentUser();

        if (user!=null&&user.isEmailVerified()){
            textView.setText(user.getEmail());
            Toast.makeText(getActivity(), user.getUid(), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;



    }


}