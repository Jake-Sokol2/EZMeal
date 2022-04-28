package com.example.ezmeal.FindRecipes.FindRecipesModels;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface Test
{
    public MutableLiveData<List<HorizontalRecipe>> callback(List<HorizontalRecipe> list);
}