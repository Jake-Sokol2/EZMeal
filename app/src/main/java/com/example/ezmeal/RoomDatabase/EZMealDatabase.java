package com.example.ezmeal.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ezmeal.RoomDatabase.Recipe;
import com.example.ezmeal.RoomDatabase.TestDao;

@Database(entities = {Recipe.class, CategoryEntity.class}, version = 6, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    public abstract TestDao testDao();

/*    private static EZMealDatabase INSTANCE;

    public static EZMealDatabase getDbInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EZMealDatabase.class, "RecipeDB").;
        }

        return INSTANCE;
    }*/
}