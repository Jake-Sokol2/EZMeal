package com.example.ezmeal.FindRecipes.FindRecipesViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.FeaturedFragmentRepository;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.FeaturedFragmentRoomRepository;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.TestDao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

public class FeaturedFragmentViewModel extends AndroidViewModel
{
    private MutableLiveData<Integer> numOfRetrievedRecipes;
    private MutableLiveData<Integer> numOfRetrievedHighRatedRecipes;

    private MutableLiveData<Set<Integer>> setOfUniqueRecipes;
    private MutableLiveData<Set<Integer>> setOfUniqueHighRatedRecipes;

    private MutableLiveData<Integer> horizontalRecipeRandomNum;
    private MutableLiveData<Integer> taskFeaturedRandomNum;

    private MutableLiveData<Task<QuerySnapshot>> taskPopularRecipesGreater;
    private MutableLiveData<Task<QuerySnapshot>> taskPopularRecipesLess;
    private MutableLiveData<Task<QuerySnapshot>> taskFeatured;

    //private FeaturedFragmentRepository repository = FeaturedFragmentRepository.getInstance(); //new FeaturedFragmentRepository();
    private FeaturedFragmentRepository repository;// = new FeaturedFragmentRepository();
    @NonNull
    private FeaturedFragmentRoomRepository roomRepository;

    private MediatorLiveData<List<String>> mediatorLiveData = new MediatorLiveData<>();

    private MutableLiveData<List<String>> lastActiveCategories = new MutableLiveData<>();

    @NonNull
    private LiveData<List<String>> returnList;

    private MutableLiveData<List<String>> allNotes;

    private MutableLiveData<List<HorizontalRecipe>> horizontalList;

    private Observer<List<HorizontalRecipe>> testObserver;
    private TestDao testDao;

    public FeaturedFragmentViewModel(@NonNull Application application)
    {
        super(application);
        repository = new FeaturedFragmentRepository(application);
        roomRepository = new FeaturedFragmentRoomRepository(application);
        EZMealDatabase roomDatabase = EZMealDatabase.getInstance(application);
        testDao = roomDatabase.testDao();

        numOfRetrievedRecipes = new MutableLiveData<>(0);

        numOfRetrievedHighRatedRecipes = new MutableLiveData<>(0);

        Set<Integer> initialSet = new HashSet<>();
        setOfUniqueRecipes = new MutableLiveData<Set<Integer>>(initialSet);

        Set<Integer> initialHighRatedSet = new HashSet<>();
        setOfUniqueHighRatedRecipes = new MutableLiveData<Set<Integer>>(initialHighRatedSet);

        //allNotes = roomRepository.getAllNotes();
        //returnList = roomRepository.getActiveCategoriesFromIdentifier();

    }

    public void setLastActiveCategories(List<String> lastActiveCategories)
    {
        this.lastActiveCategories.setValue(lastActiveCategories);
    }

    public MutableLiveData<List<String>> getLastActiveCategories()
    {
        return lastActiveCategories;
    }

    public LiveData<List<String>> getActiveCategoriesFromIdentifier()
    {
        return roomRepository.getActiveCategoriesFromIdentifier();
    }

    public MutableLiveData<List<HorizontalRecipe>> getHorizontalList(List<String> categoryList)
    {
        if (horizontalList == null || categoryList != lastActiveCategories)
        {
            horizontalList = new MutableLiveData<List<HorizontalRecipe>>();

            repository.getHorizontalLists(categoryList).observeForever(list ->
            {
                horizontalList.setValue(list);
                lastActiveCategories.setValue(categoryList);
            });
        }


        //repository.getHorizontalLists().removeObserver(testObserver);
        //horizontalList = repository.getHorizontalLists();
        return horizontalList;
    }
}
