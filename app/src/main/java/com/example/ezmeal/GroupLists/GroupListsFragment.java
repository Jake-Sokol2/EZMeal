package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.Model.Item;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.RoomDatabase.Rating;
import com.example.ezmeal.SwipeDeleteCallback;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Not the bubbles but the actual list itself
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListsFragment extends Fragment
{
    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroupListsFragmentModel theModel = new GroupListsFragmentModel();
    private List<List<String>> localShoppingList;
    private GroupListsViewModel theVM;

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private GroupListsFragmentRecyclerAdapter adapter;
    private GroupListFragHorizontalRecyclerAdapter hAdapter;
    //Firebase variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public FirebaseUser mCurrentUser;

    private String brand, name;
    private double quantity;
    public String email;
    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupListsFragment()
    {
        // Required empty public constructor
    }

    public GroupListsFragment(GroupListsFragmentModel theModel, GroupListFragHorizontalRecyclerAdapter adapter)
    {
        this.theModel = theModel;
        hAdapter = adapter;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupListsFragment newInstance(String param1, String param2)
    {
        GroupListsFragment fragment = new GroupListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            theModel.restoreGroceryList((List<List<String>>) savedInstanceState.getSerializable(RV_DATA));
        }
        else
        {
            if (getArguments() != null)
            {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        adapter = new GroupListsFragmentRecyclerAdapter(theModel.getGroceryList());



        /*
        No longer needed because adding items works and the items
        are not nuked on rotation.

        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        */

    }


    private EditText itemname, brandname;
    private Button additem;

    private String itemName, brandName, userName;


    private void addDataToFirestore(String itemName, String brandName, String userName)
    {

        //Code to make retrieval of items user specific
        //Get FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get current user instance
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();

        CollectionReference dbItems = db.collection("Items");
        Item item = new Item(itemName, brandName, userName);
        dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getContext(), "Item not added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_group_lists, container, false);
        view = inflater.inflate(R.layout.fragment_group_list_category, container, false);
        theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);





        //Observe live data and update grocery list
        theVM.updateShoppingList().observe(getViewLifecycleOwner(), shoppingList ->
        {
            if(shoppingList != null)
            {

                if (shoppingList.size() > 0)
                {
                    for(int i = 0; i < shoppingList.size(); i++)
                        theModel.addItem(shoppingList.get(i));
                }

            }
            adapter.notifyDataSetChanged();
            Log.d("adapter", "adapter has been notified");
        });

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupList);
        rvGroupList.setAdapter(adapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);

        //RatingsDatabase ratingsDb = Room.databaseBuilder(getContext().getApplicationContext(), RatingsDatabase.class, "user")
        //        .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        //float r = ratingsDb.ratingDao().getSpecificRating("1QEndfywxZpq7vnzFZo0");
        // back stack logs
        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Group Lists opened: " + numOfBackstack);

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        // back stack logs

        //adapter = new MainRecyclerAdapter(groceryList);
        //adapter = new MainRecyclerAdapter(theModel.getGroceryList());





        //adapter.notifyDataSetChanged();

        //Attach the ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeDeleteCallback(adapter, theModel));
        itemTouchHelper.attachToRecyclerView(rvGroupList);

        // Add some data
        // todo: remove this when user's list saves on application close
        Log.d("RecyclerView", "There is a recycler view?" + rvGroupList);
        adapter.notifyDataSetChanged();

        //clickedView = (View) view.findViewById(R.id.editListItem);
        // edit list item feature should start here
        adapter.setOnItemClickListener(new GroupListsFragmentRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, TextView text)
            {
                //String selectedName = groceryList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                Toast.makeText(getContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…
                //long tv = adapter.getItemId(position);

                // RecyclerView.ViewHolder vh = adapter.getView;
                //View v = vh.itemView;
            }


        });


        Button btnAddListItem = (Button) view.findViewById(R.id.btnAddItem4);
        btnAddListItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //Save recycler view and the model list
                Parcelable rvState = rvGroupList.getLayoutManager().onSaveInstanceState();
                Bundle out = new Bundle();
                out.putSerializable("rvData", (Serializable) theModel.getGroupList());
                out.putParcelable("rvState", rvState);

                //Fragment manager to open new AddListItemFrag
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //AddListItemFragment addItemFrag = new AddListItemFragment(theModel, adapter);
                AddButtonFragment addBtn = new AddButtonFragment();

                //Set the arguments to grab in the new fragment
                addBtn.setArguments(out);

                ft.setReorderingAllowed(true);

                ft.add(addBtn, "TAG").addToBackStack("TAG");
                ft.show(addBtn);
                ft.commit();


            }//Add item onClick
        });
    /*
        //Get FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Get current user instance
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        String email = mCurrentUser.getEmail();

        //Code to display database items
        db = FirebaseFirestore.getInstance();

        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("MYDEBUG", document.getId() + " => " + document.getData());
                                brand = document.getString("brand");
                                name = document.getString("name");
                                //quantity = document.getDouble("quantity");
                                if (Objects.equals(document.getString("user"), email))
                                {
                                    theModel.addItem(name, brand);

                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Log.w("MYDEBUG", "Error getting documents.", task.getException());
                        }
                    }
                });
        */
    }


}



/*
=======
    @Override
    public void onStart()
    {
        super.onStart();
        adapter.notifyDataSetChanged();
    }


>>>>>>> Stashed changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        Parcelable rvState = rvGroupList.getLayoutManager().onSaveInstanceState();
        super.onSaveInstanceState(outState);
        //I need to save the grocery list here
        //save recycler view position?
        outState.putParcelable(RECYCLER_VIEW_KEY, rvState);
        //save recycler view items?
        outState.putSerializable(RV_DATA, (Serializable) theModel.getGroceryList());
        //getChildFragmentManager().putFragment(outState, "bottom_dialog", bottomSheetDialogFrag);
    }
*/





