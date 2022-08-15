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

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;

import java.util.List;

import static com.orderpizaonline.pizzabella.fragments.CartCheckoutFragment.ordersDeal;
import static com.orderpizaonline.pizzabella.fragments.CartCheckoutFragment.setTotalCartPrice;


public class CartDealHolder extends RecyclerView.ViewHolder {

    private ImageView image, plus, minus, delete;
    private TextView name, price, quantity, totalPrice;
    private List<ComboOrderInfo> mList;
    Context context;

    Dialog rateus_dialog;
    TextView rateus_noText, dialog_ok_txt;

    public CartDealHolder(@NonNull View itemView, List<ComboOrderInfo> mList, Context context) {
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


    public void bindData(final ComboOrderInfo achievementModel, final int position, int size) {

        name.setText(achievementModel.getItemName());
        price.setText(""+ achievementModel.getPrice());
        quantity.setText(achievementModel.getQuantity()+"");
        totalPrice.setText(achievementModel.getTotalPrice()+"");

        //Picasso.get().load(achievementModel.getImageURL()).into(image);
       Glide.with(context).load(achievementModel.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(image);
       // image.setImageDrawable(context.getResources().getDrawable(R.drawable.soft_drink));
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()+1;
                mList.get(position).setQuantity(q);
                ordersDeal.get(position).setQuantity(q);
                new DBClass(context).UpdateDealQuantity(achievementModel.getId(),q);
                quantity.setText(q+"");
                totalPrice.setText( (q * Integer.parseInt(mList.get(position).getPrice())) +"");
                ordersDeal.get(position).setTotalPrice(q * Integer.parseInt(mList.get(position).getPrice()) );
                setTotalCartPrice();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()-1;
                if (q != 0) {
                    mList.get(position).setQuantity(q);
                    ordersDeal.get(position).setQuantity(q);
                    ordersDeal.get(position).setTotalPrice(q * Integer.parseInt(mList.get(position).getPrice()) );
                    new DBClass(context).UpdateDealQuantity(achievementModel.getId(), q);
                    quantity.setText(q + "");
                    totalPrice.setText((q * Integer.parseInt(mList.get(position).getPrice())) + "");
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
                    new DBClass(context).DeleteDeal(achievementModel.getId());
                    setTotalCartPrice();
                    rateus_dialog.dismiss();
                });

            }
        });

    }



}
