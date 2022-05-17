package com.example.ezmeal.FindRecipes;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.AddListItemFragmentViewModel;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RateRecipeBottomDialogFragment extends BottomSheetDialogFragment{
    AddListItemFragmentViewModel theViewModel;

    private static final String ITEM_NAME = "item_name";
    private static final String BRAND_NAME = "brand_name";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1, mParam2;
    private GroupListsFragmentModel theModel;
    private List<GroupListsFragmentModel> toSave = new ArrayList<GroupListsFragmentModel>();
    private GroupListsFragmentRecyclerAdapter adapter;
    private EditText editItemName, editBrandName;

    private RatingBar ratingBar;
    private float rating;
    private RateRecipeDialogInterface rateRecipeInterface;
    private Button btnConfirm;
    private RateRecipeBubbleViewModel vmRateRecipeBubble;
    private RateRecipeViewModel vmRateRecipe;
    private Button btnCancel;

    private ObjectAnimator alpha;

    public RateRecipeBottomDialogFragment() {

    }

    public RateRecipeBottomDialogFragment(float rating, RateRecipeDialogInterface rateRecipeInterface)
    {
        this.rating = rating;
        this.rateRecipeInterface = rateRecipeInterface;
    }

    public RateRecipeBottomDialogFragment(float rating)
    {
        this.rating = rating;
    }

    // TODO: Rename and change types and numbers of params
    public static RateRecipeBottomDialogFragment newInstance(String param1, String param2){
        RateRecipeBottomDialogFragment fragment = new RateRecipeBottomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //TODO: save the adapter in onSaveInstanceState
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_recipe_fragments, container, false);

        vmRateRecipeBubble = new ViewModelProvider(requireActivity()).get(RateRecipeBubbleViewModel.class);
        vmRateRecipe = new ViewModelProvider(requireActivity()).get(RateRecipeViewModel.class);

        ratingBar = view.findViewById(R.id.rbRateRecipeDialog);
        ratingBar.setRating(rating);

        TextView txtRatingDescription = view.findViewById(R.id.txtRatingDescription);

        loadTextRatingFragment(rating, 0f, txtRatingDescription);
        updateRatingDescription(rating, 0f, txtRatingDescription);

        // set rating in ViewModel to null in case old value (user opened rating multiple times) is still present
        vmRateRecipe.setStar(null);

        btnConfirm = view.findViewById(R.id.btnConfirmRating);
        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                vmRateRecipe.setStar(rating);

                dismiss();
            }
        });

        btnCancel = view.findViewById(R.id.btnCancelRating);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                vmRateRecipe.setStar(0f);
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser)
            {
                Log.i("test5", "rating changed");


                if (fromUser)
                {
                    loadTextRatingFragment(value, rating, txtRatingDescription);


                    if (alpha != null)
                    {
                        alpha.end();
                        alpha.cancel();
                    }
                    alpha = updateRatingDescription(value, rating, txtRatingDescription);


                    //if (value != rating)
                    //{
                        rating = value;
                        ratingBar.setRating(rating);
                    //}

                    //rating = value;
                    //ratingBar.setRating(2);
                    // user changed from low/mid rating to high rating, show appropriate bubbles
                    /*if (value >= 4 && rating < 4)
                    {
                        rating = value;
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        HighRatingBubbleFragment highRatingBubbleFrag = new HighRatingBubbleFragment();
                        ft.setReorderingAllowed(true);

                        ft.replace(R.id.fragRatingBubbleContainer, highRatingBubbleFrag);
                        //ft.show(highRatingBubbleFrag);
                        ft.commit();*/
                    /*ObjectAnimator animation11 = ObjectAnimator.ofFloat(card1, "translationX", 50f);
                    //ObjectAnimator animation1Fade = ObjectAnimator.ofFloat()
                    ObjectAnimator animation22 = ObjectAnimator.ofFloat(card2, "translationX", 50f);
                    //animation22.setStartDelay(75);
                    ObjectAnimator animation33 = ObjectAnimator.ofFloat(card3, "translationX", 50f);
                    //animation33.setStartDelay(125);


                    AnimatorSet animatorSet2 = new AnimatorSet();
                    animatorSet2.setDuration(1);
                    animatorSet2.playTogether(animation11, animation22, animation33);*/
                        //animatorSet.setDuration(400);

                        //BounceInterpolator bounce = new BounceInterpolator();
                        //animatorSet.setInterpolator(new AccelerateInterpolator());
                        //animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                        //animatorSet.setInterpolator(pathInterpolator);
                        //ConstraintLayout.LayoutParams lay = (ConstraintLayout.LayoutParams) card1.getLayoutParams();
                   /* txt1.setText("Quick");
                    //txt1.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 35));
                    txt2.setText("Easy to Make");
                    txt3.setText("Would Make Again");
                    //txt3.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 35));
                    rating = v;
                    animatorSet.start();*/

                        //card1.setLayoutParams(lp);
                        //card2.setLayoutParams(lp2);
                        //card3.setLayoutParams(lp3);
                        //BounceInterpolator bounce = new BounceInterpolator();
                        //animatorSet.setInterpolator(new AccelerateInterpolator());
                        //animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                        //animatorSet.setInterpolator(pathInterpolator);
                        //animatorSet.start();
                    //}
                    // user changed from low/high to mid rating, show appropriate bubbles
                    /*else if (value == 3 && rating != 3)
                    {
                        rating = value;
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        MidRatingBubbleFragment midRatingBubbleFrag = new MidRatingBubbleFragment();
                        ft.setReorderingAllowed(true);

                        ft.replace(R.id.fragRatingBubbleContainer, midRatingBubbleFrag);
                        //ft.show(highRatingBubbleFrag);
                        ft.commit();*/
                        //ConstraintLayout.LayoutParams lay = (ConstraintLayout.LayoutParams) txt3.getLayoutParams();
                        //cl.addView(txt3);
                        //cl.removeView(txt3);
                        //cl.addView(txt3);
                        //txt3.setVisibility(View.GONE);
                        //txt3.setVisibility(View.VISIBLE);
                    /*txt1.setText("Easy to Make");
                    txt2.setText("Needed Changes");
                    txt3.setText("Bland");
                    txt3.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 35));
                    rating = v;*/
                        //animatorSet.start();
                    /*}
                    // user changed from mid/high to low rating, show appropriate bubbles
                    else if (value <= 2 && rating > 2)
                    {
                        rating = value;
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        LowRatingBubbleFragment lowRatingBubbleFrag = new LowRatingBubbleFragment();
                        ft.setReorderingAllowed(true);

                        ft.replace(R.id.fragRatingBubbleContainer, lowRatingBubbleFrag);
                        //ft.show(highRatingBubbleFrag);
                        ft.commit();*/

                    /*txt1.setText("Bland");
                    txt2.setText("Needed Changes");
                    txt3.setText("Didn't Turn Out Right");
                    rating = v;
                    animatorSet.start();*/
                    //}
                }
            }
        });




        return view;
    }

    private ObjectAnimator updateRatingDescription(float rating, float oldRating, TextView txtRatingDescription)
    {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setReorderingAllowed(true);

        float changeInRating = rating - oldRating;

        ObjectAnimator alpha = null;
        
        if (changeInRating != 0)
        {
            
            switch ((int) rating)
            {

                case 1:
                {
                    txtRatingDescription.setText("Very bad");
                    break;
                }
                case 2:
                {
                    txtRatingDescription.clearAnimation();
                    txtRatingDescription.setText("Bad");
                    break;
                }
                case 3:
                {
                    txtRatingDescription.setText("It's okay");
                    break;
                }
                case 4:
                {
                    txtRatingDescription.setText("Good");
                    break;
                }
                case 5:
                {
                    txtRatingDescription.clearAnimation();
                    txtRatingDescription.setText("Excellent");
                    break;
                }
            }

            txtRatingDescription.clearAnimation();
            txtRatingDescription.setAlpha(0f);
            alpha = ObjectAnimator.ofFloat(txtRatingDescription, "alpha", 1f);
            alpha.setStartDelay(200);
            alpha.setDuration(1400);
            alpha.setInterpolator(new DecelerateInterpolator());

            alpha.start();
        }

        return alpha;
    }

    private void loadTextRatingFragment(float rating, float oldRating, TextView txtRatingDescription)
    {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setReorderingAllowed(true);

        if (rating > 3 && oldRating < 4)
        {
            HighRatingBubbleFragment highRatingBubbleFrag = new HighRatingBubbleFragment();
            ft.replace(R.id.fragRatingBubbleContainer, highRatingBubbleFrag);
            ft.commit();
        }
        else if (rating == 3 && oldRating != 3)
        {
            MidRatingBubbleFragment midRatingBubbleFrag = new MidRatingBubbleFragment();
            ft.replace(R.id.fragRatingBubbleContainer, midRatingBubbleFrag);
            ft.commit();
        }
        // todo: actually account for 0 stars!  or find out how to disable it.  this isn't the answer because it still loads the fragment
        else if (((rating > 0) && (rating < 3)) && ((oldRating > 2) || oldRating == 0))
        {
            LowRatingBubbleFragment lowRatingBubbleFrag = new LowRatingBubbleFragment();
            ft.replace(R.id.fragRatingBubbleContainer, lowRatingBubbleFrag);
            ft.commit();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onCancel(final DialogInterface dialog)
    {
        vmRateRecipe.setStar(0f);
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(final DialogInterface dialog)
    {
        super.onDismiss(dialog);
    }

}
