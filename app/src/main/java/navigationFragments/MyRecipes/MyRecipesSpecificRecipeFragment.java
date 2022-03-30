package navigationFragments.MyRecipes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ezmeal.MainActivity;
import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.CategoryEntity;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.Recipe;
import com.example.ezmeal.RoomDatabase.RecipeCategoryTuple;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesNutritionRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesSingleRecipeRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.RecipeViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesSpecificRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesSpecificRecipeFragment extends Fragment
{
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroceryListModel theModel = new GroceryListModel();
    private ArrayList<List<String>> nutritionList = new ArrayList<List<String>>();
    private GroceryListModel nutritionModel = new GroceryListModel();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private MyRecipesSingleRecipeRecyclerAdapter adapter;
    List<String> nutritionListInner = new ArrayList<String>();
    private RecyclerView rvNutritionList;
    private MyRecipesNutritionRecyclerAdapter nutritionAdapter;

    private MotionLayout motionLayout;
    private NestedScrollView nestedScrollView;

    private Button btnDeleteFromMyRecipes;
    public String recipeId;
    private boolean confirmChoice;

    //private FragmentMyRecipesSpecificRecipeBinding binding;

    ViewPager2 vpRecipe;
    TabLayout tabRecipe;
    RecipeViewPagerAdapter vpAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRecipesSpecificRecipeFragment() {
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
    public static MyRecipesSpecificRecipeFragment newInstance(String param1, String param2) {
        MyRecipesSpecificRecipeFragment fragment = new MyRecipesSpecificRecipeFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_recipes_specific_recipe, container, false);

        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Specific Recipes opened.  Count: " + numOfBackstack);

        Bundle extras = getArguments();
        recipeId = extras.getString("id");

        ImageView imageRecipe = view.findViewById(R.id.imageRecipeImage);
        TextView txtRecipeTitle = view.findViewById(R.id.txtRecipeTitle);

        String queryString = "";
        String imageUrl = "";

        boolean containsCondition = false;

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        RecipeCategoryTuple recipeCategoryTuple = sqlDb.testDao().getSpecificCategoryItems(recipeId);

        // insert text and image from Room database query into the ImageView
        txtRecipeTitle.setText(recipeCategoryTuple.getTitle());
        Glide.with(getContext()).load(Uri.parse(recipeCategoryTuple.getPathToImage())).into(imageRecipe);


        confirmChoice = false;

        btnDeleteFromMyRecipes = view.findViewById(R.id.btnDeleteFromMyRecipes);
        btnDeleteFromMyRecipes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setTitle("Remove " + recipeCategoryTuple.getTitle()).setMessage("Are you sure you want to delete this recipe?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        confirmChoice = true;
                        dialogInterface.dismiss();

                        // if user clicked yes in confirm deletion dialog, delete the recipe and navigate up to the previous screen
                        if (confirmChoice)
                        {
                            sqlDb.testDao().deleteSingleRecipeFromItem(recipeId);
                            sqlDb.testDao().deleteSingleRecipeFromRecipe(recipeId);
                            Navigation.findNavController(getActivity(), R.id.fragContainer).navigateUp();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        nestedScrollView = view.findViewById(R.id.nestedScrollNutrition);

        vpRecipe = view.findViewById(R.id.vpRecipe);
        tabRecipe = view.findViewById(R.id.tabRecipe);

        FragmentManager fragmentManager = getChildFragmentManager();
        vpAdapter = new RecipeViewPagerAdapter(fragmentManager, getLifecycle());
        vpRecipe.setAdapter(vpAdapter);

        vpRecipe.requestDisallowInterceptTouchEvent(true);

        TextView txt = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);

        new TabLayoutMediator(tabRecipe, vpRecipe, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                    {
                        TextView txtIngredients = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtIngredients.setText("Ingredients");
                        nestedScrollView = view.findViewById(R.id.nestedScrollIngredients);
                        tab.setCustomView(txtIngredients);
                        break;
                    }
                    case 1:
                    {
                        TextView txtDirections = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtDirections.setText("Directions");
                        nestedScrollView = view.findViewById(R.id.nestedScrollDirections);
                        tab.setCustomView(txtDirections);
                        break;
                    }
                    case 2:
                    {
                        TextView txtNutrition = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtNutrition.setText("Nutrition");
                        nestedScrollView = view.findViewById(R.id.nestedScrollNutrition);
                        tab.setCustomView(txtNutrition);
                        break;
                    }
                }
            }
        }).attach();

        // If scrollview is at the top (not scrolled), allow MotionLayout transition.  Otherwise, disable MotionLayout transition
        // prevents the user from pulling the image up and down while the scrollview is scrolled.  Image should only move when scrollview is at the top
        /*nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (nestedScrollView.getScrollY() == 0)
                {
                    motionLayout.getTransition(R.id.recipeTransition).setEnable(true);
                }
                else
                {
                    motionLayout.getTransition(R.id.recipeTransition).setEnable(false);
                }
            }
        });*/

        return view;
    }

    public class InsertAsynchTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            return null;
        }
    }
}