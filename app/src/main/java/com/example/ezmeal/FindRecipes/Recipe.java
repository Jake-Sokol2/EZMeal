package com.example.ezmeal.FindRecipes;

import java.util.ArrayList;

public class Recipe
{
    private double recipeId;
    private ArrayList<String> categories;
    private ArrayList<String> directions;
    private ArrayList<String> ingredients;
    private String imageUrl;
    private String title;
    private double rating;
    private double countRating;
    private boolean highlyRated;
    private String calories;
    private String protein;
    private String carbohydrates;
    private String fat;
    private String cholesterol;
    private String sodium;

    public Recipe(double recipeId, ArrayList<String> categories, ArrayList<String> directions, ArrayList<String> ingredients,
                   String imageUrl, String title, double rating, double countRating, boolean highlyRated, String calories, String protein, String carbohydrates, String fat, String cholesterol, String sodium)
    {
         this.recipeId = recipeId;
         this.categories = categories;
         this.directions = directions;
         this.imageUrl = imageUrl;
         this.title = title;
         this.rating = rating;
         this.ingredients = ingredients;
         this.countRating = countRating;
         this.highlyRated = highlyRated;
         this.calories = calories;
         this.protein = protein;
         this.carbohydrates = carbohydrates;
         this.fat = fat;
         this.cholesterol = cholesterol;
         this.sodium = sodium;
    }

    public boolean isHighlyRated()
    {
        return highlyRated;
    }

    public String getCalories()
    {
        return calories;
    }

    public void setCalories(String calories)
    {
        this.calories = calories;
    }

    public String getProtein()
    {
        return protein;
    }

    public void setProtein(String protein)
    {
        this.protein = protein;
    }

    public String getCarbohydrates()
    {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates)
    {
        this.carbohydrates = carbohydrates;
    }

    public String getFat()
    {
        return fat;
    }

    public void setFat(String fat)
    {
        this.fat = fat;
    }

    public String getCholesterol()
    {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol)
    {
        this.cholesterol = cholesterol;
    }

    public String getSodium()
    {
        return sodium;
    }

    public void setSodium(String sodium)
    {
        this.sodium = sodium;
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
