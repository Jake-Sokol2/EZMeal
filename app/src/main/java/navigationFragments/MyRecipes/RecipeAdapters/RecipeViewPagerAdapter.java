package navigationFragments.MyRecipes.RecipeAdapters;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import navigationFragments.MyRecipes.RecipeDirectionsFragment;
import navigationFragments.MyRecipes.RecipeInstructionsFragment;
import navigationFragments.MyRecipes.RecipeNutritionFragment;

public class RecipeViewPagerAdapter extends FragmentStateAdapter
{
    public RecipeViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new RecipeDirectionsFragment();
            case 2:
                return new RecipeNutritionFragment();
            default:
                return new RecipeInstructionsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
