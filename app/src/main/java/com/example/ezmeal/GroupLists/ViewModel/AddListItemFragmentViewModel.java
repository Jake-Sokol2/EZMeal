package com.example.ezmeal.GroupLists.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.GroupLists.Repository.AddListItemRepository;

//This class has to handle passing data back to the View/Fragment

public class AddListItemFragmentViewModel extends AndroidViewModel
{
    //Not used yet until we can read/write firebase
    private AddListItemRepository listItemRepo;


    //Live data
    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();


    public AddListItemFragmentViewModel(@NonNull Application application){
        super(application);
        listItemRepo = new AddListItemRepository(application);


    }

    public void setData(String item) {
        selectedItem.setValue(item);

    }

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

}
