package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListSelectionAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.R;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//These are the little bubbles at the top
public class GroupListsCategoryFragment extends Fragment
{
    private FirebaseFirestore db;
    private GroupListsFragmentModel glCatModel = new GroupListsFragmentModel();
    private RecyclerView rvGroupListBubbles;
    private GroupListFragHorizontalRecyclerAdapter glFragAdapter;
    private List<String> grpListBubbles = new ArrayList<String>();



    String groupName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_new_group_list, container,
                false);

        grpListBubbles.add("My List");
        grpListBubbles.add("Jake's List");
        grpListBubbles.add("Tristan's List");
        glCatModel.addList(grpListBubbles.get(0), true);
        glCatModel.addList(grpListBubbles.get(1), false);
        glCatModel.addList(grpListBubbles.get(2), false);

        FragmentManager grpListMgr = getChildFragmentManager();
        Fragment frag = new GroupListsFragment();
        grpListMgr.beginTransaction().replace(R.id.grpListContainerView, frag).commit();

        rvGroupListBubbles = (RecyclerView) view.findViewById(R.id.rvGrpHorizontalSelector);
        glFragAdapter = new GroupListFragHorizontalRecyclerAdapter(glCatModel.getGroupList(), glCatModel.getIsSelectedList());
        rvGroupListBubbles.setAdapter(glFragAdapter);
        RecyclerView.LayoutManager hLayoutMgr = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvGroupListBubbles.setLayoutManager(hLayoutMgr);


        //rvGroupLists = (RecyclerView) view.findViewById(R.id.rvGroupList);
        //glFragAdapter = new GroupListSelectionAdapter(glCatModel.getGroupList());
        //rvGroupLists.setAdapter(glFragAdapter);
        //RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        return view;
    }
}
