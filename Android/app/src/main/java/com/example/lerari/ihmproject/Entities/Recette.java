package com.example.lerari.ihmproject.Entities;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by LERARI on 21/11/2017.
 */

public class Recette implements Serializable {

    private String nom;
    private int nbrPersonne;
    private String duree;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Etape> etapes;
    private int image;

    public Recette(String nom, int nbrPersonne, String duree, ArrayList<Ingredient> ingredients, ArrayList<Etape> etapes, int image) {
        this.nom = nom;
        this.nbrPersonne = nbrPersonne;
        this.duree = duree;
        this.ingredients = ingredients;
        this.etapes = etapes;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public String getDuree() {
        return duree;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Etape> getEtapes() {
        return etapes;
    }

    public int getImage() {
        return image;
    }
}
