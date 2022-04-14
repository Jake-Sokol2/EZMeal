package com.example.ezmeal.FindRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.CategoryFragmentViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.Category;
import com.example.ezmeal.RoomDatabase.Category2;
import com.example.ezmeal.RoomDatabase.CategoryWithRecipes;
import com.example.ezmeal.RoomDatabase.CategoryWithRecyclerRecipes;
import com.example.ezmeal.RoomDatabase.Category_RecyclerRecipe;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.RecyclerRecipe;
import com.example.ezmeal.RoomDatabase.RecyclerRecipe2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryFragment extends Fragment
{
    private FirebaseFirestore db;

    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();

    private RecyclerView rvFindRecipes;
    private CategoryFragmentAdapter categoryFragmentAdapter;

    String category;

    public List<String> recipeId;
    private List<String> highRatedRecipeIdList;
    public Set<Integer> uniqueRecipes;
    public Set<String> uniqueHighRatedRecipes;
    public Random rand;
    public CollectionReference dbRecipes;
    public List<String> highRatedTitles;
    public List<String> highRatedImages;
    public List<Integer> totalRatingsCountList;
    public List<Double> ratings;
    public List<Double> highRatedRatings;
    public List<RecyclerRecipe2> recyclerRecipeList2;
    public List<Category_RecyclerRecipe> categoryRecyclerRecipeList;
    public EZMealDatabase sqlDb;

    private SwipeRefreshLayout swipeLayout;

    private CategoryFragmentViewModel viewModel;

    private final int RECIPE_RESET_TIME = 100;
    private final int NUM_OF_RECIPES = 15;
    private final int HALF_MAX_NUMBER_OF_HIGH_RATED_RECIPES = 3;
    private final int INDIVIDUAL_QUERY_VERTICAL_RECIPE_LIMIT = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_recipes_category, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryFragmentViewModel.class);

       swipeLayout = view.findViewById(R.id.swipeCategory);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // clear all category recipes from Room, to be overwritten with new ones
                deleteEntireCategory(category);
                sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(category);


                viewModel = new ViewModelProvider(requireActivity()).get(CategoryFragmentViewModel.class);

                int randomBound = Integer.MAX_VALUE - 2;
                Category2 cat2 = null;

                rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);

                highRatedTitles = new ArrayList<String>();
                highRatedImages = new ArrayList<String>();
                highRatedRecipeIdList = new ArrayList<String>();
                highRatedRatings = new ArrayList<Double>();
                totalRatingsCountList = new ArrayList<Integer>();
                ratings = new ArrayList<Double>();

                categoryFragmentModel.dumpList();

                Set<Integer> newSetOfUniqueRecipes = new HashSet<>();
                viewModel.setSetOfUniqueRecipes(newSetOfUniqueRecipes);


                viewModel.setNumOfRetrievedRecipes(0);
                viewModel.setNumOfRetrievedHighRatedRecipes(0);

                Set<Integer> resetSet = new HashSet<>();
                viewModel.setSetOfUniqueHighRatedRecipes(resetSet);

                //categoryFragmentModel.dumpList();
                categoryFragmentAdapter.notifyDataSetChanged();

                retrieveAndSaveRandomRecipesGreaterThan(rand, NUM_OF_RECIPES, HALF_MAX_NUMBER_OF_HIGH_RATED_RECIPES, INDIVIDUAL_QUERY_VERTICAL_RECIPE_LIMIT, category);



                // turn off refreshing logo
                swipeLayout.setRefreshing(false);
            }
        });

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);

        highRatedTitles = new ArrayList<String>();
        highRatedImages = new ArrayList<String>();
        highRatedRecipeIdList = new ArrayList<String>();
        highRatedRatings = new ArrayList<Double>();
        totalRatingsCountList = new ArrayList<Integer>();
        ratings = new ArrayList<Double>();

        categoryFragmentAdapter = new CategoryFragmentAdapter(categoryFragmentModel.getRecipeList(), categoryFragmentModel.getUriList(), highRatedTitles, highRatedImages,
                highRatedRecipeIdList, totalRatingsCountList, ratings, highRatedRatings);
        rvFindRecipes.setAdapter(categoryFragmentAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvFindRecipes.setLayoutManager(staggeredGridLayoutManager);
        //rvFindRecipes.setVisibility(View.VISIBLE);
        //rvFindRecipes.suppressLayout(true);
        /*@Override
                public void onLayoutCompleted(RecyclerView.State state)
        {
            super.onLayoutCompleted(state);
        }*/

        rvFindRecipes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                rvFindRecipes.setVisibility(View.VISIBLE);
                //rvFindRecipes.setEnabled(true);
                rvFindRecipes.suppressLayout(false);
                rvFindRecipes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //btn = view.findViewById(R.id.button2);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Bundle extras = getArguments();

        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().enableMultiInstanceInvalidation().build();

        recipeId = new ArrayList<String>();

        categoryFragmentAdapter.setOnItemClickListener(new CategoryFragmentAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, boolean isClickable)
            {
                // certain positions may be an entire horizontal recyclerview - only allow clicking for positions that are actual individual recipes, not holders for horizontal rv's
                if (isClickable)
                {
                    Intent intent = new Intent(getContext(), RecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", recipeId.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        // todo: RecipesRating
        dbRecipes = db.collection("RecipesRatingBigInt");



        // TODO: remove, these are nuking the database
        //sqlDb.testDao().deleteCategory2();
        //sqlDb.testDao().deleteRecyclerRecipe2();
        //sqlDb.testDao().deleteCategoryRecyclerRecipe();

        Log.i("tag", String.valueOf(new Date().getTime()));

        recyclerRecipeList2 = new ArrayList<RecyclerRecipe2>();
        categoryRecyclerRecipeList = new ArrayList<Category_RecyclerRecipe>();

        rand = new Random();

        // tracks duplicate recipes for vertical recyclerview
        uniqueRecipes = new HashSet<>();
        // tracks duplicate recipes for horizontal (highly rated) recyclerview
        uniqueHighRatedRecipes = new HashSet<>();


        List<CategoryWithRecipes> categoryWithRecipes;

        // if bundle was null, it means that the user just opened the screen and is on the "featured" page.  Query random, highly rated recipes (right now just querying all recipes)
        if (extras == null)
        {
            categoryWithRecipes = sqlDb.testDao().getCategoriesWithRecipes("Featured");

            if ((categoryWithRecipes.size() != 0) && (new Date().getTime()) < (categoryWithRecipes.get(0).category2.getDateRetrieved() + RECIPE_RESET_TIME))
            {
                List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                addToRecycler(listOfRecyclerRecipes2);
            }
            else
            {
                deleteEntireCategory(category);
                sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(category);

                int randomBound = 27;
                retrieveAndSaveRandomRecipesFeatured(rand, randomBound, NUM_OF_RECIPES,"Featured");

            }
        }
        // if not null, the user picked a category, so query for the existing category only
        else
        {
            category = extras.getString("cat");
            categoryWithRecipes = sqlDb.testDao().getCategoriesWithRecipes(category);

            if ((categoryWithRecipes.size() != 0) && (new Date().getTime()) < (categoryWithRecipes.get(0).category2.getDateRetrieved() + RECIPE_RESET_TIME))
            {
                List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                addToRecycler(listOfRecyclerRecipes2);
            }
            else
            {
                // clear all category recipes from Room, to be overwritten with new ones
                deleteEntireCategory(category);

                int randomBound = Integer.MAX_VALUE - 2;
                Category2 cat2 = null;


                Set<Integer> newSetOfUniqueRecipes = new HashSet<>();
                viewModel.setSetOfUniqueRecipes(newSetOfUniqueRecipes);

                viewModel.setNumOfRetrievedRecipes(0);
                viewModel.setNumOfRetrievedHighRatedRecipes(0);

                Set<Integer> resetSet = new HashSet<>();
                viewModel.setSetOfUniqueHighRatedRecipes(resetSet);

                categoryFragmentModel.dumpList();
                categoryFragmentAdapter.notifyDataSetChanged();

                retrieveAndSaveRandomRecipesGreaterThan(rand, NUM_OF_RECIPES, HALF_MAX_NUMBER_OF_HIGH_RATED_RECIPES, INDIVIDUAL_QUERY_VERTICAL_RECIPE_LIMIT, category);
                //retrieveAndSaveRandomRecipes(rand, randomBound, 10, category, cat2);

            }
        }

    }

    @Override
    public void onStop()
    {
        super.onStop();
        categoryFragmentModel.dumpList();
    }

    public void addToRecycler(List<RecyclerRecipe2> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            // todo: uncomment
            //if (Objects.equals(listOfRecylerRecipes.get(i).getTypeOfItem(), "vertical"))
            if (Objects.equals(list.get(i).getTypeOfItem(), "vertical"))
            {
                // todo: uncomment
                //categoryFragmentModel.addItem(listOfRecylerRecipes.get(i).getTitle(), listOfRecylerRecipes.get(i).getImageUrl());
                categoryFragmentModel.addItem(list.get(i).getTitle(), list.get(i).getImageUrl(), list.get(i).getAverageRating(), list.get(i).getTotalRatings());
                totalRatingsCountList.add(list.get(i).getTotalRatings());
                ratings.add(list.get(i).getAverageRating());
                // todo: uncomment
                //recipeId.add(listOfRecylerRecipes.get(i).getRecipeId());
                recipeId.add(list.get(i).getRecipeId());
            }
            // todo: uncomment
            //else if (Objects.equals(listOfRecylerRecipes.get(i).getTypeOfItem(), "popular recipe"))
            else if (Objects.equals(list.get(i).getTypeOfItem(), "Popular Recipe"))
            {
                highRatedTitles.add(list.get(i).getTitle());
                highRatedImages.add(list.get(i).getImageUrl());
                highRatedRecipeIdList.add(list.get(i).getRecipeId());
                highRatedRatings.add(list.get(i).getAverageRating());
            }
        }

        categoryFragmentAdapter.notifyDataSetChanged();
    }

    public void deleteEntireCategory(String cat)
    {
        sqlDb.testDao().deleteFromCategory2SpecificCategory(cat);
        sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(cat);
    }

    // todo: remove when Featured Recipes becomes Recommended
    public void retrieveAndSaveRandomRecipesFeatured(Random rand, int bound, int numberOfRecipes, String category)
    {
        int randomNum = rand.nextInt(bound);

        uniqueRecipes.add(randomNum);

        dbRecipes.whereArrayContains("categories", category).whereGreaterThanOrEqualTo("recipeId", randomNum).orderBy("recipeId").limit(numberOfRecipes).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        //actualRetrievedRecipes = actualRetrievedRecipes + task.getResult().size();

                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            String imageUrl = document.getString("imageUrl");
                            String title = document.getString("title");
                            String recipeIdString = document.getId();
                            boolean highlyRated = document.getBoolean("highlyRated");

                            Double countRating = document.getDouble("countRating");
                            Double avgRating;
                            if (countRating != null)
                            {
                                avgRating = document.getDouble("rating") / document.getDouble("countRating");
                            }
                            else
                            {
                                countRating = 0.0;
                                avgRating = 0.0;
                            }

                            Integer totalRating = countRating.intValue();

                            if (Double.isNaN(avgRating))
                            {
                                avgRating = 0.0;
                            }

                            ratings.add(avgRating);
                            totalRatingsCountList.add(totalRating);

                            // todo: switch back to recyclerRecipe
                            // String category, String recipeId, String title, String imageUrl, double averageRating, String typeOfItem, boolean isHorizontal
                            RecyclerRecipe2 recyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl, avgRating, "vertical", false, totalRating);
                            recyclerRecipeList2.add(recyclerRecipe2);

                            // uniqueHighRatedRecipes is a Set, meaning duplicate values cannot be added to it
                            // keeps track of duplicates - if a highly rated recipe is shown in the vertical recyclerview, make sure it isnt also shown in the horizontal
                            if (document.getBoolean("highlyRated"))
                            {
                                uniqueHighRatedRecipes.add(recipeIdString);
                            }

                            categoryFragmentModel.addItem(title, imageUrl, avgRating, totalRating);
                            recipeId.add(recipeIdString);
                        }
                        categoryFragmentAdapter.notifyDataSetChanged();

                        long dateRetrieved = new Date().getTime();

                        Category2 newCategory2 = new Category2(category, dateRetrieved);

                        sqlDb.testDao().insertCategory2(newCategory2);
                        sqlDb.testDao().insertAllRecyclerRecipe2(recyclerRecipeList2);

                        retrieveAndSaveHorizontalRecipes(10, category, "Popular Recipe");

                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.i("random recipes", "no random recipe for that query");
            }
        });
    }

    // Unlike retrieveAndSaveRandomRecipesGreaterThan/LessThan, this shouldn't be recursive because there is never
    // a guarantee as to how many highly rated recipes are in the database.  Could lead to a stack overflow

    // Parameters
    //
    // halfMaxNumberOfHighRatedRecipes - maximum number of recipes that can be retrieved in one query.  This number * 2 equals the actual max number of recipes that will be retrieved
    //          , since 2 queries are called
    public void retrieveAndSaveHorizontalRecipesCategory(Random rand, int halfMaxNumberOfHighRatedRecipes, String category, String typeOfRecyclerItem)
    {
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

        // prevent unnecessary reads if we are already over the recipe limit
        if (viewModel.getNumOfRetrievedHighRatedRecipes() < (halfMaxNumberOfHighRatedRecipes * 2))
        {
            dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", randomNum).whereEqualTo("highlyRated", true).limit(halfMaxNumberOfHighRatedRecipes).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            // some high rated recipes were already admitted during the vertical recyclerview query.  Count these in so we don't go over our max limit of recipes
                            int numRetrieved = viewModel.getNumOfRetrievedHighRatedRecipes();

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Set<Integer> setOfUniqueHighlyRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();

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

                                //int sizeOfSet = uniqueHighRatedRecipes.size();

                                int sizeOfSet = setOfUniqueHighlyRatedRecipes.size();
                                viewModel.addToSetOfUniqueHighRatedRecipes(highRatedRecipeIdInt);
                                //uniqueHighRatedRecipes.add(highRatedRecipeId);

                                // we aren't over the max limit of recipes, and the current recipe is not a duplicate - it may be added to the recyclerview
                                if ((numRetrieved < (halfMaxNumberOfHighRatedRecipes * 2)) && (sizeOfSet != viewModel.getSetOfUniqueHighRatedRecipes().size()))
                                {
                                    numRetrieved = numRetrieved + 1;
                                    viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                                    highRatedTitles.add(title);
                                    highRatedImages.add(imageUrl);
                                    highRatedRecipeIdList.add(highRatedRecipeId);
                                    highRatedRatings.add(avgRating);

                                    RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                                            avgRating, typeOfRecyclerItem, true, totalRating);

                                    sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);

                                }
                            }
                            // todo: delete
                            //categoryFragmentAdapter.notifyDataSetChanged();

                            // prevent unnecessary reads if we are already over the limit of recipes
                            if (viewModel.getNumOfRetrievedHighRatedRecipes() < (halfMaxNumberOfHighRatedRecipes * 2))
                            {
                                dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", randomNum).whereEqualTo("highlyRated", true).limit(halfMaxNumberOfHighRatedRecipes).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                                            {
                                                int numRetrieved = viewModel.getNumOfRetrievedHighRatedRecipes();

                                                for (QueryDocumentSnapshot document : task.getResult())
                                                {
                                                    Set<Integer> setOfUniqueHighlyRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();

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

                                                    //int sizeOfSet = uniqueHighRatedRecipes.size();

                                                    int sizeOfSet = setOfUniqueHighlyRatedRecipes.size();
                                                    viewModel.addToSetOfUniqueHighRatedRecipes(highRatedRecipeIdInt);
                                                    //uniqueHighRatedRecipes.add(highRatedRecipeId);

                                                    // we aren't over the recipe limit and the current recipe is not a duplicate - it may be added to the recyclerview
                                                    if ((viewModel.getNumOfRetrievedHighRatedRecipes() < (halfMaxNumberOfHighRatedRecipes * 2)) && (sizeOfSet != viewModel.getSetOfUniqueHighRatedRecipes().size()))
                                                    {
                                                        viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                                                        highRatedTitles.add(title);
                                                        highRatedImages.add(imageUrl);
                                                        highRatedRecipeIdList.add(highRatedRecipeId);
                                                        highRatedRatings.add(avgRating);

                                                        // String category, String recipeId, String title, String imageUrl, double averageRating, String typeOfItem, boolean isHorizontal
                                                        RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                                                                avgRating, typeOfRecyclerItem, true, totalRating);

                                                        sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
                                                    }
                                                }
                                                // todo: delete
                                                //categoryFragmentAdapter.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Log.i("random recipes", "no random recipe for that query");
                                    }
                                });
                            }
                            // todo: delete
                            //categoryFragmentAdapter.notifyDataSetChanged();

                            categoryFragmentAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Log.i("random recipes", "no random recipe for that query");
                }
            });
        }
        else
        {
            categoryFragmentAdapter.notifyDataSetChanged();
        }
    }

    // Uses methods outline by Cloud Manager of Firestore Dan McGrath
    // https://stackoverflow.com/questions/46798981/firestore-how-to-get-random-documents-in-a-collection
    //
    // Queries Firebase for "random" recipes by generating a random number and grabbing the next (or several next) greatest recipeId (also a random number) stored in Firebase.
    // Recursively calls a similar function, retrieveAndSaveRandomRecipesLessThan, using the same random number from the first run  this time instead querying less than to improve
    // randomness.  The second function recursively calls the first to switch back and forth between greater and less than until the correct number of recipes has been retrieved.
    //
    //
    // Parameters
    // rand - the Random() instance that generates random numbers for querying recipes in Firebase
    // numOfRecipes - total number of recipes intended to be retrieved after all recursions
    // numOfRetrievedRecipes - actual number that has been retrieved so far
    // individualQueryLimit - how "far out" each query checks from the given random number.  Ex: 1 = first recipe greater than random number, 2 = first and second, etc.
    //          this drastically increases speed but also reduces randomness
    // category - the category of recipes that we query
    public void retrieveAndSaveRandomRecipesGreaterThan(Random rand, int numOfRecipes, int halfMaxNumberOfHighRatedRecipes, int individualQueryVerticalRecipeLimit, String category)
    {
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

        int numOfRetrievedRecipes = viewModel.getNumOfRetrievedRecipes();

        if (numOfRetrievedRecipes >= (numOfRecipes))
        {
            long dateRetrieved = new Date().getTime();
            Category2 newCategory = new Category2(category, dateRetrieved);
            sqlDb.testDao().insertCategory2(newCategory);
            retrieveAndSaveHorizontalRecipesCategory(rand, halfMaxNumberOfHighRatedRecipes, category, "Popular Recipe");
        }
        else //if (randChance == 1)
        {
            int remainingNumberOfRecipesToQuery = numOfRecipes - viewModel.getNumOfRetrievedRecipes();
            int currentQueryVerticalRecipeLimit = individualQueryVerticalRecipeLimit;
            if (remainingNumberOfRecipesToQuery < individualQueryVerticalRecipeLimit)
            {
                currentQueryVerticalRecipeLimit = remainingNumberOfRecipesToQuery;
            }

            dbRecipes.whereArrayContains("categories", category).whereGreaterThanOrEqualTo("recipeId", randomNum).orderBy("recipeId").limit(currentQueryVerticalRecipeLimit).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            int numOfRetrievedRecipes = viewModel.getNumOfRetrievedRecipes();

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if (numOfRetrievedRecipes < numOfRecipes)
                                {

                                    double recipeIdDouble = document.getDouble("recipeId");
                                    int recipeIdInt = (int) recipeIdDouble;
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

                                    Set<Integer> setOfUniqueRecipes = viewModel.getSetOfUniqueRecipes();
                                    int sizeOfSet = setOfUniqueRecipes.size();
                                    viewModel.addToSetOfUniqueRecipes(recipeIdInt);

                                    if (viewModel.getSetOfUniqueRecipes().size() != sizeOfSet)
                                    {
                                        // only add a high rated recipe to the vertical recycler if we've already filled the horizontal with the max number of recipes
                                        if ((!highlyRated) || (viewModel.getNumOfRetrievedHighRatedRecipes() >= (halfMaxNumberOfHighRatedRecipes * 2)))
                                        {
                                            viewModel.incrementNumOfRetrievedRecipesBy(1);

                                            ratings.add(avgRating);
                                            totalRatingsCountList.add(totalRating);

                                            RecyclerRecipe2 recyclerRecipe2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl, avgRating, "vertical", false, totalRating);
                                            recyclerRecipeList2.add(recyclerRecipe2);

                                            categoryFragmentModel.addItem(title, imageUrl, avgRating, totalRating);
                                            recipeId.add(recipeIdString);
                                        }
                                        // add to horizontal highly rated set instead of vertical recyclerview
                                        else
                                        {
                                            Set<Integer> setOfUniqueHighRatedRecipes = viewModel.getSetOfUniqueHighRatedRecipes();
                                            int sizeOfHighRatedSetBefore = setOfUniqueHighRatedRecipes.size();
                                            viewModel.addToSetOfUniqueHighRatedRecipes(recipeIdInt);

                                            if (sizeOfHighRatedSetBefore != setOfUniqueHighRatedRecipes.size())
                                            {
                                                viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                                                highRatedTitles.add(title);
                                                highRatedImages.add(imageUrl);
                                                highRatedRecipeIdList.add(recipeIdString);
                                                highRatedRatings.add(avgRating);

                                                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, recipeIdString, title, imageUrl,
                                                        avgRating, "Popular Recipe", true, totalRating);

                                                sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
                                            }
                                        }
                                    }
                                }

                                if (recyclerRecipeList2 != null)
                                {
                                    sqlDb.testDao().insertAllRecyclerRecipe2(recyclerRecipeList2);
                                }
                            }

                            // todo: change this to retrieveAndSaveRandomRecipesLessThan to improve randomness.  Make sure new functions does NOT create a new random number!  Pass in current random number instead
                            retrieveAndSaveRandomRecipesGreaterThan(rand, numOfRecipes, halfMaxNumberOfHighRatedRecipes, individualQueryVerticalRecipeLimit, category);

                            // todo: delete
                            //categoryFragmentAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public void retrieveAndSaveHorizontalRecipes(int numberOfRecipes, String category, String typeOfRecyclerItem)
    {
        dbRecipes.whereEqualTo("highlyRated", true).limit(numberOfRecipes).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            String imageUrl = document.getString("imageUrl");
                            String title = document.getString("title");
                            String highRatedRecipeId = document.getId();

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

                            int sizeOfSet = uniqueHighRatedRecipes.size();
                            uniqueHighRatedRecipes.add(highRatedRecipeId);

                            // the current recipe is not a duplicate and may be added to the recyclerview
                            if (sizeOfSet != uniqueHighRatedRecipes.size())
                            {
                                highRatedTitles.add(title);
                                highRatedImages.add(imageUrl);
                                highRatedRecipeIdList.add(highRatedRecipeId);
                                highRatedRatings.add(avgRating);

                                // String category, String recipeId, String title, String imageUrl, double averageRating, String typeOfItem, boolean isHorizontal
                                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                                        avgRating, typeOfRecyclerItem, true, totalRating);

                                sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);

                            }
                            // else, do nothing.  The recipe was already shown in the vertical recyclerview so there is no need to duplicate it in the horizontal

                        }
                        categoryFragmentAdapter.notifyDataSetChanged();

                        // increment only when new recipe has actually been added to the recyclerview
                        Log.i("test", "test");
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.i("random recipes", "no random recipe for that query");
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // close Room
        if ((sqlDb != null) && (sqlDb.isOpen()))
        {
            sqlDb.close();
        }

    }
}
