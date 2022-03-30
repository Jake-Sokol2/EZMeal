package navigationFragments.MyRecipes.RecipeAdapters;

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

public class MyRecipesRecyclerAdapter extends RecyclerView.Adapter<MyRecipesRecyclerAdapter.MainViewHolder>
{
    private List<String> list;
    private List<String> url;
    private MainAdapterListener listener;



    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        private CardView cardView;
        private TextView txtDirection;
        private TextView txtTitleRecipe;
        private ImageView imgRecipe;
        private ImageView imgBackground;

        public MainViewHolder(View view)
        {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cardCategory);
            txtTitleRecipe = (TextView) view.findViewById(R.id.txtTitleRecipe);
            imgRecipe = (ImageView) view.findViewById(R.id.imgRecipe);
            imgBackground = (ImageView) view.findViewById(R.id.imageCategoryBackground);

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

    public MyRecipesRecyclerAdapter(List<String> list, List<String> url)
    {
        this.list = list;
        this.url = url;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_recipe_recycler_category_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
        holder.txtTitleRecipe.setText(list.get(position));

        String urllll = url.get(position);
        Glide.with(holder.itemView.getContext()).load(urllll).into(holder.imgRecipe);
        Glide.with(holder.itemView.getContext()).load(R.drawable.recycler_image_shadow).into(holder.imgBackground);


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