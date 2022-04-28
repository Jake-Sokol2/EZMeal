package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddGroupListFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GroupListsFragmentModel theModel = new GroupListsFragmentModel();
    private GroupListFragHorizontalRecyclerAdapter hAdapter;
    private GroupListsViewModel theViewModel;
    private static List<GroupListsFragmentModel> toSave = new ArrayList<GroupListsFragmentModel>();
    private static List<GroupListFragHorizontalRecyclerAdapter> saveAdapter = new ArrayList<>();

    private EditText editListName;
    private View view;



    public AddGroupListFragment()
    {

    }

    public AddGroupListFragment(GroupListsFragmentModel theModel, GroupListFragHorizontalRecyclerAdapter hAdapter)
    {
        //this.theModel = theModel;
        this.hAdapter = hAdapter;
        //boolean isItNull = this.hAdapter == null;
        Log.d("AddGroupListFragment", "hAdapter is theoretically not null?");
        Log.d("AddGroupListFragment", String.valueOf(hAdapter));

    }

    public static AddGroupListFragment newInstance(GroupListsFragmentModel theModel, GroupListFragHorizontalRecyclerAdapter hAdapter)
    {
        AddGroupListFragment myFrag = new AddGroupListFragment();
        Bundle args = new Bundle();
        toSave.add(theModel);
        saveAdapter.add(hAdapter);

        args.putSerializable("model", (Serializable) toSave);
        args.putSerializable("hAdapter", (Serializable) saveAdapter);
        myFrag.setArguments(args);
        return myFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle blunt = getArguments();

        if(blunt != null)
        {
            theModel.setGroupList((List<String>) blunt.getSerializable("GroupList"));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_add_group_list, container, false);
        theViewModel = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

        //Observer
        theViewModel.updateGroupList().observe(getViewLifecycleOwner(), groupList ->
        {
            if(groupList.size() > 0)
            {
                for(int i = 0; i < groupList.size(); i++)
                {
                    if(i==0)
                    {
                        theModel.addList(groupList.get(i), true);
                    }
                    else
                    {
                        theModel.addList(groupList.get(i), false);
                    }
                }

            }
        });



        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Button createListBtn = (Button) view.findViewById(R.id.btnConfirmList);
        createListBtn.setOnClickListener(this);

        Button cancelBtn = (Button) view.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(this);

        editListName = view.findViewById(R.id.editListName);
    }



    //Handles button interactions
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnConfirmList:
                if(TextUtils.isEmpty(editListName.getText().toString()))
                    editListName.setError("Empty field");
                else
                {

                    theModel.addList(editListName.getText().toString(), false);
                    //TODO data needs to be added to firestore

                    dismiss();
                }
                break;

            case R.id.btnCancel:
                dismiss();
                break;

            default:
                break;
        }
    }

}
