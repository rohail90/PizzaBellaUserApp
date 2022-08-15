package com.orderpizaonline.pizzabella.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SelectedSidelinesHolder extends RecyclerView.ViewHolder {

    private ImageView image, plus, minus, delete;
    private TextView name, price, quantity, totalPrice;
    private List<SidelineOrderInfo> mList;

    public SelectedSidelinesHolder(@NonNull View itemView, List<SidelineOrderInfo> mList) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        delete = itemView.findViewById(R.id.iv_delete);
        plus = itemView.findViewById(R.id.plus);
        minus = itemView.findViewById(R.id.minus);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.quantity);
        totalPrice = itemView.findViewById(R.id.totalPrice);
        this.mList = mList;
    }


    public void bindData(SidelineOrderInfo achievementModel, int position, int size) {
        plus.setVisibility(View.INVISIBLE);
        minus.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);

        int p = Integer.parseInt(achievementModel.getPrice());
        name.setText(achievementModel.getSidelineName());
        price.setText(( p / achievementModel.getQuantity())+"");
        quantity.setText(achievementModel.getQuantity()+"");
        totalPrice.setText(achievementModel.getPrice()+"");

        Picasso.get().load(achievementModel.getImageURL()).into(image);
        //Glide.with(c).load(achievementModel.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(image);



    }



}
