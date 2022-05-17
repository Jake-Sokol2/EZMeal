package com.example.ezmeal.FindRecipes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.FindRecipesFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.FindRecipesViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindRecipesFragment extends Fragment
{
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private EZMealDatabase sqlDb;

    private ArrayList<List<String>> recipeList = new ArrayList<List<String>>();
    List<String> list = new ArrayList<String>();
    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();
    private FindRecipesFragmentModel findRecipesFragmentModel = new FindRecipesFragmentModel();

    private RecyclerView rvFindRecipes;
    private RecyclerView rvHorizontal;
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private FindRecipesFragmentHorizontalRecyclerAdapter horizontalAdapter;

    private List<String> recipeId = new ArrayList<String>();

    public List<String> categories = new ArrayList<String>();
    private List<String> displayCategories = new ArrayList<String>();
    private List<Boolean> isSelected = new ArrayList<>();
    private Integer lastSelectedCategory = 0;

    private FindRecipesViewModel viewModel;

    private int currentSelectedCategoryPosition = 0;
    public ImageView imageView;
    public String[] imageList;
    public Bitmap[] bitmapList;
    public String[] titleList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindRecipesFragment newInstance(String param1, String param2) {
        FindRecipesFragment fragment = new FindRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_recipes, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FindRecipesViewModel.class);

        rvHorizontal = (RecyclerView) view.findViewById(R.id.rvHorizontalSelector);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvHorizontal.setLayoutManager(horizontalLayoutManager);

        horizontalAdapter = new FindRecipesFragmentHorizontalRecyclerAdapter(categories, isSelected);
        rvHorizontal.setAdapter(horizontalAdapter);

        viewModel.getHorizontalRecyclerModel().observe(getViewLifecycleOwner(), model ->
        {
            categories = model.getCategoryList();
            displayCategories = model.getDisplayCategoryList();
            isSelected = model.getIsSelectedList();
            horizontalAdapter.setData(model.getCategoryList(), model.getIsSelectedList());
            horizontalAdapter.notifyDataSetChanged();
        });

        if (savedInstanceState == null)
        {
            FindRecipesFragmentModel fm = new FindRecipesFragmentModel();
            List<String> categoryList = new ArrayList<String>();
            List<Boolean> isSelectedList = new ArrayList<Boolean>();
            fm.setCategoryList(categoryList);
            fm.setIsSelectedList(isSelectedList);
            viewModel.setHorizontalRecyclerModel(fm);

            db = FirebaseFirestore.getInstance();

            // todo: RecipesRating
            CollectionReference dbRecipes = db.collection("Recipes");

            db.collection("RecipeCategoryRatingList").document("categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    //viewModel.addItem("Featured", true);
                    categories = (ArrayList<String>) task.getResult().get("categories");

                    for (int i = 0; i < categories.size(); i++)
                    {
                        if (i == 0)
                        {
                            viewModel.addItem(categories.get(i), true);
                        }
                        else
                        {
                            viewModel.addItem(categories.get(i), false);
                        }

                        //findRecipesFragmentModel.addItem(categories.get(i), false);
                    }


                    // retrieve categories from SQL here


                    horizontalAdapter.notifyDataSetChanged();
                }
            });
        }

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        /*FragmentManager categoryFragManager = getChildFragmentManager();
        Fragment frag = new FeaturedFragment();
        categoryFragManager.beginTransaction().replace(R.id.fragmentContainerView4, frag).commit();*/

        //getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();


        viewModel.getLastSelectedCategory().observe(getViewLifecycleOwner(), categoryPosition ->
        {
            lastSelectedCategory = categoryPosition;
        });

        horizontalAdapter.setOnItemClickListener(new FindRecipesFragmentHorizontalRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {

                Fragment fragCategoryClick;


                // position 0 is "featured" section, which is not stored as a category in the database and would cause a crash if passed in as one
                // only pass a bundle if the user selects a card other than featured
                if (position != 0)
                {
                    fragCategoryClick = new CategoryFragment();

                    viewModel.setSelected(lastSelectedCategory, false);
                    viewModel.setSelected(0, false);
                    viewModel.setSelected(position, true);
                    viewModel.setLastSelectedCategory(position);


                    Bundle categoryBundleClick = new Bundle();
                    categoryBundleClick.putString("cat", categories.get(position));
                    fragCategoryClick.setArguments(categoryBundleClick);

                    getChildFragmentManager().popBackStack();

                    getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragCategoryClick).commit();
                }
                else
                // featured was clicked, set last category to unclicked (visually) and set featured to clicked
                {
                    fragCategoryClick = new FeaturedFragment();

                    viewModel.setSelected(lastSelectedCategory, false);
                    viewModel.setSelected(0, true);
                    viewModel.setLastSelectedCategory(0);


                    getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragCategoryClick).commit();
                }
                horizontalAdapter.notifyDataSetChanged();

                //getChildFragmentManager().popBackStackImmediate();

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