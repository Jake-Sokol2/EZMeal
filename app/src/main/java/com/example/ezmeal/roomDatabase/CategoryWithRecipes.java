package com.example.ezmeal.roomDatabase;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithRecipes
{
    @Embedded public Category2 category2;
    @Relation(
            parentColumn = "category",
            entityColumn = "category"
    )
    public List<RecyclerRecipe2> recyclerRecipe2List;
}