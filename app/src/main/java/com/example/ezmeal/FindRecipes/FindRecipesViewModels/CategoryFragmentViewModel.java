package com.example.ezmeal.FindRecipes.FindRecipesViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.RetrievedRecipeLists;
import com.example.ezmeal.FindRecipes.FindRecipesModels.VerticalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.CategoryFragmentRepository;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.CategoryFragmentRoomRepository;
import com.example.ezmeal.FindRecipes.FindRecipesRespositories.FeaturedFragmentRoomRepository;
import com.example.ezmeal.roomDatabase.CategoryWithRecipes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CategoryFragmentViewModel extends AndroidViewModel
{
    private CategoryFragmentRoomRepository roomRepository;

    private MutableLiveData<Integer> numOfRetrievedRecipes;
    private MutableLiveData<Integer> numOfRetrievedHighRatedRecipes;
    private MutableLiveData<Set<Integer>> setOfUniqueRecipes;
    private MutableLiveData<Set<Integer>> setOfUniqueHighRatedRecipes;

    private MutableLiveData<Integer> horizontalRecipeRandomNum;
    private MutableLiveData<Integer> taskFeaturedRandomNum;

    private MutableLiveData<Task<QuerySnapshot>> taskPopularRecipesGreater;
    private MutableLiveData<Task<QuerySnapshot>> taskPopularRecipesLess;
    private MutableLiveData<Task<QuerySnapshot>> taskFeatured;

    public MutableLiveData<RetrievedRecipeLists> recipeLists = new MutableLiveData<>();
    private CategoryFragmentRepository firebaseRepository;

    private MutableLiveData<String> lastCategory = new MutableLiveData<>();

    private MutableLiveData<Boolean> isPopulated = new MutableLiveData<>();

    MediatorLiveData<RetrievedRecipeLists> mediatorTest = new MediatorLiveData<>();

    private MutableLiveData<RetrievedRecipeLists> anotherTest = new MutableLiveData<>();
    //LiveData<RetrievedRecipeLists> data = Transformations.switchMap(anotherTest, CategoryFragmentViewModel::processData);

    private boolean isActive = false;
    private LiveData<RetrievedRecipeLists> liveTest;

    //private RetrievedRecipesCustomLiveData<RetrievedRecipeLists> test;


    private static LiveData<RetrievedRecipeLists> processData(RetrievedRecipeLists retrievedRecipeLists)
    {
        LiveData<RetrievedRecipeLists> temp = new LiveData<RetrievedRecipeLists>()
        {
            @Override
            protected void setValue(RetrievedRecipeLists value)
            {
                super.setValue(retrievedRecipeLists);
            }
        };

        return temp;
    }


    public void setDataOther(String category, UUID queryId)
    {
        firebaseRepository.setDataOther(category, queryId);
    }

    public void setLastCategory(String category)
    {
        this.lastCategory.setValue(category);
    }

    public MutableLiveData<String> getLastCategory()
    {
        return lastCategory;
    }

    public void setDataWithRoomRecipes(String category)
    {
        //recipeLists.setValue(roomRepository.getCategoriesWithRecipes(category));

        //LiveData<List<CategoryWithRecipes>> roomRecipesLive = roomRepository.getCategoriesWithRecipes(category);

        /*List<CategoryWithRecipes> roomRecipes = roomRecipesLive.getValue();
        RetrievedRecipeLists tempRecipeLists = new RetrievedRecipeLists();
        List<String> recipeIdList = new ArrayList<>();
        List<HorizontalRecipe> horizontalRecipesList = new ArrayList<>();
        List<VerticalRecipe> verticalRecipeList = new ArrayList<>();
        if (roomRecipes != null)
        {
            for (int i = 0; i < roomRecipes.get(0).recyclerRecipe2List.size(); i++)
            {
                Log.i("test", "async test");

                //recipeIdList.add(roomRecipes.get(i).recyclerRecipe2List.get(i).getRecipeId());
                //horizontalRecipesList.add(roomRecipes.get(i).recyclerRecipe2List.get(i).get());
                //verticalRecipeList.add(tempRecipeLists.getVerticalList().get(i));
            }

            //tempRecipeLists.appendRecipeIdList(recipeIdList);

        }
         */
    }

    public void setDataWithSavedRecipes(String category)
    {
        roomRepository.queryCategoriesWithRecipes();
    }

    public MutableLiveData<RetrievedRecipeLists> getDataOther()
    {
        firebaseRepository.getDataOther().observeForever(returned ->
        {
            if (returned != null && recipeLists.getValue() != returned)
            {
                recipeLists.setValue(returned);
            }
        });

        return recipeLists;
    }


    public LiveData<RetrievedRecipeLists> getData()
    {
        return liveTest;
    }

    public void setData()
    {

        //liveTest = firebaseRepository.getLists();
    }

    public CategoryFragmentViewModel(@NonNull Application application)
    {
        super(application);
        firebaseRepository = new CategoryFragmentRepository(application);
        roomRepository = new CategoryFragmentRoomRepository(application);

        numOfRetrievedRecipes = new MutableLiveData<>(0);

        numOfRetrievedHighRatedRecipes = new MutableLiveData<>(0);

        Set<Integer> initialSet = new HashSet<>();
        setOfUniqueRecipes = new MutableLiveData<Set<Integer>>(initialSet);

        Set<Integer> initialHighRatedSet = new HashSet<>();
        setOfUniqueHighRatedRecipes = new MutableLiveData<Set<Integer>>(initialHighRatedSet);

        isPopulated = new MutableLiveData<>(false);
    }

    public MutableLiveData<Boolean> getIsPopulated()
    {
        return isPopulated;
    }

    public void setPopulated(Boolean choice)
    {
        isPopulated.setValue(choice);
    }

    LiveData<RetrievedRecipeLists> test = Transformations.map(recipeLists, list ->
    {
        return list;
    });

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public LiveData<RetrievedRecipeLists> select()
    {
        return mediatorTest;
    }

    public MutableLiveData<RetrievedRecipeLists> getListsTest()
    {
        /*firebaseRepository.getLists().observeForever(lists ->
        {
            if (lists != null)
            {
                recipeLists.setValue(lists);
            }
        });
*/
        return recipeLists;
    }

    // taskFeatured
    public MutableLiveData<Task<QuerySnapshot>> getTaskFeatured()
    {
        if (taskFeatured == null)
        {
            taskFeatured = new MutableLiveData<>();
        }

        return taskFeatured;
    }

    public void setTaskFeatured(Task<QuerySnapshot> retrievedTask)
    {
        this.taskFeatured.setValue(retrievedTask);
    }

    // taskFeaturedRandomNum
    public void setTaskFeaturedRandomNum(int randomNum)
    {
        this.taskFeaturedRandomNum.setValue(randomNum);
    }

    public MutableLiveData<Integer> getTaskFeaturedRandomNum()
    {
        if (taskFeaturedRandomNum == null)
        {
            taskFeaturedRandomNum = new MutableLiveData<>();
        }

        return taskFeaturedRandomNum;
    }




    // horizontalRecipeRandomNum
    public void setHorizontalRecipeRandomNum(int randomNum)
    {
        this.horizontalRecipeRandomNum.setValue(randomNum);
    }

    public MutableLiveData<Integer> getHorizontalRecipeRandomNum()
    {
        if (horizontalRecipeRandomNum == null)
        {
            horizontalRecipeRandomNum = new MutableLiveData<>();
        }

        return horizontalRecipeRandomNum;
    }

    // taskPopularRecipesGreater
    public MutableLiveData<Task<QuerySnapshot>> getTaskPopularRecipesGreater()
    {
        if (taskPopularRecipesGreater == null)
        {
            taskPopularRecipesGreater = new MutableLiveData<>();
        }

        return taskPopularRecipesGreater;
    }

    public void setTaskPopularRecipesGreater(Task<QuerySnapshot> retrievedTask)
    {
        this.taskPopularRecipesGreater.setValue(retrievedTask);
    }


    // taskPopularRecipesLess
    public MutableLiveData<Task<QuerySnapshot>> getTaskPopularRecipesLess()
    {
        if (taskPopularRecipesLess == null)
        {
            taskPopularRecipesLess = new MutableLiveData<>();
        }

        return taskPopularRecipesLess;
    }

    public void setTaskPopularRecipesLess(Task<QuerySnapshot> retrievedTask)
    {
        this.taskPopularRecipesLess.setValue(retrievedTask);
    }


    // setOfUniqueRecipes
    public Set<Integer> getSetOfUniqueRecipes()
    {
        if (setOfUniqueRecipes == null)
        {
            setOfUniqueRecipes = new MutableLiveData<Set<Integer>>();
        }

        return setOfUniqueRecipes.getValue();
    }

    public void setSetOfUniqueRecipes(Set<Integer> uniqueRecipes)
    {
        setOfUniqueRecipes.setValue(uniqueRecipes);
    }

    public void addToSetOfUniqueRecipes(Integer newRecipeId)
    {
        Set<Integer> uniqueRecipes = setOfUniqueRecipes.getValue();
        uniqueRecipes.add(newRecipeId);

        setOfUniqueRecipes.setValue(uniqueRecipes);
    }



    // setOfUniqueHighRatedRecipes
    public Set<Integer> getSetOfUniqueHighRatedRecipes()
    {
        if (setOfUniqueHighRatedRecipes == null)
        {
            setOfUniqueHighRatedRecipes = new MutableLiveData<Set<Integer>>();
        }

        return setOfUniqueHighRatedRecipes.getValue();
    }

    public void setSetOfUniqueHighRatedRecipes(Set<Integer> uniqueRecipes)
    {
        setOfUniqueHighRatedRecipes.setValue(uniqueRecipes);
    }

    public void addToSetOfUniqueHighRatedRecipes(Integer newRecipeId)
    {
        Set<Integer> uniqueRecipes = setOfUniqueHighRatedRecipes.getValue();
        uniqueRecipes.add(newRecipeId);

        setOfUniqueHighRatedRecipes.setValue(uniqueRecipes);
    }



    // numOfRetrievedRecipes
    public Integer getNumOfRetrievedRecipes()
    {
        if (numOfRetrievedRecipes == null)
        {
            numOfRetrievedRecipes = new MutableLiveData<>();
        }

        return numOfRetrievedRecipes.getValue();
    }

    public void setNumOfRetrievedRecipes(Integer numOfRecipes)
    {
        numOfRetrievedRecipes.setValue(numOfRecipes);
    }

    public void incrementNumOfRetrievedRecipesBy(Integer numIncrement)
    {
        numOfRetrievedRecipes.setValue(numOfRetrievedRecipes.getValue() + numIncrement);
    }



    // numOfRetrievedHighRatedRecipes
    public Integer getNumOfRetrievedHighRatedRecipes()
    {
        if (numOfRetrievedHighRatedRecipes == null)
        {
            numOfRetrievedHighRatedRecipes = new MutableLiveData<>();
        }

        return numOfRetrievedHighRatedRecipes.getValue();
    }

    public void setNumOfRetrievedHighRatedRecipes(Integer numOfRecipes)
    {
        numOfRetrievedHighRatedRecipes.setValue(numOfRecipes);
    }

    public void incrementNumOfRetrievedHighRatedRecipes(Integer numIncrement)
    {
        numOfRetrievedHighRatedRecipes.setValue(numOfRetrievedHighRatedRecipes.getValue() + numIncrement);
    }
}
