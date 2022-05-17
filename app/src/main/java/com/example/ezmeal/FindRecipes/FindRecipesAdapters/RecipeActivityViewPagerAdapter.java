package com.example.ezmeal.FindRecipes.FindRecipesAdapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ezmeal.FindRecipes.RecipeDirectionsFragment;
import com.example.ezmeal.FindRecipes.RecipeIngredientsFragment;
import com.example.ezmeal.FindRecipes.RecipeNutritionFragment;
import com.example.ezmeal.FindRecipes.RecipeRatingsFragment;

import java.util.ArrayList;

public class RecipeActivityViewPagerAdapter extends FragmentStateAdapter
{
    private String recipeId;

    private ArrayList<String> directions;
    private ArrayList<String> nutrition;
    private ArrayList<String> ingredients;

    public RecipeActivityViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                                          ArrayList<String> directions, ArrayList<String> nutrition, ArrayList<String> ingredients, String recipeId) {

        super(fragmentManager, lifecycle);

        this.recipeId = recipeId;
        this.directions = directions;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment frag;
        Bundle bundleRecipeId = new Bundle();
        /*Gson gson = new Gson();
        String json;*/

        // determines which fragment is created depending on which tab is selected
        // pass the recipeId in so the fragment knows what to query firebase for the image, and pass in the existing
        // list of directions/nutrition/ingredients so that we are less reliant on querying
        switch (position)
        {
            case 1:
                frag = new RecipeDirectionsFragment();

                bundleRecipeId.putString("id", recipeId);
                //theModel.restoreGroceryList((List<List<String>>)savedInstanceState.getSerializable(RV_DATA));
                //outState.putSerializable(RV_DATA, (Serializable) theModel.getGroceryList());
                /*json = gson.toJson(directions);
                bundleRecipeId.putSerializable("directions", (Serializable) directions);*/
                frag.setArguments(bundleRecipeId);

                return frag;
            case 2:
                frag = new RecipeNutritionFragment();

                bundleRecipeId.putString("id", recipeId);
                //bundleRecipeId.putStringArrayList("nutrition", nutrition);
                //bundleRecipeId.putSerializable("nutrition", (Serializable) nutrition);
                frag.setArguments(bundleRecipeId);

                return frag;
            case 3:
                frag = new RecipeRatingsFragment();

                bundleRecipeId.putString("id", recipeId);
                /*json = gson.toJson(ingredients);
                bundleRecipeId.putString("test", json);*/
                //bundleRecipeId.putSerializable("ingredients", ingredients);
                frag.setArguments(bundleRecipeId);

                return frag;
            default:
                frag = new RecipeIngredientsFragment();

                bundleRecipeId.putString("id", recipeId);
                /*json = gson.toJson(ingredients);
                bundleRecipeId.putString("test", json);*/
                //bundleRecipeId.putSerializable("ingredients", ingredients);
                frag.setArguments(bundleRecipeId);

                return frag;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
