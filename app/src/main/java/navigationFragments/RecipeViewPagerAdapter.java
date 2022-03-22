package navigationFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ezmeal.RecipeDirectionsFragment;
import com.example.ezmeal.RecipeNutritionFragment;

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
                return new RecipeInstructionsFragment();
            case 2:
                return new RecipeDirectionsFragment();
            default:
                return new RecipeNutritionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
