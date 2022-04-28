package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.List;

public class AddButtonFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    private GroupListsFragmentRecyclerAdapter adapter;
    public GroupListsFragmentModel theModel;
    private FragmentManager fm;
    private FragmentTransaction ft;
    public GroupListFragHorizontalRecyclerAdapter hAdapter;
    public RecyclerView rvGroupList;

    public AddButtonFragment() {

    }

    public AddButtonFragment(GroupListsFragmentModel theModel, GroupListsFragmentRecyclerAdapter adapter, GroupListFragHorizontalRecyclerAdapter hAdapter) {
        this.theModel = theModel;
        this.adapter = adapter;
        this.hAdapter = hAdapter;
    }



    public static AddButtonFragment newInstance(String param1, String param2) {
        AddButtonFragment frag = new AddButtonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        frag.setArguments(args);

        return frag;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
        Bundle args = this.getArguments();
        if(args != null)
        {
            theModel.setGroupList((List<String>) args.getSerializable("rvData"));
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_button, container, false);
        Button createNewListBtn = (Button) view.findViewById(R.id.createNewList);
        createNewListBtn.setOnClickListener(this);
        Button addItemBtn = (Button) view.findViewById(R.id.addNewItem);
        addItemBtn.setOnClickListener(this);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.cancelBtn)
        {
            dismiss();
        }
        else if(view.getId() == R.id.addNewItem)
        {
            //launch the AddListItemFrag
            fm = getParentFragmentManager();
            ft = fm.beginTransaction();
            AddListItemFragment addItemFrag = new AddListItemFragment(theModel, adapter);

            ft.add(addItemFrag, "newTAG");
            getParentFragmentManager().popBackStack("TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.show(addItemFrag);
            ft.commit();
        }
        else if(view.getId() == R.id.createNewList)
        {
            //launch the CreateNewList
            fm = getParentFragmentManager();
            ft = fm.beginTransaction();
            AddGroupListFragment addGListFrag = new AddGroupListFragment();

            //Bundle blunt = new Bundle();
            //blunt.putSerializable("GroupList", (Serializable) theModel.getGroupList());
            //addGListFrag.setArguments(blunt);


            ft.add(addGListFrag, "gListFrag");
            getParentFragmentManager().popBackStack("TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.show(addGListFrag);
            ft.commit();

        }
    }
}