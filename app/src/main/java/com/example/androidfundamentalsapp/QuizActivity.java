package com.example.androidfundamentalsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.androidfundamentalsapp.fragments.QuizCreateFragment;

// this activity will allow the user to add its own quizzes

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Fragment quizFrag = new QuizCreateFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_quiz_create,quizFrag,"QuizFragment").addToBackStack(null).commit();
    }
}