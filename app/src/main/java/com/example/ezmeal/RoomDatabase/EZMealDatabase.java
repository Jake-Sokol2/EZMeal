package com.example.ezmeal.roomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {Recipe.class, CategoryEntity.class, Rating.class, Category.class, Identifier.class, Category_RecyclerRecipe.class,
        Category2.class, RecyclerRecipe2.class, UsersCategory.class}, version = 20, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    private static EZMealDatabase instance;
    public abstract TestDao testDao();

    public static synchronized EZMealDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), EZMealDatabase.class, "EZMeal_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}