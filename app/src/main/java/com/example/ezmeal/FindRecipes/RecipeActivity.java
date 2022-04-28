package com.example.ezmeal.FindRecipes;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.RecipeActivityViewPagerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeFragmentRecyclerAdapter;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.CategoryEntity;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.Rating;
import com.example.ezmeal.roomDatabase.Recipe;
import com.example.ezmeal.roomDatabase.TextRatings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RecipeActivity extends AppCompatActivity implements RateRecipeDialogInterface
{
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroupListsFragmentModel theModel = new GroupListsFragmentModel();
    private ArrayList<List<String>> nutritionList = new ArrayList<List<String>>();
    private GroupListsFragmentModel nutritionModel = new GroupListsFragmentModel();

    List<String> list = new ArrayList<String>();
    private List<String> chosenBubbles;
    private RecyclerView rvGroupList;
    private RecipeFragmentRecyclerAdapter adapter;
    List<String> nutritionListInner = new ArrayList<String>();
    private RecyclerView rvNutritionList;

    // storing these as global so they can be called in onStop(), saving database costs
    private Double countOfRatings;
    private Double totalRatingFirebase;
    private boolean highlyRated;

    private RateRecipeBubbleViewModel vmRateRecipeBubble;
    private RateRecipeViewModel vmRateRecipe;

    private RateRecipeDialogInterface rateRecipeInterface;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private CardView card1;
    private CardView card2;
    private CardView card3;

    ArrayList<String> categories;
    ArrayList<String> directions;
    ArrayList<String> nutrition;
    ArrayList<String> ingredients;
    String imageUrl;
    String title;

    EZMealDatabase sqlDb;

    private MotionLayout motionLayout;
    private NestedScrollView nestedScrollView;

    private FloatingActionButton btnAddToMyRecipes;
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

        rateRecipeInterface = this;

        Bundle extras = getIntent().getExtras();
        recipeId = extras.getString("id");

        ImageView imageRecipe = findViewById(R.id.imageRecipeImage);
        TextView txtRecipeTitle = findViewById(R.id.txtRecipeTitle);

        rateRecipe = findViewById(R.id.rateRecipe);

        vmRateRecipeBubble = new ViewModelProvider(this).get(RateRecipeBubbleViewModel.class);
        vmRateRecipe = new ViewModelProvider(this).get(RateRecipeViewModel.class);

        Observer<Float> ratingObserver = new Observer<Float>()
        {
            @Override
            public void onChanged(Float f)
            {
                if (f != null)
                {
                    rateRecipe.setRating(f);
                }
            }
        };
        vmRateRecipe.getStarRating().observe(this, ratingObserver);

        // Room database instance
        //ratingsDb = Room.databaseBuilder(getApplicationContext(), RatingsDatabase.class, "user")
        //        .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        sqlDb = Room.databaseBuilder(getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();


        // retrieve current rating for this recipe (if one exists for this user) from Room to display in bottom rating bar
        Rating userRating = sqlDb.testDao().getSpecificRatingObject(recipeId);

        rateRecipe.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser)
            {
                if (fromUser)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    RateRecipeBottomDialogFragment rateRecipeFrag = new RateRecipeBottomDialogFragment(value, rateRecipeInterface);
                    ft.setReorderingAllowed(true);

                    ft.add(rateRecipeFrag, "TAG");
                    ft.show(rateRecipeFrag);
                    ft.commit();
                }
            }
        });


        db = FirebaseFirestore.getInstance();

        // todo: RecipesRating
        CollectionReference dbRecipes = db.collection("RecipesRatingBigInt");


        tv1 = findViewById(R.id.textRating1);
        tv2 = findViewById(R.id.textRating2);
        tv3 = findViewById(R.id.textRating3);

        card1 = findViewById(R.id.cardRating1);
        card2 = findViewById(R.id.cardRating2);
        card3 = findViewById(R.id.cardRating3);

        float avgRating = 0f;

        // todo: RecipesRating
        db.collection("RecipesRatingBigInt").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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

                countOfRatings = task.getResult().getDouble("countRating");
                totalRatingFirebase = task.getResult().getDouble("rating");
                highlyRated = task.getResult().getBoolean("highlyRated");

                Double avgRating;

                if (countOfRatings != null)
                {
                    avgRating = totalRatingFirebase / countOfRatings;
                }
                else
                {
                    countOfRatings = 0.0;
                    avgRating = 0.0;
                }

                RatingBar rbRecipeIndicator = findViewById(R.id.rbRecipeIndicator);

                if (Double.isNaN(avgRating))
                {
                    rbRecipeIndicator.setVisibility(View.INVISIBLE);
                    avgRating = 0.0;
                }
                else
                {
                    float avgRatingFloat = avgRating.floatValue();
                    rbRecipeIndicator.setRating(avgRatingFloat);
                }



                Integer totalRating = countOfRatings.intValue();

                Map<String, Long> firebaseTextRatingsMap = (Map<String, Long>) task.getResult().get("textRatings");

                List<CardView> textRatingCardViews = new ArrayList<CardView>();//(Arrays.asList(findViewById(R.id.cardRating1), findViewById(R.id.cardRating2), findViewById(R.id.cardRating3)));
                List<TextView> textRatingTextViews = new ArrayList<TextView>();//(Arrays.asList(findViewById(R.id.textRating1), findViewById(R.id.textRating2), findViewById(R.id.textRating3)));

                List<CardView> textRatingCardViewsInvisible = new ArrayList<CardView>();
                List<TextView> textRatingTextViewsInvisible = new ArrayList<TextView>();

                List<String> finalRatingList = new ArrayList<String>();

                if (firebaseTextRatingsMap != null)
                {
                    // sort the list in reverse order to get top 3 ratings (we only display the top three bubbles)
                    Map<String, Long> reverseSortedByNumberOfRatings = new TreeMap<>(Collections.reverseOrder());
                    reverseSortedByNumberOfRatings.putAll(firebaseTextRatingsMap);


                    Iterator<Map.Entry<String, Long>> iterator = reverseSortedByNumberOfRatings.entrySet().iterator();

                    finalRatingList = sortTextRatingsByLength(iterator);

                    if (finalRatingList.size() >= 3)
                    {
                        textRatingCardViews = new ArrayList<CardView>
                                (Arrays.asList(findViewById(R.id.cardRating1), findViewById(R.id.cardRating2), findViewById(R.id.cardRating3)));
                        textRatingTextViews = new ArrayList<TextView>
                                (Arrays.asList(findViewById(R.id.textRating1), findViewById(R.id.textRating2), findViewById(R.id.textRating3)));

                        updateLessThanThreeTextRatings(textRatingCardViews, textRatingTextViews, finalRatingList);

                        textRatingCardViewsInvisible = new ArrayList<CardView>
                                (Arrays.asList(findViewById(R.id.cardRating4), findViewById(R.id.cardRating5)));
                        textRatingTextViewsInvisible = new ArrayList<TextView>
                                (Arrays.asList(findViewById(R.id.textRating4), findViewById(R.id.textRating5)));

                    }
                    else
                    {
                        textRatingCardViews = new ArrayList<CardView>(Arrays.asList(findViewById(R.id.cardRating4), findViewById(R.id.cardRating5)));
                        textRatingTextViews = new ArrayList<TextView>(Arrays.asList(findViewById(R.id.textRating4), findViewById(R.id.textRating5)));

                        textRatingCardViewsInvisible = new ArrayList<CardView>
                                (Arrays.asList(findViewById(R.id.cardRating1), findViewById(R.id.cardRating2), findViewById(R.id.cardRating3)));
                        textRatingTextViewsInvisible = new ArrayList<TextView>
                                (Arrays.asList(findViewById(R.id.textRating1), findViewById(R.id.textRating2), findViewById(R.id.textRating3)));

                        updateLessThanThreeTextRatings(textRatingCardViews, textRatingTextViews, finalRatingList);
                    }

                    for (int i = 0; i < textRatingCardViewsInvisible.size(); i++)
                    {
                        textRatingCardViewsInvisible.get(i).setVisibility(View.INVISIBLE);
                        textRatingTextViewsInvisible.get(i).setVisibility(View.INVISIBLE);
                    }

                    /*for (int i = 0; i < 3; i++)
                    {
                        if (i < finalRatingList.size())
                        {
                            textRatingTextViews.get(i).setText(finalRatingList.get(i));
                        }
                        else
                        {
                            textRatingTextViews.get(i).setVisibility(View.INVISIBLE);
                            textRatingCardViews.get(i).setVisibility(View.INVISIBLE);
                        }
                    }*/
                }
                else
                {
                    // all cards and textviews are marked to be made invisible
                    textRatingCardViewsInvisible = new ArrayList<CardView>
                            (Arrays.asList(findViewById(R.id.cardRating1), findViewById(R.id.cardRating2), findViewById(R.id.cardRating3),
                                    findViewById(R.id.cardRating4), findViewById(R.id.cardRating5)));
                    textRatingTextViewsInvisible = new ArrayList<TextView>
                            (Arrays.asList(findViewById(R.id.textRating1), findViewById(R.id.textRating2), findViewById(R.id.textRating3),
                                    findViewById(R.id.textRating4), findViewById(R.id.textRating5)));
                }

                for (int i = 0; i < textRatingCardViewsInvisible.size(); i++)
                {
                    textRatingCardViewsInvisible.get(i).setVisibility(View.INVISIBLE);
                    textRatingTextViewsInvisible.get(i).setVisibility(View.INVISIBLE);
                }
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();

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


                // todo: RecipesRating
                db.collection("RecipesRatingBigInt").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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

    private void updateThreeTextRatings(List<CardView> textRatingCardViews, List<TextView> textRatingTextViews, List<String> finalRatingList)
    {
        for (int i = 0; i < 3; i++)
        {
            if (i < finalRatingList.size())
            {
                textRatingTextViews.get(i).setText(finalRatingList.get(i));
            }
            else
            {
                textRatingTextViews.get(i).setVisibility(View.INVISIBLE);
                //textRatingCardViews.get(i).clearAnimation();
                textRatingCardViews.get(i).setVisibility(View.INVISIBLE);
                //textRatingCardViews.get(i).setCardBackgroundColor(Color.parseColor("#000000"));
            }

            if (finalRatingList.size() < 3)
            {



                /*ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textRatingCardViews.get(i).getLayoutParams();
                layoutParams.topMargin = 100;

                ConstraintSet constraintSet = ml.getConstraintSet(R.id.start);
                constraintSet.connect();
*/
                //layoutParams.setMargins(0, intPixels, 0, 0);
                //textRatingCardViews.get(i).setLayoutParams(layoutParams);
            }
        }


    }

    private void updateLessThanThreeTextRatings(List<CardView> textRatingCardViews, List<TextView> textRatingTextViews, List<String> finalRatingList)
    {
        for (int i = 0; i < textRatingCardViews.size(); i++)
        {
            if (i < finalRatingList.size())
            {
                textRatingTextViews.get(i).setText(finalRatingList.get(i));
            }
            else
            {
                textRatingTextViews.get(i).setVisibility(View.INVISIBLE);
                //textRatingCardViews.get(i).clearAnimation();
                textRatingCardViews.get(i).setVisibility(View.INVISIBLE);
                //textRatingCardViews.get(i).setCardBackgroundColor(Color.parseColor("#000000"));
            }
        }


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

/*    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }*/



    private List<String> sortTextRatingsByLength(Iterator<Map.Entry<String, Long>> iterator)
    {
        List<String> finalRatingList = new ArrayList<String>();

        int i = 0;
        int largest = 0;
        String key = "";
        while (iterator.hasNext() && i < 3)
        {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() >= 1L)
            {
                key = entry.getKey().toString();

                if (key.length() > largest)
                {
                    largest = key.length();
                    finalRatingList.add(key);
                }
                else
                {
                    finalRatingList.add(0, key);
                }
            }

            i++;
        }

        return finalRatingList;
    }

    @Override
    public void onStop()
    {
        super.onStop();

/*        chosenBubbles = new ArrayList<String>();
        Observer<List<String>> textRatingObserver = new Observer<List<String>>()
        {
            @Override
            public void onChanged(List<String> s)
            {
                // user selected text ratings - clear out any old text ratings in Room, decrement the corresponding counters in Firebase, and increment the new counters in Firebase
                if (s.size() > 0)
                {
                    chosenBubbles = s;
                }
                // user previously had selected text ratings, but left a new review with no ratings.  Clear the old ratings in SQL and firebase
                if (s.size() == 0)
                {

                }

                for (int i = 0; i < s.size(); i++)
                {
                    Log.i("test7", s.get(i));
                }
            }
        };*/
        MutableLiveData<List<String>> test = vmRateRecipeBubble.getRatingBubbleList();
        chosenBubbles = test.getValue();

        // even though it is initialized in the ViewModel, getRatingBubbleList will return null if a value has not been set yet by the user
        if (chosenBubbles == null)
        {
            chosenBubbles = new ArrayList<String>();
        }

        // Room db insert/update expects 3 bubble selections when creating the record, so give it nulls for any missing ratings in case the user only selected 0-2 text ratings
        if (chosenBubbles.size() < 3)
        {
            for (int i = chosenBubbles.size(); i < 3; i++)
            {
                chosenBubbles.add(null);
            }
        }

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
            // todo: RecipesRating
            CollectionReference dbRecipes = db.collection("RecipesRatingBigInt");

            // if user has never left a review on this recipe before, increment number of ratings in firestore
            if (oldRating == 0)
            {
                dbRecipes.document(recipeId).update("countRating", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "numRating updated");
                    }
                });

                // add a new entry to Room so their rating displays correctly next time they return to the recipe
                Rating newRatingEntry = new Rating(recipeId, newRating, chosenBubbles);
                sqlDb.testDao().insert(newRatingEntry);

                // increment count of Ratings so onStop() knows how to calculate the correct final average rating after new rating is written to firebase
                countOfRatings++;
            }
            // if newRating is not 0, they changed their rating, so update existing Room entry
            else if (newRating != 0)
            {
                // update their existing Room entry with the correct new rating
                Rating newRatingEntry = new Rating(recipeId, newRating, chosenBubbles);

                // decrement counters in firebase for any old text rating's that the user may have chosen on their last review
                decrementTextRatingsInFirebase();

                sqlDb.testDao().updateRating(newRating, recipeId);
                sqlDb.testDao().updateTextRatings(chosenBubbles.get(0), chosenBubbles.get(1), chosenBubbles.get(2), recipeId);

            }

            // if user added or changed their rating, update the total rating score in firestore
            if (newRating != 0)
            {
                // increment counters in firebase for text ratings
                // using chosenBubbles global list
                incrementTextRatingsInFirebase();

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

            // new rating pushed Recipe above the highly rated threshold - notify firebase
            if ((highlyRated == false) && ((changeInRating + totalRatingFirebase) / countOfRatings) >= 4)
            {
                dbRecipes.document(recipeId).update("highlyRated", true).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "highlyRated updated");
                    }
                });
            }
            // new rating dropped Recipe beneath the highly rated threshold - notify firebase
            else if ((highlyRated) && ((changeInRating + totalRatingFirebase) / countOfRatings) < 4)
            {
                dbRecipes.document(recipeId).update("highlyRated", false).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "highlyRated updated");
                    }
                });
            }

            if (sqlDb.isOpen())
            {
                sqlDb.close();
            }
        }
    }

    private void decrementTextRatingsInFirebase()
    {
        TextRatings previousTextRatings = sqlDb.testDao().getSpecificTextRatings(recipeId);
        List<String> previousRatingsList = new ArrayList<>(Arrays.asList(previousTextRatings.getTextRating1(), previousTextRatings.getTextRating2(), previousTextRatings.getTextRating3()));

        for (int i = 0; i < previousRatingsList.size(); i++)
        {
            // if user actually saved a text rating last time
            if (previousRatingsList.get(i) != null)
            {
                // delete the rating in firebase
                // todo: RecipesRating
                db.collection("RecipesRatingBigInt").document(recipeId).update("textRatings." + previousRatingsList.get(i), FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "textRatings updated");
                    }
                });
            }
        }
    }

    private void incrementTextRatingsInFirebase()
    {
        //TextRatings previousTextRatings = sqlDb.testDao().getSpecificTextRatings(recipeId);
        //List<String> previousRatingsList = new ArrayList<>(Arrays.asList(previousTextRatings.getTextRating1(), previousTextRatings.getTextRating2(), previousTextRatings.getTextRating3()));

        for (int i = 0; i < chosenBubbles.size(); i++)
        {
            // if user actually saved a text rating last time
            if (chosenBubbles.get(i) != null)
            {
                // delete the rating in firebase
                // todo: RecipesRating
                db.collection("RecipesRatingBigInt").document(recipeId).update("textRatings." + chosenBubbles.get(i), FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.i("rating", "textRatings updated");
                    }
                });
            }
        }
    }

    @Override
    public void recipeCallback(float rating)
    {

    }
}

