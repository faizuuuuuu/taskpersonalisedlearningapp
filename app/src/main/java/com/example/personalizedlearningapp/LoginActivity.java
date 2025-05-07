package com.example.personalizedlearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput_for;
    Button loginButton_for;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput_for = findViewById(R.id.usernameInput_for);
        loginButton_for = findViewById(R.id.loginButton_for);

        loginButton_for.setOnClickListener(v -> {
            String enteredName = usernameInput_for.getText().toString().trim();

            if (TextUtils.isEmpty(enteredName)) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String storedName = prefs.getString("name", null);
            Set<String> topicSet = prefs.getStringSet("topics", null);

            if (storedName != null && storedName.equalsIgnoreCase(enteredName)) {
                if (topicSet != null && !topicSet.isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("username", storedName);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No preferences found. Please sign up again.", Toast.LENGTH_SHORT).show();
                    Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(signupIntent);
                }
            } else {
                Toast.makeText(this, "Name not found. Redirecting to Sign Up...", Toast.LENGTH_SHORT).show();
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }
}
