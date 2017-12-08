package com.example.lerari.ihmproject.ItemAdapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lerari.ihmproject.Entities.Etape;
import com.example.lerari.ihmproject.Entities.Recette;
import com.example.lerari.ihmproject.R;

import java.util.ArrayList;


public class RecetteItemAdapter extends ArrayAdapter<Recette> {

    public RecetteItemAdapter(@NonNull Context context, ArrayList<Recette> recettes) {
        super(context, R.layout.activity_recette_item_adapter ,recettes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Recette recette = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_recette_item_adapter, parent, false);
        }

        ImageView recette_image = convertView.findViewById(R.id.recette_img);
        TextView recette_name = convertView.findViewById(R.id.recette_name);
        TextView recette_nbrPersonne = convertView.findViewById(R.id.recette_nbrPersonne);
        TextView recette_duree = convertView.findViewById(R.id.recette_duree);


        recette_image.setImageResource(recette.getImage());
        recette_name.setText(recette.getNom());
        recette_nbrPersonne.setText(String.valueOf(recette.getNbrPersonne()));
        recette_duree.setText(recette.getDuree());



        return convertView;

    }


}






