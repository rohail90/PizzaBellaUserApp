package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.SidelineInfo;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.fragments.PizzaToppingVegetablesFrag.toppingVegeList;

public class PizzaVegetablesAdapter extends RecyclerView.Adapter<PizzaVegetablesAdapter.ViewHolder>  {

    private List<SidelineInfo> mList = new ArrayList<>();
    private Context mContext;
    public PizzaVegetablesAdapter(List<SidelineInfo> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }


    @NonNull
    @Override
    public PizzaVegetablesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(mContext).inflate(R.layout.sideslines_item,parent,false);

            return new PizzaVegetablesAdapter.ViewHolder(view);
        }



    @Override
    public void onBindViewHolder(@NonNull final PizzaVegetablesAdapter.ViewHolder holder, final int position) {

        holder.price.setVisibility(View.GONE);
        holder.name.setText(mList.get(position).getSidelineName());
//        Picasso.get().load(mList.get(position).getImageURL()).into(holder.image);
        Glide.with(mContext).load(mList.get(position).getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.image);


        if (toppingVegeList != null) {

            boolean isFOund = true;

            for (int i = 0; i < toppingVegeList.size(); i++) {
                if (mList.get(position).getId().equals(toppingVegeList.get(i).getId())) {
                    isFOund = false;
                }
            }

            if (isFOund) {
                holder.selection.setChecked(false);
                holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));

            } else {
                holder.selection.setChecked(true);
                holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
            }

            /*for (int j = 0; j < toppingVegeList.size(); j++) {
                if (mList.get(position).getId().equals(toppingVegeList.get(j).getId())) {
                    holder.selection.setChecked(true);
                    holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
                }
            }*/
        }
        holder.ll_sideline_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    boolean isFOund = false;
                    if (toppingVegeList != null) {
                        if (toppingVegeList.size() > 0) {
                            for (int i = 0; i < toppingVegeList.size(); i++) {
                                if (mList.get(position).getId().equals(toppingVegeList.get(i).getId())) {
                                    isFOund = true;
                                    toppingVegeList.remove(i);
                                }
                            }
                        }
                    }
                    if (isFOund) {
                        holder.selection.setChecked(false);
                        holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));

                    } else {
                        holder.selection.setChecked(true);
                        holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
                        toppingVegeList.add(mList.get(position));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                /*
                if (holder.selection.isChecked()) {
                    for (int j = 0; j < toppingVegeList.size(); j++) {
                        if (toppingVegeList.get(j).getId().equals(mList.get(position).getId())) {
                            toppingVegeList.remove(j);
                            holder.selection.setChecked(false);
                            holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));

                            //notifyDataSetChanged();
                        }
                    }
                }else {

                    for (int j = 0; j < toppingVegeList.size(); j++) {
                        if (toppingVegeList.get(j).getId().equals(mList.get(position).getId())) {
                            toppingVegeList.add(mList.get(position));

                            holder.selection.setChecked(true);
                            holder.ll_sideline_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));

                            //notifyDataSetChanged();
                        }
                    }
                }*/
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

    public void setData(List<SidelineInfo> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView price;
        private LinearLayout ll_sideline_item;
        private RadioButton selection;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            selection = itemView.findViewById(R.id.selection);
            ll_sideline_item = itemView.findViewById(R.id.ll_sideline_item);

        }
    }
}