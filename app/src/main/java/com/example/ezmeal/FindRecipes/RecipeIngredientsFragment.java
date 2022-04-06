package com.example.ezmeal.FindRecipes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.ezmeal.GroupLists.Model.Item;
import com.example.ezmeal.MyRecipes.RecipeAdapters.RecipeIngredientsFragmentRecyclerAdapter;
import com.example.ezmeal.MyRecipes.RecipeModels.RecipeIngredientsFragmentModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIngredientsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private List<String> ingredientList = new List<String>;
    private RecipeIngredientsFragmentModel recipeIngredientsFragmentModel = new RecipeIngredientsFragmentModel();
    private RecyclerView rvIngredients;
    private RecipeIngredientsFragmentRecyclerAdapter recipeIngredientsFragmentRecyclerAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button btnAddToList;

    public List<String> ingredients;
    public boolean itemAdded = false;
    public ArrayList<String> ingredientsArray;

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
        recipeIngredientsFragmentRecyclerAdapter = new RecipeIngredientsFragmentRecyclerAdapter(recipeIngredientsFragmentModel.getIngredients());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvIngredients.setAdapter(recipeIngredientsFragmentRecyclerAdapter);
        rvIngredients.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvIngredients.getContext(), DividerItemDecoration.VERTICAL);
        rvIngredients.addItemDecoration(dividerItemDecoration);

        Bundle extras = getArguments();

        String recipeId = extras.getString("id");

        db.collection("Recipes").document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                ingredients = (ArrayList<String>) task.getResult().get("ingredients");

                for (int i = 0; i < ingredients.size(); i++)
                {
                    if (!Objects.equals(ingredients.get(i), ""))
                    {
                        recipeIngredientsFragmentModel.addItem(ingredients.get(i));
                    }
                    else
                    {
                        i = ingredients.size();
                    }
                }

                recipeIngredientsFragmentRecyclerAdapter.notifyDataSetChanged();
            }
        });

        //ingredientsArray = extras.getStringArrayList("ingredients");





        /*EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
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
        ingredientsRecyclerAdapter.notifyDataSetChanged();*/

        // add all ingredients for this recipe to the user's Item collection in Firestore


        EZMealDatabase sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // add all ingredients for this recipe to the user's Item collection in Firestore
        btnAddToList = view.findViewById(R.id.btnAddToList);
        btnAddToList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // in case user edited ingredient list, re-read ingredients from database
                //ingredients = sqlDb.testDao().getIngredients(recipeId);

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
                        dbItems.whereEqualTo("name", ingredients.get(i)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.getResult().isEmpty())
                                {
                                    dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // keep track that an item was added so we can tell the user with a toast later
                                            itemAdded = true;
                                            Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    });

                                    // if any items were actually added, tell the user
                                    if (itemAdded)
                                    {
                                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                                    }
                                }



                                /*if (task.isSuccessful())
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
                                            }).addOnFailureListener(new OnFailureListener()
                                            {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    e.printStackTrace();
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
                                else
                                {
                                    Log.i("q", "task not successful");
                                }*/
                            }

                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Log.i("q", "get failed");
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