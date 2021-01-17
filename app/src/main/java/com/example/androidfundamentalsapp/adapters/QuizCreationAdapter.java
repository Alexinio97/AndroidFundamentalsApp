package com.example.androidfundamentalsapp.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuizCreationAdapter extends RecyclerView.Adapter<QuizCreationAdapter.QuizCreationViewHolder> {


    @NonNull
    @Override
    public QuizCreationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizCreationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class QuizCreationViewHolder extends RecyclerView.ViewHolder{

        public QuizCreationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
