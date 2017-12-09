package com.example.lerari.ihmproject.Entities;

import java.io.Serializable;

/**
 * Created by LERARI on 21/11/2017.
 */

public class Etape implements Serializable {


    private String etape ;
    private int media;
    private int dureeEtape; // Fabien : supprimer duréeEtape


    public Etape(String etape, int media, int dureeEtape) {
        this.etape = etape;
        this.media = media;
        this.dureeEtape = dureeEtape;
    }

    public int getMedia() {
        return media;
    }

    public int getDureeEtape() {
        return dureeEtape;
    }

    public String getEtape() {
        return etape;
    }


}
