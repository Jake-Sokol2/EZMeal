package com.example.ezmeal.FindRecipes.FindRecipesViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Set;

public class FeaturedFragmentViewModel extends ViewModel
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



    public FeaturedFragmentViewModel()
    {
        numOfRetrievedRecipes = new MutableLiveData<>(0);

        numOfRetrievedHighRatedRecipes = new MutableLiveData<>(0);

        Set<Integer> initialSet = new HashSet<>();
        setOfUniqueRecipes = new MutableLiveData<Set<Integer>>(initialSet);

        Set<Integer> initialHighRatedSet = new HashSet<>();
        setOfUniqueHighRatedRecipes = new MutableLiveData<Set<Integer>>(initialHighRatedSet);
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
    public MutableLiveData<Integer> getNumOfRetrievedHighRatedRecipes()
    {
        if (numOfRetrievedHighRatedRecipes == null)
        {
            numOfRetrievedHighRatedRecipes = new MutableLiveData<>();
        }

        return numOfRetrievedHighRatedRecipes;
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
