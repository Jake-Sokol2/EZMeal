package com.example.ezmeal.FindRecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentFeaturedRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.FeaturedFragmentViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.Category2;
import com.example.ezmeal.RoomDatabase.CategoryWithRecipes;
import com.example.ezmeal.RoomDatabase.Category_RecyclerRecipe;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.RecyclerRecipe2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class FeaturedFragment extends Fragment
{
    private FirebaseFirestore db;

    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();

    private RecyclerView rvFindRecipes;
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private CategoryFragmentFeaturedRecyclerAdapter categoryFragmentFeaturedAdapter;

    String category;

    public List<String> recipeId;
    private List<String> highRatedRecipeIdList;
    public Set<Integer> uniqueRecipes;
    public Set<String> uniqueHighRatedRecipes;
    public Random rand;
    public CollectionReference dbRecipes;
    public List<String> verticalTitleList = new ArrayList<String>();
    public List<String> highRatedTitles;
    public List<String> highRatedImages;
    public List<Integer> totalRatingsCountList;
    public List<Double> ratings;
    public List<Double> highRatedRatings;
    public List<RecyclerRecipe2> recyclerRecipeList2;
    public List<Category_RecyclerRecipe> categoryRecyclerRecipeList;
    public EZMealDatabase sqlDb;
    private int numRecipes;

    private ArrayList<List<HorizontalRecipe>> horizontalLists = new ArrayList<List<HorizontalRecipe>>();


    private SwipeRefreshLayout swipeLayout;

    private FeaturedFragmentViewModel viewModel;

    private final int RECIPE_RESET_TIME = 5;
    private final int NUM_OF_RECIPES = 15;
    private final int HALF_MAX_NUMBER_OF_HIGH_RATED_RECIPES = 3;
    private final int INDIVIDUAL_QUERY_VERTICAL_RECIPE_LIMIT = 3;

    private int currentNumOfQueries = 0;

    private boolean bln = false;

    private int recommendedRecipesForYouDebtPrevention = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_recipes_category, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FeaturedFragmentViewModel.class);

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);

        highRatedTitles = new ArrayList<String>();
        highRatedImages = new ArrayList<String>();
        highRatedRecipeIdList = new ArrayList<String>();
        highRatedRatings = new ArrayList<Double>();
        totalRatingsCountList = new ArrayList<Integer>();
        ratings = new ArrayList<Double>();



        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvFindRecipes.setLayoutManager(staggeredGridLayoutManager);

        rvFindRecipes.suppressLayout(true);
        //rvFindRecipes.setVisibility(View.INVISIBLE);
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

        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();

        recipeId = new ArrayList<String>();

        db = FirebaseFirestore.getInstance();


        // todo: RecipesRating
        dbRecipes = db.collection("RecipesRatingBigInt");





        // TODO: remove, these are nuking the database
        //sqlDb.testDao().deleteCategory2();
        //sqlDb.testDao().deleteRecyclerRecipe2();
        //sqlDb.testDao().deleteCategoryRecyclerRecipe();

        recyclerRecipeList2 = new ArrayList<RecyclerRecipe2>();
        categoryRecyclerRecipeList = new ArrayList<Category_RecyclerRecipe>();

        rand = new Random();

        // tracks duplicate recipes for vertical recyclerview
        uniqueRecipes = new HashSet<>();
        // tracks duplicate recipes for horizontal (highly rated) recyclerview
        uniqueHighRatedRecipes = new HashSet<>();


        List<CategoryWithRecipes> categoryWithRecipes;


        categoryFragmentFeaturedAdapter = new CategoryFragmentFeaturedRecyclerAdapter(verticalTitleList, horizontalLists);
        rvFindRecipes.setAdapter(categoryFragmentFeaturedAdapter);

        categoryFragmentFeaturedAdapter.setOnItemClickListener(new CategoryFragmentFeaturedRecyclerAdapter.MainAdapterListener()
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

        // primary ingredient is required - if this isn't present in firebase, query will fail
        /*List<String> primaryIngredients = new ArrayList<String>(Arrays.asList("chicken", "beef", "cookie dough"));
        // secondary ingredients are optional.
        List<String> secondaryIngredients = new ArrayList<String>(Arrays.asList("salt", "pepper"));

        Map<String, String> categoryListSql = ImmutableMap.of(
                "chicken", "Breakfast",
                "cookie dough", "Cookies",
                "cookies", "Cookies");

        List<String> finalIngredientsList = new ArrayList<String>();
        for (String i : primaryIngredients)
        {
            if (categoryListSql.containsKey(i))
            {
                finalIngredientsList.add(categoryListSql.get(i));
            }
        }
        finalIngredientsList.add("Breakfast");*/
        List<String> finalIngredientsList = sqlDb.testDao().getActiveCategoriesFromIdentifier();

        if (finalIngredientsList.size() > 0)
        {
            //for (int i = 0; i < finalIngredientsList.size(); i++)
            //{
            // todo: remove, no longer needed?
            //categoryWithRecipes = sqlDb.testDao().getCategoriesWithRecipes("Featured");


            //getActivity().getSharedPreferences("FeaturedRecipes", 0).edit().clear().commit();

            SharedPreferences sp;
            sp = getActivity().getSharedPreferences("FirstRunAfterUpdate", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            long timeLastEntered = sp.getLong("FeaturedRecipes", 0);


                /*if ((categoryWithRecipes.size() != 0) && (new Date().getTime()) < (categoryWithRecipes.get(0).category2.getDateRetrieved() + RECIPE_RESET_TIME))
                {
                    List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                    addToRecycler(listOfRecyclerRecipes2);
                }*/
            if ((timeLastEntered != 0) && (new Date().getTime()) < timeLastEntered + RECIPE_RESET_TIME)
            {
                //List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                //addToRecycler(listOfRecyclerRecipes2);
            }
            else
            {
                // clear all category recipes from Room, to be overwritten with new ones
                editor.putLong("FeaturedRecipes", new Date().getTime());
                deleteEntireCategory("Featured");

                int randomBound = Integer.MAX_VALUE - 2;
                Category2 cat2 = null;

                Set<Integer> newSetOfUniqueRecipes = new HashSet<>();
                viewModel.setSetOfUniqueRecipes(newSetOfUniqueRecipes);

                viewModel.setNumOfRetrievedRecipes(0);
                viewModel.setNumOfRetrievedHighRatedRecipes(0);

                Set<Integer> resetSet = new HashSet<>();
                viewModel.setSetOfUniqueHighRatedRecipes(resetSet);

                categoryFragmentModel.dumpList();
                categoryFragmentFeaturedAdapter.notifyDataSetChanged();

                currentNumOfQueries = 0;

                // numRecipes is the amount of recipes to retrieve per category.  Due to integer division the actual amount retrieved may be lower
                numRecipes = 10 / finalIngredientsList.size();

                //int halfMaxNumberOfHighRatedRecipes = 5;

                // add "Recommended for you" section to recyclerview
                verticalTitleList.add("Recommended for you");
                List<HorizontalRecipe> newRecipeList = new ArrayList<HorizontalRecipe>();
                horizontalLists.add(newRecipeList);
                Random rand = new Random();
                for (int i = 0; i < finalIngredientsList.size(); i++)
                {
                    category = finalIngredientsList.get(i);
                    //verticalList.add("Position " + i);
                    int randomNum = rand.nextInt();
                    retrieveAndSaveHorizontalRecipes(numRecipes, category, randomNum);
                }

                    /*category = finalIngredientsList.get(0);
                    verticalList.add("set 1");
                    retrieveAndSaveHorizontalRecipesCategoryGreater(numRecipes, category);

                    category = finalIngredientsList.get(1);
                    retrieveAndSaveHorizontalRecipesCategoryGreater(HALF_MAX_NUMBER_OF_HIGH_RATED_RECIPES, category);*/
            }
            //}
        }


        // when firebase query for horizontal recipes finishes, run onComplete code
        viewModel.getTaskPopularRecipesGreater().observe(getViewLifecycleOwner(), taskGreater ->
        {
            if (taskGreater != null)
            {
                //viewModel.setHorizontalRecipeRandomNum(rand.nextInt());
                retrieveHorizontal(category, "Popular Recipe", taskGreater);
                //retrieveAndSaveHorizontalRecipesCategoryLess(numRecipes, category, randomNum);
            }
        });

        /*int numCategoriesQueried;
        viewModel.getTaskPopularRecipesLess().observe(getViewLifecycleOwner(), taskLess ->
        {
            if (taskLess != null)
            {
                retrieveHorizontal(category, "Popular Recipe", taskLess);
                //categoryFragmentFeaturedAdapter.notifyDataSetChanged();
            }
        });*/

        viewModel.getNumOfRetrievedHighRatedRecipes().observe(getViewLifecycleOwner(), numRetrieved ->
        {
            // numRecipes / size / 2 is how many recipes each individual firebase query returns

            if (finalIngredientsList.size() > 0)
            {
                int numRecipesPerQuery = (numRecipes / finalIngredientsList.size()) / 2;
                // total recipes returned after all queries are finished (finalIngredientsList * 2 since each index runs two queries - one Greater and one LessThan)
                int totalNumOfRecipes = numRecipesPerQuery * (finalIngredientsList.size() * 2);
                if (numRetrieved >= totalNumOfRecipes)
                {
                    //retrieveHorizontal(category, "Popular Recipe", taskLess);
                    categoryFragmentFeaturedAdapter.notifyDataSetChanged();
                }
            }

        });

        return view;
    }

    // returns number of recipes that were actually retrieved
    private int retrieveHorizontal(String category, String typeOfRecyclerItem, Task<QuerySnapshot> task)
    {
        final int halfMaxNumberOfHighRatedRecipes = 3;

        // some high rated recipes were already admitted during the vertical recyclerview query.  Count these in so we don't go over our max limit of recipes

        int numRetrieved = 0;
        /*if (viewModel.getNumOfRetrievedHighRatedRecipes().getValue() != null)
        {
            numRetrieved = viewModel.getNumOfRetrievedHighRatedRecipes().getValue();
        }*/

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

            int sizeOfSet = setOfUniqueHighlyRatedRecipes.size();
            viewModel.addToSetOfUniqueHighRatedRecipes(highRatedRecipeIdInt);
            //uniqueHighRatedRecipes.add(highRatedRecipeId);

            // the current recipe is not a duplicate - it may be added to the recyclerview
            if ((sizeOfSet != viewModel.getSetOfUniqueHighRatedRecipes().size()))
            {
                numRetrieved = numRetrieved + 1;
                //viewModel.incrementNumOfRetrievedHighRatedRecipes(1);

                HorizontalRecipe newRecipe = new HorizontalRecipe(title, imageUrl, highRatedRecipeId, avgRating);
                // add this recipe to the most recently added vertical item
                horizontalLists.get(horizontalLists.size() - 1).add(newRecipe);
                //highRatedTitles.add(title);
                //highRatedImages.add(imageUrl);
                //highRatedRecipeIdList.add(highRatedRecipeId);
                //highRatedRatings.add(avgRating);

                RecyclerRecipe2 recyclerRecipePopular2 = new RecyclerRecipe2(category, highRatedRecipeId, title, imageUrl,
                        avgRating, typeOfRecyclerItem, true, totalRating);

                sqlDb.testDao().insertRecyclerRecipe2(recyclerRecipePopular2);
            }
        }

        //viewModel.setTaskPopularRecipesLess(null);

        //categoryFragmentAdapter.notifyDataSetChanged();

        categoryFragmentFeaturedAdapter.notifyDataSetChanged();
        // todo: delete
        //categoryFragmentAdapter.notifyDataSetChanged();
        return numRetrieved;
    }

    @Override
    public void onResume()
    {
        super.onResume();

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

        categoryFragmentFeaturedAdapter.notifyDataSetChanged();
    }

    public void deleteEntireCategory(String cat)
    {
        sqlDb.testDao().deleteFromCategory2SpecificCategory(cat);
        sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(cat);
    }


    // Unlike retrieveAndSaveRandomRecipesGreaterThan/LessThan, this shouldn't be recursive because there is never
    // a guarantee as to how many highly rated recipes are in the database.  Could lead to a stack overflow

    // Parameters
    //
    // halfMaxNumberOfHighRatedRecipes - maximum number of recipes that can be retrieved in one query.  This number * 2 equals the actual max number of recipes that will be retrieved
    //          , since 2 queries are called

    public void retrieveAndSaveHorizontalRecipesCategoryLess(int numRecipes, String category, int randomNum)
    {
        // prevent unnecessary reads if we are already over the limit of recipes

        if (viewModel.getNumOfRetrievedHighRatedRecipes().getValue() != null)
        {
                dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", randomNum).whereEqualTo("highlyRated", true).limit(numRecipes / 2).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                viewModel.setTaskPopularRecipesLess(task);
                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.i("random recipes", "no random recipe for that query");
                    }
                });
