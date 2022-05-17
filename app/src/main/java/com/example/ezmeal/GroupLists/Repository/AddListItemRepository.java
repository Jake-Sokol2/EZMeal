package com.example.ezmeal.GroupLists.Repository;

// A repository is a class where we fetch data from API or DB.

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.roomDatabase.Category;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.Identifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
    Working with live data and SQL cannot use mutable live data
    has to be live data.
 */

public class AddListItemRepository{
    final MutableLiveData<List<List<String>>> aList = new MutableLiveData<List<List<String>>>();
    final MutableLiveData<List<Boolean>> selectList = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public List<String> identifiers = new ArrayList<String>();
    public EZMealDatabase sqlDb;
    Application application;

    //private GroupListsViewModel theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

    String brandName, itemName, creator;

    public AddListItemRepository(@NonNull Application application)
    {
         this.application = application;
         sqlDb = Room.databaseBuilder(application.getApplicationContext(), EZMealDatabase.class, "user")
                 .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();

    }

    public void doSomething() {

        identifiers = sqlDb.testDao().getDistinctIdentifiers();

        // todo: remove, this is nuking shared preferences
        //getActivity().getSharedPreferences("FirstRunAfterUpdate", 0).edit().clear().commit();

        SharedPreferences sp;
        sp = application.getSharedPreferences("FirstRunAfterUpdate", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);

        // only executes the first time the app is installed or after it is updated
        if (isFirstRun) {
            sqlDb.testDao().deleteAllFromCategory();
            sqlDb.testDao().deleteAllFromIdentifier();

            // Insert list of categories for Find Recipes page into Room
            db.collection("RecipeCategoryRatingList").document("categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    List<String> categories = (ArrayList<String>) task.getResult().get("categories");
                    List<Category> categoryList = new ArrayList<Category>();
                    for (int i = 0; i < categories.size(); i++) {
                        Category newCategory = new Category(categories.get(i));
                        categoryList.add(newCategory);
                    }

                    sqlDb.testDao().insertAllCategory(categoryList);
                }
            });

            // Insert pairs of identifiers into Room for Find Recipes page
            db.collection("RecipeCategoryRatingList").document("categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Map<String, String> identifiers = (Map<String, String>) task.getResult().get("categoryIdentifiers");
                    List<String> substringList = new ArrayList<String>();
                    List<String> categoryList = new ArrayList<String>();

                    List<Identifier> identifierList = new ArrayList<Identifier>();

                    for (Map.Entry<String, String> entry : identifiers.entrySet()) {
                        substringList.add(entry.getValue());
                        categoryList.add(entry.getKey());
                    }

                    for (int i = 0; i < identifiers.size(); i++) {
                        Identifier newIdentifier = new Identifier(substringList.get(i), categoryList.get(i), false);
                        identifierList.add(newIdentifier);
                        //categoryList.add(newCategory);
                    }

                    sqlDb.testDao().insertAllIdentifier(identifierList);
                }
            });

            //sqlDb.testDao().getCategoriesCategoryEntity();
            Log.i("sp", "first run");


            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            Log.i("sp", "not first run");
        }

    }

    public void setShoppingList(String name)
    {
        String groupListName = "";
        if(name != "" )
            groupListName = name;

        GetItemCallBack aCallback = new GetItemCallBack() {
            @Override
            public void onCallback(List<List<String>> someList) {
                aList.setValue(someList);
            }
        };

        getDataFirebase(aCallback, groupListName);
    }

    public MutableLiveData<List<Boolean>> getSelectList() { return selectList; }

    public MutableLiveData<List<List<String>>> getShoppingList()
    {
        return aList;
    }

    public void getDataFirebase(GetItemCallBack aCallback, String groupListName)
    {
        /*
        sqlDb.testDao().updateAllIdentifiersIsNotActive();

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
                                    doSomething();
                                    for (String identifier:identifiers)
                                    {
                                        if(brandName!= null) {
                       Í                     if (brandName.toLowerCase().contains(identifier)) {
                                                // mark the identifier as active - tells Find Recipes to query recipes for the category belonging to this identifier
                                                sqlDb.testDao().updateIdentifierIsActive(identifier);

                                            }
                                        }
                                    }
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

        */
        String email = mAuth.getCurrentUser().getEmail();

        List<List<String>> tmpListOfLists = new ArrayList<List<String>>();

        db.collection("Groups").whereEqualTo("ListName", groupListName).get()
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


                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot docBoi : task.getResult()) {
                                            //add the items (sub documents) to a list and return it as the shopping list
                                            List<String> tmpList = new ArrayList<>();
                                            brandName = docBoi.getString("brand");
                                            itemName = docBoi.getString("name");
                                            creator = docBoi.getString("user");

                                            tmpList.add(itemName);
                                            tmpList.add(brandName);
                                            tmpList.add(creator);
                                            tmpList.add("1");
                                            tmpListOfLists.add(tmpList);
                                            //tmpList.clear();



                                        }
                                    } else {
                                        Log.i("Retrieval", "Error getting documents", task.getException());
                                    }

                                    aCallback.onCallback(tmpListOfLists);
                                }


                            });

                        }
                        else
                        {
                            Log.d("Retrieval", "There are no lists for some reason.");
                        }

                        }
                        else
                        {
                            Log.d("Retrieval", "Nothing here chief");
                        }
                    }
                });
        //return tmpListOfLists;

    }



}
