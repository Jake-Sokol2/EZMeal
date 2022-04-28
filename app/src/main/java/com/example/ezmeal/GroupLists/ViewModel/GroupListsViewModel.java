package com.example.ezmeal.GroupLists.ViewModel;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.Repository.AddListItemRepository;
import com.example.ezmeal.GroupLists.Repository.GroupListRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GroupListsViewModel extends ViewModel {
    public MutableLiveData<List<String>> groupList;
    public MutableLiveData<List<List<String>>> shoppingList;
    private AddListItemRepository theRepo = new AddListItemRepository();
    private GroupListRepository glRepo = new GroupListRepository();

    private GroupListsFragmentModel theModel;

    private List<Boolean> isSelectedList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String itemName;
    String brandName;
    String docID;

    public GroupListsViewModel()
    {
        //groupList = new MutableLiveData<>();
        isSelectedList = new ArrayList<Boolean>();
        //shoppingList = itemRepo.getShoppingList();
        theModel = new GroupListsFragmentModel();
    }

    public void addItem(String itemName, String itemBrand){
        List<String> tmp = new ArrayList<String>();
        List<List<String>> tmpList = new ArrayList<List<String>>();

        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add("1");

        if(shoppingList.getValue().size() > 0)
            tmpList = shoppingList.getValue();

        tmpList.add(tmp);
        shoppingList.setValue(tmpList);
    }

    public void addItem(String itemName, String itemBrand, int itemQuantity){
        List<String> tmp = new ArrayList<String>();
        List<List<String>> tmpList = new ArrayList<List<String>>();

        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add(String.valueOf(itemQuantity));

        if(shoppingList.getValue().size() > 0)
            tmpList = shoppingList.getValue();

        tmpList.add(tmp);
        shoppingList.setValue(tmpList);
    }

    public void removeItem(int position)
    {
        List<List<String>> tmp = new ArrayList<List<String>>();
        tmp = shoppingList.getValue();

        itemName = tmp.get(position).get(0);
        brandName = tmp.get(position).get(1);
        tmp.remove(position);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();
        CollectionReference dbItems = db.collection("Items");
        dbItems.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Hoyah", document.getId() + " => " + document.getData());
                        if(Objects.equals(itemName, document.getString("name")) &&
                                Objects.equals(document.getString("user"), email))
                        {
                            docID = document.getId();
                            dbItems.document(docID).delete();
                        } //end if
                    }// end for loop
                } //end task successful
            } //end onComplete

        });
    }

    public MutableLiveData<List<List<String>>> updateShoppingList()
    {
        if(shoppingList == null)
        {
            shoppingList = new MutableLiveData<List<List<String>>>();

            theRepo.getShoppingList().observeForever(aList ->
            {
                shoppingList.setValue(aList);
            });
        }

        return shoppingList;
    }

    public MutableLiveData<List<String>> updateGroupList()
    {
        if(groupList == null)
        {
            groupList = new MutableLiveData<List<String>>();
            glRepo.getGroupList().observeForever(repoGroupList ->
            {
                groupList.setValue(repoGroupList);
            });

        }
        return groupList;
    }






}
