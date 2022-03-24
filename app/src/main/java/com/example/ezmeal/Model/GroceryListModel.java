package com.example.ezmeal.Model;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroceryListModel {
    private List<List<String>> shoppingList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GroceryListModel(){
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

    public int listLength()
    {
        return shoppingList.size();
    }

    public void dumpList()
    {
        shoppingList.clear();
    }

    public void addDataToFirestore(String itemName, String brandName) {
        CollectionReference dbItems = db.collection("Items");
        Item item = new Item(itemName, brandName);
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
