package com.example.lerari.ihmproject.Entities;

import java.io.Serializable;

/**
 * Created by LERARI on 21/11/2017.
 */

public class Etape implements Serializable {


    private String etape ;
    private int media;
    private int durreEtape; // en seconde


    public Etape(String etape, int media, int durreEtape) {
        this.etape = etape;
        this.media = media;
        this.durreEtape = durreEtape;
    }

    public int getMedia() {
        return media;
    }

    public int getDurreEtape() {
        return durreEtape;
    }

    public String getEtape() {
        return etape;
    }


}
