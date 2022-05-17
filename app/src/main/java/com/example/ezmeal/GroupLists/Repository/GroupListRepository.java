package com.example.ezmeal.GroupLists.Repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.ezmeal.roomDatabase.Category;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.Identifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupListRepository {
    final MutableLiveData<List<String>> repoGroupList = new MutableLiveData<>();
    final MutableLiveData<List<String>> sharedGroupLists = new MutableLiveData<>();
    final MutableLiveData<List<Boolean>> isSelectedList = new MutableLiveData<>();
    private List<String> tmpGroupList = new ArrayList<String>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public List<String> identifiers = new ArrayList<String>();
    private Application application;
    EZMealDatabase sqlDb;

    String listName;

    public GroupListRepository(@NonNull Application application)
    {
        this.application = application;
        sqlDb = Room.databaseBuilder(application.getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();
    }

    public MutableLiveData<List<String>> getGroupList()
    {
        //tmpGroupList = new ArrayList<String>();
        SomeCallBack asyncBB = new SomeCallBack() {
            @Override
            public void callback(List<String> someList) { repoGroupList.setValue(someList); }
        };
        //getListData(asyncBB);
        getSharedListData(asyncBB);
        return repoGroupList;
        //getSharedListData();

    }

    public MutableLiveData<List<String>> getSharedGroupList()
    {
        YetAnotherCallBack yeet = new YetAnotherCallBack() {
            @Override
            public void callback(List<String> someList) { sharedGroupLists.setValue(someList); }
        };

        //getSharedListData(yeet);
        return sharedGroupLists;

    }

    public void getIdentifiers() {

        identifiers = sqlDb.testDao().getDistinctIdentifiers();

        // todo: remove, this is nuking shared preferences
        application.getSharedPreferences("FirstRunAfterUpdate", 0).edit().clear().commit();

        sqlDb = Room.databaseBuilder(application.getApplicationContext(), EZMealDatabase.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().enableMultiInstanceInvalidation().build();

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
                        Identifier newIdentifier = new Identifier(substringList.get(i), categoryList.get(i), false, 0L);
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

    public MutableLiveData<List<Boolean>> getSelected()
    {
        AnotherCallBack asyncBB = new AnotherCallBack() {
            @Override
            public void callback(List<Boolean> selectedList) { isSelectedList.setValue(selectedList); }
        };
        return isSelectedList;
    }

    //Gets data from Firebase
    public void getListData(SomeCallBack beep)
    {
        List<String> tmpList = new ArrayList<String>();
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {

                        if(task.isSuccessful())
                        {
                                //Looping through documents in the current collection
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listName = document.getString("ListName");
                                if (Objects.equals(document.getString("Creator"), email)) {

                                    tmpList.add(listName);

                                }
                            }
                            //If there are no lists for a user, create My List
                            //So they always have a list.
                            if(tmpList.size() == 0)
                            {
                                Map<String, Object> data = new HashMap<>();
                                data.put("Creator", email);
                                data.put("ListName", "My List");

                                List<String> users = new ArrayList<String>();
                                data.put("SharedUsers", users);

                                tmpList.add("My List");

                            }

                        }
                        else
                        {
                            Log.w("GroupListRepo", "No hay nada");
                        }
                        beep.callback(tmpList);
                    }
                });
        /*
        db.collection("Groups").whereArrayContains("SharedUsers", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        tmpList.add(document.getString("ListName"));
                    }
                }

            }

        });*/


    }

    public void getSharedListData(SomeCallBack beep)
    {
        List<String> tmpList = new ArrayList<String>();
        String email = mAuth.getCurrentUser().getEmail();


       //List<String> finalTmpList = tmpList;
        db.collection("Groups").whereArrayContains("SharedUsers", email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        tmpList.add(document.getString("ListName"));
                    }
                }
                beep.callback(tmpList);
            }
        });
    }



    public MutableLiveData<List<String>> returnGroupList()
    {
        return repoGroupList;
    }


    public interface SomeCallBack
    {
        public void callback(List<String> someList);
    }

    public interface AnotherCallBack
    {
        public void callback(List<Boolean> selectedList);
    }

    public interface YetAnotherCallBack
    {
        public void callback(List<String> someList);
    }

}


