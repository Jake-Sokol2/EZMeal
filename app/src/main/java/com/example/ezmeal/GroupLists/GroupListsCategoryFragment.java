package com.example.ezmeal.GroupLists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ezmeal.FindRecipes.CategoryFragment;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
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
    private int currentSelectedCategoryPosition = 0;



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

        glFragAdapter.setOnItemClickListener(new GroupListFragHorizontalRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {

                Fragment fragCategoryClick = new GroupListsFragment();

                // position 0 is "featured" section, which is not stored as a category in the database and would cause a crash if passed in as one
                // only pass a bundle if the user selects a card other than featured
                if (position != 0)
                {
                    glCatModel.setNotSelected(currentSelectedCategoryPosition);
                    glCatModel.setSelected(position);
                    currentSelectedCategoryPosition = position;
                    glFragAdapter.notifyDataSetChanged();

                    Bundle categoryBundleClick = new Bundle();
                    categoryBundleClick.putString("cat", grpListBubbles.get(position));
                    fragCategoryClick.setArguments(categoryBundleClick);
                }
                else
                // featured was clicked, set last category to unclicked (visually) and set featured to clicked
                {
                    glCatModel.setNotSelected(currentSelectedCategoryPosition);
                    glCatModel.setSelected(0);
                    currentSelectedCategoryPosition = 0;
                    glFragAdapter.notifyDataSetChanged();
                }

                getChildFragmentManager().popBackStack();
                getChildFragmentManager().beginTransaction().replace(R.id.grpListContainerView, fragCategoryClick).commit();
            }
        });

        return view;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        glCatModel.dumpGroupList();
        rvGroupListBubbles.getAdapter().notifyDataSetChanged();
    }
}
