package com.example.ezmeal.GroupLists.ViewModel;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.Repository.AddListItemRepository;
import com.example.ezmeal.GroupLists.Repository.GroupListRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GroupListsViewModel extends AndroidViewModel {
    public MutableLiveData<List<String>> groupList = new MutableLiveData<>();
    public MutableLiveData<List<List<String>>> shoppingList = new MutableLiveData<>();
    public MutableLiveData<List<Boolean>> selectedList = new MutableLiveData<>();
    Application application;
    private AddListItemRepository theRepo;
    private GroupListRepository glRepo = new GroupListRepository();
    public List<Boolean> isSelectedList = new ArrayList<Boolean>();
    public List<String> groupListNames = new ArrayList<String>();
    //public GroupListFragHorizontalRecyclerAdapter glFragAdapter = new GroupListFragHorizontalRecyclerAdapter(groupListNames, isSelectedList);
    private GroupListsFragmentModel theModel;
    private String currentGroupList;
    public List<String> grpListBubbles = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String itemName;
    String brandName;
    String docID;
    String groupListName;
    public GroupListFragHorizontalRecyclerAdapter glFragAdapter = new GroupListFragHorizontalRecyclerAdapter(groupListNames, isSelectedList);

    public GroupListsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        //groupList = new MutableLiveData<>();
        //isSelectedList = new ArrayList<Boolean>();
        //shoppingList = itemRepo.getShoppingList();
        theModel = new GroupListsFragmentModel();
        theRepo = new AddListItemRepository(application);
    }

    public void addList(String listName, Boolean isSelected)
    {
        groupListNames.add(listName);
        isSelectedList.add(isSelected);
    }
    public void addItem(String itemName, String itemBrand) {
        List<String> tmp = new ArrayList<String>();
        List<List<String>> tmpList = new ArrayList<List<String>>();

        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add("1");

        if (shoppingList.getValue().size() > 0)
            tmpList = shoppingList.getValue();

        tmpList.add(tmp);
        shoppingList.setValue(tmpList);
    }

    public void addItem(String itemName, String itemBrand, int itemQuantity) {
        List<String> tmp = new ArrayList<String>();
        List<List<String>> tmpList = new ArrayList<List<String>>();

        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add(String.valueOf(itemQuantity));

        if (shoppingList.getValue().size() > 0)
            tmpList = shoppingList.getValue();

        tmpList.add(tmp);
        shoppingList.setValue(tmpList);
    }
    public void setSelected(int position)
    {
        for(int i = 0; i < isSelectedList.size(); i++)
        {
            isSelectedList.set(i, false);
        }
        isSelectedList.set(position, true);
    }
    public void dumpGroupList()
    {
        groupListNames.clear();
        isSelectedList.clear();
    }

    public void setNotSelected(int position)
    {
        isSelectedList.set(position, false);
    }

    public void removeItem(int position) {
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
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Hoyah", document.getId() + " => " + document.getData());
                        if (Objects.equals(itemName, document.getString("name")) &&
                                Objects.equals(document.getString("user"), email)) {
                            docID = document.getId();
                            dbItems.document(docID).delete();
                        } //end if
                    }// end for loop
                } //end task successful
            } //end onComplete

        });
    }

    public void wipeList() {
        List<List<String>> tmpList = new ArrayList<>();
        shoppingList.setValue(tmpList);
    }

    public void wipeSelList() {
        List<Boolean> tmpList = new ArrayList<>();
        selectedList.setValue(tmpList);
    }


    public void setSelectList(int size) {
        if (isSelectedList.size() == 0) {
            //This is to initialize the selected values. The first group list
            //will always be selected by default.
            for (int i = 0; i < size; i++) {
                if (i == 0)
                    isSelectedList.add(true);
                else
                    isSelectedList.add(false);

            }
        }

        selectedList.setValue(isSelectedList);
    }

    public void setShoppingList(String name) {
        if (name != "")
            groupListName = name;

        theRepo.setShoppingList(name);
    }

    public void updateSelList(List<Boolean> bop)
    {
        isSelectedList = bop;
    }

    public MutableLiveData<List<Boolean>> updateSelectList()
    {
        /*
        theRepo.getSelectList().observeForever(selectList ->
        {
           if(selectList != null && selectedList.getValue() != selectList)
           {
               selectedList.setValue(selectList);
           }
        });
        */
        return selectedList;
    }

    public MutableLiveData<List<List<String>>> updateShoppingList()
    {

        theRepo.getShoppingList().observeForever(aList ->
        {
            if(aList != null && shoppingList.getValue() != aList)
                shoppingList.setValue(aList);
        });

        return shoppingList;
    }

    public MutableLiveData<List<String>> getGroupList()
    {

        //groupList = new MutableLiveData<List<String>>();
        glRepo.getGroupList().observeForever(repoGroupList ->
        {
            groupList.setValue(repoGroupList);
        });


        return groupList;
    }


    public List<List<String>> fillShoppingList()
    {

        //String currentListName = groupList.get(getCurrentSelected());
        String currentListName = "Tristan";
        List<List<String>> tmpListOfLists = new ArrayList<>();
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Groups").whereEqualTo("ListName", currentListName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().getDocuments().size() > 0)
                            {
                                DocumentSnapshot tmpDoc = task.getResult().getDocuments().get(0);
                                String tmpDocName = tmpDoc.getId();

                                CollectionReference dbShoppingList = db.collection("Groups").document(tmpDocName).collection("Items");
                                dbShoppingList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(QueryDocumentSnapshot docBoi : task.getResult()) {
                                                List<String> tmpList = new ArrayList<>();
                                                brandName = docBoi.getString("brand");
                                                itemName = docBoi.getString("name");

                                                tmpList.add(itemName);
                                                tmpList.add(brandName);
                                                tmpList.add("1");
                                                tmpListOfLists.add(tmpList);
                                            }
                                            Log.i("Query", "Finished filling shopping list");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

        return tmpListOfLists;
    }

    public void setActiveGroupList(String name)
    {
        currentGroupList = name;
    }
    public String getActiveGrpListName()
    {
        return currentGroupList;
    }









}
