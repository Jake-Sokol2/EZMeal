package navigationFragments.MyRecipes.RecipeModels;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MyRecipesModel
{
    private List<String> categoryList;
    private List<String> url;
    //private boolean isChecked;
    //private boolean isOnList;

    public MyRecipesModel()
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