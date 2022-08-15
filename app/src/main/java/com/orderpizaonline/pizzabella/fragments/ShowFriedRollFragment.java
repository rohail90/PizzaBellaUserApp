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
import com.orderpizaonline.pizzabella.adapter.ComboDialogAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectFriedRollAddOnDialogAdapter;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.FriedRollInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.OptionalInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.QuantityInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.friedRollOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class ShowFriedRollFragment extends Fragment implements View.OnClickListener {
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
        sidelinesOrderList.clear();

        ((MainActivity) context).setToolbarTitle("Fried/Roll");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas = firebaseDatabase.getReference(Common.FRIED_ROLL);
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

        FirebaseRecyclerOptions<FriedRollInfo> options = new FirebaseRecyclerOptions.Builder<FriedRollInfo>().setQuery(
                pizzas.orderByChild("id"), FriedRollInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<FriedRollInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_pizza_sub_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final FriedRollInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                //if (model.getDiscount() == 0) {
                 itemPrice.setText("RS." + model.getPrice());
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

                        qty = 1;
                        orderPrice = 0;
                        friedRollOrderInfoBella = null;
                        friedRollOrderInfoBella = new FriedRollOrderInfo();
                        friedRollOrderInfoBella.setId(model.getId());
                        friedRollOrderInfoBella.setQuantity(1);
                        friedRollOrderInfoBella.setImageURL(model.getImageURL());
                        friedRollOrderInfoBella.setItemName(model.getItemName());
                        friedRollOrderInfoBella.setSinglePrice(model.getPrice());
                        friedRollOrderInfoBella.setTotalPrice(model.getPrice());
                        showActonDialog(model);


                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    private void showActonDialog(FriedRollInfo model) {
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
            TextView variation_tag = dialogView.findViewById(R.id.variation_tag);
            TextView textView5 = dialogView.findViewById(R.id.textView5);

            ConstraintLayout variation_layout = dialogView.findViewById(R.id.variation_layout);
            itemName.setText(model.getItemName());

            if (model.getVariationList() != null){
                variation_tag.setText("Variation");
                if (friedRollOrderInfoBella.getVariationList() != null) {

                    tv_price.setText("Rs: " + friedRollOrderInfoBella.getSinglePrice());
                    tv_total_price.setText("" + friedRollOrderInfoBella.getTotalPrice());
                    tv_change.setText("Change");
                    tv_required.setVisibility(View.GONE);
                    tv_size.setVisibility(View.VISIBLE);
                    tv_additional_price.setVisibility(View.VISIBLE);
                    additional_price_tag.setVisibility(View.VISIBLE);
                    tv_size.setText(friedRollOrderInfoBella.getVariationList().getNoOfPieces());
                    tv_additional_price.setText(friedRollOrderInfoBella.getVariationList().getPrice()+"");

                }else {

                    tv_price.setText("Rs: " + model.getPrice());
                    tv_total_price.setText("" + model.getPrice());
                }


            }else {
                textView5.setVisibility(View.GONE);
                variation_layout.setVisibility(View.GONE);
                tv_price.setText("Rs: " +model.getPrice());
                tv_total_price.setText("" + model.getPrice());
            }

            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    qty++;
                    tv_quantity.setText("" + qty);
                    if (friedRollOrderInfoBella.getVariationList()!=null) {
                        tv_total_price.setText(" " + (friedRollOrderInfoBella.getSinglePrice()/*+friedRollOrderInfoBella.getVariationList().getPrice()*/) * qty);
                        friedRollOrderInfoBella.setTotalPrice((friedRollOrderInfoBella.getSinglePrice()/*+friedRollOrderInfoBella.getVariationList().getPrice()*/) * qty);
                    }else {
                        friedRollOrderInfoBella.setTotalPrice(friedRollOrderInfoBella.getSinglePrice() * qty);
                        tv_total_price.setText(""+ friedRollOrderInfoBella.getSinglePrice() * qty);

                    }
                    friedRollOrderInfoBella.setQuantity(qty);

                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (qty > 1){
                       qty--;
                       tv_quantity.setText("" + qty);
                       if (friedRollOrderInfoBella.getVariationList()!=null) {
                       /*if (friedRollOrderInfoBella.getVariationList().getPrice() != 0 ) {
                           friedRollOrderInfoBella.setTotalPrice((friedRollOrderInfoBella.getSinglePrice()*//*+friedRollOrderInfoBella.getVariationList().getPrice()*//*) * qty);
                       }*/
                           tv_total_price.setText(" " + (friedRollOrderInfoBella.getSinglePrice()/*+friedRollOrderInfoBella.getVariationList().getPrice()*/) * qty);
                           friedRollOrderInfoBella.setTotalPrice((friedRollOrderInfoBella.getSinglePrice()/*+friedRollOrderInfoBella.getVariationList().getPrice()*/) * qty);
                       }else {
                           friedRollOrderInfoBella.setTotalPrice(friedRollOrderInfoBella.getSinglePrice() * qty);
                           tv_total_price.setText(""+ friedRollOrderInfoBella.getSinglePrice() * qty);

                       }
                       friedRollOrderInfoBella.setQuantity(qty);
                   }
                }
            });

            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    variationDialog(model);
                    alertDialog.dismiss();
                }
            });

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friedRollOrderInfoBella = null;
                    friedRollOrderInfoBella = new FriedRollOrderInfo();
                    alertDialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friedRollOrderInfoBella.setTotalPrice(friedRollOrderInfoBella.getSinglePrice() * qty);
                    friedRollOrderInfoBella.setType(Common.FRIED_ROLL);
                    alertDialog.dismiss();
                    setNextPhoneFragment();

                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

   /* private void extraToppingDialog(BurgerOrSandwichInfo sizeStr){
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

    private void variationDialog(FriedRollInfo spInfo){
        try {
            List<PairValuesModel> pairList = new ArrayList<PairValuesModel>();
            for (int i = 0; i< spInfo.getVariationList().size(); i++){
                PairValuesModel pvm = new PairValuesModel();
                pvm.setName(spInfo.getVariationList().get(i).getNoOfPieces());
                pvm.setValue(""+spInfo.getVariationList().get(i).getPrice());
                pvm.setChecked(false);
                pairList.add(pvm);
            }

            final View dialogView = View.inflate(context, R.layout.sub_dialog, null);
            final AlertDialog alertDialog1 = new AlertDialog.Builder(context).create();
            alertDialog1.setCancelable(false);
            TextView tv_variation_title = dialogView.findViewById(R.id.tv_variation_title);
            TextView noBtn = dialogView.findViewById(R.id.tv_btn_cancel);
            TextView yesBtn = dialogView.findViewById(R.id.tv_btn_confirm);

            SelectFriedRollAddOnDialogAdapter sAdapter = new SelectFriedRollAddOnDialogAdapter(pairList, context);
            RecyclerView recyclerview = dialogView.findViewById(R.id.recyclerview);
            LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(staggeredGridLayoutManager);
            recyclerview.setAdapter(sAdapter);

            tv_variation_title.setText("Add On");


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPairValuesModel = new PairValuesModel();
                    alertDialog1.dismiss();
                    showActonDialog(spInfo);
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPairValuesModel != null){
                        if (selectedPairValuesModel.getValue() != null && !selectedPairValuesModel.getValue().equals("")){
                            QuantityInfo si = new QuantityInfo();
                            si.setNoOfPieces(selectedPairValuesModel.getName());
                            si.setPrice(Integer.parseInt(selectedPairValuesModel.getValue()));
                            friedRollOrderInfoBella.setVariationList(si);
                            if (friedRollOrderInfoBella.getVariationList().getPrice() != 0){
                                friedRollOrderInfoBella.setSinglePrice(friedRollOrderInfoBella.getVariationList().getPrice());
                            }
                            friedRollOrderInfoBella.setTotalPrice(friedRollOrderInfoBella.getSinglePrice() * friedRollOrderInfoBella.getQuantity());
                            alertDialog1.dismiss();
                            showActonDialog(spInfo);
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

    public static void setAddOn(PairValuesModel p) {
        selectedPairValuesModel.setName(p.getName());
        selectedPairValuesModel.setValue(p.getValue());

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


}