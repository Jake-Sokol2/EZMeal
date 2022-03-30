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

    @Query("SELECT pathToImage, title FROM Recipe WHERE recipeId = :rid")
    public RecipeCategoryTuple getSpecificCategoryItems(String rid);

    @RawQuery
    public String getImage2(SupportSQLiteQuery query);

    @Query("SELECT pathToImage FROM recipe WHERE recipeId = :rid")
    public String getImage(String rid);

    @Query("SELECT ingredient FROM CategoryEntity WHERE recipeId = :rid")
    public List<String> getIngredients(String rid);

    @Query("SELECT direction FROM CategoryEntity WHERE recipeId = :rid")
    public List<String> getDirections(String rid);

    @Query("SELECT nutrition FROM CategoryEntity WHERE recipeId = :rid")
    public List<String> getNutrition(String rid);

    @Query("DELETE FROM CategoryEntity WHERE recipeId = :rid")
    public void deleteSingleRecipeFromItem(String rid);

    @Query("DELETE FROM Recipe WHERE recipeId = :rid")
    public void deleteSingleRecipeFromRecipe(String rid);

    @Query("SELECT EXISTS(SELECT * FROM Recipe WHERE recipeId = :rid)")
    public boolean isRecipeExists(String rid);

    @Query("SELECT DISTINCT category FROM CategoryEntity WHERE category NOT NULL")
    public List<String> getCategories();

    @Query("SELECT Recipe.recipeID, Recipe.pathToImage, Recipe.title FROM Recipe join CategoryEntity on Recipe.recipeId = CategoryEntity.recipeId WHERE category = :cat")
    public List<recipePathTitle> getCategoryRecipes(String cat);
    //@Query("SELECT ")

    @Query("SELECT DISTINCT pathToImage FROM CategoryEntity JOIN Recipe on CategoryEntity.recipeId = Recipe.recipeID")
    List<String> getCatUrl();


}

