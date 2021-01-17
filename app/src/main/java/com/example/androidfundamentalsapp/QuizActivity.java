package com.example.androidfundamentalsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// this activity will allow the user to add its own quizzes

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
    }
}