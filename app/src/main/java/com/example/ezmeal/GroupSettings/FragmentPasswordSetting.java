package com.example.ezmeal.GroupSettings;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.FirebaseAuth;

public class FragmentPasswordSetting extends Fragment {
    private FirebaseAuth mAuth;


    private void beginRecovery (String email){
        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Log.i("", "Password changed");
                    Toast.makeText(getContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("", "Re-enter Email");
                    Toast.makeText(getContext(), "Re-enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password_setting, container, false);

        final EditText Email = v.findViewById(R.id.passChangeEmail);
        Button updatePassword = v.findViewById(R.id.uPasswordBtn);

        mAuth = FirebaseAuth.getInstance();

        // click on this to change email
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                beginRecovery(email);

            }
        });
        return v;
    }

}