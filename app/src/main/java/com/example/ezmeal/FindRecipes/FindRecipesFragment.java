package com.example.ezmeal.FindRecipes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezmeal.FindRecipes.FindRecipesAdapters.CategoryFragmentAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesAdapters.FindRecipesFragmentHorizontalRecyclerAdapter;
import com.example.ezmeal.FindRecipes.FindRecipesModels.CategoryFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesModels.FindRecipesFragmentModel;
import com.example.ezmeal.FindRecipes.FindRecipesViewModels.FindRecipesViewModel;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindRecipesFragment extends Fragment
{
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private EZMealDatabase sqlDb;

    private ArrayList<List<String>> recipeList = new ArrayList<List<String>>();
    List<String> list = new ArrayList<String>();
    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();
    private FindRecipesFragmentModel findRecipesFragmentModel = new FindRecipesFragmentModel();

    private RecyclerView rvFindRecipes;
    private RecyclerView rvHorizontal;
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private FindRecipesFragmentHorizontalRecyclerAdapter horizontalAdapter;

    private List<String> recipeId = new ArrayList<String>();

    public List<String> categories = new ArrayList<String>();
    private List<Boolean> isSelected = new ArrayList<>();
    private Integer lastSelectedCategory = 0;

    private FindRecipesViewModel viewModel;

    private int currentSelectedCategoryPosition = 0;
    public ImageView imageView;
    public String[] imageList;
    public Bitmap[] bitmapList;
    public String[] titleList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String RECYCLER_VIEW_KEY = "recycler_view_key";
    private static final String RV_DATA = "rv_data";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindRecipesFragment newInstance(String param1, String param2) {
        FindRecipesFragment fragment = new FindRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_recipes, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FindRecipesViewModel.class);

        rvHorizontal = (RecyclerView) view.findViewById(R.id.rvHorizontalSelector);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvHorizontal.setLayoutManager(horizontalLayoutManager);

        //horizontalAdapter = new FindRecipesFragmentHorizontalRecyclerAdapter(findRecipesFragmentModel.getCategoryList(), findRecipesFragmentModel.getIsSelectedList());
        horizontalAdapter = new FindRecipesFragmentHorizontalRecyclerAdapter(categories, isSelected);
        //horizontalAdapter.setData(model.getCategoryList(), model.getIsSelectedList());
        rvHorizontal.setAdapter(horizontalAdapter);

        viewModel.getHorizontalRecyclerModel().observe(getViewLifecycleOwner(), model ->
        {
            categories = model.getCategoryList();
            isSelected = model.getIsSelectedList();
            horizontalAdapter.setData(model.getCategoryList(), model.getIsSelectedList());
            horizontalAdapter.notifyDataSetChanged();
        });

        if (savedInstanceState == null)
        {
            FindRecipesFragmentModel fm = new FindRecipesFragmentModel();
            List<String> categoryList = new ArrayList<String>();
            List<Boolean> isSelectedList = new ArrayList<Boolean>();
            fm.setCategoryList(categoryList);
            fm.setIsSelectedList(isSelectedList);
            viewModel.setHorizontalRecyclerModel(fm);
         /*Fragment oldFragment = getChildFragmentManager().findFragmentById(R.id.fragmentContainerView4);
                if (oldFragment instanceof FeaturedFragment)
                {
                    ((FeaturedFragment) oldFragment).cleanUpFragmentInstanceState();
                }*/

            //categories.add("Featured");
            //findRecipesFragmentModel.addItem(categories.get(0), true);






            db = FirebaseFirestore.getInstance();

            // todo: RecipesRating
            CollectionReference dbRecipes = db.collection("Recipes");

            db.collection("RecipeCategoryRatingList").document("categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    viewModel.addItem("Featured", true);
                    categories = (ArrayList<String>) task.getResult().get("categories");

                    for (int i = 0; i < categories.size(); i++)
                    {
                        viewModel.addItem(categories.get(i), false);
                        //findRecipesFragmentModel.addItem(categories.get(i), false);
                    }


                    // retrieve categories from SQL here


                    horizontalAdapter.notifyDataSetChanged();
                }
            });
        }
        else
        {
            //viewModel.getListSize();


            //horizontalAdapter = viewModel.getHorizontalRecyclerModel().getValue();
        }





        /*// todo: find
        db.collection("RecipeCategoryRatingList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                for (QueryDocumentSnapshot document : task.getResult())
                {
                    //Log.i("retrieve", document.getId() + "=> " + document.getData());
                    //String title = document.getString("title");
                    //String imageUrl = document.getString("imageUrl");
                    categories.add(document.getString("category"));
                    findRecipesFragmentModel.addItem(document.getString("category"), false);
                    //recipeId.add(document.getId());
                }

                horizontalAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });*/

        /*viewModel.getHorizontalRecyclerAdapter().observe(getViewLifecycleOwner(), adapter ->
        {
            if (adapter != null)
            {
                horizontalAdapter = adapter;
                rvHorizontal.setAdapter(horizontalAdapter);
            }
        });*/



        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        /*FragmentManager categoryFragManager = getChildFragmentManager();
        Fragment frag = new FeaturedFragment();
        categoryFragManager.beginTransaction().replace(R.id.fragmentContainerView4, frag).commit();*/

        //getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();getChildFragmentManager().popBackStackImmediate();


        viewModel.getLastSelectedCategory().observe(getViewLifecycleOwner(), categoryPosition ->
        {
            lastSelectedCategory = categoryPosition;
        });

        horizontalAdapter.setOnItemClickListener(new FindRecipesFragmentHorizontalRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {

                Fragment fragCategoryClick;

                /*Fragment oldFragment = getChildFragmentManager().findFragmentById(R.id.fragmentContainerView4);
                if (oldFragment instanceof CategoryFragment)
                {
                    ((CategoryFragment) oldFragment).cleanUpFragmentInstanceState();

                }*/

                FragmentTransaction fragmentTransaction;

                // position 0 is "featured" section, which is not stored as a category in the database and would cause a crash if passed in as one
                // only pass a bundle if the user selects a card other than featured
                if (position != 0)
                {
                    fragCategoryClick = new CategoryFragment();

                    viewModel.setSelected(lastSelectedCategory, false);
                    viewModel.setSelected(position, true);
                    viewModel.setLastSelectedCategory(position);

                    //findRecipesFragmentModel.setNotSelected(currentSelectedCategoryPosition);
                    //findRecipesFragmentModel.setSelected(position);

                    //currentSelectedCategoryPosition = position;

                    Bundle categoryBundleClick = new Bundle();
                    categoryBundleClick.putString("cat", categories.get(position - 1));
                    fragCategoryClick.setArguments(categoryBundleClick);

                    fragmentTransaction = getChildFragmentManager().beginTransaction();
                    getChildFragmentManager().popBackStack();

                    getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragCategoryClick).commit();
                }
                else
                // featured was clicked, set last category to unclicked (visually) and set featured to clicked
                {
                    fragCategoryClick = new FeaturedFragment();

                    viewModel.setSelected(lastSelectedCategory, false);
                    viewModel.setSelected(0, true);
                    viewModel.setLastSelectedCategory(0);
                    //findRecipesFragmentModel.setNotSelected(currentSelectedCategoryPosition);
                    //findRecipesFragmentModel.setSelected(0);

                    //currentSelectedCategoryPosition = 0;

                    fragmentTransaction = getChildFragmentManager().beginTransaction();

                    getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragCategoryClick).commit();
                }
                horizontalAdapter.notifyDataSetChanged();

                //getChildFragmentManager().popBackStackImmediate();

            }
        });
    }
