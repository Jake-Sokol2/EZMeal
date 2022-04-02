package com.example.ezmeal.FindRecipes;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.RecipeActivityViewPagerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeFragmentRecyclerAdapter;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.CategoryEntity;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.Rating;
import com.example.ezmeal.RoomDatabase.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeActivity extends AppCompatActivity
{
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroupListsFragmentModel theModel = new GroupListsFragmentModel();
    private ArrayList<List<String>> nutritionList = new ArrayList<List<String>>();
    private GroupListsFragmentModel nutritionModel = new GroupListsFragmentModel();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private RecipeFragmentRecyclerAdapter adapter;
    List<String> nutritionListInner = new ArrayList<String>();
    private RecyclerView rvNutritionList;

    ArrayList<String> categories;
    ArrayList<String> directions;
    ArrayList<String> nutrition;
    ArrayList<String> ingredients;
    String imageUrl;
    String title;

    EZMealDatabase sqlDb;

    private MotionLayout motionLayout;
    private NestedScrollView nestedScrollView;

    private Button btnAddToMyRecipes;
    public String recipeId;

    //public RatingsDatabase ratingsDb;

    ViewPager2 vpRecipe;
    TabLayout tabRecipe;
    RecipeActivityViewPagerAdapter vpAdapter;

    private RatingBar rateRecipe;
    float ratingValue;
    String tempRating;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_specific_recipe);

        Bundle extras = getIntent().getExtras();
        recipeId = extras.getString("id");

        ImageView imageRecipe = findViewById(R.id.imageRecipeImage);
        TextView txtRecipeTitle = findViewById(R.id.txtRecipeTitle);

        rateRecipe = findViewById(R.id.rateRecipe);

        // Room database instance
        //ratingsDb = Room.databaseBuilder(getApplicationContext(), RatingsDatabase.class, "user")
        //        .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        sqlDb = Room.databaseBuilder(getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();


        // retrieve current rating for this recipe (if one exists for this user) from Room to display in bottom rating bar
        Rating userRating = sqlDb.testDao().getSpecificRatingObject(recipeId);

        if (userRating == null)
        {
            Toast.makeText(getApplicationContext(), "NO RATING FOR THIS RECIPE", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Rating: " + userRating.getRating(), Toast.LENGTH_SHORT).show();
        }


        rateRecipe.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b)
            {
                Toast.makeText(getApplicationContext(), "Thank you for rating this recipe!", Toast.LENGTH_SHORT).show();
            }
        });

        // get rating that is currently in the bar
        // rateRecipe.getRating();


        db = FirebaseFirestore.getInstance();
        CollectionReference dbRecipes = db.collection("Recipes");

        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                Glide.with(getApplicationContext()).load(Uri.parse(task.getResult().getString("imageUrl"))).into(imageRecipe);
                txtRecipeTitle.setText(task.getResult().getString("title"));

                categories = (ArrayList<String>) task.getResult().get("categories");
                directions = (ArrayList<String>) task.getResult().get("directions");
                ingredients = (ArrayList<String>) task.getResult().get("ingredients");
                nutrition = (ArrayList<String>) task.getResult().get("nutrition");
                imageUrl = task.getResult().getString("imageUrl");
                title = task.getResult().getString("title");
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();

        // Parameters:
        //@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<String> directions,
        // ArrayList<String> nutrition, ArrayList<String> ingredients, String recipeId

        vpRecipe = findViewById(R.id.vpRecipe);
        tabRecipe = findViewById(R.id.tabRecipe);

        // todo: find out what this actually does... and if we need it or not
        vpRecipe.requestDisallowInterceptTouchEvent(true);

        vpAdapter = new RecipeActivityViewPagerAdapter(fragmentManager, getLifecycle(), directions, nutrition, ingredients, recipeId);
        vpRecipe.setAdapter(vpAdapter);



        btnAddToMyRecipes = findViewById(R.id.btnAddToMyRecipes);

        // if recipe already exists in user's My Recipes, hide the add recipe button
        if(sqlDb.testDao().isRecipeExists(recipeId))
        {
            btnAddToMyRecipes.setEnabled(false);
            btnAddToMyRecipes.setVisibility(View.GONE);
        }


        btnAddToMyRecipes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CollectionReference dbItems = db.collection("Items");
                //Item item = new Item(ingredients.get(i), null, email);

                // prevent user from adding same list of ingredients twice
                //.whereEqualTo(ingredients.get(i), null).get().addOnCompleteListener(


                db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        // find largest list between categories, directions, ingredients, and nutrition
                        int maxSize = Collections.max(Arrays.asList(categories.size(), directions.size(), ingredients.size(), nutrition.size()));

                        EZMealDatabase sqlDb = Room.databaseBuilder(getApplicationContext(), EZMealDatabase.class, "user")
                                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

                        // uncomment to nuke the users database
                        //sqlDb.testDao().BOOM();
                        //sqlDb.testDao().BOOOOOOOM();

                        Recipe newRecipe = new Recipe(imageUrl, title, recipeId);
                        sqlDb.testDao().insert(newRecipe);

                        // loop through and create a CategoryEntity instance for every category/direction/etc.  If you exceed one lists size, fill its entry with null
                        // this is really hacky, not sure if there's a better way to insert these lists given they are all of different sizes and doing it all in one loop
                        // would result in out of bound errors
                        for(int x = 0; x < maxSize; x++)
                        {
                            String cat;
                            String dir;
                            String ing;
                            String nut;

                            if (x < categories.size())
                            {
                                cat = categories.get(x);
                            }
                            else
                            {
                                cat = null;
                            }

                            if (x < directions.size())
                            {
                                dir = directions.get(x);
                            }
                            else
                            {
                                dir = null;
                            }

                            if (x < ingredients.size())
                            {
                                ing = ingredients.get(x);
                            }
                            else
                            {
                                ing = null;
                            }
                            if (x < nutrition.size())
                            {
                                nut = nutrition.get(x);
                            }
                            else
                            {
                                nut = null;
                            }

                            CategoryEntity item = new CategoryEntity(recipeId, cat, nut, dir, ing);
                            sqlDb.testDao().insertItem(item);
                        }

                        Toast.makeText(getApplicationContext(), "Recipe added!", Toast.LENGTH_SHORT).show();

                        // disable add recipe button so that user cannot attempt to add same recipe twice
                        btnAddToMyRecipes.setEnabled(false);
                        btnAddToMyRecipes.setVisibility(View.GONE);

                        //mAuth = FirebaseAuth.getInstance();
                        //FirebaseUser mCurrentUser = mAuth.getCurrentUser();
                        //String email = mCurrentUser.getEmail();

                        //UserRecipe savedRecipe = new UserRecipe(categories, directions, ingredients, nutrition, imageUrl, title, recipeId);


                        //CollectionReference dbRecipes = db.collection("UserRecipes");

                        //ArrayList<UserRecipe> userRecipeList = new ArrayList<UserRecipe>();
                        //userRecipeList.add(savedRecipe);


                        //getContext().deleteDatabase("EZMealDatabase");

                        /*db.collection("Recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    Log.i("retrieve", document.getId() + "=> " + document.getData());
                                    String title = document.getString("title");
                                    Uri uri = Uri.parse(document.getString("imageUrl"));

                                    //findRecipesModel.addItem(title, uri);
                                    //findRecipesAdapter.notifyDataSetChanged();
                                    //recipeId.add(document.getId());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                            }
                        });*/
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
            }
        });

        /*
        String recipeId = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            // retrieve category name from the Intent
            recipeId = extras.getString("id");
        }
         */
        /*
        if (savedInstanceState == null)
        {
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragmentContainerView, RecipeInstructionsFragment.class, null)
                    .commit();
        }
        */

        nestedScrollView = findViewById(R.id.nestedScrollNutrition);

        TextView txt = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_name, null);

        new TabLayoutMediator(tabRecipe, vpRecipe, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                        //tab.setText("Ingredients");
                        TextView txtIngredients = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_name, null);
                        txtIngredients.setText("Ingredients");
                        nestedScrollView = findViewById(R.id.nestedScrollIngredients);
                        tab.setCustomView(txtIngredients);
                        break;
                    case 1:
                        //tab.setText("Directions");
                        TextView txtDirections = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_name, null);
                        txtDirections.setText("Directions");
                        nestedScrollView = findViewById(R.id.nestedScrollDirections);
                        tab.setCustomView(txtDirections);
                        break;
                    case 2:
                        //tab.setText("Nutrition");
                        TextView txtNutrition = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_name, null);
                        txtNutrition.setText("Nutrition");
                        nestedScrollView = findViewById(R.id.nestedScrollNutrition);
                        tab.setCustomView(txtNutrition);
                        break;
                }
            }
        }).attach();

    }

    public void rateApp()
    {

    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_specific_recipe, container, false);



        // If scrollview is at the top (not scrolled), allow MotionLayout transition.  Otherwise, disable MotionLayout transition
        // prevents the user from pulling the image up and down while the scrollview is scrolled.  Image should only move when scrollview is at the top
        *//*nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (nestedScrollView.getScrollY() == 0)
                {
                    motionLayout.getTransition(R.id.recipeTransition).setEnable(true);
                }
                else
                {
                    motionLayout.getTransition(R.id.recipeTransition).setEnable(false);
                }
            }
        });*//*

        return view;
    }*/

