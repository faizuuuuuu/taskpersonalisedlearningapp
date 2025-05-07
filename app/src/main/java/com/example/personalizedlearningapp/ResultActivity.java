package com.example.personalizedlearningapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    TextView textResult;
    Button btnBackToDashboard;
    LinearLayout layoutResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textResult = findViewById(R.id.textResult);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        layoutResults = findViewById(R.id.layoutResults);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        ArrayList<String> questions = getIntent().getStringArrayListExtra("questions");
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");
        ArrayList<ArrayList<String>> optionsList = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("options");

        textResult.setText("You scored " + score + " out of " + total);

        // Display full quiz
        for (int i = 0; i < questions.size(); i++) {
            TextView qText = new TextView(this);
            qText.setText((i + 1) + ". " + questions.get(i));
            qText.setTextSize(16);
            qText.setPadding(0, 20, 0, 10);
            layoutResults.addView(qText);

            ArrayList<String> opts = optionsList.get(i);
            for (int j = 0; j < opts.size(); j++) {
                TextView optText = new TextView(this);
                char optLabel = (char) ('A' + j);
                optText.setText("Option " + optLabel + ": " + opts.get(j));
                optText.setPadding(20, 2, 0, 2);
                layoutResults.addView(optText);
            }

            TextView ansText = new TextView(this);
            ansText.setText("✔ Correct Answer: " + correctAnswers.get(i));
            ansText.setTextColor(Color.parseColor("#4CAF50"));
            ansText.setPadding(0, 8, 0, 16);
            layoutResults.addView(ansText);
        }

        btnBackToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
