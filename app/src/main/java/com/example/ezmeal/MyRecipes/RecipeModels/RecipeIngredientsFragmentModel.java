package com.example.ezmeal.MyRecipes.RecipeModels;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsFragmentModel
{
    private List<String> ingredientList;

    public RecipeIngredientsFragmentModel()
    {
        ingredientList = new ArrayList<String>();
    }

    public void addItem(String ingredient)
    {
        ingredientList.add(ingredient);
    }

    public int listLength()
    {
        return ingredientList.size();
    }

    public void dumpList()
    {
        ingredientList.clear();
    }

    public List<String> getIngredients()
    {
        return ingredientList;
    }

    public void restoreRecipeList(List<String> recipeList)
    {
        this.ingredientList = recipeList;
    }
}