package com.example.ezmeal.MyRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.example.ezmeal.MyRecipes.RecipeAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.CategoryFragmentModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.recipePathTitle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    private ArrayList<List<String>> recipeList = new ArrayList<List<String>>();
    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();

    public BottomNavigationView bottomNav;

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private CategoryFragmentAdapter adapter;

    private List<String> recipeId;
    private TextView txtTitle;

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private CardView mCard;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipesSpecificCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.activity_find_recipes_specific_recipe, container, false);

        bottomNav =  (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
        txtTitle = (TextView) view.findViewById(R.id.txtmyRecipesTitle);

        String categoryName = null;
        Bundle extras = getArguments();
        if (extras != null)
        {
            // retrieve category name from the Intent
            categoryName = extras.getString("category");
            txtTitle.setText(categoryName);
        }

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvMyRecipes);
        adapter = new CategoryFragmentAdapter(categoryFragmentModel.getRecipeList(), categoryFragmentModel.getUrlList());
        rvGroupList.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
/*
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                if (position == 6)
                {
                    return 3;
                }
                else
                {
                    return 1;
                }
            }
        });*/

        rvGroupList.setLayoutManager(staggeredManager);

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // get recipeId, pathToImage, and title for recipes of a specific category
        List<recipePathTitle> items = sqlDb.testDao().getCategoryRecipes(categoryName);

        recipeId = new ArrayList<String>();
        for(int i = 0; i < items.size(); i++)
        {
            categoryFragmentModel.addItem(items.get(i).getTitle(), items.get(i).getPathToImage());

            // keep list of recipeId's in memory so we know which recipeId to pass to the next fragment when the user clicks on a recipe
            recipeId.add(items.get(i).getRecipeId());
        }

        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new CategoryFragmentAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, CardView cardView)
            {

                Bundle bundle = new Bundle();

                // pass recipeId to specific recipe page so that it knows which recipe to use
                bundle.putString("id", recipeId.get(position));

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_myRecipesSpecificCategoryFragment_to_myRecipesSpecificRecipeFragment, bundle, new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in)
                        .setExitAnim(R.anim.stall)
                        .setPopExitAnim(R.anim.slide_out)
                        .build());
            }
        });

        return view;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        categoryFragmentModel.dumpList();
    }
}