package com.example.ezmeal.FindRecipes.FindRecipesRespositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.RetrievedRecipeLists;
import com.example.ezmeal.FindRecipes.FindRecipesModels.VerticalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.CategoryFragmentViewModel;
import com.example.ezmeal.roomDatabase.Recipe;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CategoryFragmentRepository
{
    private CategoryFragmentRoomRepository roomRepository;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRecipes = db.collection("RecipesRatingBigInt");

    private int numOfRetrievedHighRatedRecipes = 0;
    private Set<Integer> setOfUniqueHighlyRatedRecipes = new HashSet<>();
    private List<HorizontalRecipe> horizontalLists = new ArrayList<HorizontalRecipe>();
    private Application application;

    private RetrievedRecipeLists recipeLists;

    private int numRemainingRecipes;
    private int startRecipeId;
    private int endRecipeId;

    private boolean pleaseWork = false;

    public MutableLiveData<RetrievedRecipeLists> returnLists = new MutableLiveData<>();

    private CountDownLatch c;

    private interface HorizontalCallback
    {
        void onCallback();
    }

    private interface RecipeCallback
    {
        //void onCallback(List<VerticalRecipe> verticalList, List<HorizontalRecipe> horizontalList, int startId, int endId);
        void onCallback(RetrievedRecipeLists retrievedRecipeLists);
    }

    private interface RecipeLessThanCallback
    {
        void onCallback(List<VerticalRecipe> verticalList, List<HorizontalRecipe> horizontalList, int startId);
    }

    public CategoryFragmentRepository(@NonNull Application application)
    {
        roomRepository = new CategoryFragmentRoomRepository(application);
        this.application = application;
    }

    public void setDataOther(String category)
    {
        recipeLists = new RetrievedRecipeLists();
        //MutableLiveData<RetrievedRecipeLists> returnLists = new MutableLiveData<>();

        Random rand = new Random();
        recipeLists.setRandomQueryId(rand.nextInt(Integer.MAX_VALUE - 2));

        // todo: stop hardcoding this
        int numRecipes = 10;
        //String category = "Cookies";

        roomRepository.deleteFromCategory2SpecificCategory(category);
        roomRepository.deleteFromRecyclerRecipe2SpecificCategory(category);

        MutableLiveData<List<HorizontalRecipe>> liveDataHorizontal = new MutableLiveData<>();


        // query 20 vertical recipes
        recipeLists.setNumVerticalToQuery(20);
        recipeLists.setNumHorizontalToQuery(10);

        // total number remaining
        recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumVerticalToQuery());

        //sqlDb.testDao().insertRecyclerRecipe2(recipe);

        readRecipes(new RecipeCallback()
        {
            @Override
            public void onCallback(RetrievedRecipeLists retrievedRecipeLists)
            {
                recipeLists = retrievedRecipeLists;
                Log.i("a", "a");
                returnLists.setValue(recipeLists);
            }
        }, category, recipeLists);
    }

    public MutableLiveData<RetrievedRecipeLists> getDataOther()
    {
        return returnLists;
    }

    public void readRecipes(RecipeCallback callback, String category, RetrievedRecipeLists recipeLists)
    {
            int numRecipesToQuery;
            int recipeSearchStartId;

            // try to query only half the recipes the first time to reduce repeating patterns in random queries
            // the rest will be queried later
            if (recipeLists.getNumVerticalToQuery() == recipeLists.getNumRemainingVerticalRecipes())
            {
                numRecipesToQuery = recipeLists.getNumVerticalToQuery() / 2;
                recipeSearchStartId = recipeLists.getRandomQueryId();
            }
            // if this isn't the first time through, just query the rest of the remaining recipes starting where the first query ended
            // doing this guarantees that we will find all of the recipes we need
            else
            {
                numRecipesToQuery = recipeLists.getNumRemainingVerticalRecipes();
                recipeSearchStartId = recipeLists.getVerticalEndId();
            }

            dbRecipes.whereArrayContains("categories", category).whereGreaterThanOrEqualTo("recipeId", recipeSearchStartId).orderBy("recipeId").limit(numRecipesToQuery).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            List<VerticalRecipe> verticalList = new ArrayList<>();
                            List<HorizontalRecipe> horizontalList = new ArrayList<>();
                            List<RecyclerRecipe2> recyclerRecipe2List = new ArrayList<>();
                            List<RecyclerRecipe2> horizontalRecyclerRecipe2List = new ArrayList<>();

                            int startId = 0;
                            int endId = 0;
                            int i = 0;

                            // todo: change for loop format, enhanced for isn't right for this
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                    double recipeIdDouble = document.getDouble("recipeId");
                                    int recipeIdInt = (int) recipeIdDouble;

                                    if (i == 0)
                                    {
                                        // keep track of first recipeId for later queries
                                        startId = recipeIdInt;
                                    }
                                    else
                                    {
                                        // keep track of last recipeId for later queries
                                        endId = recipeIdInt;
                                    }

                                    String imageUrl = document.getString("imageUrl");
                                    String title = document.getString("title");
                                    String recipeIdString = document.getId();
                                    boolean highlyRated = document.getBoolean("highlyRated");

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

                                    int sizeOfSet = recipeLists.getSetOfUniqueVerticalRecipes().size();
                                    recipeLists.addToSetOfUniqueVerticalRecipes(recipeIdInt);

                                    // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                    if (recipeLists.getSetOfUniqueVerticalRecipes().size() != sizeOfSet)
                                    {
                                        // only add recipe to the vertical recycler if its not highly rated, we've already filled the horizontal with the max number of recipes,
                                        // or we aren't in our first query (accounting for high rated recipes in the third query would force us to do recursion to ensure we get enough vertical recipes)

                                        if ((!highlyRated) || ((recipeLists.getHorizontalList().size()) >= recipeLists.getNumHorizontalToQuery())) //(viewModel.getNumOfRetrievedHighRatedRecipes() >= (halfMaxNumberOfHighRatedRecipes * 2)))
                                        {
                                            recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumRemainingVerticalRecipes() - 1);

                                            RecyclerRecipe2 recyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl, avgRating, "vertical", false, totalRating);
                                            recyclerRecipe2List.add(recyclerRecipe2);

                                            VerticalRecipe newRecipe = new VerticalRecipe(title, imageUrl, recipeIdString, avgRating, totalRating);
                                            verticalList.add(newRecipe);

                                            ///verticalRecipeIdList.add(recipeIdString);

                                            // todo: may need to uncomment and convert this for onClick
                                            //recipeId.add(recipeIdString);
                                        }
                                        // add to horizontal highly rated list instead of vertical recyclerview
                                        else
                                        {
                                            //Set<Integer> setOfUniqueHighRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();

                                            int sizeOfHighRatedSetBefore = recipeLists.getSetOfUniqueHorizontalRecipes().size();
                                            recipeLists.addToSetOfUniqueHorizontalRecipes(recipeIdInt);

                                            //viewModel.addToSetOfUniqueHighRatedRecipes(recipeIdInt);
                                            //setOfUniqueHighRatedRecipes.add(recipeIdInt);

                                            // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                            if (sizeOfHighRatedSetBefore != recipeLists.getSetOfUniqueHorizontalRecipes().size())
                                            {
                                                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);
                                                // todo: may need to uncomment and convert, adding new private member to the class
                                                //numOfRetrievedHighRatedRecipes++;

                                                //highRatedTitles.add(title);
                                                //highRatedImages.add(imageUrl);
                                                //highRatedRecipeIdList.add(recipeIdString);
                                                //highRatedRatings.add(avgRating);

                                                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, recipeIdString, avgRating);
                                                horizontalList.add(newRecipe);
                                                //horizontalLists.get(horizontalLists.size() - 1).add(newRecipe);

                                                ///horizontalRecipeIdList.add(recipeIdString);

                                                RecyclerRecipe2 horizontalRecyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl,
                                                        avgRating, "Popular Recipe", true, totalRating);
                                                horizontalRecyclerRecipe2List.add(horizontalRecyclerRecipe2);
                                                //sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
                                            }
                                        }
                                    }

                                    i++;
                                }

                            //recipeLists.appendVerticalList(verticalList);
                            //recipeLists.appendHorizontalList(horizontalList);
                            //recipeLists.setVerticalStartId(startId);
                            //recipeLists.setVerticalEndId(endId);

                            // todo: uncomment
                            //callback.onCallback(verticalList, horizontalList, startId, endId);

                            recipeLists.setVerticalStartId(startId);
                            recipeLists.setVerticalEndId(endId);

                            recipeLists.appendVerticalList(verticalList);
                            recipeLists.appendHorizontalList(horizontalList);

                            //recipeLists.setNumRemainingVerticalRecipes(verticalList.size() - recipeLists.getNumRemainingVerticalRecipes());

                            // try to query all remaining vertical recipes in the opposite direction.  This could still fail to return all vertical recipes
                            dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", recipeLists.getRandomQueryId()).orderBy("recipeId").limit(recipeLists.getNumRemainingVerticalRecipes()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                        {
                                            //int numRetrievedRecipes = numOfRetrievedRecipes; //viewModel.getNumOfRetrievedRecipes();

                                            List<VerticalRecipe> verticalList = new ArrayList<>();
                                            List<HorizontalRecipe> horizontalList = new ArrayList<>();
                                            int startId = 0;
                                            //int endId = 0;

                                            // todo: change for loop format, enhanced for isn't right for this
                                            for (QueryDocumentSnapshot document : task.getResult())
                                            {
                                                double recipeIdDouble = document.getDouble("recipeId");
                                                int recipeIdInt = (int) recipeIdDouble;

                                                // here, startId refers to the entire query's start.  It is the left bound of all searched recipeId's
                                                startId = recipeIdInt;

                                                String imageUrl = document.getString("imageUrl");
                                                String title = document.getString("title");
                                                String recipeIdString = document.getId();
                                                boolean highlyRated = document.getBoolean("highlyRated");

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

                                                //Set<Integer> setOfUniqueRecipes = viewModel.getSetOfUniqueRecipes();

                                                int sizeOfSet = recipeLists.getSetOfUniqueVerticalRecipes().size();
                                                recipeLists.addToSetOfUniqueVerticalRecipes(recipeIdInt);

                                                // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                                if (recipeLists.getSetOfUniqueVerticalRecipes().size() != sizeOfSet)
                                                {
                                                    // only add recipe to the vertical recycler if its not highly rated or we've already filled the horizontal with the max number of recipes
                                                    if ((!highlyRated) || ((recipeLists.getHorizontalList().size()) >= recipeLists.getNumHorizontalToQuery())) //(viewModel.getNumOfRetrievedHighRatedRecipes() >= (halfMaxNumberOfHighRatedRecipes * 2)))
                                                    {
                                                        //viewModel.incrementNumOfRetrievedRecipesBy(1);
                                                        recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumRemainingVerticalRecipes() - 1);

                                                        //ratings.add(avgRating);
                                                        //totalRatingsCountList.add(totalRating);

                                                        RecyclerRecipe2 recyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl, avgRating, "vertical", false, totalRating);
                                                        //recyclerRecipeList2.add(recyclerRecipe2);
                                                        recyclerRecipe2List.add(recyclerRecipe2);
                                                        //categoryFragmentModel.addItem(title, imageUrl, avgRating, totalRating);

                                                        VerticalRecipe newRecipe = new VerticalRecipe(title, imageUrl, recipeIdString, avgRating, totalRating);
                                                        verticalList.add(newRecipe);

                                                        ///verticalRecipeIdList.add(recipeIdString);
                                                        //verticalRecipes.add(newRecipe);

                                                        // todo: may need to uncomment and convert this for onClick
                                                        //recipeId.add(recipeIdString);
                                                    }
                                                    // add to horizontal highly rated list instead of vertical recyclerview
                                                    else
                                                    {
                                                        //Set<Integer> setOfUniqueHighRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();

                                                        int sizeOfHighRatedSetBefore = recipeLists.getSetOfUniqueHorizontalRecipes().size();
                                                        recipeLists.addToSetOfUniqueHorizontalRecipes(recipeIdInt);
                                                        //viewModel.addToSetOfUniqueHighRatedRecipes(recipeIdInt);
                                                        //setOfUniqueHighRatedRecipes.add(recipeIdInt);

                                                        // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                                        if (sizeOfHighRatedSetBefore != recipeLists.getSetOfUniqueHorizontalRecipes().size())
                                                        {
                                                            //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);
                                                            // todo: may need to uncomment and convert, adding new private member to the class
                                                            //numOfRetrievedHighRatedRecipes++;

                                                            //highRatedTitles.add(title);
                                                            //highRatedImages.add(imageUrl);
                                                            //highRatedRecipeIdList.add(recipeIdString);
                                                            //highRatedRatings.add(avgRating);

                                                            HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, recipeIdString, avgRating);
                                                            horizontalList.add(newRecipe);

                                                            ///horizontalRecipeIdList.add(recipeIdString);
                                                            //horizontalLists.get(horizontalLists.size() - 1).add(newRecipe);

                                                            RecyclerRecipe2 horizontalRecyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl,
                                                                    avgRating, "Popular Recipe", true, totalRating);
                                                            horizontalRecyclerRecipe2List.add(horizontalRecyclerRecipe2);
                                                            //sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
                                                        }
                                                    }
                                                }
                                            }

                                            recipeLists.setVerticalStartId(startId);

                                            recipeLists.appendVerticalList(verticalList);
                                            recipeLists.appendHorizontalList(horizontalList);

                                            //recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumRemainingVerticalRecipes() - verticalList.size());


                                            // query the rest of the vertical recipes if necessary
                                            if ((recipeLists.getNumRemainingVerticalRecipes() > 0) && (recipeLists.getNumRemainingVerticalRecipes() < recipeLists.getNumVerticalToQuery()))
                                            {
                                                int numRecipesToQuery;
                                                int recipeSearchStartId;

                                                // try to query only half the recipes the first time to reduce repeating patterns in random queries
                                                // the rest will be queried later
                                                if (recipeLists.getNumVerticalToQuery() == recipeLists.getNumRemainingVerticalRecipes())
                                                {
                                                    numRecipesToQuery = recipeLists.getNumVerticalToQuery() / 2;
                                                    recipeSearchStartId = recipeLists.getRandomQueryId();
                                                }
                                                // if this isn't the first time through, just query the rest of the remaining recipes starting where the first query ended
                                                // doing this guarantees that we will find all of the recipes we need
                                                else
                                                {
                                                    numRecipesToQuery = recipeLists.getNumRemainingVerticalRecipes();
                                                    recipeSearchStartId = recipeLists.getVerticalEndId();
                                                }

                                                dbRecipes.whereArrayContains("categories", category).whereGreaterThanOrEqualTo("recipeId", recipeSearchStartId).orderBy("recipeId").limit(numRecipesToQuery).get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                            {
                                                                //int numRetrievedRecipes = numOfRetrievedRecipes; //viewModel.getNumOfRetrievedRecipes();

                                                                List<VerticalRecipe> verticalList = new ArrayList<>();
                                                                List<HorizontalRecipe> horizontalList = new ArrayList<>();
                                                                int startId = 0;
                                                                int endId = 0;
                                                                int i = 0;

                                                                // todo: change for loop format, enhanced for isn't right for this
                                                                for (QueryDocumentSnapshot document : task.getResult())
                                                                {
                                                                    double recipeIdDouble = document.getDouble("recipeId");
                                                                    int recipeIdInt = (int) recipeIdDouble;

                                                                    if (i == 0)
                                                                    {
                                                                        // keep track of first recipeId for later queries
                                                                        startId = recipeIdInt;
                                                                    }
                                                                    else
                                                                    {
                                                                        // keep track of last recipeId for later queries
                                                                        endId = recipeIdInt;
                                                                    }

                                                                    String imageUrl = document.getString("imageUrl");
                                                                    String title = document.getString("title");
                                                                    String recipeIdString = document.getId();
                                                                    boolean highlyRated = document.getBoolean("highlyRated");

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

                                                                    //Set<Integer> setOfUniqueRecipes = viewModel.getSetOfUniqueRecipes();

                                                                    int sizeOfSet = recipeLists.getSetOfUniqueVerticalRecipes().size();
                                                                    recipeLists.addToSetOfUniqueVerticalRecipes(recipeIdInt);

                                                                    // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                                                    if (recipeLists.getSetOfUniqueVerticalRecipes().size() != sizeOfSet)
                                                                    {
                                                                        // only add recipe to the vertical recycler if its not highly rated, we've already filled the horizontal with the max number of recipes,
                                                                        // or we aren't in our first query (accounting for high rated recipes in the third query would force us to do recursion to ensure we get enough vertical recipes)
                                                                        if ((!highlyRated) || ((recipeLists.getHorizontalList().size()) >= recipeLists.getNumHorizontalToQuery()) || (numRecipesToQuery <= 0)) //(viewModel.getNumOfRetrievedHighRatedRecipes() >= (halfMaxNumberOfHighRatedRecipes * 2)))
                                                                        {
                                                                            //viewModel.incrementNumOfRetrievedRecipesBy(1);
                                                                            recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumRemainingVerticalRecipes() - 1);

                                                                            //ratings.add(avgRating);
                                                                            //totalRatingsCountList.add(totalRating);

                                                                            RecyclerRecipe2 recyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl, avgRating, "vertical", false, totalRating);
                                                                            recyclerRecipe2List.add(recyclerRecipe2);

                                                                            //categoryFragmentModel.addItem(title, imageUrl, avgRating, totalRating);

                                                                            VerticalRecipe newRecipe = new VerticalRecipe(title, imageUrl, recipeIdString, avgRating, totalRating);
                                                                            verticalList.add(newRecipe);

                                                                            ///verticalRecipeIdList.add(recipeIdString);
                                                                            //verticalRecipes.add(newRecipe);

                                                                            // todo: may need to uncomment and convert this for onClick
                                                                            //recipeId.add(recipeIdString);
                                                                        }
                                                                        // add to horizontal highly rated list instead of vertical recyclerview
                                                                        else
                                                                        {
                                                                            //Set<Integer> setOfUniqueHighRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();

                                                                            int sizeOfHighRatedSetBefore = recipeLists.getSetOfUniqueHorizontalRecipes().size();
                                                                            recipeLists.addToSetOfUniqueHorizontalRecipes(recipeIdInt);
                                                                            //viewModel.addToSetOfUniqueHighRatedRecipes(recipeIdInt);
                                                                            //setOfUniqueHighRatedRecipes.add(recipeIdInt);

                                                                            // duplicates cannot be added to Sets - only go ahead if we haven't found a duplicate recipe
                                                                            if (sizeOfHighRatedSetBefore != recipeLists.getSetOfUniqueHorizontalRecipes().size())
                                                                            {
                                                                                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);
                                                                                // todo: may need to uncomment and convert, adding new private member to the class
                                                                                //numOfRetrievedHighRatedRecipes++;

                                                                                //highRatedTitles.add(title);
                                                                                //highRatedImages.add(imageUrl);
                                                                                //highRatedRecipeIdList.add(recipeIdString);
                                                                                //highRatedRatings.add(avgRating);

                                                                                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, recipeIdString, avgRating);
                                                                                horizontalList.add(newRecipe);

                                                                                ///horizontalRecipeIdList.add(recipeIdString);
                                                                                //horizontalLists.get(horizontalLists.size() - 1).add(newRecipe);

                                                                                RecyclerRecipe2 horizontalRecyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl,
                                                                                        avgRating, "Popular Recipe", true, totalRating);
                                                                                horizontalRecyclerRecipe2List.add(horizontalRecyclerRecipe2);

                                                                                //sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
                                                                            }
                                                                        }
                                                                    }

                                                                    i++;
                                                                }

                                                                recipeLists.setVerticalEndId(endId);

                                                                recipeLists.appendVerticalList(verticalList);
                                                                recipeLists.appendHorizontalList(horizontalList);
                                                                //recipeLists.appendVerticalRecipeIdList(verticalRecipeIdList);
                                                                //recipeLists.appendHorizontalRecipeIdList(horizontalRecipeIdList);

                                                                recipeLists.setNumRemainingVerticalRecipes(recipeLists.getNumRemainingVerticalRecipes() - verticalList.size());

                                                                List<RecyclerRecipe2> tempList = new ArrayList<>();
                                                                        //recyclerRecipe2List.addAll(horizontalRecyclerRecipe2List);
                                                                tempList.addAll(recyclerRecipe2List);
                                                                tempList.addAll(horizontalRecyclerRecipe2List);

                                                                roomRepository.insertAllRecyclerRecipe2(recyclerRecipe2List);
                                                                roomRepository.insertAllRecyclerRecipe2(horizontalRecyclerRecipe2List);

                                                                if (recipeLists.getHorizontalList().size() < recipeLists.getNumHorizontalToQuery())
                                                                {
                                                                    // search to the left of the left bound
                                                                    dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", recipeLists.getVerticalStartId())
                                                                            .limit(recipeLists.getNumHorizontalToQuery() - recipeLists.getHorizontalList().size()).get().addOnCompleteListener(taskHorizontal ->
                                                                    {
                                                                        Log.i("queries", "queried outer");
                                                                        List<HorizontalRecipe> retrievedHorizontalList = retrieveHorizontal(recipeLists, category, "Popular Recipe", taskHorizontal);
                                                                        recipeLists.appendHorizontalList(retrievedHorizontalList);

                                                                        int numLeftToRetrieve = recipeLists.getNumHorizontalToQuery() - recipeLists.getHorizontalList().size();

                                                                        if (numLeftToRetrieve > 0)
                                                                        {
                                                                            // search to the right of the right bound
                                                                            dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", recipeLists.getVerticalEndId()).limit(numLeftToRetrieve).get()
                                                                                    .addOnCompleteListener(taskSecondHorizontal ->
                                                                                    {

                                                                                        Log.i("queries", "queried inner");
                                                                                        List<HorizontalRecipe> retrievedListInner = retrieveHorizontal(recipeLists, category, "Popular Recipe", taskSecondHorizontal);
                                                                                        recipeLists.appendHorizontalList(retrievedListInner);

                                                                                        callback.onCallback(recipeLists);
                                                                                        //liveDataHorizontal.setValue(horizontalLists);
                                                                                    });
                                                                        }
                                                                        else
                                                                        {
                                                                            callback.onCallback(recipeLists);
                                                                        }
                                                                    });
                                                                }
                                                                else
                                                                {

                                                                }
                                                            }
                                                        });

                                            }
                                            else
                                            {
                                                if (recipeLists.getHorizontalList().size() < recipeLists.getNumHorizontalToQuery())
                                                {
                                                    // search to the left of the left bound
                                                    dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", recipeLists.getVerticalStartId())
                                                            .limit(recipeLists.getNumHorizontalToQuery()).get().addOnCompleteListener(taskHorizontal ->
                                                    {
                                                        Log.i("queries", "queried outer");
                                                        List<HorizontalRecipe> retrievedHorizontalList = retrieveHorizontal(recipeLists, category, "Popular Recipe", taskHorizontal);
                                                        recipeLists.appendHorizontalList(retrievedHorizontalList);

                                                        int numLeftToRetrieve = recipeLists.getNumHorizontalToQuery() - recipeLists.getHorizontalList().size();

                                                        if (numLeftToRetrieve > 0)
                                                        {
                                                            // search to the right of the right bound
                                                            dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", recipeLists.getVerticalEndId()).limit(numLeftToRetrieve).get()
                                                                    .addOnCompleteListener(taskSecondHorizontal ->
                                                                    {

                                                                        Log.i("queries", "queried inner");
                                                                        List<HorizontalRecipe> retrievedListInner = retrieveHorizontal(recipeLists, category, "Popular Recipe", taskSecondHorizontal);
                                                                        recipeLists.appendHorizontalList(retrievedListInner);

                                                                        callback.onCallback(recipeLists);
                                                                        //liveDataHorizontal.setValue(horizontalLists);
                                                                    });
                                                        }
                                                        else
                                                        {
                                                            callback.onCallback(recipeLists);
                                                        }
                                                    });
                                                }
                                            }

                                            //recipeLists.appendVerticalList(verticalList);
                                            //recipeLists.appendHorizontalList(horizontalList);
                                            //recipeLists.setVerticalStartId(startId);
                                            //recipeLists.setVerticalEndId(endId);

                                            //callback.onCallback(verticalList, horizontalList, startId);

                                            //currentNumOfQueries = currentNumOfQueries + task.getResult().size() - 1;

                                            // number of reads to firebase - for preventing excessive and expensive reads
                                            //Log.i("number of queries", String.valueOf(currentNumOfQueries));

                                            // todo: change this to retrieveAndSaveRandomRecipesLessThan to improve randomness.  Make sure new functions does NOT create a new random number!  Pass in current random number instead
                                            //retrieveAndSaveRandomRecipesGreaterThanQuery(rand, numOfRecipes, halfMaxNumberOfHighRatedRecipes, individualQueryVerticalRecipeLimit, category, finalTotalRecursions);

                                            // todo: delete
                                            //categoryFragmentAdapter.notifyDataSetChanged();
                                        }
                                    });



                            //currentNumOfQueries = currentNumOfQueries + task.getResult().size() - 1;

                            // number of reads to firebase - for preventing excessive and expensive reads
                            //Log.i("number of queries", String.valueOf(currentNumOfQueries));

                            // todo: change this to retrieveAndSaveRandomRecipesLessThan to improve randomness.  Make sure new functions does NOT create a new random number!  Pass in current random number instead
                            //retrieveAndSaveRandomRecipesGreaterThanQuery(rand, numOfRecipes, halfMaxNumberOfHighRatedRecipes, individualQueryVerticalRecipeLimit, category, finalTotalRecursions);

                            // todo: delete
                            //categoryFragmentAdapter.notifyDataSetChanged();
                        }
                    });
    }

    public void readRemainingHorizontalRecipes(HorizontalCallback callback, String category, RetrievedRecipeLists recipeList)
    {
        // search to the left of the left bound
        dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", recipeList.getVerticalStartId())
                .limit(recipeList.getNumHorizontalToQuery()).get().addOnCompleteListener(task ->
        {
            Log.i("queries", "queried outer");
            List<HorizontalRecipe> retrievedHorizontalList = retrieveHorizontal(recipeList, category, "Popular Recipe", task);
            recipeList.appendHorizontalList(retrievedHorizontalList);

            int numLeftToRetrieve = recipeList.getNumHorizontalToQuery() - recipeList.getHorizontalList().size();

            if (numLeftToRetrieve > 0)
            {
                // search to the right of the right bound
                dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", recipeList.getVerticalEndId()).limit(numLeftToRetrieve).get()
                        .addOnCompleteListener(task1 ->
                        {

                            Log.i("queries", "queried inner");
                            List<HorizontalRecipe> retrievedListInner = retrieveHorizontal(recipeList, category, "Popular Recipe", task1);
                            recipeList.appendHorizontalList(retrievedListInner);

                            callback.onCallback();
                            //liveDataHorizontal.setValue(horizontalLists);
                        });
            }
            else
            {
                callback.onCallback();
            }
        });
    }

    // returns number of recipes that were actually retrieved
    private List<HorizontalRecipe> retrieveHorizontal(RetrievedRecipeLists recipeList, String category, String typeOfRecyclerItem, Task<QuerySnapshot> task)
    {
        // delete all existing featured recipes in SQL
        deleteAllRecycler2();

        int numRetrieved = 0;

        List<HorizontalRecipe> returnList = new ArrayList<>();
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

            int sizeOfSet = recipeList.getSetOfUniqueHorizontalRecipes().size();
            recipeList.addToSetOfUniqueHorizontalRecipes(highRatedRecipeIdInt);

            // the current recipe is not a duplicate - it may be added to the recyclerview
            if ((sizeOfSet != recipeList.getSetOfUniqueHorizontalRecipes().size()))
            {
                // todo: may need to uncomment and modify class
                //numRetrieved = numRetrieved + 1;
                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, highRatedRecipeId, avgRating);
                // add this recipe to the most recently added vertical item
                //horizontalLists.add(newRecipe);
                returnList.add(newRecipe);

                RecyclerRecipe2 horizontalRecyclerRecipe2 = new RecyclerRecipe2("RecommendedForYou", highRatedRecipeId, title, imageUrl,
                        avgRating, typeOfRecyclerItem, true, totalRating);

                 roomRepository.insertRecycler2(horizontalRecyclerRecipe2);
                // todo: fix this and other inserts to Room
                //insertRecycler2(recyclerRecipePopular2);
            }
        }

        return returnList;
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