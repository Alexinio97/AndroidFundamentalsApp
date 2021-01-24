package com.example.androidfundamentalsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfundamentalsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {
    // constants
    private static final String CATEGORY_ID="com.example.androidfundamentalsapp.category_id";
    private static final String QUIZ_ID="com.example.androidfundamentalsapp.quiz_id";
    private static final String QUIZ_TITLE="com.example.androidfundamentalsapp.quiz_title";
    private static final String USER_SCORE="com.example.androidfundamentalsapp.user_score";
    private static final String TAG="QuestionsActivity";

    // data variables
    private FirebaseFirestore m_db;
    private FirebaseAuth m_auth;
    private Map<String,Object> questions;
    private ArrayList<HashMap<String,Object>> formattedQuestions;
    private ArrayList<String> userAnswers;
    private int answerCount = 0;

    Button btnLastQuestion;
    Button btnNextQuestion;
    TextView questionString;
    TextView quizTitle;
    RadioGroup rgAnswers;
    ProgressBar pbAnswersProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        m_db = FirebaseFirestore.getInstance();
        m_auth = FirebaseAuth.getInstance();
        String categoryId = getIntent().getStringExtra(CATEGORY_ID);
        String quizId = getIntent().getStringExtra(QUIZ_ID);

        // instantiate list of map array
        questions = new HashMap<>();
        formattedQuestions = new ArrayList<>();
        userAnswers = new ArrayList<>();

        questionString = findViewById(R.id.txt_question_string);
        quizTitle = findViewById(R.id.txt_quiz_title_header);
        rgAnswers = findViewById(R.id.rg_questions);
        btnNextQuestion = findViewById(R.id.btn_next_question);
        btnLastQuestion = findViewById(R.id.btn_last_question);
        pbAnswersProgress = findViewById(R.id.pb_quiz_progress);

        quizTitle.setText(getIntent().getStringExtra(QUIZ_TITLE));

        m_db.collection("categories").document(categoryId).collection("Quizzes")
                .document(quizId).collection("Questions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                questions = document.getData();
                                for(Map.Entry<String, Object> entry : questions.entrySet())
                                {
                                    formattedQuestions = (ArrayList<HashMap<String,Object>>)entry.getValue();
                                }
                                pbAnswersProgress.setMax(formattedQuestions.size());
                                pbAnswersProgress.setProgress(1);
                                Log.d(TAG,Integer.toString(formattedQuestions.size()));
                            }
                            btnLastQuestion.setEnabled(false);
                            constructAnswers();
                        }
                        else
                        {
                            Log.d(TAG,"Exception caught while retrieving quiz questions.",task.getException());
                            Toast.makeText(QuestionsActivity.this, "Quiz questions could not be retrieved!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store user answer in the array
                if(answerCount < formattedQuestions.size())
                {
                    answerCount += 1;
                    btnLastQuestion.setEnabled(true);
                    pbAnswersProgress.incrementProgressBy(1);
                    Log.d(TAG,"Answer count: "  + Integer.toString(answerCount));
                }

                if(answerCount == formattedQuestions.size() - 1)
                {
                    constructAnswers();
                    btnNextQuestion.setText("Finish");
                }
                else if(btnNextQuestion.getText().equals("Finish")) {
                    int score = calculateScore();
                    // add quiz to user "my quizzes"
                    addToMyQuizzes(quizId,score,categoryId);
                }
                else
                {
                    constructAnswers();
                }
            }
        });

        btnLastQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerCount -= 1;
                constructAnswers();
                pbAnswersProgress.incrementProgressBy(-1);
                btnNextQuestion.setText("Next");
                if(answerCount == 0)
                {
                    btnLastQuestion.setEnabled(false);
                }
            }
        });
    }

    public void constructAnswers()
    {
        rgAnswers.removeAllViews();
        HashMap<String,Object> questionMap = formattedQuestions.get(answerCount);
        questionString.setText(questionMap.get("questionString").toString());

        ArrayList<String> answers = (ArrayList<String>)formattedQuestions.get(answerCount).get("answers");
        for(int i = 0; i< answers.size(); i++)
        {
            RadioButton rdBtn = new RadioButton(this);
            rdBtn.setId(i);
            rdBtn.setPadding(0,0,0,20);
            rdBtn.setText(answers.get(i));
            rdBtn.setTextSize(20);
            if(userAnswers.size() > answerCount)
            {
                if(userAnswers.get(answerCount).equals("answer" + i))
                {
                    rdBtn.setChecked(true);
                }
            }
            rdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, " Name " + ((RadioButton)v).getText() +" Id is "+v.getId());
                    if(userAnswers.size() == answerCount)
                    {
                        Log.d(TAG,"Answer doesn't exists, adding it." + v.getId());
                        userAnswers.add(answers.get(v.getId()));
                    }
                    else
                    {
                        Log.d(TAG,"Answer already saved, setting new answer.");
                        userAnswers.set(answerCount,answers.get(v.getId()));
                    }
                }
            });
            rgAnswers.addView(rdBtn);
        }
    }

    public int calculateScore()
    {
        int userScore = 0;
        for(int i = 0; i < formattedQuestions.size(); i++)
        {
            String correctAnswer = formattedQuestions.get(i).get("correctAnswer").toString();
            String userAnswer = userAnswers.get(i);
            Log.d(TAG,"User answer - " + userAnswer + "| Correct answer: " + correctAnswer);
            if(userAnswer.equals(correctAnswer))
            {
                userScore++;
                Log.d(TAG,"Correct answer!");
            }
        }
        return userScore;
    }

    public void addToMyQuizzes(String quizId,int score,String categoryId)
    {
        Map<String,Object> quizData = new HashMap<>();
        quizData.put("quizReference",quizId);
        quizData.put("categoryReference",categoryId);
        quizData.put("quizTitle",quizTitle.getText());
        quizData.put("questionsCount",formattedQuestions.size());
        quizData.put("score",score);
        m_db.collection("users").document(m_auth.getUid())
                .collection("myQuizzes").document(quizId).set(quizData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Quiz " + quizId + " added to user!");
                        Toast.makeText(QuestionsActivity.this, "Quiz added to my quizzes!", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent(QuestionsActivity.this,ResultActivity.class);
                        resultIntent.putExtra(USER_SCORE,score);
                        resultIntent.putExtra(QUIZ_TITLE,quizTitle.getText());
                        startActivity(resultIntent);
                    }
                });
    }
}