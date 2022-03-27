package navigationFragments.FindRecipes;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ezmeal.Model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FindRecipesModel
{
    private List<List<String>> recipeList;
    private List<List<Bitmap>> bitmapList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FindRecipesModel()
    {
        recipeList = new ArrayList<List<String>>();
        bitmapList = new ArrayList<List<Bitmap>>();
    }

    public void addItem(String recipeTitle, Bitmap bitmapImage)
    {
        List<String> tmp = new ArrayList<String>();
        tmp.add(recipeTitle);
        tmp.add("");

        List<Bitmap> tmpBitmap = new ArrayList<Bitmap>();
        tmpBitmap.add(bitmapImage);

        bitmapList.add(tmpBitmap);
        recipeList.add(tmp);
    }

    public int listLength()
    {
        return recipeList.size();
    }

    public void dumpList()
    {
        recipeList.clear();
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

    public List<List<String>> getRecipeList()
    {
        return recipeList;
    }

    public List<List<Bitmap>> getBitmapList()
    {
        return bitmapList;
    }

    public void restoreRecipeList(List<List<String>> recipeList)
    {
        this.recipeList = recipeList;
    }
}