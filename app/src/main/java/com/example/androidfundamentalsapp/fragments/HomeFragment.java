package com.example.androidfundamentalsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.adapters.CategoriesAdapter;
import com.example.androidfundamentalsapp.activities.QuizzesActivity;
import com.example.androidfundamentalsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Category;

public class HomeFragment extends Fragment implements CategoriesAdapter.OnCategoryListener {
    private static String TAG="HomeFragment";
    private static final String CATEGORY_ID="com.example.androidfundamentalsapp.category_id";
    private static final String CATEGORY_TITLE="com.example.androidfundamentalsapp.category_title";

    private List<Category> m_categories;
    // firebase variables
    private FirebaseAuth m_auth;
    private FirebaseFirestore m_db;

    private Intent m_quizzesIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_auth = FirebaseAuth.getInstance();
        m_db = FirebaseFirestore.getInstance();
        m_categories = new ArrayList<Category>();
        RecyclerView rvCategories = view.findViewById(R.id.rv_categories_list);

        // prepare the intent for switch to all the quizzes view
        m_quizzesIntent = new Intent(view.getContext(),QuizzesActivity.class);

        m_db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                {
                                    m_categories.add(new Category(document.getId(),
                                            document.get("categoryName").toString(),document.getDouble("categoryQuizCount")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                CategoriesAdapter adapter = new CategoriesAdapter(m_categories,HomeFragment.this::onCategoryClick);
                                rvCategories.setAdapter(adapter);
                                rvCategories.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onCategoryClick(int position) {
        Category selectedCategory = m_categories.get(position);
        if(selectedCategory.getCategoryQuizCount() == 0) {
            Toast.makeText(getContext(), "No quizzes on this category.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(m_quizzesIntent != null) {
            m_quizzesIntent.putExtra(CATEGORY_ID,selectedCategory.getId());
            m_quizzesIntent.putExtra(CATEGORY_TITLE,selectedCategory.getCategoryName());
            startActivity(m_quizzesIntent);
        }


    }
}
