package com.example.ezmeal.MyRecipes.RecipeAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ezmeal.R;

import java.util.List;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.MainViewHolder>
{
    private List<String> list;
    private List<String> url;
    private MainAdapterListener listener;


    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtTitle;
        public ImageView recipeImage;

        public MainViewHolder(View view)
        {
            super(view);

            recipeImage = view.findViewById(R.id.imgRecipeImage);
            txtTitle = view.findViewById(R.id.textTitleRecipe);

            CardView cardView = (CardView) view.findViewById(R.id.cardRecipe);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    if (listener != null)
                    {
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

    public CategoryFragmentAdapter(List<String> list, List<String> urls)
    {
        this.list = list;
        this.url = urls;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout. recipe_recycler_recipe_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        String recipeTitle = list.get(position);
        String urlssss = url.get(position);

        holder.txtTitle.setText(recipeTitle);
        Glide.with(holder.itemView.getContext()).load(urlssss).into(holder.recipeImage);
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