/*            else
            {
                categoryFragmentFeaturedAdapter.notifyDataSetChanged();

                if (!bln)
                {
                    if (recyclerRecipeList2 != null)
                    {
                        sqlDb.testDao().insertAllRecyclerRecipe2(recyclerRecipeList2);
                        bln = true;
                    }
                }
            }*/
        }
    }

    public void retrieveAndSaveHorizontalRecipesFeaturedGreater(int numQueries, String category)
    {
        int randomNum;

        if (viewModel.getTaskFeaturedRandomNum().getValue() != null)
        {
            randomNum = viewModel.getTaskFeaturedRandomNum().getValue();
        }
        else
        {
            Random rand = new Random();
            randomNum = rand.nextInt(Integer.MAX_VALUE - 2);
        }

        // prevent unnecessary reads if we are already over the recipe limit
        if (viewModel.getNumOfRetrievedHighRatedRecipes().getValue() != null)
        {
            if (viewModel.getNumOfRetrievedHighRatedRecipes().getValue() < (numQueries * 2))
            {
                dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", randomNum).limit(numQueries).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                // observing the view model in the main thread lets it know when onComplete runs (firebase calls are asynchronous)
                                viewModel.setTaskFeatured(task);
                            }
                        });
            }
        }
    }


    public void retrieveAndSaveHorizontalRecipes(int numRecipes, String category, int randomNum)
    {
        //int randomNum;
        // prevent unnecessary reads if we are already over the recipe limit

        if (viewModel.getNumOfRetrievedHighRatedRecipes().getValue() != null)
        {
            dbRecipes.whereArrayContains("categories", category).whereGreaterThan("recipeId", randomNum)
                    .limit(numRecipes).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            // observing the view model in the main thread lets it know when onComplete runs (firebase calls are asynchronous)
                            //viewModel.setTaskPopularRecipesGreater(task);
                            int numRetrieved = retrieveHorizontal(category, "Popular Recipe", task);

                            int numLeftToRetrieve = numRecipes - numRetrieved;

                            if (numLeftToRetrieve > 0)
                            {
                                dbRecipes.whereArrayContains("categories", category).whereLessThan("recipeId", randomNum).limit(numLeftToRetrieve).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                                            {
                                                retrieveHorizontal(category, "Popular Recipe", task);
                                                //viewModel.setTaskPopularRecipesGreater(task);
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

    }

































    public void saveCategory()
    {
        long dateRetrieved = new Date().getTime();
        Category2 newCategory = new Category2(category, dateRetrieved);
        sqlDb.testDao().insertCategory2(newCategory);
    }



























   /* public void retrieveAndSaveHorizontalRecipes(int numberOfRecipes, String category, String typeOfRecyclerItem)
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
    }*/

  /*  public void retrieveAndSaveRecommendedBasedOnIngredientsGreaterThan(Random rand, int numOfRecipes, int halfMaxNumberOfHighRatedRecipes,
                                                                        int individualQueryVerticalRecipeLimit, int maxNumberOfQueries, int currentNumberOfQueries,
                                                                        String category)
    {*/



        /*
        int randomNum = rand.nextInt(Integer.MAX_VALUE - 2);

        int numOfRetrievedRecipes = viewModel.getNumOfRetrievedRecipes();

        // if we retrieved max number of recipes      or       if we exceeded max number of queries (prevent infinite recursion)
        if ((numOfRetrievedRecipes >= (numOfRecipes)) || (currentNumberOfQueries >= maxNumberOfQueries))
        {
            long dateRetrieved = new Date().getTime();
            //Category2 newCategory = new Category2(category, dateRetrieved);
            //sqlDb.testDao().insertCategory2(newCategory);
            //retrieveAndSaveHorizontalRecipesCategory(rand, halfMaxNumberOfHighRatedRecipes, category, "Popular Recipe");
        }
        else
        {
            int remainingNumberOfRecipesToQuery = numOfRecipes - numOfRetrievedRecipes;
            int currentQueryVerticalRecipeLimit = individualQueryVerticalRecipeLimit;
            if (remainingNumberOfRecipesToQuery < individualQueryVerticalRecipeLimit)
            {
                currentQueryVerticalRecipeLimit = remainingNumberOfRecipesToQuery;
            }

            dbRecipes.whereArrayContains("ingredients", category).whereGreaterThanOrEqualTo("recipeId", randomNum).orderBy("recipeId").limit(currentQueryVerticalRecipeLimit).get()
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
                            }

                            // todo: change this to retrieveAndSaveRandomRecipesLessThan to improve randomness.  Make sure new functions does NOT create a new random number!  Pass in current random number instead
                            retrieveAndSaveRandomRecipesGreaterThan(rand, numOfRecipes, halfMaxNumberOfHighRatedRecipes, individualQueryVerticalRecipeLimit, category);

                            // todo: delete
                            //categoryFragmentAdapter.notifyDataSetChanged();
                        }
                    });
        }

         */

    public void cleanUpFragmentInstanceState()
    {
        FragmentManager fm = getChildFragmentManager();
        //fm.findFragmentByTag(String.valueOf((fm.getBackStackEntryAt(fm.getBackStackEntryCount()))))
        Fragment f = fm.findFragmentByTag("testtag");
        fm.beginTransaction().remove(f).commit();
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
