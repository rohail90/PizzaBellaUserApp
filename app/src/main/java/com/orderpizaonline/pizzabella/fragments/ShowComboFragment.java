package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.PizzaInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfoInCombo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class ShowComboFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas, combos;
    FirebaseRecyclerAdapter adapter, dessertsAdapter;
    RecyclerView recyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    RecyclerView combo_dialog_recycler;
    ComboDialogAdapter comboDialogAdapter;

    ArrayList<ComboInfo> combosList;
    ArrayList<PizzaInfo> pizzasList;

    String description = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.show_pizzas_fragment, container, false);
        context = container.getContext();
        ((MainActivity) context).setToolbarTitle("Order Combos");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        combos = firebaseDatabase.getReference(Common.COMBO);
        pizzas = firebaseDatabase.getReference(Common.PIZZA);

        recyclerView = v.findViewById(R.id.pizzaRecyclerview);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        showPizzas();
        //getPizzas();
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void showPizzas() {

        FirebaseRecyclerOptions<ComboInfo> options = new FirebaseRecyclerOptions.Builder<ComboInfo>().setQuery(
                combos.orderByChild("id"), ComboInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<ComboInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_sub_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final ComboInfo model) {

                final String mealID = adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                if (model.getDiscount() == 0) {
                    itemPrice.setText("RS." + model.getPrice());
                }else {
                    itemPrice.setText("RS." + model.getDiscount());
                }
                TextView itemDescription = holder.itemView.findViewById(R.id.description);
                description = "";
                if (model.getPizzaInfo().getPizzaName() != null && !model.getPizzaInfo().getPizzaName().equals("")) {
                    description = description + model.getPizzaInfo().getQuantiy() + " " + model.getPizzaInfo().getPizzaName();
                }

//                for (SidelineInfoInCombo models : model.getSidelineInfo()) {
//                    if (!models.getSidelineName().equals("")) {
//                        if (!description.equals("")) {
//                            description = description + ", " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                        } else {
//                            description = description + " " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                        }
//                    }
//                }
//
//                if (model.getDrinksInfo().getQuantiy() != 0) {
//                    if (!description.equals("")) {
//                        description = description + ", " + model.getDrinksInfo().getQuantiy() + " " + model.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//                    } else {
//                        description = description + " " + model.getDrinksInfo().getQuantiy() + " " + model.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//
//                    }
//                }

                itemDescription.setText(description);
                itemPrice.setVisibility(View.VISIBLE);

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);

                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                        //showActonDialog();
                        if (model.getPizzaInfo() != null) {

                            showActonDialog(model.getPizzaInfo().getPizzaName(), model);

                        } else {
                            showActonDialog("", model);
                            //Log.d("hassan", model.getPizzaInfo().getPizzaName());
                        }
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void showActonDialog(String pizzaName, ComboInfo model) {
        try {
            Toast.makeText(getContext(), "jj" + pizzaName, Toast.LENGTH_SHORT).show();
            final View dialogView = View.inflate(context, R.layout.show_combo_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(true);
            combo_dialog_recycler = dialogView.findViewById(R.id.tv_item_name);

            LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            combo_dialog_recycler.setLayoutManager(staggeredGridLayoutManager);

            Button noBtn = dialogView.findViewById(R.id.button);
            Button yesBtn = dialogView.findViewById(R.id.btn_yes);

            if (model.getPizzaInfo() != null) {
                if (pizzaName != null && !pizzaName.equals("")) {
                    noBtn.setText("Skip");
                    yesBtn.setText("Add Combo");
                } else {
                    noBtn.setText("Dismiss");
                    yesBtn.setText("Add to cart");
                }
            } else {
                noBtn.setText("Dismiss");
                yesBtn.setText("Add to cart");
            }


            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (model.getPizzaInfo() != null) {
                        if (pizzaName != null && !pizzaName.equals("")) {

                            //pizzaOrderInfoModel.setComboInfo(comboDialogAdapter.selectedCombo);
                            pizzaOrderInfoModel.setPizzaName(model.getPizzaInfo().getPizzaName());
                            pizzaOrderInfoModel.setImageURL(model.getPizzaInfo().getImageURL());
                            pizzaOrderInfoModel.setId(model.getPizzaInfo().getId());
                            pizzaOrderInfoModel.setQuantity(model.getPizzaInfo().getQuantiy());
                            pizzaOrderInfoModel.setPizzaType(model.getPizzaInfo().getType());
                            if (model.getDiscount() == 0) {
                                pizzaOrderInfoModel.setPrice(model.getPrice());
                                pizzaOrderInfoModel.setTotalPrice(model.getPrice());
                            } else {
                                pizzaOrderInfoModel.setTotalPrice(model.getDiscount() + "");
                                pizzaOrderInfoModel.setPrice(model.getDiscount() + "");
                            }
                            setNextPhoneFragment();
                            alertDialog.dismiss();

                        } else {
                            alertDialog.dismiss();
                        }
                    } else {
                        alertDialog.dismiss();
                    }


                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // add to list
                    if (model.getPizzaInfo() != null) {
                        if (pizzaName != null && !pizzaName.equals("")) {
                            pizzaOrderInfoModel.setComboInfo(comboDialogAdapter.selectedCombo);
                            pizzaOrderInfoModel.setPizzaName(model.getPizzaInfo().getPizzaName());
                            pizzaOrderInfoModel.setImageURL(model.getPizzaInfo().getImageURL());
                            pizzaOrderInfoModel.setId(model.getPizzaInfo().getId());
                            pizzaOrderInfoModel.setQuantity(model.getPizzaInfo().getQuantiy());
                            pizzaOrderInfoModel.setPizzaType(model.getPizzaInfo().getType());
                            if (model.getDiscount() == 0) {
                                pizzaOrderInfoModel.setPrice(model.getPrice());
                                pizzaOrderInfoModel.setTotalPrice(model.getPrice());
                            } else {
                                pizzaOrderInfoModel.setTotalPrice(model.getDiscount() + "");
                                pizzaOrderInfoModel.setPrice(model.getDiscount() + "");
                            }
                            setNextPhoneFragment();
                            alertDialog.dismiss();
                        } else {
                            List<SidelineInfoInCombo> tempCombo = new ArrayList<>();
//                            tempCombo = comboDialogAdapter.selectedCombo.getSidelineInfo();
//                            String description = "";
//                            for (SidelineInfoInCombo models : comboDialogAdapter.selectedCombo.getSidelineInfo()) {
//                                if (!models.getSidelineName().equals("")) {
//                                    if (!description.equals("")) {
//                                        description = description + ", " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                                    } else {
//                                        description = description + " " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                                    }
//                                }
//                            }
//
//                            if (comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() != 0) {
//                                if (!description.equals("")) {
//                                    description = description + ", " + comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//                                } else {
//                                    description = description + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//
//                                }
//                            }
                            if (tempCombo != null) {
                                if (tempCombo.size() > 0) {
                                    for (SidelineInfoInCombo soi : tempCombo) {
                                        SidelineOrderInfo tmp = new SidelineOrderInfo();
                                        tmp.setImageURL(soi.getImageURL());
                                        tmp.setId(soi.getId());
                                        tmp.setCategory(soi.getCategory());
                                        tmp.setSidelineName(description);
                                        tmp.setQuantity(soi.getQuantiy());
                                        tmp.setPrice(comboDialogAdapter.selectedCombo.getPrice());
                                        /*int q = tmp.getQuantity();
                                        if (soi.getDiscount() != 0){
                                            //tmp.setDiscount(soi.getDiscount());
                                            int pri = Integer.parseInt(soi.getPrice());
                                            tmp.setPrice((soi.getDiscount() / q)+"");
                                        }else {
                                            int pri = Integer.parseInt(soi.getPrice());
                                            tmp.setPrice(pri / q +"");
                                            tmp.setPrice(soi.getPrice());
                                        }*/

                                        new DBClass(context).InsertSidelines(tmp);
                                    }
                                }
                            }

                        /*    if (comboDialogAdapter.selectedCombo.getDrinksInfo()!=null && comboDialogAdapter.selectedCombo.getDrinksInfo().equals("")){
                                DrinksInfoInCombo soi = new DrinksInfoInCombo();
                                soi = comboDialogAdapter.selectedCombo.getDrinksInfo();

                                SidelineOrderInfo tmp = new SidelineOrderInfo();
                                tmp.setImageURL(soi.getImageURL());
                                tmp.setId(soi.getId());
                                tmp.setCategory(soi.getCategory());
                                tmp.setSidelineName(description);
                                tmp.setQuantity(soi.getQuantiy());
                                int q = tmp.getQuantity();
                                if (soi.getDiscount() != 0){
                                    //tmp.setDiscount(soi.getDiscount());
                                    int pri = Integer.parseInt(soi.getPrice());
                                    tmp.setPrice((tmp.getDiscount() / q)+"");
                                }else {
                                    int pri = Integer.parseInt(tmp.getPrice());
                                    tmp.setPrice(pri / q +"");
                                    tmp.setPrice(soi.getPrice());
                                }

                                new DBClass(context).InsertSidelines(tmp);


                            }*/
                            setAddToCartFragment();
                            alertDialog.dismiss();

                        }
                    } else {
                        List<SidelineInfoInCombo> tempCombo = new ArrayList<>();
//                        tempCombo = comboDialogAdapter.selectedCombo.getSidelineInfo();
//
//                        String description = "";
//                        for (SidelineInfoInCombo models : comboDialogAdapter.selectedCombo.getSidelineInfo()) {
//                            if (!models.getSidelineName().equals("")) {
//                                if (!description.equals("")) {
//                                    description = description + ", " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                                } else {
//                                    description = description + " " + models.getQuantiy() + "  pcs of " + models.getSidelineName();
//                                }
//                            }
//                        }
//
//                        if (comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() != 0) {
//                            if (!description.equals("")) {
//                                description = description + ", " + comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//                            } else {
//                                description = description + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getQuantiy() + " " + comboDialogAdapter.selectedCombo.getDrinksInfo().getDrinksSize() + " Drink(s) ";
//
//                            }
//                        }
                        if (tempCombo != null) {
                            if (tempCombo.size() > 0) {
                                for (SidelineInfoInCombo soi : tempCombo) {
                                    SidelineOrderInfo tmp = new SidelineOrderInfo();
                                    tmp.setImageURL(soi.getImageURL());
                                    tmp.setId(soi.getId());
                                    tmp.setCategory(soi.getCategory());
                                    tmp.setSidelineName(description);
                                    tmp.setQuantity(soi.getQuantiy());
                                    tmp.setPrice(comboDialogAdapter.selectedCombo.getPrice());
                                    int q = tmp.getQuantity();
                                    /*if (soi.getDiscount() != 0){
                                        tmp.setPrice((soi.getDiscount() / q)+"");
                                    }else {
                                        int pri = Integer.parseInt(soi.getPrice());
                                        tmp.setPrice(pri / q +"");
                                        tmp.setPrice(soi.getPrice());
                                    }*/

                                    new DBClass(context).InsertSidelines(tmp);
                                }
                            }
                        }
                        setAddToCartFragment();
                        alertDialog.dismiss();

                    }

                }
            });
            alertDialog.setView(dialogView);
            getCombos(pizzaName, alertDialog, model);
        } catch (Exception e) {
            Log.d("TAG", "exception in showActonDialog: " + e.getMessage());
        }

    }

    private void getCombos(final String pizzaName, final AlertDialog alertDialog, ComboInfo model) {
        combosList = new ArrayList<>();
        combos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                combosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ComboInfo chat = snapshot.getValue(ComboInfo.class);
                    if (chat.getPizzaInfo().getPizzaName().equals(pizzaName)) {
                        if (chat.getPizzaInfo() != null) {
                            combosList.add(chat);
                        }
                    }
                    /*messageAdapter = new MessageAdapter(context, coldrinksList);
                    recyclerView.setAdapter(messageAdapter);*/
                }

                if (combosList.size() != 0) {
                    comboDialogAdapter = new ComboDialogAdapter(combosList, context);
                    combo_dialog_recycler.setAdapter(comboDialogAdapter);
                    alertDialog.show();

                } else {
                    combosList.add(model);
                    comboDialogAdapter = new ComboDialogAdapter(combosList, context);
                    combo_dialog_recycler.setAdapter(comboDialogAdapter);
                    alertDialog.show();
                    //sidelines.add(model.get);
                    //pizzaOrderInfoModel.setSidelineInfoList();
                /*    pizzaOrderInfoModel.setPizzaName(model.getItemName());
                    pizzaOrderInfoModel.setImageURL(model.getImageURL());
                    pizzaOrderInfoModel.setId(model.getId());
                    pizzaOrderInfoModel.setQuantity(1);
                    pizzaOrderInfoModel.setPizzaType(model.getType());
                    if (model.getDiscount() == 0) {
                        pizzaOrderInfoModel.setPrice(model.getPrice());
                        pizzaOrderInfoModel.setTotalPrice(model.getPrice());
                    }else {
                        pizzaOrderInfoModel.setTotalPrice(model.getDiscount()+"");
                        pizzaOrderInfoModel.setPrice(model.getDiscount()+"");
                    }*/
                    setNextPhoneFragment();
                }
                Log.d("TAG", "onDataChange: combos List :" + Utils.getGsonParser().toJson(combosList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPizzas() {
        pizzasList = new ArrayList<>();
        pizzas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pizzasList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaInfo chat = snapshot.getValue(PizzaInfo.class);
                    // if (chat.getItemName().equals(pizzaName)) {
                    pizzasList.add(chat);
                    //}
                    /*messageAdapter = new MessageAdapter(context, coldrinksList);
                    recyclerView.setAdapter(messageAdapter);*/
                }

              /*  if (pizzasList.size() != 0) {
                    comboDialogAdapter = new ComboDialogAdapter(pizzasList, context);
                    combo_dialog_recycler.setAdapter(comboDialogAdapter);
                    alertDialog.show();

                }else {
                    pizzaOrderInfoModel.setPizzaName(model.getItemName());
                    pizzaOrderInfoModel.setImageURL(model.getImageURL());
                    pizzaOrderInfoModel.setId(model.getId());
                    pizzaOrderInfoModel.setQuantity(1);
                    pizzaOrderInfoModel.setPizzaType(model.getType());
                    if (model.getDiscount() == 0) {
                        pizzaOrderInfoModel.setPrice(model.getPrice());
                        pizzaOrderInfoModel.setTotalPrice(model.getPrice());
                    }else {
                        pizzaOrderInfoModel.setTotalPrice(model.getDiscount()+"");
                        pizzaOrderInfoModel.setPrice(model.getDiscount()+"");
                    }
                    setNextPhoneFragment();
                }*/
                Log.d("TAG", "onDataChange: combos List :" + Utils.getGsonParser().toJson(pizzasList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PizzaDoughFragment hf = (PizzaDoughFragment) fm.findFragmentByTag("PizzaDoughFragment");
            if (hf == null) {
                hf = new PizzaDoughFragment();
                ft.replace(R.id.main_frame, hf, "PizzaDoughFragment");
                ft.addToBackStack("PizzaDoughFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "PizzaDoughFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }


    }

    private void setAddToCartFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm.getBackStackEntryCount() >= 1) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            }
            FragmentTransaction ft = fm.beginTransaction();
            CartCheckoutFragment hf = (CartCheckoutFragment) fm.findFragmentByTag("CartCheckoutFragment");
            if (hf == null) {
                hf = new CartCheckoutFragment();
                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
                ft.addToBackStack("CartCheckoutFragment");
            } else {

                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }
        combosList.clear();

    }


}