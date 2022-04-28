package com.example.ezmeal.roomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ezmeal.roomDatabase.CategoryEntity;
import com.example.ezmeal.roomDatabase.Recipe;


@Database(entities = {Recipe.class, CategoryEntity.class, com.example.ezmeal.roomDatabase.Rating.class, com.example.ezmeal.roomDatabase.Category.class, com.example.ezmeal.roomDatabase.Identifier.class, com.example.ezmeal.roomDatabase.Category_RecyclerRecipe.class,
        com.example.ezmeal.roomDatabase.Category2.class, com.example.ezmeal.roomDatabase.RecyclerRecipe2.class, com.example.ezmeal.roomDatabase.UsersCategory.class}, version = 20, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    private static EZMealDatabase instance;
    public abstract com.example.ezmeal.roomDatabase.TestDao testDao();

    public static synchronized EZMealDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), EZMealDatabase.class, "EZMeal_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}