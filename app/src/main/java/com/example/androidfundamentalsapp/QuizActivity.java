package com.example.androidfundamentalsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.androidfundamentalsapp.fragments.QuestionFragment;
import com.example.androidfundamentalsapp.fragments.QuizCreateFragment;

// this activity will allow the user to add its own quizzes

public class QuizActivity extends AppCompatActivity {
    private static final String QUIZ_CREATE_TAG = "quizCreateFragment";
    private static final String QUESTION_TAG = "questionFragment";
    Fragment quizFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null)
        {
            quizFrag = getSupportFragmentManager().getFragment(savedInstanceState,QUIZ_CREATE_TAG);
        }
        else {
            quizFrag = new QuizCreateFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frag_quiz_create, quizFrag, QUIZ_CREATE_TAG).addToBackStack(QUIZ_CREATE_TAG).commit();
        }
    }

    public void switchToQuizCreateFrag()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_quiz_create,quizFrag,QUIZ_CREATE_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void switchToQuestionFrag()
    {
        QuestionFragment fragment = new QuestionFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_quiz_create,fragment,QUESTION_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,QUIZ_CREATE_TAG,quizFrag);
    }
}