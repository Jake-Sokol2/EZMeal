package com.example.ezmeal.FindRecipes;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RateRecipeBubbleViewModel extends ViewModel
{
    private MutableLiveData<List<String>> ratingBubbleList;

    public RateRecipeBubbleViewModel()
    {
        ratingBubbleList = new MutableLiveData<List<String>>();
    }

    public void setRatingBubbleList(List<String> chosenBubbles)
    {
        ratingBubbleList.setValue(chosenBubbles);
    }

    public MutableLiveData<List<String>> getRatingBubbleList()
    {
        if (ratingBubbleList == null)
        {
            ratingBubbleList = new MutableLiveData<>();
        }

        return ratingBubbleList;
    }
}
