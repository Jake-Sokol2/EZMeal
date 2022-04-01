package com.example.ezmeal.MyRecipes.RecipeModels;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesFragmentModel
{
    private List<String> categoryList;
    private List<String> url;

    public MyRecipesFragmentModel()
    {
        categoryList = new ArrayList<String>();
        url = new ArrayList<String>();
    }

    public void addItem(String ingredient, String url)
    {
        categoryList.add(ingredient);
        this.url.add(url);
    }

    public int listLength()
    {
        return categoryList.size();
    }

    public void dumpList()
    {
        categoryList.clear();
        url.clear();
    }

    public List<String> getCategoryList()
    {
        return categoryList;
    }

    public List<String> getUrl()
    {
        return url;
    }

    public void restoreCategoryList(List<String> categoryList)
    {
        this.categoryList = categoryList;
    }
}