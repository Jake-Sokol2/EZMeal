
package com.example.ezmeal.roomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RatingDao
{
    //@Insert
    //void insertAll(Recipe... recipes);

    //@Insert
    //void insertAllItems(String recipeId, List<String> category, List<String> ingredient, List<String> nutrition, List<String> direction);

    @Insert
    void insert(Rating rating);

    @Query("UPDATE Rating SET ratingValue = :rating WHERE recipeId = :rid")
    void updateRating(Float rating, String rid);

    @Query("SELECT * FROM Rating WHERE recipeId = :rid")
    public Rating getSpecificRatingObject(String rid);

    @Query("SELECT * FROM Rating")
    public Rating getAllTheThings();

    @Query("SELECT ratingValue FROM Rating WHERE recipeId LIKE :rid")
    public float getSpecificRating(String rid);

    /*@Delete
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
    List<String> getCatUrl();*/


}

