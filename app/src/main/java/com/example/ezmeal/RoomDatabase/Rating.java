package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Rating
{
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo (name = "recipeId")
    public String recipeId;

    @ColumnInfo (name = "ratingValue")
    public float ratingValue;

    public Rating(String recipeId, float ratingValue)
    {
        this.recipeId = recipeId;
        this.ratingValue = ratingValue;
    }

    public float getRating()
    {
        return ratingValue;
    }

    public void setRating(float rating)
    {
        this.ratingValue = rating;
    }
}
