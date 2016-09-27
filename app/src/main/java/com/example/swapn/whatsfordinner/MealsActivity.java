package com.example.swapn.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MealsActivity extends AppCompatActivity {
    private List<View> itemViews;
    private RecyclerView rv;
    private List<MealClass> meals = new ArrayList<MealClass>();
    private DatabaseHelper db;
    private String mealDate = "";
    private String mealTime= "";
    private String receipe_name= "";
    private static final String RECEIPE_NAME = "receipe_name";
    public static final String PREFS_NAME = "MyMealsPref";
    String nutrition;
    String remainingnutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        db = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        mealDate = intent.getStringExtra("mealdate");
        mealTime = intent.getStringExtra("mealtime");
        receipe_name = intent.getStringExtra(RECEIPE_NAME);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        nutrition = settings.getString("weeknutrition", "");
        remainingnutrition = settings.getString("remainingnutrition", "");
        int duration = Toast.LENGTH_SHORT;
        if(nutrition.equals("") || nutrition.equals("0:0:0:0:0")) {
            Toast toast = Toast.makeText(getApplicationContext(), "No Weekly Nutrition Plan Set", duration);
            toast.show();
        } else {
            checkNutritionPlan();
        }
        Button setGoals = (Button) findViewById(R.id.btn_setGoals);
        setGoals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NutritionGoalsActivity.class);
                startActivity(intent);
            }
        });
        if(mealDate != null)
            updateData(mealDate, mealTime, receipe_name);
        meals = db.getMealsForWeek();
        if(meals.size() == 0 || checkDataFreshness() ) {
            initializeData();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("weeknutrition", "0:0:0:0:0");
            editor.putString("remainingnutrition", "0:0:0:0:0");

            // Commit the edits!
            editor.commit();
        } else {
            meals = db.getMealsForWeek();
        }

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(meals);
        rv.setAdapter(adapter);
    }


    private int updateData(String mealDate, String mealTime, String receipe_name) {
        if(!mealDate.equals("")) {

            String nutritions = db.getNutritions(receipe_name);

            Date[] days = getDaysOfWeek(new Date(), Calendar.getInstance().getFirstDayOfWeek());
            Date date = stringToDate(mealDate);
            for(Date day: days) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy");
                if(sdf.format(day).equals(sdf.format(date))) {
                    db.updateMealData(date, mealTime, receipe_name);
                }
            }

            checkNutritionPlan();
        }
        return 0;
    }

    private void checkNutritionPlan() {
        int duration = Toast.LENGTH_LONG;
        if(nutrition.equals("") || nutrition.equals("0:0:0:0:0")) {
            Toast toast = Toast.makeText(getApplicationContext(), "No Nutrition Plan Set", duration);
            toast.show();
        } else {
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
            String[] weekplan = nutrition.split(":");
            int remainingCal = Integer.parseInt(weekplan[0]) - totalCal;
            int remainingCarbo = Integer.parseInt(weekplan[1]) - totalCarbo;
            int remainingmin = Integer.parseInt(weekplan[2]) - totalMin;
            int remainingvit = Integer.parseInt(weekplan[3]) - totalVit;
            int remainingCsug = Integer.parseInt(weekplan[4]) - totalSug;
            String message = "";
            boolean toastflag = false;
            if(remainingCal < 0) {
                message += "Calories consumption exceeds for this week plan \n\n";
                toastflag = true;
            }

            if(remainingCarbo < 0) {
                message += "Carbohydrates consumption exceeds for this week plan \n\n";
                toastflag = true;
            }

            if(remainingmin < 0) {
                message += "Minerals consumption exceeds for this week plan \n\n";
                toastflag = true;
            }

            if(remainingvit < 0) {
                message += "Vitamins consumption exceeds for this week plan \n\n";
                toastflag = true;
            }

            if(remainingCsug < 0) {
                message += "Sugar consumption exceeds for this week plan \n\n";
                toastflag = true;
            }

            if(toastflag == true) {
                Toast toast = Toast.makeText(getApplicationContext(), message, duration);
                toast.show();
            }


            //String remainingPlan = Integer.parseInt(weekplan[0] - )
        }
    }

    private boolean checkDataFreshness () {
        Date today = new Date();
        Date[] days = getDaysOfWeek(today, Calendar.getInstance().getFirstDayOfWeek());
        int count = 0;
        for(int i = 0;i < meals.size(); i++) {
            if(today.after(meals.get(i).getDate())) {
                count++;
            }
        }

        if(count > 7) {
            return true;
        } else
            return false;
    }

    private Date stringToDate (String dt ) {
        DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
        Date date = new Date();
        try {
            date = df.parse(dt);
        } catch (ParseException e) {
            Log.e("errorParseDate", "Error in parsing date");
        }

        return date;
    }


    private void initializeData() {
        Date refDate = new Date();
        meals.clear();
        Date[] days = getDaysOfWeek(refDate, Calendar.getInstance().getFirstDayOfWeek());

        for (Date day  : days) {
            MealClass mc = new MealClass(day, "" , "", "");
            meals.add(mc);
        }
        db.createMealPlan(meals);
       // db.createMealPlan(meals);
    }

    private Date[] getDaysOfWeek(Date refDate, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        Date[] daysOfWeek = new Date[7];
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }


    public static class RVAdapter extends RecyclerView.Adapter<RVAdapter.MealsViewHolder> {

        List<MealClass> meals;

        RVAdapter(List<MealClass> ingredients) {
            this.meals = ingredients;
        }
        @Override
        public MealsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meals_card, viewGroup, false);
            MealsViewHolder pvh = new MealsViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(MealsViewHolder holder, int i) {
            DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
           holder.mealDate.setText(df.format(meals.get(i).getDate()));
            holder.receipeBreakfast.setText(meals.get(i).getReceipe_breakfast());
            holder.receipeLunch.setText(meals.get(i).getReceipe_lunch());
            holder.receipeDinner.setText(meals.get(i).getReceipe_dinner());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return meals.size();
        }


        public static class MealsViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView mealBreakfast;
            TextView receipeBreakfast;
            TextView mealLunch;
            TextView receipeLunch;
            TextView mealDinner;
            TextView receipeDinner;
            TextView mealDate;
            LinearLayout breakfast;
            LinearLayout lunch;
            LinearLayout dinner;

            MealsViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                mealBreakfast = (TextView) itemView.findViewById(R.id.meal_breakfast);
                receipeBreakfast = (TextView) itemView.findViewById(R.id.receipe_breakfast);
                mealLunch = (TextView) itemView.findViewById(R.id.meal_lunch);
                receipeLunch = (TextView) itemView.findViewById(R.id.receipe_lunch);
                mealDinner = (TextView) itemView.findViewById(R.id.meal_dinner);
                receipeDinner = (TextView) itemView.findViewById(R.id.receipe_dinner);
                mealDate = (TextView) itemView.findViewById(R.id.mealdate);
                breakfast = (LinearLayout) itemView.findViewById(R.id.breakfast);
                breakfast.setLongClickable(true);
                lunch = (LinearLayout) itemView.findViewById(R.id.lunch);
                dinner = (LinearLayout) itemView.findViewById(R.id.dinner);

                breakfast.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ReceipesActivity.class);
                        intent.putExtra("mealdate", mealDate.getText().toString());
                        intent.putExtra("time", "breakfast");
                        context.startActivity(intent);
                        return true;
                    }
                });

                lunch.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ReceipesActivity.class);
                        intent.putExtra("mealdate", mealDate.getText().toString());
                        intent.putExtra("time", "lunch");
                        context.startActivity(intent);
                        return true;
                    }
                });

                dinner.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ReceipesActivity.class);
                        intent.putExtra("mealdate", mealDate.getText().toString());
                        intent.putExtra("time", "dinner");
                        context.startActivity(intent);
                        return true;
                    }
                });
            }
        }
    }
}
