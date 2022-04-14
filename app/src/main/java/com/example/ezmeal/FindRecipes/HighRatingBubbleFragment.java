package com.example.ezmeal.FindRecipes;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.ezmeal.GroupLists.Adapter.GroupListsFragmentRecyclerAdapter;
import com.example.ezmeal.GroupLists.Model.GroupListsFragmentModel;
import com.example.ezmeal.GroupLists.ViewModel.AddListItemFragmentViewModel;
import com.example.ezmeal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class HighRatingBubbleFragment extends BottomSheetDialogFragment implements View.OnClickListener{
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
    private RateRecipeBubbleViewModel vm;

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    private CardView card1;
    private CardView card2;
    private CardView card3;

    private List<String> chosenBubbles;

    // maximum number of bubbles (text ratings) the user is allowed to choose
    private final int BUBBLE_LIST_LIMIT = 2;

    public HighRatingBubbleFragment() {

    }

    public HighRatingBubbleFragment(float rating, RateRecipeDialogInterface rateRecipeInterface)
    {
        this.rating = rating;
        this.rateRecipeInterface = rateRecipeInterface;
    }

    // TODO: Rename and change types and numbers of params
    public static HighRatingBubbleFragment newInstance(String param1, String param2){
        HighRatingBubbleFragment fragment = new HighRatingBubbleFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_high_rated_bubble, container, false);

        chosenBubbles = new ArrayList<String>();

        //ratingBar = view.findViewById(R.id.rbRateRecipeDialog);
        //ratingBar.setRating(rating);


        card1 = view.findViewById(R.id.cvRate1);
        card1.setOnClickListener(this);
        card2 = view.findViewById(R.id.cvRate2);
        card2.setOnClickListener(this);
        card3 = view.findViewById(R.id.cvRate3);
        card3.setOnClickListener(this);

        txt1 = view.findViewById(R.id.txtRate1);
        txt2 = view.findViewById(R.id.txtRate2);
        txt3 = view.findViewById(R.id.txtRate3);


        Path path = new Path();

        path.arcTo(-200f, 0f, -50f, 5f, 0f, -90f, true);

        ObjectAnimator animation1 = ObjectAnimator.ofFloat(card1, "translationX", "translationY", path);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(card1, "alpha", 1f);
        animation1.setStartDelay(200);
        alpha1.setStartDelay(200);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(card2, "translationX", "translationY", path);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(card2, "alpha", 1f);
        alpha2.setStartDelay(250);
        animation2.setStartDelay(250);
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(card3, "translationX", "translationY", path);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(card3, "alpha", 1f);
        animation3.setStartDelay(300);
        alpha3.setStartDelay(300);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(animation1, alpha1, animation2, alpha2, animation3, alpha3);
        animatorSet.setDuration(520);

        animatorSet.start();

        ConstraintLayout cl = (ConstraintLayout) view.findViewById(R.id.clHighRate);

        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b)
            {
                Log.i("test5", "rating changed");

                // user changed from low/mid rating to high rating, show appropriate bubbles
                if (v >= 4 && rating < 4)
                {
                    txt1.setText("Quick");
                    txt2.setText("Easy to Make");
                    txt3.setText("Would Make Again");
                    //rating = v;
                    animatorSet.start();
                }
                // user changed from low/high to mid rating, show appropriate bubbles
                else if (v == 3 && rating != 3)
                {
                    txt1.setText("Easy to Make");
                    txt2.setText("Needed Changes");
                    txt3.setText("Bland");
                    txt3.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 35));
                    //rating = v;
                }
                // user changed from mid/high to low rating, show appropriate bubbles
                else if (v <= 2 && rating > 2)
                {
                    txt1.setText("Bland");
                    txt2.setText("Needed Changes");
                    txt3.setText("Didn't Turn Out Right");
                    //rating = v;
                    animatorSet.start();
                }

            }
        });*/

        vm = new ViewModelProvider(requireActivity()).get(RateRecipeBubbleViewModel.class);

        btnConfirm = view.findViewById(R.id.btnConfirmRating);


        return view;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.cvRate1)
        {
            addOrRemoveBubbleToList(card1, txt1);
        }
        else if (view.getId() == R.id.cvRate2)
        {
            addOrRemoveBubbleToList(card2, txt2);
        }
        else if (view.getId() == R.id.cvRate3)
        {
            addOrRemoveBubbleToList(card3, txt3);
        }
    }

    public void addOrRemoveBubbleToList(CardView card, TextView tv)
    {
        String selectedText = String.valueOf(tv.getText());

        // if user hasn't chosen 3 bubbles yet and they didn't choose a bubble that is already selected
        if (chosenBubbles.size() < BUBBLE_LIST_LIMIT && !(chosenBubbles.contains(selectedText)))
        {
            // add this bubble to the list of chosen ratings
            chosenBubbles.add(selectedText);

            // change color of card and textview to make it appear selected
            card.setCardBackgroundColor(Color.parseColor("#2dba73"));
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            // remove rating from the list as the user unselected the bubble.  ArrayList automatically resizes so it will not fill with empty values
            chosenBubbles.remove(selectedText);

            // change color of card and textview to show user it is unselected
            card.setCardBackgroundColor(Color.parseColor("#ffffff"));
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

/*    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                vm.setNameData("clicked");
                dismiss();
            }
        });
    }*/

    @Override
    public void onStop()
    {
        super.onStop();

        RateRecipeBubbleViewModel vm;
        vm = new ViewModelProvider(requireActivity()).get(RateRecipeBubbleViewModel.class);

        vm.setRatingBubbleList(chosenBubbles);


        /*Observer<List<String>> rateObserver = new Observer<List<String>>()
        {
            @Override
            public void onChanged(List<String> s)
            {
                for (int i = 0; i < s.size(); i++)
                {
                    Log.i("test7", s.get(i));
                }
            }
        };
        vm.getRatingBubbleList().observe(this, rateObserver);*/

        //vm.getNameData().observe(this, rateObserver);
        //List<String> uploadedRatings = vm.getRatingBubbleList();

        //Log.i("test6", String.valueOf(vm.getRatingBubbleList()));

    }

    @Override
    public void onCancel(final DialogInterface dialog)
    {
        super.onCancel(dialog);

        Log.i("testtesttest", "cancelled");
    }

    @Override
    public void onDismiss(final DialogInterface dialog)
    {
        super.onDismiss(dialog);

        Log.i("testtesttest", "dismissed");
    }


}
