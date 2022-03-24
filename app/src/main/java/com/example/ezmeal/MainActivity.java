package com.example.ezmeal;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import navigationFragments.GroupListsFragment;
import navigationFragments.GroupRecipesFragment;
import navigationFragments.GroupSettingsFragment;
import navigationFragments.MyRecipesFragment;
import navigationFragments.MyRecipesSpecificCategoryFragment;

public class MainActivity extends AppCompatActivity
{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String numOfBackstack = String.valueOf(getSupportFragmentManager().getBackStackEntryCount());
        Log.w("TRACK BACKSTACK", "Main Activity created: " + numOfBackstack);
        // bottom navigation bar code
        //loadFragment(new GroupListsFragment());
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        //NavController navController = navHostFragment.getNavController();
        //AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(R.id.groupListsFragment, R.id.groupRecipesFragment, R.id.myRecipesFragment, R.id.groupSettingsFragment).build();

        // determines which fragment will open depending on which bottom navigation tab is selected

        //NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Fragment frag = null;
                // tags keep track of the fragments on the backstack so they can be manually popped later
                String backStackTag = null;

                if (item.getItemId() == R.id.groupListsFragment)
                {
                    // default fragment is always on backstack (nav component rules), so just pop the entire stack after the default fragment
                    // whenever user returns to default fragment
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
                else if (item.getItemId() == R.id.groupRecipesFragment)
                {
                    frag = new GroupRecipesFragment();
                    backStackTag = "groupRecipesFragment";
                }
                else if (item.getItemId() == R.id.myRecipesFragment)
                {
                    frag = new MyRecipesFragment();
                    backStackTag = "myRecipesFragment";
                }
                else if (item.getItemId() == R.id.groupSettingsFragment)
                {
                    frag = new GroupSettingsFragment();
                    backStackTag = "groupSettingsFragment";
                }

                return loadFragment(frag, backStackTag);

            }
        });

        // handles the backstack / back button for fragContainer which holds bottom navigation's screens
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                //getSupportFragmentManager().popBackStack("myRecipesFragment", 1);

                Log.w("TRACK BACKSTACK 2.0", "------------------------");
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < backStackCount; i++)
                {
                    Log.w("TRACK BACKSTACK 2.0", "back stack tags     : " + getSupportFragmentManager().getBackStackEntryAt(i).getName());
                }

                String temp = String.valueOf(getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1));
                Log.w("TRACK BACKSTACK 2.0 LALALALALALALLALALALALALLALA", temp);
                Log.w("TRACK BACKSTACK 2.0", "------------------------");

                // if user is on default screen of nav bar (group lists) exit and return to login screen
                if (bottomNav.getSelectedItemId() == R.id.groupListsFragment)
                {
                    finish();
                }
                else if (Objects.equals(getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName(), "specific_category") || (Objects.equals(getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName(), "specific_recipe")))
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                else
                {
                    //bottomNav.setSelectedItemId(R.id.groupListsFragment);
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    String numOfBackstack = String.valueOf(getSupportFragmentManager().getBackStackEntryCount());
                    Log.w("TRACK BACKSTACK", "back pressed on screen other than default: " + numOfBackstack);

                    // updates actual nav bar view itself to highlight the correct fragment
                    bottomNav.setSelectedItemId(R.id.groupListsFragment);
                }

            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        //NavigationUI.setupWithNavController(bottomNav, navController);
    }

    public boolean loadFragment(Fragment frag, String backStackTag)
    {
        if (frag != null)
        {
            // pop back stack every time we load a new fragment so stack doesn't fill with duplicate fragments
            getSupportFragmentManager().popBackStack();


            // attempts to pop back to fragment with tag backStackTag on the backstack.  If fragment doesnt exist on stack, returns false
            // boolean popFrag = getSupportFragmentManager().popBackStackImmediate(backStackTag, 0);

            // if false was returned, fragment didn't exist on stack so create it
            //if (!popFrag)
            //{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragContainer, frag)
                        .addToBackStack(backStackTag)
                        .commit();
            //}

        }



        //FragmentManager fm = getSupportFragmentManager();

        //Log.w("BACKSTACK", "----------");
        //for(int entry = 0; entry<fm.getBackStackEntryCount(); entry++){
        //    Log.w("BACKSTACK", "Found fragment at position: " + entry);
        //}
        //Log.w("BACKSTACK", "----------");
        String numOfBackstack = String.valueOf(getSupportFragmentManager().getBackStackEntryCount());
        Log.w("TRACK BACKSTACK", "popped and fragment replaced: " + numOfBackstack);

        return true;
    }





}