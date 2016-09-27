package com.example.swapn.whatsfordinner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NutritionGoalsActivity extends AppCompatActivity {
    private EditText calories;
    private EditText carbohydrates;
    private EditText minerals;
    private EditText vitamins;
    private EditText sugar;
    private TextView caloriest;
    private TextView carbohydratest;
    private TextView mineralst;
    private TextView vitaminst;
    private TextView sugart;
    private DatabaseHelper db;
    public static final String PREFS_NAME = "MyMealsPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_goals);
        db = new DatabaseHelper(getApplicationContext());
        calories = (EditText) findViewById(R.id.caloriesQuantity);
        carbohydrates = (EditText) findViewById(R.id.carbohydratesQuantity);
        minerals = (EditText) findViewById(R.id.mineralsQuantity);
        vitamins = (EditText) findViewById(R.id.vitaminsQuantity);
        sugar = (EditText) findViewById(R.id.sugarQuantity);
        caloriest = (TextView) findViewById(R.id.caloriesweekQuantity);
        carbohydratest = (TextView) findViewById(R.id.carbohydratesweekQuantity);
        mineralst = (TextView) findViewById(R.id.mineralsweekQuantity);
        vitaminst = (TextView) findViewById(R.id.vitaminsweekQuantity);
        sugart = (TextView) findViewById(R.id.sugarweekQuantity);
        Button setGoals = (Button) findViewById(R.id.btn_setGoals);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String nutrition = settings.getString("weeknutrition", "");
        setGoals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nutrition = calories.getText() + ":" +
                        carbohydrates.getText() + ":" +
                        minerals.getText() + ":" +
                        vitamins.getText() + ":" +
                        sugar.getText();

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("weeknutrition", nutrition);
                editor.putString("remainingnutrition", nutrition);

                // Commit the edits!
                editor.commit();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), "Nutrition Plan Set for this week.", duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MealsActivity.class);
                startActivity(intent);
            }
        });
        if(!nutrition.equals("")) {
            String[] nutritions = nutrition.split(":");
            calories.setText(nutritions[0]);
            carbohydrates.setText(nutritions[1]);
            minerals.setText(nutritions[2]);
            vitamins.setText(nutritions[3]);
            sugar.setText(nutritions[4]);
            checkNutritionPlan();
        }

    }


    private void checkNutritionPlan() {
        int duration = Toast.LENGTH_LONG;
            List<MealClass> meal =  db.getMealsForWeek();
            int totalCal = 0;
            int totalCarbo = 0;
            int totalMin = 0;
            int totalVit = 0;
            int totalSug = 0;
            for(MealClass mc : meal) {
                if(!mc.getReceipe_breakfast().equals(MealClass.NO_MEAL)) {
                    String[] nut = db.getNutritions(mc.getReceipe_breakfast()).split(":");
                    totalCal += Integer.parseInt(nut[0]);
                    totalCarbo += Integer.parseInt(nut[1]);
                    totalMin += Integer.parseInt(nut[2]);
                    totalVit += Integer.parseInt(nut[3]);
                    totalSug += Integer.parseInt(nut[4]);
                }

                if(!mc.getReceipe_lunch().equals(MealClass.NO_MEAL)) {
                    String[] nut = db.getNutritions(mc.getReceipe_lunch()).split(":");
                    totalCal += Integer.parseInt(nut[0]);
                    totalCarbo += Integer.parseInt(nut[1]);
                    totalMin += Integer.parseInt(nut[2]);
                    totalVit += Integer.parseInt(nut[3]);
                    totalSug += Integer.parseInt(nut[4]);
                }
                if(!mc.getReceipe_dinner().equals(MealClass.NO_MEAL)) {
                    String[] nut = db.getNutritions(mc.getReceipe_dinner()).split(":");
                    totalCal += Integer.parseInt(nut[0]);
                    totalCarbo += Integer.parseInt(nut[1]);
                    totalMin += Integer.parseInt(nut[2]);
                    totalVit += Integer.parseInt(nut[3]);
                    totalSug += Integer.parseInt(nut[4]);
                }
            }

        caloriest.setText(Integer.toString(totalCal));
        carbohydratest.setText(Integer.toString(totalCarbo));
        mineralst.setText(Integer.toString(totalMin));
        vitaminst.setText(Integer.toString(totalVit));
        sugart.setText(Integer.toString(totalSug));
        }


}
