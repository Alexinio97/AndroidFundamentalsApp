package com.example.androidfundamentalsapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfundamentalsapp.MainActivity;
import com.example.androidfundamentalsapp.activities.QuizActivity;
import com.example.androidfundamentalsapp.R;
import com.example.androidfundamentalsapp.adapters.QuizCreationAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Category;
import model.Quiz;
import model.User;

public class QuizCreateFragment extends Fragment implements QuizCreationAdapter.OnQuestionListener {
    private static final String TAG ="QuizCreateFragment";
    private FirebaseAuth m_auth;
    private FirebaseFirestore m_db;
    private User mUserLogged;

    //layout controls
    private TextInputLayout txtTitle;
    private TextInputLayout txtDifficulty;
    private RecyclerView rvQuestions;
    Spinner spCategories;
    Button btnSaveQuiz;
    Button btnCancelQuiz;
    ImageButton btnAddQuestion;

    private List<Category> categories;
    private ArrayList<HashMap<String,Object>> mQuestions;
    private QuizCreationAdapter quizAdapter;

    public QuizCreateFragment(){
        super(R.layout.fragment_quiz_create);
    }

    public static QuizCreateFragment newInstance(String title,String difficulty,int categoryId,ArrayList<HashMap<String,Object>> questions)
    {
        QuizCreateFragment quizCreateFragment = new QuizCreateFragment();
        Bundle args = new Bundle();
        args.putString("Title",title);
        args.putString("Difficulty",difficulty);
        args.putInt("CategoryId",categoryId);
        args.putSerializable("questions",questions);
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
        rvQuestions = view.findViewById(R.id.rv_questions_list);
        btnCancelQuiz = view.findViewById(R.id.btn_cancel_quiz);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        m_db = FirebaseFirestore.getInstance();
        m_auth = FirebaseAuth.getInstance();

        categories = new ArrayList<>();

        //check if state has been saved from parent activity
        Bundle args = ((QuizActivity)getActivity()).getStateFromMainActivity();
        // set empty arraylist if there are no args
        mQuestions = new ArrayList<>();

        boolean questionExists = false;
        m_db.collection("users").document(m_auth.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    mUserLogged = new User(documentSnapshot.getId(),documentSnapshot.getString("firstName"),
                            documentSnapshot.getString("lastName"),documentSnapshot.getString("email"),
                            documentSnapshot.getString("nickname"));
                });

        if(args != null)
        {
            txtTitle.getEditText().setText(args.getString("Title"));
            txtDifficulty.getEditText().setText(args.getString("Difficulty"));

            try {
                if(args.getSerializable("questions") != null)
                {
                    mQuestions = (ArrayList<HashMap<String,Object>>)args.getSerializable("questions");
                }
                HashMap<String,Object> questionToAdd = (HashMap<String, Object>) args.getSerializable("question");
                // don't add question if it already exists in array
                String questionToAddId = questionToAdd.get("questionId").toString();
                for(int i = 0; i< mQuestions.size(); i++)
                {
                    if(mQuestions.get(i).get("questionId").toString().equals(questionToAddId))
                    {
                        questionExists = true;
                        mQuestions.set(i,questionToAdd);
                        break;
                    }
                }
                if(!questionExists)
                {
                    mQuestions.add(questionToAdd);
                }
            }
            catch (Exception ex)
            {
                Log.d(TAG,"Could not get question serializable!");
            }
        }


