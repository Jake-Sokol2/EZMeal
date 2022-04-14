package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class RecyclerRecipe
{
    @PrimaryKey
    @NonNull
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

    public RecyclerRecipe(String recipeId, String title, String imageUrl, double averageRating, String typeOfItem, boolean isHorizontal)
    {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
        this.typeOfItem = typeOfItem;
        this.isHorizontal = isHorizontal;
    }

    @NonNull
    public String getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(@NonNull String recipeId)
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
}
