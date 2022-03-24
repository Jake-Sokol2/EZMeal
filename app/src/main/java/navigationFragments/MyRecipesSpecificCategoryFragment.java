package navigationFragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesSpecificCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesSpecificCategoryFragment extends Fragment {

    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroceryListModel theModel = new GroceryListModel();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private RecipeSpecificCategoryRecyclerAdapter adapter;

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
        View view = inflater.inflate(R.layout.activity_my_recipes_single_category, container, false);

        //mCard = view.findViewById(R.id.cardSingleCategory);
        //ViewCompat.setTransitionName(mCard, "test");
        //postponeEnterTransition(2, TimeUnit.SECONDS);

        String categoryName = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            // retrieve category name from the Intent
            categoryName = extras.getString("category name");
        }

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvMyRecipes);
        adapter = new RecipeSpecificCategoryRecyclerAdapter(theModel.getGroceryList());
        rvGroupList.setAdapter(adapter);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        //rvGroupList.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        // LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvGroupList.setLayoutManager(gridLayoutManager);
        theModel.addItem("Chicken", "R.drawable.crispy_fried_chicken_exps_tohjj22_6445_dr__02_03_11b");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");

        theModel.addItem("Milk", "");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");
        theModel.addItem("Milk", "");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");
        theModel.addItem("Milk", "");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");
        theModel.addItem("Milk", "");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");
        theModel.addItem("Milk", "");
        theModel.addItem("Fruit", "");
        theModel.addItem("Huevo", "");


        adapter.setOnItemClickListener(new RecipeSpecificCategoryRecyclerAdapter.MainAdapterListener() {
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

                Fragment endFrag = new SpecificRecipeFragment();
                // FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

                //AutoTransition at = new AutoTransition();

                //setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.anim.stall));

                //endFrag.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
                //endFrag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

                String name = "transition" + position;

                //Bundle bundle = new Bundle();
                //bundle.putString("category", ViewCompat.getTransitionName(cardView));
                //endFrag.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()

                        //.addSharedElement(cardView, "test")
                        //       animations:    enter            exit          popEnter        popExit
                        .setCustomAnimations(R.anim.slide_in, R.anim.stall, R.anim.fade_in, R.anim.slide_out)
                        .addToBackStack("specific recipe fragment")
                        .replace(R.id.fragContainer, endFrag)
                        .commit();

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
}