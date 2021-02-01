package com.example.androidfundamentalsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidfundamentalsapp.MainActivity;
import com.example.androidfundamentalsapp.R;

public class ResultActivity extends AppCompatActivity {
    // constants
    private static final String QUIZ_TITLE="com.example.androidfundamentalsapp.quiz_title";
    private static final String USER_SCORE="com.example.androidfundamentalsapp.user_score";
    private static final String QUIZ_FRAGMENT="com.example.androidfundamentalsapp.quiz_fragment";

    private TextView quizTitle;
    private TextView userScore;
    private Button btnBackToMyQuizzes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        quizTitle = findViewById(R.id.txt_quiz_title_result);
        userScore = findViewById(R.id.txt_user_score);
        btnBackToMyQuizzes = findViewById(R.id.btn_back_to_my_quizzes);

        String quizTitleExtra = getIntent().getStringExtra(QUIZ_TITLE);
        quizTitle.setText(quizTitleExtra);
        int userScoreExtra = getIntent().getIntExtra(USER_SCORE,0);
        userScore.setText(String.valueOf(userScoreExtra));

        btnBackToMyQuizzes.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
            mainIntent.putExtra(QUIZ_FRAGMENT,1);
            startActivity(mainIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
        mainIntent.putExtra(QUIZ_FRAGMENT,1);
        startActivity(mainIntent);
        finish();
    }
}