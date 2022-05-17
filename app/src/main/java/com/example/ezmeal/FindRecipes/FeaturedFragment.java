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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentFeaturedRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.FeaturedFragmentRoomRepository;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.FeaturedFragmentViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeaturedFragment extends Fragment
{
    private FirebaseFirestore db;

    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();

    private RecyclerView rvFindRecipes;
    private CategoryFragmentFeaturedRecyclerAdapter categoryFragmentFeaturedAdapter;

    public List<String> recipeId;
    public CollectionReference dbRecipes;
    public List<String> verticalTitleList = new ArrayList<String>();
    public List<Double> ratings;
    public EZMealDatabase sqlDb;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private boolean isPopulated;

    private FeaturedFragmentRoomRepository testR;

    private ArrayList<List<HorizontalRecipe>> horizontalLists = new ArrayList<List<HorizontalRecipe>>();
    private FeaturedFragmentViewModel viewModel;

    private List<String> activeCategoryList;

    private final int RECIPE_RESET_TIME = 86400000;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_find_recipes_category, container, false);

        // ViewModel
        viewModel =  new ViewModelProvider(requireActivity()).get(FeaturedFragmentViewModel.class);

        // Room
        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();

        // Featured Recipes shares a layout with the other recipe categories, but shouldn't be refreshable
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeCategory);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);


            // Firebase
        db = FirebaseFirestore.getInstance();
        dbRecipes = db.collection("Recipes");

        testR = new FeaturedFragmentRoomRepository(getActivity().getApplication());

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvFindRecipes.setLayoutManager(staggeredGridLayoutManager);
        rvFindRecipes.suppressLayout(true);

        rvFindRecipes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                rvFindRecipes.setVisibility(View.VISIBLE);
                rvFindRecipes.suppressLayout(false);
                rvFindRecipes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

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

        viewModel.getIsPopulated().observe(getViewLifecycleOwner(), returnIsPopulated ->
        {
            isPopulated = returnIsPopulated;
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getHorizontalList(activeCategoryList).observe(getViewLifecycleOwner(), list ->
        {
                Log.i("active categories", "Fragment - observer");
                Log.i("active categories", String.valueOf(list));
                Log.i("active categories", "size before - " + String.valueOf(list.size()));
                if (list != null)// && list.size() > 0)
                {
                    Log.i("active categories", "Fragment - observer - list not null");
                    if (list.size() != 0)
                    {
                        Log.i("active categories", "Fragment - observer - list not empty");
                        Log.i("active categories", String.valueOf(list));
                        Log.i("active categories", "size after - " + String.valueOf(list.size()));
                        horizontalLists.add(list);
                        verticalTitleList.add("Recommended for you");
                        categoryFragmentFeaturedAdapter.notifyDataSetChanged();
                    }

                }
        });

        viewModel.getNewRecipesList().observe(getViewLifecycleOwner(), list ->
        {
            if (list != null && list.size() > 0)
            {
                horizontalLists.add(list);
                verticalTitleList.add("New Recipes this Week");
                categoryFragmentFeaturedAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getPopularRecipesThisWeekList().observe(getViewLifecycleOwner(), list ->
        {
            if (list != null && list.size() > 0)
            {
                horizontalLists.add(list);
                verticalTitleList.add("Popular Recipes this Week");
                categoryFragmentFeaturedAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
                                                    // 2 minutes ago
        viewModel.getRecentCategoriesFromIdentifier(new Date().getTime() - 120000L).observe(getViewLifecycleOwner(), new Observer<List<String>>()
        {
            @Override
            public void onChanged(List<String> activeCategories)
            {
                // todo: remove, this is hardcoded
                //activeCategoryList = new ArrayList<>(Arrays.asList("Pancakes", "Cookies"));
                //activeCategories = activeCategoryList;

                // recipes are queried based on how long ago the user added similar items to their list.  More recent items appear while older items stop appearing
                if (!isPopulated)// && list.size() > 0)
                {
                    if (activeCategories != null && activeCategories.size() > 0)
                    {
                        viewModel.setHorizontalList(activeCategories);
                    }
                    viewModel.setPopulated(true);
                    /*if (activeCategories.size() > 0)
                    {
                        //getActivity().getSharedPreferences("FeaturedRecipes", 0).edit().clear().commit();

                        SharedPreferences sp;
                        sp = getActivity().getSharedPreferences("FirstRunAfterUpdate", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sp.edit();
                        long timeLastEntered = sp.getLong("FeaturedRecipes", 0);



                        if ((timeLastEntered != 0) && (new Date().getTime()) < timeLastEntered + RECIPE_RESET_TIME)
                        {
                            // keep track of last time user retrieved featured recipes
                            editor.putLong("FeaturedRecipes", new Date().getTime());

                            if (!isPopulated)
                            {
                                viewModel.setHorizontalList(activeCategories);
                            }
                            //List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                            //addToRecycler(listOfRecyclerRecipes2);
                        }
                        else
                        {
                            sqlDb.testDao().updateAllIdentifiersIsNotActive();
                            //String email = mAuth.getCurrentUser().getEmail();
                            //List<String> listOfShoppingLists = new ArrayList<>();
                            //
                            //List<String> listOfShoppingIds = new ArrayList<>();
                            *//*db.collection("Groups").whereEqualTo("Creator", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        for (QueryDocumentSnapshot document: task.getResult())
                                        {
                                            listOfShoppingLists.add(document.getString("ListName"));
                                            listOfShoppingIds.add(document.getId());
                                        }
                                    }
                                }
                            });*//*

                            *//*db.collection("Groups").whereArrayContains("SharedUsers", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        for (QueryDocumentSnapshot document: task.getResult())
                                        {
                                            listOfShoppingLists.add(document.getString("ListName"));
                                        }
                                    }
                                }
                            });*//*



                            // keep track of last time user retrieved featured recipes
                            *//*editor.putLong("FeaturedRecipes", new Date().getTime());
                            //verticalTitleList.add("Recommended for you");

                            if (!isPopulated)
                            {
                                viewModel.setHorizontalList(activeCategories);
                            }*//*
                        }
                    }*/
                }
            }
        });


        if (!isPopulated)
        {
            viewModel.setNewRecipesList();
            viewModel.setPopularRecipesThisWeekList();
        }


    }

    @Override
    public void onStop()
    {
        super.onStop();

        viewModel.setPopulated(true);

        categoryFragmentModel.dumpList();
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
