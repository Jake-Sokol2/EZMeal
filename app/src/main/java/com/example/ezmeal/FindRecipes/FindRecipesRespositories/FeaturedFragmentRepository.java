package com.example.ezmeal.FindRecipes.FindRecipesRespositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalQueryResult;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;

import com.example.ezmeal.roomDatabase.RecyclerRecipe2;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeaturedFragmentRepository
{
    private FeaturedFragmentRoomRepository roomRepository;
    // fuck you github
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRecipes = db.collection("Recipes");

    private Set<Integer> setOfUniqueHighlyRatedRecipes = new HashSet<>();
    private List<HorizontalRecipe> horizontalLists = new ArrayList<HorizontalRecipe>();
    private Application application;
    private MutableLiveData<List<HorizontalRecipe>> liveDataHorizontal = new MutableLiveData<>();
    private MutableLiveData<List<HorizontalRecipe>> returnNewRecipeList = new MutableLiveData<>();
    private MutableLiveData<List<HorizontalRecipe>> returnPopularRecipesThisWeekList = new MutableLiveData<>();

    public void setNewRecipesList()
    {
        readNewRecipesList(new RecipeCallback()
        {
            @Override
            public void onCallback(HorizontalQueryResult horizontalQueryResult)
            {
                returnNewRecipeList.setValue(horizontalQueryResult.getHorizontalRecipeList());
            }
        }, 10);

        // 604800000 milliseconds in a week
        /*if (currentTime < (timeBeginningOfWeek + 604800000))
        {

        }*/
    }

    public MutableLiveData<List<HorizontalRecipe>> getNewRecipesList()
    {
        return returnNewRecipeList;
    }

    public MutableLiveData<List<HorizontalRecipe>> getPopularRecipesThisWeekList()
    {
        return returnPopularRecipesThisWeekList;
    }

    public void setPopularRecipesThisWeekList()
    {
        readPopularRecipesThisWeekList(new RecipeCallback()
        {
            @Override
            public void onCallback(HorizontalQueryResult horizontalQueryResult)
            {
                returnPopularRecipesThisWeekList.setValue(horizontalQueryResult.getHorizontalRecipeList());
            }
        }, 3);

        // 604800000 milliseconds in a week
        /*if (currentTime < (timeBeginningOfWeek + 604800000))
        {

        }*/
    }

    public void readPopularRecipesThisWeekList(RecipeCallback recipeCallback, int numPopularRecipesToRetrieve)
    {
        DatabaseReference queryRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        queryRef.keepSynced(true);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long timeBeginningOfWeek = cal.getTimeInMillis();
        String stringTimeBeginningOfWeek = String.valueOf(timeBeginningOfWeek);
        long currentTime = new Date().getTime();

        queryRef.child(stringTimeBeginningOfWeek).orderByChild("numClicked").limitToLast(numPopularRecipesToRetrieve).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // If a week exists in the database and it isn't this week, delete all data
                if (snapshot.getValue() != null)
                {
                    List<String> recipeIdList = new ArrayList<>();
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        recipeIdList.add(ds.getKey());
                        Log.i("query2", String.valueOf(ds));
                        //queryRef.child(ds.getKey()).removeValue();
                    }
                    Log.i("query2", "a");

                    dbRecipes.whereIn(FieldPath.documentId(), recipeIdList).get().addOnCompleteListener(task ->
                    {
                        HorizontalQueryResult newRecipesQuery = new HorizontalQueryResult();
                        newRecipesQuery = retrieveHorizontalNew("Popular Recipes This Week", "Popular Recipes This Week", task, newRecipesQuery);

                        recipeCallback.onCallback(newRecipesQuery);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void readNewRecipesList(RecipeCallback recipeCallback, int numNewRecipesToRetrieve)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long timeBeginningOfWeek = cal.getTimeInMillis();
        Log.i("time", String.valueOf(timeBeginningOfWeek));
        long currentTime = new Date().getTime();

            //int finalNumNewRecipesToRetrieve = numNewRecipesToRetrieve;

            // try to query only half of the recipes first, then query the rest in the other direction.  Reduces pattern repetition
            dbRecipes.whereEqualTo("weekCreated", "1651982400000").whereGreaterThan("recipeId", randomNum)
                    .limit(numNewRecipesToRetrieve / 2).get().addOnCompleteListener(taskGreater ->
            {
                Log.i("queries", "queried outer");

                final HorizontalQueryResult[] newRecipesQuery = {new HorizontalQueryResult()};
                newRecipesQuery[0] = retrieveHorizontalNew("New Recipes This Week", "New Recipes This Week", taskGreater, newRecipesQuery[0]);

                int numLeftToRetrieve = numNewRecipesToRetrieve - newRecipesQuery[0].getNumReturned();

                if (numLeftToRetrieve > 0)
                {
                    dbRecipes.whereEqualTo("weekCreated", "1651982400000").whereLessThan("recipeId", randomNum).limit(numLeftToRetrieve).get()
                            .addOnCompleteListener(taskLess ->
                            {
                                newRecipesQuery[0] = retrieveHorizontalNew("New Recipes This Week", "New Recipes This Week", taskLess, newRecipesQuery[0]);

                                recipeCallback.onCallback(newRecipesQuery[0]);
                                //readData(numRecipesPerCategory, categoryList, recursions, numCategories);

                                //someCallback.onCallback(horizontalLists);
                                //liveDataHorizontal.setValue(horizontalLists);
                            });
                }
                else
                {
                    recipeCallback.onCallback(newRecipesQuery[0]);
                }
            });
    }



    private interface SomeCallback {
        void onCallback(List<HorizontalRecipe> list);
    }

    private interface RecipeCallback {
        void onCallback(HorizontalQueryResult horizontalQueryResult);
    }

    public FeaturedFragmentRepository(@NonNull Application application)
    {
        roomRepository = new FeaturedFragmentRoomRepository(application);
        this.application = application;
    }

    public void setHorizontalLists(List<String> categoryList)
    {
        Random rand = new Random();

        // todo: stop hardcoding this
        int numRecipes = 10;

        int categoryListSize = categoryList.size();
        int numRecipesPerCategory = numRecipes / categoryListSize;

        //liveDataHorizontal = new MutableLiveData<>();

        List<HorizontalRecipe> recursedHorizontalList = horizontalLists;
            readHorizontalData(new SomeCallback()
            {
                @Override
                public void onCallback(List<HorizontalRecipe> list)
                {
                    Log.i("active categories", "Repository - setting value of MutableLiveData");
                    liveDataHorizontal.setValue(list);
                    Log.i("active categories", "Repository - value set!");
                }
            }, numRecipes, numRecipesPerCategory, categoryList, 0, recursedHorizontalList, 0);


        //return liveDataHorizontal;
    }

    public MutableLiveData<List<HorizontalRecipe>> getHorizontalLists()
    {
        return liveDataHorizontal;
    }

    public void readHorizontalData(SomeCallback someCallback, int numRecipes, int numRecipesPerCategory, List<String> categoryList, int numRetrieved, List<HorizontalRecipe> recursedHorizontalLists, int numRecursions)
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
        categoryList.add("Breakfast");
        dbRecipes.whereArrayContainsAny("categories", categoryList).whereGreaterThan("recipeId", randomNum).limit(10).get().addOnCompleteListener(task ->
        {
            retrieveHorizontal(categoryList.get(0), "Popular Recipe", task);

            someCallback.onCallback(horizontalLists);
        });

        /*dbRecipes.whereArrayContains("categories", categoryList.get(0)).whereGreaterThan("recipeId", randomNum)
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
                            Log.i("queries", "queried inner");
                            retrieveHorizontal(categoryList.get(0), "Popular Recipe", task1);

                            // recurse
                            if (categoryList.size() > 1)
                            {
                                ExecutorService es = Executors.newSingleThreadExecutor();
                                CountDownLatch latch = new CountDownLatch(1);

                                es.submit((Callable<Void>) () ->
                                {
                                    readData(finalNumRecipesPerCategory, categoryList, 1, categoryList.size());
                                    latch.countDown();
                                    return null;
                                });

                                try
                                {
                                    latch.await();
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                            }

                            someCallback.onCallback(horizontalLists);
                        });
            }
            else
            {
                if (categoryList.size() > 1)
                {
                    ExecutorService es = Executors.newSingleThreadExecutor();
                    CountDownLatch latch = new CountDownLatch(1);

                    es.submit((Callable<Void>) () ->
                    {
                        readData(finalNumRecipesPerCategory, categoryList, 1, categoryList.size());
                        latch.countDown();
                        return null;
                    });

                    try
                    {
                        latch.await();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                someCallback.onCallback(horizontalLists);
            }
        });*/
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
                    liveDataHorizontal.setValue(horizontalLists);
                    readData(numRecipesPerCategory, categoryList, recursions, numCategories);
                }
            });
        }
        else
        {
            liveDataHorizontal.setValue(horizontalLists);
        }
    }

    // returns number of recipes that were actually retrieved
    private int retrieveHorizontal(String category, String typeOfRecyclerItem, Task<QuerySnapshot> task)
    {
        Log.i("active categories", "Repository - start of retrieveHorizontal");
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
            if (sizeOfSet != setOfUniqueHighlyRatedRecipes.size())
            {
                numRetrieved = numRetrieved + 1;
                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, highRatedRecipeId, avgRating, category);
                // add this recipe to the most recently added vertical item
                horizontalLists.add(newRecipe);
                //horizontalList.add(newRecipe);


                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                        avgRating, typeOfRecyclerItem, true, totalRating);


                insertRecycler2(recyclerRecipePopular2);
            }
        }

        Log.i("active categories", "Repository - end of retrieveHorizontal");

        return horizontalLists.size();
    }

    // returns number of recipes that were actually retrieved
    private HorizontalQueryResult retrieveHorizontalNew(String category, String typeOfRecyclerItem, Task<QuerySnapshot> task, HorizontalQueryResult recipeList)
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

            int sizeOfSet = recipeList.getSetOfUniqueRecipes().size();
            recipeList.appendSetOfUniqueRecipes(highRatedRecipeIdInt);

            // the current recipe is not a duplicate - it may be added to the recyclerview
            if ((sizeOfSet != recipeList.getSetOfUniqueRecipes().size()))
            {
                numRetrieved = numRetrieved + 1;
                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, highRatedRecipeId, avgRating, category);
                // add this recipe to the most recently added vertical item
                recipeList.appendHorizontalRecipeList(newRecipe);
                //horizontalList.add(newRecipe);


                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                        avgRating, typeOfRecyclerItem, true, totalRating);


                insertRecycler2(recyclerRecipePopular2);
            }
        }

        return recipeList;
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