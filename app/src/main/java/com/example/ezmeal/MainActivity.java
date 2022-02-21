package com.example.ezmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    // todo: remove entire MainActivity fragment after group lists fragment is functional, its no longer used
    /*
    private List<String> testList = new ArrayList<>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        rvGroupList = (RecyclerView) findViewById(R.id.rvGroupList);
        adapter = new MainRecyclerAdapter(testList);
        adapter.setOnItemClickListener(new MainRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {
                String selectedName = testList.get(position);
                // Code to use the selected name goes here…
                Toast.makeText(getApplicationContext(), selectedName, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvGroupList.setLayoutManager(layoutManager);
        rvGroupList.setAdapter(adapter);

        // Add some data
        testList.add("Gallon of Milk");
        testList.add("Fruit");
        testList.add("Eggs");
        adapter.notifyDataSetChanged();
        */
        //navigation bar
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfig =  new AppBarConfiguration.Builder(R.id.groupListsFragment, R.id.groupRecipesFragment, R.id.myRecipesFragment, R.id.groupSettingsFragment).build();

        // todo: remove this?  Sets title for each bottom nav fragment, won't be needed if we set the title in middle of screen
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);

        NavigationUI.setupWithNavController(bottomNav, navController);


    }
}