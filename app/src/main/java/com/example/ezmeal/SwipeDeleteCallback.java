package com.example.ezmeal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.Model.GroceryListModel;

import javax.annotation.Nonnull;

//Following tutorial from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e

public class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private Drawable icon;
    private final ColorDrawable background;
    private MainRecyclerAdapter mAdapter;
    private GroceryListModel theModel;

    public SwipeDeleteCallback(MainRecyclerAdapter adapter, GroceryListModel theModel)
    {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        //TODO: We need an icon for deleting
        //icon = ContextCompat.getDrawable(mAdapter.getContext(),
        //        R.drawable.ic_delete_white_36);
        background = new ColorDrawable(Color.RED);
        this.theModel = theModel;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        theModel.removeItem(viewHolder.getAbsoluteAdapterPosition());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive)
    {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dX > 0)
        {
            //swiping right
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() +
                    ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0)
        {
            //swiping left
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else
        {
            //view is unswiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                       RecyclerView.ViewHolder target)
    {
        return false;
    }

}
