package com.example.ezmeal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nonnull;

//Following tutorial from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e

public class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private Drawable icon;
    private final ColorDrawable background;
    private MainRecyclerAdapter mAdapter;

    public SwipeDeleteCallback(MainRecyclerAdapter adapter)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        //TODO: We need an icon for deleting
        //icon = ContextCompat.getDrawable(mAdapter.getContext(),
        //        R.drawable.ic_delete_white_36);
        background = new ColorDrawable(Color.RED);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAbsoluteAdapterPosition();
        mAdapter.deleteItem(position);
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
