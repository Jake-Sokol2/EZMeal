package com.example.ezmeal.GroupLists.Adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class GroupListsFragmentRecyclerAdapter extends RecyclerView.Adapter<GroupListsFragmentRecyclerAdapter.MainViewHolder>
{
    private List<List<String>> list;
    private MainAdapterListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String listName;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtListItem;
        public TextView txtBrandName;
        public CheckBox checkCrossOffItem;
        public TextView creator;

        public MainViewHolder(View view)
        {
            super(view);
            txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);
            creator = (TextView) view.findViewById(R.id.txtAuthor);


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

    public GroupListsFragmentRecyclerAdapter(List<List<String>> list)
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
        String tempCreator = list.get(position).get(2);

        Boolean isChecked = false;
        isChecked = Boolean.valueOf(list.get(position).get(4));
        listName = list.get(position).get(5);




        if (isChecked)
        {
            holder.checkCrossOffItem.setChecked(true);
            //holder.txtListItem.setPaintFlags(holder.txtListItem.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtListItem.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.group_list_item_crossed_out));
        }
        else
        {
            holder.checkCrossOffItem.setChecked(false);
            holder.txtListItem.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.group_list_item));
        }

        holder.txtListItem.setText(itemName);

        // todo: fix/remove this line when user data is being saved on app exit
        holder.txtBrandName.setText(brand);
        holder.creator.setText(tempCreator);

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
                //holder.txtListItem.setPaintFlags(holder.txtListItem.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

                db.collection("Groups").document(listName).collection("Items").whereEqualTo("name", itemName).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for (QueryDocumentSnapshot document:task.getResult())
                        {
                            String docId = document.getId();

                            // if user checked the checkbox, update firebase to "checked" and change visually
                            if (holder.checkCrossOffItem.isChecked())
                            {
                                //holder.txtListItem.setPaintFlags(holder.txtListItem.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                                db.collection("Groups").document(listName).collection("Items").document(docId).update("isSelected", true);
                                holder.txtListItem.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.group_list_item_crossed_out));
                            }
                            // else update to not checked and change visually
                            else
                            {
                                db.collection("Groups").document(listName).collection("Items").document(docId).update("isSelected", false);
                                holder.txtListItem.setTextColor(ContextCompat.getColor(view.getContext(), R.color.group_list_item));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.i("a", "FAILURE");
                    }
                });
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