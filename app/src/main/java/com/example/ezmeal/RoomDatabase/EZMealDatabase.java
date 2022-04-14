package com.example.ezmeal.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, CategoryEntity.class, Rating.class, Category.class, RecyclerRecipe.class, Category_RecyclerRecipe.class,
        Category2.class, RecyclerRecipe2.class}, version = 13, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    public abstract TestDao testDao();
}
