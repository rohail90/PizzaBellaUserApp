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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.ColdPizSubDialogAdapter;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ColdPizModel;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.OptionalInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.comboOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.finalColdPizList;
import static com.orderpizaonline.pizzabella.MainActivity.selectColdPizItm;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;


public class ShowMealsFragment extends Fragment implements View.OnClickListener {
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
    ColdPizSubDialogAdapter coldPizSubDialogAdapter;
    int qty = 1;


    FirebaseRecyclerAdapter crustAdapter;
    RecyclerView crustRecyclerView;
    private SidelineInfo crustModel = new SidelineInfo();
    int index4 = -1;
    int orderPrice = 0;
    List<PizzaToppingInfo> pizzaToppingInfo;
    List<SidelineInfo> sidelineInfoList;

    String des = "";
/*    public static PairValuesModel selectColdPizItm = new PairValuesModel();
    public static List<ColdPizModel> finalColdPizList = new ArrayList<>();*/
    public OptionalInfo selectedExtraTopping = new OptionalInfo();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.show_pizzas_fragment, container, false);
        context = container.getContext();
        Utils.clearModels();
        sidelinesOrderInfoBella = null;
        sidelinesOrderInfoBella = new SidelineOrderInfo();
        //sidelinesOrderList.clear();

        ((MainActivity) context).setToolbarTitle("Meals");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas = firebaseDatabase.getReference(Common.MEAL);
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
       /* if (pizzaOrderInfoBella != null){
            showActonDialog(pizzaOrderInfoBella);
        }*/
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void showPizzas() {

        FirebaseRecyclerOptions<ComboInfo> options = new FirebaseRecyclerOptions.Builder<ComboInfo>().setQuery(
                pizzas.orderByChild("id"), ComboInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<ComboInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_sub_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final ComboInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                TextView description = holder.itemView.findViewById(R.id.description);
                //if (model.getDiscount() == 0) {
                 itemPrice.setText("RS." + model.getPrice());
                /*}else {
                    itemPrice.setText("RS." + model.getDiscount());
                }*/
                //itemPrice.setVisibility(View.VISIBLE);

                ImageView itemImage=holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);

                int indx = 0;
                des = "";
                des = getDescription(model, indx);
                description.setText(des);


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();

                        comboOrderInfoBella = null;
                        comboOrderInfoBella = new ComboOrderInfo();
                        selectColdPizItm = new PairValuesModel();
                        finalColdPizList.clear();

                        qty = 1;
                        orderPrice = 0;
                        comboOrderInfoBella.setId(model.getId());
                        comboOrderInfoBella.setQuantity(1);
                        comboOrderInfoBella.setImageURL(model.getImageURL());
                        comboOrderInfoBella.setItemName(model.getItemName());
                        //if (model.getDiscount() == 0) {
                            comboOrderInfoBella.setPrice(model.getPrice()+"");
                            comboOrderInfoBella.setTotalPrice(Integer.parseInt(model.getPrice()));

                        /*}else {
                            comboOrderInfoBella.setPrice(model.getDiscount()+"");
                        }*/


                        comboOrderInfoBella.setDealDescription(getDescription(model, indx));
                        showActonDialog(model);


                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private String getDescription(@NonNull ComboInfo model, int indx) {
        String des = "";
        if (model.getBurgerOrSandwichInfoList()!= null){
            if (model.getBurgerOrSandwichInfoList().size() > 0){
                for (int i = 0; i < model.getBurgerOrSandwichInfoList().size(); i++){
                    if (i == 0){
                        des += model.getBurgerOrSandwichInfoList().get(i).getQuantity()+ " , "+ model.getBurgerOrSandwichInfoList().get(i).getItemName();
                    }else {
                        des += model.getBurgerOrSandwichInfoList().get(i).getQuantity()+" , "+ model.getBurgerOrSandwichInfoList().get(i).getItemName();
                    }
                }
                indx++;
            }
        }

        if (model.getFriedRollInfoList()!= null) {
            if (model.getFriedRollInfoList().size() > 0) {
                for (int i = 0; i < model.getFriedRollInfoList().size(); i++) {
                    if (i == 0) {
                        if (indx > 0) {
                            des += model.getFriedRollInfoList().get(i).getQuantity()+" , " + model.getFriedRollInfoList().get(i).getItemName();
                        } else {
                            des += model.getFriedRollInfoList().get(i).getQuantity()+" , "+model.getFriedRollInfoList().get(i).getItemName();
                        }
                    } else {
                        des += model.getFriedRollInfoList().get(i).getQuantity()+ " , " + model.getFriedRollInfoList().get(i).getItemName();
                    }
                }
            }
        }

        if (model.getBeveragesInfoInCombo()!= null) {
            des += ", " + model.getBeveragesInfoInCombo().getQuantiy() + " , " + model.getBeveragesInfoInCombo().getDrinksSize()
                    + " soft drink(s)";
        }
        return des;
    }


    private void showActonDialog(ComboInfo model) {
        try {
            final View dialogView = View.inflate(context, R.layout.meal_general_dialog, null);
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
            TextView variation_tag = dialogView.findViewById(R.id.variation_tag);
            TextView textView5 = dialogView.findViewById(R.id.textView5);

            ConstraintLayout variation_layout = dialogView.findViewById(R.id.variation_layout);
            RecyclerView recyclerViews = dialogView.findViewById(R.id.cold_piz_recyclerview);
            itemName.setText(model.getItemName());
            tv_price.setText(model.getPrice());
            tv_total_price.setText("" + model.getPrice());

            if (model.getBeveragesInfoInCombo().getQuantiy() > 0){
                variation_layout.setVisibility(View.GONE);
                recyclerViews.setVisibility(View.VISIBLE);
                variation_tag.setText("Coldrinks");
                    if (finalColdPizList.size() == 0){
                        for (int i = 0; i<model.getBeveragesInfoInCombo().getQuantiy(); i++){
                            ColdPizModel po = new ColdPizModel();
                            po.setName("Coldrink "+ (i+1));
                            po.setSize("");
                            po.setPrice("");
                            finalColdPizList.add(po);
                        }
                    }

                coldPizSubDialogAdapter = new ColdPizSubDialogAdapter(finalColdPizList, context);
                LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
                recyclerViews.setLayoutManager(staggeredGridLayoutManager);
                recyclerViews.setAdapter(coldPizSubDialogAdapter);

/*
                if (burgSandOrderInfoBella.getOptionalInfo() != null) {


                    tv_price.setText("Rs: " + burgSandOrderInfoBella.getPrice());
                    tv_total_price.setText("" + burgSandOrderInfoBella.getTotalPrice());

                    tv_required.setVisibility(View.GONE);
                    tv_size.setVisibility(View.VISIBLE);
                    tv_additional_price.setVisibility(View.VISIBLE);
                    additional_price_tag.setVisibility(View.VISIBLE);
                    tv_size.setText(burgSandOrderInfoBella.getOptionalInfo().getOptionalName());
                    tv_additional_price.setText(burgSandOrderInfoBella.getOptionalInfo().getOptionalPrice()+"");

                }else {

                    tv_price.setText("Rs: " + model.getPrice());
                    tv_total_price.setText("" + model.getPrice());
                }*/


            }else {
                textView5.setVisibility(View.GONE);
                recyclerViews.setVisibility(View.GONE);
                variation_layout.setVisibility(View.GONE);
                tv_price.setText("Rs: " +model.getPrice());
                tv_total_price.setText("" + model.getPrice());
            }

            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (comboOrderInfoBella.getColdPizList()!=null) {
                        qty++;
                        tv_quantity.setText("" + qty);

                        tv_total_price.setText(" " + Integer.parseInt(comboOrderInfoBella.getPrice()) * qty);
                        comboOrderInfoBella.setTotalPrice(Integer.parseInt(comboOrderInfoBella.getPrice()) * qty);
                        comboOrderInfoBella.setQuantity(qty);
                    }


                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (qty > 1) {
                       qty--;
                       tv_quantity.setText("" + qty);
                       tv_total_price.setText(" " + Integer.parseInt(comboOrderInfoBella.getPrice()) * qty);
                       comboOrderInfoBella.setTotalPrice(Integer.parseInt(comboOrderInfoBella.getPrice()) * qty);
                       comboOrderInfoBella.setQuantity(qty);

                   }
                }
            });

           /* tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    extraToppingDialog(model);
                    alertDialog.dismiss();
                }
            });*/

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    comboOrderInfoBella = null;
                    comboOrderInfoBella = new ComboOrderInfo();
                    selectColdPizItm = new PairValuesModel();
                    finalColdPizList.clear();
                    alertDialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<PairValuesModel> tmp = new ArrayList<>();
                    if (finalColdPizList.size() == model.getBeveragesInfoInCombo().getQuantiy()){
                        for (int i = 0; i<finalColdPizList.size(); i++){
                            if (finalColdPizList.get(i).getSize() != null && !finalColdPizList.get(i).getSize().equals("") ) {
                                PairValuesModel pi = new PairValuesModel();
                                pi.setName(finalColdPizList.get(i).getSize());
                                pi.setValue("");

                                tmp.add(pi);
                            }
                        }
                    }


                    if (tmp.size() == model.getBeveragesInfoInCombo().getQuantiy()){
                        comboOrderInfoBella.setColdPizList(tmp);
                        comboOrderInfoBella.setType(Common.MEAL);
                        alertDialog.dismiss();
                        setNextPhoneFragment();
                    }else {
                        Toast.makeText(context, "Please Select Drinks", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

  /*  private void extraToppingDialog(BurgerOrSandwichInfo sizeStr){
        try {

            final View dialogView = View.inflate(context, R.layout.extra_topping_sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);
            ConstraintLayout select = dialogView.findViewById(R.id.recyclerview);

            ImageView checked = dialogView.findViewById(R.id.checked);
            TextView additional_price_tag = dialogView.findViewById(R.id.additional_price_tag);
            TextView tv_additional_price = dialogView.findViewById(R.id.tv_additional_price);
            if (sizeStr.getOptionalInfo() != null){
                additional_price_tag.setText(sizeStr.getOptionalInfo().getOptionalName());
                tv_additional_price.setText(sizeStr.getOptionalInfo().getOptionalPrice()+"");
            }

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedExtraTopping = new OptionalInfo();
                    selectedExtraTopping = sizeStr.getOptionalInfo();
                    checked.setImageDrawable(context.getResources().getDrawable(R.drawable.email_icon));
                }
            });


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedExtraTopping = new OptionalInfo();
                    alertDialog1.dismiss();
                    showActonDialog(sizeStr);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedExtraTopping != null) {
                        if (selectedExtraTopping.getOptionalName() != null && !selectedExtraTopping.getOptionalName().equals("")) {
                            burgSandOrderInfoBella.setOptionalInfo(selectedExtraTopping);
                            burgSandOrderInfoBella.setTotalPrice(burgSandOrderInfoBella.getTotalPrice() + sizeStr.getOptionalInfo().getOptionalPrice() * burgSandOrderInfoBella.getQuantity());
                            selectedExtraTopping = new OptionalInfo();
                            showActonDialog(sizeStr);
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

    }*/

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


}