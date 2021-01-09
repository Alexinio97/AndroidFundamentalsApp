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

import model.Category;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    private List<Category> m_categoryList;
    private OnCategoryListener m_onCategoryListener;

    public CategoriesAdapter(List<Category> categoryList,OnCategoryListener onCategoryListener) {
        this.m_categoryList = categoryList;
        m_onCategoryListener = onCategoryListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context thisContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(thisContext);

        // inflate category layout
        View categoryView = inflater.inflate(R.layout.category_item,parent,false);
        // return a new view holder;
        return new CategoryViewHolder(categoryView,m_onCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoryViewHolder holder, int position) {
        Category category = m_categoryList.get(position);
        TextView categoryName = holder.categoryName;
        categoryName.setText(category.getCategoryName());

        TextView quizCount = holder.categoryCount;
        quizCount.setText(String.format("%.0f",category.getCategoryQuizCount()));
    }

    @Override
    public int getItemCount() {
        return m_categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;
        public TextView categoryCount;
        OnCategoryListener onCategoryListener;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);

            categoryName = (TextView) itemView.findViewById(R.id.txt_category_name);
            categoryCount = (TextView) itemView.findViewById(R.id.txt_category_quiz_count);
            this.onCategoryListener = onCategoryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface OnCategoryListener {
        void onCategoryClick(int position);
    }
}
