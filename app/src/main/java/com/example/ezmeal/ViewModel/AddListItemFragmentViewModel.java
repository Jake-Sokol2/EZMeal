package com.example.ezmeal.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.Repository.AddListItemRepository;

//This class has to handle passing data back to the View/Fragment

public class AddListItemFragmentViewModel extends ViewModel
{
    //Not used yet until we can read/write firebase
    private AddListItemRepository listItemRepo;

    //Live data
    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();

    public AddListItemFragmentViewModel(){
        listItemRepo = new AddListItemRepository();
    }

    public void setData(String item) {
        selectedItem.setValue(item);

    }

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

}
