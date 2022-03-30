package navigationFragments.MyRecipes.RecipeModels;

import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class IngredientsModel
{
    private List<String> ingredientList;
    //private boolean isChecked;
    //private boolean isOnList;

    public IngredientsModel()
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