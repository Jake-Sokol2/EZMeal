package com.example.ezmeal.FindRecipes.FindRecipesModels;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentModel
{
    private List<String> recipeList;
    private List<String> uriList;
    private List<Double> avgRatingList;
    private List<Integer> totalRatingsList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryFragmentModel()
    {
        recipeList = new ArrayList<String>();
        uriList = new ArrayList<String>();
        avgRatingList = new ArrayList<Double>();
        totalRatingsList = new ArrayList<Integer>();
    }

    public void addItem(String recipeTitle, String uri, Double avgRating, Integer totalRatings)
    {
        uriList.add(uri);
        recipeList.add(recipeTitle);
        avgRatingList.add(avgRating);
        totalRatingsList.add(totalRatings);
    }

    public void setRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }

    public void setUriListEnabled(int position)
    {
        this.uriList = uriList;
    }

    public int listLength()
    {
        return recipeList.size();
    }

    public void dumpList()
    {
        recipeList.clear();
        uriList.clear();
        avgRatingList.clear();
        totalRatingsList.clear();
    }

    public List<String> getRecipeList()
    {
        return recipeList;
    }

    public List<String> getUriList()
    {
        return uriList;
    }

    public void restoreRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }

    public void setUriList(List<String> uriList)
    {
        this.uriList = uriList;
    }

    public List<Double> getAvgRatingList()
    {
        return avgRatingList;
    }

    public void setAvgRatingList(List<Double> avgRatingList)
    {
        this.avgRatingList = avgRatingList;
    }

    public List<Integer> getTotalRatingsList()
    {
        return totalRatingsList;
    }

    public void setTotalRatingsList(List<Integer> totalRatingsList)
    {
        this.totalRatingsList = totalRatingsList;
    }
}