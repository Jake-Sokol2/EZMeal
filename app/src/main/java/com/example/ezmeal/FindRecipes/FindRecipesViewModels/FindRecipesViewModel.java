package com.example.ezmeal.FindRecipes.FindRecipesViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.FindRecipesFragmentModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Set;

public class FindRecipesViewModel extends ViewModel
{
    private MutableLiveData<FindRecipesFragmentModel> horizontalRecyclerModel;
    private MutableLiveData<FindRecipesFragmentHorizontalRecyclerAdapter> horizontalRecyclerAdapter;

    public FindRecipesViewModel()
    {
        horizontalRecyclerModel = new MutableLiveData<>();
        horizontalRecyclerAdapter = new MutableLiveData<>();
    }

    public MutableLiveData<FindRecipesFragmentModel> getHorizontalRecyclerModel()
    {
        if (horizontalRecyclerModel == null)
        {
            horizontalRecyclerModel = new MutableLiveData<>();
        }

        return horizontalRecyclerModel;
    }

    public void setHorizontalRecyclerAdapter(FindRecipesFragmentHorizontalRecyclerAdapter horizontalRecyclerAdapter)
    {
        this.horizontalRecyclerAdapter.setValue(horizontalRecyclerAdapter);
    }

    public MutableLiveData<FindRecipesFragmentHorizontalRecyclerAdapter> getHorizontalRecyclerAdapter()
    {
        if (horizontalRecyclerAdapter == null)
        {
            horizontalRecyclerAdapter = new MutableLiveData<>();
        }

        return horizontalRecyclerAdapter;
    }

    public void setHorizontalRecyclerModel(FindRecipesFragmentModel recyclerModel)
    {
        horizontalRecyclerModel.setValue(recyclerModel);
    }

    public void setSelected(int position, boolean isSelected)
    {
        horizontalRecyclerModel.getValue().getIsSelectedList().set(position, isSelected);
    }

    public void addItem(String category, Boolean isSelected)//, String url)
    {
        horizontalRecyclerModel.getValue().getCategoryList().add(category);
        horizontalRecyclerModel.getValue().getIsSelectedList().add(isSelected);
        //this.url.add(url);
    }

    public int getListSize()
    {
        return horizontalRecyclerModel.getValue().getCategoryList().size();
    }
}
