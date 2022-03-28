package com.example.ezmeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {



    // todo: remove btnToMainActivityDev when Login is functional
    private Button btnToMainActivityDev;
    private Button btnToLogin;
    private Button btnToRegister;
    private FirebaseAuth mAuth;
    private String email = "";
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        btnToMainActivityDev = (Button) findViewById(R.id.btnToMainActivityDev);
        btnToMainActivityDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });

        btnToLogin = (Button) findViewById(R.id.loginButton);
        btnToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loginActivityStart();
            }
        });

        btnToRegister = (Button) findViewById(R.id.registerButton);
        btnToRegister.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) { openActivityRegister();}
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openActivityMain();
        }
    }


    public void openActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // kill activity so user cannot return to this screen without clicking logout button
        finish();
    }

    public void openActivityRegister(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void loginActivityStart(){
        EditText emailText = findViewById(R.id.emailTextField);
        email= emailText.getText().toString();
        EditText passwordText = findViewById(R.id.passwordTextField);
        password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {@Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Was the sign in successful?
                    if (task.isSuccessful()) {
                        // Put successful log in code here...
                        Toast.makeText(Login.this, "Login success.", Toast.LENGTH_SHORT).show();
                        openActivityMain();
                    } else {
                        // Put unsuccessful log in code here
                        Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                });
    }


}



