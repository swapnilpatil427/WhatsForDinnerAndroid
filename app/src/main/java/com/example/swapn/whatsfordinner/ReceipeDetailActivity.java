package com.example.swapn.whatsfordinner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ReceipeDetailActivity extends AppCompatActivity {
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_DIRECTION = "receipe_direction";
    private static final String RECEIPE_PIC = "receipe_pic";
    private DatabaseHelper db;
    private List<ReceipeIngredients> receipeIngredientList;
    private TextView receipe_name;
    private TextView receipe_directions;
    private TextView receipe_ingredients;
    private ImageView receipe_image;
    String receipeName;
    String receipeDirection;
    String receipePic;
    private TextView  nutrition_calories;
    private TextView  nutrition_carbohydrates;
    private TextView  nutrition_minerals;
    private TextView  nutrition_vitamins;
    private TextView  nutrition_sugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe_detail);
        Intent intent = getIntent();
        receipeName = intent.getStringExtra(RECEIPE_NAME);
        receipeDirection = intent.getStringExtra(RECEIPE_DIRECTION);
        receipePic = intent.getStringExtra(RECEIPE_PIC);
        initialize();

    }

    private void initialize () {
        db = new DatabaseHelper(getApplicationContext());
        receipeIngredientList = db.getReceipeIngredient(receipeName);
        String[] nutritions = db.getNutritions(receipeName).split(":");
        receipe_name = (TextView) findViewById(R.id.receipe_name);
        receipe_directions = (TextView) findViewById(R.id.receipe_direction);
        receipe_ingredients = (TextView) findViewById(R.id.receipe_ingredient);
        receipe_image = (ImageView) findViewById(R.id.receipe_pic);
        nutrition_calories = (TextView) findViewById(R.id.caloriesQuantity);
        nutrition_carbohydrates = (TextView) findViewById(R.id.carbohydratesQuantity);
        nutrition_minerals = (TextView) findViewById(R.id.mineralsQuantity);
        nutrition_vitamins = (TextView) findViewById(R.id.vitaminsQuantity);
        nutrition_sugar = (TextView) findViewById(R.id.sugarQuantity);
        receipe_image.setImageBitmap(BitmapFactory.decodeFile(receipePic));
        receipe_name.setText(receipeName);
        receipe_directions.setText(receipeDirection);
        nutrition_calories.setText(nutritions[0] + " cal");
        nutrition_carbohydrates.setText(nutritions[1] + " unit");
        nutrition_minerals.setText(nutritions[2] + " unit");
        nutrition_vitamins.setText(nutritions[3] + " unit");
        nutrition_sugar.setText(nutritions[4] + " unit");

        //setPicture(receipe_image, receipePic);
        String ingredientText = "";
        for(ReceipeIngredients receipeIngredient : receipeIngredientList) {
            String[] temp = receipeIngredient.getIngredient_quantity().split(":");
            ingredientText += "* "
                    + receipeIngredient.getIngredient_name()
                    + " (" + temp[0] + " " + temp[1] + ") "
                    + System.getProperty ("line.separator")
                    + System.getProperty ("line.separator");
        }
       receipe_ingredients.setText(ingredientText);
    }

    protected void setPicture(ImageView iv, String uriString) {
        try {
            Uri imageUri = Uri.parse(uriString);
            final InputStream imageStream =getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            iv.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            Log.e("errorReceipeDetail",e.getMessage());
        }
    }
}
