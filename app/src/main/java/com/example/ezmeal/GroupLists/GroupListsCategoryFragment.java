package com.example.ezmeal.GroupLists;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ezmeal.FindRecipes.CategoryFragment;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListSelectionAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
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
    private GroupListFragHorizontalRecyclerAdapter glFragAdapter = new GroupListFragHorizontalRecyclerAdapter(glCatModel.getGroupList(), glCatModel.getIsSelectedList());
    private List<String> grpListBubbles = new ArrayList<String>();
    private int currentSelectedCategoryPosition = 0;
    private GroupListsViewModel glViewModel;



    String groupName;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //glFragAdapter = new GroupListFragHorizontalRecyclerAdapter(glCatModel.getGroupList(), glCatModel.getIsSelectedList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_new_group_list, container,
                false);
        FragmentManager grpListMgr = getChildFragmentManager();
        Fragment frag = new GroupListsFragment();
        grpListMgr.beginTransaction().replace(R.id.grpListContainerView, frag).commit();

        glViewModel = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

        glViewModel.getGroupList().observe(getViewLifecycleOwner(), groupList ->
        {
           if (groupList != null)
           {
               if(groupList.size() != glViewModel.groupListNames.size())
               {
                   glViewModel.grpListBubbles.clear();
                   glViewModel.groupListNames.clear();
                   glViewModel.isSelectedList.clear();
                   for(int i = 0; i < groupList.size(); i++)
                   {

                       if(i == 0)
                       {
                           glViewModel.addList(groupList.get(i), true);
                       }
                       else
                       {
                           glViewModel.addList(groupList.get(i), false);
                       }

                       //glCatModel.addList(groupList.get(i));
                       glViewModel.grpListBubbles.add(groupList.get(i));
                   }
                   //glViewModel.setSelectList(glCatModel.groupListLength());
               }
           }
           glViewModel.glFragAdapter.notifyDataSetChanged();
           Log.d("glFragAdapter", "adapter se actualizo");
        });

        //TODO: replace with view model
        /*
        grpListBubbles.add("My List");
        grpListBubbles.add("Jake's List");
        grpListBubbles.add("Tristan's List");
        glCatModel.addList(grpListBubbles.get(0), true);
        glCatModel.addList(grpListBubbles.get(1), false);
        glCatModel.addList(grpListBubbles.get(2), false);
        */



        rvGroupListBubbles = (RecyclerView) view.findViewById(R.id.rvGrpHorizontalSelector);

        rvGroupListBubbles.setAdapter(glViewModel.glFragAdapter);
        RecyclerView.LayoutManager hLayoutMgr = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvGroupListBubbles.setLayoutManager(hLayoutMgr);

        glViewModel.glFragAdapter.setOnItemClickListener(new GroupListFragHorizontalRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {

                Fragment fragCategoryClick = new GroupListsFragment();

                // position 0 is "featured" section, which is not stored as a category in the database and would cause a crash if passed in as one
                // only pass a bundle if the user selects a card other than featured
                if (position != 0)
                {
                    //check if it wasn't just deleted from the end
                    if(currentSelectedCategoryPosition < glViewModel.isSelectedList.size()) {
                        glViewModel.setNotSelected(currentSelectedCategoryPosition);
                    }
                    glViewModel.setSelected(position);
                    glViewModel.updateSelList(glViewModel.isSelectedList);
                    glViewModel.updateSelectList().setValue(glViewModel.isSelectedList);
                    currentSelectedCategoryPosition = position;
                    glViewModel.glFragAdapter.notifyDataSetChanged();

                    Bundle categoryBundleClick = new Bundle();
                    categoryBundleClick.putString("cat", glViewModel.grpListBubbles.get(position));
                    fragCategoryClick.setArguments(categoryBundleClick);
                }
                else
                // featured was clicked, set last category to unclicked (visually) and set featured to clicked
                {
                    glViewModel.setNotSelected(currentSelectedCategoryPosition);
                    glViewModel.setSelected(0);
                    currentSelectedCategoryPosition = 0;
                    glViewModel.glFragAdapter.notifyDataSetChanged();
                }

                getChildFragmentManager().popBackStack();
                getChildFragmentManager().beginTransaction().replace(R.id.grpListContainerView, fragCategoryClick).commit();
            }

            @Override
            public void onItemLongClick(int position)
            {
                Toast.makeText(getContext(), "Long press", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to delete " + glViewModel.groupListNames.get(position) + "?");
                LinearLayout linearLayout = new LinearLayout(getContext());

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
                {
                   @Override
                   public void onClick(DialogInterface dialog, int pos)
                   {
                       dialog.dismiss();
                   }

                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int pos)
                   {
                       Fragment fragCategoryClick = new GroupListsFragment();

                       String deletedName = glViewModel.groupListNames.get(position);
                       glViewModel.deleteGroupList(position);
                       glViewModel.glFragAdapter.notifyDataSetChanged();

                       //getChildFragmentManager().popBackStack();
                       //getChildFragmentManager().beginTransaction().replace(R.id.grpListContainerView, fragCategoryClick).commit();

                       Toast.makeText(getContext(), deletedName + " deleted successfully", Toast.LENGTH_SHORT).show();
                   }
                });


                builder.create().show();
            }

        });




        return view;


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        glViewModel.dumpGroupList();
        glViewModel.glFragAdapter.notifyDataSetChanged();

    }


}
