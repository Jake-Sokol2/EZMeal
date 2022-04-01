package com.example.ezmeal.FindRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment
{
    private FirebaseFirestore db;

    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();

    private RecyclerView rvFindRecipes;
    private CategoryFragmentAdapter categoryFragmentAdapter;

    String category;

    private List<String> recipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_recipes_category, container, false);

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipes);
        categoryFragmentAdapter = new CategoryFragmentAdapter(categoryFragmentModel.getRecipeList(), categoryFragmentModel.getUriList());
        rvFindRecipes.setAdapter(categoryFragmentAdapter);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvFindRecipes.setLayoutManager(gridLayoutManager);


        categoryFragmentAdapter.setOnItemClickListener(new CategoryFragmentAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {
                Intent intent = new Intent(getContext(), RecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", recipeId.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

                //Activity frag = new FindRecipesSpecificRecipeFragment();
                //frag.setArguments(bundle);

                //getParentFragmentManager().beginTransaction().replace(R.id.fragContainer, frag).commit();
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Bundle extras = getArguments();




        /*findRecipesModel.addItem("abc", "a");
        findRecipesModel.addItem("ddd", "a");
        findRecipesModel.addItem("some category", "a");
        findRecipesModel.addItem("some category", "a");
        findRecipesModel.addItem("some category", "a");
        findRecipesModel.addItem("some category", "a");
        findRecipesModel.addItem("some category", "a");
        //rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipesCategory);
        //findRecipesAdapter = new FindRecipesAdapter(findRecipesModel.getRecipeList(), findRecipesModel.getUriList());
        *//*rvFindRecipes.setAdapter(findRecipesAdapter);
        RecyclerView.LayoutManager verticalLayoutManager = new GridLayoutManager(this.getActivity(), 2, RecyclerView.VERTICAL, false);
        rvFindRecipes.setLayoutManager(verticalLayoutManager);*//*
        findRecipesAdapter.notifyDataSetChanged();*/

        db = FirebaseFirestore.getInstance();
        CollectionReference dbRecipes = db.collection("Recipes");

        // if bundle was null, it means that the user just opened the screen and is on the "featured" page.  Query random, highly rated recipes (right now just querying all recipes)
        if (extras == null)
        {
            db.collection("Recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    recipeId = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        //Log.i("retrieve", document.getId() + "=> " + document.getData());
                        String title = document.getString("title");
                        String imageUrl = document.getString("imageUrl");

                        categoryFragmentModel.addItem(title, imageUrl);
                        recipeId.add(document.getId());
                    }

                    categoryFragmentAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });
        }
        // if not null, the user picked a category, so query for the existing category only
        else
        {
            category = extras.getString("cat");

            db.collection("Recipes").whereArrayContains("categories", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    recipeId = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        //Log.i("retrieve", document.getId() + "=> " + document.getData());
                        String title = document.getString("title");
                        String imageUrl = document.getString("imageUrl");

                        categoryFragmentModel.addItem(title, imageUrl);
                        recipeId.add(document.getId());
                    }

                    categoryFragmentAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });
        }

    }

    @Override
    public void onStop()
    {
        super.onStop();
        categoryFragmentModel.dumpList();
    }
}
