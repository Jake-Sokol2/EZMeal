package com.example.ezmeal.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, CategoryEntity.class, Rating.class}, version = 6, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    public abstract TestDao testDao();
}