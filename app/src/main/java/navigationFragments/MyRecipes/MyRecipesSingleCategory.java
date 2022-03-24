package navigationFragments.MyRecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ezmeal.R;

public class MyRecipesSingleCategory extends AppCompatActivity {
    //private CardView mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_single_category);

        //mCard = findViewById(R.id.cardSingleCategory);

        //ViewCompat.setTransitionName(mCard, "a");

        String categoryName = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // retrieve category name from the Intent
            categoryName = extras.getString("category name");
        }
        
        //TextView txt = findViewById(R.id.txtMyRecipeTitle);
        //txt.setText("aaa");
        //loadItem();
    }
}