package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.PairValuesModel;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.fragments.ShowBeveragesFragment.setCodrinkSize;

public class SelectCodrinkSizeDialogAdapter extends RecyclerView.Adapter<SelectCodrinkSizeDialogAdapter.ViewHolder>  {

    private List<PairValuesModel> mList = new ArrayList<>();
    private Context mContext;
    private int index = 0;
    public SelectCodrinkSizeDialogAdapter(List<PairValuesModel> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }


    @NonNull
    @Override
    public SelectCodrinkSizeDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.sub_dialog_item,parent,false);
            return new SelectCodrinkSizeDialogAdapter.ViewHolder(view);
        }


    @Override
    public void onBindViewHolder(@NonNull SelectCodrinkSizeDialogAdapter.ViewHolder holder, final int position) {


        holder.cl_sub_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i<mList.size(); i++){
                    if (i == position) {
                        mList.get(i).setChecked(true);
                    }else {
                        mList.get(i).setChecked(false);
                    }
                }
                notifyDataSetChanged();
                setCodrinkSize(mList.get(position));
            }
        });

        holder.name.setText(mList.get(position).getName());
        holder.price.setText("Rs."+mList.get(position).getValue());
        if (mList.get(position).getChecked()){
            holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.email_icon));
        }else {
            holder.image.setImageDrawable(null);
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

    public void setData(List<PairValuesModel> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name, price;
        private ConstraintLayout cl_sub_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.checked);
            name = itemView.findViewById(R.id.additional_price_tag);
            price = itemView.findViewById(R.id.tv_additional_price);
            cl_sub_item = itemView.findViewById(R.id.cl_sub_item);

        }
    }
}