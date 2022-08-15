package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;

import java.util.ArrayList;
import java.util.List;

public class PizzaOrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PizzaOrderCartModel> mList = new ArrayList<>();
    private Context context;

    public PizzaOrderDetailsAdapter(Context context, List<PizzaOrderCartModel> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.piza_info_item,parent,false);
        return new PizzaOrderDetailsHolder(view , mList, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int size= mList.size();
        ((PizzaOrderDetailsHolder)holder).bindData(mList.get(position),position,size);

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

    public void setData(List<PizzaOrderCartModel> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
}