package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText fullNameInput_for, emailInput_for, phoneInput_for, passwordInput_for, confirmPasswordInput_for;
    Button signupButton_for;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullNameInput_for = findViewById(R.id.fullNameInput_for);
        emailInput_for = findViewById(R.id.emailInput_for);
        phoneInput_for = findViewById(R.id.phoneInput_for);
        passwordInput_for = findViewById(R.id.passwordInput_for);
        confirmPasswordInput_for = findViewById(R.id.confirmPasswordInput_for);
        signupButton_for = findViewById(R.id.signupButton_for);

        signupButton_for.setOnClickListener(v -> {
            String name_for = fullNameInput_for.getText().toString().trim();
            String email_for = emailInput_for.getText().toString().trim();
            String phone_for = phoneInput_for.getText().toString().trim();
            String pass_for = passwordInput_for.getText().toString().trim();
            String confirm_for = confirmPasswordInput_for.getText().toString().trim();

            if (TextUtils.isEmpty(name_for) || TextUtils.isEmpty(email_for) ||
                    TextUtils.isEmpty(phone_for) || TextUtils.isEmpty(pass_for) || TextUtils.isEmpty(confirm_for)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass_for.equals(confirm_for)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {

                // Inside the else block
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, InterestSelectionActivity.class);
                intent.putExtra("name", name_for);
                startActivity(intent);
                finish();

            }
        });
    }
}
