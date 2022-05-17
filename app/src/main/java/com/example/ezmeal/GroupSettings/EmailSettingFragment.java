package com.example.ezmeal.GroupSettings;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ezmeal.Login.LoginActivity;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailSettingFragment extends Fragment{


    Button updateEmail;
    EditText newEmail;

    // Here we are going to change our email using firebase re-authentication
    private void updateEmail(String currentEmail, final String password) {



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, password); // Current Login Credentials

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Log.d("value", "User re-authenticated.");

                // Now change your email address \\
                //----------------Code for Changing Email Address----------\\
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(newEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("","Email Changed" + " Current Email is " + newEmail.getText().toString());
                            Toast.makeText(getContext(), "Successful Changed Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_email_setting, container, false);

        newEmail = v.findViewById(R.id.newEmail);

        final EditText oldEmail = v.findViewById(R.id.currentEmail);
        final EditText password = v.findViewById(R.id.passwordChange);
        updateEmail = v.findViewById(R.id.emailUpdateBtn);

        // click on this to change email
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail(oldEmail.getText().toString(), password.getText().toString());

            }
            });
        return v;
    }


}

