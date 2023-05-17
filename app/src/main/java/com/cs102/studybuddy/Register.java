package com.cs102.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private final Pattern emailPattern = Pattern.compile(
        "([a-zA-Z0-9._]+)@([a-z]*?\\.?bilkent\\.edu\\.tr)"
    );
    private Matcher emailMatcher;

    private EditText txtEmail, txtPassword, txtPasswordConfirm, txtBirthYear;
    private RadioGroup genderGroup;
    private Button btnSignup;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onStart() {
        super.onStart();
    }

    private void addUserToDb(@NonNull FirebaseUser authUser) {
        CollectionReference users = FirebaseFirestore
            .getInstance().collection("users");

        int birthYear = -1;
        try {
            birthYear = Integer.parseInt(txtBirthYear.getText().toString());
        } catch (NumberFormatException e) {
            txtBirthYear.setError(e.getLocalizedMessage());
        }

        String email = Objects.requireNonNull(authUser.getEmail());
        String username = emailMatcher.group(1);

        String gender;
        try {
            RadioButton radioGender = findViewById(genderGroup.getCheckedRadioButtonId());
            gender = radioGender.getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User(email, "John", "Doe",
            birthYear, gender, new HashMap<>(), "", true);

        users.document(Objects.requireNonNull(username))
            .set(user, SetOptions.merge())
            .addOnFailureListener(exception -> Toast.makeText(
                Register.this,
                exception.getLocalizedMessage(),
                Toast.LENGTH_LONG
            ).show());
    }

    private boolean isValidEmail(String email) {
        emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.find()) {
            txtEmail.setError("Invalid email");
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password, String passwordConfirm) {
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Password cannot be empty");
            return false;
        } else if (password.length() < 8) {
            txtPassword.setError("Password cannot be less than 8 characters");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            txtPasswordConfirm.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void onEmailVerification(@NonNull Task<Void> task) {
        String message;
        Class<?> nextActivity;

        if (!task.isSuccessful()) {
            message = Objects.requireNonNull(task.getException()).getLocalizedMessage();
            nextActivity = Register.class;
        } else {
            message = getString(R.string.msg_check_email_verify);
            nextActivity = Login.class;
        }

        Toast.makeText(Register.this, message, Toast.LENGTH_LONG)
            .show();

        startActivity(
                new Intent(getApplicationContext(), nextActivity)
        );
        finish();
    }

    private void onCreateUser(@NonNull Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(
                Register.this,
                Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnSignup.setText(R.string.msg_verification_sent);
        btnSignup.setEnabled(false);

        AuthResult authResult = Objects.requireNonNull(task.getResult());

        Objects.requireNonNull(authResult.getUser()).sendEmailVerification()
                .addOnCompleteListener(this::onEmailVerification);

        addUserToDb(authResult.getUser());
    }

    private void onSignup(@NonNull View v) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (!isValidEmail(email)) return;
        if (!isValidPassword(password, txtPasswordConfirm.getText().toString())) return;
        if (TextUtils.isEmpty(txtBirthYear.getText())) {
            txtBirthYear.setError("Birth year is required");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this::onCreateUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.email_signup);
        txtPassword = findViewById(R.id.password_signup);
        txtPasswordConfirm = findViewById(R.id.confirm_password_signup);
        txtBirthYear = findViewById(R.id.year_birth);
        genderGroup = findViewById(R.id.gender);

        btnSignup = findViewById(R.id.SignUp);
        btnSignup.setOnClickListener(this::onSignup);
    }
}