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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnToRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String email = "";
    private String password = "";
    private String username = "";
    private static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        db = FirebaseFirestore.getInstance();


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
        EditText usernameText = findViewById(R.id.editTextUsername);
        username = usernameText.getText().toString();

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", email);


        if (!Objects.equals(email, "") && !Objects.equals(password, "")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("DEBUG", "createUserWithEmail:success");




                                db.collection("Users")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("DEBUG", "DocumentSnapshot written with ID: " + documentReference.getId());
                                                openActivityMain();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("DEBUG", "Error adding document", e);
                                            }
                                        });


                                //FirebaseUser user = mAuth.getCurrentUser();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("DEBUG", "createUserWithEmail:failure", task.getException());
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