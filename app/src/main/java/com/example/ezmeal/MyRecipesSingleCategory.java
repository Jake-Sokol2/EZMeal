package com.example.ezmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.widget.TextView;

public class MyRecipesSingleCategory extends AppCompatActivity {
    //private CardView mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_single_category);

        //mCard = findViewById(R.id.cardSingleCategory);

        //ViewCompat.setTransitionName(mCard, "a");

        String categoryName = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // retrieve category name from the Intent
            categoryName = extras.getString("category name");
        }
        
        TextView txt = findViewById(R.id.txtMyRecipeTitle);
        txt.setText("aaa");
        //loadItem();
    }
}