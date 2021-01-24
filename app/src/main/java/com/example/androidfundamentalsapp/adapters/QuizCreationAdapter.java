package com.example.androidfundamentalsapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;

import java.util.HashMap;
import java.util.List;

public class QuizCreationAdapter extends RecyclerView.Adapter<QuizCreationAdapter.QuizCreationViewHolder> {
    private static final String TAG= "QuizCreationAdapter";
    private List<HashMap<String,Object>> mQuestions;

    public QuizCreationAdapter(List<HashMap<String,Object>> questions)
    {
        mQuestions = questions;
    }

    @NonNull
    @Override
    public QuizCreationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context thisContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(thisContext);

        // inflate category layout
        View quizView = inflater.inflate(R.layout.quiz_question_item,parent,false);
        // return a new view holder;
        return new QuizCreationViewHolder(quizView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizCreationViewHolder holder, int position) {
        HashMap<String,Object> question = mQuestions.get(position);

        try {
            holder.txtQuestion.setText(question.get("questionString").toString());
            holder.txtPossibleAnswers.setText(question.get("possibleAnswers").toString());
        }
        catch (Exception ex)
        {
            Log.d(TAG,"Error at binding view holder.",ex);
        }
        holder.imgBtnDeleteQuestion.setOnClickListener(v -> {
            mQuestions.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public static class QuizCreationViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtQuestion;
        private final TextView txtPossibleAnswers;
        private final ImageButton imgBtnDeleteQuestion;

        public QuizCreationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txt_question_create_title);
            txtPossibleAnswers = itemView.findViewById(R.id.txt_possible_answers);
            imgBtnDeleteQuestion = itemView.findViewById(R.id.img_btn_delete_question);
        }
    }
}
