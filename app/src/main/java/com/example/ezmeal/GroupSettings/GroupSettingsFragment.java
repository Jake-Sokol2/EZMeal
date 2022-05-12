package com.example.ezmeal.GroupSettings;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.util.StringUtil;

import com.example.ezmeal.FindRecipes.FindRecipesModels.RecipeClicks;
import com.example.ezmeal.FindRecipes.Recipe;
import com.example.ezmeal.Login.LoginActivity;
import com.example.ezmeal.R;
import com.example.ezmeal.roomDatabase.Identifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
    public String[] urlList;

    DatabaseReference realtimeDb = FirebaseDatabase.getInstance().getReference();




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




        // click on forget password text


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

        Button deleteBtn = (Button) rootView.findViewById(R.id.deleteAccountbtn);
        // click on forget password text
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //deleteBtn.setEnabled(false);
                //deleteUser();
                //Log.i("TAG", "anything");
                //openActivityLogin();
                showRecoverPasswordDialog();
            }
        });
        //int randomNum = ThreadLocalRandom.current().nextInt(1, 24);

        /*int randomNum = new Random().nextInt(10) + 1;
        Log.i("myRand", String.valueOf(randomNum));*/

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long test = cal.getTimeInMillis();
        Date dateBeginningOfWeek = cal.getTime();
        Log.i("a", String.valueOf(test));

        long a = new Date().getTime();
        Log.i("now", String.valueOf(a));

        //realtimeDb.child("Recipes").child("Week").child("1").child("RecipeId").child("0").child("numClicks").setValue(ServerValue.increment(1));

        /*realtimeDb.child("Recipes").child("0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    Log.i("a", String.valueOf(task.getResult().getValue()));
                    RecipeClicks rc = task.getResult().getValue(RecipeClicks.class);

                    Log.i("a", String.valueOf("recipeId " + rc.getRecipeId()));
                    Log.i("a", String.valueOf("numClicked " + rc.getNumClicked()));
                    Log.i("a", String.valueOf("week " + rc.getWeek()));


                }
                else
                {
                    Log.i("a", "error getting realtime data");
                }
            }
        });
*/
      /*  long cutoff = test;
        Query updateRecipeClicks = realtimeDb.child("Recipes").orderByChild("recipeId").equalTo(0);
        updateRecipeClicks.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot itemSnapshot: snapshot.getChildren())
                {
                    //itemSnapshot.getRef().removeValue();
                    if (itemSnapshot.ge< 1)
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                throw error.toException();
            }
        });*/



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


    public void deleteUser() {
        // [START delete_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "User account deleted.");
                        }
                    }
                });
        // [END delete_user]
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

        AsyncClass ac = new AsyncClass();
        ac.execute();

    }

    private void showRecoverPasswordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Delete Your Account");
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        final TextView deleteQuestion = new TextView(this.getContext());

        // write the email using which you registered
        deleteQuestion.setText("Are you sure? This will delete your account");
        deleteQuestion.setTextSize(18);
        //deleteQuestion.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(deleteQuestion);
        linearLayout.setPadding(10, 20, 10, 20);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
                openActivityLogin();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



    public class AsyncClass extends AsyncTask<Void, Void, Void>
    {
        private Random rand;
        //private Document doc = Jsoup.connect("https://cookstre.com").get();
        //String url = "https://firebase.google.com/";
        // NOT READY
        String urlText = "https://www.allrecipes.com/recipes/1316/breakfast-and-brunch/waffles/";
        //String category = "Breakfast";
        ArrayList<String> categoryList = new ArrayList<>();
        // ready for next run
        double recipeId = 25;
        Elements divs;
        Elements individialDivs;
        ProgressDialog progressDialog;
        private Context ctx;
        public Bitmap bitmap;
        public String title;
        public String imgSrc;
        public String imgUrl;
        TextView textView;
        private View view;
        private Map<String, Object> recipe;

        AsyncClass()
        {
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            rand = new Random();
            //progressDialog = new ProgressDialog(getActivity());
            //progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            //Document get request stuff?
            try
            {
                categoryList.add("Waffles");
                categoryList.add("Breakfast");

                int randInt = rand.nextInt(Integer.MAX_VALUE - 2);
                //Connect to website
                Document doc = Jsoup.connect(urlText).get();
                // this "Breakfast and Brunch" page has two sets of div classes - one containing the 12 staff picks recipes, the second containing 24 "More Breakfast and Brunch"
                // recipes at the bottom of the screen.  We get the first of the two divs and ignore the second
                Element content = doc.getElementsByClass("category-page-list-content category-page-list-content__recipe-card karma-main-column").get(0);
                divs = content.getElementsByClass("component card card__category");
                individialDivs = content.getElementsByClass("card__imageContainer");

                imageList = new String[divs.size()];
                bitmapList = new Bitmap[divs.size()];
                titleList = new String[divs.size()];
                urlList = new String[individialDivs.size()];
                mStorageRef = FirebaseStorage.getInstance().getReference();
                db = FirebaseFirestore.getInstance();
                // todo: RecipesRating
                CollectionReference dbRecipes = db.collection("Recipes");
                int i = 0;
                for(Element e: divs)
                {
                    urlList[i] = e.select("a").attr("href");
                    Log.i("urlList", String.valueOf(urlList[i]));
                    i++;

                }
                //urlList.length

                for (int iterator = 0; iterator < urlList.length; iterator++)
                {
                    // urlList[iterator]
                    Document newDoc = Jsoup.connect(urlList[iterator]).get();
                    Element contentSpecificPage = newDoc.getElementsByClass("recipe-container two-col-container").get(0);
                    Element divSpecificPage;

                    if (contentSpecificPage.getElementsByClass("lead-content-aside-wrapper video-with-tout-image").size() > 0)
                    {
                        divSpecificPage = contentSpecificPage.getElementsByClass("lead-content-aside-wrapper video-with-tout-image").get(0);
                    }
                    else
                    {
                        divSpecificPage = contentSpecificPage.getElementsByClass("primary-media-section primary-media-with-filmstrip").get(0);
                    }

                    Element divTitle = contentSpecificPage.getElementsByClass("intro article-info").get(0);

                    imageList[iterator] = divSpecificPage.select("img").attr("src");
                    Log.i("imageList", String.valueOf(imageList[iterator]));

                    String title = contentSpecificPage.select("h1").text();
                    Log.i("title", String.valueOf(title));

                    Element divIngredients = contentSpecificPage.getElementsByClass("recipe-shopper-wrapper").get(0);
                    Elements ingredientLis = divIngredients.getElementsByClass("ingredients-item");

                    ArrayList<String> ingredientList = new ArrayList<>();
                    for (Element element: ingredientLis)
                    {
                        String ingredient = element.select("span").first().text();
                        ingredientList.add(ingredient);
                        Log.i("ingredient", String.valueOf(ingredient));
                    }
                    Log.i("ingredient", String.valueOf(""));

                    Element classDirections = contentSpecificPage.getElementsByClass("instructions-section__fieldset non-visual-fieldset").get(0);
                    Elements directionLis = classDirections.getElementsByClass("subcontainer instructions-section-item");

                    ArrayList<String> directionsList = new ArrayList<>();
                    for (Element element: directionLis)
                    {
                        String direction = element.select("p").text();
                        directionsList.add(direction);
                        Log.i("direction", String.valueOf(direction));
                    }
                    //og.i("direction", String.valueOf(""));

                    Element classNutrition = contentSpecificPage.getElementsByClass("recipeNutritionSectionBlock").get(0);
                    //Element directionLis = classNutrition.getElementsByClass("subcontainer instructions-section-item");




                    String nutrition = classNutrition.select("div[class=section-body]").text();
                    Log.i("nutrition", nutrition);

                    //nutrition = nutrition.split("\\.", 2)[0];
                    // remove the ". Full Ingredients" from the end
                    nutrition = nutrition.substring(0, nutrition.lastIndexOf("."));
                    //Log.i("nutrition2", nutrition2);

                    List<String> nutritionList = new ArrayList<>();
                    // split into individual items
                    nutritionList = Arrays.asList(nutrition.split("; "));

                    for(int q = 0; q < nutritionList.size(); q++)
                    {
                        Log.i("individual nutrition item", String.valueOf(nutritionList.get(q)));
                    }

                    String calories = "";
                    String protein = "";
                    String carbohydrates = "";
                    String fat = "";
                    String cholesterol = "";
                    String sodium = "";
                    for (String s: nutritionList)
                    {
                        if (s.contains("calories"))
                        {
                            calories = s.substring(0, s.lastIndexOf(" "));
                            //break;
                        }
                        else if (s.contains("protein"))
                        {
                            protein = s.substring(s.indexOf(" ") + 1);
                            protein.trim();
                            Log.i("individual protein", String.valueOf(protein));
                            //break;
                        }
                        else if (s.contains("carbohydrates"))
                        {
                            carbohydrates = s.substring(s.indexOf(" ") + 1);
                            carbohydrates.trim();
                            Log.i("individual protein", String.valueOf(protein));
                           //break;
                        }
                        else if (s.contains("fat"))
                        {
                            fat = s.substring(s.indexOf(" ") + 1);
                            fat.trim();
                            Log.i("individual protein", String.valueOf(protein));
                            //break;
                        }
                        else if (s.contains("cholesterol"))
                        {
                            cholesterol = s.substring(s.indexOf(" ") + 1);
                            cholesterol.trim();
                            Log.i("individual protein", String.valueOf(protein));
                        }
                        else if (s.contains("sodium"))
                        {
                            sodium = s.substring(s.indexOf(" ") + 1);
                            sodium.trim();
                            Log.i("individual protein", String.valueOf(protein));
                        }
                    }

                    /*Recipe recipe = new Recipe(rand.nextInt(Integer.MAX_VALUE - 2),
                            categoryList, directionsList, ingredientList, "",
                            title, 0, 0, false,
                            calories, protein, carbohydrates, fat, cholesterol, sodium);*/

                    recipe = new HashMap<>();
                    recipe.put("recipeId", rand.nextInt(Integer.MAX_VALUE - 2));
                    recipe.put("categories", categoryList);
                    recipe.put("directions", directionsList);
                    recipe.put("ingredients", ingredientList);
                    recipe.put("title", title);
                    recipe.put("rating", 0);
                    recipe.put("countRating", 0);
                    recipe.put("highlyRated", false);
                    recipe.put("calories", calories);
                    recipe.put("protein", protein);
                    recipe.put("carbohydrates", carbohydrates);
                    recipe.put("fat", fat);
                    recipe.put("cholesterol", cholesterol);
                    recipe.put("sodium", sodium);


                    URL url = new URL(imageList[iterator]);
                    String random = UUID.randomUUID().toString();
                    StorageReference recipeRef = mStorageRef.child("recipeImages/" + random);
                    InputStream stream = url.openConnection().getInputStream();
                    UploadTask uploadTask = recipeRef.putStream(stream);
                    uploadTask.addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.i("firebase storage tag", "IMAGE INSERT FAILED!");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Log.i("firebase storage tag", "image insert succeeded");
                            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {

                                    recipe.put("imageUrl", String.valueOf(uri));
                                    //recipe.setImageUrl(String.valueOf(uri));
                                    // int recipeId, String[] categories, String[] directions, String[] ingredients,
                                    //                  String[] nutrition, String imageUrl, String title, double rating, int countRating

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
                    //bitmapList[i] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    //titleList[i] = e.select("h3").text();
                    Thread.sleep(15000);

                    i++;
                }

                    /*Map<String, String> identifiers = new HashMap<>();
                    identifiers.put("calories", "calories");
                    identifiers.put("protein", "protein");
                    identifiers.put("carbohydrates", "carbohydrates");
                    identifiers.put("fat", "fat");
                    identifiers.put("cholesterol", "cholesterol");
                    identifiers.put("sodium", "sodium");

                    List<String> substringList = new ArrayList<String>();
                    List<String> categoryList = new ArrayList<String>();

                    List<Identifier> identifierList = new ArrayList<Identifier>();

                    for (Map.Entry<String, String> entry : identifiers.entrySet()) {
                        substringList.add(entry.getValue());
                        categoryList.add(entry.getKey());
                    }

                    for (int i = 0; i < identifiers.size(); i++) {
                        Identifier newIdentifier = new Identifier(substringList.get(i), categoryList.get(i), false);
                        identifierList.add(newIdentifier);
                        //categoryList.add(newCategory);
                    }*/

  //                  Log.i("urlList", String.valueOf(urlList[iterator]));
    //                Log.i("imageList", String.valueOf(imageList[iterator]));
                    /*for (Element e: divsSpecificPage)
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
                    }*/

                }
            catch (MalformedURLException malformedURLException)
            {
                malformedURLException.printStackTrace();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


            // original working code

                /*int randInt = rand.nextInt(Integer.MAX_VALUE - 2);
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
                CollectionReference dbRecipes = db.collection("RecipesRatingFull");
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
                    i++;*/

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
            Log.i("asynctask", "FINISHED");
            //progressDialog.dismiss();
        }
    }
}