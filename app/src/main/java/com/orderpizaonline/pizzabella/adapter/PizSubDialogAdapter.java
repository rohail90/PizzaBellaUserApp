package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.ColdPizModel;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.finalPizFlavour;
import static com.orderpizaonline.pizzabella.MainActivity.finalPizList;
import static com.orderpizaonline.pizzabella.MainActivity.selectPizItm;

public class PizSubDialogAdapter extends RecyclerView.Adapter<PizSubDialogAdapter.ViewHolder>  {

    private List<ColdPizModel> mList = new ArrayList<>();
    private ArrayList<PairValuesModel> coldList = new ArrayList<>();
    private Context mContext;
    private int index = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas, combos;
    FirebaseRecyclerAdapter adapter, dessertsAdapter;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    public PizSubDialogAdapter(List<ColdPizModel> mList, Context context) {
        this.mList = mList;
        this.mContext=context;
    }


    @NonNull
    @Override
    public PizSubDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cold_piz_itemlist, parent, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        combos = firebaseDatabase.getReference(Common.PIZZA_TOPPING);
        getBaverages();

        return new PizSubDialogAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PizSubDialogAdapter.ViewHolder holder, final int position) {


        holder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coldList.size() != 0) {
                    variationDialog(coldList);
                    selectPizItm.setValue(position+"");
                }
                notifyDataSetChanged();
                //selectColdPizItem(mList.get(position), position);
            }
        });


        holder.name.setText(mList.get(position).getName());
        if (mList.get(position).getSize()!= null && !mList.get(position).getSize().equals("")) {
            holder.size.setVisibility(View.VISIBLE);
            //holder.price_tag.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.required.setVisibility(View.GONE);
            holder.size.setText(mList.get(position).getSize());
            holder.price.setText("");
            holder.change.setText("Change");
        }else {
            holder.size.setVisibility(View.GONE);
            holder.price_tag.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            holder.required.setVisibility(View.VISIBLE);
            holder.change.setText("Choose");
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

    public void setData(List<ColdPizModel> bottelList) {
        this.mList.clear();
        this.mList = bottelList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name, price, size, required, change, price_tag;
        ConstraintLayout cl_sub_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.tv_size);
            name = itemView.findViewById(R.id.variation_tag);
            price = itemView.findViewById(R.id.tv_additional_price);
            price_tag = itemView.findViewById(R.id.additional_price_tag);
            change = itemView.findViewById(R.id.tv_change);
            required = itemView.findViewById(R.id.tv_required);
            cl_sub_item = itemView.findViewById(R.id.cl_sub_item);

        }
    }


    private void variationDialog(ArrayList<PairValuesModel> spInfo){
        try {

            final View dialogView = View.inflate(mContext, R.layout.sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(mContext).create();
            alertDialog1.setCancelable(false);
            TextView tv_variation_title = dialogView.findViewById(R.id.tv_variation_title);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);

            SelectPizSubDialogAdapter sAdapter = new SelectPizSubDialogAdapter(spInfo, mContext);
            RecyclerView recyclerview = dialogView.findViewById(R.id.recyclerview);
            LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(mContext,  LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(staggeredGridLayoutManager);
            recyclerview.setAdapter(sAdapter);

            tv_variation_title.setText("Flavour");

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //selectedPairValuesModel = new PairValuesModel();
                    selectPizItm = new PairValuesModel();
                    alertDialog1.dismiss();
                    //showActonDialog(sidelinesOrderInfoBella,model);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectPizItm!= null){
                        if (selectPizItm.getName()!= null && !selectPizItm.getName().equals("")){
                            finalPizList.get(Integer.parseInt(selectPizItm.getValue())).setSize(selectPizItm.getName());
                            selectPizItm = new PairValuesModel();
                            notifyDataSetChanged();
                            alertDialog1.dismiss();
                        }else {
                            Toast.makeText(mContext, "Please Choose", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(mContext, "Please Choose", Toast.LENGTH_SHORT).show();
                    }

                   /* if (selectedPairValuesModel != null){
                        if (selectedPairValuesModel.getValue() != null && !selectedPairValuesModel.getValue().equals("")){
                            SizeInfo si = new SizeInfo();
                            si.setSize(selectedPairValuesModel.getName());
                            si.setPrice(Integer.parseInt(selectedPairValuesModel.getValue()));
                            sidelinesOrderInfoBella.setSizeInfo(si);
                            sidelinesOrderInfoBella.setTotalPrice(selectedPairValuesModel.getValue());
                            alertDialog1.dismiss();
                            showActonDialog(sidelinesOrderInfoBella,model);
                            selectedPairValuesModel = new PairValuesModel();
                        }
                    }*/
                }
            });
            alertDialog1.setView(dialogView);
            alertDialog1.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }



    private void getBaverages() {
        coldList = new ArrayList<>();
        finalPizFlavour.clear();
        finalPizFlavour = new ArrayList<>();
        combos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                coldList.clear();
                finalPizFlavour.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaToppingInfo chat = snapshot.getValue(PizzaToppingInfo.class);
                    finalPizFlavour.add(chat);
                    PairValuesModel p = new PairValuesModel();
                    p.setName(chat.getItemName());
                    p.setValue("");
                    p.setChecked(false);
                    coldList.add(p);

                    /*
                     if (chat.getBeveragesType().equals(Common.SOFT_DRINK)) {
                         for (int i = 0; i<chat.getColdDrinksInfo().size();i++){
                             PairValuesModel p = new PairValuesModel();
                             p.setName(chat.getColdDrinksInfo().get(i));
                             p.setValue("");
                             p.setChecked(false);
                             coldList.add(p);
                         }
                         break;

                     }*/
                    /*messageAdapter = new MessageAdapter(context, coldrinksList);
                    recyclerView.setAdapter(messageAdapter);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}