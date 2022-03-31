package navigationFragments.FindRecipes.FindRecipesAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ezmeal.R;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FindRecipesAdapter extends RecyclerView.Adapter<FindRecipesAdapter.MainViewHolder>
{
    private List<String> list;
    private List<String> uriList;
    private MainAdapterListener listener;
    private String uri;
    public TextView txtTitle;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {

        //public TextView txtBrandName;
        //public CheckBox checkCrossOffItem;
        public ImageView recipeImage;
        public TextView txtTitle;

        public MainViewHolder(View view)
        {
            super(view);
            //txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            //txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            //checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);

            recipeImage = view.findViewById(R.id.imgRecipeImage);
            txtTitle = view.findViewById(R.id.textTitleRecipe);

            CardView cardView = (CardView) view.findViewById(R.id.cardCategory);

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

    public FindRecipesAdapter(List<String> list, List<String> uriList)
    {
        this.list = list;
        this.uriList = uriList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_recipe_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        String recipeTitle = list.get(position);
        uri = uriList.get(position);
        //String recipeImageUrl = list.get(position).get(1);
        //Bitmap bitmap =  bitmapList.get(position).get(0);

        //holder.recipeImage.setImageBitmap(bitmap);
        holder.txtTitle.setText(recipeTitle);
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.recipeImage);
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