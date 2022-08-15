package com.orderpizaonline.pizzabella.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;


public class VegetablesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;


    public VegetablesViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view, getAdapterPosition(), false);
    }
}
