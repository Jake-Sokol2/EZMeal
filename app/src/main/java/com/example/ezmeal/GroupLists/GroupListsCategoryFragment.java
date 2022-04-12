package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ezmeal.GroupLists.Adapter.GroupListSelectionAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.R;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupListsCategoryFragment extends Fragment
{
    private FirebaseFirestore db;
    private GroupListsFragmentModel glCatModel;
    private RecyclerView rvGroupLists;
    private GroupListSelectionAdapter glFragAdapter;

    String groupName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group_list_category, container,
                false);

        rvGroupLists = (RecyclerView) view.findViewById(R.id.rvGroupList);
        glFragAdapter = new GroupListSelectionAdapter(glCatModel.getGroupList());

        return view;
    }
}
