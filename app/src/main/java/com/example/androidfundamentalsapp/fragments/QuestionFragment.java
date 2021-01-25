package com.example.androidfundamentalsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidfundamentalsapp.activities.QuizActivity;
import com.example.androidfundamentalsapp.R;
import com.example.androidfundamentalsapp.adapters.QuestionAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionFragment extends Fragment implements QuestionAdapter.OnAnswerListener {
    private static final String TAG="QuestionFragment";
    private ArrayList<String> mAnswers;

    private Button btnSaveQuestion;
    private ImageButton btnAddAnswer;
    private RecyclerView rvAnswers;
    private TextInputLayout txtQuestion;
    private TextInputLayout txtPossibleAnswer;

    private int selectedPosition = -1;
    QuestionAdapter questionAdapter;
    private String correctAnswer = "";

    private HashMap<String,Object> questionReceived;

    public static QuestionFragment newInstance(HashMap<String,Object> question) {
        QuestionFragment fragment = new QuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        btnSaveQuestion = view.findViewById(R.id.btn_save_question);
        btnAddAnswer = view.findViewById(R.id.img_btn_add_answer);
        rvAnswers = view.findViewById(R.id.rv_answers);
        txtQuestion = view.findViewById(R.id.txt_question_string_create);
        txtPossibleAnswer = view.findViewById(R.id.txt_possible_answer);

        mAnswers = new ArrayList<>();
        Bundle args = getArguments();
        if(args != null)
        {
            try {
                questionReceived = (HashMap<String, Object>) args.getSerializable("question");
                mAnswers = (ArrayList<String>) questionReceived.get("answers");
                txtQuestion.getEditText().setText(questionReceived.get("questionString").toString());
                correctAnswer = (String) questionReceived.get("correctAnswer");
            }
            catch (Exception ex)
            {
                Log.d(TAG,"Could not fetch fragment arguments.",ex);
            }
        }
        questionAdapter = new QuestionAdapter(mAnswers,this::onAnswerClick);
        rvAnswers.setAdapter(questionAdapter);
        rvAnswers.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if(!correctAnswer.equals(""))
        {
            questionAdapter.setSelectedPosition(correctAnswer);
        }

        btnSaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = txtQuestion.getEditText().getText().toString();
                if(question.equals(""))
                {
                    Toast.makeText(view.getContext(), "Question cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mAnswers.size() < 2)
                {
                    Toast.makeText(view.getContext(), "At least two possible answers must be inserted.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(correctAnswer.equals(""))
                {
                    Toast.makeText(view.getContext(), "Please select correct answer.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(questionReceived == null) {
                    HashMap<String, Object> questionMap = new HashMap<>();
                    questionMap.put("questionId",question);
                    questionMap.put("questionString", question);
                    questionMap.put("correctAnswer", correctAnswer);
                    questionMap.put("answers", mAnswers);
                    questionMap.put("possibleAnswers", mAnswers.size());
                    correctAnswer = "";
                    ((QuizActivity)getActivity()).switchToQuizCreateFrag(questionMap);
                }
                else {
                    // in order to store the question ID
                    questionReceived.put("questionString", question);
                    questionReceived.put("correctAnswer", correctAnswer);
                    questionReceived.put("answers", mAnswers);
                    questionReceived.put("possibleAnswers", mAnswers.size());
                    correctAnswer = "";
                    ((QuizActivity)getActivity()).switchToQuizCreateFrag(questionReceived);
                }


            }
        });

        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerToInsert = txtPossibleAnswer.getEditText().getText().toString();
                if(mAnswers.size() == 5)
                {
                    Toast.makeText(view.getContext(), "Can't add more answers, 5 is the limit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!answerToInsert.equals(""))
                {
                    Log.d(TAG,"Question added to array!");
                    if(checkIfAnswerExists(answerToInsert))
                    {
                        mAnswers.add(answerToInsert);
                        questionAdapter.notifyDataSetChanged();
                        txtPossibleAnswer.getEditText().setText("");
                    }
                }
                else
                {
                    Log.d(TAG,"Answer is empty, will not add it to list!");
                    Toast.makeText(view.getContext(), "Please add an answer first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIfAnswerExists(String answerToInsert)
    {
        for(String answer: mAnswers)
        {
            if(answer.equals(answerToInsert))
            {
                Toast.makeText(getView().getContext(), "Answer already exists!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onAnswerClick(int position) {
        Log.d(TAG,"Correct answer: " + mAnswers.get(position));
        questionAdapter.setSelectedPosition(mAnswers.get(position));
        correctAnswer = mAnswers.get(position);
    }
}