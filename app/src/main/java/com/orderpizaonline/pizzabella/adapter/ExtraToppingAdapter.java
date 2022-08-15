package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;

import java.util.ArrayList;
import java.util.List;

public class ExtraToppingAdapter extends BaseAdapter {
    private Context context;
    private String isHidePrice;
    private List<SidelineOrderInfo> extraToppingList = new ArrayList<>();

    public ExtraToppingAdapter(Context context, List<SidelineOrderInfo> extraToppingList, String str) {
        this.context = context;
        this.extraToppingList = extraToppingList;
        this.isHidePrice = str;
        Log.i("TAG", "LocationsAdapter: ");
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        gridView = inflater.inflate(R.layout.extrass_topping_item_in_before_cart, parent, false);

        final ToppingViewHolder viewHolder = new ToppingViewHolder();
        viewHolder.toppingName = gridView.findViewById(R.id.tv_extras_name);
        viewHolder.toppingPrice = gridView.findViewById(R.id.tv_extras_price);
        viewHolder.toppingName.setText(extraToppingList.get(position).getSidelineName());
        if (isHidePrice.equals("showPrice")) {
            viewHolder.toppingPrice.setText("+Rs." + extraToppingList.get(position).getPrice());
        }else {
            viewHolder.toppingPrice.setVisibility(View.GONE);
        }
        return gridView;
    }
    public class ToppingViewHolder {
        public TextView toppingName;
        public TextView toppingPrice;
    }
    @Override
    public int getCount() {

        return extraToppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return extraToppingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}