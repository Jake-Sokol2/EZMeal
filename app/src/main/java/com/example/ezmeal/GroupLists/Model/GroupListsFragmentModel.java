package com.example.ezmeal.GroupLists.Model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Repository.GroupListRepository;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupListsFragmentModel
{
    private List<String> groupList = new ArrayList<String>(); //This represents a list you share with other people
    private List<Boolean> isSelectedList = new ArrayList<Boolean>(); //Marker that a given list is selected
    public List<String> grpListBubbles = new ArrayList<String>();
    private List<List<String>> shoppingList = new ArrayList<List<String>>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GroupListRepository glRepo;
    public String itemName;
    public String brandName;
    String docID;
    String tmpName;
    private String currentGroupList;


    public GroupListsFragmentModel()
    {
        groupList = new ArrayList<String>();
        isSelectedList = new ArrayList<Boolean>();
        shoppingList = new ArrayList<List<String>>();
       // getListData();

    }

    public void addList(String listName, Boolean isSelected)
    {
        groupList.add(listName);
        isSelectedList.add(isSelected);
    }

    public void addList(String listName)
    {
        groupList.add(listName);
    }

    public void addItem(String itemName, String itemBrand){
        List<String> tmp = new ArrayList<String>();
        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add("1");

        shoppingList.add(tmp);
    }

    public void addItem(String itemName, String itemBrand, int itemQuantity){
        List<String> tmp = new ArrayList<String>();
        tmp.add(itemName);
        tmp.add(itemBrand);
        tmp.add(String.valueOf(itemQuantity));

        shoppingList.add(tmp);
    }

    public void addItem(List<String> completeItem)
    {
        shoppingList.add(completeItem);
    }

    public void removeItem(int position)
    {

        itemName = shoppingList.get(position).get(0);
        brandName = shoppingList.get(position).get(1);

        //String groupName = "Tristan";

        shoppingList.remove(position);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();

        /*
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
        */

        CollectionReference dbItems = db.collection("Groups");
        dbItems.whereEqualTo("ListName", currentGroupList).get()
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
                                   public void onComplete(@NonNull Task<QuerySnapshot> task)
                                   {
                                       if(task.isSuccessful()) {
                                           for(QueryDocumentSnapshot aDoc : task.getResult())
                                           {
                                               if(Objects.equals(itemName, aDoc.getString("name")))
                                               {
                                                   docID = aDoc.getId();
                                                   dbShoppingList.document(docID).delete();
                                               }
                                           }
                                       }
                                   }
                                });
                            }
                        }
                    }
                });

    }

    public int listLength()
    {
        return shoppingList.size();
    }

    public int groupListLength()
    {
        return groupList.size();
    }

    public void dumpList()
    {
        shoppingList.clear();
    }

    public void dumpGroupList()
    {
        if(groupList != null)
            groupList.clear();

        if(isSelectedList != null)
            isSelectedList.clear();
    }

    public void setGroupList(List<String> categoryList)
    {
        this.groupList = categoryList;
    }

    public void setSelected(int position)
    {
        for(int i = 0; i < isSelectedList.size(); i++)
        {
            isSelectedList.set(i, false);
        }
        isSelectedList.set(position, true);
    }

    public void setNotSelected(int position)
    {
        isSelectedList.set(position, false);
    }

    public List<String> getGroupList()
    {
        return groupList;
    }

    public List<Boolean> getIsSelectedList()
    {
        return isSelectedList;
    }

    public int getCurrentSelected() {
        for(int i = 0; i < isSelectedList.size(); i++)
            if(isSelectedList.get(i))
                return i;
            return 0;
    }

    public void addDataToFirestore(String itemName, String brandName) {


        tmpName = "";
        //The list is empty for some reason so we're not getting the correct list later on
        Log.i("Lists", "There are " + isSelectedList.size() + " bools and " + groupList.size()
        + " strings");
        for(int i = 0; i < isSelectedList.size(); i++)
        {
            if(isSelectedList.get(i) == true)
                tmpName = groupList.get(i);
        }

        //Code to make retrieval of items user specific
        //Get FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get current user instance
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();
        Item item = new Item(itemName, brandName, email);

        //CollectionReference dbItems = db.collection("Items");
        db.collection("Groups").whereEqualTo("Creator", email).whereEqualTo("ListName", tmpName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot tmp = task.getResult().getDocuments().get(0);
                            String tmpDocName = tmp.getId();

                            db.collection("Groups").document(tmpDocName).collection("Items").add(item);

                        }
                        else
                        {
                            Log.i("AddItem", "didn't get the correct list");
                        }
                    }
                });



        /*
        //Code to make retrieval of items user specific
        //Get FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get current user instance
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();

        CollectionReference dbItems = db.collection("Items");
        Item item = new Item(itemName, brandName, email);
        dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                //Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                //Toast.makeText(getContext(), "Item not added", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    public List<List<String>> getGroceryList()
    {
        return shoppingList;
    }


    public void fillGroceryList() {

        //String currentListName = groupList.get(getCurrentSelected());
        String currentListName = "Tristan";
        //List<List<String>> tmpListOfLists = new ArrayList<>();
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
                                              shoppingList.add(tmpList);
                                          }
                                          Log.i("Query", "Finished filling shopping list");
                                      }
                                  }
                               });
                           }
                       }
                   }
                });


    }

    public void setActiveGroupList(String name)
    {
        currentGroupList = name;
    }

    public String getActiveGrpListName()
    {
        return currentGroupList;
    }

    public void restoreGroceryList(List<List<String>> theList) {
        shoppingList = theList;
    }

    public void restoreSelectList(List<Boolean> theList)
    {
        isSelectedList = theList;
    }






}
