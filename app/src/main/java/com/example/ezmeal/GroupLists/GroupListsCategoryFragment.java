package com.example.ezmeal.GroupLists;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ezmeal.FindRecipes.CategoryFragment;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListSelectionAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Group Options");
                LinearLayout linearLayout = new LinearLayout(getContext());
                String selectedName = glViewModel.getActiveGrpListName();

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                {
                   @Override
                   public void onClick(DialogInterface dialog, int pos)
                   {
                       dialog.dismiss();
                   }

                });

                builder.setNegativeButton("Delete Group", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int pos)
                   {
                       Fragment fragCategoryClick = new GroupListsFragment();

                       String deletedName = glViewModel.groupListNames.get(position);
                       glViewModel.deleteGroupList(position);
                       glViewModel.glFragAdapter.notifyDataSetChanged();


                       Toast.makeText(getContext(), deletedName + " deleted successfully", Toast.LENGTH_SHORT).show();
                   }
                });

                builder.setPositiveButton("Add User", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int pos)
                   {
                       AlertDialog.Builder newBuilder = new AlertDialog.Builder(getContext());
                       newBuilder.setTitle("Enter new user's email address: ");
                       final EditText emailField = new EditText(getContext());
                       LinearLayout linearLayout = new LinearLayout(getContext());

                       emailField.setHint("Email");
                       emailField.setMinEms(16);
                       emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                       linearLayout.addView(emailField);
                       linearLayout.setPadding(10, 20, 10, 20);
                       newBuilder.setView(linearLayout);

                       newBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int pos) { dialog.dismiss(); }
                       });

                       newBuilder.setPositiveButton("Add User", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               //Toast.makeText(getContext(), "Add a user", Toast.LENGTH_SHORT).show();
                               String email = emailField.getText().toString().toLowerCase(Locale.ROOT).trim();

                               //Add user to array field from FireBase
                               FirebaseFirestore db = FirebaseFirestore.getInstance();
                               FirebaseAuth mAuth = FirebaseAuth.getInstance();
                               String currentUser = mAuth.getCurrentUser().getEmail();

                               db.collection("Groups").get()
                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if(task.isSuccessful())
                                               {
                                                   for(QueryDocumentSnapshot doc : task.getResult())
                                                   {
                                                       String listName = doc.getString("ListName");
                                                       if(Objects.equals(listName, selectedName))
                                                       {
                                                           //add the new email to the list of users
                                                           db.collection("Groups").document(doc.getId()).update("SharedUsers", FieldValue.arrayUnion(email));
                                                           Toast.makeText(getContext(), "User added", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               }
                                           }
                                       });

                           }
                       });

                       newBuilder.create().show();
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
