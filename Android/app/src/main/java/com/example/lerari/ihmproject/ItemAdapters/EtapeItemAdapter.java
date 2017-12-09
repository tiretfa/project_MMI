package com.example.lerari.ihmproject.ItemAdapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lerari.ihmproject.Entities.Etape;
import com.example.lerari.ihmproject.R;

import java.util.ArrayList;

public class  EtapeItemAdapter extends ArrayAdapter<Etape> {

    public EtapeItemAdapter(Context context, ArrayList<Etape> etapes) {

        super(context, 0, etapes);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Etape etape = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_etape_item_adapter, parent, false);
        }

        TextView etape_text = convertView.findViewById(R.id.etape_text);
        ImageView etape_image = convertView.findViewById(R.id.etape_img);

        etape_text.setText(etape.getEtape());
        etape_image.setImageResource(etape.getMedia());

        return convertView;

    }

}