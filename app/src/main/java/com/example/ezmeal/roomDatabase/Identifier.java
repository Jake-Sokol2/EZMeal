package com.example.ezmeal.roomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Identifier
{
    @PrimaryKey (autoGenerate = true)
    @NonNull
    @ColumnInfo (name = "id")
    public int id;

    @ColumnInfo (name = "category")
    public String category;

    @ColumnInfo (name = "identifier")
    public String identifier;

    @ColumnInfo (name = "isActive")
    public boolean isActive;

    @ColumnInfo (name = "timeActivated")
    public Long timeActivated;

    public Identifier(String category, String identifier, boolean isActive, Long timeActivated)
    {
        this.category = category;
        this.identifier = identifier;
        this.isActive = isActive;
        this.timeActivated = timeActivated;
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

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}

