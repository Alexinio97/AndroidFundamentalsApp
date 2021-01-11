package com.example.androidfundamentalsapp.fragments;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidfundamentalsapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
    private TextInputLayout phone;
    private Button btnEditSave;
    private Button btnCancel;

    private boolean isEditing = false;

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
        phone = view.findViewById(R.id.txt_account_phone);
        btnEditSave = view.findViewById(R.id.btn_account_edit);
        btnCancel = view.findViewById(R.id.btn_account_cancel);

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
                            phone.getEditText().setText(userData.getPhone());
                    }
                });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditing) {
                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    phone.setEnabled(true);
                    btnEditSave.setText("Save");
                    btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.primaryColor));
                    btnCancel.setVisibility(View.VISIBLE);
                    isEditing = true;
                }
                else
                {
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    phone.setEnabled(false);
                    btnEditSave.setText("Edit");
                    btnCancel.setVisibility(View.INVISIBLE);
                    btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.secondaryColor));
                    isEditing = false;
                    saveUserData(view);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditing = false;
                btnCancel.setVisibility(View.INVISIBLE);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                phone.setEnabled(false);
                btnEditSave.setText("Edit");
                btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.secondaryColor));
            }
        });
    }


    private void saveUserData(View view)
    {
        String strFirstName = firstName.getEditText().getText().toString();
        String strLastName = lastName.getEditText().getText().toString();
        String strEmail = email.getEditText().getText().toString();
        String strPhone = phone.getEditText().getText().toString();

        userData.setEmail(strEmail);
        userData.setPhone(strPhone);
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
