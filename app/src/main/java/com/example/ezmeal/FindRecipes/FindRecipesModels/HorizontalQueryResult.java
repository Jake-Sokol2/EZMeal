package com.example.ezmeal.FindRecipes.FindRecipesModels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HorizontalQueryResult
{
    Set<Integer> setOfUniqueRecipes = new HashSet<>();
    List<HorizontalRecipe> horizontalRecipeList = new ArrayList<>();
    int numReturned = 0;

    public Set<Integer> getSetOfUniqueRecipes()
    {
        return setOfUniqueRecipes;
    }

    public void setSetOfUniqueRecipes(Set<Integer> setOfUniqueRecipes)
    {
        this.setOfUniqueRecipes = setOfUniqueRecipes;
    }

    public void appendSetOfUniqueRecipes(Integer recipeId)
    {
        this.setOfUniqueRecipes.add(recipeId);
    }

    public List<HorizontalRecipe> getHorizontalRecipeList()
    {
        return horizontalRecipeList;
    }

    public void setHorizontalRecipeList(List<HorizontalRecipe> horizontalRecipeList)
    {
        this.horizontalRecipeList = horizontalRecipeList;
    }

    public void appendHorizontalRecipeList(HorizontalRecipe horizontalRecipe)
    {
        this.horizontalRecipeList.add(horizontalRecipe);
    }

    public int getNumReturned()
    {
        return numReturned;
    }

    public void setNumReturned(int numReturned)
    {
        this.numReturned = numReturned;
    }
}
