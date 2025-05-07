package com.example.personalizedlearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DashboardActivity extends AppCompatActivity {

    TextView welcome_for, task_label_for;
    Button btn_for_task;
    String selectedTopic = "machine learning"; // fallback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcome_for = findViewById(R.id.textWelcome);
        task_label_for = findViewById(R.id.textTaskLabel);
        btn_for_task = findViewById(R.id.btnStartTask);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "User");
        Set<String> topicSet = prefs.getStringSet("topics", null);

        // Set welcome message
        welcome_for.setText("Hello, " + name + "!");

        if (topicSet != null && !topicSet.isEmpty()) {
            List<String> topics = new ArrayList<>(topicSet);
            selectedTopic = topics.get(0); // get first topic
            task_label_for.setText("Today's task is based on: " + selectedTopic);
        } else {
            task_label_for.setText("No topic selected. Please update preferences.");
        }

        btn_for_task.setOnClickListener(view_for -> {
            Intent go_to_quiz = new Intent(DashboardActivity.this, QuizActivity.class);
            go_to_quiz.putExtra("topic", selectedTopic);
            startActivity(go_to_quiz);
        });
    }
}
