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
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Adapter.GroupListFragHorizontalRecyclerAdapter;
import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.GroupListsViewModel;
import com.example.ezmeal.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //Variable to hold the group name
    private String groupNameString;

    private String currentUserEmail;

    //Firebase and Firestore variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String documentID1;



    public AddGroupListFragment()
    {

    }

    public AddGroupListFragment(GroupListsFragmentModel theModel, GroupListFragHorizontalRecyclerAdapter hAdapter)
    {
        this.theModel = theModel;
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


        mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        db = FirebaseFirestore.getInstance();


        //Get the email address of the currently logged in user
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //currentUserEmail = currentUser.getEmail();
        //Toast.makeText(getContext(), "Username of current user is: " + currentUserEmail, Toast.LENGTH_SHORT).show();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_add_group_list, container, false);
        theViewModel = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);
        /*
        //Observer
        theViewModel.getGroupList().observe(getViewLifecycleOwner(), groupList ->
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
        */


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
        theViewModel = new ViewModelProvider(requireActivity()).get(GroupListsViewModel.class);

        switch (view.getId()){
            case R.id.btnConfirmList:
                if(TextUtils.isEmpty(editListName.getText().toString()))
                    editListName.setError("Empty field");
                else
                {

                    theModel.addList(editListName.getText().toString(), false);
                    theModel.grpListBubbles.add(editListName.getText().toString());
                    hAdapter.notifyDataSetChanged();
                    //theViewModel.

                    //TODO data needs to be added to firestore
                    //Get the name of the group
                    groupNameString = editListName.getText().toString();

                    //Get the email address of the currently logged in user
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    currentUserEmail = currentUser.getEmail();

                    //Create an array of all the users associated with the group
                    //ArrayList<String> users = new ArrayList<String>();
                    //users.add(currentUserEmail);

                    String users[] = new String[]{currentUserEmail};

                    Map<String, Object> docData1 = new HashMap<>();
                    docData1.put("Creator", currentUserEmail);
                    docData1.put("ListName", groupNameString);
                    docData1.put("SharedUsers", Arrays.asList(users));

                    db.collection("Groups").add(docData1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference){
                            documentID1 = documentReference.getId();
                            /*
                            Map<String, Object> docData2 = new HashMap<>();
                            docData2.put("Creator", currentUserEmail);
                            docData2.put("ItemBrand", "");
                            docData2.put("ItemName", "Welcome to group " + groupNameString);
                            docData2.put("ItemQuantity", 0);
                            docData2.put("ItemChecked", false);

                            db.collection("Groups").document(documentID1).collection("Items").add(docData2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Debug code
                                    //Toast.makeText(getApplicationContext(), documentID2, Toast.LENGTH_SHORT).show();


                                }
                            });

                             */
                            //Debug code
                            //Log.i("DEBUG", "Printing document ID in the onSuccess: " + documentID1);
                            //Toast.makeText(getActivity(), "Creation of group " + groupNameString + " success!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //theViewModel.updateGroupList()
                    //theViewModel.glFragAdapter.notifyDataSetChanged();
                    hAdapter.notifyDataSetChanged();
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
