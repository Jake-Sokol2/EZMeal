package com.example.ezmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//todo: change variable names:
//  testList
//  txtTest
//  text
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>
{
    private List<String> testList;
    private MainAdapterListener listener;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public EditText editListItem;

        public MainViewHolder(View view)
        {
            super(view);
            editListItem = (EditText) view.findViewById(R.id.editListItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (listener != null) {
                        // was getAdapterPosition(), this is deprecated now
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public MainRecyclerAdapter(List<String> testList)
    {
        this.testList = testList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout. activity_main_recycler_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        String text = testList.get(position);
        holder.editListItem.setText(text);
    }

    @Override
    public int getItemCount()
    {
        return testList.size();
    }

    //todo: maybe find a better way to name the varibles in setData() and other similar methods.  Could end up shadowing
    public void setData(List<String> testList)
    {
        this.testList = testList;
        notifyDataSetChanged();
    }

    public interface MainAdapterListener
    {
        void onItemClick(int position); //, String name);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }


}
