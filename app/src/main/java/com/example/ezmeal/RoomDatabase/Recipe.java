package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe
{
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String recipeId;

    @ColumnInfo(name = "pathToImage")
    public String pathToImage;
    @ColumnInfo(name = "title")
    public String title;

    public Recipe(String pathToImage, String title, String recipeId)
    {
        this.pathToImage = pathToImage;
        this.title = title;
        this.recipeId = recipeId;
    }
}