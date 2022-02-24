package navigationFragments.addItemView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import navigationFragments.addItemViewModel.AddListItemFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddListItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddListItemFragment extends BottomSheetDialogFragment {

    AddListItemFragmentViewModel vmAddListItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddListItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddListItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddListItemFragment newInstance(String param1, String param2) {
        AddListItemFragment fragment = new AddListItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_list_item, container, false);
        vmAddListItem = new ViewModelProvider(requireActivity()).get(AddListItemFragmentViewModel.class);

        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editItemName = view.findViewById(R.id.editItemName);
                vmAddListItem.setData(editItemName.getText().toString());
                Toast.makeText(getContext(), "clicked button", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}