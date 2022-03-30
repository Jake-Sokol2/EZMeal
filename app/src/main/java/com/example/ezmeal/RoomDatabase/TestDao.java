package com.example.ezmeal.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface TestDao {
    @Insert
    void insertAll(Recipe... recipes);

    //@Insert
    //void insertAllItems(String recipeId, List<String> category, List<String> ingredient, List<String> nutrition, List<String> direction);

    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM Recipe")
    List<Recipe> getAll();

    @Query("DELETE FROM Recipe")
    public void BOOM();

    @Query("DELETE FROM CategoryEntity")
    public void BOOOOOOOM();

    @Query("SELECT * FROM Recipe")
    public List<RecipeItems> getRecipeItems();

    @Insert
    void insertItem(CategoryEntity categoryEntity);

    @Query("SELECT COUNT(recipeId) FROM Recipe")
    LiveData<Integer> getDataCount();

    @Query("SELECT pathToImage, title FROM Recipe WHERE recipeId LIKE :rid")
    public RecipeCategoryTuple getSpecificCategoryItems(String rid);

    @RawQuery
    public String getImage2(SupportSQLiteQuery query);

    @Query("SELECT pathToImage FROM recipe WHERE recipeId LIKE :rid")
    public String getImage(String rid);

    @Query("SELECT ingredient FROM CategoryEntity WHERE recipeId LIKE :rid")
    public List<String> getIngredients(String rid);
}

