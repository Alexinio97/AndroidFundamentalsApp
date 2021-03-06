package com.example.androidfundamentalsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;

import java.util.List;

import model.Quiz;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> m_quizzes;
    private OnQuizListener mOnQuizListener;

    public QuizAdapter(List<Quiz> quizzes,OnQuizListener onQuizListener) {
        m_quizzes = quizzes;
        mOnQuizListener = onQuizListener;
    }

    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context thisContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(thisContext);

        // inflate category layout
        View quizView = inflater.inflate(R.layout.quiz_item,parent,false);
        // return a new view holder;
        return new QuizViewHolder(quizView, mOnQuizListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.QuizViewHolder holder, int position) {
        Quiz quiz = m_quizzes.get(position);

        TextView txtTitle = holder.txtTitle;
        txtTitle.setText(quiz.getTitle());
        TextView txtDifficulty = holder.txtDifficulty;
        txtDifficulty.setText(quiz.getDifficulty());
        TextView txtQuestionsCount = holder.txtQuestionsCount;
        holder.txtMadeBy.setText(quiz.getMadeBy());
        txtQuestionsCount.setText(String.format("%.0f",quiz.getQuestionsCount()));
    }

    @Override
    public int getItemCount() {
        return m_quizzes.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtTitle;
        public TextView txtQuestionsCount;
        public TextView txtDifficulty;
        public TextView txtMadeBy;
        public OnQuizListener onQuizListener;

        public QuizViewHolder(@NonNull View itemView,OnQuizListener onQuizListener) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_quiz_title);
            txtQuestionsCount = itemView.findViewById(R.id.txt_quiz_questions_count);
            txtDifficulty = itemView.findViewById(R.id.txt_difficulty);
            txtMadeBy = itemView.findViewById(R.id.txt_madeBy);
            this.onQuizListener = onQuizListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizListener.onQuizClick(getAdapterPosition());
        }
    }

    public interface OnQuizListener{
        void onQuizClick(int position);
    }
}
