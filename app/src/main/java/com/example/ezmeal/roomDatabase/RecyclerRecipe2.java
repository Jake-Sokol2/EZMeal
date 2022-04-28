
package com.example.ezmeal.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecyclerRecipe2
{
    @PrimaryKey (autoGenerate = true)
    @NonNull
    @ColumnInfo (name = "id")
    public int id;
    @ColumnInfo (name = "category")
    public String category;
    @ColumnInfo (name = "recipeId")
    public String recipeId;
    @ColumnInfo (name = "title")
    public String title;
    @ColumnInfo (name = "imageUrl")
    public String imageUrl;
    @ColumnInfo (name = "averageRating")
    public double averageRating;
    @ColumnInfo
    public String typeOfItem;
    @ColumnInfo
    public boolean isHorizontal;
    @ColumnInfo (name = "totalRatings")
    public Integer totalRatings;

    public RecyclerRecipe2(String category, String recipeId, String title, String imageUrl, double averageRating, String typeOfItem, boolean isHorizontal, Integer totalRatings)
    {
        this.category = category;
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
        this.typeOfItem = typeOfItem;
        this.isHorizontal = isHorizontal;
        this.totalRatings = totalRatings;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(String recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public double getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(double averageRating)
    {
        this.averageRating = averageRating;
    }

    public String getTypeOfItem()
    {
        return typeOfItem;
    }

    public void setTypeOfItem(String typeOfItem)
    {
        this.typeOfItem = typeOfItem;
    }

    public boolean isHorizontal()
    {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal)
    {
        isHorizontal = horizontal;
    }

    public Integer getTotalRatings()
    {
        return totalRatings;
    }

    public void setTotalRatings(Integer totalRatings)
    {
        this.totalRatings = totalRatings;
    }
}

