package com.example.aedinger16woche26;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth firebaseAuth;

    //member
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        init();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                signInUser(email, password);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                registerUser(email, password);
            }
        });

    }

    private void signInUser(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, (task) ->
                        {
                            if (task.isSuccessful()) {
                                Log.d("authenticationDemo", "signInWithEmail:success");

                                Intent intent = new Intent(this, TopicActivity.class);
                                startActivity(intent);

                                Toast.makeText(LoginActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("authenticationDemo", "signInWithEmail:failure", task.getException());

                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    private void registerUser(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, (task) ->
                        {
                            if (task.isSuccessful()) {
                                Log.d("authenticationDemo", "signInWithEmail:success");

                                Intent intent = new Intent(this, TopicActivity.class);
                                startActivity(intent);

                                Toast.makeText(LoginActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("authenticationDemo", "signInWithEmail:failure", task.getException());

                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    private void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}
