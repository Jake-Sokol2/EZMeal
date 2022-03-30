package com.example.ezmeal.RoomDatabase;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeItems
{
    @Embedded public Recipe recipe;
    @Relation(parentColumn = "recipeId",
              entityColumn = "recipeId")
    public List<CategoryEntity> categoryEntities;

}
