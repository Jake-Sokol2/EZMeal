package com.example.ezmeal.GroupLists.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupListRepository {
    final MutableLiveData<List<String>> repoGroupList = new MutableLiveData<>();
    final MutableLiveData<List<String>> sharedGroupLists = new MutableLiveData<>();
    final MutableLiveData<List<Boolean>> isSelectedList = new MutableLiveData<>();
    private List<String> tmpGroupList = new ArrayList<String>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String listName;


    public MutableLiveData<List<String>> getGroupList()
    {
        //tmpGroupList = new ArrayList<String>();
        SomeCallBack asyncBB = new SomeCallBack() {
            @Override
            public void callback(List<String> someList) { repoGroupList.setValue(someList); }
        };
        //getListData(asyncBB);
        getSharedListData(asyncBB);
        return repoGroupList;
        //getSharedListData();

    }
/*
    public MutableLiveData<List<String>> getSharedGroupList()
    {
        YetAnotherCallBack yeet = new YetAnotherCallBack() {
            @Override
            public void callback(List<String> someList) { sharedGroupLists.setValue(someList); }
        };

        //getSharedListData(yeet);
        return sharedGroupLists;

    }

*/

    public MutableLiveData<List<Boolean>> getSelected()
    {
        AnotherCallBack asyncBB = new AnotherCallBack() {
            @Override
            public void callback(List<Boolean> selectedList) { isSelectedList.setValue(selectedList); }
        };
        return isSelectedList;
    }

    //Gets data from Firebase
    public void getListData(SomeCallBack beep)
    {
        List<String> tmpList = new ArrayList<String>();
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {

                        if(task.isSuccessful())
                        {
                                //Looping through documents in the current collection
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listName = document.getString("ListName");
                                if (Objects.equals(document.getString("Creator"), email)) {

                                    tmpList.add(listName);

                                }
                            }
                            //If there are no lists for a user, create My List
                            //So they always have a list.
                            if(tmpList.size() == 0)
                            {
                                Map<String, Object> data = new HashMap<>();
                                data.put("Creator", email);
                                data.put("ListName", "My List");

                                List<String> users = new ArrayList<String>();
                                data.put("SharedUsers", users);

                                tmpList.add("My List");

                            }

                        }
                        else
                        {
                            Log.w("GroupListRepo", "No hay nada");
                        }
                        beep.callback(tmpList);
                    }
                });
        /*
        db.collection("Groups").whereArrayContains("SharedUsers", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        tmpList.add(document.getString("ListName"));
                    }
                }

            }

        });*/


    }

    public void getSharedListData(SomeCallBack beep)
    {
        List<String> tmpList = new ArrayList<String>();
        String email = mAuth.getCurrentUser().getEmail();


       //List<String> finalTmpList = tmpList;
        db.collection("Groups").whereArrayContains("SharedUsers", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        tmpList.add(document.getString("ListName"));
                    }
                }
                beep.callback(tmpList);
            }
        });
    }



    public MutableLiveData<List<String>> returnGroupList()
    {
        return repoGroupList;
    }

    public void manualSetValue(List<String> aList) { repoGroupList.setValue(aList); }
    public void manualSetSelValue(List<Boolean> aList) { isSelectedList.setValue(aList); }

    public interface SomeCallBack
    {
        public void callback(List<String> someList);
    }

    public interface AnotherCallBack
    {
        public void callback(List<Boolean> selectedList);
    }

    public interface YetAnotherCallBack
    {
        public void callback(List<String> someList);
    }



}


