package com.orderpizaonline.pizzabella.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;
import static com.orderpizaonline.pizzabella.fragments.PizzaToppingFlavorFrag.selectedFullCircleModel;
import static com.orderpizaonline.pizzabella.fragments.PizzaToppingFlavorFrag.selectedLeftHalfCircleModel;
import static com.orderpizaonline.pizzabella.fragments.PizzaToppingFlavorFrag.selectedRightHalfCircleModel;

public class FreeToppingsAdapter extends RecyclerView.Adapter<FreeToppingsAdapter.ViewHolder>  {

    private List<PizzaToppingInfo> mList = new ArrayList<>();
    private Context mContext;
    public FreeToppingsAdapter(List<PizzaToppingInfo> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FreeToppingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.topping_item,parent,false);
            return new FreeToppingsAdapter.ViewHolder(view);
        }


    @Override
    public void onBindViewHolder(@NonNull FreeToppingsAdapter.ViewHolder holder, final int position) {

        try {
            boolean isFOund = false;

            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE) || pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                if (selectedFullCircleModel.getId() != null) {
                    if (selectedFullCircleModel.getId().equals(mList.get(position).getId())) {
                        isFOund = true;
                    }
                }

                if (selectedLeftHalfCircleModel.getId() != null) {
                    if (selectedLeftHalfCircleModel.getId().equals(mList.get(position).getId())) {
                        isFOund = true;
                    }
                }

                if (selectedRightHalfCircleModel.getId() != null) {
                    if (selectedRightHalfCircleModel.getId().equals(mList.get(position).getId())) {
                        isFOund = true;
                    }
                }
            }


            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                if (selectedFullCircleModel.getId() != null) {
                    if (selectedFullCircleModel.getId().equals(mList.get(position).getId())) {
                        isFOund = true;
                    }
                }
            }

            if (isFOund) {
                holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));

            } else {
                holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));
            }

        /*if (selectedFullCircleModel.getId() != null){
            if (selectedFullCircleModel.getId().equals(mList.get(position).getId())){
                holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
            }
        }else if (selectedLeftHalfCircleModel.getId() != null){
            if (selectedLeftHalfCircleModel.getId().equals(mList.get(position).getId())){
                holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
            } else if (selectedRightHalfCircleModel.getId() != null){
                if (selectedRightHalfCircleModel.getId().equals(mList.get(position).getId())){
                    holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
                }
            }
        } else if (selectedRightHalfCircleModel.getId() != null){
            if (selectedRightHalfCircleModel.getId().equals(mList.get(position).getId())){
                holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_shape));
            }
        } else {
            holder.ll_topping_item.setBackground(mContext.getResources().getDrawable(R.drawable.simple_border_shape));
        }*/

            holder.name.setText(mList.get(position).getItemName());
            holder.name.setTag(mList.get(position).getItemName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> str = new ArrayList<>();
                    str.add("free");
                    str.add(mList.get(position).getItemName());
                    EventBus.getDefault().post(str);
                }
            });
/*
            holder.name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {

                    View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
                    ClipData.Item item = new ClipData.Item(v.getTag().toString());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(data, mShadow, null, 0);
                    } else {
                        v.startDrag(data, mShadow, null, 0);
                    }

                    return false;
                }
            });*/


        }catch (Exception e){
            e.printStackTrace();
        }
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

    public void setData(List<PizzaToppingInfo> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private LinearLayout ll_topping_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ll_topping_item = itemView.findViewById(R.id.ll_topping_item);

        }
    }
}