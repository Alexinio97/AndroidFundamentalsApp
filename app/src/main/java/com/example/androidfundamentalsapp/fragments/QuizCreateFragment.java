package com.example.androidfundamentalsapp.fragments;

import android.content.Context;
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
    private TextInputLayout txtTitle;
    private TextInputLayout txtDifficulty;
    Spinner spCategories;
    Button btnSaveQuiz;
    ImageButton btnAddQuestion;

    private Bundle quizState;
    private List<Category> categories;
    public QuizCreateFragment(){
        super(R.layout.fragment_quiz_create);
    }

    public static QuizCreateFragment newInstance(String title,String difficulty,int categoryId)
    {
        QuizCreateFragment quizCreateFragment = new QuizCreateFragment();
        Bundle args = new Bundle();
        args.putString("Title",title);
        args.putString("Difficulty",difficulty);
        args.putInt("CategoryId",categoryId);
        quizCreateFragment.setArguments(args);
        return  quizCreateFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_create,container,false);
        txtTitle =  view.findViewById(R.id.txt_quiz_title_create);
        txtDifficulty = view.findViewById(R.id.txt_quiz_create_difficulty);
        spCategories = view.findViewById(R.id.sp_quiz_create_category);
        btnSaveQuiz = view.findViewById(R.id.btn_save_quiz);
        btnAddQuestion = view.findViewById(R.id.img_btn_add_question);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        m_db = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();

        //check if state has been saved from parent activity
        Bundle args = ((QuizActivity)getActivity()).getStateFromMainActivity();

        if(args != null)
        {
            txtTitle.getEditText().setText(args.getString("Title"));
            txtDifficulty.getEditText().setText(args.getString("Difficulty"));
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
                Bundle args = new Bundle();
                args.putString("Title",txtTitle.getEditText().getText().toString());
                args.putString("Difficulty",txtDifficulty.getEditText().getText().toString());
                args.putInt("CategoryIndex",spCategories.getSelectedItemPosition());
                ((QuizActivity)getActivity()).saveStateFromMainActivity(args);
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
                        if(args != null)
                            spCategories.setSelection(args.getInt("CategoryIndex"));
                    }
                });
    }


    public void getSelectedCategory(View v)
    {
        Category category = (Category)spCategories.getSelectedItem();
    }
}
