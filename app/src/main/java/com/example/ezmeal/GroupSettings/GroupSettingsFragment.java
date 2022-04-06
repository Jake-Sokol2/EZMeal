package com.example.ezmeal.GroupSettings;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ezmeal.Login.LoginActivity;
import com.example.ezmeal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

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

    private FirebaseFirestore db;
    private StorageReference mStorageRef;

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

        try
        {
            AssetManager assetManager = getContext().getAssets();
            InputStream inputStream = getContext().getAssets().open("cookies2.jpg");
            Drawable d = Drawable.createFromStream(inputStream, null);
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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
        //private Document doc = Jsoup.connect("https://cookstre.com").get();
        //String url = "https://firebase.google.com/";
        String urlText = "https://www.allrecipes.com/recipes/248/main-dish/burgers/";
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
                                    categories.add("Burgers");
                                    ArrayList<String> directions = new ArrayList<>();
                                    directions.add("Burgers direction 1");
                                    directions.add("Burgers direction 2");
                                    directions.add("Burgers direction 3");

                                    ArrayList<String> ingredients = new ArrayList<>();
                                    ingredients.add("Cow");
                                    ingredients.add("Burgers ingredient 2");
                                    ingredients.add("Burgers ingredient 3");

                                    ArrayList<String> nutrition = new ArrayList<>();
                                    nutrition.add("Burgers calories");
                                    nutrition.add("Burgers protein");
                                    nutrition.add("Burgers sodium");

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



            //for(int i = 0; i < divs.size(); i++)
            //{
                //findRecipesModel.addItem(titleList[i], bitmapList[i]);
            //}
            //findRecipesAdapter.notifyDataSetChanged();

            progressDialog.dismiss();

        }
    }
    */
}