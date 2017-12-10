package com.example.lerari.ihmproject;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.lerari.ihmproject.Activities.EtapeActivity;
import com.example.lerari.ihmproject.Entities.Etape;
import com.example.lerari.ihmproject.Entities.Ingredient;
import com.example.lerari.ihmproject.Entities.Recette;
import com.example.lerari.ihmproject.ItemAdapters.RecetteItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Recette> listRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        // Construct the data source
        listRecette = generateRecette();

        // Create the adapter to convert the array to views
        RecetteItemAdapter recetteAdapter = new RecetteItemAdapter(this, listRecette);

        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(recetteAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i= new Intent(MainActivity.this, EtapeActivity.class);
                i.putExtra("recette", listRecette.get(position));
                startActivity(i);

            }
        });



    }


    private ArrayList<Recette> generateRecette(){
        ArrayList<Recette> recettes = new ArrayList<Recette>();
        ArrayList<Etape> etapes = new ArrayList<Etape>();
        ArrayList<Ingredient> ingredients= new ArrayList<Ingredient>();

        /* Fabien : supprimer duréeEtape des objets Etape
        etapes.add(new Etape("couper les courgettes ", R.drawable.img1, 2000)) ;
        etapes.add(new Etape("couper les carottes", R.drawable.img2, 2000));
        etapes.add( new Etape( "couper la viande en petit morceaux", R.drawable.img3, 2000));
        etapes.add( new Etape( "mettre le tout dans une casserole", R.drawable.img4, 2000));
        */
        etapes.add(new Etape("taillez les poivron en dès", R.drawable.poivron,2000));
        etapes.add(new Etape("coupez les oignons en dès", R.drawable.oignon,2000));




        ingredients.add(new Ingredient("courgette", R.drawable.ing2));
        ingredients.add(new Ingredient("carotte", R.drawable.ing4));
        ingredients.add(new Ingredient("viande", R.drawable.ing3));




        recettes.add(new Recette("Viande", 3, "30 min", ingredients, etapes, R.drawable.image50));
        recettes.add(new Recette("poulet roti", 3, "40 min", ingredients, etapes, R.drawable.image51));
        recettes.add(new Recette("poulet roti", 3, "50 min", ingredients, etapes, R.drawable.image49));
        recettes.add(new Recette("Salade", 3, "40 min", ingredients, etapes, R.drawable.image52));
        recettes.add(new Recette("poulet roti", 3, "12 min", ingredients, etapes, R.drawable.image46));
        recettes.add(new Recette("poulet roti", 3, "15 min", ingredients, etapes, R.drawable.image48));
        recettes.add(new Recette("Soupe", 3, "40 min", ingredients, etapes, R.drawable.image43));


        return recettes;
    }

}
