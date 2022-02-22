package com.example.ezmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AddListItemActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_item);
    }


}