package com.example.ezmeal.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.sql.Date;
import java.util.List;

public class CategoryWithRecyclerRecipes
{
    @Embedded public Category category;
    @Relation(
            parentColumn = "category",
            entityColumn = "recipeId",
            associateBy = @Junction(Category_RecyclerRecipe.class)
    )
    public List<RecyclerRecipe> recyclerRecipes;
}