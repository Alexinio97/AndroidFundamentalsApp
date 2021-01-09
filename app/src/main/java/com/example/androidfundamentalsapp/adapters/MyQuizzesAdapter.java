package com.example.androidfundamentalsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;

import org.w3c.dom.Text;

import java.util.List;

import model.MyQuiz;

public class MyQuizzesAdapter extends RecyclerView.Adapter<MyQuizzesAdapter.MyQuizzesViewHolder> {
    private List<MyQuiz> m_UserQuiz;

    public MyQuizzesAdapter(List<MyQuiz> userQuiz) {
        this.m_UserQuiz = userQuiz;
    }

    @NonNull
    @Override
    public MyQuizzesAdapter.MyQuizzesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View myQuizView = inflater.inflate(R.layout.myquiz_item,parent,false);
        return new MyQuizzesViewHolder(myQuizView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQuizzesAdapter.MyQuizzesViewHolder holder, int position) {
        MyQuiz myQuiz = m_UserQuiz.get(position);

        TextView quizTitle = holder.quizTitle;
        TextView quizScore = holder.scoreObtained;
        TextView questionsCount = holder.questionCount;

        quizTitle.setText(myQuiz.get_Title());
        quizScore.setText(String.format("%.0f",myQuiz.get_Score()));
        questionsCount.setText(String.format("%.0f",myQuiz.get_QuestionsCount()));
    }

    @Override
    public int getItemCount() {
        return m_UserQuiz.size();
    }

    public static class MyQuizzesViewHolder extends RecyclerView.ViewHolder{

        private TextView scoreObtained;
        private TextView quizTitle;
        private TextView questionCount;

        public MyQuizzesViewHolder(@NonNull View itemView) {
            super(itemView);

            quizTitle = itemView.findViewById(R.id.txt_my_quiz_title);
            scoreObtained = itemView.findViewById(R.id.txt_my_quiz_score);
            questionCount = itemView.findViewById(R.id.txt_my_quiz_question_count);


        }
    }
}
