package com.example.ezmeal.FindRecipes.FindRecipesModels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RetrievedRecipeLists
{
    private List<VerticalRecipe> verticalList = new ArrayList<>();
    private List<HorizontalRecipe> horizontalList = new ArrayList<>();
    private List<String> recipeIdList = new ArrayList<>();
    private Set<Integer> setOfUniqueVerticalRecipes = new HashSet<>();
    private Set<Integer> setOfUniqueHorizontalRecipes = new HashSet<>();
    private String category;
    private int verticalStartId;
    private int verticalEndId;
    private int numVerticalToQuery = 0;
    private int numHorizontalToQuery = 0;
    private int totalVerticalRecipesRetrieved = 0;
    private int numRemainingVerticalRecipes = 0;
    private int randomQueryId = 0;

    public List<String> getRecipeIdList()
    {
        return recipeIdList;
    }

    public void appendRecipeIdList(List<String> recipeIdList)
    {
        this.recipeIdList.addAll(recipeIdList);
    }

    public int getNumHorizontalToQuery()
    {
        return numHorizontalToQuery;
    }

    public void setNumHorizontalToQuery(int numHorizontalToQuery)
    {
        this.numHorizontalToQuery = numHorizontalToQuery;
    }

    public Set<Integer> getSetOfUniqueVerticalRecipes()
    {
        return setOfUniqueVerticalRecipes;
    }

    public void addToSetOfUniqueVerticalRecipes(Integer recipeId)
    {
        this.setOfUniqueVerticalRecipes.add(recipeId);
    }

    public Set<Integer> getSetOfUniqueHorizontalRecipes()
    {
        return setOfUniqueHorizontalRecipes;
    }

    public void addToSetOfUniqueHorizontalRecipes(Integer recipeId)
    {
        this.setOfUniqueHorizontalRecipes.add(recipeId);
    }

    public List<VerticalRecipe> getVerticalList()
    {
        return verticalList;
    }

    public int getNumVerticalToQuery()
    {
        return numVerticalToQuery;
    }

    public void setNumVerticalToQuery(int numVerticalToQuery)
    {
        this.numVerticalToQuery = numVerticalToQuery;
    }

    public int getRandomQueryId()
    {
        return randomQueryId;
    }

    public void setRandomQueryId(int randomQueryId)
    {
        this.randomQueryId = randomQueryId;
    }

    // todo: change this to the other format in appendRecipeList if it works
    public void appendVerticalList(List<VerticalRecipe> verticalList)
    {
        List<VerticalRecipe> tempList = new ArrayList<>();
        tempList.addAll(this.verticalList);
        tempList.addAll(verticalList);
        this.verticalList = tempList;
    }

    public int getNumRemainingVerticalRecipes()
    {
        return numRemainingVerticalRecipes;
    }

    public void setNumRemainingVerticalRecipes(int numRemainingVerticalRecipes)
    {
        this.numRemainingVerticalRecipes = numRemainingVerticalRecipes;
    }

    public int getTotalVerticalRecipesRetrieved()
    {
        return totalVerticalRecipesRetrieved;
    }

    public void setTotalVerticalRecipesRetrieved(int totalVerticalRecipesRetrieved)
    {
        this.totalVerticalRecipesRetrieved = totalVerticalRecipesRetrieved;
    }

    public List<HorizontalRecipe> getHorizontalList()
    {
        return horizontalList;
    }

    public void appendHorizontalList(List<HorizontalRecipe> horizontalList)
    {
        List<HorizontalRecipe> tempList = new ArrayList<>();
        tempList.addAll(this.horizontalList);
        tempList.addAll(horizontalList);
        this.horizontalList = tempList;
    }

    public int getVerticalStartId()
    {
        return verticalStartId;
    }

    public void setVerticalStartId(int verticalStartId)
    {
        this.verticalStartId = verticalStartId;
    }

    public int getVerticalEndId()
    {
        return verticalEndId;
    }

    public void setVerticalEndId(int verticalEndId)
    {
        this.verticalEndId = verticalEndId;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }
}