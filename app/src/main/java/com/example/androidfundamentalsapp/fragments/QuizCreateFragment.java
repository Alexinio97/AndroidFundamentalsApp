package com.example.androidfundamentalsapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidfundamentalsapp.QuizActivity;
import com.example.androidfundamentalsapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Category;

public class QuizCreateFragment extends Fragment {
    private static final String TAG ="QuizCreateFragment";
    // firestore
    private FirebaseFirestore m_db;

    //layout controls
    TextInputLayout txtTitle;
    TextInputLayout txtDifficulty;
    Spinner spCategories;
    Button btnSaveQuiz;
    ImageButton btnAddQuestion;

    private List<Category> categories;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_create,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        m_db = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();

        if(savedInstanceState != null)
        {
            Log.d(TAG,"Already there!");
        }
        // get control references
        txtTitle = view.findViewById(R.id.txt_question_create_title);
        txtDifficulty = view.findViewById(R.id.txt_quiz_create_difficulty);
        spCategories = view.findViewById(R.id.sp_quiz_create_category);
        btnSaveQuiz = view.findViewById(R.id.btn_save_quiz);
        btnAddQuestion = view.findViewById(R.id.img_btn_add_question);

        if(savedInstanceState != null) {
            txtTitle.getEditText().setText(savedInstanceState.getString("Title"));
            txtDifficulty.getEditText().setText(savedInstanceState.getString("Difficulty"));
            spCategories.setSelection(savedInstanceState.getInt("CategoryIndex"));
        }

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuizActivity)getActivity()).switchToQuestionFrag();
            }
        });

        m_db.collection("categories").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            categories.add(new Category(snapshot.getId(),snapshot.getString("category"),snapshot.getDouble("QuizCount")));
                        }
                        ArrayAdapter<Category> spAdapter = new ArrayAdapter<Category>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,categories);
                        spCategories.setAdapter(spAdapter);
                    }
                });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"Saving fragment state!");
        outState.putString("Title",txtTitle.getEditText().getText().toString());
        outState.putString("Difficulty",txtDifficulty.getEditText().getText().toString());
        outState.putInt("CategoryIndex",spCategories.getSelectedItemPosition());
    }


    public void getSelectedCategory(View v)
    {
        Category category = (Category)spCategories.getSelectedItem();
    }
}
