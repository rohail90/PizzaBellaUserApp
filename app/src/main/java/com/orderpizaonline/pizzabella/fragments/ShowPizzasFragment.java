package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.ComboDialogAdapter;
import com.orderpizaonline.pizzabella.adapter.SubDialogAdapter;
import com.orderpizaonline.pizzabella.enums.PizzaIngredientCategoryENum;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfoBella;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.model.SizePriceInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class ShowPizzasFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas, crust, pizzaToppings, pizzaExtraToppings;
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

    public static PairValuesModel selectedPairValuesModel = new PairValuesModel();
    public PairValuesModel selectedExtraTopping = new PairValuesModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.show_pizzas_fragment, container, false);
        context = container.getContext();
        Utils.clearModels();
        sidelinesOrderInfoBella = null;
        sidelinesOrderInfoBella = new SidelineOrderInfo();
        sidelinesOrderList.clear();

        ((MainActivity) context).setToolbarTitle("Pizza");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas = firebaseDatabase.getReference(Common.PIZZA);
        crust = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_TOPPING);
        pizzaExtraToppings = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);
        //combos = firebaseDatabase.getReference(Common.COMBO);
        //coldrinks = firebaseDatabase.getReference(Common.SIDE_LINES);

        //getColdrinks();

        recyclerView =v.findViewById(R.id.pizzaRecyclerview);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        showPizzas();
