package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.example.ezmeal.roomDatabase.Rating;
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
    private boolean grpListLoaded = false;

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
    private List<Boolean> selectedList = new ArrayList<Boolean>();
    private List<String> groupListNames = new ArrayList<String>();
    private String listName;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group_list_category, container, false);

        adapter = new GroupListsFragmentRecyclerAdapter(theModel.getGroceryList());
        theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);
        hAdapter = new GroupListFragHorizontalRecyclerAdapter(theVM.groupListNames, theVM.isSelectedList);



        //Get the current selected list name.
        theVM.getGroupList().observe(getViewLifecycleOwner(), groupList ->
        {
            if(groupList != null)
            {
                if(groupList.size() > theModel.groupListLength())
                {

                    theModel.dumpGroupList();
                    for(int i = 0; i < groupList.size(); i++)
                    {
                        theModel.addList(groupList.get(i));
                    }
                    theVM.setSelectList(theVM.groupListNames.size());
                    loadListData();

                }
            }
        });

        theVM.updateShoppingList().observe(getViewLifecycleOwner(), shoppingList ->
        {
            if(shoppingList != null)
            {

                if (shoppingList.size() > theModel.getGroceryList().size())
                {
                    theModel.dumpList();
                    for(int i = 0; i < shoppingList.size(); i++) {
                        if(!Objects.equals(shoppingList.get(0), theModel.itemName))
                            theModel.addItem(shoppingList.get(i));
                    }
                }

            }
            adapter.notifyDataSetChanged();
            Log.d("adapter", "adapter has been notified");
        });

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupList);
        rvGroupList.setAdapter(adapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        //Attach the ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeDeleteCallback(adapter, theModel, theVM));
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
                // Code to use the selected name goes here???
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
                AddButtonFragment addBtn = new AddButtonFragment(theModel, adapter, hAdapter);

                //Set the arguments to grab in the new fragment
                addBtn.setArguments(out);

                ft.setReorderingAllowed(true);

                ft.add(addBtn, "TAG").addToBackStack("TAG");
                ft.show(addBtn);
                ft.commit();


            }//Add item onClick
        });
    }

    public void loadListData()
    {
        theModel.restoreSelectList(theVM.updateSelectList().getValue());
        theVM.wipeList();
        for(int i = 0; i < theVM.isSelectedList.size(); i++)
        {
            if(theVM.isSelectedList.get(i)) {
                theVM.setActiveGroupList(theVM.groupListNames.get(i));
                listName = theVM.getActiveGrpListName();
                theModel.setActiveGroupList(listName);
            }
        }
            //listName = theModel.getGroupList().get(theModel.getCurrentSelected());
        theVM.setShoppingList(listName);
        //theVM.setShoppingList("Tristan");
        }
    }









