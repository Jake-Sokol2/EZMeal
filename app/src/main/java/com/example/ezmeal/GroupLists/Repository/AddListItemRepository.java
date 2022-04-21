package com.example.ezmeal.GroupLists.Repository;

// A repository is a class where we fetch data from API or DB.

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddListItemRepository{
    final MutableLiveData<List<List<String>>> aList = new MutableLiveData<List<List<String>>>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private GroupListsViewModel theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

    String brandName, itemName;



    public MutableLiveData<List<List<String>>> getShoppingList()
    {
        GetItemCallBack aCallback = new GetItemCallBack() {
            @Override
            public void callback(List<List<String>> someList) {
                aList.setValue(someList);
            }
        };
        getDataFirebase(aCallback);
        return aList;
    }

    public void getDataFirebase(GetItemCallBack beep)
    {

        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        List<List<String>> tmpListList = new ArrayList<List<String>>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("MYDEBUG", document.getId() + " => " + document.getData());
                                brandName = document.getString("brand");
                                itemName = document.getString("name");
                                //quantity = document.getDouble("quantity");
                                if (Objects.equals(document.getString("user"), email))
                                {
                                    List<String> tmpList = new ArrayList<>();
                                    tmpList.add(itemName);
                                    tmpList.add(brandName);
                                    tmpList.add("1");
                                    tmpListList.add(tmpList);
                                }

                            }
                        }
                        else
                        {
                            Log.w("MYDEBUG", "Error getting documents.", task.getException());
                        }

                        beep.callback(tmpListList);
                    }
                });
    }

}
