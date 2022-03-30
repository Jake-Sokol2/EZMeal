package navigationFragments.MyRecipes;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SpecificCategoryModel
{
    private List<String> recipeList;
    private List<Bitmap> imageList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SpecificCategoryModel()
    {
        recipeList = new ArrayList<String>();
        imageList = new ArrayList<Bitmap>();
    }

    public void addItem(String recipeTitle, Bitmap pathToImage)
    {
        imageList.add(pathToImage);
        recipeList.add(recipeTitle);
    }

    public int listLength()
    {
        return recipeList.size();
    }

    public void dumpList()
    {
        recipeList.clear();
        imageList.clear();
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

    public List<Bitmap> getImageList()
    {
        return imageList;
    }

    public void restoreRecipeList(List<String> recipeList)
    {
        this.recipeList = recipeList;
    }

    public void restoreImageList(List<Bitmap> imageList)
    {
        this.imageList = imageList;
    }
}