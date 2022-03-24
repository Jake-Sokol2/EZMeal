package navigationFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.MainRecyclerAdapter;
import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.example.ezmeal.ViewModel.AddListItemFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Implementing View.OnClickListener here so that I could make one function for
//handling the buttons instead of rewriting the same onClick function.
public class AddListItemFragment extends BottomSheetDialogFragment
        implements View.OnClickListener{
    AddListItemFragmentViewModel theViewModel;

    private static final String ITEM_NAME = "item_name";
    private static final String BRAND_NAME = "brand_name";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1, mParam2;
    private GroceryListModel theModel;
    private List<GroceryListModel> toSave = new ArrayList<GroceryListModel>();
    private MainRecyclerAdapter adapter;
    private EditText editItemName, editBrandName;

    public AddListItemFragment() {

    }

    public AddListItemFragment(GroceryListModel theModel, MainRecyclerAdapter adapter) {
        this.theModel = theModel;
        this.adapter = adapter;

    }

    // TODO: Rename and change types and numbers of params
    public static AddListItemFragment newInstance(String param1, String param2){
        AddListItemFragment fragment = new AddListItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: save the adapter in onSaveInstanceState
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mParam1 = savedInstanceState.getString(ITEM_NAME);
            mParam2 = savedInstanceState.getString(BRAND_NAME);
            toSave = (List) savedInstanceState.getSerializable("model");
            theModel = toSave.get(0);
        }
        else {
            if(getArguments() != null){
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_list_item, container, false);
        theViewModel = new ViewModelProvider(requireActivity()).get(AddListItemFragmentViewModel.class);

        editItemName = view.findViewById(R.id.editItemName);
        editBrandName = view.findViewById(R.id.editBrandName);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        Button btnAddItem = (Button) view.findViewById(R.id.btnConfirm);
        btnAddItem.setOnClickListener(this);

        return view;
    }

    //Handles button interactions
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnConfirm:
                if(TextUtils.isEmpty(editItemName.getText().toString()) ) {
                    editItemName.setError("Enter item name...");
                }
                if(TextUtils.isEmpty(editBrandName.getText().toString())) {
                    editBrandName.setError("Enter brand name...");
                }

                theModel.addDataToFirestore(editItemName.getText().toString(), editBrandName.getText().toString());

                /*
                theModel.addItem(editItemName.getText().toString(),
                    editBrandName.getText().toString());
                */

                adapter.notifyDataSetChanged();
                dismiss();
                break;


            case R.id.btnCancel:
                dismiss();

            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        toSave.add(theModel);
        outState.putString(ITEM_NAME, mParam1);
        outState.putString(BRAND_NAME, mParam2);
        outState.putSerializable("model", (Serializable) toSave);
        super.onSaveInstanceState(outState);
    }
}
