package com.example.ezmeal.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    TextView forgotpass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AssetManager assetManager = getAssets();

        forgotpass = findViewById(R.id.forgotPasswordTextView);


        // click on forget password text
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });





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

        Button btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword("merge@email.com", "testmerge")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        btnToLogin = (Button) findViewById(R.id.loginButton);
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivityStart();
            }
        });

        btnToRegister = (Button) findViewById(R.id.registerButton);
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRegister();
            }
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

    public void openActivityRegister() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void loginActivityStart() {
        EditText emailText = findViewById(R.id.emailTextField);
        email = emailText.getText().toString();
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
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            //Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            emailText.setError("Field empty");
            passwordText.setError("Field empty");

        }
    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 20, 10, 20);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void beginRecovery (String email){
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this, "Done sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}


