package navigationFragments.MyRecipes.RecipeModels;

import java.util.ArrayList;
import java.util.List;

public class NutritionModel
{
    private List<String> nutritionList;

    public NutritionModel()
    {
        nutritionList = new ArrayList<String>();
    }

    public void addItem(String nutrition)
    {
        nutritionList.add(nutrition);
    }

    public int listLength()
    {
        return nutritionList.size();
    }

    public void dumpList()
    {
        nutritionList.clear();
    }

    public List<String> getNutritionList()
    {
        return nutritionList;
    }

    public void restoreNutritionList(List<String> nutritionList)
    {
        this.nutritionList = nutritionList;
    }
}