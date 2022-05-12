package com.example.ezmeal.FindRecipes;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.ezmeal.GroupLists.Model.Item;
import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeIngredientsFragmentRecyclerAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.RecipeIngredientsFragmentModel;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.Rating;
import com.example.ezmeal.roomDatabase.TextRatings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeRatingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeRatingsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private List<String> ingredientList = new List<String>;
    private RecipeIngredientsFragmentModel recipeIngredientsFragmentModel = new RecipeIngredientsFragmentModel();
    private RecyclerView rvIngredients;
    private RecipeIngredientsFragmentRecyclerAdapter recipeIngredientsFragmentRecyclerAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button btnAddToList;

    public List<String> ingredients;
    public boolean itemAdded = false;
    public ArrayList<String> ingredientsArray;
    private RatingBar rateRecipe;

    private EZMealDatabase sqlDb;

    private List<String> chosenBubbles;
    private Double countOfRatings;
    private Double totalRatingFirebase;
    private boolean highlyRated;
    private String recipeId;

    private RateRecipeBubbleViewModel vmRateRecipeBubble;
    private RateRecipeViewModel vmRateRecipe;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeRatingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeInstructionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeRatingsFragment newInstance(String param1, String param2) {
        RecipeRatingsFragment fragment = new RecipeRatingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_ratings, container, false);

        vmRateRecipeBubble = new ViewModelProvider(requireActivity()).get(RateRecipeBubbleViewModel.class);
        vmRateRecipe = new ViewModelProvider(requireActivity()).get(RateRecipeViewModel.class);

        Bundle extras = getArguments();
        recipeId = extras.getString("id");

        rateRecipe = view.findViewById(R.id.rateRecipe2);

        sqlDb = Room.databaseBuilder(requireContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
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

                //RatingBar rbRecipeIndicator = findViewById(R.id.rbRecipeIndicator);

                if (Double.isNaN(avgRating))
                {
                    //rbRecipeIndicator.setVisibility(View.INVISIBLE);
                    avgRating = 0.0;
                }
                else
                {
                    float avgRatingFloat = avgRating.floatValue();
                    //rbRecipeIndicator.setRating(avgRatingFloat);
                }
            }
        });

        rateRecipe.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser)
            {
                if (fromUser)
                {
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    RateRecipeBottomDialogFragment rateRecipeFrag = new RateRecipeBottomDialogFragment(value);
                    ft.setReorderingAllowed(true);

                    ft.add(rateRecipeFrag, "TAG");
                    ft.show(rateRecipeFrag);
                    ft.commit();
                }
            }
        });

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
        vmRateRecipe.getStarRating().observe(requireActivity(), ratingObserver);

        return view;
    }

    @Override
    public void onStop()
    {
        super.onStop();

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
            CollectionReference dbRecipes = db.collection("Recipes");

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
                db.collection("Recipes").document(recipeId).update("textRatings." + previousRatingsList.get(i), FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>()
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
                db.collection("Recipes").document(recipeId).update("textRatings." + chosenBubbles.get(i), FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>()
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
}
