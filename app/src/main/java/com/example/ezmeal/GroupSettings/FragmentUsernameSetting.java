package com.example.ezmeal.GroupSettings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;


public class FragmentUsernameSetting extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText username;
    Button updateUsername;
    TextView txtusername;
    FirebaseAuth mAuth;


    private void UsernameChange(String username) {
        CollectionReference washingtonRef = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();
// Set the "isCapital" field of the city 'DC'
        washingtonRef
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentID = document.getId();
                            washingtonRef.document(documentID).update("username", username)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("", "Successfully update username");
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("", "Failure");
                                }
                            });

                        }
                    }
                });

    }


    private void getUsername() {
        CollectionReference washingtonRef = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();
// Set the "isCapital" field of the city 'DC'
        washingtonRef
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.get("username") != null){
                                String user = "Username: ";
                                String retrieveUser = document.get("username").toString();
                                txtusername.setText(user + retrieveUser);
                            }


                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_username_setting, container, false);

        username = v.findViewById(R.id.usernameTextEdit);
        updateUsername = v.findViewById(R.id.usernameUpdateBtn);

        txtusername = v.findViewById(R.id.usernameTextView);
        getUsername();
        return v;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        updateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                UsernameChange(Username);
                txtusername.setText("Username: " + Username);
            }
        });
    }
}