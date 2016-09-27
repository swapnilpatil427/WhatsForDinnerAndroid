package com.example.swapn.whatsfordinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swapn on 9/19/2016.
 */

public class MealClass {
    public static final String NO_MEAL = "Eating Out.";
    private String receipe_breakfast;
    private String receipe_lunch;


    public String getReceipe_dinner() {
        if(receipe_dinner.equals("")) {
            return NO_MEAL;
        } else {
            return receipe_dinner;
        }
    }

    public void setReceipe_dinner(String receipe_dinner) {
        this.receipe_dinner = receipe_dinner;
    }

    public String getReceipe_breakfast() {
        if(receipe_breakfast.equals("")) {
            return NO_MEAL;
        } else {
            return receipe_breakfast;
        }
    }

    public void setReceipe_breakfast(String receipe_breakfast) {
        this.receipe_breakfast = receipe_breakfast;
    }

    public String getReceipe_lunch() {
        if(receipe_lunch.equals("")) {
            return NO_MEAL;
        } else {
            return receipe_lunch;
        }
    }

    public void setReceipe_lunch(String receipe_lunch) {
        this.receipe_lunch = receipe_lunch;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MealClass(Date date, String receipe_breakfast, String receipe_lunch, String receipe_dinner) {
        this.date = date;
        this.receipe_breakfast = receipe_breakfast;

        this.receipe_lunch = receipe_lunch;

        this.receipe_dinner = receipe_dinner;

    }

    private String receipe_dinner;
    private Date date;
}
