package navigationFragments.MyRecipes;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesNutritionRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesSingleRecipeRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.RecipeViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpecificRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecificRecipeFragment extends Fragment
{
    private FirebaseFirestore db;

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

    public SpecificRecipeFragment() {
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
    public static SpecificRecipeFragment newInstance(String param1, String param2) {
        SpecificRecipeFragment fragment = new SpecificRecipeFragment();
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

        String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        Log.i("TRACK BACKSTACK", "Specific Recipes opened.  Count: " + numOfBackstack);

        Bundle extras = getArguments();
        String recipeId = extras.getString("id");
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

        FragmentManager fragmentManager = getParentFragmentManager();
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

        //binding = ActivityMainBinding.inflate(this, container, false);
        //binding = DataBindingUtil.setContentView(this, R.layout.fragment_my_recipes_specific_recipe);
        //binding = FragmentMyRecipesSpecificRecipeBinding.inflate(inflater);

        // sets each tab to have a custom view
        //for (int i = 0; i < 3; i++)
        //{
            //TextView txt = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_name, null);
            //tabRecipe.getTabAt(i).setCustomView(txt);

            //binding.tabRecipe.getTabAt(i).setCustomView(txt);

            //tabRecipe.getTabAt(i).setCustomView(txt);


            /*
            // code for changing the width of selected item highlight image in tablayout
            LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabRecipe.getChildAt(0)).getChildAt(i));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.weight = 0f;
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            linearLayout.setLayoutParams(layoutParams);
            */
            //TabLayout.Tab tab = tabRecipe.getTabAt(i);


            //tab.setCustomView(R.layout.tab_name);


        //}


/*
        rvGroupList = (RecyclerView) view.findViewById(R.id.rvRecipe);
        adapter = new MyRecipesSingleRecipeRecyclerAdapter(theModel.getGroceryList());
        rvGroupList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        //rvGroupList.setLayoutManager(gridLayoutManager);

        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");

        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");

        adapter.notifyDataSetChanged();

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        motionLayout = view.findViewById(R.id.motionLayout);

        // If scrollview is at the top (not scrolled), allow MotionLayout transition.  Otherwise, disable MotionLayout transition
        // prevents the user from pulling the image up and down while the scrollview is scrolled.  Image should only move when scrollview is at the top
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
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
        });
/*








        /*
        adapter.setOnItemClickListener(new MyRecipesSingleRecipeRecyclerAdapter() {
            @Override
            public void onItemClick(int position, CardView cardView)
            {
                //String selectedName = groceryList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                Toast.makeText(getContext(), Integer.toString(cardView.getId()), Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…

                // todo: fix this when database is working
                // name of the clicked category, gets sent to new Activity and is later used to let Firebase know which data to populate Activity with

                String categoryName = "Chicken";

                Fragment frag = new MyRecipesSpecificCategoryFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                Intent intent = new Intent(getActivity(), MyRecipesSingleCategory.class);

                // key / value pair, passes extra info to the new Activity
                intent.putExtra("category name", categoryName);
                //TextView tv = view.findViewById(R.id.textView);

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        new Pair<>((cardView), ("a")));
                ActivityOptionsCompat second = activityOptionsCompat;

                ActivityCompat.startActivity(getActivity(), intent, activityOptionsCompat.toBundle());


            }
        });

         */

        // fragment transition code, old
        /*binding = FragmentMyRecipesAnimatedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();



        cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });*/


        /*
        rvMyRecipes = (RecyclerView) view.findViewById(R.id.rvGroupLists);
        adapter = new MainRecyclerAdapter(myRecipeCategory);
        rvMyRecipes.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvMyRecipes.setLayoutManager(layoutManager);

        // Add some data
        // todo: remove this when user's list saves on application close
        myRecipesList = new ArrayList<String>();
        myRecipesList.add("Gallon of Milk");
        myRecipesList.add("milk brand");
        myRecipeCategory.add(myRecipesList);

        myRecipesList = new ArrayList<String>();
        myRecipesList.add("Fruit");
        myRecipesList.add("fruit brand");
        myRecipeCategory.add(myRecipesList);

        myRecipesList = new ArrayList<String>();
        myRecipesList.add("Eggs");
        myRecipesList.add("egg brand");
        myRecipeCategory.add(myRecipesList);
        adapter.notifyDataSetChanged();
        */

        // old size expand code
        /*cardView = (MaterialCardView) view.findViewById(R.id.cardChickenAnimated);
        ConstraintLayout cl = (ConstraintLayout) view.findViewById(R.id.clChickenAnimated);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.frameCategoriesAnimated);
        TextView txtChicken = (TextView) view.findViewById(R.id.txtChickenAnimated);
        ImageView imageChicken = (ImageView) view.findViewById(R.id.imageChickenAnimated);
        ScrollView sv = (ScrollView) view.findViewById(R.id.scrollMyRecipesAnimated);
        vm = new ViewModelProvider(this).get(MyRecipesFragmentViewModel.class);

        cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // todo: figure out how to save this info every time a category is created.  Its the key to returning to original size + location
                // FrameLayout.LayoutParams originalLayoutParams = (FrameLayout.LayoutParams) cardView.getLayoutParams();
                // cardView.setLayoutParams(originalLayoutParams);

                FrameLayout.LayoutParams layoutMargins = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                layoutMargins.setMargins(0,0,0,0);
                fl.setPadding(0,0,0,0);
                cardView.setLayoutParams(layoutMargins);
                //sv.setFillViewport(false);

                // todo: figure out how to find height + width at runtime because apparently MATCH_PARENT doesn't actually MATCH_PARENT
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

                DisplayMetrics displayMetrics = new DisplayMetrics();

                Display display = wm.getDefaultDisplay();

                Point size = new Point();
                display.getRealSize(size);

                FrameLayout.LayoutParams layoutParams = vm.setCardWidthHeight(1, 1);
                cardView.setLayoutParams(layoutParams);
                //cardView.setRadius(10f);
                //FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams();
                //layoutParams.setMargins(0, 10, 0, 210);
                //cardView.setLayoutParams(layoutParams);


                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(cl);
                constraintSet.connect(R.id.txtChickenAnimated, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                constraintSet.setHorizontalBias(R.id.txtChickenAnimated, .1f);
                constraintSet.setVerticalBias(R.id.txtChickenAnimated, .03f);
                txtChicken.setTextSize(40);
                constraintSet.applyTo(cl);
                imageChicken.setVisibility(View.GONE);

                //constraintSet = new ConstraintSet();
                //constraintSet.clone(cl);
                //constraintSet.connect(R.id.imageChicken, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);


                //constraintSet.setHorizontalBias(R.id.imageChicken, .5f);
                //constraintSet.applyTo(cl);

                //constraintSet = new ConstraintSet();
                //constraintSet.connect(R.id.imageChicken, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                //constraintSet.setVerticalBias(R.id.imageChicken, .8f);
                //constraintSet.applyTo(cl);
                //cardView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                cardView.setElevation(10);
            }
        });*/



/*
        rvNutritionList = (RecyclerView) view.findViewById(R.id.rvNutritionalInfo);
        nutritionAdapter = new MyRecipesNutritionRecyclerAdapter(nutritionModel.getGroceryList());
        rvNutritionList.setAdapter(nutritionAdapter);
        RecyclerView.LayoutManager nutritionLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);  //   , LinearLayoutManager.HORIZONTAL, false
        rvNutritionList.setLayoutManager(nutritionLayoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        //rvGroupList.setLayoutManager(gridLayoutManager);

        nutritionModel.addItem("Milk", "milk brand");
        nutritionModel.addItem("Fruit", "fruit brand");
        nutritionModel.addItem("Huevo", "Huevo del super");

        nutritionModel.addItem("Milk", "milk brand");
        nutritionModel.addItem("Fruit", "fruit brand");
        nutritionModel.addItem("Huevo", "Huevo del super");
        nutritionModel.addItem("Milk", "milk brand");
        nutritionModel.addItem("Fruit", "fruit brand");
        nutritionModel.addItem("Huevo", "Huevo del super");
        nutritionModel.addItem("Milk", "milk brand");
        nutritionModel.addItem("Fruit", "fruit brand");
        nutritionModel.addItem("Huevo", "Huevo del super");
        nutritionModel.addItem("Milk", "milk brand");
        nutritionModel.addItem("Fruit", "fruit brand");
        nutritionModel.addItem("Huevo", "Huevo del super");


        nutritionAdapter.notifyDataSetChanged();
*/
        return view;
    }
}