package navigationFragments.FindRecipes;

import java.util.ArrayList;

public class Recipe
{
    private ArrayList<String> categories;
    private ArrayList<String> directions;
    private ArrayList<String> ingredients;
    private ArrayList<String> nutrition;
    private String imageUrl;
    private String title;
    private double rating;

    public Recipe(ArrayList<String> categories, ArrayList<String> directions, ArrayList<String> ingredients,
                  ArrayList<String> nutrition, String imageUrl, String title, double rating)
    {
         this.categories = categories;
         this.directions = directions;
         this.nutrition = nutrition;
         this.imageUrl = imageUrl;
         this.title = title;
         this.rating = rating;
    }

    public ArrayList<String> getCategories()
    {
        return categories;
    }

    public void setCategories(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    public ArrayList<String> getDirections()
    {
        return directions;
    }

    public void setDirections(ArrayList<String> directions)
    {
        this.directions = directions;
    }

    public ArrayList<String> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getNutrition()
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

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }
}
