package com.example.ezmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import java.util.Objects;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>
{
    private List<List<String>> testList;
    public CategoryAdapterListener categoryListener;

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtListItem;
        public TextView txtBrandName;

        public CategoryViewHolder(View view)
        {
            super(view);
            txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (categoryListener != null) {
                        // was getAdapterPosition(), this is deprecated now
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            categoryListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CategoryRecyclerAdapter(List<List<String>> testList)
    {
        this.testList = testList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout. activity_main_recycler_item_rounded, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryRecyclerAdapter.CategoryViewHolder holder, int position)
    {
        String text = testList.get(position).get(0);
        String brand = testList.get(position).get(1);
        holder.txtListItem.setText(text);

        // todo: fix/remove this line when user data is being saved on app exit
        holder.txtBrandName.setText(brand);

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
    }

    @Override
    public int getItemCount()
    {
        return testList.size();
    }

    //todo: maybe find a better way to name the varibles in setData() and other similar methods.  Could end up shadowing
    public void setData(List<List<String>> testList)
    {
        this.testList = testList;
        notifyDataSetChanged();
    }

    public interface CategoryAdapterListener
    {
        void onItemClick(int position); //, String name);
    }

    public void setOnItemClickListener(CategoryAdapterListener categoryListener)
    {
        this.categoryListener = categoryListener;
    }
}
