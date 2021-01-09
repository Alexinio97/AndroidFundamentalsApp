package com.example.androidfundamentalsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfundamentalsapp.adapters.QuizAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Quiz;

public class QuizzesActivity extends AppCompatActivity implements QuizAdapter.OnQuizListener {
    private static final String CATEGORY_ID="com.example.androidfundamentalsapp.category_id";
    private static final String QUIZ_ID="com.example.androidfundamentalsapp.quiz_id";
    private static final String QUIZ_TITLE="com.example.androidfundamentalsapp.quiz_title";
    private static final String CATEGORY_TITLE="com.example.androidfundamentalsapp.category_title";
    private static final String TAG="QuizzesActivity";

    TextView categoryTitle;
    private FirebaseFirestore m_db;
    // Quizzes
    private List<Quiz> quizList;
    Intent questionsIntent;
    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);

        RecyclerView rvQuizList = findViewById(R.id.rv_quiz_list);
        categoryTitle = findViewById(R.id.txt_quizzes_category_title);
        categoryTitle.setText(getIntent().getStringExtra(CATEGORY_TITLE));
        categoryId = getIntent().getStringExtra(CATEGORY_ID);
        m_db = FirebaseFirestore.getInstance();
        // prepare the intent for questions
        questionsIntent = new Intent(QuizzesActivity.this,QuestionsActivity.class);

        m_db.collection("categories").document(categoryId).collection("Quizzes").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        quizList = new ArrayList<Quiz>();
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot snapshot : task.getResult())
                            {
                                quizList.add(new Quiz(snapshot.getId(),snapshot.getString("title"),
                                        snapshot.getDouble("questionsCount"),snapshot.getString("difficulty")));
                            }
                            QuizAdapter adapter = new QuizAdapter(quizList,QuizzesActivity.this::onQuizClick);
                            rvQuizList.setAdapter(adapter);
                            rvQuizList.setLayoutManager(new LinearLayoutManager(QuizzesActivity.this));

                        }
                        else {
                            Log.d(TAG,"Get quizzes failed.",task.getException());
                            Toast.makeText(QuizzesActivity.this, "Failed to retrieve quizzes!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onQuizClick(int position) {
        questionsIntent.putExtra(QUIZ_ID,quizList.get(position).getId());
        questionsIntent.putExtra(CATEGORY_ID,categoryId);
        questionsIntent.putExtra(QUIZ_TITLE,quizList.get(position).getTitle());
        startActivity(questionsIntent);
    }
}