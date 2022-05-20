package com.example.ezmeal.roomDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
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

    @Query("SELECT * FROM Recipe")
    public LiveData<List<RecipeItems>> getRecipeItemsLive();

    @Query("SELECT pathToImage FROM Recipe r INNER JOIN CategoryEntity ce ON r.RecipeId = ce.RecipeId WHERE category = :cat")
    public List<String> getImagesForCategories(String cat);

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
//AND NOT category = 'Breakfast' AND NOT category = 'Lunch' AND NOT category = 'Dinner' AND NOT category = 'Dessert'
    @Query("SELECT DISTINCT category FROM CategoryEntity WHERE category NOT NULL AND NOT category = 'Breakfast' AND NOT category = 'Lunch' AND NOT category = 'Dinner' AND NOT category = 'Dessert'")
    public List<String> getCategoriesCategoryEntity();

    @Query("SELECT Recipe.recipeID, Recipe.pathToImage, Recipe.title FROM Recipe join CategoryEntity on Recipe.recipeId = CategoryEntity.recipeId WHERE category = :cat")
    public List<recipePathTitle> getCategoryRecipes(String cat);
    //@Query("SELECT ")

    @Query("SELECT DISTINCT pathToImage FROM CategoryEntity JOIN Recipe on CategoryEntity.recipeId = Recipe.recipeID")
    List<String> getCatUrl();




    @Insert
    void insert(Rating rating);

    @Query("UPDATE Rating SET ratingValue = :rating WHERE recipeId = :rid")
    void updateRating(Float rating, String rid);

    @Query("UPDATE Rating SET textRating1 = :text1, textRating2 = :text2, textRating3 = :text3 WHERE recipeId = :rid")
    void updateTextRatings(String text1, String text2, String text3, String rid);

    @Query("SELECT * FROM Rating WHERE recipeId = :rid")
    public Rating getSpecificRatingObject(String rid);

    @Query("SELECT * FROM Rating")
    public Rating getAllTheThings();

    @Query("SELECT ratingValue FROM Rating WHERE recipeId LIKE :rid")
    public float getSpecificRating(String rid);

    @Query("SELECT textRating1, textRating2, textRating3 FROM Rating WHERE recipeId = :rid")
    public TextRatings getSpecificTextRatings(String rid);

    @Query("DELETE FROM Rating")
    public void BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOM();



    @Transaction
    @Query("SELECT * FROM Category2 WHERE category = :cat")
    public List<CategoryWithRecipes> getCategoriesWithRecipes(String cat);

    @Transaction
    @Query("SELECT * FROM Category WHERE category = :cat")
    public List<CategoryWithIdentifiers> getCategoriesWithIdentifiers(String cat);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllCategory(List<Category> categories);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertIdentifier(Identifier identifier);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllIdentifier(List<Identifier> identifiers);

    @Query("SELECT * FROM Category")
    public List<Category> categories();

    @Query("DELETE FROM Category")
    public void deleteAllFromCategory();

    @Query("DELETE FROM Identifier")
    public void deleteAllFromIdentifier();

    //@Transaction
    //@Query("SELECT * FROM RecyclerRecipe WHERE isHorizontal != 1")
    //public List<CategoryWithRecyclerRecipes> getRecipesWithCategories(String cat);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCategory2(Category2 category2);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecyclerRecipe2(RecyclerRecipe2 recyclerRecipe2);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecyclerRecipe2(List<RecyclerRecipe2> recyclerRecipe2);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecyclerRecipe2Live(RecyclerRecipe2 recyclerRecipe2);

    @Query("DELETE FROM Category2")
    public void deleteCategory2();

    @Query("DELETE FROM RecyclerRecipe2")
    public void deleteRecyclerRecipe2();


    @Query("DELETE FROM Category")
    public void deleteCategory();

    @Query("DELETE FROM Category2 WHERE category = :cat")
    public void deleteFromCategory2SpecificCategory(String cat);

    @Query("DELETE FROM RecyclerRecipe2 WHERE category = :cat")
    public void deleteFromRecyclerRecipe2SpecificCategory(String cat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCategoryRecyclerRecipe(List<Category_RecyclerRecipe> categoryRecyclerRecipeList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategoryRecyclerRecipe(Category_RecyclerRecipe category_recyclerRecipe);

    @Query("DELETE FROM Category_RecyclerRecipe")
    void deleteCategoryRecyclerRecipe();

    @Query("DELETE FROM Category2")
    void deleteAllCategories();

    @Query("DELETE FROM RecyclerRecipe2")
    void deleteALlRecyclerRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsersCategory(UsersCategory usersCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllUsersCategory(List<UsersCategory> usersCategoryList);

    @Query("SELECT DISTINCT identifier FROM Identifier")
    public List<String> getDistinctIdentifiers();

    @Query("UPDATE IDENTIFIER SET isActive = 1 WHERE identifier = :identifier")
    public void updateIdentifierIsActive(String identifier);

    @Query("UPDATE IDENTIFIER SET isActive = 0 WHERE identifier = :identifier")
    public void updateIdentifierIsNotActive(String identifier);

    @Query("UPDATE IDENTIFIER SET isActive = 0")
    public void updateAllIdentifiersIsNotActive();

    @Query("SELECT DISTINCT category FROM IDENTIFIER WHERE isActive = 1")
    public LiveData<List<String>> getActiveCategoriesFromIdentifier();

    @Query("SELECT DISTINCT category FROM IDENTIFIER WHERE timeActivated > :resetTime")
    public LiveData<List<String>> getRecentCategoriesFromIdentifier(Long resetTime);

    @Query("SELECT DISTINCT category FROM IDENTIFIER WHERE isActive = 1")
    public List<String> getActiveCategoriesFromIdentifier2();

    @Query("UPDATE IDENTIFIER SET timeActivated = :readTime WHERE identifier = :identifier")
    void updateIdentifierReadTime(String identifier, Long readTime);
}