/*    public class InsertAsynchTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            return null;
        }
    }*/

        /*@Override
    public void onResume()
    {
        super.onResume();
    }*/

/*    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }*/

    @Override
    public void onStop()
    {
        super.onStop();

        //RatingsDatabase ratingsDb = Room.databaseBuilder(getApplicationContext(), RatingsDatabase.class, "user")
        //        .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // get change in rating compared to user's previous rating (if one exists)
        float oldRating = sqlDb.testDao().getSpecificRating(recipeId);
        float newRating = rateRecipe.getRating();
        float changeInRating = newRating - oldRating;

        Rating r1 = sqlDb.testDao().getSpecificRatingObject(recipeId);
        Rating r2 = sqlDb.testDao().getAllTheThings();
        // if rating is different than it was when the user entered the recipe, they added / updated their rating, so update both database entries
        if (changeInRating != 0)
        {
            db = FirebaseFirestore.getInstance();
            CollectionReference dbRecipes = db.collection("Recipes");

            // if user has never left a review on this recipe before, increment number of ratings in firestore
            if (oldRating == 0)
            {
                dbRecipes.document(recipeId).update("numRating", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "numRating updated");
                    }
                });

                // add a new entry to Room so their rating displays correctly next time they return to the recipe
                Rating newRatingEntry = new Rating(recipeId, newRating);
                sqlDb.testDao().insert(newRatingEntry);
            }
            // if newRating is not 0, they added a new rating, so update existing Room entry
            else if (newRating != 0)
            {
                // update their existing Room entry with the correct new rating
                Rating newRatingEntry = new Rating(recipeId, newRating);
                sqlDb.testDao().updateRating(newRating, recipeId);
            }

            // if user added or changed their rating, update the total rating score in firestore
            if (newRating != 0)
            {
                // increment or decrement (depending on if changeInRating is positive or negative) Firestore rating by new value
                dbRecipes.document(recipeId).update("rating", FieldValue.increment(changeInRating)).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "rating updated");
                    }
                });
            }
                // otherwise do nothing, as they neither had a rating to begin with nor added a new one

            if (sqlDb.isOpen())
            {
                sqlDb.close();
            }



            // update Room with new value
            //sqlDb.testDao().updateRating(rateRecipe.getRating(), recipeId);

            /*            {
             *//*@Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        //Log.i("retrieve", document.getId() + "=> " + document.getData());
                        //String title = document.getString("title");
                        //String imageUrl = document.getString("imageUrl");
                        categories.add(document.getString("category"));
                        findRecipesFragmentModel.addItem(document.getString("category"), false);
                        //recipeId.add(document.getId());
                    }

                    horizontalAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });*//*
        }*/
        }

    }
}

