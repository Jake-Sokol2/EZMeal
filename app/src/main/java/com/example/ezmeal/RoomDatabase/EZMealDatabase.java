package com.example.ezmeal.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, CategoryEntity.class, Rating.class, Category.class, Identifier.class, Category_RecyclerRecipe.class,
        Category2.class, RecyclerRecipe2.class, UsersCategory.class}, version = 16, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    public abstract TestDao testDao();
}
