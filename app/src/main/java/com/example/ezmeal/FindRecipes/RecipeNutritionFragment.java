package com.example.ezmeal.FindRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeNutritionFragmentRecyclerAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.RecipeNutritionFragmentModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeNutritionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeNutritionFragment extends Fragment {

    private RecipeNutritionFragmentModel recipeNutritionFragmentModel = new RecipeNutritionFragmentModel();
    private RecyclerView rvNutrition;
    private RecipeNutritionFragmentRecyclerAdapter recipeNutritionFragmentRecyclerAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public List<String> nutrition;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeNutritionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeNutritionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeNutritionFragment newInstance(String param1, String param2) {
        RecipeNutritionFragment fragment = new RecipeNutritionFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipe_nutrition, container, false);

        Bundle extras = getArguments();
        String recipeId = extras.getString("id");

        rvNutrition = (RecyclerView) view.findViewById(R.id.rvNutritionList);
        recipeNutritionFragmentRecyclerAdapter = new RecipeNutritionFragmentRecyclerAdapter(recipeNutritionFragmentModel.getNutritionList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvNutrition.setAdapter(recipeNutritionFragmentRecyclerAdapter);
        rvNutrition.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvNutrition.getContext(), DividerItemDecoration.VERTICAL);
        rvNutrition.addItemDecoration(dividerItemDecoration);

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                nutrition = (ArrayList<String>) task.getResult().get("nutrition");

                for (int i = 0; i < nutrition.size(); i++)
                {
                    if (nutrition.get(i) != null)
                    {
                        recipeNutritionFragmentModel.addItem(nutrition.get(i));
                    }
                    else
                    {
                        i = nutrition.size();
                    }
                }

                recipeNutritionFragmentRecyclerAdapter.notifyDataSetChanged();
            }
        });



        return view;
    }
}