/*    @Override
    public void onStop()
    {
        super.onStop();
        findRecipesHorizontalModel.dumpList();
    }*/




/*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        AsyncClass ac = new AsyncClass();
        ac.execute();
    }

    public class AsyncClass extends AsyncTask<Void, Void, Void> {
        //private Document doc = Jsoup.connect("https://cookstre.com").get();
        //String url = "https://firebase.google.com/";
        String urlText = "https://www.allrecipes.com/recipes/78/breakfast-and-brunch/";
        Elements divs;

        ProgressDialog progressDialog;
        private Context ctx;
        public Bitmap bitmap;
        public String title;
        public String imgSrc;
        public String imgUrl;

        TextView textView;
        private View view;

        AsyncClass()
        {

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //textView = findViewById(R.id.textView);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            //Document get request stuff?
            try
            {


                //Connect to website
                Document doc = Jsoup.connect(urlText).get();

                // this "Breakfast and Brunch" page has two sets of div classes - one containing the 12 staff picks recipes, the second containing 24 "More Breakfast and Brunch"
                // recipes at the bottom of the screen.  We get the first of the two divs and ignore the second
                Element content = doc.getElementsByClass("category-page-list-content category-page-list-content__recipe-card karma-main-column").get(0);
                divs = content.getElementsByClass("component card card__category");

                imageList = new String[divs.size()];
                bitmapList = new Bitmap[divs.size()];
                titleList = new String[divs.size()];

                mStorageRef = FirebaseStorage.getInstance().getReference();

                db = FirebaseFirestore.getInstance();
                CollectionReference dbRecipes = db.collection("Recipes");


                int i = 0;
                for(Element e: divs)
                {
                    imageList[i] = e.select("img").attr("src");
                    URL url = new URL(imageList[i]);

                    String random = UUID.randomUUID().toString();
                    StorageReference recipeRef = mStorageRef.child("recipeImages/" + random);

                    InputStream stream = url.openConnection().getInputStream();
                    UploadTask uploadTask = recipeRef.putStream(stream);
                    uploadTask.addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.i("firebase storage tag", "inputstream failed");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Log.i("firebase storage tag", "inputstream succeeded");

                            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    // String[] categories, String[] directions, String[] ingredients,
                                    //                  String[] nutrition, String imageUrl, String title, double rating
                                    ArrayList<String> categories = new ArrayList<>();
                                    categories.add("Breakfast");
                                    ArrayList<String> directions = new ArrayList<>();
                                    directions.add("direction 1");
                                    directions.add("direction 2");
                                    directions.add("direction 3");

                                    ArrayList<String> ingredients = new ArrayList<>();
                                    ingredients.add("ingredient 1");
                                    ingredients.add("ingredient 2");
                                    ingredients.add("ingredient 3");

                                    ArrayList<String> nutrition = new ArrayList<>();
                                    nutrition.add("calories");
                                    nutrition.add("protein");
                                    nutrition.add("sodium");

                                    String title = e.select("h3").text();

                                    Recipe recipe = new Recipe(categories, directions, ingredients, nutrition, String.valueOf(uri), title, 0);

                                    dbRecipes.add(recipe).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                    {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference)
                                        {
                                            Log.i("firebase storage tag", "recipe added to firestore database");
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Log.i("firebase storage tag", "FAILED to add recipe to firestore database");
                                        }
                                    });
                                    //Log.i("firebase storage tag", String.valueOf(uri));
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Log.i("firebase storage tag", "failed to get URL");
                                }
                            });
                        }
                    });




                    bitmapList[i] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    titleList[i] = e.select("h3").text();
                    i++;
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

                return null;
            }
        // everything in this method is performed on the main thread!
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);



            for(int i = 0; i < divs.size(); i++)
            {
                findRecipesModel.addItem(titleList[i], bitmapList[i]);
            }
            findRecipesAdapter.notifyDataSetChanged();

            progressDialog.dismiss();

        }
    }*/

    @Override
    public void onPause()
    {
        super.onPause();

        //getChildFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onStop()
    {
        super.onStop();


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // close Room
        if ((sqlDb != null) && (sqlDb.isOpen()))
        {
            sqlDb.close();
        }


    }

}