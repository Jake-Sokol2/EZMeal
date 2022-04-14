package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Rating
{
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo (name = "recipeId")
    public String recipeId;

    @ColumnInfo (name = "ratingValue")
    public float ratingValue;

    @ColumnInfo (name = "textRating1")
    public String textRating1;

    @ColumnInfo (name = "textRating2")
    public String textRating2;

    @ColumnInfo (name = "textRating3")
    public String textRating3;

    public Rating()
    {
    }


    public Rating(@NonNull String recipeId, float ratingValue, List<String> chosenBubbles)
    {
        this.recipeId = recipeId;
        this.ratingValue = ratingValue;

        textRating1 = chosenBubbles.get(0);
        textRating2 = chosenBubbles.get(1);
        textRating3 = chosenBubbles.get(2);
    }

    public float getRating()
    {
        return ratingValue;
    }

    public void setRating(float rating)
    {
        this.ratingValue = rating;
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

    public float getRatingValue()
    {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue)
    {
        this.ratingValue = ratingValue;
    }

    public String getTextRating1()
    {
        return textRating1;
    }

    public void setTextRating1(String textRating1)
    {
        this.textRating1 = textRating1;
    }

    public String getTextRating2()
    {
        return textRating2;
    }

    public void setTextRating2(String textRating2)
    {
        this.textRating2 = textRating2;
    }

    public String getTextRating3()
    {
        return textRating3;
    }

    public void setTextRating3(String textRating3)
    {
        this.textRating3 = textRating3;
    }
}
