package com.example.ezmeal.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Category
{
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "category")
    public String category;

    public Category(String category)
    {
        this.category = category;
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
}

