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
import java.util.List;
import java.util.Objects;

public class GroupListRepository {
    final MutableLiveData<List<String>> repoGroupList = new MutableLiveData<>();
    final MutableLiveData<List<Boolean>> isSelectedList = new MutableLiveData<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String listName;


    public MutableLiveData<List<String>> getGroupList()
    {
        SomeCallBack asyncBB = new SomeCallBack() {
            @Override
            public void callback(List<String> someList) { repoGroupList.setValue(someList); }
        };
        getListData(asyncBB);
        return repoGroupList;
    }



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
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        List<String> tmpList = new ArrayList<String>();
                        if(task.isSuccessful())
                        {

                            //Looping through documents in the current collection
                            for(QueryDocumentSnapshot document: task.getResult())
                            {
                                listName = document.getString("ListName");
                                if (Objects.equals(document.getString("Creator"), email))
                                {

                                    tmpList.add(listName);

                                }
                            }
                        }
                        else
                        {
                            Log.w("GroupListRepo", "No hay nada");
                        }
                        beep.callback(tmpList);
                    }
                });

    }




    public interface SomeCallBack
    {
        public void callback(List<String> someList);
    }

    public interface AnotherCallBack
    {
        public void callback(List<Boolean> selectedList);
    }

}


