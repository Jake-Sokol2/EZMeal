package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class CategoryEntity
{
    @PrimaryKey(autoGenerate = true)
    public int itemId;

    public String recipeId;

    @ColumnInfo (name = "category")
    public String category;

    @ColumnInfo (name = "ingredient")
    public String ingredient;

    @ColumnInfo (name = "nutrition")
    public String nutrition;

    @ColumnInfo (name = "direction")
    public String direction;

    public CategoryEntity (String recipeId, String category, String nutrition, String direction, String ingredient)
    {
        this.recipeId = recipeId;
        this.category = category;
        this.nutrition = nutrition;
        this.direction = direction;
        this.ingredient = ingredient;
    }

}
