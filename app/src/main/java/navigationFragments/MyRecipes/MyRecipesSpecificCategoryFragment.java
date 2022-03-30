package navigationFragments.MyRecipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.RoomDatabase.RecipeCategoryTuple;
import com.example.ezmeal.RoomDatabase.recipePathTitle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.RecipeSpecificCategoryRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.SpecificCategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesSpecificCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesSpecificCategoryFragment extends Fragment {

    private ArrayList<List<String>> recipeList = new ArrayList<List<String>>();
    private SpecificCategoryModel specificCategoryModel = new SpecificCategoryModel();

    public BottomNavigationView bottomNav;

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private SpecificCategoryAdapter adapter;

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

    public MyRecipesSpecificCategoryFragment() {
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
    public static MyRecipesSpecificCategoryFragment newInstance(String param1, String param2) {
        MyRecipesSpecificCategoryFragment fragment = new MyRecipesSpecificCategoryFragment();
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

        //Transition transition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_right);
        //setEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recipes_single_category, container, false);

        // back stack logs
        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Specific Category opened.  Count: " + numOfBackstack);

        //mCard = view.findViewById(R.id.cardSingleCategory);
        //ViewCompat.setTransitionName(mCard, "test");
        //postponeEnterTransition(2, TimeUnit.SECONDS);
        bottomNav =  (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
        txtTitle = (TextView) view.findViewById(R.id.txtmyRecipesTitle);
        //bottomNav.setSelectedItemId(R.id.groupListsFragment);

        String categoryName = null;
        Bundle extras = getArguments();
        if (extras != null)
        {
            // retrieve category name from the Intent
            categoryName = extras.getString("category");
            txtTitle.setText(categoryName);
        }

        //((BottomNavigationView) view.findViewById(R.id.bottomNavigationView)).setSelectedItemId(R.id.groupRecipesFragment);

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvMyRecipes);
        adapter = new SpecificCategoryAdapter(specificCategoryModel.getRecipeList(), specificCategoryModel.getUrlList());
        rvGroupList.setAdapter(adapter);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        //rvGroupList.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvGroupList.setLayoutManager(gridLayoutManager);

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        List<recipePathTitle> items = sqlDb.testDao().getCategoryRecipes(categoryName);

        recipeId = new ArrayList<String>();
        for(int i = 0; i < items.size(); i++)
        {
            specificCategoryModel.addItem(items.get(i).getTitle(), items.get(i).getPathToImage());
            recipeId.add(items.get(i).getRecipeId());
        }

        adapter.notifyDataSetChanged();

        //List<RecipeCategoryTuple> recipes = sqlDb.testDao().getSpecificCategoryItems();
        //for(int i = 0; i < 2; i++)
        //{
/*        String image = "cookies.webp";
        int id = getResources().getIdentifier("com.example.ezmeal:drawable/" + image, null, null);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        specificCategoryModel.addItem("test", bitmap);
        image = "spaghet.webp";
        id = getResources().getIdentifier("com.example.ezmeal:drawable/" + image, null, null);
        bitmap = BitmapFactory.decodeResource(getResources(), id);
        specificCategoryModel.addItem("test", bitmap);*/
            //specificCategoryModel.addItem();
        //}

        //specificCategoryModel.addItem("Chicken", "R.drawable.crispy_fried_chicken_exps_tohjj22_6445_dr__02_03_11b");

        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        //Gson gson = new Gson();
        //String json = sharedPreferences.getString("ghTwqzYq3mmp9apaZUZm", null);
        //Type type = new TypeToken<UserRecipe>(){}.getType();
        //UserRecipe recipe = gson.fromJson(sharedPreferences.getString("ghTwqzYq3mmp9apaZUZm", ""), UserRecipe);
        //UserRecipe userRecipe = gson.fromJson(json, UserRecipe);

        //String json = sharedPreferences.getString("ghTwqzYq3mmp9apaZUZm", null);
        //UserRecipe userRecipe = gson.fromJson(json, UserRecipe.class);

/*        InputStream inputStream = null;
        AssetManager assetManager = getActivity().getAssets();
        try
        {
*//*            inputStream = assetManager.open("co.webp");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);
            specificCategoryModel.addItem("test", bitmap);*//*
            adapter.notifyDataSetChanged();
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        //Bitmap bit = null;

        //specificCategoryModel.addItem("some title", bit);


        //specificCategoryModel.addItem(userRecipe.getTitle(), Uri.parse(userRecipe.getImageUrl()));

        //recipeId = new ArrayList<String>();

        //recipeId.add("ghTwqzYq3mmp9apaZUZm");

        adapter.setOnItemClickListener(new SpecificCategoryAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, CardView cardView)
            {
                //String selectedName = groceryList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                //Toast.makeText(getContext(), Integer.toString(cardView.getId()), Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…

                // todo: fix this when database is working
                // name of the clicked category, gets sent to new Activity and is later used to let Firebase know which data to populate Activity with

                String categoryName = "Chicken";


                // FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                //AutoTransition at = new AutoTransition();

                //setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.anim.stall));

                //endFrag.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //endFrag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

                String name = "transition" + position;

                Bundle bundle = new Bundle();

                // pass recipeId to specific recipe page so that it knows which recipe to use
                bundle.putString("id", recipeId.get(position));

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_myRecipesSpecificCategoryFragment_to_myRecipesSpecificRecipeFragment, bundle, new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in)
                        .setExitAnim(R.anim.stall)
                        .setPopExitAnim(R.anim.slide_out)
                        .build());


                /*
                Fragment endFrag = new SpecificRecipeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        //.addSharedElement(cardView, "test")
                        //       animations:    enter            exit          popEnter        popExit
                        .setCustomAnimations(R.anim.slide_in, R.anim.stall, R.anim.fade_in, R.anim.slide_out)
                        .addToBackStack("specific_recipe")
                        .replace(R.id.fragContainer, endFrag)
                        .commit();
                 */
                /*
                Intent intent = new Intent(getActivity(), MyRecipesSingleCategory.class);

                // key / value pair, passes extra info to the new Activity
                intent.putExtra("category name", categoryName);
                //TextView tv = view.findViewById(R.id.textView);

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        new Pair<>((cardView), ("a")));
                ActivityOptionsCompat second = activityOptionsCompat;

                ActivityCompat.startActivity(getActivity(), intent, activityOptionsCompat.toBundle());
                */
            }
        });





        //TextView txt = view.findViewById(R.id.txtMyRecipeTitle);
        //txt.setText(categoryName);
        //loadItem();

        return view;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        specificCategoryModel.dumpList();
    }
}