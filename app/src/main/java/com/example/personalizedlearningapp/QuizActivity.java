package com.example.personalizedlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class QuizActivity extends AppCompatActivity {

    LinearLayout layoutQuestions;
    Button btn_for_submit;
    ProgressBar progressBar;
    TextView topicHeader;

    List<String> questionList = new ArrayList<>();
    List<List<String>> optionsList = new ArrayList<>();
    List<String> correctAnswers = new ArrayList<>();
    List<RadioGroup> radioGroups = new ArrayList<>();

    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        layoutQuestions = findViewById(R.id.layoutQuestions);
        btn_for_submit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        topicHeader = findViewById(R.id.topicHeader);

        topic = getIntent().getStringExtra("topic");
        if (topic == null || topic.isEmpty()) topic = "machine learning";  // fallback

        topicHeader.setText("Topic: " + topic);
        Log.d("QUIZ_DEBUG", "Topic = " + topic);

        fetchQuizFromAPI(topic);

        btn_for_submit.setOnClickListener(v -> {
            int score = 0;
            String[] optionLetters = {"A", "B", "C", "D"};

            for (int i = 0; i < radioGroups.size(); i++) {
                int selectedId = radioGroups.get(i).getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selected = findViewById(selectedId);
                    int selectedIndex = radioGroups.get(i).indexOfChild(selected);

                    if (selectedIndex >= 0 && selectedIndex < optionLetters.length) {
                        String userAnswer = optionLetters[selectedIndex].trim().toUpperCase();
                        String correctAnswer = correctAnswers.get(i).trim().toUpperCase();

                        if (userAnswer.equals(correctAnswer)) {
                            score++;
                        }
                    }
                }
            }

            Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
            resultIntent.putStringArrayListExtra("questions", new ArrayList<>(questionList));
            resultIntent.putExtra("options", (Serializable) optionsList);
            resultIntent.putStringArrayListExtra("correctAnswers", new ArrayList<>(correctAnswers));
            resultIntent.putExtra("score", score);
            resultIntent.putExtra("total", questionList.size());
            resultIntent.putExtra("topic", topic);
            startActivity(resultIntent);
        });
    }

    private void fetchQuizFromAPI(String topic) {
        String url = "http://10.0.2.2:5000/getQuiz?topic=" + topic.replace(" ", "%20");
        Log.d("QUIZ_REQUEST", "Fetching from URL: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONArray quizArray = response.getJSONArray("quiz");
                        for (int i = 0; i < quizArray.length(); i++) {
                            JSONObject q = quizArray.getJSONObject(i);
                            String question = q.getString("question");
                            JSONArray options = q.getJSONArray("options");
                            String correct = q.getString("correct_answer");

                            List<String> optionsTemp = new ArrayList<>();
                            for (int j = 0; j < options.length(); j++) {
                                optionsTemp.add(options.getString(j));
                            }

                            questionList.add(question);
                            optionsList.add(optionsTemp);
                            correctAnswers.add(correct);

                            addQuestionToLayout(question, optionsTemp);
                        }
                    } catch (JSONException e) {
                        Log.e("QUIZ_API_ERROR", "Parsing error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing quiz", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("QUIZ_API_ERROR", "Volley error: " + error.toString());
                    Toast.makeText(this, "API error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    private void addQuestionToLayout(String question, List<String> options) {
        TextView questionText = new TextView(this);
        questionText.setText((questionList.size()) + ". " + question);
        questionText.setTextSize(16);
        questionText.setPadding(0, 24, 0, 8);
        layoutQuestions.addView(questionText);

        RadioGroup group = new RadioGroup(this);
        for (String opt : options) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            group.addView(rb);
        }

        layoutQuestions.addView(group);
        radioGroups.add(group);
    }
}
