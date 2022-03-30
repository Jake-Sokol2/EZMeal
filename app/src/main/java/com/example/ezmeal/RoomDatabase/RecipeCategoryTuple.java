package com.example.ezmeal.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class RecipeCategoryTuple
{
    @ColumnInfo (name = "pathToImage")
    public String pathToImage;
    @ColumnInfo (name = "title")
    public String title;

    public String getPathToImage()
    {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage)
    {
        this.pathToImage = pathToImage;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}