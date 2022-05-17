package com.example.ezmeal.MyRecipes.RecipeAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.R;

import java.util.List;

public class RecipeIngredientsFragmentRecyclerAdapter extends RecyclerView.Adapter<RecipeIngredientsFragmentRecyclerAdapter.MainViewHolder>
{
    private List<String> list;
    private MainAdapterListener listener;



    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        private CardView cardView;
        private TextView txtIngredient;

        public MainViewHolder(View view)
        {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cardCategory);
            txtIngredient = (TextView) view.findViewById(R.id.txtRecipeItem);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    if (listener != null)
                    {
                        // was getAdapterPosition(), this is deprecated now
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position, cardView);
                        }
                    }
                }
            });
        }
    }

    public RecipeIngredientsFragmentRecyclerAdapter(List<String> list)
    {
        this.list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        holder.txtIngredient.setText(list.get(position));
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
        void onItemClick(int position, CardView cardView);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }
}