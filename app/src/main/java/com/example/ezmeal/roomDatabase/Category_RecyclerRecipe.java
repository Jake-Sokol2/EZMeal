package com.example.ezmeal.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"category", "recipeId"})
public class Category_RecyclerRecipe
{
/*    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    public int id;*/
    @ColumnInfo (name = "category")
    @NonNull
    public String category;
    @ColumnInfo (name = "recipeId")
    @NonNull
    public String recipeId;

    public Category_RecyclerRecipe(@NonNull String category, @NonNull String recipeId)
    {
        this.category = category;
        this.recipeId = recipeId;
    }


}
