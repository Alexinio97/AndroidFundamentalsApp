package com.example.androidfundamentalsapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;
import com.example.androidfundamentalsapp.adapters.MyQuizzesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.MyQuiz;

public class MyQuizzesFragment extends Fragment {
    private static final String TAG="MyQuizzesFragment";
    private ArrayList<MyQuiz> userQuizzes;

    //firestore
    private FirebaseFirestore m_db;
    private FirebaseAuth m_auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_quizzes,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        m_db = FirebaseFirestore.getInstance();
        m_auth = FirebaseAuth.getInstance();
        RecyclerView rvUserQuizzes = view.findViewById(R.id.rv_my_quizzes);
        userQuizzes = new ArrayList<>();
        
        m_db.collection("users").document(m_auth.getUid()).collection("myQuizzes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots)
                        {
                            String quizId = document.getId();
                            String categoryId = document.getString("categoryReference");
                            double score = document.getDouble("score");
                            double questionsCount = document.getDouble("questionsCount");
                            String quizTitle = document.getString("quizTitle");
                            userQuizzes.add(new MyQuiz(quizId,quizTitle,score,questionsCount,categoryId));
                            Log.d(TAG,"Quiz added to user quizzes!");
                        }
                        if(userQuizzes.size() != 0) {
                            Log.d(TAG,"Consturcting adapter");
                            MyQuizzesAdapter quizzesAdapter = new MyQuizzesAdapter(userQuizzes);
                            rvUserQuizzes.setAdapter(quizzesAdapter);
                            rvUserQuizzes.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        }
                        else
                        {
                              TextView noContentText = view.findViewById(R.id.txt_my_quizzes_no_content);
                              noContentText.setText("No quizzes completed yet!");
                        }
                    }
                });
    }


}
