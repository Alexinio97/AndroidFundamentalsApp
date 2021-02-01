package com.example.androidfundamentalsapp.adapters;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import model.Category;
import model.MyQuiz;

public class MyQuizzesAdapter extends RecyclerView.Adapter<MyQuizzesAdapter.MyQuizzesViewHolder> {
    private static final String TAG="MyQuizzesAdapter";
    private List<MyQuiz> m_UserQuizzes;
    private FirebaseAuth m_auth;
    private FirebaseFirestore m_db;
    AlertDialog.Builder builder;

    public MyQuizzesAdapter(List<MyQuiz> userQuiz) {
        this.m_UserQuizzes = userQuiz;
        m_auth = FirebaseAuth.getInstance();
        m_db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyQuizzesAdapter.MyQuizzesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        builder = new AlertDialog.Builder(parent.getContext());
        View myQuizView = inflater.inflate(R.layout.myquiz_item,parent,false);
        return new MyQuizzesViewHolder(myQuizView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQuizzesAdapter.MyQuizzesViewHolder holder, int position) {
        MyQuiz myQuiz = m_UserQuizzes.get(position);


        TextView quizTitle = holder.quizTitle;
        TextView quizScore = holder.scoreObtained;
        TextView questionsCount = holder.questionCount;

        if(myQuiz.getM_userRef() != null) {
            if (myQuiz.getM_userRef().equals(m_auth.getUid())) {
                holder.imgBtnDeleteQuiz.setVisibility(View.VISIBLE);
                holder.imgBtnDeleteQuiz.setOnClickListener(v -> {
                    builder.setMessage("Are you sure?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                removeQuizFromUser(myQuiz);
                                removeQuiz(myQuiz);
                                decreaseQuizzesCounter(myQuiz);
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.cancel())
                            .show();
                });
            }
        }

        quizTitle.setText(myQuiz.get_Title());
        quizScore.setText(String.format("%.0f",myQuiz.get_Score()));
        questionsCount.setText(String.format("%.0f",myQuiz.get_QuestionsCount()));
    }

    // removes quiz reference from user
    private void removeQuizFromUser(MyQuiz myQuiz)
    {
        m_db.collection("users").document(m_auth.getUid())
                .collection("myQuizzes").document(myQuiz.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG,"Quiz deleted from user collection!");
                });
    }
    // removes it from quizzes collection
    private void removeQuiz(MyQuiz myQuiz)
    {
        m_db.collection("categories").document(myQuiz.getM_categoryId())
                .collection("Quizzes").document(myQuiz.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG,"Quiz deleted!");
                    m_UserQuizzes.remove(myQuiz);
                    notifyDataSetChanged();
                });
    }

    private void decreaseQuizzesCounter(MyQuiz myQuiz)
    {
        m_db.collection("categories").document(myQuiz.getM_categoryId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Category newCateg = new Category(documentSnapshot.getId(),documentSnapshot.getString("categoryName")
                    ,documentSnapshot.getDouble("categoryQuizCount"));
                    double quizCount = newCateg.getCategoryQuizCount();
                    quizCount--;
                    newCateg.setCategoryQuizCount(quizCount);

                    m_db.collection("categories").document(newCateg.getId())
                            .set(newCateg)
                            .addOnSuccessListener(aVoid -> {
                               Log.d(TAG,"Quiz counter updated!");
                            });
                });
    }


    @Override
    public int getItemCount() {
        return m_UserQuizzes.size();
    }

    public static class MyQuizzesViewHolder extends RecyclerView.ViewHolder{

        private final TextView scoreObtained;
        private final TextView quizTitle;
        private final TextView questionCount;
        private final ImageButton imgBtnDeleteQuiz;

        public MyQuizzesViewHolder(@NonNull View itemView) {
            super(itemView);

            quizTitle = itemView.findViewById(R.id.txt_my_quiz_title);
            scoreObtained = itemView.findViewById(R.id.txt_my_quiz_score);
            questionCount = itemView.findViewById(R.id.txt_my_quiz_question_count);
            imgBtnDeleteQuiz = itemView.findViewById(R.id.img_btn_quiz_delete);

        }
    }
}
