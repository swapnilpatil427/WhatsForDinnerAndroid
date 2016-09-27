package com.example.swapn.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewDishctivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_INGREDIENTS = "receipe_ingredients";
    private static final String RECEIPE_IMAGE = "receipe_image";
    private static final String RECEIPE_DIRECTION = "receipe_DIRECTION";
    // Database Helper
    List<Ingredients> ingredientsList;
    String[] ingredients;
    List<Spinner> ingredient_spinners = new ArrayList<Spinner>();
    private int max_ingredients = 10;
    private String imageUri = null;
    private EditText receipeName;
    private EditText receipeDirection;
    private ImageView receipePic;
    private EditText calories;
    private EditText carbohydrates;
    private EditText minerals;
    private EditText vitamins;
    private EditText sugar;
    String receipe_name = "";
    String receipe_dir = "";
    String receipe_pic = "";
    private DatabaseHelper db;
    private List<ReceipeIngredients> receipeIngredientList;
    private TextView  nutrition_calories;
    private TextView  nutrition_carbohydrates;
    private TextView  nutrition_minerals;
    private TextView  nutrition_vitamins;
    private TextView  nutrition_sugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dishctivity);
        db = new DatabaseHelper(getApplicationContext());
        ingredientsList = db.getAllIngredients();
        Intent intent = getIntent();
        receipe_name = intent.getStringExtra("receipe_name");
        receipe_dir = intent.getStringExtra("receipe_dir");
        receipe_pic = intent.getStringExtra("receipe_pic");
        ingredients = new String[ingredientsList.size()];
        int count = 0;
        for(Ingredients ingredient : ingredientsList) {
            ingredients[count] = ingredient.getIngredient_name();
            count++;
        }

        receipeName = (EditText) findViewById(R.id.receipe_name);
        receipeDirection = (EditText) findViewById(R.id.receipe_direction);
        receipePic = (ImageView) findViewById(R.id.receipeImage);
        calories = (EditText) findViewById(R.id.caloriesQuantity);
        carbohydrates = (EditText) findViewById(R.id.carbohydratesQuantity);
        minerals = (EditText) findViewById(R.id.mineralsQuantity);
        vitamins = (EditText) findViewById(R.id.vitaminsQuantity);
        sugar = (EditText) findViewById(R.id.sugarQuantity);
        ImageView selectImage = (ImageView) findViewById(R.id.imageButton2);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
       // spinner.setBackgroundResource(R.drawable.dropdownbackground);
        if(receipe_name == null) {
            addIngredient();
        } else {
            receipeName.setFocusable(false);
            populateFields();
        }
    }

    protected void populateFields() {
        receipeName.setText(receipe_name);
        receipeDirection.setText(receipe_dir);
        if(receipe_pic.equals("default")) {
            receipePic.setImageResource(R.drawable.a);
        } else {
            receipePic.setImageBitmap(BitmapFactory.decodeFile(receipe_pic));
        }
        db = new DatabaseHelper(getApplicationContext());
        receipeIngredientList = db.getReceipeIngredient(receipe_name);

        String[] nutritions = db.getNutritions(receipe_name).split(":");
        nutrition_calories = (TextView) findViewById(R.id.caloriesQuantity);
        nutrition_carbohydrates = (TextView) findViewById(R.id.carbohydratesQuantity);
        nutrition_minerals = (TextView) findViewById(R.id.mineralsQuantity);
        nutrition_vitamins = (TextView) findViewById(R.id.vitaminsQuantity);
        nutrition_sugar = (TextView) findViewById(R.id.sugarQuantity);
        nutrition_calories.setText(nutritions[0]);
        nutrition_carbohydrates.setText(nutritions[1]);
        nutrition_minerals.setText(nutritions[2]);
        nutrition_vitamins.setText(nutritions[3]);
        nutrition_sugar.setText(nutritions[4]);
        for(ReceipeIngredients ingredient : receipeIngredientList) {
            Spinner spinner = new Spinner(this);
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ingredients));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                    // showToast("Spinner2: position=" + pos + " id=" + id);
                    String item = adapterView.getItemAtPosition(pos).toString();

                    // Showing selected spinner item
                    // Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            LinearLayout items_layout = (LinearLayout) findViewById(R.id.itemsLayout);
            items_layout.addView(spinner);
            ingredient_spinners.add(spinner);
            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(ingredient.getIngredient_name());
//set the default according to value
            spinner.setSelection(spinnerPosition);
        }
    }

    protected void selectReceipeImage (View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void addIngredientClick ( View view) {
       addIngredient();
    }
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void addIngredient() {
        if(ingredient_spinners.size() < max_ingredients) {
            Spinner spinner = new Spinner(this);
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ingredients));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                   // showToast("Spinner2: position=" + pos + " id=" + id);
                    String item = adapterView.getItemAtPosition(pos).toString();

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            LinearLayout items_layout = (LinearLayout) findViewById(R.id.itemsLayout);
            items_layout.addView(spinner);
            ingredient_spinners.add(spinner);
            if(ingredient_spinners.size() >= max_ingredients) {
                ImageButton imageButton = (ImageButton) findViewById(R.id.addingredient);
                imageButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void submitReceipeClick(View view) {
       String ingredients = "";
        for(Spinner ingredient_spinner : ingredient_spinners) {
            ingredients += ingredient_spinner.getSelectedItem().toString() + ";";
            //Log.d("spinner",ingredient_spinner.getSelectedItemPosition().getSelectedItem().toString());
            //getSelectedItemPosition
        }


        if(validateReceipe()) {

            Intent intent = new Intent(this, GroceryEditActivity.class);
            intent.putExtra(RECEIPE_NAME, receipeName.getText().toString());
            intent.putExtra(RECEIPE_INGREDIENTS, ingredients);
            String nutrition = calories.getText() + ":" +
                               carbohydrates.getText() + ":" +
                               minerals.getText() + ":" +
                                vitamins.getText() + ":" +
                                sugar.getText();
            if(receipe_name != null) {
                intent.putExtra("edit", "true");
            }
            if(imageUri != null) {
                intent.putExtra(RECEIPE_IMAGE, imageUri);
            } else {
                intent.putExtra(RECEIPE_IMAGE, "default");
            }

            intent.putExtra("nutrition", nutrition);


            intent.putExtra(RECEIPE_DIRECTION, receipeDirection.getText().toString());
            startActivity(intent);
        }
    }

    public boolean validateReceipe() {

        if(db.checkIsReceipeAlreadyInDBorNot(receipeName.getText().toString()) && receipe_name == null) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "Receipe Name Already Exists. Choose Another Name.", duration);
            toast.show();
            return false;
        }

        return true;
    }
    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                Uri imageUriTemp = data.getData();
                imageUri = getPathFromURI(imageUriTemp);
                final InputStream imageStream = getContentResolver().openInputStream(imageUriTemp);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView = (ImageView) findViewById(R.id.receipeImage);

                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                Log.e("error",e.getMessage());
            }
        }
    }
}
