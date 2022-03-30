package navigationFragments.FindRecipes;

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

import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.DirectionsRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeModels.DirectionsModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindRecipesDirectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindRecipesDirectionsFragment extends Fragment
{
    private DirectionsModel directionsModel = new DirectionsModel();
    private RecyclerView rvDirections;
    private DirectionsRecyclerAdapter directionsRecyclerAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public List<String> directions;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindRecipesDirectionsFragment() {
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
    public static FindRecipesDirectionsFragment newInstance(String param1, String param2) {
        FindRecipesDirectionsFragment fragment = new FindRecipesDirectionsFragment();
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
        directionsRecyclerAdapter = new DirectionsRecyclerAdapter(directionsModel.getDirectionList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvDirections.setAdapter(directionsRecyclerAdapter);
        rvDirections.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDirections.getContext(), DividerItemDecoration.VERTICAL);
        rvDirections.addItemDecoration(dividerItemDecoration);

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
                        directionsModel.addItem(directions.get(i));
                    }
                    else
                    {
                        i = directions.size();
                    }
                }

                directionsRecyclerAdapter.notifyDataSetChanged();
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