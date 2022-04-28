package com.example.ezmeal.GroupSettings;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.ezmeal.Login.LoginActivity;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.AppDatabase;
import com.example.ezmeal.roomDatabase.EZMealDatabase;
import com.example.ezmeal.roomDatabase.Recipe;
import com.example.ezmeal.roomDatabase.RecipeItems;
import com.example.ezmeal.roomDatabase.RecyclerRecipe2;
import com.example.ezmeal.roomDatabase.User;
import com.example.ezmeal.roomDatabase.UserDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String[] imageList;
    public Bitmap[] bitmapList;
    public String[] titleList;

    private List<String> testList;


    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    //public int recipeId = 0;


    public GroupSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupSettingsFragment newInstance(String param1, String param2) {
        GroupSettingsFragment fragment = new GroupSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_settings, container, false);


        EZMealDatabase sqlDb;
        sqlDb = Room.databaseBuilder(getActivity().getApplicationContext(), EZMealDatabase.class, "user").allowMainThreadQueries().build();
        RecyclerRecipe2 rec = new RecyclerRecipe2("btewr", "a", "a", "a", 2.0, "a", false, 2);
        //sqlDb.testDao().insertRecyclerRecipe2(rec);
        //sqlDb.testDao().deleteAllCategories();
        //sqlDb.testDao().deleteALlRecyclerRecipes();

        List<RecipeItems> r = sqlDb.testDao().getRecipeItemsLive().getValue();

        sqlDb.testDao().deleteALlRecyclerRecipes();
        /*for (int i = 0; i < 500000; i++)
        {
            Log.i("a", r.get(i).recipe.title.toString());
        }*/
        long a = new Date().getTime();
        Log.i("a", String.valueOf(a));


        //testList; //= sqlDb.testDao().getActiveCategoriesFromIdentifier2();
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                testList = sqlDb.testDao().getActiveCategoriesFromIdentifier2();
            }
        });

        /*try
        {
            executor.wait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }*/

        /*Callable callable = new Callable()
        {
            @Override
            public Object call() throws Exception
            {
                sqlDb.testDao().insertRecyclerRecipe2(rec);
                return this;
            }
        };

        Future future = Executors.newSingleThreadExecutor().submit(callable);*/


        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Log.i("a", "a");
        // Kotlin room test
        /*AppDatabase db = Room.databaseBuilder(getContext().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        UserDao userDao = db.userDao();

        User u1 = new User(1, "aa", "bb");
        User u2 = new User(2, "cc", "dd");
        userDao.insertAll(u1, u2);
        List<User> users = userDao.getAll();

        String s = "";
        for (int i = 0; i < users.size(); i++)
        {
            s = s + "   " + users.get(i);
        }
        Log.i("a", s);*/

        //String numOfBackstack = String.valueOf(getParentFragmentManager().getBackStackEntryCount());
        //Log.w("TRACK BACKSTACK", "Group Settings opened: " + numOfBackstack);

        View rootView = inflater.inflate(R.layout.fragment_group_settings, container, false);
        Button btnLogout = (Button) rootView.findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                openActivityLogin();
            }
        });

        //int randomNum = ThreadLocalRandom.current().nextInt(1, 24);

        int randomNum = new Random().nextInt(10) + 1;
        Log.i("myRand", String.valueOf(randomNum));

        /*String image = "cookies.webp";
        int id = getContext().getResources().getIdentifier("drawable/cookies", null, getContext().getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);*/


        //img.setImageBitmap(bitmap);

        //Glide.with(getContext()).load(getResources().getIdentifier("cookies", "drawable", getActivity().getPackageName())).into(img);
        //img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_baseline_donut_large_24, getActivity().getApplicationContext().getTheme()));

        //String uri = "@drawable/cookies";

/*        int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        img.setImageDrawable(res);

        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try
        {
            inputStream = assetManager.open("assets/co.webp");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);*/

/*        try
        {
            String[] images = getActivity().getAssets().list("images");
            ArrayList<String> listImages = new ArrayList<String>(Arrays.asList(images));
            InputStream inputStream1 = getActivity().getAssets().open("images/"+listImages.get(3));
            Drawable drawable = Drawable.createFromStream(inputStream1, null);
            img.setImageDrawable(drawable);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        /*try
        {
            AssetManager assetManager = getContext().getAssets();
            InputStream inputStream = getContext().getAssets().open("cookies2.jpg");
            Drawable d = Drawable.createFromStream(inputStream, null);
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        return rootView;
    }

    public void openActivityLogin(){
        //getParentFragmentManager().popBackStack(getParentFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // kill back stack so user cannot return to main screens without logging in again
        getActivity().finish();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //AsyncClass ac = new AsyncClass();
        //ac.execute();


    }

    /*public class AsyncClass extends AsyncTask<Void, Void, Void>
    {
        private Random rand;

        //private Document doc = Jsoup.connect("https://cookstre.com").get();
        //String url = "https://firebase.google.com/";






        // NOT READY
        String urlText = "https://www.allrecipes.com/recipes/1316/breakfast-and-brunch/waffles/";
        String category = "Breakfast";

        // ready for next run
        double recipeId = 25;










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

            rand = new Random();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {

            //Document get request stuff?
            try
            {

                int randInt = rand.nextInt(Integer.MAX_VALUE - 2);

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

                // todo: RecipesRating
                CollectionReference dbRecipes = db.collection("RecipesRatingBigInt");

                int i = 0;
                for(Element e: divs)
                {
                    imageList[i] = e.select("img").attr("src");
                    URL url = new URL(imageList[i]);

                    String random = UUID.randomUUID().toString();
                    StorageReference recipeRef = mStorageRef.child("recipeImagesRating/" + random);

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
                                    // int recipeId, String[] categories, String[] directions, String[] ingredients,
                                    //                  String[] nutrition, String imageUrl, String title, double rating, int countRating
                                    ArrayList<String> categories = new ArrayList<>();
                                    categories.add(category);

                                    ArrayList<String> directions = new ArrayList<>();
                                    directions.add(categories.get(0) + " direction 1");
                                    directions.add(categories.get(0) + " direction 2");
                                    directions.add(categories.get(0) + " direction 3");

                                    ArrayList<String> ingredients = new ArrayList<>();
                                    ingredients.add(categories.get(0) + " ingredient 1");
                                    ingredients.add(categories.get(0) + " ingredient 2");
                                    ingredients.add(categories.get(0) + " ingredient 3");

                                    ArrayList<String> nutrition = new ArrayList<>();
                                    nutrition.add(categories.get(0) + " calories");
                                    nutrition.add(categories.get(0) + " protein");
                                    nutrition.add(categories.get(0) + " sodium");

                                    String title = e.select("h3").text();


                                    Recipe recipe = new Recipe(rand.nextInt(Integer.MAX_VALUE - 2), categories, directions, ingredients, nutrition, String.valueOf(uri), title, 0, 0, false);


                                    dbRecipes.add(recipe).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                    {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference)
                                        {
                                            Log.i("firebase storage tag", "recipe added to firestore database with id: " + recipeId);
                                            recipeId++;
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



            //for(int i = 0; i < divs.size(); i++)
            //{
                //findRecipesModel.addItem(titleList[i], bitmapList[i]);
            //}
            //findRecipesAdapter.notifyDataSetChanged();

            progressDialog.dismiss();

        }
    }*/

}