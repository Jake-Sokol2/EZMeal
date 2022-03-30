package navigationFragments.MyRecipes;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.transition.AutoTransition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;


import java.util.ArrayList;
import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeModels.MyRecipesModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment
{
    //private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private MyRecipesModel myRecipesModel = new MyRecipesModel();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvCategories;
    private MyRecipesRecyclerAdapter adapter;
    // old size expand code
    /*private List<List<String>> myRecipeCategory = new ArrayList<List<String>>();
    List<String> myRecipesList = new ArrayList<String>();
    private RecyclerView rvMyRecipes;
    private MainRecyclerAdapter adapter;
    private View cardView;
    public MyRecipesFragmentViewModel vm;
    */

    public List<String> cat;
    private View cardView;
    //private FragmentMyRecipesAnimatedBinding binding;

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_recipes_category, container, false);

        // back stack logs
        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "My Recipes opened: " + numOfBackstack);

        rvCategories = (RecyclerView) view.findViewById(R.id.rvMyRecipeCategories);
        adapter = new MyRecipesRecyclerAdapter(myRecipesModel.getCategoryList(), myRecipesModel.getUrl());
        rvCategories.setAdapter(adapter);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        //rvGroupList.setLayoutManager(layoutManager);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(linearLayoutManager);

        //theModel.addItem("Milk", "milk brand");

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        cat = sqlDb.testDao().getCategories();
        List<String> urls = sqlDb.testDao().getCatUrl();
        for (int i = 0; i < cat.size(); i++)
        {
            if (cat.get(i) != null)
            {
                myRecipesModel.addItem(cat.get(i), urls.get(i));
            }
        }

        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new MyRecipesRecyclerAdapter.MainAdapterListener() {
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

                //Fragment endFrag = new MyRecipesSpecificCategoryFragment();
                // FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                AutoTransition at = new AutoTransition();

                //setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.anim.stall));

                //endFrag.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //endFrag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

                String name = "transition" + position;

                Bundle bundle = new Bundle();
                bundle.putString("category", cat.get(position));

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_myRecipesFragment_to_myRecipesSpecificCategoryFragment, bundle, new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in)
                        .setExitAnim(R.anim.fade_out)
                        .setPopExitAnim(R.anim.slide_out)
                        .setPopEnterAnim(R.anim.fade_in)
                        .build());


                /*
                endFrag.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()

                        //.addSharedElement(cardView, "test")
                        //       animations:    enter            exit          popEnter        popExit
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .addToBackStack("specific_category")
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

        return view;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        myRecipesModel.dumpList();
    }

/*    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        Parcelable rvState = rvGroupList.getLayoutManager().onSaveInstanceState();
        super.onSaveInstanceState(outState);
        //I need to save the grocery list here
        //save recycler view position?
        outState.putParcelable(RECYCLER_VIEW_KEY, rvState);
        //save recycler view items?
        outState.putSerializable(RV_DATA, (Serializable) theModel.getGroceryList());
        //getChildFragmentManager().putFragment(outState, "bottom_dialog", bottomSheetDialogFrag);


    }*/
}