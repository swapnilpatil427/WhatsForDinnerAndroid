package com.example.swapn.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by swapn on 9/16/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "whatsForDinner";

    // Table Names
    private static final String TABLE_INGREDIENT = "ingredients";
    private static final String TABLE_RECEIPE = "receipes";
    private static final String TABLE_RECEIPE_INGREDIENTS = "receipe_ingredients";
    private static final String TABLE_MEALS = "meals";
    private static final String TABLE_RECEIPE_NUTRITIONS = "nutritions";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Ingredient Table - column nmaes
    private static final String KEY_INGREDIENT = "ingredient_unit";
    private static final String KEY_INGREDIENT_NAME = "ingredient_name";
    private static final String KEY_INGREDIENT_QUANTITY = "ingredient_quantity";

    // Receipes Table - column nmaes
    private static final String KEY_RECEIPE_NAME = "receipe_name";
    private static final String KEY_RECEIPE_DIRECTION = "receipe_direction";
    private static final String KEY_RECEIPE_PIC= "receipe_pic";

    // Meals Table - column nmaes
    private static final String MEAL_DATE = "meal_date";
    private static final String MEAL_BREAKFAST= "meal_breakfast";
    private static final String MEAL_LUNCH= "meal_lunch";
    private static final String MEAL_DINNER= "meal_dinner";

    private static final String RECEIPE_NUTRITION = "receipe_nutritions";





    // Table Create Statements
    // Ingredient table create statement
    private static final String CREATE_TABLE_INGREDIENT = "CREATE TABLE "
            + TABLE_INGREDIENT + "(" + KEY_INGREDIENT + " TEXT , " + KEY_INGREDIENT_NAME
            + " TEXT)";

    // Tag table create statement
    private static final String CREATE_TABLE_RECEIPE = "CREATE TABLE " + TABLE_RECEIPE
            + "(" + KEY_RECEIPE_NAME + " TEXT PRIMARY KEY, " + KEY_RECEIPE_DIRECTION + " TEXT, " + KEY_RECEIPE_PIC + " TEXT)";

    // Tag table create statement
    private static final String CREATE_TABLE_RECEIPE_INGREDIENTS = "CREATE TABLE " + TABLE_RECEIPE_INGREDIENTS
            + "(" + KEY_RECEIPE_NAME + " TEXT," + KEY_INGREDIENT_NAME + " TEXT, " + KEY_INGREDIENT_QUANTITY + " TEXT)";

    private static final String CREATE_TABLE_MEALS = "CREATE TABLE " + TABLE_MEALS
            + "(" + MEAL_DATE + " TEXT," + MEAL_BREAKFAST + " TEXT , " + MEAL_LUNCH + " TEXT , " + MEAL_DINNER + " TEXT)";

    // Tag table create statement
    private static final String CREATE_TABLE_NUTRITION = "CREATE TABLE " + TABLE_RECEIPE_NUTRITIONS
            + "(" + KEY_RECEIPE_NAME + " TEXT PRIMARY KEY, " + RECEIPE_NUTRITION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INGREDIENT);
        db.execSQL(CREATE_TABLE_RECEIPE);
        db.execSQL(CREATE_TABLE_RECEIPE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_MEALS);
        db.execSQL(CREATE_TABLE_NUTRITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RECEIPE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RECEIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MEALS);

        onCreate(db);
    }

    public String createIngredient(Ingredients ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT, ingredient.getIngredient_unit());
        values.put(KEY_INGREDIENT_NAME, ingredient.getIngredient_name());

        // insert row
        long ingredient_id = db.insert(TABLE_INGREDIENT, null, values);
        return ingredient.getIngredient_unit();
    }

    public void insertReceipeIngredient(ReceipeIngredients ingredient_list) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_RECEIPE_NAME, ingredient_list.getReceipe_name());
            values.put(KEY_INGREDIENT_NAME, ingredient_list.getIngredient_name());
            values.put(KEY_INGREDIENT_QUANTITY, ingredient_list.getIngredient_quantity());
            db.insert(TABLE_RECEIPE_INGREDIENTS, null, values);
        } catch (Exception e) {
            Log.e("errorDB", e.getMessage().toString());
        }
    }

    /**
     * getting all tags
     * */
    public List<Ingredients> getAllIngredients() {
        List<Ingredients> IngredientList = new ArrayList<Ingredients>();
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Ingredients t = new Ingredients();
                t.setIngredient_unit(c.getString(c.getColumnIndex(KEY_INGREDIENT)));
                t.setIngredient_name(c.getString(c.getColumnIndex(KEY_INGREDIENT_NAME)));

                // adding to tags list
                IngredientList.add(t);
            } while (c.moveToNext());
        }
        return IngredientList;
    }

    public List<Receipe> getAllReceipes() {
        List<Receipe> receipeList = new ArrayList<Receipe>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Receipe t = new Receipe(c.getString(c.getColumnIndex(KEY_RECEIPE_NAME)),
                                        c.getString(c.getColumnIndex(KEY_RECEIPE_DIRECTION)) ,
                                        c.getString(c.getColumnIndex(KEY_RECEIPE_PIC)));

                receipeList.add(t);
            } while (c.moveToNext());
        }
        return receipeList;
    }

    public List<ReceipeIngredients> getReceipeIngredient(String receipe_name) {
        List<ReceipeIngredients> receipeList = new ArrayList<ReceipeIngredients>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE_INGREDIENTS + " where " +KEY_RECEIPE_NAME + "='" + receipe_name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ReceipeIngredients t = new ReceipeIngredients(c.getString(c.getColumnIndex(KEY_RECEIPE_NAME)),
                        c.getString(c.getColumnIndex(KEY_INGREDIENT_NAME)) ,
                        c.getString(c.getColumnIndex(KEY_INGREDIENT_QUANTITY)));

                receipeList.add(t);
            } while (c.moveToNext());
        }
        return receipeList;
    }

    public boolean updateMealData(Date date, String mealTime, String receipe) {
        try {
           SQLiteDatabase db=this.getWritableDatabase();
            String strSQL = "";
            DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
            String dateTemp = df.format(date).toString().trim();
            ContentValues values=new ContentValues();
            if(mealTime.equals("breakfast"))
                values.put(MEAL_BREAKFAST,receipe);
               // strSQL = "UPDATE " + TABLE_MEALS + " SET " + MEAL_BREAKFAST + "='" + receipe + "'" +" where " + MEAL_DATE + "='" + dateTemp + "'";
            else if(mealTime.equals("lunch")) {
                values.put(MEAL_LUNCH,receipe);
                //strSQL = "UPDATE " + TABLE_MEALS + " SET " + MEAL_LUNCH + "='" + receipe + "'" +" where " + MEAL_DATE + "='" + dateTemp + "'";
            } else {
                values.put(MEAL_DINNER,receipe);
               // strSQL = "UPDATE " + TABLE_MEALS + " SET " + MEAL_DINNER + "='" + receipe + "'" +" where " + MEAL_DATE + "='" + dateTemp + "'";
            }
            int id=db.update(TABLE_MEALS,values,"MEAL_DATE='"+dateTemp+"'",null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public HashMap<String, String> getIngredientCount() {
        List<MealClass> list = this.getMealsForWeek();
        HashMap<String, String> ingredientCount = new HashMap<String,String>();
        List<ReceipeIngredients> listIngredient = new ArrayList<ReceipeIngredients>();
        for(MealClass m : list) {
            listIngredient.clear();
            if(!m.getReceipe_breakfast().equals(MealClass.NO_MEAL)) {
                listIngredient = this.getReceipeIngredient(m.getReceipe_breakfast());
            }
            if(!m.getReceipe_lunch().equals(MealClass.NO_MEAL)) {
                listIngredient.addAll(this.getReceipeIngredient(m.getReceipe_lunch()));
                //hs.add(m.getReceipe_lunch());
            }

            if(!m.getReceipe_dinner().equals(MealClass.NO_MEAL)) {
                listIngredient.addAll(this.getReceipeIngredient(m.getReceipe_dinner()));
                //hs.add(m.getReceipe_dinner());
            }

            for(ReceipeIngredients temp : listIngredient){
                String[] quantity = temp.getIngredient_quantity().split(":");
                if(ingredientCount.containsKey(temp.getIngredient_name())) {
                    String[] pv = ingredientCount.get(temp.getIngredient_name()).split(":");
                    int previousQuantity = Integer.parseInt(pv[0]);
                    ingredientCount.put(temp.getIngredient_name(),Integer.toString(previousQuantity +
                            Integer.parseInt(quantity[0])) + ":" + pv[1]);
                } else {
                    ingredientCount.put(temp.getIngredient_name(), temp.getIngredient_quantity());
                }
                //System.out.println(temp.getReceipe_name() + "   " +temp.getIngredient_name() + "  " + temp.getIngredient_quantity());
            }
        }

        return ingredientCount;
    }

    public List<MealClass> getMealsForWeek() {
        List<MealClass> MealList = new ArrayList<MealClass>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
                Date date = new Date();

                try {
                    String mealDate = c.getString(c.getColumnIndex(MEAL_DATE));
                    date = df.parse(mealDate);
                } catch (ParseException e) {
                    Log.e("errorParseDate", "Error in parsing date database helper");
                }

                MealClass mc = new MealClass(date,
                        c.getString(c.getColumnIndex(MEAL_BREAKFAST)) ,
                        c.getString(c.getColumnIndex(MEAL_LUNCH)),
                        c.getString(c.getColumnIndex(MEAL_DINNER))
                        );

                MealList.add(mc);
            } while (c.moveToNext());
        }
        return MealList;
    }

    public Ingredients getIngredient(String ingredient_name) {
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENT + " where " +KEY_INGREDIENT_NAME + "='" + ingredient_name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list

        if (c != null) {
            c.moveToFirst();
        }

        Ingredients t = new Ingredients();
        t.setIngredient_unit(c.getString(c.getColumnIndex(KEY_INGREDIENT)));
        t.setIngredient_name(c.getString(c.getColumnIndex(KEY_INGREDIENT_NAME)));
        return t;
    }

    public void deleteIngredientTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_INGREDIENT);
    }

    public String createMealPlan(List<MealClass> meal_list) {
        // assigning tags to todo
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_MEALS);
            db = this.getWritableDatabase();
            for(MealClass meal : meal_list) {
                ContentValues values = new ContentValues();
                DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy");
                values.put(MEAL_DATE, df.format(meal.getDate()).toString().trim());
                values.put(MEAL_BREAKFAST, meal.getReceipe_breakfast());
                values.put(MEAL_LUNCH, meal.getReceipe_lunch());
                values.put(MEAL_DINNER, meal.getReceipe_dinner());
                db.insert(TABLE_MEALS, null, values);
            }

            return "done";
        } catch (Exception e) {
            Log.e("errorMealCreation", e.getMessage());
        }

        return "";
    }

    public boolean checkIsReceipeAlreadyInDBorNot(String receipe_name) {
        String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE + " where " +KEY_RECEIPE_NAME + "='" + receipe_name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public String getNutritions(String receipe_name) {
       // String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE_INGREDIENTS + " where " +KEY_RECEIPE_NAME + "='" + receipe_name + "'";
       // SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);


        //System.out.println(cursor.getString(cursor.getColumnIndex(KEY_RECEIPE_NAME)));

        String nut ="";

        String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE_NUTRITIONS + " where " +KEY_RECEIPE_NAME + "='" + receipe_name + "'";
       // String selectQuery = "SELECT  * FROM " + TABLE_RECEIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                nut = c.getString(c.getColumnIndex(RECEIPE_NUTRITION));
               /* Receipe t = new Receipe(c.getString(c.getColumnIndex(KEY_RECEIPE_NAME)),
                        c.getString(c.getColumnIndex(KEY_RECEIPE_DIRECTION)) ,
                        c.getString(c.getColumnIndex(KEY_RECEIPE_PIC))); */
            } while (c.moveToNext());
        }

        return nut;
    }

    public void updateReceipeIngredients(ReceipeIngredients ingredient_list) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_RECEIPE_NAME, ingredient_list.getReceipe_name());
            values.put(KEY_INGREDIENT_NAME, ingredient_list.getIngredient_name());
            values.put(KEY_INGREDIENT_QUANTITY, ingredient_list.getIngredient_quantity());


            db.execSQL("DELETE FROM " + TABLE_RECEIPE_INGREDIENTS + " where receipe_name='"+ingredient_list.getReceipe_name()+"'");
        } catch (Exception e) {
            Log.e("errorDB", e.getMessage().toString());
        }
    }

    public boolean updateReceipeData(Receipe receipe, List<ReceipeIngredients> receipe_ingredient_list) {
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            String strSQL = "";
            ContentValues values=new ContentValues();
            values.put(KEY_RECEIPE_DIRECTION,receipe.getReceipe_direction());
            values.put(KEY_RECEIPE_PIC,receipe.getReceipe_pic());
            int id=db.update(TABLE_RECEIPE,values,"receipe_name='"+receipe.getReceipe_name()+"'",null);
            db.execSQL("DELETE FROM " + TABLE_RECEIPE_INGREDIENTS + " where receipe_name='"+receipe.getReceipe_name()+"'");
            for(ReceipeIngredients receipe_ingredient : receipe_ingredient_list) {
                insertReceipeIngredient(receipe_ingredient);
            }
        } catch (Exception e) {
            Log.e("errorDB", e.getMessage().toString());
            return false;
        }
        return true;
    }

    public void insertNutrition(String receipe_name, String nutrition) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_RECEIPE_NAME, receipe_name);
            values.put(RECEIPE_NUTRITION, nutrition);
            long receipe_id = db.insert(TABLE_RECEIPE_NUTRITIONS, null, values);

        } catch (Exception e) {
            Log.e("errorReceipeCreation", e.getMessage());
        }




    }


    public String createReceipe(Receipe receipe, List<ReceipeIngredients> receipe_ingredient_list, String nutrition) {
        // assigning tags to todo
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_RECEIPE_NAME, receipe.getReceipe_name());
            values.put(KEY_RECEIPE_DIRECTION, receipe.getReceipe_direction());
            values.put(KEY_RECEIPE_PIC, receipe.getReceipe_pic());
            long receipe_id = db.insert(TABLE_RECEIPE, null, values);

            for(ReceipeIngredients receipe_ingredient : receipe_ingredient_list) {
                insertReceipeIngredient(receipe_ingredient);
            }

            insertNutrition(receipe.getReceipe_name(), nutrition);
            return receipe.getReceipe_name();
        } catch (Exception e) {
            Log.e("errorReceipeCreation", e.getMessage());
        }

        return "";
    }


    /**
     * Remove all users and groups from database.
     */
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
      // db.execSQL("DROP TABLE "+TABLE_INGREDIENT);
      //  db.execSQL("DROP TABLE "+TABLE_RECEIPE);
     //   db.delete(DatabaseHelper.TABLE_INGREDIENT, null, null);
     //   db.delete(DatabaseHelper.TABLE_RECEIPE, null, null);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
