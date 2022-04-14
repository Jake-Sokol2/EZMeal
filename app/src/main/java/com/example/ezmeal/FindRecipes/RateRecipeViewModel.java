package com.example.ezmeal.FindRecipes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RateRecipeViewModel extends ViewModel
{
   private MutableLiveData<Float> starRating;

    public RateRecipeViewModel()
    {
        starRating = new MutableLiveData<>();
    }

    public void setStar(Float rating)
    {
        starRating.setValue(rating);
    }

    public MutableLiveData<Float> getStarRating()
    {
        if (starRating == null)
        {
            starRating = new MutableLiveData<>();
        }

        return starRating;
    }
}
