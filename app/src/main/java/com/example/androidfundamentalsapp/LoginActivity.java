package com.example.androidfundamentalsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputEditText email;
    private TextInputEditText password;
    private FirebaseAuth m_auth;
    private FirebaseUser m_user;
    private static String TAG="LoginActivity";

    @Override
    protected void onStart() {
        super.onStart();
        m_user = m_auth.getCurrentUser();
        if(m_user != null)
        {
            Log.d(TAG,"User logged in already!");
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m_auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().equals("") || password.getText().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Email and password can't be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                DoLogin(email.toString(),password.toString());
            }
        });
    }

    private  void DoLogin(String email,String password)
    {
        m_auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG,"SignIn: succesful!");
                            m_user = m_auth.getCurrentUser();
                            Intent mainIntent = new Intent("MainActivity");
                            startActivity(mainIntent);
                        }
                        else{
                            Log.w(TAG,"SignIn: failure!",task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}