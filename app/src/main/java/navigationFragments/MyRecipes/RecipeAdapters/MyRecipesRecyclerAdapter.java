package navigationFragments.MyRecipes.RecipeAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.R;

import java.util.List;

public class MyRecipesRecyclerAdapter extends RecyclerView.Adapter<MyRecipesRecyclerAdapter.MainViewHolder>
{
    private List<List<String>> list;
    private MainAdapterListener listener;



    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        private CardView cardView;
        //public TextView txtListItem;
        //public TextView txtBrandName;
        //public CheckBox checkCrossOffItem;

        public MainViewHolder(View view)
        {
            super(view);
            //txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            //txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            //checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);
            cardView = (CardView) view.findViewById(R.id.cardCategory);


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

    public MyRecipesRecyclerAdapter(List<List<String>> list)
    {
        this.list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout. fragment_my_recipe_recycler_category_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        holder.cardView.setTransitionName("transition" + position);

        // get(position) determines which recyclerview item was clicked - .get(0) or 1 is the first or second item in the 2d list
        /*
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
         */
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
        void onItemClick(int position, CardView cardView);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }
}