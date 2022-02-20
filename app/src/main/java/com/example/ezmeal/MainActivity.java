package com.example.ezmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private List<String> testList = new ArrayList<>();
    private RecyclerView rvGroupList;
    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}