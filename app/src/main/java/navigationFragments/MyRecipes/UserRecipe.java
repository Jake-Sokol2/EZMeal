package navigationFragments.MyRecipes;

import java.util.ArrayList;
import java.util.List;

public class UserRecipe
{
    private List<String> categories;
    private List<String> directions;
    private List<String> ingredients;

    private List<String> nutrition;
    private String imageUrl;
    private String title;
    private String email;

    public UserRecipe(List<String> categories, List<String> directions, List<String> ingredients,
                      List<String> nutrition, String imageUrl, String title, String email)
    {
         this.categories = categories;
         this.directions = directions;
         this.nutrition = nutrition;
         this.imageUrl = imageUrl;
         this.title = title;
         this.email = email;
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void setCategories(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    public List<String> getDirections()
    {
        return directions;
    }

    public void setDirections(ArrayList<String> directions)
    {
        this.directions = directions;
    }

    public List<String> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public List<String> getNutrition()
    {
        return nutrition;
    }

    public void setNutrition(ArrayList<String> nutrition)
    {
        this.nutrition = nutrition;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