//        if (pizzaOrderInfoBella != null){
//            showActonDialog(pizzaOrderInfoBella);
//        }
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void showPizzas() {

        FirebaseRecyclerOptions<PizzaInfo> options = new FirebaseRecyclerOptions.Builder<PizzaInfo>().setQuery(
                pizzas.orderByChild("id"), PizzaInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<PizzaInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_pizza_sub_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final PizzaInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                //if (model.getDiscount() == 0) {
                 itemPrice.setText("RS." + model.getSizePriceInfo().getSixInchPrice());
                /*}else {
                    itemPrice.setText("RS." + model.getDiscount());
                }*/
                //itemPrice.setVisibility(View.VISIBLE);

                ImageView itemImage=holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);



                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();

                        pizzaOrderInfoBella = null;
                        pizzaOrderInfoBella = new PizzaOrderInfoBella();
                        qty = 1;
                        index4 = -1;
                        crustModel = new SidelineInfo();
                        orderPrice = 0;
                        pizzaOrderInfoBella.setId(model.getId());
                        pizzaOrderInfoBella.setQuantity(1);
                        pizzaOrderInfoBella.setFlavourId(model.getFlavourId());
                        pizzaOrderInfoBella.setImageURL(model.getImageURL());
                        pizzaOrderInfoBella.setItemName(model.getItemName());
                        pizzaOrderInfoBella.setSizePriceInfo(model.getSizePriceInfo());
                        pizzaOrderInfoBella.setTotalPrice(model.getSizePriceInfo().getSixInchPrice());
                        getToppingObject();
                        showActonDialog(pizzaOrderInfoBella);

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    private void showActonDialog(PizzaOrderInfoBella model) {
        try {
            final View dialogView = View.inflate(context, R.layout.general_items_customization_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            TextView itemName = dialogView.findViewById(R.id.tv_item_name);
            TextView tv_price = dialogView.findViewById(R.id.tv_price);
            TextView tv_total_price = dialogView.findViewById(R.id.tv_total_price);
            TextView variation_tag = dialogView.findViewById(R.id.variation_tag);
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

            ConstraintLayout extra_topping_layout = dialogView.findViewById(R.id.extra_topping_layout);
            TextView tv_extra_topping_change = dialogView.findViewById(R.id.tv_extra_topping_change);
            TextView tv_extra_topping_required = dialogView.findViewById(R.id.tv_extra_topping_required);
            TextView tv_extra_topping_size = dialogView.findViewById(R.id.tv_extra_topping_size);
            TextView extra_topping_additional_price_tag = dialogView.findViewById(R.id.extra_topping_additional_price_tag);
            TextView tv_extra_topping_additional_price = dialogView.findViewById(R.id.tv_extra_topping_additional_price);


            itemName.setText(model.getItemName());
            variation_tag.setText("Select Size");

            tv_change.setText("Choose");
            if (model.getSizePrice() != null && !model.getSizePrice().equals("")){
                getExtraTopping();
                tv_change.setText("Change");
                extra_topping_layout.setVisibility(View.VISIBLE);
                tv_price.setText("Rs: " +model.getSizePrice());
                tv_total_price.setText("" + model.getTotalPrice());
                tv_required.setVisibility(View.GONE);
                tv_size.setVisibility(View.VISIBLE);
                tv_additional_price.setVisibility(View.VISIBLE);
                additional_price_tag.setVisibility(View.VISIBLE);
                tv_size.setText(model.getSizeName());
                tv_quantity.setText(model.getQuantity()+"");
                tv_additional_price.setText(model.getSizePrice());
                if (model.getExtraTopping() != null){
                    tv_extra_topping_required.setVisibility(View.GONE);
                    tv_extra_topping_size.setVisibility(View.VISIBLE);
                    tv_extra_topping_additional_price.setVisibility(View.VISIBLE);
                    extra_topping_additional_price_tag.setVisibility(View.VISIBLE);

                    tv_extra_topping_change.setText("Change");
                    tv_extra_topping_size.setText(model.getExtraTopping().getName());
                    tv_extra_topping_additional_price.setText(model.getExtraTopping().getValue());
                }

            }else {
                tv_price.setText("from Rs: " +model.getSizePriceInfo().getSixInchPrice());
                tv_total_price.setText("" + model.getTotalPrice());
            }

            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model.getSizePrice() != null /*&& !model.getSizePrice().equals("")*/) {
                        qty++;
                        if (model.getExtraTopping() != null){
                            pizzaOrderInfoBella.setTotalPrice((Integer.parseInt(model.getSinglePrice())/* + Integer.parseInt(model.getSizePrice())*/) * qty);

                            tv_total_price.setText(" " +    (Integer.parseInt(model.getSinglePrice()) /*+ Integer.parseInt(model.getSizePrice())*/) * qty);
                        }else {
                            pizzaOrderInfoBella.setTotalPrice(Integer.parseInt(model.getSizePrice()) * qty);

                             tv_total_price.setText(" " +    Integer.parseInt(model.getSizePrice()) * qty);
                        }
                        tv_quantity.setText("" + qty);
                        pizzaOrderInfoBella.setQuantity(qty);
                    }
                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (qty > 1){
                       qty--;

                       if (model.getExtraTopping() != null/* && !model.getSizePrice().equals("")*/){
                           pizzaOrderInfoBella.setTotalPrice((Integer.parseInt(model.getSinglePrice()) /*+ Integer.parseInt(model.getSizePrice())*/) * qty);

                           tv_total_price.setText(" " +(Integer.parseInt(model.getSinglePrice()) /*+ Integer.parseInt(model.getSizePrice())*/) * qty);
                       }else {
                           pizzaOrderInfoBella.setTotalPrice(Integer.parseInt(model.getSizePrice()) * qty);

                           tv_total_price.setText(" " +    Integer.parseInt(model.getSizePrice()) * qty);
                       }
                      // tv_total_price.setText(" " + /*(Integer.parseInt(model.getSizePrice()) * qty)*/ pizzaOrderInfoBella.getTotalPrice());
                       pizzaOrderInfoBella.setQuantity(qty);
                       tv_quantity.setText("" + qty);
                       /*
                       pizzaOrderInfoBella.setTotalPrice(Integer.parseInt(model.getSizePrice()) * qty);
                       pizzaOrderInfoBella.setQuantity(qty);*/
                   }
                }
            });

            tv_extra_topping_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sidelineInfoList.size() > 0) {
                        extraToppingDialog(pizzaOrderInfoBella.getSizeName());
                        alertDialog.dismiss();
                    }else {
                        Toast.makeText(context, "No Extra Topping Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    variationDialog(model.getSizePriceInfo());
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
                    if (pizzaOrderInfoBella.getSizePrice() != null && !pizzaOrderInfoBella.getSizePrice().equals("")) {
                        if (pizzaOrderInfoBella.getPizzaToppingInfo() != null){

                            crustDialog();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(context, "Flavour not available", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(context, "Please Select Size", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void crustDialog(){
        try {

            final View dialogView = View.inflate(context, R.layout.select_crust_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_addtocart);

            crustRecyclerView = dialogView.findViewById(R.id.pizzaCrustREcyclerview);
            StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            crustRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);
            orderPrice = pizzaOrderInfoBella.getTotalPrice();


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showActonDialog(pizzaOrderInfoBella);
                    alertDialog1.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pizzaOrderInfoBella.getCrust() != null) {
                        if (pizzaOrderInfoBella.getCrust().getSidelineName() != null) {
                            int i = pizzaOrderInfoBella.getTotalPrice() / pizzaOrderInfoBella.getQuantity();
                            pizzaOrderInfoBella.setSinglePrice(i+"");
                            setNextPhoneFragment();
                            alertDialog1.dismiss();
                        }
                    }
                }
            });
            alertDialog1.setView(dialogView);
            alertDialog1.show();
            showCrust();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void showCrust() {


        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                crust.orderByChild("category").equalTo(PizzaIngredientCategoryENum.Crust.toString()), SidelineInfo.class).build();
        crustAdapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sideslines_item, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final SidelineInfo model) {

                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getSidelineName());

                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                if (model.getDiscount() != 0) {
                    itemPrice.setText(model.getDiscount());
                } else {
                    itemPrice.setText(model.getPrice());
                }

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);

                //final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item = holder.itemView.findViewById(R.id.ll_sideline_item);

                final RadioButton selection = holder.itemView.findViewById(R.id.selection);

                if (index4 == position) {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                    selection.setChecked(true);
                } else {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                    selection.setChecked(false);
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        index4 = position;
                        notifyDataSetChanged();

                        if (crustModel != null) {
                            if (crustModel.getDiscount() != 0) {
                                if (orderPrice != 0) {
                                    orderPrice = orderPrice - crustModel.getDiscount();
                                }
                            } else {
                                if (orderPrice != 0) {
                                    if (crustModel.getPrice() != null && !crustModel.getPrice().equals("")) {
                                        orderPrice = orderPrice - Integer.parseInt(crustModel.getPrice());
                                    }
                                }
                            }
                        }

                        if (model.getDiscount() != 0) {
                            orderPrice = orderPrice + model.getDiscount();
                        } else {
                            if (model.getPrice() != null && !model.getPrice().equals("")) {
                                orderPrice = orderPrice + Integer.parseInt(model.getPrice());
                            }
                        }
                        crustModel = model;
                        pizzaOrderInfoBella.setCrust(model);
                        pizzaOrderInfoBella.setTotalPrice(orderPrice);

                    }
                });
            }
        };
        crustAdapter.startListening();
        crustRecyclerView.setAdapter(crustAdapter);

    }

    private void variationDialog(SizePriceInfo spInfo){
        try {
            List<PairValuesModel> pairList = new ArrayList<PairValuesModel>();
            PairValuesModel pvm = new PairValuesModel();

            pvm.setName(spInfo.getSixInchSize());
            pvm.setValue(""+spInfo.getSixInchPrice());
            pvm.setChecked(false);
            pairList.add(pvm);

            PairValuesModel pvm1 = new PairValuesModel();
            pvm1.setName(spInfo.getEightInchSize());
            pvm1.setValue(""+spInfo.getEightInchPrice());
            pvm1.setChecked(false);
            pairList.add(pvm1);

            PairValuesModel pvm2 = new PairValuesModel();
            pvm2.setName(spInfo.getTenInchSize());
            pvm2.setValue(""+spInfo.getTenInchPrice());
            pvm2.setChecked(false);
            pairList.add(pvm2);

            PairValuesModel pvm3 = new PairValuesModel();
            pvm3.setName(spInfo.getTwelveInchSize());
            pvm3.setValue(""+spInfo.getTwelveInchPrice());
            pvm3.setChecked(false);
            pairList.add(pvm3);

            PairValuesModel pvm4 = new PairValuesModel();
            pvm4.setName(spInfo.getFourteenInchSize());
            pvm4.setValue(""+spInfo.getFourteenInchPrice());
            pvm4.setChecked(false);
            pairList.add(pvm4);

            PairValuesModel pvm5 = new PairValuesModel();
            pvm5.setName(spInfo.getSixteenInchSize());
            pvm5.setValue(""+spInfo.getSixteenInchPrice());
            pvm5.setChecked(false);
            pairList.add(pvm5);


            final View dialogView = View.inflate(context, R.layout.sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView tv_variation_title = dialogView.findViewById(R.id.tv_variation_title);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);

            SubDialogAdapter sAdapter = new SubDialogAdapter(pairList, context);
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
                    showActonDialog(pizzaOrderInfoBella);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pizzaOrderInfoBella.setSizeName(selectedPairValuesModel.getName());
                    pizzaOrderInfoBella.setSizePrice(selectedPairValuesModel.getValue());
                    pizzaOrderInfoBella.setTotalPrice(Integer.parseInt(selectedPairValuesModel.getValue()));
                    if (pizzaOrderInfoBella.getExtraTopping() != null) {
                        if (pizzaOrderInfoBella.getExtraTopping().getName() != null && !pizzaOrderInfoBella.getExtraTopping().getName().equals("")) {
                            pizzaOrderInfoBella.setExtraTopping(new PairValuesModel());
                        }
                    }
                    alertDialog1.dismiss();
                    showActonDialog(pizzaOrderInfoBella);
                    selectedPairValuesModel = new PairValuesModel();
                }
            });
            alertDialog1.setView(dialogView);
            alertDialog1.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void extraToppingDialog(String sizeStr){
        try {
            List<PairValuesModel> pairList = new ArrayList<PairValuesModel>();
            SizePriceInfo spInfo = sidelineInfoList.get(0).getSizePriceInfo();
            PairValuesModel pvm = new PairValuesModel();

            pvm.setName(spInfo.getSixInchSize());
            pvm.setValue(""+spInfo.getSixInchPrice());
            pvm.setChecked(false);
            pairList.add(pvm);

            PairValuesModel pvm1 = new PairValuesModel();
            pvm1.setName(spInfo.getEightInchSize());
            pvm1.setValue(""+spInfo.getEightInchPrice());
            pvm1.setChecked(false);
            pairList.add(pvm1);

            PairValuesModel pvm2 = new PairValuesModel();
            pvm2.setName(spInfo.getTenInchSize());
            pvm2.setValue(""+spInfo.getTenInchPrice());
            pvm2.setChecked(false);
            pairList.add(pvm2);

            PairValuesModel pvm3 = new PairValuesModel();
            pvm3.setName(spInfo.getTwelveInchSize());
            pvm3.setValue(""+spInfo.getTwelveInchPrice());
            pvm3.setChecked(false);
            pairList.add(pvm3);

            PairValuesModel pvm4 = new PairValuesModel();
            pvm4.setName(spInfo.getFourteenInchSize());
            pvm4.setValue(""+spInfo.getFourteenInchPrice());
            pvm4.setChecked(false);
            pairList.add(pvm4);

            PairValuesModel pvm5 = new PairValuesModel();
            pvm5.setName(spInfo.getSixteenInchSize());
            pvm5.setValue(""+spInfo.getSixteenInchPrice());
            pvm5.setChecked(false);
            pairList.add(pvm5);
            PairValuesModel pair = new PairValuesModel();

            for ( int i = 0; i < pairList.size(); i++){
                if (sizeStr.equals(pairList.get(i).getName())){
                    pair = pairList.get(i);
                }
            }


            final View dialogView = View.inflate(context, R.layout.extra_topping_sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);
            ConstraintLayout select = dialogView.findViewById(R.id.recyclerview);

            ImageView checked = dialogView.findViewById(R.id.checked);
            TextView additional_price_tag = dialogView.findViewById(R.id.additional_price_tag);
            TextView tv_additional_price = dialogView.findViewById(R.id.tv_additional_price);
            if (pair != null){
                additional_price_tag.setText(pair.getName());
                tv_additional_price.setText(pair.getValue());
            }

            PairValuesModel finalPair = pair;
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedExtraTopping = new PairValuesModel();
                    selectedExtraTopping = finalPair;
                    checked.setImageDrawable(context.getResources().getDrawable(R.drawable.email_icon));
                }
            });


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedExtraTopping = new PairValuesModel();
                    alertDialog1.dismiss();
                    showActonDialog(pizzaOrderInfoBella);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedExtraTopping != null) {
                        if (selectedExtraTopping.getName() != null && !selectedExtraTopping.getName().equals("")) {

                            if (pizzaOrderInfoBella.getExtraTopping() == null) {
                                pizzaOrderInfoBella.setExtraTopping(selectedExtraTopping);
                                pizzaOrderInfoBella.setSinglePrice(Integer.parseInt(pizzaOrderInfoBella.getSizePrice()) + Integer.parseInt(selectedExtraTopping.getValue()) + "");
                                pizzaOrderInfoBella.setTotalPrice(pizzaOrderInfoBella.getTotalPrice() + Integer.parseInt(selectedExtraTopping.getValue()) * pizzaOrderInfoBella.getQuantity());
                                selectedExtraTopping = new PairValuesModel();
                                showActonDialog(pizzaOrderInfoBella);
                                alertDialog1.dismiss();
                            }else {
                                selectedExtraTopping = new PairValuesModel();
                                showActonDialog(pizzaOrderInfoBella);
                                alertDialog1.dismiss();
                            }
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


    public static void setSizePrice(PairValuesModel p) {
        selectedPairValuesModel.setName(p.getName());
        selectedPairValuesModel.setValue(p.getValue());

    }

    private void getToppingObject() {
        pizzaToppingInfo = new ArrayList<>();
        pizzaToppings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pizzaToppingInfo.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaToppingInfo chat = snapshot.getValue(PizzaToppingInfo.class);
                    if (chat.getId().equals(pizzaOrderInfoBella.getFlavourId())) {
                        pizzaToppingInfo.add(chat);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (pizzaToppingInfo.size() > 0){
            pizzaOrderInfoBella.setPizzaToppingInfo(pizzaToppingInfo.get(0));
        }
    }

    private void getExtraTopping() {
        sidelineInfoList = new ArrayList<>();
        pizzaExtraToppings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sidelineInfoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SidelineInfo chat = snapshot.getValue(SidelineInfo.class);
                    if (chat.getCategory().equals(PizzaIngredientCategoryENum.Extra_Topping.toString())) {
                        sidelineInfoList.add(chat);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (pizzaToppingInfo.size() > 0){
            pizzaOrderInfoBella.setPizzaToppingInfo(pizzaToppingInfo.get(0));
        }
    }


}