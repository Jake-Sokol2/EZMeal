package com.example.ezmeal;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TestEntity.class}, version = 1, exportSchema = false)
public abstract class EZMealDatabase extends RoomDatabase
{
    public abstract TestDao testDao();
}