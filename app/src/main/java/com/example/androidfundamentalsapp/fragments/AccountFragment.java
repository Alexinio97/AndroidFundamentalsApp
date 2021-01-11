package com.example.androidfundamentalsapp.fragments;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidfundamentalsapp.R;
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
    private TextInputLayout phone;
    private Button btnEditSave;

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

        m_db.collection("users").document(userUid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG,"User fetched!");
                        userData = new User(userUid,documentSnapshot.getString("firstName"),documentSnapshot.getString("lastName")
                                            ,documentSnapshot.getString("email"));
                        try {
                            userData.setPhone(documentSnapshot.getString("phone"));
                            // populate ui with user data
                            firstName.getEditText().setText(userData.getFirstName());
                            lastName.getEditText().setText(userData.getLastName());
                            email.getEditText().setText(userData.getEmail());
                            phone.getEditText().setText(userData.getPhone());
                        }catch (Exception ex)
                        {
                            Log.d(TAG,"Phone cannot be set.",ex);
                        }

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
                    isEditing = true;
                }
                else
                {
                    // TODO: Save user data to database
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    phone.setEnabled(false);
                    btnEditSave.setText("Edit");
                    btnEditSave.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.secondaryColor));
                    isEditing = false;
                }

            }
        });
    }
}
