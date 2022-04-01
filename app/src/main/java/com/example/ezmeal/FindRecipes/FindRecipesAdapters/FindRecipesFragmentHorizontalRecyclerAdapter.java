package com.example.ezmeal.FindRecipes.FindRecipesAdapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.R;

import java.util.List;

public class FindRecipesFragmentHorizontalRecyclerAdapter extends RecyclerView.Adapter<FindRecipesFragmentHorizontalRecyclerAdapter.MainViewHolder>
{
    private List<String> list;
    //private List<String> url;
    private MainAdapterListener listener;
    private List<Boolean> isSelectedList;



    public class MainViewHolder extends RecyclerView.ViewHolder
    {
       // private CardView cardView;
        //private TextView txtDirection;
        private TextView txtBubbleTitle;
        private CardView cardBubble;
        //private ImageView imgRecipe;
        //private ImageView imgBackground;

        public MainViewHolder(View view)
        {
            super(view);

            //cardView = (CardView) view.findViewById(R.id.cardCategory);
            txtBubbleTitle = (TextView) view.findViewById(R.id.txtBubbleTitle);
            cardBubble = (CardView) view.findViewById(R.id.cardBubble);
            //imgRecipe = (ImageView) view.findViewById(R.id.imgRecipe);
            //imgBackground = (ImageView) view.findViewById(R.id.imageCategoryBackground);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position); //, cardView);
                        }
                    }
                }
            });
        }
    }

    public FindRecipesFragmentHorizontalRecyclerAdapter(List<String> list, List<Boolean> isSelected)//, List<String> url)
    {
        this.list = list;
        this.isSelectedList = isSelected;
        //this.url = url;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_selector_find_recipes, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        holder.txtBubbleTitle.setText(list.get(position));

        // if current recyclerview item is marked as selected, then color the bubble to show it
        if(isSelectedList.get(position))
        {
            holder.cardBubble.setCardBackgroundColor(Color.parseColor("#2dba73"));
            holder.txtBubbleTitle.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.cardBubble.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.txtBubbleTitle.setTextColor(Color.parseColor("#000000"));
        }

            //String urllll = url.get(position);
        //Glide.with(holder.itemView.getContext()).load(urllll).into(holder.imgRecipe);
        //Glide.with(holder.itemView.getContext()).load(R.drawable.recycler_image_shadow).into(holder.imgBackground);


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setData(List<String> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface MainAdapterListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }
}