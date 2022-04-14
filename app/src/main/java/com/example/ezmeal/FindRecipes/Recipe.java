package com.example.ezmeal.FindRecipes;

import java.util.ArrayList;

public class Recipe
{
    private double recipeId;
    private ArrayList<String> categories;
    private ArrayList<String> directions;
    private ArrayList<String> ingredients;
    private ArrayList<String> nutrition;
    private String imageUrl;
    private String title;
    private double rating;
    private double countRating;
    private boolean highlyRated;

    public Recipe(double recipeId, ArrayList<String> categories, ArrayList<String> directions, ArrayList<String> ingredients,
                  ArrayList<String> nutrition, String imageUrl, String title, double rating, double countRating, boolean highlyRated)
    {
         this.recipeId = recipeId;
         this.categories = categories;
         this.directions = directions;
         this.nutrition = nutrition;
         this.imageUrl = imageUrl;
         this.title = title;
         this.rating = rating;
         this.ingredients = ingredients;
         this.countRating = countRating;
         this.highlyRated = highlyRated;
    }


    public boolean
    isHighlyRated()
    {
        return highlyRated;
    }

    public void setHighlyRated(boolean highlyRated)
    {
        this.highlyRated = highlyRated;
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

    public double getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(double recipeId)
    {
        this.recipeId = recipeId;
    }

    public double getCountRating()
    {
        return countRating;
    }

    public void setCountRating(double countRating)
    {
        this.countRating = countRating;
    }
}
