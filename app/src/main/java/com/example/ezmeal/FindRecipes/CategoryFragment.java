package com.example.ezmeal.FindRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentFeaturedRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.RetrievedRecipeLists;
import com.example.ezmeal.FindRecipes.FindRecipesModels.VerticalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.CategoryFragmentViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.Category2;
import com.example.ezmeal.roomDatabase.CategoryWithRecipes;
import com.example.ezmeal.roomDatabase.Category_RecyclerRecipe;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class CategoryFragment extends Fragment
{
    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();
    private RecyclerView rvFindRecipes;
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private String category;
    private List<String> recipeIdList = new ArrayList<>();
    private EZMealDatabase sqlDb;
    private ArrayList<List<HorizontalRecipe>> horizontalLists = new ArrayList<List<HorizontalRecipe>>();
    private List<VerticalRecipe> verticalRecipes = new ArrayList<VerticalRecipe>();
    private List<CategoryWithRecipes> categoryWithRecipes = new ArrayList<>();
    private SwipeRefreshLayout swipeLayout;
    private final int RECIPE_RESET_TIME = 5;
    private boolean isPopulated;
    private CategoryFragmentViewModel viewModel;
    private String lastCategory;
    private UUID queryId;


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState = null;
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_recipes_category, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryFragmentViewModel.class);

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvFindRecipes.setLayoutManager(staggeredGridLayoutManager);
        rvFindRecipes.suppressLayout(true);
        rvFindRecipes.setVisibility(View.INVISIBLE);

        horizontalLists = new ArrayList<List<HorizontalRecipe>>();
        verticalRecipes = new ArrayList<VerticalRecipe>();

        categoryFragmentAdapter = new CategoryFragmentAdapter(verticalRecipes, horizontalLists);
        rvFindRecipes.setAdapter(categoryFragmentAdapter);
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
                    bundle.putString("id", recipeIdList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        // todo: remove view tree observer?  It used to be for setting visibiltiy but now viewmodel observer takes care of that
        /*rvFindRecipes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                //rvFindRecipes.setVisibility(View.VISIBLE);
                //rvFindRecipes.suppressLayout(false);
                rvFindRecipes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/

        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();

        Bundle extras = getArguments();

        if (extras != null)
        {
            category = extras.getString("cat");
        }

        swipeLayout = view.findViewById(R.id.swipeCategory);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // clear all category recipes from Room, to be overwritten with new ones
                deleteEntireCategory(category);

                verticalRecipes.clear();
                horizontalLists.clear();

                categoryFragmentAdapter.notifyDataSetChanged();

                queryId = UUID.randomUUID();
                viewModel.setDataOther(category, queryId);

                // turn off refreshing logo
                swipeLayout.setRefreshing(false);
            }
        });



        // Observers

        viewModel.getIsPopulated().observe(getViewLifecycleOwner(), returnIsPopulated ->
        {
            isPopulated = returnIsPopulated;
        });

        viewModel.getLastCategory().observe(getViewLifecycleOwner(), returnedCategory ->
        {
            lastCategory = returnedCategory;
        });

        viewModel.getDataOther().observe(getViewLifecycleOwner(), new Observer<RetrievedRecipeLists>()
        {
            @Override
            public void onChanged(RetrievedRecipeLists retrievedRecipeLists)
            {
                if (retrievedRecipeLists != null)
                {
                    horizontalLists.clear();
                    verticalRecipes.clear();
                    recipeIdList.clear();


                    List<HorizontalRecipe> tempHorizontalList = new ArrayList<>();
                    List<HorizontalRecipe> tempRetrievedHorizontalList = retrievedRecipeLists.getHorizontalList();

                    for (int i = 0; i < tempRetrievedHorizontalList.size(); i++)
                    {
                        //retrievedRecipeLists.getHorizontalList();
                        //
                        tempHorizontalList.add(tempRetrievedHorizontalList.get(i));
                    }

                    horizontalLists.add(tempHorizontalList);
                    //horizontalLists.add(retrievedRecipeLists.getHorizontalList());

                    List<VerticalRecipe> tempRetrievedVerticalList = retrievedRecipeLists.getVerticalList();
                    //tempVerticalList = retrievedRecipeLists.getVerticalList();

                    for (int i = 0; i < tempRetrievedVerticalList.size(); i++)
                    {
                        // tempRetrievedVerticalList.get(i).getQueryId() == queryId
                        verticalRecipes.add(tempRetrievedVerticalList.get(i));
                        recipeIdList.add(tempRetrievedVerticalList.get(i).getRecipeId());
                    }

                    // category may have changed since the query started due to async (if user spam clicks categories, launching multiple queries)
                    // do this check at the very last moment to reduce the amount of irrelevant queries that make it through
                    if (retrievedRecipeLists.getCategory() == category)
                    {
                        rvFindRecipes.setVisibility(View.VISIBLE);
                        rvFindRecipes.suppressLayout(false);
                        categoryFragmentAdapter.notifyDataSetChanged();
                    }

                }
            }
        });



        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        // if isPopulated, user is returning to an already created fragment.  Don't repopulate the fragment with new recipes, let LiveData handle it
        if (lastCategory != category)
        {
            viewModel.setLastCategory(category);
            viewModel.setPopulated(true);

            // user may have force closed app, clearing LiveData.  If they are still within time limit, pull saved recipes from Room

            if ((categoryWithRecipes.size() != 0) && (new Date().getTime()) < (categoryWithRecipes.get(0).category2.getDateRetrieved() + RECIPE_RESET_TIME))
            {
                // todo: get rid of this "category" line, its rigging the results
                category = "Cookies";
                viewModel.setDataWithSavedRecipes(category);
            }
            else
            {
                categoryFragmentAdapter.notifyDataSetChanged();

                queryId = UUID.randomUUID();
                viewModel.setDataOther(category, queryId);
                viewModel.setPopulated(true);
            }
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

// todo: actually keep this one - adapt it
    /*public void addToRecycler(List<RecyclerRecipe2> list)
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
                recipeIdList.add(list.get(i).getRecipeId());
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
    }*/

    public void deleteEntireCategory(String cat)
    {
        sqlDb.testDao().deleteFromCategory2SpecificCategory(cat);
        sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(cat);
    }
}