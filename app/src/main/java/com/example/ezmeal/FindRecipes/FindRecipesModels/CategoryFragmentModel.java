package com.example.ezmeal.FindRecipes.FindRecipesModels;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentModel
{
    private List<String> recipeList;
    private List<String> uriList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryFragmentModel()
    {
        recipeList = new ArrayList<String>();
        uriList = new ArrayList<String>();
    }

    public void addItem(String recipeTitle, String uri)
    {
        uriList.add(uri);
        recipeList.add(recipeTitle);
    }

    public void setRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }

    public void setUriListEnabled(int position)
    {
        this.uriList = uriList;
    }

    public int listLength()
    {
        return recipeList.size();
    }

    public void dumpList()
    {
        recipeList.clear();
        uriList.clear();
    }

    /*
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
     */

    public List<String> getRecipeList()
    {
        return recipeList;
    }

    public List<String> getUriList()
    {
        return uriList;
    }

    public void restoreRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }
}