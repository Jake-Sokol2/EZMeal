package com.example.ezmeal.FindRecipes.FindRecipesModels;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentChildHorizontalRecyclerModel
{
    private List<String> recipeList;
    private List<String> uriList;
    private List<String> recipeIdList;
    private List<Double> avgRatingList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryFragmentChildHorizontalRecyclerModel()
    {
        recipeList = new ArrayList<String>();
        uriList = new ArrayList<String>();
        avgRatingList = new ArrayList<Double>();
    }

    public CategoryFragmentChildHorizontalRecyclerModel(List<String> recipeList, List<String> uriList, List<Double> avgRatingList)
    {
        this.recipeList = recipeList;
        this.uriList = uriList;
        this.avgRatingList = avgRatingList;
    }

    public void addItem(String recipeTitle, String uri, Double avgRating)
    {
        uriList.add(uri);
        recipeList.add(recipeTitle);
        avgRatingList.add(avgRating);
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
        avgRatingList.clear();
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

    public void setUriList(List<String> uriList)
    {
        this.uriList = uriList;
    }

    public List<String> getRecipeIdList()
    {
        return recipeIdList;
    }

    public void setRecipeIdList(List<String> recipeIdList)
    {
        this.recipeIdList = recipeIdList;
    }

    public List<Double> getAvgRatingList()
    {
        return avgRatingList;
    }

    public void setAvgRatingList(List<Double> avgRatingList)
    {
        this.avgRatingList = avgRatingList;
    }

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