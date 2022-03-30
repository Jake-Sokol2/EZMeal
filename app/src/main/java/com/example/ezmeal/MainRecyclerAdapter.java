package com.example.ezmeal;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>
{
    private List<List<String>> list;
    private MainAdapterListener listener;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtListItem;
        public TextView txtBrandName;
        public CheckBox checkCrossOffItem;

        public MainViewHolder(View view)
        {
            super(view);
            txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position, txtListItem);
                        }
                    }
                }
            });
        }
    }

    public MainRecyclerAdapter(List<List<String>> list)
    {
        this.list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout. activity_main_recycler_item_rounded, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        // get(position) determines which recyclerview item was clicked - .get(0) or 1 is the first or second item in the 2d list
        String itemName = list.get(position).get(0);
        String brand = list.get(position).get(1);
        holder.txtListItem.setText(itemName);

        // todo: fix/remove this line when user data is being saved on app exit
        holder.txtBrandName.setText(brand);

        // if user doesn't enter a brand, set brand textview to be invisible, disabled, and 0 width/height so that cardview shrinks in size
        if (Objects.equals(brand, ""))
        {
            holder.txtBrandName.setVisibility(View.INVISIBLE);
            holder.txtBrandName.setEnabled(false);
            holder.txtBrandName.setHeight(0);
            holder.txtBrandName.setWidth(0);
        }
        else
        {
            holder.txtBrandName.setText(brand);
        }

        // CheckBox on click listener
        holder.checkCrossOffItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // add strikethrough flag to grocery list item name's paint flags.  When clicked a second time, the strikethrough flag is removed
                holder.txtListItem.setPaintFlags(holder.txtListItem.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

                // when grocery list item is checked
                boolean isChecked = holder.checkCrossOffItem.isChecked();
                if (isChecked)
                {
                    holder.txtListItem.setTextColor(ContextCompat.getColor(view.getContext(), R.color.group_list_item_crossed_out));
                }
                else
                {
                    holder.txtListItem.setTextColor(ContextCompat.getColor(view.getContext(), R.color.group_list_item));
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setData(List<List<String>> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface MainAdapterListener
    {
        void onItemClick(int position, TextView text);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }

}