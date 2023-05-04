package com.cs102.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText regEmail, regPass, regConfirm, year, gender;
    private Button signUp;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regEmail = findViewById(R.id.email_signup);
        regPass = findViewById(R.id.password_signup);
        regConfirm = findViewById(R.id.confirm_password_signup);
        year = findViewById(R.id.year_birth);
        gender = findViewById(R.id.gender);
        signUp = findViewById(R.id.SignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, passconfirm;
                email = String.valueOf(regEmail.getText());
                pass = String.valueOf(regPass.getText());
                passconfirm = String.valueOf(regConfirm.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!email.endsWith("bilkent.edu.tr")) {
                    Toast.makeText(Register.this, "Email must be a Bilkent email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pass.equals(passconfirm)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(authResult -> {
                            signUp.setText(R.string.msg_verification_sent);
                            signUp.setEnabled(false);

                            Objects.requireNonNull(authResult.getUser()).sendEmailVerification()
                                    .addOnSuccessListener(result -> {
                                        Toast.makeText(
                                            Register.this,
                                            "Check your email for verification link",
                                            Toast.LENGTH_LONG
                                        ).show();

                                        startActivity(
                                                new Intent(getApplicationContext(), Login.class)
                                        );
                                        finish();
                                    })
                                    .addOnFailureListener(result -> {
                                        Toast.makeText(
                                            Register.this,
                                            "Failed to send verification",
                                            Toast.LENGTH_LONG
                                        ).show();

                                        startActivity(
                                                new Intent(getApplicationContext(), Register.class)
                                        );
                                        finish();
                                    });
                        })
                        .addOnFailureListener(authException -> {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, authException.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}