package navigationFragments.FindRecipes;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.viewpager2.widget.ViewPager2;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ezmeal.MainActivity;
import com.example.ezmeal.Model.GroceryListModel;
import com.example.ezmeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import navigationFragments.FindRecipes.FindRecipesAdapters.FindRecipesAdapter;
import navigationFragments.MyRecipes.MyRecipesSpecificCategoryFragment;
import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.MyRecipesSingleRecipeRecyclerAdapter;
import navigationFragments.MyRecipes.RecipeAdapters.RecipeViewPagerAdapter;
import navigationFragments.MyRecipes.SpecificRecipeFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private RecyclerView rvFindRecipes;
    private FindRecipesAdapter findRecipesAdapter;

    private List<String> recipeId;

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
        String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        Log.i("TRACK BACKSTACK", "Group Recipes opened: " + numOfBackstack);


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

        rvFindRecipes = (RecyclerView) view.findViewById(R.id.rvFindRecipesCategory);
        findRecipesAdapter = new FindRecipesAdapter(findRecipesModel.getRecipeList(), findRecipesModel.getUriList());
        rvFindRecipes.setAdapter(findRecipesAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        rvFindRecipes.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        CollectionReference dbRecipes = db.collection("Recipes");


        db.collection("Recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                recipeId = new ArrayList<String>();
                for (QueryDocumentSnapshot document : task.getResult())
                {
                    Log.i("retrieve", document.getId() + "=> " + document.getData());
                    String title = document.getString("title");
                    Uri uri = Uri.parse(document.getString("imageUrl"));

                    findRecipesModel.addItem(title, uri);
                    findRecipesAdapter.notifyDataSetChanged();
                    recipeId.add(document.getId());
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });


        findRecipesAdapter.setOnItemClickListener(new FindRecipesAdapter.MainAdapterListener()
        {
            @Override
            public void onItemClick(int position, CardView cardView)
            {
                //Toast.makeText(getContext(), Integer.toString(cardView.getId()), Toast.LENGTH_SHORT).show();
                // Code to use the selected name goes here…

                // todo: fix this when database is working
                // name of the clicked category, gets sent to new Activity and is later used to let Firebase know which data to populate Activity with

                String id = "a";

                Fragment endFrag = new SpecificRecipeFragment();

                String name = "transition" + position;

                Bundle bundle = new Bundle();
                bundle.putString("id", recipeId.get(position));
                endFrag.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()

                        //.addSharedElement(cardView, "test")
                        //       animations:    enter            exit          popEnter        popExit
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .addToBackStack("specific_recipe")
                        .replace(R.id.fragContainer, endFrag)
                        .commit();
            }
        });

        return view;
    }




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