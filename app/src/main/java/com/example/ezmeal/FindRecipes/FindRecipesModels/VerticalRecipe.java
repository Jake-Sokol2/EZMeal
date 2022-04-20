package com.example.ezmeal.FindRecipes.FindRecipesModels;

public class VerticalRecipe
{
    private String title;
    private String image;
    private String recipeId;
    private Double avgRating;
    private int totalCountOfRatings;

    public VerticalRecipe(String title, String image, String recipeId, Double avgRating, int totalCountOfRatings)
    {
        this.title = title;
        this.image = image;
        this.recipeId = recipeId;
        this.avgRating = avgRating;
        this.totalCountOfRatings = totalCountOfRatings;
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


