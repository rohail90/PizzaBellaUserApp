package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;

import java.util.List;


public class ChineseOrderDetailsHolder extends RecyclerView.ViewHolder {

    private ImageView image, plus, minus, delete;
    private TextView name, price, quantity, totalPrice;
    private List<ChineseCornerOrderInfo> mList;
    Context context;


    public ChineseOrderDetailsHolder(@NonNull View itemView, List<ChineseCornerOrderInfo> mList, Context context) {
        super(itemView);
        this.context = context;
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


    public void bindData(final ChineseCornerOrderInfo achievementModel, final int position, int size) {

        name.setText(achievementModel.getName());
        price.setText(achievementModel.getSinglePrice()+"");
        quantity.setText(achievementModel.getQuantity()+"");
        totalPrice.setText(achievementModel.getTotalPrice()+"");

//        Picasso.get().load(achievementModel.getImageURL()).into(image);
        Glide.with(context).load(achievementModel.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(image);

        plus.setVisibility(View.GONE);
        minus.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    /*    plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()+1;
                mList.get(position).setQuantity(q);
                orders.get(position).setQuantity(q);
                int pr= Integer.parseInt(mList.get(position).getPrice());
                new DBClass(context).UpdateQuantity(achievementModel.getId(),q);
                quantity.setText(q+"");
                totalPrice.setText( (q * pr) +"");
                setTotalCartPrice();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()-1;
                if (q != 0) {
                    mList.get(position).setQuantity(q);
                    orders.get(position).setQuantity(q);
                    int pr = Integer.parseInt(mList.get(position).getPrice());
                    new DBClass(context).UpdateQuantity(achievementModel.getId(), q);
                    quantity.setText(q + "");
                    totalPrice.setText((q * pr) + "");
                    setTotalCartPrice();

                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mList.remove(position);
                   // orders.remove(position);
                    new DBClass(context).DeleteSideline(achievementModel.getId());
                    setTotalCartPrice();

            }
        });*/

    }



}
