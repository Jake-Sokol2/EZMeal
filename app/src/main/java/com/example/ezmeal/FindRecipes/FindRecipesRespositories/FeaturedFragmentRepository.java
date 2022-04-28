package com.example.ezmeal.FindRecipes.FindRecipesRespositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.Test;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.EZMealDatabaseKotlin;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;
import com.example.ezmeal.roomDatabase.TestDaoKotlin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FeaturedFragmentRepository
{
    private FeaturedFragmentRoomRepository roomRepository;
    // fuck you github
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRecipes = db.collection("RecipesRatingBigInt");

    private Set<Integer> setOfUniqueHighlyRatedRecipes = new HashSet<>();
    private List<HorizontalRecipe> horizontalLists = new ArrayList<HorizontalRecipe>();
    private Application application;

    private interface SomeCallback {
        void onCallback(List<HorizontalRecipe> list);
    }

    public FeaturedFragmentRepository(@NonNull Application application)
    {
        roomRepository = new FeaturedFragmentRoomRepository(application);
        this.application = application;
    }

    public MutableLiveData<List<HorizontalRecipe>> getHorizontalLists(List<String> categoryList)
    {
        Random rand = new Random();

        // todo: stop hardcoding this
        int numRecipes = 10;

        int categoryListSize = categoryList.size();
        int numRecipesPerCategory = numRecipes / categoryListSize;

        MutableLiveData<List<HorizontalRecipe>> liveDataHorizontal = new MutableLiveData<>();

        List<HorizontalRecipe> recursedHorizontalList = horizontalLists;
            readDataCallback(new SomeCallback()
            {
                @Override
                public void onCallback(List<HorizontalRecipe> list)
                {
                    liveDataHorizontal.setValue(list);
                }
            }, numRecipes, numRecipesPerCategory, categoryList, 0, recursedHorizontalList, 0);


        return liveDataHorizontal;
    }

    public void readDataCallback(SomeCallback someCallback, int numRecipes, int numRecipesPerCategory, List<String> categoryList, int numRetrieved, List<HorizontalRecipe> recursedHorizontalLists, int numRecursions)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

/*        int numLeftToRetrieve = numRecipes - numRetrieved;

        if (numLeftToRetrieve <= 0 || numRecursions >= 10)
        {
            someCallback.onCallback(recursedHorizontalLists);
        }
        else
        {
            if (numRecipesPerCategory > numLeftToRetrieve)
            {
                numRecipesPerCategory = numLeftToRetrieve;
            }
        }*/

        int finalNumRecipesPerCategory = numRecipesPerCategory;

        dbRecipes.whereArrayContains("categories", categoryList.get(0)).whereGreaterThan("recipeId", randomNum)
                .limit(numRecipesPerCategory).get().addOnCompleteListener(task ->
        {
            Log.i("queries", "queried outer");
            int numOfRetrievedRecipes = retrieveHorizontal(categoryList.get(0), "Popular Recipe", task);

            int numLeftToRetrieve = numRecipesPerCategory - numOfRetrievedRecipes;

            if (numLeftToRetrieve > 0)
            {
                dbRecipes.whereArrayContains("categories", categoryList.get(0)).whereLessThan("recipeId", randomNum).limit(numLeftToRetrieve).get()
                        .addOnCompleteListener(task1 ->
                        {
                            //int recursions = numRecursions + 1;
                            Log.i("queries", "queried inner");
                            retrieveHorizontal(categoryList.get(0), "Popular Recipe", task1);

                            // recurse
                            if (categoryList.size() > 1)
                            {
                                readData(finalNumRecipesPerCategory, categoryList, 1, categoryList.size());
                            }

                            someCallback.onCallback(horizontalLists);
                        });
            }
            else
            {
                if (categoryList.size() > 1)
                {
                    readData(finalNumRecipesPerCategory, categoryList, 1, categoryList.size());
                }

                someCallback.onCallback(horizontalLists);
            }
        });
    }

    public void readData(int numRecipesPerCategory, List<String> categoryList, int numRecursions, int numCategories)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

        if (numRecursions < numCategories)
        {
            int finalNumRecipesPerCategory = numRecipesPerCategory;
            dbRecipes.whereArrayContains("categories", categoryList.get(numRecursions)).whereGreaterThan("recipeId", randomNum)
                    .limit(numRecipesPerCategory).get().addOnCompleteListener(task ->
            {
                Log.i("queries", "queried outer");
                int numOfRetrievedRecipes = retrieveHorizontal(categoryList.get(numRecursions), "Popular Recipe", task);

                int numLeftToRetrieve = finalNumRecipesPerCategory - numOfRetrievedRecipes;

                if (numLeftToRetrieve > 0)
                {
                    dbRecipes.whereArrayContains("categories", categoryList.get(numRecursions)).whereLessThan("recipeId", randomNum).limit(numLeftToRetrieve).get()
                            .addOnCompleteListener(task1 ->
                            {
                                int recursions = numRecursions + 1;
                                Log.i("recursions", String.valueOf(recursions));
                                retrieveHorizontal(categoryList.get(numRecursions), "Popular Recipe", task1);

                                readData(numRecipesPerCategory, categoryList, recursions, numCategories);

                                //someCallback.onCallback(horizontalLists);
                                //liveDataHorizontal.setValue(horizontalLists);
                            });
                }
                else
                {
                    int recursions = numRecursions + 1;
                    Log.i("recursions", String.valueOf(recursions));
                    //retrieveHorizontal(category, "Popular Recipe", task1);

                    readData(numRecipesPerCategory, categoryList, recursions, numCategories);
                }
            });
        }
    }

    // returns number of recipes that were actually retrieved
    private int retrieveHorizontal(String category, String typeOfRecyclerItem, Task<QuerySnapshot> task)
    {
        // delete all existing featured recipes in SQL
        deleteAllRecycler2();

        int numRetrieved = 0;

        for (QueryDocumentSnapshot document : task.getResult())
        {
            String imageUrl = document.getString("imageUrl");
            String title = document.getString("title");
            String highRatedRecipeId = document.getId();
            Double highRatedRecipeIdDouble = document.getDouble("recipeId");
            Integer highRatedRecipeIdInt = highRatedRecipeIdDouble.intValue();

            Double countRating = document.getDouble("countRating");
            Double avgRating;
            if (countRating != null)
            {
                avgRating = document.getDouble("rating") / countRating;
            }
            else
            {
                countRating = 0.0;
                avgRating = 0.0;
            }

            if (Double.isNaN(avgRating))
            {
                avgRating = 0.0;
            }

            Integer totalRating = countRating.intValue();

            int sizeOfSet = setOfUniqueHighlyRatedRecipes.size();
            setOfUniqueHighlyRatedRecipes.add(highRatedRecipeIdInt);

            // the current recipe is not a duplicate - it may be added to the recyclerview
            if ((sizeOfSet != setOfUniqueHighlyRatedRecipes.size()))
            {
                numRetrieved = numRetrieved + 1;
                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, highRatedRecipeId, avgRating);
                // add this recipe to the most recently added vertical item
                horizontalLists.add(newRecipe);


                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2("RecommendedForYou", highRatedRecipeId, title, imageUrl,
                        avgRating, typeOfRecyclerItem, true, totalRating);


                insertRecycler2(recyclerRecipePopular2);
            }
        }

        return numRetrieved;
    }



    public void insertRecycler2(RecyclerRecipe2 recyclerRecipe2)
    {
        roomRepository.insertRecycler2(recyclerRecipe2);
    }

    public void deleteAllRecycler2()
    {
        roomRepository.deleteAllRecycler2();
    }
}