package com.example.lerari.ihmproject.ItemAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lerari.ihmproject.Entities.Ingredient;
import com.example.lerari.ihmproject.R;

import java.util.ArrayList;

public class IngredientItemAdapter extends ArrayAdapter<Ingredient> {

    public IngredientItemAdapter(@NonNull Context context, ArrayList<Ingredient> ingredients) {
        super(context, R.layout.activity_ingredient_item_adapter ,ingredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ingredient ingredient = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ingredient_item_adapter, parent, false);
        }

        ImageView ingredient_image = convertView.findViewById(R.id.ingredient_img);
        TextView ingredient_name = convertView.findViewById(R.id.ingredient_name);



        ingredient_image.setImageResource(ingredient.getMedia());
        ingredient_name.setText(ingredient.getIngredient());


        return convertView;

    }
}
