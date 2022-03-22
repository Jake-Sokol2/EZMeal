package navigationFragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ezmeal.Model.GroceryListModel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezmeal.MainRecyclerAdapter;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.checkbox.MaterialCheckBox;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListsFragment extends Fragment
{
    private ArrayList<List<String>> groceryList = new ArrayList<List<String>>();
    private GroceryListModel theModel = new GroceryListModel();
    //BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
    //BottomSheetDialogFragment bottomSheetDialogFrag = new BottomSheetDialogFragment();

    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupListsFragment newInstance(String param1, String param2) {
        GroupListsFragment fragment = new GroupListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            theModel.restoreGroceryList((List<List<String>>)savedInstanceState.getSerializable(RV_DATA));
        }else {
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        /*
        No longer needed because adding items works and the items
        are not nuked on rotation.

        theModel.addItem("Milk", "milk brand");
        theModel.addItem("Fruit", "fruit brand");
        theModel.addItem("Huevo", "Huevo del super");
        */

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group_lists, container, false);

        String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        Log.w("TRACK BACKSTACK", "Group Lists opened: " + numOfBackstack);

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupLists);
        adapter = new MainRecyclerAdapter(theModel.getGroceryList());
        rvGroupList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);




        // Add some data
        // todo: remove this when user's list saves on application close
        //adapter.notifyDataSetChanged();

        //clickedView = (View) view.findViewById(R.id.editListItem);
        // edit list item feature should start here
        adapter.setOnItemClickListener(new MainRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, TextView text)
            {
                //String selectedName = groceryList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                Toast.makeText(getContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…
                //long tv = adapter.getItemId(position);

               // RecyclerView.ViewHolder vh = adapter.getView;
                //View v = vh.itemView;
            }
        });




        Button btnAddListItem = (Button) view.findViewById(R.id.btnAddItem);
        btnAddListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_list_item, (LinearLayout) view.findViewById(R.id.bottomSheetAddList));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                // the bottom sheet views
                Button btnConfirm = (Button) bottomSheetDialog.findViewById(R.id.btnConfirm);
                EditText editItemName = (EditText) bottomSheetDialog.findViewById(R.id.editItemName);
                EditText editBrandName = (EditText) bottomSheetDialog.findViewById(R.id.editBrandName);

                btnConfirm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        theModel.addItem(editItemName.getText().toString(), editBrandName.getText().toString());
                        adapter.notifyDataSetChanged();

                        // Closes the bottom sheet after the User enters an item
                        bottomSheetDialog.dismiss();
                    }//end additembutton in add item BottomSheetDialog
                });//OnclickListener

                /*
                    TODO: This block creates new frag, but layout is fucky.
                     Implement cancel button as well so you can get back to previous screen.
                */
/*
                AddListItemFragment AddItemFrag = new AddListItemFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.group_lists, AddItemFrag);
                transaction.commit();
*/

            }//Add item onClick
        });

        return view;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        Parcelable rvState = rvGroupList.getLayoutManager().onSaveInstanceState();
        super.onSaveInstanceState(outState);
        //I need to save the grocery list here
        //save recycler view position?
        outState.putParcelable(RECYCLER_VIEW_KEY, rvState);
        //save recycler view items?
        outState.putSerializable(RV_DATA, (Serializable) theModel.getGroceryList());
        //getChildFragmentManager().putFragment(outState, "bottom_dialog", bottomSheetDialogFrag);


    }



}