package com.example.swapn.whatsfordinner;

/**
 * Created by swapn on 9/16/2016.
 */
public class Receipe {
    private String receipe_name;
    private String receipe_direction;

    public Receipe(String receipe_name, String receipe_direction, String receipe_pic) {
        this.receipe_name = receipe_name;
        this.receipe_direction = receipe_direction;
        this.receipe_pic = receipe_pic;
    }

    public String getReceipe_pic() {
        return receipe_pic;
    }

    public void setReceipe_pic(String receipe_pic) {
        this.receipe_pic = receipe_pic;
    }

    public String getReceipe_direction() {
        return receipe_direction;
    }

    public void setReceipe_direction(String receipe_direction) {
        this.receipe_direction = receipe_direction;
    }

    public String getReceipe_name() {
        return receipe_name;
    }

    public void setReceipe_name(String receipe_name) {
        this.receipe_name = receipe_name;
    }

    private String receipe_pic;
}
