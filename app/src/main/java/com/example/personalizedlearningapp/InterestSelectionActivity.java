package com.example.personalizedlearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class InterestSelectionActivity extends AppCompatActivity {

    private static final int MAX_SELECTION = 10;
    private LinearLayout topicContainer;
    private Button btnSubmitInterests;

    private String[] topics = {
            "Machine Learning", "Data Science", "Artificial Intelligence",
            "Cybersecurity", "Cloud Computing", "Web Development",
            "Android Development", "Blockchain", "Internet of Things",
            "Natural Language Processing", "DevOps", "Game Development"
    };

    private List<CheckBox> selectedBoxes = new ArrayList<>();
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selection);

        topicContainer = findViewById(R.id.topicContainer);
        btnSubmitInterests = findViewById(R.id.btnSubmitInterests);

        userName = getIntent().getStringExtra("name");

        for (String topic : topics) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(topic);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedBoxes.size() >= MAX_SELECTION) {
                        buttonView.setChecked(false);
                        Toast.makeText(this, "Select up to 10 only!", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedBoxes.add(checkBox);
                    }
                } else {
                    selectedBoxes.remove(checkBox);
                }
            });
            topicContainer.addView(checkBox);
        }

        btnSubmitInterests.setOnClickListener(v -> {
            ArrayList<String> selectedTopics = new ArrayList<>();
            for (CheckBox cb : selectedBoxes) {
                selectedTopics.add(cb.getText().toString());
            }

            if (selectedTopics.isEmpty()) {
                Toast.makeText(this, "Please select at least one topic.", Toast.LENGTH_SHORT).show();
                return;
            }


            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", userName);
            editor.putStringSet("topics", new HashSet<>(selectedTopics));
            editor.apply();


            Toast.makeText(this, "Preferences saved. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InterestSelectionActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
