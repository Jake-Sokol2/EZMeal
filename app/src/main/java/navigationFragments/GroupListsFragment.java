package navigationFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ezmeal.MainRecyclerAdapter;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    private List<List<String>> groceryList = new ArrayList<List<String>>();
    List<String> list = new ArrayList<String>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
            groceryList = (List) savedInstanceState.getSerializable("grocery");
        }else {
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }
        list = new ArrayList<String>();
        list.add("Gallon of Milk");
        list.add("milk brand");
        groceryList.add(list);

        list = new ArrayList<String>();
        list.add("Fruit");
        list.add("fruit brand");
        groceryList.add(list);

        list = new ArrayList<String>();
        list.add("Eggs");
        list.add("egg brand");
        groceryList.add(list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_group_lists, container, false);
        View view = inflater.inflate(R.layout.fragment_group_lists, container, false);

        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupLists);
        adapter = new MainRecyclerAdapter(groceryList);
        rvGroupList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);

        // Add some data
        // todo: remove this when user's list saves on application close
        adapter.notifyDataSetChanged();

        //clickedView = (View) view.findViewById(R.id.editListItem);

        adapter.setOnItemClickListener(new MainRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {
                //String selectedName = groceryList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                //Toast.makeText(getActivity(), clickedView., Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…

            }


        });

        Button btnAddListItem = (Button) view.findViewById(R.id.btnAddItem);
        btnAddListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);//com.google.android.material.R.style.Theme_Design_BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_list_item, (LinearLayout) view.findViewById(R.id.bottomSheetAddList));
                //BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
                //FragmentManager foo = getActivity().getSupportFragmentManager();
                //bottomSheet.show();
                //openActivityAddListItem();

                //bottomSheetDialog.setContentView(R.layout.fragment_add_list_item);

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                // onClickListeners for the bottom sheet's views
                Button btnConfirm = (Button) bottomSheetDialog.findViewById(R.id.btnConfirm);
                EditText editItemName = (EditText) bottomSheetDialog.findViewById(R.id.editItemName);
                EditText editBrandName = (EditText) bottomSheetDialog.findViewById(R.id.editBrandName);
                //TextView txtBrandName = (TextView)

                btnConfirm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        list = new ArrayList<String>();
                        list.add(editItemName.getText().toString());
                        list.add(editBrandName.getText().toString());
                        groceryList.add(list);
                        adapter.notifyDataSetChanged();

                        // Closes the bottom sheet after the User enters an item
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    // Clears the recyclerview each time the fragment is paused, as each time the fragment opens it is filled with new data
    @Override
    public void onPause()
    {
        super.onPause();

        groceryList.clear();
        rvGroupList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        //I need to save the grocery list here
        outState.putSerializable("grocery", (Serializable) groceryList);
    }



}