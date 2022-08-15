package com.orderpizaonline.pizzabella.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.DrinksModel;
import com.orderpizaonline.pizzabella.utils.Utils;

import java.util.ArrayList;


public class DrinksAdapter extends ArrayAdapter{

    ArrayList<DrinksModel> listOfItems;
    Context context;
    public DrinksAdapter(Context context, ArrayList<DrinksModel> listOfItems) {
        super(context, R.layout.colddrinks_item, listOfItems);
        this.context=context;
        this.listOfItems=listOfItems;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.colddrinks_item, parent, false);
        Log.i("TAGI", "position: "+ position+  "  "+ Utils.getGsonParser().toJson(listOfItems));

        TextView coldDrinksSpinner=convertView.findViewById(R.id.coldDrinksSpinner);
        coldDrinksSpinner.setText(listOfItems.get(position).getDrinkName());



                Log.i("TAGI", "onItemSelected: "+ position);
                //coldDrinksSpinner.setSelection(((ArrayAdapter<String>) coldDrinksSpinner.getAdapter()).getPosition(drinksList.get(position)));

        return convertView;
    }

    public ArrayList<DrinksModel> getList(){
        return listOfItems;
    }
}