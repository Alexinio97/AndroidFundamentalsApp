package com.example.androidfundamentalsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import model.User;

public class RegisterActivity extends AppCompatActivity {
    private static String TAG="RegisterActivity";
    private FirebaseFirestore m_db;

    private Button btnRegister;
    private Button btnGoToLogin;
    private FirebaseAuth m_auth;
    // form data
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        m_auth = FirebaseAuth.getInstance();
        m_db = FirebaseFirestore.getInstance();

        btnGoToLogin = findViewById(R.id.btn_go_to_login);
        btnRegister = findViewById(R.id.btn_register);

        // text inputs
        firstName = findViewById(R.id.txt_first_name);
        lastName = findViewById(R.id.txt_last_name);
        txtEmail = findViewById(R.id.txt_email_register);
        txtPassword = findViewById(R.id.txt_password_register);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setEnabled(false);
                btnGoToLogin.setEnabled(false);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                txtEmail.setEnabled(false);
                txtPassword.setEnabled(false);

                CreateUserAuthentication(txtEmail.getEditText().getText().toString(),
                        txtPassword.getEditText().getText().toString());
            }
        });
    }

    private void CreateUserAuthentication(String email,String password)
    {
        m_auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG,"createUserWithEmail: Succes!");
                            // Add user data to firestore
                            User user = new User(m_auth.getUid(),firstName.getEditText().getText().toString(),
                                    lastName.getEditText().getText().toString(),email,null);
                            addUserToFirestore(user);
                        }
                        else {
                            Log.d(TAG,"createUserWithEmail: FAILED!",task.getException());
                            Toast.makeText(RegisterActivity.this,"User could not be created!",Toast.LENGTH_LONG).show();
                            btnRegister.setEnabled(true);
                            btnGoToLogin.setEnabled(true);
                            firstName.setEnabled(true);
                            lastName.setEnabled(true);
                            txtEmail.setEnabled(true);
                            txtPassword.setEnabled(true);
                        }
                    }
                });
    }

    private  void addUserToFirestore(User user)
    {
        Log.d(TAG,"User uid: " + user.getUID());
        m_db.collection("users").document(user.getUID())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Document written succesfully!");
                        m_auth.signOut();
                        Intent goToLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                        Toast.makeText(RegisterActivity.this,"User created, please login.",Toast.LENGTH_LONG).show();
                        startActivity(goToLogin);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error writing document",e);
                    }
                });
    }
}