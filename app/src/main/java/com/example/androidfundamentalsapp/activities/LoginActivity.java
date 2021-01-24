package com.example.androidfundamentalsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfundamentalsapp.MainActivity;
import com.example.androidfundamentalsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnGoToRegister;
    private TextInputLayout email;
    private TextInputLayout password;

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
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        btnGoToRegister = findViewById(R.id.btn_go_to_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getEditText().getText().toString().equals("")
                        || password.getEditText().getText().toString().equals(""))
                {
                    Log.w(TAG,"No input received!");
                    Toast.makeText(LoginActivity.this, "Email and password can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DoLogin(email.getEditText().getText().toString(),password.getEditText().getText().toString());
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
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
                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.w(TAG,"SignIn: failure!",task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}