package com.example.ezmeal.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Category2
{
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "category")
    public String category;

    // Room does not support Date variables
    @ColumnInfo (name = "dateRetrieved")
    public long dateRetrieved;

    public Category2(String category, long dateRetrieved)
    {
        this.category = category;
        this.dateRetrieved = dateRetrieved;
    }

    @NonNull
    public String getCategory()
    {
        return category;
    }

    public void setCategory(@NonNull String category)
    {
        this.category = category;
    }

    public long getDateRetrieved()
    {
        return dateRetrieved;
    }

    public void setDateRetrieved(long dateRetrieved)
    {
        this.dateRetrieved = dateRetrieved;
    }
}

