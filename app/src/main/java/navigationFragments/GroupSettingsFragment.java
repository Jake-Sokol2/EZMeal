package navigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ezmeal.Login;
import com.example.ezmeal.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupSettingsFragment newInstance(String param1, String param2) {
        GroupSettingsFragment fragment = new GroupSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_settings, container, false);


        String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        Log.w("TRACK BACKSTACK", "Group Settings opened: " + numOfBackstack);

        View rootView = inflater.inflate(R.layout.fragment_group_settings, container, false);
        Button btnLogout = (Button) rootView.findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                openActivityLogin();
            }
        });

        return rootView;
    }

    public void openActivityLogin(){
        //getParentFragmentManager().popBackStack(getParentFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

        // kill back stack so user cannot return to main screens without logging in again
        getActivity().finish();
    }
}