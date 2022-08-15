package com.orderpizaonline.pizzabella.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import java.util.List;

import static com.orderpizaonline.pizzabella.fragments.CartCheckoutFragment.orders;
import static com.orderpizaonline.pizzabella.fragments.CartCheckoutFragment.setTotalCartPrice;


public class CartCheckoutHolder extends RecyclerView.ViewHolder {

    private ImageView image, plus, minus, delete;
    private TextView name, price, quantity, totalPrice;
    private List<SidelineOrderInfo> mList;
    Context context;

    Dialog rateus_dialog;
    TextView rateus_noText, dialog_ok_txt;

    public CartCheckoutHolder(@NonNull View itemView, List<SidelineOrderInfo> mList, Context context) {
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


        rateus_dialog = new Dialog(context, R.style.CustomDialog);
        rateus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateus_dialog.setCancelable(false);
        rateus_dialog.setContentView(R.layout.delete_dialog);

        rateus_dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                return true;
            }
            return false;
        });

        rateus_noText = rateus_dialog.findViewById(R.id.dismiss_txt);
        dialog_ok_txt = rateus_dialog.findViewById(R.id.ok_txt);


    }


    public void bindData(final SidelineOrderInfo achievementModel, final int position, int size) {

       // final int p = Integer.parseInt(achievementModel.getPrice());
        name.setText(achievementModel.getSidelineName());
        price.setText(""+ Integer.parseInt(achievementModel.getPrice()));
        quantity.setText(achievementModel.getQuantity()+"");
        totalPrice.setText(Integer.parseInt(achievementModel.getPrice() )* achievementModel.getQuantity()+"");

        //Picasso.get().load(achievementModel.getImageURL()).into(image);
       // Glide.with(context).load(achievementModel.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(image);
        //image.setImageDrawable(context.getResources().getDrawable(R.drawable.soft_drink));
        if (mList.get(position).getBeveragesType().equals(Common.MINERAL_WATER)){
            image.setImageResource(R.mipmap.minerlwater2);
        }else if (mList.get(position).getBeveragesType().equals(Common.JUICES)){
            image.setImageResource(R.mipmap.nestlejuice1);
        }else if (mList.get(position).getBeveragesType().equals(Common.SOFT_DRINK)){
            image.setImageResource(R.mipmap.softdrink1);
        }
        plus.setOnClickListener(new View.OnClickListener() {
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
                if (!rateus_dialog.isShowing()) {
                    rateus_dialog.show();
                }

                rateus_noText.setOnClickListener(vi -> {
                    rateus_dialog.dismiss();
                });

                dialog_ok_txt.setOnClickListener(vi -> {
                    mList.remove(position);
                    // orders.remove(position);
                    new DBClass(context).DeleteSideline(achievementModel.getId());
                    setTotalCartPrice();
                    rateus_dialog.dismiss();
                });

            }
        });

    }



}
