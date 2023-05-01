package com.cs102.studybuddy;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button rememberMe = findViewById(R.id.RememberMe);
        final Button forgotPass = findViewById(R.id.forgotPass);
        final Button login = findViewById(R.id.Login);
        final Button goSignUp = findViewById(R.id.goSignUp);

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.length());
            }
        });

        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}