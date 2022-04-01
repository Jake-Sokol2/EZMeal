package com.example.ezmeal.MyRecipes.RecipeModels;

import android.graphics.Bitmap;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentModel
{
    private List<String> recipeList;
    private List<String> urlList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryFragmentModel()
    {
        recipeList = new ArrayList<String>();
        urlList = new ArrayList<String>();
    }

    public void addItem(String recipeTitle, String url)
    {
        urlList.add(url);
        recipeList.add(recipeTitle);
    }

    public int listLength()
    {
        return recipeList.size();
    }

    public void dumpList()
    {
        recipeList.clear();
        urlList.clear();
    }

    public List<String> getRecipeList()
    {
        return recipeList;
    }

    public List<String> getUrlList()
    {
        return urlList;
    }

    public void restoreRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }

    public void restoreImageList(List<Bitmap> imageList)
    {
        this.urlList = urlList;
    }
}