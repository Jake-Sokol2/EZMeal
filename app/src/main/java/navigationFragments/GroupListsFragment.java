package navigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ezmeal.AddListItemActivity;
import com.example.ezmeal.MainRecyclerAdapter;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListsFragment extends Fragment
{
    private List<String> testList = new ArrayList<>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;
    public View clickedView;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }









    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_group_lists, container, false);
        View view = inflater.inflate(R.layout.fragment_group_lists, container, false);
        rvGroupList = (RecyclerView) view.findViewById(R.id.rvGroupLists);
        adapter = new MainRecyclerAdapter(testList);
        rvGroupList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGroupList.setLayoutManager(layoutManager);

        // Add some data
        testList.add("Gallon of Milk-----------------------------------------");
        testList.add("Fruit");
        testList.add("Eggs");
        adapter.notifyDataSetChanged();

        //clickedView = (View) view.findViewById(R.id.editListItem);

        adapter.setOnItemClickListener(new MainRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {
                String selectedName = testList.get(position);
                //clickedView = (View) layoutManager.findViewByPosition(position);
                if (layoutManager.findViewByPosition(position).getTag() == "editListItem")
                {
                    clickedView = (EditText) layoutManager.findViewByPosition(position);
                    Toast.makeText(getActivity(), "clicked on edit text", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "didnt click on edit text", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(), clickedView., Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…

            }
        });



        Button btnAddListItem = (Button) view.findViewById(R.id.btnAddItem);
        btnAddListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), com.google.android.material.R.style.Theme_Design_BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_list_item, (LinearLayout) view.findViewById(R.id.bottomSheetAddList));
                //BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext());
                //FragmentManager foo = getActivity().getSupportFragmentManager();
                //bottomSheet.show();
                //openActivityAddListItem();

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }

        });

        return view;
    }


    // Clears the recyclerview each time the fragment is paused, as each time the fragment opens it is filled with new data
    @Override
    public void onPause()
    {
        super.onPause();

        testList.clear();
        rvGroupList.getAdapter().notifyDataSetChanged();
    }

    // todo: turn this into MVVM
    public void openActivityAddListItem()
    {
        Intent intent = new Intent(getActivity(), AddListItemActivity.class);
        startActivity(intent);
    }
}