package com.example.ezmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

//todo: change variable names:
//  testList
//  txtTest
//  text
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>
{
    private List<List<String>> testList;
    private MainAdapterListener listener;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtListItem;
        public TextView txtBrandName;

        public MainViewHolder(View view)
        {
            super(view);
            txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);

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

    public MainRecyclerAdapter(List<List<String>> testList)
    {
        this.testList = testList;
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

    public interface MainAdapterListener
    {
        void onItemClick(int position); //, String name);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }


}
