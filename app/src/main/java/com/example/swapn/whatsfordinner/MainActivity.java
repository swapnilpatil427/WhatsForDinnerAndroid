package com.example.swapn.whatsfordinner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button popupButton, insidePopupButton;
    TextView popupText;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    // Database Helper
    DatabaseHelper db;
    public static final String PREFS_NAME = "MyMealsPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        popupInit();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("granted");
                    //receipe_image = (ImageView) findViewById(R.id.receipe_pic);
                    //setPicture(receipe_image, receipePic);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void logoClick(View view) {
        final ImageView btnOpenPopup = (ImageView) findViewById(R.id.logo);
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_app_details, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }});
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(btnOpenPopup, -400, 10);

    }

    public void newDishClick (View view) {
         Intent intent = new Intent(this, NewDishctivity.class);
         startActivity(intent);
    }

    public void receipeClick(View view) {
        Intent intent = new Intent(this, ReceipeActivity.class);
        startActivity(intent);
    }

    public void mealsClick(View view) {
        Intent intent = new Intent(this, MealsActivity.class);
        startActivity(intent);
    }

    public void groceryClick(View view) {
        Intent intent = new Intent(this, GroceryShoppingActivity.class);
        startActivity(intent);
    }

    public void reset() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        String meal_receipes = settings.getString("meal_receipes", "");
        String meal_receipes_dir = settings.getString("meal_receipes_dir", "");
        String meal_receipes_pic = settings.getString("meal_receipes_pic", "");
        editor.putString("meal_receipes", "");
        editor.putString("meal_receipes_dir", "");
        editor.putString("meal_receipes_pic", "");
        editor.putString("weeknutrition", "0:0:0:0:0");
        editor.putString("remainingnutrition", "0:0:0:0:0");
        editor.commit();

        getApplicationContext().deleteDatabase("whatsForDinner");
    }


    public void init() {
       // popupButton = (Button) findViewById(R.id.popupbutton);
        popupText = new TextView(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        insidePopupButton.setText("OK");
        popupText.setText("This is Popup Window.press OK to dismiss it.");
        popupText.setPadding(0, 0, 0, 20);
        layoutOfPopup.setOrientation(LinearLayout.HORIZONTAL);
        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(insidePopupButton);

        //Insert Ingredients

        //reset();

        db = new DatabaseHelper(getApplicationContext());
       // db.removeAll();
       // db.onUpgrade(db, 1 , 1);
        db.deleteIngredientTable();
        String[][] ingredientsList = {{"Tomato","lbs"},{"Onions","lbs"} ,{ "Chicken","piece"} , {"Olive Oil","Tea Spoon"}, {"eggs","units"}, {"Potato", "lbs"}, {"Garlic", "piece"}};
        for(int i = 0; i< ingredientsList.length; i++) {
            Ingredients ingredient = new Ingredients(ingredientsList[i][0], ingredientsList[i][1]);
            db.createIngredient(ingredient);
        }


        db.closeDB();
    }

    public void popupInit() {
       // popupButton.setOnClickListener(this);
      //  insidePopupButton.setOnClickListener(this);
        popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
