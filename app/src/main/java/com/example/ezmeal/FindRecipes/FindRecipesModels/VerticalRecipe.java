package com.example.ezmeal.FindRecipes.FindRecipesModels;

import java.util.UUID;

public class VerticalRecipe
{
    private String title;
    private String image;
    private String recipeId;
    private Double avgRating;
    private int totalCountOfRatings;
    private String category;
    private UUID queryId;

    public VerticalRecipe(String title, String image, String recipeId, Double avgRating, int totalCountOfRatings, String category, UUID queryId)
    {
        this.title = title;
        this.image = image;
        this.recipeId = recipeId;
        this.avgRating = avgRating;
        this.totalCountOfRatings = totalCountOfRatings;
        this.category = category;
        this.queryId = queryId;
    }

    public VerticalRecipe(String title, String image, String recipeId, Double avgRating, int totalCountOfRatings, String category)
    {
        this.title = title;
        this.image = image;
        this.recipeId = recipeId;
        this.avgRating = avgRating;
        this.totalCountOfRatings = totalCountOfRatings;
        this.category = category;
    }

    public UUID getQueryId()
    {
        return queryId;
    }

    public void setQueryId(UUID queryId)
    {
        this.queryId = queryId;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(String recipeId)
    {
        this.recipeId = recipeId;
    }

    public Double getAvgRating()
    {
        return avgRating;
    }

    public void setAvgRating(Double avgRating)
    {
        this.avgRating = avgRating;
    }

    public int getTotalCountOfRatings()
    {
        return totalCountOfRatings;
    }

    public void setTotalCountOfRatings(int totalCountOfRatings)
    {
        this.totalCountOfRatings = totalCountOfRatings;
    }
}


