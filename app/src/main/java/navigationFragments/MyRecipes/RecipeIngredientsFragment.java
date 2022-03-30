package navigationFragments.MyRecipes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ezmeal.Model.Item;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import navigationFragments.MyRecipes.RecipeAdapters.IngredientsRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeModels.IngredientsModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIngredientsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private List<String> ingredientList = new List<String>;
    private IngredientsModel ingredientsModel = new IngredientsModel();
    private RecyclerView rvIngredients;
    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button btnAddToList;

    public List<String> ingredients;
    public boolean itemAdded = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeInstructionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeIngredientsFragment newInstance(String param1, String param2) {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        rvIngredients = (RecyclerView) view.findViewById(R.id.rvIngredientList);
        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(ingredientsModel.getIngredients());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvIngredients.setAdapter(ingredientsRecyclerAdapter);
        rvIngredients.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvIngredients.getContext(), DividerItemDecoration.VERTICAL);
        rvIngredients.addItemDecoration(dividerItemDecoration);

        Bundle extras = getArguments();
        String recipeId = extras.getString("id");

        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        ingredients = sqlDb.testDao().getIngredients(recipeId);

        for (int i = 0; i < ingredients.size(); i++)
        {
            if (ingredients.get(i) != null)
            {
                ingredientsModel.addItem(ingredients.get(i));
            }
            else
            {
                i = ingredients.size();
            }
        }
        ingredientsRecyclerAdapter.notifyDataSetChanged();

        // add all ingredients for this recipe to the user's Item collection in Firestore
        btnAddToList = view.findViewById(R.id.btnAddToList);
        btnAddToList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // in case user edited ingredient list, re-read ingredients from database
                ingredients = sqlDb.testDao().getIngredients(recipeId);

                FirebaseUser mCurrentUser = mAuth.getCurrentUser();
                String email = mCurrentUser.getEmail();

                for (int i = 0; i < ingredients.size(); i++)
                {
                    if (ingredients.get(i) != null)
                    {
                        mAuth = FirebaseAuth.getInstance();

                        CollectionReference dbItems = db.collection("Items");
                        Item item = new Item(ingredients.get(i), null, email);

                        // prevent user from adding same list of ingredients twice
                        dbItems.whereEqualTo(ingredients.get(i), null).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    for (DocumentSnapshot document : task.getResult())
                                    {
                                        // if ingredient isn't already added to database, add it now
                                        if (!document.exists())
                                        {
                                            dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // keep track that an item was added so we can tell the user with a toast later
                                                    itemAdded = true;

                                                }
                                            });
                                        }
                                    }

                                    // if any items were actually added, tell the user
                                    if (itemAdded)
                                    {
                                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        // escape loop if rest of ingredients list is null (nulls are a symptom of database insertion)
                        i = ingredients.size();
                    }
                }

                btnAddToList.setEnabled(false);
                btnAddToList.setTextColor(Color.parseColor("#808080"));


            }
        });

        return view;
    }
}