package com.example.ezmeal.FindRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeDirectionsFragmentRecyclerAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.RecipeDirectionsFragmentModel;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDirectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDirectionsFragment extends Fragment
{
    private RecipeDirectionsFragmentModel recipeDirectionsFragmentModel = new RecipeDirectionsFragmentModel();
    private RecyclerView rvDirections;
    private RecipeDirectionsFragmentRecyclerAdapter recipeDirectionsFragmentRecyclerAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public List<String> directions;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeDirectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDirectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDirectionsFragment newInstance(String param1, String param2) {
        RecipeDirectionsFragment fragment = new RecipeDirectionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipe_directions, container, false);

        Bundle extras = getArguments();
        String recipeId = extras.getString("id");
        //ArrayList<String> directions = extras.getStringArrayList("directions");

        rvDirections = (RecyclerView) view.findViewById(R.id.rvDirection);
        recipeDirectionsFragmentRecyclerAdapter = new RecipeDirectionsFragmentRecyclerAdapter(recipeDirectionsFragmentModel.getDirectionList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvDirections.setAdapter(recipeDirectionsFragmentRecyclerAdapter);
        rvDirections.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDirections.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.recycler_border_layer, null)));
        rvDirections.addItemDecoration(dividerItemDecoration);

        // todo: RecipesRating
        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                directions = (ArrayList<String>) task.getResult().get("directions");

                for (int i = 0; i < directions.size(); i++)
                {
                    if (directions.get(i) != null)
                    {
                        recipeDirectionsFragmentModel.addItem(directions.get(i));
                    }
                    else
                    {
                        i = directions.size();
                    }
                }

                recipeDirectionsFragmentRecyclerAdapter.notifyDataSetChanged();
            }
        });


        /*EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        directions = sqlDb.testDao().getDirections(recipeId);

        for (int i = 0; i < directions.size(); i++)
        {
            if (directions.get(i) != null)
            {
                directionsModel.addItem(directions.get(i));
            }
            else
            {
                i = directions.size();
            }
        }*/


        return view;
    }
}