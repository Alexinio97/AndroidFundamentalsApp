package com.example.androidfundamentalsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private static final String TAG="QuestionsActivity";

    private FirebaseFirestore m_db;
    private Map<String,Object> questions;
    private ArrayList<HashMap<String,String>> formattedQuestions;

    TextView questionString;
    RadioGroup rgAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        m_db = FirebaseFirestore.getInstance();
        String categoryId = getIntent().getStringExtra(CATEGORY_ID);
        String quizId = getIntent().getStringExtra(QUIZ_ID);

        // instantiate list of map array
        questions = new HashMap<>();
        formattedQuestions = new ArrayList<>();

        questionString = findViewById(R.id.txt_question_string);
        rgAnswers = findViewById(R.id.rg_questions);

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
                                    formattedQuestions = (ArrayList<HashMap<String,String>>)entry.getValue();
                                }
                                Log.d(TAG,Integer.toString(formattedQuestions.size()));
                            }
                            constructAnswers();
                        }
                        else
                        {
                            Log.d(TAG,"Exception caught while retrieving quiz questions.",task.getException());
                            Toast.makeText(QuestionsActivity.this, "Quiz questions could not be retrieved!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void constructAnswers()
    {
        questionString.setText(formattedQuestions.get(0).get("questionString"));
        for (int i = 1; i <= 2; i++) {
            RadioButton rdBtn = new RadioButton(this);
            rdBtn.setId(View.generateViewId());
            rdBtn.setText(formattedQuestions.get(0).get("answer"+i));
            rgAnswers.addView(rdBtn);
        }
    }

}