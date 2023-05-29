package com.cs102.studybuddy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cs102.studybuddy.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText logEmail, logPassword;
    private Button rememberMe, forgotPass, login, goSignUp;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void onSignInComplete(Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(Login.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAuth.getCurrentUser().isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Please verify your account by using the link sent to your email. ", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginClick(View v) {
        String email, pass;
        email = String.valueOf(logEmail.getText());
        pass = String.valueOf(logPassword.getText());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(Login.this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this::onSignInComplete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        logEmail = findViewById(R.id.email);
        logPassword = findViewById(R.id.password);
        rememberMe = findViewById(R.id.RememberMe);
        forgotPass = findViewById(R.id.forgotPass);
        login = findViewById(R.id.Login);
        goSignUp = findViewById(R.id.goSignUp);

        login.setOnClickListener(this::onLoginClick);

        goSignUp.setOnClickListener(this::onSignupClick);

        forgotPass.setOnClickListener(this::onForgotPassClick);
    }

    private void onSignupClick(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }

    private void onForgotPassClick(View v) {
        String email;
        email = String.valueOf(logEmail.getText());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.sendPasswordResetEmail(logEmail.getText().toString())
            .addOnCompleteListener(this::onSendPasswordResetLink);
    }

    private void onSendPasswordResetLink(Task<Void> task) {
        if (!task.isSuccessful()) return;

        Toast.makeText(
            Login.this,
            R.string.msg_passwordResetLinkSent,
            Toast.LENGTH_LONG
        ).show();
    }
}