package com.example.ezmeal.Login;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ezmeal.MainActivity;
import com.example.ezmeal.R;
import com.example.ezmeal.Registration.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {



    // todo: remove btnToMainActivityDev when Login is functional
    //private Button btnToMainActivityDev;
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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AssetManager assetManager = getAssets();

        /*ImageView img = findViewById(R.id.imageTest);

        try
        {
            InputStream inputStream = getAssets().open("cookies2.jpg");
            Drawable d = Drawable.createFromStream(inputStream, null);
            img.setImageDrawable(d);
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        /*
        btnToMainActivityDev = (Button) findViewById(R.id.btnToMainActivityDev);
        btnToMainActivityDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });
        */
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
        /*
        if(currentUser != null){
            openActivityMain();
        }
        */

    }


    public void openActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // kill activity so user cannot return to this screen without clicking logout button
        finish();
    }

    public void openActivityRegister(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void loginActivityStart(){
        EditText emailText = findViewById(R.id.emailTextField);
        email= emailText.getText().toString();
        EditText passwordText = findViewById(R.id.passwordTextField);
        password = passwordText.getText().toString();


        if (!Objects.equals(email, "") && !Objects.equals(password, "")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            // Was the sign in successful?
                            if (task.isSuccessful()) {
                                // Put successful log in code here...
                                Toast.makeText(LoginActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
                                openActivityMain();
                            } else {
                                // Put unsuccessful log in code here
                                Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(Exception e)
                {
                    e.printStackTrace();
                }
            });
        } else{
            //Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            emailText.setError("Field empty");
            passwordText.setError("Field empty");

        }
    }


}



