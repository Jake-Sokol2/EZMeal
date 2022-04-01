package com.example.ezmeal.MyRecipes.RecipeAdapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ezmeal.MyRecipes.RecipeDirectionsFragment;
import com.example.ezmeal.MyRecipes.RecipeIngredientsFragment;
import com.example.ezmeal.MyRecipes.RecipeNutritionFragment;

public class RecipeFragmentViewPagerAdapter extends FragmentStateAdapter
{
    String recipeId;

    public RecipeFragmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String recipeId) {
        super(fragmentManager, lifecycle);
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment frag;
        Bundle bundleRecipeId = new Bundle();

        switch (position)
        {
            case 1:
                frag = new RecipeDirectionsFragment();

                bundleRecipeId.putString("id", recipeId);
                frag.setArguments(bundleRecipeId);

                return frag;
            case 2:
                frag = new RecipeNutritionFragment();

                bundleRecipeId.putString("id", recipeId);
                frag.setArguments(bundleRecipeId);

                return frag;
            default:
                frag = new RecipeIngredientsFragment();

                bundleRecipeId.putString("id", recipeId);
                frag.setArguments(bundleRecipeId);

                return frag;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
