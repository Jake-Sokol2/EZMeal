package com.example.ezmeal.MyRecipes.RecipeModels;

import java.util.ArrayList;
import java.util.List;

public class RecipeDirectionsFragmentModel
{
    private List<String> directionList;

    public RecipeDirectionsFragmentModel()
    {
        directionList = new ArrayList<String>();
    }

    public void addItem(String direction)
    {
        directionList.add(direction);
    }

    public int listLength()
    {
        return directionList.size();
    }

    public void dumpList()
    {
        directionList.clear();
    }

    public List<String> getDirectionList()
    {
        return directionList;
    }

    public void restoreRecipeList(List<String> directionList)
    {
        this.directionList = directionList;
    }
}