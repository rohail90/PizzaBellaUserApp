package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;

import java.util.ArrayList;
import java.util.List;

public class CartFriedRollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FriedRollOrderInfo> mList = new ArrayList<>();
    private Context context;

    public CartFriedRollAdapter(Context context, List<FriedRollOrderInfo> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_sidelines_list_item,parent,false);
        return new CartFriedRollHolder(view , mList, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int size= mList.size();
        ((CartFriedRollHolder)holder).bindData(mList.get(position),position,size);

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
}