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
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.FeaturedFragmentRoomRepository;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.FeaturedFragmentViewModel;
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
import java.util.concurrent.ExecutionException;

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

    private FeaturedFragmentRoomRepository testR;

    private ArrayList<List<HorizontalRecipe>> horizontalLists = new ArrayList<List<HorizontalRecipe>>();
    private FeaturedFragmentViewModel viewModel;

    private final int RECIPE_RESET_TIME = 5;

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

        RecyclerRecipe2 test = new RecyclerRecipe2("Cookies", "D1ccah7inhNbzXfLk04C", "Lace Cookies (Florentine Cookies)", "test", 4, "test", true, 1);
        sqlDb.testDao().insertRecyclerRecipe2(test);
        RecyclerRecipe2 test2 = new RecyclerRecipe2("Breakfast", "9rDucWsmgDgfVam3cdqp", "Best Buckwheat Pancakes", "test", 4, "test", true, 1);
        sqlDb.testDao().insertRecyclerRecipe2(test2);
        RecyclerRecipe2 test3 = new RecyclerRecipe2("Breakfast", "V2IQKIBoxP5WFwqYGzuh", "Oatmeal Pancakes II", "test", 4, "test", true, 1);
        sqlDb.testDao().insertRecyclerRecipe2(test3);




            // Firebase
        db = FirebaseFirestore.getInstance();
        dbRecipes = db.collection("RecipesRatingBigInt");

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

            viewModel.getActiveCategoriesFromIdentifier().observe(getViewLifecycleOwner(), new Observer<List<String>>()
            {
                @Override
                public void onChanged(List<String> activeCategories)
                {
                    if (activeCategories != null)// && list.size() > 0)
                    {
                        if (activeCategories.size() > 0)
                        {
                            //getActivity().getSharedPreferences("FeaturedRecipes", 0).edit().clear().commit();

                            SharedPreferences sp;
                            sp = getActivity().getSharedPreferences("FirstRunAfterUpdate", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sp.edit();
                            long timeLastEntered = sp.getLong("FeaturedRecipes", 0);

                            if ((timeLastEntered != 0) && (new Date().getTime()) < timeLastEntered + RECIPE_RESET_TIME)
                            {
                                //List<RecyclerRecipe2> listOfRecyclerRecipes2 = categoryWithRecipes.get(0).recyclerRecipe2List;
                                //addToRecycler(listOfRecyclerRecipes2);
                            }
                            else
                            {
                                // keep track of last time user retrieved featured recipes
                                editor.putLong("FeaturedRecipes", new Date().getTime());
                                verticalTitleList.add("Recommended for you");

                                viewModel.getHorizontalList(activeCategories).observe(getViewLifecycleOwner(), list ->
                                {
                                    if (list != null)// && list.size() > 0)
                                    {
                                        horizontalLists.add(list);
                                        categoryFragmentFeaturedAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
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
}
