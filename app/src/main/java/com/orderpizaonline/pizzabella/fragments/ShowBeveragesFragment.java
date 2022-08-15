package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.ComboDialogAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectCodrinkSizeDialogAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectColdrinkDialogAdapter;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.BeveragesInfo;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfoBella;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.model.SizeInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class ShowBeveragesFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas, crust, pizzaToppings, pizzaExtraToppings, beverages;
    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerAdapter combosAdapter;
    RecyclerView recyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    RecyclerView combo_dialog_recycler;
    ArrayList<SidelineInfo> coldrinksList;
    ArrayList<ComboInfo> combosList;
    ComboDialogAdapter comboDialogAdapter;
    int qty = 1;


    FirebaseRecyclerAdapter crustAdapter;
    RecyclerView crustRecyclerView;
    private SidelineInfo crustModel = new SidelineInfo();
    int index4 = -1;
    int orderPrice = 0;
    List<PizzaToppingInfo> pizzaToppingInfo;
    List<SidelineInfo> sidelineInfoList;
    TextView title;

    public static PairValuesModel selectedPairValuesModel = new PairValuesModel();
    public static PairValuesModel selectedColdrink = new PairValuesModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.show_pizzas_fragment, container, false);
        context = container.getContext();
        Utils.clearModels();
        pizzaOrderInfoBella = null;
        pizzaOrderInfoBella = new PizzaOrderInfoBella();
        sidelinesOrderInfoBella = null;
        sidelinesOrderInfoBella = new SidelineOrderInfo();
        sidelinesOrderList.clear();

        ((MainActivity) context).setToolbarTitle("Beverages");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas = firebaseDatabase.getReference(Common.PIZZA);
        crust = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_TOPPING);
        pizzaExtraToppings = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);
        beverages = firebaseDatabase.getReference(Common.BEVERAGES);
        //combos = firebaseDatabase.getReference(Common.COMBO);
        //coldrinks = firebaseDatabase.getReference(Common.SIDE_LINES);

        //getColdrinks();

        title =v.findViewById(R.id.title);
        title.setText("Beverages");
        recyclerView =v.findViewById(R.id.pizzaRecyclerview);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        showBeverages();
        /*if (pizzaOrderInfoBella != null){
            showActonDialog(pizzaOrderInfoBella);
        }*/
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void showBeverages() {
        FirebaseRecyclerOptions<BeveragesInfo> options = new FirebaseRecyclerOptions.Builder<BeveragesInfo>().setQuery(
                beverages, BeveragesInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<BeveragesInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_pizza_sub_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final BeveragesInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                //if (model.getDiscount() == 0) {
                itemPrice.setText("RS." + model.getSizesArrayList().get(0).getPrice());
                /*}else {
                    itemPrice.setText("RS." + model.getDiscount());
                }*/
                //itemPrice.setVisibility(View.VISIBLE);

                ImageView itemImage=holder.itemView.findViewById(R.id.image);
                //Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                if (model.getBeveragesType().equals(Common.MINERAL_WATER)){
                    itemImage.setImageResource(R.mipmap.minerlwater2);
                }else if (model.getBeveragesType().equals(Common.JUICES)){
                    itemImage.setImageResource(R.mipmap.nestlejuice1);
                }else if (model.getBeveragesType().equals(Common.SOFT_DRINK)){
                    itemImage.setImageResource(R.mipmap.softdrink1);
                }
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();

                        sidelinesOrderInfoBella = null;
                        sidelinesOrderInfoBella = new SidelineOrderInfo();
                       qty = 1;
                        orderPrice = 0;
                        sidelinesOrderInfoBella.setId(model.getId());
                        sidelinesOrderInfoBella.setQuantity(1);
                        sidelinesOrderInfoBella.setSidelineName(model.getName());
                        if (model.getBeveragesType().equals(Common.SOFT_DRINK)){
                            showActonDialog(sidelinesOrderInfoBella, model);
                        }else if (model.getBeveragesType().equals(Common.JUICES)){
                            sidelinesOrderInfoBella.setSidelineName(model.getName());
                            sidelinesOrderInfoBella.setSizeInfo(model.getSizesArrayList().get(0));
                            sidelinesOrderInfoBella.setColdDrinksInfo("Juice");
                            sidelinesOrderInfoBella.setQuantity(1);
                            sidelinesOrderInfoBella.setTotalPrice(model.getSizesArrayList().get(0).getPrice()+"");
                            sidelinesOrderInfoBella.setBeveragesType(model.getBeveragesType());
                            sidelinesOrderInfoBella.setPrice(model.getSizesArrayList().get(0).getPrice()+"");
                            showActonDialog(sidelinesOrderInfoBella, model);
                        }else if (model.getBeveragesType().equals(Common.MINERAL_WATER)){
                            showActonDialog(sidelinesOrderInfoBella, model);
                        }

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showActonDialog(SidelineOrderInfo sidelineOrderInfo, BeveragesInfo model) {
        try {
            final View dialogView = View.inflate(context, R.layout.general_items_customization_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            TextView itemName = dialogView.findViewById(R.id.tv_item_name);
            TextView tv_price = dialogView.findViewById(R.id.tv_price);
            TextView tv_total_price = dialogView.findViewById(R.id.tv_total_price);
            TextView tv_change = dialogView.findViewById(R.id.tv_change);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_addtocart);

            TextView tv_required = dialogView.findViewById(R.id.tv_required);
            TextView tv_size = dialogView.findViewById(R.id.tv_size);
            TextView additional_price_tag = dialogView.findViewById(R.id.additional_price_tag);
            TextView tv_additional_price = dialogView.findViewById(R.id.tv_additional_price);
            ImageView iv_add = dialogView.findViewById(R.id.iv_add);
            ImageView iv_minus = dialogView.findViewById(R.id.iv_minus);
            TextView tv_quantity = dialogView.findViewById(R.id.tv_quantity);

            ConstraintLayout variation_layout = dialogView.findViewById(R.id.variation_layout);
            ConstraintLayout extra_topping_layout = dialogView.findViewById(R.id.extra_topping_layout);
            TextView tv_extra_topping_change = dialogView.findViewById(R.id.tv_extra_topping_change);
            TextView extra_topping_tag = dialogView.findViewById(R.id.extra_topping_tag);
            TextView tv_extra_topping_required = dialogView.findViewById(R.id.tv_extra_topping_required);
            TextView tv_extra_topping_size = dialogView.findViewById(R.id.tv_extra_topping_size);
            TextView extra_topping_additional_price_tag = dialogView.findViewById(R.id.extra_topping_additional_price_tag);
            TextView tv_extra_topping_additional_price = dialogView.findViewById(R.id.tv_extra_topping_additional_price);

            itemName.setText(model.getName());
            sidelinesOrderInfoBella.setBeveragesType(model.getBeveragesType());

            if (model.getBeveragesType().equals(Common.SOFT_DRINK)) {
                extra_topping_layout.setVisibility(View.VISIBLE);
                tv_extra_topping_required.setText("Required");
                extra_topping_tag.setText("Choose Drink");
                if (sidelineOrderInfo.getSizeInfo() != null /*&& !sidelineOrderInfo.getSizeInfo().getSize().equals("")*/) {
                    tv_price.setText("Rs: " + sidelineOrderInfo.getSizeInfo().getPrice());
                    tv_total_price.setText("" + sidelineOrderInfo.getTotalPrice());
                    tv_change.setText("Change");
                    tv_required.setVisibility(View.GONE);
                    tv_size.setVisibility(View.VISIBLE);
                    tv_additional_price.setVisibility(View.VISIBLE);
                    additional_price_tag.setVisibility(View.VISIBLE);
                    tv_size.setText(sidelineOrderInfo.getSizeInfo().getSize());
                    tv_additional_price.setText(sidelineOrderInfo.getSizeInfo().getPrice()+"");

                    if (sidelineOrderInfo.getColdDrinksInfo() != null && !sidelineOrderInfo.getColdDrinksInfo().equals(""))
                    {
                        tv_extra_topping_required.setVisibility(View.GONE);
                        tv_extra_topping_size.setVisibility(View.VISIBLE);
                        tv_extra_topping_size.setText(sidelineOrderInfo.getColdDrinksInfo());

                    }
                }else {
                    tv_price.setText("from Rs: " + model.getSizesArrayList().get(0).getPrice());
                    tv_total_price.setText("" + model.getSizesArrayList().get(0).getPrice());
                }
/*

                extra_topping_layout.setVisibility(View.VISIBLE);
                tv_price.setText("Rs: " + model.getSizePrice());
                tv_total_price.setText("" + model.getTotalPrice());
                tv_required.setVisibility(View.GONE);
                tv_size.setVisibility(View.VISIBLE);
                tv_additional_price.setVisibility(View.VISIBLE);
                additional_price_tag.setVisibility(View.VISIBLE);
                tv_size.setText(model.getSizeName());
                tv_additional_price.setText(model.getSizePrice());
                if (model.getExtraTopping() != null) {
                    tv_extra_topping_required.setVisibility(View.GONE);
                    tv_extra_topping_size.setVisibility(View.VISIBLE);
                    tv_extra_topping_additional_price.setVisibility(View.VISIBLE);
                    extra_topping_additional_price_tag.setVisibility(View.VISIBLE);

                    tv_extra_topping_size.setText(model.getExtraTopping().getName());
                    tv_extra_topping_additional_price.setText(model.getExtraTopping().getValue());
                }

*/

            }else if (model.getBeveragesType().equals(Common.MINERAL_WATER))  {
                if (sidelineOrderInfo.getSizeInfo() != null /*&& !sidelineOrderInfo.getSizeInfo().getSize().equals("")*/) {
                    tv_price.setText("Rs: " + sidelineOrderInfo.getSizeInfo().getPrice());
                    tv_total_price.setText("" + sidelineOrderInfo.getTotalPrice());

                    tv_required.setVisibility(View.GONE);

                    tv_change.setText("Change");
                    tv_size.setVisibility(View.VISIBLE);
                    tv_additional_price.setVisibility(View.VISIBLE);
                    additional_price_tag.setVisibility(View.VISIBLE);
                    tv_size.setText(sidelineOrderInfo.getSizeInfo().getSize());
                    tv_additional_price.setText(sidelineOrderInfo.getSizeInfo().getPrice()+"");

                }
                  /*  if (sidelineOrderInfo.getColdDrinksInfo() != null) {
                    if (sidelineOrderInfo.getColdDrinksInfo() != null && !sidelineOrderInfo.getColdDrinksInfo().equals("")) {
                        tv_price.setText("Rs: " + sidelineOrderInfo.getSizeInfo().getPrice());
                        tv_total_price.setText("" + sidelineOrderInfo.getTotalPrice());

                        tv_required.setVisibility(View.GONE);
                        tv_size.setVisibility(View.VISIBLE);
                        tv_additional_price.setVisibility(View.VISIBLE);
                        additional_price_tag.setVisibility(View.VISIBLE);
                        tv_size.setText(sidelineOrderInfo.getSizeInfo().getSize());
                        tv_additional_price.setText(sidelineOrderInfo.getSizeInfo().getPrice());

                    }
                } */else {
                    tv_price.setText("from Rs: " + model.getSizesArrayList().get(0).getPrice());
                    tv_total_price.setText("" + model.getSizesArrayList().get(0).getPrice());
                }
            }else if (model.getBeveragesType().equals(Common.JUICES)){

                tv_price.setText("Rs: " + sidelineOrderInfo.getSizeInfo().getPrice());
                tv_total_price.setText("" + sidelineOrderInfo.getTotalPrice());
                variation_layout.setVisibility(View.GONE);

            }

            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sidelinesOrderInfoBella.getSizeInfo() != null) {
                        qty++;
                        tv_total_price.setText(" " + sidelineOrderInfo.getSizeInfo().getPrice() * qty);
                        tv_quantity.setText("" + qty);
                        sidelinesOrderInfoBella.setTotalPrice(""+sidelineOrderInfo.getSizeInfo().getPrice() * qty);
                        sidelinesOrderInfoBella.setQuantity(qty);
                    }
                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (qty > 1){
                       qty--;
                       tv_total_price.setText(" " + sidelineOrderInfo.getSizeInfo().getPrice() * qty);
                       tv_quantity.setText("" + qty);
                       sidelinesOrderInfoBella.setTotalPrice(""+sidelineOrderInfo.getSizeInfo().getPrice() * qty);
                       sidelinesOrderInfoBella.setQuantity(qty);
                   }
                }
            });

            tv_extra_topping_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (model != null) {
                        extraToppingDialog(model.getColdDrinksInfo(), model);
                        alertDialog.dismiss();
                    }

                }
            });

            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    variationDialog(model.getSizesArrayList(), model);
                    alertDialog.dismiss();
                }
            });

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sidelinesOrderInfoBella.getBeveragesType().equals(Common.SOFT_DRINK)) {
                        if (sidelinesOrderInfoBella.getColdDrinksInfo() != null && !sidelinesOrderInfoBella.getColdDrinksInfo().equals("") && sidelinesOrderInfoBella.getSizeInfo() != null) {

                            if (sidelinesOrderInfoBella.getSizeInfo().getSize() != null && !sidelinesOrderInfoBella.getSizeInfo().getSize().equals("")) {
                                sidelinesOrderInfoBella.setPrice(sidelinesOrderInfoBella.getSizeInfo().getPrice()+"");
                                setNextPhoneFragment();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(context, "Please Select Size", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if (sidelinesOrderInfoBella.getBeveragesType().equals(Common.MINERAL_WATER)){
                        if (sidelinesOrderInfoBella.getSizeInfo() != null) {
                            if (sidelinesOrderInfoBella.getSizeInfo().getSize() != null && !sidelinesOrderInfoBella.getSizeInfo().getSize().equals("")) {
                                sidelinesOrderInfoBella.setPrice(sidelinesOrderInfoBella.getSizeInfo().getPrice()+"");
                                setNextPhoneFragment();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(context, "Please Select Size", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if (sidelinesOrderInfoBella.getBeveragesType().equals(Common.JUICES)) {
                        setNextPhoneFragment();
                        alertDialog.dismiss();

                    }
                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void variationDialog(ArrayList<SizeInfo> spInfo, BeveragesInfo model){
        try {
            List<PairValuesModel> pairList = new ArrayList<PairValuesModel>();
            for (int i = 0; i< spInfo.size(); i++){
                PairValuesModel pvm = new PairValuesModel();
                pvm.setName(spInfo.get(i).getSize());
                pvm.setValue(""+spInfo.get(i).getPrice());
                pvm.setChecked(false);
                pairList.add(pvm);
            }

            final View dialogView = View.inflate(context, R.layout.sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView tv_variation_title = dialogView.findViewById(R.id.tv_variation_title);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);

            SelectCodrinkSizeDialogAdapter sAdapter = new SelectCodrinkSizeDialogAdapter(pairList, context);
            RecyclerView recyclerview = dialogView.findViewById(R.id.recyclerview);
            LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(staggeredGridLayoutManager);
            recyclerview.setAdapter(sAdapter);

            tv_variation_title.setText("Select Size");


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPairValuesModel = new PairValuesModel();
                    alertDialog1.dismiss();
                    showActonDialog(sidelinesOrderInfoBella,model);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPairValuesModel != null){
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
                    }
                }
            });
            alertDialog1.setView(dialogView);
            alertDialog1.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void extraToppingDialog(ArrayList<String> sizeStr, BeveragesInfo model){
        try {
            List<PairValuesModel> pairList = new ArrayList<PairValuesModel>();
            PairValuesModel pair = new PairValuesModel();
            for ( int i = 0; i < sizeStr.size(); i++){
                PairValuesModel pvm = new PairValuesModel();
                pvm.setName(sizeStr.get(i));
                pvm.setValue("");
                pvm.setChecked(false);
                pairList.add(pvm);
            }

            final View dialogView = View.inflate(context, R.layout.sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);
            TextView tv_variation_sub_title = dialogView.findViewById(R.id.tv_variation_sub_title);
            TextView tv_variation_title = dialogView.findViewById(R.id.tv_variation_title);
            tv_variation_sub_title.setVisibility(View.GONE);
            tv_variation_title.setText("Select Drink");

            SelectColdrinkDialogAdapter sAdapter = new SelectColdrinkDialogAdapter(pairList, context);
            RecyclerView recyclerview = dialogView.findViewById(R.id.recyclerview);
            LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(staggeredGridLayoutManager);
            recyclerview.setAdapter(sAdapter);

            PairValuesModel finalPair = pair;


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedColdrink = new PairValuesModel();
                    alertDialog1.dismiss();
                    showActonDialog(sidelinesOrderInfoBella, model);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedColdrink != null) {
                        if (selectedColdrink.getName() != null && !selectedColdrink.getName().equals("")) {
                            sidelinesOrderInfoBella.setColdDrinksInfo(selectedColdrink.getName());
                           // sidelinesOrderInfoBella.setTotalPrice(pizzaOrderInfoBella.getTotalPrice() + Integer.parseInt(selectedExtraTopping.getValue()) * pizzaOrderInfoBella.getQuantity());
                            selectedColdrink = new PairValuesModel();
                            showActonDialog(sidelinesOrderInfoBella, model);
                            alertDialog1.dismiss();
                        }
                    }else {
                        Toast.makeText(context, "Please select", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog1.setView(dialogView);
            alertDialog1.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            OrderPreviewFragment hf = (OrderPreviewFragment) fm.findFragmentByTag("OrderPreviewFragment");
            if (hf == null) {
                hf = new OrderPreviewFragment();
                ft.replace(R.id.main_frame, hf, "OrderPreviewFragment");
                ft.addToBackStack("OrderPreviewFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "OrderPreviewFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static void setCodrinkSizePrice(PairValuesModel p) {
        selectedColdrink.setName(p.getName());
        selectedColdrink.setValue(p.getValue());

    }

    public static void setCodrinkSize(PairValuesModel p) {
        selectedPairValuesModel.setName(p.getName());
        selectedPairValuesModel.setValue(p.getValue());

    }
}