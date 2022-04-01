package com.example.ezmeal.MyRecipes;

import java.util.List;

public class UserRecipe
{
    private List<String> categories;
    private List<String> directions;
    private List<String> ingredients;
    private List<String> nutrition;
    private String imageUrl;
    private String title;
    private String recipeId;


    public UserRecipe(List<String> categories, List<String> directions, List<String> ingredients,
                      List<String> nutrition, String imageUrl, String title, String recipeId)
    {
        this.categories = categories;
        this.directions = directions;
        this.nutrition = nutrition;
        this.imageUrl = imageUrl;
        this.title = title;
        this.recipeId = recipeId;
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }

    public List<String> getDirections()
    {
        return directions;
    }

    public void setDirections(List<String> directions)
    {
        this.directions = directions;
    }

    public List<String> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public List<String> getNutrition()
    {
        return nutrition;
    }

    public void setNutrition(List<String> nutrition)
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

    public String getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(String recipeId)
    {
        this.recipeId = recipeId;
    }
}
