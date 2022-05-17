package com.example.ezmeal.GroupLists;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.AddListItemFragmentViewModel;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Implementing View.OnClickListener here so that I could make one function for
//handling the buttons instead of rewriting the same onClick function.
public class AddListItemFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{


    private static final String ITEM_NAME = "item_name";
    private static final String BRAND_NAME = "brand_name";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1, mParam2;
    public GroupListsFragmentModel theModel;
    private List<GroupListsFragmentModel> toSave = new ArrayList<GroupListsFragmentModel>();
    public GroupListsFragmentRecyclerAdapter adapter;
    private GroupListsViewModel theVM;
    private EditText editItemName, editBrandName;

    public AddListItemFragment() {

    }

    public AddListItemFragment(GroupListsFragmentModel theModel, GroupListsFragmentRecyclerAdapter adapter) {
        this.theModel = theModel;
        this.adapter = adapter;

    }

    // TODO: Rename and change types and numbers of params
    public static AddListItemFragment newInstance(String param1, String param2){
        AddListItemFragment fragment = new AddListItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: save the adapter in onSaveInstanceState
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mParam1 = savedInstanceState.getString(ITEM_NAME);
            mParam2 = savedInstanceState.getString(BRAND_NAME);
            toSave = (List) savedInstanceState.getSerializable("model");
            theModel = toSave.get(0);
        }
        else {
            if(getArguments() != null){
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_list_item, container, false);
        /*
        theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

        //Update the local model with the live shopping list
        theVM.updateShoppingList("").observe(getViewLifecycleOwner(), shoppingList ->
        {
            if(shoppingList != null) {
                for (int i = 0; i < shoppingList.size(); i++) {
                    theModel.addItem(shoppingList.get(i));
                }

            }
        });

        //Get current group lists
        theVM.getGroupList().observe(getViewLifecycleOwner(), groupList ->
        {
            if(groupList != null) {
                if (groupList.size() > 0) {
                    for (int i = 0; i < groupList.size(); i++) {
                        if (i == 0) {
                            theModel.addList(groupList.get(i), true);
                        } else {
                            theModel.addList(groupList.get(i), false);
                        }
                    }

                }
            }
        });
        */

        editItemName = view.findViewById(R.id.rbRateRecipeDialog);
        editBrandName = view.findViewById(R.id.editBrandName);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancelRating);
        btnCancel.setOnClickListener(this);
        Button btnAddItem = (Button) view.findViewById(R.id.btnConfirmRating);
        btnAddItem.setOnClickListener(this);

        return view;
    }

    //Handles button interactions
    @Override
    public void onClick(View view){
        theVM = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);
        switch (view.getId()){
            case R.id.btnConfirmRating:
                if(TextUtils.isEmpty(editItemName.getText().toString()) ) {
                    //Toast.makeText(view.getContext(), "Enter item name...", Toast.LENGTH_SHORT).show();;
                    editItemName.setError("Enter field");
                }
                /*if(TextUtils.isEmpty(editBrandName.getText().toString())) {
                    editBrandName.setError("Empty field");
                }*/
                else {
                    theModel.addItem(editItemName.getText().toString(), editBrandName.getText().toString());
                    theModel.addDataToFirestore(editItemName.getText().toString(), editBrandName.getText().toString());

                    adapter.notifyDataSetChanged();

                    dismiss();
                }
                break;

            case R.id.btnCancelRating:
                dismiss();
                break;

            default:
                break;
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        toSave.add(theModel);
        outState.putString(ITEM_NAME, mParam1);
        outState.putString(BRAND_NAME, mParam2);
        outState.putSerializable("model", (Serializable) toSave);
        super.onSaveInstanceState(outState);
    }


}
