package com.example.ezmeal.FindRecipes.FindRecipesModels;

import java.util.ArrayList;
import java.util.List;

public class FindRecipesFragmentModel
{
    private List<String> categoryList;
    private List<Boolean> isSelectedList;
    //private List<String> url;
    //private boolean isChecked;
    //private boolean isOnList;

    public FindRecipesFragmentModel()
    {
        categoryList = new ArrayList<String>();
        isSelectedList = new ArrayList<Boolean>();
        //url = new ArrayList<String>();
    }

    public void addItem(String category, Boolean isSelected)//, String url)
    {
        categoryList.add(category);
        isSelectedList.add(isSelected);
        //this.url.add(url);
    }

    public int listLength()
    {
        return categoryList.size();
    }

    public void dumpList()
    {
        categoryList.clear();
        isSelectedList.clear();
        //url.clear();
    }

    public void setCategoryList(List<String> categoryList)
    {
        this.categoryList = categoryList;
    }
    public void setIsSelectedList(List<Boolean> isSelectedList) { this.isSelectedList = isSelectedList; }

    public void setSelected(int position)
    {
        isSelectedList.set(position, true);
    }

    public void setNotSelected(int position)
    {
        isSelectedList.set(position, false);
    }

    public List<String> getCategoryList()
    {
        return categoryList;
    }

    public List<Boolean> getIsSelectedList()
    {
        return isSelectedList;
    }
/*
    public List<String> getUrl()
    {
        return url;
    }*/

    public void restoreCategoryList(List<String> categoryList)
    {
        this.categoryList = categoryList;
    }
}