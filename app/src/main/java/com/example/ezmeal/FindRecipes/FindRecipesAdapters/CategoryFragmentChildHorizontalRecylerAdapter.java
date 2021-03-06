package com.example.ezmeal.FindRecipes.FindRecipesAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentChildHorizontalRecylerAdapter extends RecyclerView.Adapter<CategoryFragmentChildHorizontalRecylerAdapter.MainViewHolder>
{
    private List<String> list;
    private List<String> uriList;
    private List<Double> avgRatingList;
    private MainAdapterListener listener;
    private String uri;
    public TextView txtTitle;
    private List<HorizontalRecipe> horizontalList;
    Context context;

    public void setOnItemClickListener(CategoryFragmentAdapter.MainAdapterListener id)
    {
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
    {

        //public TextView txtBrandName;
        //public CheckBox checkCrossOffItem;
        public ImageView recipeImage;
        public TextView txtTitle;
        public RatingBar ratingBar;
        private MaterialCardView card;

        public MainViewHolder(View view)
        {
            super(view);
            //txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            //txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            //checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);

            recipeImage = view.findViewById(R.id.imgRecipeImage);
            txtTitle = view.findViewById(R.id.textTitleRecipe);
            ratingBar = view.findViewById(R.id.rbCard);
            card = view.findViewById(R.id.cardCategory);

            //CardView cardView = (CardView) view.findViewById(R.id.cardRecipe);

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
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CategoryFragmentChildHorizontalRecylerAdapter(List<HorizontalRecipe> horizontalList)
    {
        //this.list = list;
        this.horizontalList = horizontalList;
        /*this.context = context;
        this.uriList = uriList;
        this.avgRatingList = avgRatingList;*/
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_recipe_horizontal_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        //String recipeTitle = list.get(position);
        String recipeTitle = horizontalList.get(position).getTitle();
        uri = horizontalList.get(position).getImage();
        //uri = uriList.get(position);
        //String recipeImageUrl = list.get(position).get(1);
        //Bitmap bitmap =  bitmapList.get(position).get(0);

        //holder.recipeImage.setImageBitmap(bitmap);
        holder.txtTitle.setText(recipeTitle);
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.recipeImage);

        Double avgRatingDouble = horizontalList.get(position).getAvgRating();
        float avgRatingFloat = avgRatingDouble.floatValue();

        holder.card.setStrokeColor(Color.parseColor("#2dba73"));
        holder.card.setStrokeWidth(5);
        //holder.card.setCardBackgroundColor(Color.parseColor("#2dba73"));
        holder.ratingBar.setRating(avgRatingFloat);
       // try
       // {
            //holder.recipeImage.setImageBitmap(bitmap);
       // }
        //catch (IOException e)
        //{
        //    e.printStackTrace();
        //}
        //Picasso.get().load(recipeImageUrl).into(holder.recipeImage);

        // get(position) determines which recyclerview item was clicked - .get(0) or 1 is the first or second item in the 2d list

        /*
        String itemName = list.get(position).get(0);
        String itemImage = list.get(position).get(1);

        if (itemName != null)
        {
            holder.txtTitle.setText(itemName);
        }

        if (itemImage != null)
        {
            holder.recipeImage.setImageResource();
        }
        */



        /*
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
         */


        //https://firebasestorage.googleapis.com/v0/b/labfirebase-bfbfb.appspot.com/o/recipeImages%2Fe89d5156-59a5-4e0b-bc28-4875fdc9228b?alt=media&token=f3216426-50c6-4e3d-9af6-d0b9813f8126

    }

    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }

    public void setData(List<HorizontalRecipe> horizontalList)
    {
        this.horizontalList = horizontalList;
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