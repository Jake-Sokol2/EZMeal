package navigationFragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModel;

import com.example.ezmeal.R;

public class MyRecipesFragmentViewModel extends ViewModel {
    public FrameLayout.LayoutParams setCardWidthHeight(int width, int height) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);

        //cardView.setLayoutParams(layoutParams);

        return layoutParams;
    }
}
