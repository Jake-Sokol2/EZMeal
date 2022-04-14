package com.example.ezmeal.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezmeal.MainActivity;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnToRegister;
    private FirebaseAuth mAuth;
    private String email = "";
    private String password = "";
    private static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        btnToRegister = (Button) findViewById(R.id.registerFinalButton);
        btnToRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ registerActivityStart();}
        });
    }

    public void registerActivityStart()
    {
        EditText emailText = findViewById(R.id.emailRegisterTextField);
        email = emailText.getText().toString();
        EditText passwordText = findViewById(R.id.passwordRegisterTextField);
        password = passwordText.getText().toString();
        if (!Objects.equals(email, "") && !Objects.equals(password, "")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                openActivityMain();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        } else {
            emailText.setError("Empty field");
            passwordText.setError("Empty field");
        }
    }

    public void openActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}