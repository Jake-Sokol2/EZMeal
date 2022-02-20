package com.example.ezmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    // todo: remove btnToMainActivityDev when Login is functional
    private Button btnToMainActivityDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnToMainActivityDev = (Button) findViewById(R.id.btnToMainActivityDev);
        btnToMainActivityDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });
    }

    public void openActivityMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}