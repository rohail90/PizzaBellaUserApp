package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;

import java.util.ArrayList;
import java.util.List;

public class SelectedFriedRollAdapter extends RecyclerView.Adapter<SelectedFriedRollAdapter.ViewHolder>  {

    private List<FriedRollOrderInfo> mList = new ArrayList<>();
    private Context mContext;
    public SelectedFriedRollAdapter(List<FriedRollOrderInfo> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }


    @NonNull
    @Override
    public SelectedFriedRollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(mContext).inflate(R.layout.cart_sidelines_list_item,parent,false);

            return new SelectedFriedRollAdapter.ViewHolder(view);
        }



    @Override
    public void onBindViewHolder(@NonNull SelectedFriedRollAdapter.ViewHolder holder, final int position) {
        holder.plus.setVisibility(View.INVISIBLE);
        holder.minus.setVisibility(View.INVISIBLE);
        holder.delete.setVisibility(View.INVISIBLE);

        holder.name.setText(mList.get(position).getItemName());
        holder.price.setText(( mList.get(position).getSinglePrice())+"");
        holder.quantity.setText(mList.get(position).getQuantity()+"");
        holder.totalPrice.setText(mList.get(position).getTotalPrice()+"");

//        Picasso.get().load(mList.get(position).getImageURL()).into(holder.image);
//        Glide.with(mContext).load(mList.get(position).getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.image);

       Glide.with(mContext).load(mList.get(position).getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.image);
       // holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.soft_drink));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()+1;
                mList.get(position).setQuantity(q);
            }
        });

    }

    @Override
    public int getItemCount() {

        if (mList==null){
            return 0;
        }
        else {

            return mList.size();
        }
    }

    public void setData(List<FriedRollOrderInfo> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image, plus, minus, delete;
        private TextView name, price, quantity, totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.iv_delete);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);

        }
    }
}