package com.example.ezmeal.FindRecipes.FindRecipesRespositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;
import com.example.ezmeal.roomDatabase.TestDao;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FeaturedFragmentRoomRepository
{
    private EZMealDatabase sqlDb;

    public FeaturedFragmentRoomRepository(Application application)
    {
        sqlDb = Room.databaseBuilder(application.getApplicationContext(), EZMealDatabase.class, "user").build();
        //EZMealDatabase roomDatabase = EZMealDatabase.getInstance(application);
        //testDao = roomDatabase.testDao();
        //allNotes = testDao.getActiveCategoriesFromIdentifier();
    }

    public LiveData<List<String>> getActiveCategoriesFromIdentifier()
    {
        return sqlDb.testDao().getActiveCategoriesFromIdentifier();
    }

    public void insertRecycler2(RecyclerRecipe2 recipe)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit((Callable<Void>) () ->
        {
            sqlDb.testDao().insertRecyclerRecipe2(recipe);
            return null;
        });
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
