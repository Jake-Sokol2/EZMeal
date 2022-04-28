package com.example.ezmeal.roomDatabase;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithIdentifiers
{
    @Embedded public Category category;
    @Relation(
            parentColumn = "category",
            entityColumn = "category"
    )
    public List<Identifier> identifiers;
}
