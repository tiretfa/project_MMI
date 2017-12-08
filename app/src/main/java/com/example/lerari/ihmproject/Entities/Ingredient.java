package com.example.lerari.ihmproject.Entities;

import java.io.Serializable;

/**
 * Created by LERARI on 21/11/2017.
 */

public class Ingredient implements Serializable {

    private String ingredient;
    private int media;


    public Ingredient(String ingredient, int media) {
        this.ingredient = ingredient;
        this.media = media;
    }

    public int getMedia() {
        return media;
    }

    public String getIngredient() {
        return ingredient;
    }
}
