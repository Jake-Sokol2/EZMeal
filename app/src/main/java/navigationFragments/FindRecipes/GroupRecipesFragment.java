package navigationFragments.FindRecipes;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import navigationFragments.FindRecipes.FindRecipesAdapters.FindRecipesAdapter;
import navigationFragments.FindRecipes.FindRecipesAdapters.FindRecipesHorizontalRecyclerAdapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

//import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupRecipesFragment extends Fragment
{
    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    private ArrayList<List<String>> recipeList = new ArrayList<List<String>>();
    List<String> list = new ArrayList<String>();
    private FindRecipesModel findRecipesModel = new FindRecipesModel();
    private FindRecipesHorizontalModel findRecipesHorizontalModel = new FindRecipesHorizontalModel();

    private RecyclerView rvFindRecipes;
    private RecyclerView rvHorizontal;
    private FindRecipesAdapter findRecipesAdapter;
    private FindRecipesHorizontalRecyclerAdapter horizontalAdapter;

    private List<String> recipeId = new ArrayList<String>();
    public List<String> categories = new ArrayList<String>();

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

    public GroupRecipesFragment() {
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
    public static GroupRecipesFragment newInstance(String param1, String param2) {
        GroupRecipesFragment fragment = new GroupRecipesFragment();
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

        // back stack logs
        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.i("TRACK BACKSTACK", "Group Recipes opened: " + numOfBackstack);


        // uncomment to blow the phone's memory to smithereens
        /*Gson gson = new Gson();
        // recipes_with_nutritional_info.json
        String jsonFileString = Utils.getJsonFromAssets(getActivity(), "recipes_with_nutritional_info.json");

        Type listUserType = new TypeToken<List<Example>>() { }.getType();
        List<Example> users = gson.fromJson(jsonFileString, listUserType);

        //for (int i = 0; i < users.size(); i++) {
        //    Log.i("data", "> Item " + i + "\n" + users.get(i));
        // }



        // WORKING PARSE

        /*JsonReader jsonReader;
        //JsonReader jsonReader = null;
        InputStreamReader inputStreamReader = null;
        try
        {

            inputStreamReader = new InputStreamReader(getActivity().getAssets().open("recipes_with_nutritional_info.json"));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        jsonReader = new JsonReader(inputStreamReader);


        Gson gson = new Gson();



        //final InputStreamReader isr = new InputStreamReader(inputStream);
        Type listUserType = new TypeToken<List<Example>>() { }.getType();
        //Type listUserType2 = new TypeToken<Collection<Example>>(){ }.getType();
        //Collection<Example> users = gson.fromJson(inputStreamReader, listUserType2);

        List<Example> users = gson.fromJson(inputStreamReader, listUserType);
        //final Example example = gson.fromJson(inputStreamReader, Example.class);
        //List<Example> users = gson.fromJson(inputStreamReader, listUserType);
        try
        {

            jsonReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        //for (int i = 0; i < users.size(); i++) {
        //    Log.i("data", "> Item " + i + "\n" + users.get(i));
        // }

        //String name = users.get(0).getTitle();
        //TextView txtName = view.findViewById(R.id.txtName);
       // txtName.setText(name);

        //categories.add("Breakfast");
        //categories.add("Brunch");

        //Bundle categoryBundle = new Bundle();

        //categoryBundle.putString("cat", categories.get(0));
        //bundleRecipeId.putString("id", recipeId);
        categories.add("Featured");
        findRecipesHorizontalModel.addItem(categories.get(0), true);

        FragmentManager categoryFragManager = getChildFragmentManager();
        //getSupportFragmentManager().beginTransaction().add(R.id.fragContainer, frag).addToBackStack(backStackTag).commit();
        Fragment frag = new FindRecipesCategoryFragment();
        //frag.setArguments(categoryBundle);
        categoryFragManager.beginTransaction().replace(R.id.fragmentContainerView4, frag).commit();

        rvHorizontal = (RecyclerView) view.findViewById(R.id.rvHorizontalSelector);
        horizontalAdapter = new FindRecipesHorizontalRecyclerAdapter(findRecipesHorizontalModel.getCategoryList(), findRecipesHorizontalModel.getIsSelectedList());
        rvHorizontal.setAdapter(horizontalAdapter);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvHorizontal.setLayoutManager(horizontalLayoutManager);

        db = FirebaseFirestore.getInstance();
        CollectionReference dbRecipes = db.collection("Recipes");

        db.collection("RecipeCategoryList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
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
                    findRecipesHorizontalModel.addItem(document.getString("category"), false);
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
        });

        //findRecipesHorizontalModel.addItem("abc");
        //findRecipesHorizontalModel.addItem("ddd");
        //findRecipesHorizontalModel.addItem("some category");
        //rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipesCategory);
        //findRecipesAdapter = new FindRecipesAdapter(findRecipesModel.getRecipeList(), findRecipesModel.getUriList());
        /*rvFindRecipes.setAdapter(findRecipesAdapter);
        RecyclerView.LayoutManager verticalLayoutManager = new GridLayoutManager(this.getActivity(), 2, RecyclerView.VERTICAL, false);
        rvFindRecipes.setLayoutManager(verticalLayoutManager);*/

        //db = FirebaseFirestore.getInstance();
        //CollectionReference dbRecipes = db.collection("Recipes");



        horizontalAdapter.setOnItemClickListener(new FindRecipesHorizontalRecyclerAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position)
            {

                Fragment fragCategoryClick = new FindRecipesCategoryFragment();

                // position 0 is "featured" section, which is not stored as a category in the database and would cause a crash if passed in as one
                // only pass a bundle if the user selects a card other than featured
                if (position != 0)
                {
                    findRecipesHorizontalModel.setNotSelected(currentSelectedCategoryPosition);
                    findRecipesHorizontalModel.setSelected(position);
                    currentSelectedCategoryPosition = position;
                    horizontalAdapter.notifyDataSetChanged();

                    Bundle categoryBundleClick = new Bundle();
                    categoryBundleClick.putString("cat", categories.get(position));
                    fragCategoryClick.setArguments(categoryBundleClick);
                }
                else
                // featured was clicked, set last category to unclicked (visually) and set featured to clicked
                {
                    findRecipesHorizontalModel.setNotSelected(currentSelectedCategoryPosition);
                    findRecipesHorizontalModel.setSelected(0);
                    currentSelectedCategoryPosition = 0;
                    horizontalAdapter.notifyDataSetChanged();
                }

                getChildFragmentManager().popBackStack();
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragCategoryClick).commit();
            }
        });

        return view;
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
}