        quizAdapter = new QuizCreationAdapter(mQuestions,this);
        rvQuestions.setAdapter(quizAdapter);
        rvQuestions.setLayoutManager(new LinearLayoutManager(view.getContext()));

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
                if(mQuestions.size() == 10)
                {
                    Toast.makeText(view.getContext(), "You can add only 10 questions!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle args = new Bundle();
                args.putString("Title",txtTitle.getEditText().getText().toString());
                args.putString("Difficulty",txtDifficulty.getEditText().getText().toString());
                args.putInt("CategoryIndex",spCategories.getSelectedItemPosition());
                if(mQuestions.size() != 0)
                {
                    args.putSerializable("questions",mQuestions);
                }
                ((QuizActivity)getActivity()).saveStateFromMainActivity(args);
                ((QuizActivity)getActivity()).switchToQuestionFrag(null);
            }
        });

        btnSaveQuiz.setOnClickListener(v -> {
            String title = txtTitle.getEditText().getText().toString();
            String difficulty = txtDifficulty.getEditText().getText().toString();
            if(title.equals(""))
            {
                Toast.makeText(view.getContext(), "Quiz must have a title!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(difficulty.equals(""))
            {
                Toast.makeText(view.getContext(), "Quiz must have a difficulty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mQuestions.size() < 2)
            {
                Toast.makeText(view.getContext(), "Quiz must have at least two questions!", Toast.LENGTH_SHORT).show();
                return;
            }

            Quiz myQuiz = new Quiz(title,(double)mQuestions.size(),difficulty,mUserLogged.getNickname(),mUserLogged.getUID());
            Category selectedCategory = (Category)spCategories.getSelectedItem();
            m_db.collection("categories").document(selectedCategory.getId())
                    .collection("Quizzes")
                    .add(myQuiz)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,"Quiz added, now adding questions!");
                            // add questions after quiz information has been added!
                            addQuizReferenceToUser(documentReference.getId());
                            Map<String,Object> questionsMap = new HashMap<>();
                            questionsMap.put("questions",mQuestions);
                            m_db.collection("categories").document(selectedCategory.getId())
                                    .collection("Quizzes")
                                    .document(documentReference.getId())
                                    .collection("Questions")
                                    .add(questionsMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG,"Questions added with success!");
                                            Intent mainIntent = new Intent(view.getContext(), MainActivity.class);
                                            increaseCategoryCounter();
                                            // also update quizCount
                                            Toast.makeText(view.getContext(), "Quiz added with success!", Toast.LENGTH_LONG).show();
                                            startActivity(mainIntent);
                                            ((QuizActivity)getActivity()).finish();
                                        }
                                    });
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Failed to add quiz!",e);
                }
            });
        });

        m_db.collection("categories").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            categories.add(new Category(snapshot.getId(),snapshot.getString("categoryName"),snapshot.getDouble("categoryQuizCount")));
                        }
                        ArrayAdapter<Category> spAdapter = new ArrayAdapter<Category>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,categories);
                        spCategories.setAdapter(spAdapter);
                        if(args != null)
                            spCategories.setSelection(args.getInt("CategoryIndex"));
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        btnCancelQuiz.setOnClickListener(v -> {
            builder.setMessage("Are you sure?")
                    .setNegativeButton("No",(dialog, which) -> dialog.cancel())
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent mainActivity = new Intent(view.getContext(),MainActivity.class);
                        startActivity(mainActivity);
                        ((QuizActivity)getActivity()).finish();
                    }).show();
        });
    }

    private void increaseCategoryCounter()
    {
        Category selectedCategory = (Category)spCategories.getSelectedItem();
        double categoryQuizCount = selectedCategory.getCategoryQuizCount();
        categoryQuizCount++;
        selectedCategory.setCategoryQuizCount(categoryQuizCount);

        m_db.collection("categories").document(selectedCategory.getId())
                .set(selectedCategory)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG,"Updated category quiz counter - success!");
                })
        .addOnFailureListener(e -> {
            Log.d(TAG,"Could not update quiz counter",e);
        });
    }

    private void addQuizReferenceToUser(String quizId)
    {
        Map<String,Object> quizData = new HashMap<>();
        quizData.put("quizReference",quizId);
        quizData.put("categoryReference",(categories.get(spCategories.getSelectedItemPosition()).getId()));
        quizData.put("quizTitle",txtTitle.getEditText().getText().toString());
        quizData.put("questionsCount",mQuestions.size());
        quizData.put("userRef",m_auth.getUid());

        m_db.collection("users").document(m_auth.getUid())
                .collection("myQuizzes").document(quizId).set(quizData)
                .addOnSuccessListener(aVoid -> {
                   Log.d(TAG,"Quiz added to my quizzes!");
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Could not add quiz to my quizzes!",e);
            }
        });
    }

    @Override
    public void onQuestionClick(int position) {
        Log.d(TAG,"Question clicked, populating data.");
        Bundle args = new Bundle();
        HashMap<String,Object> questionToEdit = mQuestions.get(position);
        questionToEdit.put("questionId",position);
        args.putSerializable("question",mQuestions.get(position));
        ((QuizActivity)getActivity()).switchToQuestionFrag(args);
    }
}
