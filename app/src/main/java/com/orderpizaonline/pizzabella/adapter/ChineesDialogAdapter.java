package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.ChineseCornerInfo;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.DrinksModel;
import com.orderpizaonline.pizzabella.utils.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChineesDialogAdapter extends RecyclerView.Adapter<ChineesDialogAdapter.ViewHolder>{
    private List<ChineseCornerInfo> mList = new ArrayList<>();
    private Context mContext;
    private int index = 0;
    public ChineseCornerInfo selectedCombo = new ChineseCornerInfo();
    public ChineesDialogAdapter(List<ChineseCornerInfo> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }


    @NonNull
    @Override
    public ChineesDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.combo_item,parent,false);

        return new ChineesDialogAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ChineesDialogAdapter.ViewHolder holder, final int position) {
        if (index == position){
            holder.ll_combo_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
            selectedCombo = mList.get(position);
        }else {
            holder.ll_combo_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));
        }
        String description = "";

        if (mList.get(position).getPizzaInfo().getPizzaName() != null && !mList.get(position).getPizzaInfo().getPizzaName().equals("")) {
            description = description + mList.get(position).getPizzaInfo().getQuantiy() + " " + mList.get(position).getPizzaInfo().getPizzaName();
        }

//        for (SidelineInfoInCombo models: mList.get(position).getSidelineInfo()) {
//            if (!models.getSidelineName().equals("")) {
//                if (!description.equals("")) {
//                    description = description + ", " +models.getQuantiy() + "  pcs of " +models.getSidelineName();
//                } else {
//                    description = description + " " +models.getQuantiy() + "  pcs of " +models.getSidelineName();
//                }
//            }
//        }



//        if (mList.get(position).getDrinksInfo().getQuantiy() != 0) {
//            if (!description.equals("")) {
//                description = description + ", " + mList.get(position).getDrinksInfo().getQuantiy() + " " + mList.get(position).getDrinksInfo().getDrinksSize() + " Drink(s) ";
//            } else {
//                description = description + " " + mList.get(position).getDrinksInfo().getQuantiy() + " " + mList.get(position).getDrinksInfo().getDrinksSize() + " Drink(s) ";
//
//            }
//        }

        holder.ll_combo_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
            }
        });

        holder.name.setText(mList.get(position).getItemName());
        //holder.price.setText("Rs."+mList.get(position).getPrice());
        if (mList.get(position).getDiscount() == 0) {
            holder.price.setText("RS." + mList.get(position).getPrice());
        }else {
            holder.price.setText("RS." + mList.get(position).getDiscount());
        }
        holder.description.setText(description);
        mList.get(position).setDescription(description);
        ArrayList<DrinksModel> drinksModelArrayList=new ArrayList<>();
        /*
        if (mList.get(position).getDrinksInfo()!=null && mList.get(position).getDrinksInfo().getQuantiy()>0) {
            int q = mList.get(position).getDrinksInfo().getQuantiy();
            for (int i = 0; i < q; i++) {
                DrinksModel drinksModel = new DrinksModel();
                drinksModel.setDrinkName(Common.D_Coke);
                drinksModel.setId(i);
                drinksModelArrayList.add(drinksModel);
            }
            final DrinksAdapter drinksAdapter = new DrinksAdapter(mContext, drinksModelArrayList);
            holder.nonScrollListView.setAdapter(drinksAdapter);
            final ArrayList<DrinksModel> finalDrinksModelArrayList = drinksModelArrayList;
            holder.nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    try {
                        final View dialogView = View.inflate(mContext, R.layout.spinner_coldrink_items, null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setCancelable(true);

                        TextView coke = dialogView.findViewById(R.id.cokeTv);
                        TextView sprite = dialogView.findViewById(R.id.spriteTv);
                        TextView fanta = dialogView.findViewById(R.id.fantaTv);


                        coke.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finalDrinksModelArrayList.get(i).setDrinkName(Common.D_Coke);
                                alertDialog.dismiss();
                                drinksAdapter.notifyDataSetChanged();
                            }
                        });

                        sprite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // add to list
                                finalDrinksModelArrayList.get(i).setDrinkName(Common.D_Sprite);
                                drinksAdapter.notifyDataSetChanged();

                                alertDialog.dismiss();

                            }
                        });
                        fanta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // add to list
                                finalDrinksModelArrayList.get(i).setDrinkName(Common.D_Fanta);
                                drinksAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();

                            }
                        });
                        alertDialog.setView(dialogView);
                        alertDialog.show();
                    } catch (Exception e) {
                        Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
                    }
                }
            });
            drinksModelArrayList = drinksAdapter.getList();
        }*/

//        Picasso.get().load(mList.get(position).getImageURL()).into(holder.image);
        Glide.with(mContext).load(mList.get(position).getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.image);

     /*   holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()+1;
                mList.get(position).setQuantity(q);
                MainActivity.sidelinesOrderList=mList;
            }
        });*/

    }
/*    private void showActonDialog(final int itemPosition) {
        try {
            final View dialogView = View.inflate(context, R.layout.spinner_coldrink_items, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(true);

            TextView coke = dialogView.findViewById(R.id.cokeTv);
            TextView sprite = dialogView.findViewById(R.id.spriteTv);
            TextView fanta = dialogView.findViewById(R.id.fantaTv);


            coke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listOfItems.get(itemPosition).setDrinkName(Common.D_Coke);
                    alertDialog.dismiss();
                }
            });

            sprite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // add to list
                    listOfItems.get(itemPosition).setDrinkName(Common.D_Sprite);
                    alertDialog.dismiss();

                }
            });
            fanta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // add to list
                    listOfItems.get(itemPosition).setDrinkName(Common.D_Fanta);
                    alertDialog.dismiss();

                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }*/

    @Override
    public int getItemCount() {

        if (mList==null){
            return 0;
        }
        else {

            return mList.size();
        }
    }

    public void setData(List<ChineseCornerInfo> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name, price, description;
        NonScrollListView nonScrollListView;
        private LinearLayout ll_combo_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            nonScrollListView = itemView.findViewById(R.id.coldDrinkListView);
            ll_combo_item = itemView.findViewById(R.id.ll_combo_item);

        }
    }
}
