package com.example.ezmeal.GroupLists.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupListsFragmentModel
{
    private List<List<String>> shoppingList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String itemName;
    String brandName;
    String docID;

    public GroupListsFragmentModel(){
        shoppingList = new ArrayList<List<String>>();
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

    public void removeItem(int position)
    {
        itemName = shoppingList.get(position).get(0);
        brandName = shoppingList.get(position).get(1);

        shoppingList.remove(position);

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

    public int listLength()
    {
        return shoppingList.size();
    }

    public void dumpList()
    {
        shoppingList.clear();
    }

    public void addDataToFirestore(String itemName, String brandName) {

        //Code to make retrieval of items user specific
        //Get FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get current user instance
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();

        CollectionReference dbItems = db.collection("Items");
        Item item = new Item(itemName, brandName, email);
        dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                Log.i("Item added", "success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getContext(), "Item not added", Toast.LENGTH_SHORT).show();
                Log.i("Item failed to add.", "failure");
            }
        });
    }

    public List<List<String>> getGroceryList() {
        return shoppingList;
    }

    public void restoreGroceryList(List<List<String>> theList) {
        shoppingList = theList;
    }

}
