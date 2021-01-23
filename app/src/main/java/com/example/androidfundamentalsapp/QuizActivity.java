package com.example.androidfundamentalsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.androidfundamentalsapp.fragments.QuestionFragment;
import com.example.androidfundamentalsapp.fragments.QuizCreateFragment;
import com.google.android.material.textfield.TextInputLayout;

// this activity will allow the user to add its own quizzes

public class QuizActivity extends AppCompatActivity {
    private static final String QUIZ_CREATE_TAG = "quizCreateFragment";
    private static final String QUESTION_TAG = "questionFragment";
    private static final String TAG = "QuizActivity";


    Fragment quizFrag;
    QuestionFragment questionFrag;
    private Bundle mainArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizFrag = new QuizCreateFragment();
        questionFrag = new QuestionFragment();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag_placeholder,quizFrag,null)
                .commit();
    }

    public void switchToQuizCreateFrag()
    {
        if(mainArgs != null)
        {
            String title = mainArgs.getString("Title");
            String difficulty = mainArgs.getString("Difficulty");
            int categoryId = mainArgs.getInt("CategoryId",0);
            quizFrag = QuizCreateFragment.newInstance(title,difficulty,categoryId);
        }
        else
        {
            quizFrag = new QuizCreateFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_placeholder,quizFrag,QUIZ_CREATE_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void switchToQuestionFrag()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_placeholder,questionFrag,QUESTION_TAG)
                .addToBackStack(null)
                .commit();
    }

    // this method will set the bundle for fragment
    public void saveStateFromMainActivity(Bundle args)
    {
        Log.d(TAG,"State saved!");
        mainArgs = args;
    }

    // this method will be called inside fragment in order to populate data in case of back
    public Bundle getStateFromMainActivity()
    {
        return mainArgs;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"Saving data!");
        getSupportFragmentManager().putFragment(outState,QUIZ_CREATE_TAG,quizFrag);
    }
}