package com.example.ezmeal.FindRecipes.FindRecipesViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

public class CategoryFragmentViewModel extends ViewModel
{
    private MutableLiveData<Integer> numOfRetrievedRecipes;
    private MutableLiveData<Integer> numOfRetrievedHighRatedRecipes;
    private MutableLiveData<Set<Integer>> setOfUniqueRecipes;
    private MutableLiveData<Set<Integer>> setOfUniqueHighRatedRecipes;

    public CategoryFragmentViewModel()
    {
        numOfRetrievedRecipes = new MutableLiveData<>(0);

        numOfRetrievedHighRatedRecipes = new MutableLiveData<>(0);

        Set<Integer> initialSet = new HashSet<>();
        setOfUniqueRecipes = new MutableLiveData<Set<Integer>>(initialSet);

        Set<Integer> initialHighRatedSet = new HashSet<>();
        setOfUniqueHighRatedRecipes = new MutableLiveData<Set<Integer>>(initialHighRatedSet);
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
