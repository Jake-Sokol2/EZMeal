package navigationFragments.MyRecipes.RecipeAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.R;

import java.util.List;

public class MyRecipesNutritionRecyclerAdapter extends RecyclerView.Adapter<MyRecipesNutritionRecyclerAdapter.MainViewHolder>
{
    private List<List<String>> list;
    private MainAdapterListener listener;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        public MainViewHolder(View view)
        {
            super(view);

            CardView cardView = (CardView) view.findViewById(R.id.mcardList);

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

    public MyRecipesNutritionRecyclerAdapter(List<List<String>> list)
    {
        this.list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_recipes_nutrition_recycler_item, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position)
    {
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