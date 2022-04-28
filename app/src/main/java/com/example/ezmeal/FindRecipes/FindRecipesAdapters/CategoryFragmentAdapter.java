package com.example.ezmeal.FindRecipes.FindRecipesAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentChildHorizontalRecyclerModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.HorizontalRecipe;
import com.example.ezmeal.FindRecipes.FindRecipesModels.VerticalRecipe;
import com.example.ezmeal.FindRecipes.RecipeActivity;
import com.example.ezmeal.R;
//import com.example.ezmeal.RoomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private FirebaseFirestore db;
    public CollectionReference dbRecipes;

    private List<String> list;
    private List<String> uriList;
    private List<String> popularRecipesTitleList;
    private List<String> popularRecipesImageList;
    private List<String> highRatedRecipeIdList;
    private List<Integer> totalRatingCountList;
    private List<Double> avgRatingList;
    private List<Double> avgPopularRatingList;
    private ArrayList<List<HorizontalRecipe>> horizontalLists;
    private List<VerticalRecipe> verticalRecipes;

    private MainAdapterListener listener;
    private String uri;
    public TextView txtTitle;
    private Context context;


    private final int COUNT_THRESHOLD_TO_SHOW_RECIPES = 1;
    private final int HORIZONTAL_VIEW = 0;
    private final int VERTICAL_VIEW = 1;

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerView childHorizontalRecyclerView;

        //public TextView txtBrandName;
        //public CheckBox checkCrossOffItem;
        public ImageView recipeImage;
        public TextView txtTitle;
        public RatingBar ratingBar;

        public MainViewHolder(View view)
        {
            super(view);
            //txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            //txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            //checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);

            recipeImage = view.findViewById(R.id.imgRecipeImage);
            txtTitle = view.findViewById(R.id.textTitleRecipe);
            ratingBar = view.findViewById(R.id.rbCard);

            // only used on horizontal rows
            //childHorizontalRecyclerView = itemView.findViewById(R.id.rvChildHorizontalRecipes);

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
                            // allow user to click the vertical item as it is a real recipe
                            listener.onItemClick(position, true);
                        }
                    }
                }
            });
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerView childHorizontalRecyclerView;

        //public TextView txtBrandName;
        //public CheckBox checkCrossOffItem;
        public ImageView recipeImage;
        public TextView txtTitle;
        public RatingBar ratingBar;
        private RecyclerView horizontalRecipes;

        public HorizontalViewHolder(View view)
        {
            super(view);
            //txtListItem = (TextView) view.findViewById(R.id.txtListItem);
            //txtBrandName = (TextView) view.findViewById(R.id.txtBrandName);
            //checkCrossOffItem = (CheckBox) view.findViewById(R.id.checkCrossOffItem);

            //recipeImage = view.findViewById(R.id.imgRecipeImage);
            //txtTitle = view.findViewById(R.id.textTitleRecipe);

            // only used on horizontal rows
            childHorizontalRecyclerView = itemView.findViewById(R.id.rvChildHorizontalRecipes);
            ratingBar = view.findViewById(R.id.rbCard);
            horizontalRecipes = view.findViewById(R.id.rvChildHorizontalRecipes);
            //CardView cardView = (CardView) view.findViewById(R.id.cardCategory);

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
                            // prevent user from clicking the vertical item as it is NOT a real recipe - it is only a holder for a nested horizontal recyclerview
                            listener.onItemClick(position, false);
                        }
                    }
                }
            });
        }
    }

    public CategoryFragmentAdapter(List<VerticalRecipe> verticalRecipes, ArrayList<List<HorizontalRecipe>> horizontalLists)
    {
        this.verticalRecipes = verticalRecipes;
        this.horizontalLists = horizontalLists;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 1)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_recipe_item, parent, false);
            return new MainViewHolder(itemView);
        }
        else
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_recipe_horizontal_parent_item, parent, false);
            return new HorizontalViewHolder(itemView);
        }

    }


    @Override
    public int getItemViewType(int position) {
        // based on you list you will return the ViewType
        if (position == 6) {
            return HORIZONTAL_VIEW;
        } else {
            return VERTICAL_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        final int itemType = getItemViewType(position);



        if (position == 6)
        {
            db = FirebaseFirestore.getInstance();
            // todo: RecipesRating
            dbRecipes = db.collection("RecipesRatingBigInt");

            StaggeredGridLayoutManager.LayoutParams staggeredLayout = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            staggeredLayout.setFullSpan(true);
            holder.itemView.setLayoutParams(staggeredLayout);

            HorizontalViewHolder horizontalHolder = (HorizontalViewHolder) holder;

            horizontalHolder.horizontalRecipes.setBackgroundColor(Color.parseColor("#ffffffff"));

            RecyclerView.LayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            horizontalHolder.childHorizontalRecyclerView.setHasFixedSize(true);

            horizontalHolder.childHorizontalRecyclerView.setLayoutManager(horizontalManager);

            EZMealDatabase sqlDb = Room.databaseBuilder(holder.itemView.getContext(), EZMealDatabase.class, "user")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();

            CategoryFragmentChildHorizontalRecyclerModel horizontalModel;

            horizontalModel = new CategoryFragmentChildHorizontalRecyclerModel(popularRecipesTitleList, popularRecipesImageList, avgPopularRatingList);

            CategoryFragmentChildHorizontalRecylerAdapter highRatedRecipesAdapter = new CategoryFragmentChildHorizontalRecylerAdapter(horizontalLists.get(0));
            /*CategoryFragmentChildHorizontalRecylerAdapter highRatedRecipesAdapter = new CategoryFragmentChildHorizontalRecylerAdapter(horizontalModel.getRecipeList(),
                    horizontalModel.getUriList(), horizontalHolder.childHorizontalRecyclerView.getContext(), horizontalModel.getAvgRatingList());*/

            highRatedRecipesAdapter.setOnItemClickListener(new CategoryFragmentChildHorizontalRecylerAdapter.MainAdapterListener()
            {
                @Override
                public void onItemClick(int position)
                {
                    Intent intent = new Intent(holder.itemView.getContext(), RecipeActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("id", horizontalLists.get(0).get(position).getRecipeId()); //highRatedRecipeIdList.get(position));
                    intent.putExtras(bundle);
                    holder.itemView.getContext().startActivity(intent);
                }
            });

            horizontalHolder.childHorizontalRecyclerView.setAdapter(highRatedRecipesAdapter);

        }
        else
        {
            MainViewHolder verticalHolder = (MainViewHolder) holder;

            StaggeredGridLayoutManager.LayoutParams staggeredLayout = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            staggeredLayout.setFullSpan(false);
            holder.itemView.setLayoutParams(staggeredLayout);



            String recipeTitle = verticalRecipes.get(position).getTitle();
            uri = verticalRecipes.get(position).getImage();

            verticalHolder.txtTitle.setText(recipeTitle);
            Glide.with(verticalHolder.itemView.getContext()).load(uri).into(verticalHolder.recipeImage);

            Double avgRatingDouble = verticalRecipes.get(position).getAvgRating();
            float avgRatingFloat = avgRatingDouble.floatValue();

            if (verticalRecipes.get(position).getTotalCountOfRatings() >= COUNT_THRESHOLD_TO_SHOW_RECIPES)
            {
                // todo: there is an underlying issue here - we shouldn't need to set this to VISIBLE as it should never be set INVISIBLE.  For some reason the recyclerview sets it invisible anyway
                verticalHolder.ratingBar.setVisibility(View.VISIBLE);
                verticalHolder.ratingBar.setRating(avgRatingFloat);
            }
            else
            {
                verticalHolder.ratingBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return verticalRecipes.size();
    }

    public void setData(List<VerticalRecipe> verticalRecipes)
    {
        this.verticalRecipes = verticalRecipes;
        notifyDataSetChanged();
    }

    public interface MainAdapterListener
    {
        void onItemClick(int position, boolean isClickable);
    }

    public void setOnItemClickListener(MainAdapterListener listener)
    {
        this.listener = listener;
    }
}