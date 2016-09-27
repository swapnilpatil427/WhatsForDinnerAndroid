package com.example.swapn.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.swapn.whatsfordinner.fragments.ReceipeDetailFragment;
import com.example.swapn.whatsfordinner.fragments.ReceipeFragment;

public class ReceipeActivity extends AppCompatActivity implements ReceipeDetailFragment.OnFragmentInteractionListener, ReceipeFragment.OnFragmentInteractionListener{
    private boolean mTwoPane;
    private static final String RECEIPE_NAME = "receipe_name";
    private static final String RECEIPE_DIRECTION = "receipe_direction";
    private static final String RECEIPE_PIC = "receipe_pic";

    public static final String PREFS_NAME = "MyMealsPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.receipe_detail_container) != null) {
            mTwoPane = true;
            ReceipeDetailFragment fragment = new ReceipeDetailFragment();
            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.receipe_detail_container, fragment).commit();
        }

    }

    public void onDetailsReceipe(String receipe_name, String receipe_direction, String receipe_pic){
        if (mTwoPane) {
            ReceipeDetailFragment rdf = new ReceipeDetailFragment();
            Bundle args = new Bundle();
            args.putString(RECEIPE_NAME, receipe_name);
            args.putString(RECEIPE_DIRECTION, receipe_direction);
            args.putString(RECEIPE_PIC, receipe_pic);
            rdf.setArguments(args);
//Inflate the fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.receipe_detail_container, rdf).commit();

        } else {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            String meal_receipes = settings.getString("meal_receipes", "");
            String meal_receipes_dir = settings.getString("meal_receipes_dir", "");
            String meal_receipes_pic = settings.getString("meal_receipes_pic", "");
            editor.putString("meal_receipes", meal_receipes + ":" +receipe_name);
            editor.putString("meal_receipes_dir", meal_receipes_dir + ":" +receipe_direction);
            editor.putString("meal_receipes_pic", meal_receipes_pic + ":" +receipe_pic);

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), receipe_name + " Added to meal plan selection", duration);
            toast.show();
            // Commit the edits!
            editor.commit();
            //Context context = getApplicationContext();
            Intent intent = new Intent(this, ReceipeDetailActivity.class);
            intent.putExtra(RECEIPE_NAME, receipe_name);
            intent.putExtra(RECEIPE_DIRECTION, receipe_direction);
            intent.putExtra(RECEIPE_PIC, receipe_pic);
            startActivity(intent);
        }

    }
}
