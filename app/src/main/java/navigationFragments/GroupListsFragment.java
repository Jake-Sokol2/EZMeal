package navigationFragments;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.MainActivity;
import com.example.ezmeal.Model.GroceryListModel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezmeal.MainRecyclerAdapter;
import com.example.ezmeal.Model.Item;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListsFragment extends Fragment
{
    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroceryListModel theModel = new GroceryListModel();
    //BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
    //BottomSheetDialogFragment bottomSheetDialogFrag = new BottomSheetDialogFragment();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;
    private FirebaseFirestore db;

    private String brand, name;
    private double quantity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupListsFragment() {
        // Required empty public constructor
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
    public static GroupListsFragment newInstance(String param1, String param2) {
        GroupListsFragment fragment = new GroupListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            theModel.restoreGroceryList((List<List<String>>)savedInstanceState.getSerializable(RV_DATA));
        }else {
            if (getArguments() != null) {
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

    private String itemName, brandName;



    private void addDataToFirestore(String itemName, String brandName){
        CollectionReference dbItems = db.collection("Items");
        Item item = new Item(itemName, brandName);
        dbItems.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Item not added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_group_lists, container, false);
        View view = inflater.inflate(R.layout.fragment_group_lists, container, false);

        // back stack logs
        String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        Log.i("TRACK BACKSTACK", "Group Lists opened: " + numOfBackstack);

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupLists);
        //adapter = new MainRecyclerAdapter(groceryList);
        adapter = new MainRecyclerAdapter(theModel.getGroceryList());
        rvGroupList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);


        // Add some data
        // todo: remove this when user's list saves on application close
        adapter.notifyDataSetChanged();

        //clickedView = (View) view.findViewById(R.id.editListItem);
        // edit list item feature should start here
        adapter.setOnItemClickListener(new MainRecyclerAdapter.MainAdapterListener()
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

        Button btnAddListItem = (Button) view.findViewById(R.id.btnAddItem);
        btnAddListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                //Fragment manager to open new AddListItemFrag
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                AddListItemFragment addItemFrag = new AddListItemFragment(theModel, adapter);
                ft.setReorderingAllowed(true);

                ft.add(addItemFrag, "TAG").addToBackStack("TAG");
                ft.show(addItemFrag);
                ft.commit();
                Log.d("I'm here", "help");

                 */

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);//com.google.android.material.R.style.Theme_Design_BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_list_item, (LinearLayout) view.findViewById(R.id.bottomSheetAddList));
                //BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
                //FragmentManager foo = getActivity().getSupportFragmentManager();
                //bottomSheet.show();
                //openActivityAddListItem();

                //bottomSheetDialog.setContentView(R.layout.fragment_add_list_item)

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                // onClickListeners for the bottom sheet's views
                Button btnConfirm = (Button) bottomSheetDialog.findViewById(R.id.btnConfirm);
                EditText editItemName = (EditText) bottomSheetDialog.findViewById(R.id.editItemName);
                EditText editBrandName = (EditText) bottomSheetDialog.findViewById(R.id.editBrandName);
                //TextView txtBrandName = (TextView)

                btnConfirm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        theModel.addItem(editItemName.getText().toString(), editBrandName.getText().toString());
                        adapter.notifyDataSetChanged();


                        itemName = editItemName.getText().toString();
                        brandName = editBrandName.getText().toString();

                        if (TextUtils.isEmpty(itemName)) {
                            itemname.setError("Enter item name");
                        } else if (TextUtils.isEmpty(brandName)) {
                            brandname.setError("Enter brand name");
                        } else {
                            addDataToFirestore(itemName, brandName);
                        }


                        // Closes the bottom sheet after the User enters an item
                        bottomSheetDialog.dismiss();
                    }//end additembutton in add item BottomSheetDialog
                });//OnclickListener



                /*
                    TODO: This block creates new frag, but layout is fucky.
                     Implement cancel button as well so you can get back to previous screen.
                */
/*
                AddListItemFragment AddItemFrag = new AddListItemFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.group_lists, AddItemFrag);
                transaction.commit();
*/

            }//Add item onClick
        });
        db = FirebaseFirestore.getInstance();


        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {@Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("MYDEBUG", document.getId() + " => " + document.getData());
                            brand = document.getString("brand");
                            name = document.getString("name");
                            //quantity = document.getDouble("quantity");
                            theModel.addItem(name, brand);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.w("MYDEBUG", "Error getting documents.", task.getException());
                    }
                }
                });

        return view;
    }


    // Clears the recyclerview each time the fragment is paused, as each time the fragment opens it is filled with new data
    @Override
    public void onStop()
    {
        super.onStop();

        theModel.dumpList();
        rvGroupList.getAdapter().notifyDataSetChanged();
    }
/*
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        Parcelable rvState = rvGroupList.getLayoutManager().onSaveInstanceState();
        super.onSaveInstanceState(outState);
        //I need to save the grocery list here
        //save recycler view position?
        //outState.putParcelable(RECYCLER_VIEW_KEY, rvState);
        //save recycler view items?
        //outState.putSerializable(RV_DATA, (Serializable) theModel.getGroceryList());
        //getChildFragmentManager().putFragment(outState, "bottom_dialog", bottomSheetDialogFrag);

    }


*/
}