package com.example.swapn.whatsfordinner;

/**
 * Created by swapn on 9/16/2016.
 */
public class Ingredients {
    private String ingredient_name;

    public String getIngredient_unit() {
        return ingredient_unit;
    }

    public void setIngredient_unit(String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
    }

    private String ingredient_unit;


    public Ingredients()
    {

    }
    public Ingredients(String ingredient_name, String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

}
