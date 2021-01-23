package com.example.androidfundamentalsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{
    List<String> mAnswers;
    private OnAnswerListener m_OnAnswerListener;
    private String correctAnswer;

    public void setSelectedPosition(String answer)
    {
        correctAnswer = answer;
        notifyDataSetChanged();
    }

    public QuestionAdapter(List<String> answers,OnAnswerListener onAnswerListener)
    {
        mAnswers = answers;
        m_OnAnswerListener = onAnswerListener;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View questionView = inflater.inflate(R.layout.answer_item,parent,false);
        return new QuestionViewHolder(questionView,m_OnAnswerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {
        final String answer = mAnswers.get(position);
        holder.txtPossibleAnswer.setText(answer);
        ImageButton imgBtnDelete = holder.imgBtnDeleteAnswer;

        // change correct answer color to green and store the answer in Parent Activity
        // through OnAnswerListener
        if(mAnswers.get(position).equals(correctAnswer))
        {
            holder.txtPossibleAnswer.setTextColor(Color.parseColor("#32CD32"));
        }
        else
        {
            holder.txtPossibleAnswer.setTextColor(Color.parseColor("#ffffff"));
        }
        imgBtnDelete.setOnClickListener(v -> {
            if(mAnswers.get(position).equals(correctAnswer))
            {
                correctAnswer = null;
            }
            mAnswers.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPossibleAnswer;
        ImageButton imgBtnDeleteAnswer;

        OnAnswerListener onAnswerListener;

        public QuestionViewHolder(@NonNull View itemView,OnAnswerListener onAnswerListener) {
            super(itemView);
            txtPossibleAnswer = itemView.findViewById(R.id.txt_answer_added);
            imgBtnDeleteAnswer = itemView.findViewById(R.id.img_btn_delete_answer);
            this.onAnswerListener = onAnswerListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAnswerListener.onAnswerClick(getAdapterPosition());
        }
    }

    public interface OnAnswerListener{
        void onAnswerClick(int position);
    }
}
