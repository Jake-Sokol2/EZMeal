package navigationFragments.FindRecipes;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ezmeal.RoomDatabase.CategoryEntity;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.Recipe;
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
 * Use the {@link FindRecipesSpecificRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindRecipesSpecificRecipeFragment extends Fragment
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

    private Button btnAddToMyRecipes;
    public String recipeId;

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

    public FindRecipesSpecificRecipeFragment() {
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
    public static FindRecipesSpecificRecipeFragment newInstance(String param1, String param2) {
        FindRecipesSpecificRecipeFragment fragment = new FindRecipesSpecificRecipeFragment();
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
        View view = inflater.inflate(R.layout.fragment_specific_recipe, container, false);

        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Specific Recipes opened.  Count: " + numOfBackstack);

        Bundle extras = getArguments();
        recipeId = extras.getString("id");
        Log.i("p", "a");

        ImageView imageRecipe = view.findViewById(R.id.imageRecipeImage);
        TextView txtRecipeTitle = view.findViewById(R.id.txtRecipeTitle);

        db = FirebaseFirestore.getInstance();
        CollectionReference dbRecipes = db.collection("Recipes");

        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                Glide.with(getContext()).load(Uri.parse(task.getResult().getString("imageUrl"))).into(imageRecipe);
                txtRecipeTitle.setText(task.getResult().getString("title"));

                Log.i("test", String.valueOf(task.getResult().getData()));
                Log.i("test", "pause");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });

        btnAddToMyRecipes = view.findViewById(R.id.btnAddToMyRecipes);
        btnAddToMyRecipes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // todo: make it so user can't add same recipe twice
                db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        ArrayList<String> categories = (ArrayList<String>) task.getResult().get("categories");
                        ArrayList<String> directions = (ArrayList<String>) task.getResult().get("directions");
                        ArrayList<String> ingredients = (ArrayList<String>) task.getResult().get("ingredients");
                        ArrayList<String> nutrition = (ArrayList<String>) task.getResult().get("nutrition");
                        String imageUrl = task.getResult().getString("imageUrl");
                        String title = task.getResult().getString("title");

                        // find largest list between categories, directions, ingredients, and nutrition
                        int maxSize = Collections.max(Arrays.asList(categories.size(), directions.size(), ingredients.size(), nutrition.size()));

                        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user").allowMainThreadQueries().fallbackToDestructiveMigration().build();

                        sqlDb.testDao().BOOM();
                        sqlDb.testDao().BOOOOOOOM();

                        Recipe newRecipe = new Recipe(imageUrl, title, recipeId);
                        sqlDb.testDao().insert(newRecipe);

                        // loop through and create a CategoryEntity instance for every category/direction/etc.  If you exceed one lists size, fill its entry with null
                        // this is really hacky, not sure if there's a better way to insert these lists given they are all of different sizes and doing it all in one loop
                            // would result in out of bound errors
                        for(int x = 0; x < maxSize; x++)
                        {
                            String cat;
                            String dir;
                            String ing;
                            String nut;

                            if (x < categories.size())
                            {
                               cat = categories.get(x);
                            }
                            else
                            {
                                cat = null;
                            }

                            if (x < directions.size())
                            {
                                dir = directions.get(x);
                            }
                            else
                            {
                                dir = null;
                            }

                            if (x < ingredients.size())
                            {
                                ing = ingredients.get(x);
                            }
                            else
                            {
                                ing = null;
                            }

                            if (x < nutrition.size())
                            {
                                nut = nutrition.get(x);
                            }
                            else
                            {
                                nut = null;
                            }

                            CategoryEntity item = new CategoryEntity(recipeId, cat, nut, dir, ing);
                            sqlDb.testDao().insertItem(item);
                        }

/*                        int i = 0;
                        while(i < categories.size() || i < directions.size() || i < ingredients.size() || i < nutrition.size())
                        {

                        }*/

                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
                        //String email = mCurrentUser.getEmail();

                        //UserRecipe savedRecipe = new UserRecipe(categories, directions, ingredients, nutrition, imageUrl, title, recipeId);


                        //CollectionReference dbRecipes = db.collection("UserRecipes");

                        //ArrayList<UserRecipe> userRecipeList = new ArrayList<UserRecipe>();
                        //userRecipeList.add(savedRecipe);


                        //getContext().deleteDatabase("EZMealDatabase");

                        /*db.collection("Recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    Log.i("retrieve", document.getId() + "=> " + document.getData());
                                    String title = document.getString("title");
                                    Uri uri = Uri.parse(document.getString("imageUrl"));

                                    //findRecipesModel.addItem(title, uri);
                                    //findRecipesAdapter.notifyDataSetChanged();
                                    //recipeId.add(document.getId());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                            }
                        });*/




                      String recipeIdString = "recipe 1";
                        String recipeIdString2 = "recipe 2";

                        /*EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user").allowMainThreadQueries().fallbackToDestructiveMigration().build();

                        sqlDb.testDao().BOOM();

                        Recipe newRecipe = new Recipe(imageUrl, title, recipeId);

                        sqlDb.testDao().insert(newRecipe);*/
                        //sqlDb.testDao().insertAllItems(recipeId, categories, ingredients, nutrition, directions);

                        /*Recipe recipeMe = new Recipe("cookies.webp", "title", recipeIdString);
                        Recipe recipeSomeone = new Recipe("spaghet.webp", "title2", recipeIdString2);

                        sqlDb.testDao().insert(recipeMe);
                        sqlDb.testDao().insert(recipeSomeone);
                        // String recipeId, String category, String nutrition, String direction, String ingredient

                        CategoryEntity item1 = new CategoryEntity(recipeIdString, "cat1", "n1", "d1", "i1");
                        CategoryEntity item2 = new CategoryEntity(recipeIdString, "cat2", "n1", "d1", "i1");
                        CategoryEntity item3 = new CategoryEntity(recipeIdString, null, "n1", "d1", "i1");
                        CategoryEntity item4 = new CategoryEntity(recipeIdString, null, "n1", "d1", "i1");
                        CategoryEntity item5 = new CategoryEntity(recipeIdString, null, "n1", "d1", null);

                        CategoryEntity item11 = new CategoryEntity(recipeIdString2, "cat11", "n11", "d11", "i11");
                        CategoryEntity item12 = new CategoryEntity(recipeIdString2, "cat22", "n11", "d11", "i11");
                        CategoryEntity item13 = new CategoryEntity(recipeIdString2, null, "n11", "d11", "i11");
                        CategoryEntity item14 = new CategoryEntity(recipeIdString2, null, "n11", "d11", "i11");
                        CategoryEntity item15 = new CategoryEntity(recipeIdString2, null, "n11", "d11", null);

                        sqlDb.testDao().insertItem(item1);
                        sqlDb.testDao().insertItem(item2);
                        sqlDb.testDao().insertItem(item3);
                        sqlDb.testDao().insertItem(item4);
                        sqlDb.testDao().insertItem(item5);
                        sqlDb.testDao().insertItem(item11);
                        sqlDb.testDao().insertItem(item12);
                        sqlDb.testDao().insertItem(item13);
                        sqlDb.testDao().insertItem(item14);
                        sqlDb.testDao().insertItem(item15);

                        List<Recipe> recipeList = sqlDb.testDao().getAll();*/

                        //sqlDb.testDao().BOOM();

                        Log.i("a","a");

                        /*
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("recipes", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(savedRecipe);
                        editor.putString(recipeId, json);

                        editor.apply();
                        */








                        /*dbRecipes.document(mCurrentUser.getUid()).collection("SpecificUserCollection").add(savedRecipe)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {

                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                            }
                        });*/

                        Log.i("test", String.valueOf(task.getResult().getData()));
                        Log.i("test", "pause");
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
            }
        });

        /*
        String recipeId = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            // retrieve category name from the Intent
            recipeId = extras.getString("id");
        }
         */
        /*
        if (savedInstanceState == null)
        {
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragmentContainerView, RecipeInstructionsFragment.class, null)
                    .commit();
        }
        */

        nestedScrollView = view.findViewById(R.id.nestedScrollNutrition);

        vpRecipe = view.findViewById(R.id.vpRecipe);
        tabRecipe = view.findViewById(R.id.tabRecipe);

        FragmentManager fragmentManager = getChildFragmentManager();
        vpAdapter = new RecipeViewPagerAdapter(fragmentManager, getLifecycle());
        vpRecipe.setAdapter(vpAdapter);

        //vpRecipe.setUserInputEnabled(false);
        vpRecipe.requestDisallowInterceptTouchEvent(true);

        TextView txt = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);

        new TabLayoutMediator(tabRecipe, vpRecipe, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                        //tab.setText("Ingredients");
                        TextView txtIngredients = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtIngredients.setText("Ingredients");
                        nestedScrollView = view.findViewById(R.id.nestedScrollIngredients);
                        tab.setCustomView(txtIngredients);
                        break;
                    case 1:
                        //tab.setText("Directions");
                        TextView txtDirections = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtDirections.setText("Directions");
                        nestedScrollView = view.findViewById(R.id.nestedScrollDirections);
                        tab.setCustomView(txtDirections);
                        break;
                    case 2:
                        //tab.setText("Nutrition");
                        TextView txtNutrition = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
                        txtNutrition.setText("Nutrition");
                        nestedScrollView = view.findViewById(R.id.nestedScrollNutrition);
                        tab.setCustomView(txtNutrition);
                        break;
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