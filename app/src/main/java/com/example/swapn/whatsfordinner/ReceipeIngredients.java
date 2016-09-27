package com.example.swapn.whatsfordinner;

/**
 * Created by swapn on 9/18/2016.
 */
public class ReceipeIngredients {
    public ReceipeIngredients(String receipe_name, String ingredient_name, String ingredient_quantity) {
        this.ingredient_name = ingredient_name;
        this.receipe_name = receipe_name;
        this.ingredient_quantity = ingredient_quantity;
    }

    private String receipe_name;

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getReceipe_name() {
        return receipe_name;
    }

    public void setReceipe_name(String receipe_name) {
        this.receipe_name = receipe_name;
    }

    public String getIngredient_quantity() {
        return ingredient_quantity;
    }

    public void setIngredient_quantity(String ingredient_quantity) {
        this.ingredient_quantity = ingredient_quantity;
    }

    private String ingredient_name;
    private String ingredient_quantity;
}
