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
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Implementing View.OnClickListener here so that I could make one function for
//handling the buttons instead of rewriting the same onClick function.
public class AddListItemFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{
    AddListItemFragmentViewModel theViewModel;

    private static final String ITEM_NAME = "item_name";
    private static final String BRAND_NAME = "brand_name";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1, mParam2;
    private GroupListsFragmentModel theModel;
    private List<GroupListsFragmentModel> toSave = new ArrayList<GroupListsFragmentModel>();
    private GroupListsFragmentRecyclerAdapter adapter;
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
        theViewModel = new ViewModelProvider(requireActivity()).get(AddListItemFragmentViewModel.class);

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
        switch (view.getId()){
            case R.id.btnConfirmRating:
                if(TextUtils.isEmpty(editItemName.getText().toString()) ) {
                    //Toast.makeText(view.getContext(), "Enter item name...", Toast.LENGTH_SHORT).show();;
                    editItemName.setError("Enter field");
                }
                if(TextUtils.isEmpty(editBrandName.getText().toString())) {
                    editBrandName.setError("Empty field");
                }
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
    public void onDismiss(final DialogInterface dialog)
    {
        super.onDismiss(dialog);
        /*
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
        */
        FragmentManager fm = getParentFragmentManager();

        adapter.notifyDataSetChanged();
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
