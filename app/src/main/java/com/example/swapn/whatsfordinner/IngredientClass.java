package com.example.swapn.whatsfordinner;

/**
 * Created by swapn on 9/17/2016.
 */
public class IngredientClass {
    private String ingredient_name;
    private String ingredient_unit;
    private int ingredient_quantity;

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_unit() {
        return ingredient_unit;
    }

    public void setIngredient_unit(String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
    }

    public int getIngredient_quantity() {
        return ingredient_quantity;
    }

    public void setIngredient_quantity(int ingredient_quantity) {
        this.ingredient_quantity = ingredient_quantity;
    }

    IngredientClass(String ingredient_name, String ingredient_unit, int ingredient_quantity) {
        this.ingredient_name = ingredient_name;
        this.ingredient_unit = ingredient_unit;
        this.ingredient_quantity = ingredient_quantity;
    }
}
