package com.example.ezmeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.example.ezmeal.GroupLists.GroupListsFragment;


/**
 * The main activity for EZMeal.  Contains a FragmentContainerView which holds the GroupLists, FindRecipes, MyRecipes, and GroupSettings fragments
 *
 * layout: {@link R.layout#activity_main}
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String numOfBackstack = String.valueOf(getSupportFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Main Activity created: " + numOfBackstack);
        // bottom navigation bar code
        //loadFragment(new GroupListsFragment());
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragContainer);

        // Old code, do not remove - uncomment these to return to default nav component backstack
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(R.id.groupListsFragment, R.id.groupRecipesFragment, R.id.myRecipesFragment, R.id.groupSettingsFragment).build();
        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                NavigationUI.onNavDestinationSelected(item, navController);
                return true;
            }
        });


       /* bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationUI.onNavDestinationSelected(item,navController);
                navController.popBackStack(item.getItemId(),false);
                return true;
            }
        });*/

        // for resetting selected tab backstack? seperate from my backstack code
/*        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.groupRecipesFragment)
                {
                    bottomNav.setSelectedItemId(R.id.groupRecipesFragment);
                }
                return false;
            }});*/

        // click listener for the bottom navigation bar
        /*
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Fragment frag = null;
                // tags keep track of the fragments on the backstack so we can manually pop them later
                String backStackTag = null;

                if (item.getItemId() == R.id.groupListsFragment)
                {
                    // default fragment isn't even listed on the back stack (nav component rules), so just pop the entire stack from 0 to return to default
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                // if user is on default screen of nav bar (group lists) exit and return to home screen follow Androids principles of navigation
                // login screen is skipped because it calls finish() when it opens MainActivity
                if (bottomNav.getSelectedItemId() == R.id.groupListsFragment)
                {
                    finish();
                }
                // if user is on a specific screen that we specify, return to the previous screen when back is pressed
                else if (Objects.equals(getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName(), "specific_category") || (Objects.equals(getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName(), "specific_recipe")))
                {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                // on initial navigation bar screens, ignore user history and just return to the default fragment (pop entire stack)
                else
                {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    // updates actual nav bar view itself to highlight the correct fragment
                    bottomNav.setSelectedItemId(R.id.groupListsFragment);
                }

                Log.i("TRACK BACKSTACK 2.0", "------------------------");
                Log.i("TRACK BACKSTACK 2.0", "Back stack tags (group lists does not appear here, but it is there.  It is always first on back stack): ");
                backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                StringBuilder tab = new StringBuilder();
                for (int i = 0; i < backStackCount; i++)
                {
                    Log.i("TRACK BACKSTACK 2.0", tab.toString() + i + " - " + getSupportFragmentManager().getBackStackEntryAt(i).getName());
                    tab.append("\t\t");
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
            // pop back stack every time we load a new fragment so stack doesn't fill with duplicate fragment tags
            //getSupportFragmentManager().popBackStack();

            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

            if (backStackCount >= 1)
            {
                //int tag = getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getId();
                Fragment frag2 = getSupportFragmentManager().findFragmentById(R.id.fragContainer);

                //getSupportFragmentManager().beginTransaction()
                //        .remove(frag2).add(R.id.fragContainer, frag).addToBackStack(backStackTag).commit();
               getSupportFragmentManager().beginTransaction()
                       .remove(frag2).commit();
               //getSupportFragmentManager().popBackStackImmediate();

               getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, frag).addToBackStack(null).commit();


                //getSupportFragmentManager().beginTransaction().add(R.id.fragContainer, frag).addToBackStack(backStackTag).commit();


            }
            else
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragContainer, frag)
                        .addToBackStack(backStackTag)
                        .commit();
            }

        }

        String numOfBackstack = String.valueOf(getSupportFragmentManager().getBackStackEntryCount());
        Log.i("TRACK BACKSTACK", "popped and fragment replaced: " + numOfBackstack);

        return true;
    }
    */

    }
    public void reloadListFrag()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.groupListsFragment, new GroupListsFragment()).commit();
    }






}
