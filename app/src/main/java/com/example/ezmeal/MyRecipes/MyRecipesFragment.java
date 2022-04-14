package com.example.ezmeal.MyRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.ezmeal.MyRecipes.RecipeAdapters.MyRecipesFragmentRecyclerAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.MyRecipesFragmentModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment
{
    private MyRecipesFragmentModel myRecipesFragmentModel = new MyRecipesFragmentModel();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvCategories;
    private MyRecipesFragmentRecyclerAdapter adapter;

    public List<String> cat;

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EZMealDatabase sqlDb;

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

        rvCategories = (RecyclerView) view.findViewById(R.id.rvMyRecipeCategories);
        adapter = new MyRecipesFragmentRecyclerAdapter(myRecipesFragmentModel.getCategoryList(), myRecipesFragmentModel.getUrl());
        rvCategories.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategories.setLayoutManager(linearLayoutManager);


        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().enableMultiInstanceInvalidation().build();

        // retrieve list of categories and some random images from Room
        cat = sqlDb.testDao().getCategories();
        //List<String> urls = sqlDb.testDao().getCatUrl();
        for (int i = 0; i < cat.size(); i++)
        {
            if (cat.get(i) != null)
            {
                List<String> url = sqlDb.testDao().getImagesForCategories(cat.get(i));
                myRecipesFragmentModel.addItem(cat.get(i), url.get(0));
            }
        }

        // todo: 4/1/2022        remove, is clearing entire Ratings db
        //sqlDb.testDao().BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOM();

        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new MyRecipesFragmentRecyclerAdapter.MainAdapterListener() {
            @Override
            public void onItemClick(int position, CardView cardView)
            {
                // name of the clicked category, gets sent to new Fragment and is later used to let Firebase know which data to populate Activity with
                Bundle bundle = new Bundle();
                bundle.putString("category", cat.get(position));

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_myRecipesFragment_to_myRecipesSpecificCategoryFragment, bundle, new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in)
                        .setExitAnim(R.anim.fade_out)
                        .setPopExitAnim(R.anim.slide_out)
                        .setPopEnterAnim(R.anim.fade_in)
                        .build());
            }
        });

        return view;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        myRecipesFragmentModel.dumpList();

        if (sqlDb.isOpen())
        {
            sqlDb.close();
        }
    }
}