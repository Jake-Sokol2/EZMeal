package com.example.ezmeal.GroupLists.Repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface GetItemCallBack {

    public void onCallback(List<List<String>> aList);

}