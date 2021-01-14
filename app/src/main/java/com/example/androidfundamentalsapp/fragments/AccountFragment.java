package com.example.androidfundamentalsapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidfundamentalsapp.R;
import com.example.androidfundamentalsapp.helper.ValidationHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import model.User;

public class AccountFragment extends Fragment {
    private static final String TAG="AccountFragment";

    private FirebaseAuth m_auth;
    private FirebaseFirestore m_db;
    private User userData;

    // input textlayouts
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout email;
    private TextInputLayout nickname;
    private Button btnEditSave;
    private Button btnCancel;

    private boolean isEditing = false;

    // validation helper
    private ValidationHelper validationHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        m_auth = FirebaseAuth.getInstance();
        m_db = FirebaseFirestore.getInstance();

        String userUid = m_auth.getUid();
        firstName = view.findViewById(R.id.txt_account_first_name);
        lastName = view.findViewById(R.id.txt_account_last_name);
        email = view.findViewById(R.id.txt_account_email);
        nickname = view.findViewById(R.id.txt_nickname);
        btnEditSave = view.findViewById(R.id.btn_account_edit);
        btnCancel = view.findViewById(R.id.btn_account_cancel);

        // instantiate object set validation boolean isValid to True
        validationHelper = new ValidationHelper();
        validationHelper.setValid(true);

        m_db.collection("users").document(userUid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG,"User fetched!");
                        userData = new User(userUid,documentSnapshot.getString("firstName"),documentSnapshot.getString("lastName")
                                            ,documentSnapshot.getString("email"),documentSnapshot.getString("phone"));
                            firstName.getEditText().setText(userData.getFirstName());
                            lastName.getEditText().setText(userData.getLastName());
                            email.getEditText().setText(userData.getEmail());
                            nickname.getEditText().setText(userData.getNickname());
                    }
                });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditing) {
                    enableControls(true);
                    btnEditSave.setText("Save");
                    btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.primaryColor));
                    btnCancel.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(!validateAccountData()) {
                        return;
                    }
                    enableControls(false);
                    btnEditSave.setText("Edit");
                    btnCancel.setVisibility(View.INVISIBLE);
                    btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.secondaryColor));

                    validationHelper.clearError(firstName);
                    validationHelper.clearError(lastName);
                    saveUserData(view);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel.setVisibility(View.INVISIBLE);
                enableControls(false);
                validationHelper.clearError(firstName);
                validationHelper.clearError(lastName);
                btnEditSave.setText("Edit");
                btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.secondaryColor));
            }
        });
    }

    private void enableControls(boolean isEnabled)
    {
        firstName.setEnabled(isEnabled);
        lastName.setEnabled(isEnabled);
        nickname.setEnabled(isEnabled);
        isEditing = isEnabled;
    }

    // input validation method
    private boolean validateAccountData()
    {
        validationHelper.isEmptyError(firstName,"First name is required!");
        validationHelper.isEmptyError(lastName,"Last name is required!");

        validationHelper.setMinMaxError(firstName,4,20);
        validationHelper.setMinMaxError(lastName,4,30);
        return validationHelper.isValid();
    }


    private void saveUserData(View view)
    {
        String strFirstName = firstName.getEditText().getText().toString();
        String strLastName = lastName.getEditText().getText().toString();
        String strEmail = email.getEditText().getText().toString();
        String strNickname = nickname.getEditText().getText().toString();

        userData.setEmail(strEmail);
        userData.setNickname(strNickname);
        userData.setFirstName(strFirstName);
        userData.setLastName(strLastName);

        m_db.collection("users").document(m_auth.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"User updated successfully!");
                        Toast.makeText(view.getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"User could not be updated!",e);
                        Toast.makeText(view.getContext(), "Data could not be saved!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
