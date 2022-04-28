package com.example.ezmeal.FindRecipes.FindRecipesRespositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.RetrievedRecipeLists;
import com.example.ezmeal.FindRecipes.FindRecipesModels.VerticalRecipe;
import com.example.ezmeal.roomDatabase.CategoryWithRecipes;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragmentRoomRepository
{
    private EZMealDatabase sqlDb;
    private List<CategoryWithRecipes> savedRecipeList;
    private MutableLiveData<RetrievedRecipeLists> returnRecipes = new MutableLiveData<>();

    public CategoryFragmentRoomRepository(Application application)
    {
        sqlDb = Room.databaseBuilder(application.getApplicationContext(), EZMealDatabase.class, "user").build();
        //EZMealDatabase roomDatabase = EZMealDatabase.getInstance(application);
        //testDao = roomDatabase.testDao();
        //allNotes = testDao.getActiveCategoriesFromIdentifier();
    }

    public MutableLiveData<RetrievedRecipeLists> getCategoriesWithRecipes()
    {
        //savedRecipeList = new ArrayList<>();
        //savedRecipeList = sqlDb.testDao().getCategoriesWithRecipes(category);
        return returnRecipes;
    }
    public void queryCategoriesWithRecipes()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit((Callable<Void>) () ->
        {
            RetrievedRecipeLists tempRecipeList = new RetrievedRecipeLists();
            List<HorizontalRecipe> tempHorizontalList = new ArrayList<>();
            List<VerticalRecipe> tempVerticalList = new ArrayList<>();
            List<String> tempRecipeIdList = new ArrayList<>();

            if (savedRecipeList == null)
            {
                savedRecipeList = new ArrayList<>();
            }
            else
            {
                List<RecyclerRecipe2> tempRecyclerRecipeList = savedRecipeList.get(0).recyclerRecipe2List;

                for (int i = 0; i < tempRecyclerRecipeList.size(); i++)
                {
                    if (tempRecyclerRecipeList.get(i).isHorizontal)
                    {
                        HorizontalRecipe tempHorizontalRecipe = new HorizontalRecipe(tempRecyclerRecipeList.get(i).getTitle(), tempRecyclerRecipeList.get(i).getImageUrl(),
                                tempRecyclerRecipeList.get(i).getRecipeId(), tempRecyclerRecipeList.get(i).getAverageRating());

                        tempHorizontalList.add(tempHorizontalRecipe);
                    }
                    else
                    {
                        VerticalRecipe tempVerticalRecipe = new VerticalRecipe(tempRecyclerRecipeList.get(i).getTitle(), tempRecyclerRecipeList.get(i).getImageUrl(),
                                tempRecyclerRecipeList.get(i).getRecipeId(), tempRecyclerRecipeList.get(i).getAverageRating(), tempRecyclerRecipeList.get(i).getTotalRatings());

                        tempVerticalList.add(tempVerticalRecipe);
                    }

                    tempRecipeIdList.add(tempRecyclerRecipeList.get(i).getRecipeId());
                }

                tempRecipeList.appendRecipeIdList(tempRecipeIdList);
                tempRecipeList.appendHorizontalList(tempHorizontalList);
                tempRecipeList.appendVerticalList(tempVerticalList);
            }

            returnRecipes.setValue(tempRecipeList);
            return null;
        });
    }

    public void insertRecycler2(RecyclerRecipe2 recipe)
    {
        // todo: close threads after use
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().insertRecyclerRecipe2(recipe);
            return null;
        });
    }

    public void insertAllRecyclerRecipe2(List<RecyclerRecipe2> insertList)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().insertAllRecyclerRecipe2(insertList);
            return null;
        });
    }

    public void deleteFromCategory2SpecificCategory(String category)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().deleteFromCategory2SpecificCategory(category);
            latch.countDown();
            return null;
        });

        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteFromRecyclerRecipe2SpecificCategory(String category)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().deleteFromRecyclerRecipe2SpecificCategory(category);
            latch.countDown();
            return null;
        });

        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteAllRecycler2()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().deleteALlRecyclerRecipes();
            return null;
        });
    }
}
