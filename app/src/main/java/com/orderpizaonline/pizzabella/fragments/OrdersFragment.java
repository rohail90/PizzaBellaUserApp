package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaSidelinesDBModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.ui.OrderDetailsActivity;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userAddresses;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SwipeRefreshLayout swipeRefreshLayout;
    PizzaSidelinesDBModel addressInfo=new PizzaSidelinesDBModel();
    TextView tv_null;
    int t = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.orders_fragment, container, false);
        context = container.getContext();

        ((MainActivity) context).setToolbarTitle("My Orders");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        userAddresses = firebaseDatabase.getReference(Common.ORDERS);
        recyclerView =v.findViewById(R.id.pizzaRecyclerview);
        tv_null =v.findViewById(R.id.tv_null);
        /*recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);*/
        LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        showAddresses();
        if (t == 1){
            tv_null.setVisibility(View.GONE);
        }else {
            tv_null.setVisibility(View.VISIBLE);
        }

        return v;
    }
    private void deleteFood(String key) {
        userAddresses.child(key).removeValue();
    }

    @Override
    public void onClick(View v) {

    }

    private void showAddresses() {
        FirebaseRecyclerOptions<PizzaSidelinesDBModel> options = new FirebaseRecyclerOptions.Builder<PizzaSidelinesDBModel>().setQuery(
                userAddresses.orderByChild("userId").equalTo(Session.getUserInfo().getId())/*.limitToFirst(-1)*/, PizzaSidelinesDBModel.class).build();

        adapter = new FirebaseRecyclerAdapter<PizzaSidelinesDBModel, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final PizzaSidelinesDBModel model) {

                t = 1;
                tv_null.setVisibility(View.GONE);
                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.orderDate);
                itemName.setText(model.getOrderDate());
                TextView tv_status = holder.itemView.findViewById(R.id.orderStatus);
                TextView deleteTV = holder.itemView.findViewById(R.id.deleteTV);
                int status = model.getOrderStatus();
                if (status == 0) {
                    tv_status.setText("Pending");
                    deleteTV.setVisibility(View.VISIBLE);
                    tv_status.setBackground(context.getResources().getDrawable(R.drawable.red_filled_shape));
                }else if (status == 1) {
                    deleteTV.setVisibility(View.GONE);
                    tv_status.setText("Preparing");
                    tv_status.setBackground(context.getResources().getDrawable(R.drawable.darkyellow_filled_shape));
                }else  if (status == 2) {
//                    deleteTV.setVisibility(View.VISIBLE);
                    deleteTV.setVisibility(View.GONE);
                    tv_status.setText("Dispatched");
                    tv_status.setBackground(context.getResources().getDrawable(R.drawable.green_filled_shape));
                } else if (status == 3) {
                    deleteTV.setVisibility(View.GONE);
                    tv_status.setText("Cancelled");
                    tv_status.setBackground(context.getResources().getDrawable(R.drawable.grey_filled_shape));
                }else {
                    deleteTV.setVisibility(View.GONE);
                }

                TextView reOrderBtn = holder.itemView.findViewById(R.id.reOrderTV);
                deleteTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userAddresses.child(model.getId()).removeValue();
                    }
                });

                reOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new DBClass(context).DeletePizzaCart();
                        new DBClass(context).DeleteCart();
                        new DBClass(context).DeleteChineseCart();
                        new DBClass(context).DeleteMealCart();
                        new DBClass(context).DeleteDealCart();
                        new DBClass(context).DeleteFriedRollCart();
                        if (model.getSidelinesOrder() != null) {
                            if (model.getSidelinesOrder().size() > 0) {
                                for (SidelineOrderInfo soi : model.getSidelinesOrder()) {
                                    new DBClass(context).InsertSidelines(soi);
                                }
                            }
                        }
                        if (model.getPizzaOrder()!=null ) {
                            if (model.getPizzaOrder().size() > 0) {
                                for (PizzaOrderCartModel soi : model.getPizzaOrder()) {
                                    new DBClass(context).InsertPizzas(soi);
                                }
                            }
                        }
                        if (model.getChineseOrder()!=null ) {
                            if (model.getChineseOrder().size() > 0) {
                                for (ChineseCornerOrderInfo soi : model.getChineseOrder()) {
                                    new DBClass(context).InsertChinese(soi);
                                }
                            }
                        }

                        if (model.getFriedOrder()!=null ) {
                            if (model.getFriedOrder().size() > 0) {
                                for (FriedRollOrderInfo soi : model.getFriedOrder()) {
                                    new DBClass(context).InsertFriedRoll(soi);
                                }
                            }
                        }

                        if (model.getMealOrder()!=null ) {
                            if (model.getMealOrder().size() > 0) {
                                for (ComboOrderInfo soi : model.getMealOrder()) {
                                    new DBClass(context).InsertMeal(soi);
                                }
                            }
                        }
                        if (model.getDealOrder()!=null ) {
                            if (model.getDealOrder().size() > 0) {
                                for (ComboOrderInfo soi : model.getDealOrder()) {
                                    new DBClass(context).InsertDeal(soi);
                                }
                            }
                        }

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("showCart", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

                TextView pizzaTitle = holder.itemView.findViewById(R.id.pizzaName);
                TextView pizzaFlavours = holder.itemView.findViewById(R.id.pizzaFlavours);
                TextView sideline_title = holder.itemView.findViewById(R.id.sideline_title);
                TextView sidelineFlavours = holder.itemView.findViewById(R.id.sidelineFlavours);

                int price = 0;
                String pizzaName = "",pizzaFlavour = "", sidelinesDetails = "";
                if (model.getPizzaOrder() != null) {
                    if (model.getPizzaOrder().size() != 0) {
                        for (int i = 0; i < model.getPizzaOrder().size(); i++){
                            if (model.getPizzaOrder().get(i).getPrice() != null) {
                                price += (Integer.parseInt(model.getPizzaOrder().get(i).getPrice()) * model.getPizzaOrder().get(i).getQuantity());
                            }
                            List<PizzaToppingInfo> toppings = new ArrayList<>();
                            toppings.add(model.getPizzaOrder().get(i).getPizzaToppingInfo());
                            for (int j =0; j< toppings.size(); j++) {
                                if (i == 0) {
                                    pizzaFlavour = toppings.get(j).getItemName();
                                }else {
                                    pizzaFlavour = pizzaFlavour + ", "+ toppings.get(j).getItemName();
                                }
                                List<SidelineInfo> vegeis = toppings.get(j).getVegeInfoArrayList();
                                for (int k = 0; k < vegeis.size(); k++) {
                                    pizzaFlavour = pizzaFlavour + ", " + vegeis.get(k).getSidelineName();
                                }
                            }

                            if (i == 0){
                                pizzaName = model.getPizzaOrder().get(i).getPizzaName();
                            }else {
                                pizzaName = pizzaName + ", " +model.getPizzaOrder().get(i).getPizzaName();
                            }

                            /*List<PizzaToppingInfo> sides = model.getPizzaOrder().get(i).getToppingInfoList();
                            for (int m = 0; m < sides.size() ; m++) {
                                List<SidelineInfo> vegs =  sides.get(m).getVegeInfoArrayList();
                                for (int n = 0; n < vegs.size(); n++) {
                                    if (m == 0){
                                        sidelinesDetails  = vegs.get(m).getSidelineName();
                                    }else {
                                        sidelinesDetails  = sidelinesDetails + ", " +vegs.get(m).getSidelineName();
                                    }

                                }

                            }*/

                        }

                        pizzaTitle.setText(pizzaName);
                        pizzaFlavours.setText(pizzaFlavour);
                    }else {
                        pizzaTitle.setVisibility(View.GONE);
                        pizzaFlavours.setVisibility(View.GONE);
                    }
                }else {
                    pizzaTitle.setVisibility(View.GONE);
                    pizzaFlavours.setVisibility(View.GONE);
                }


                if (model.getSidelinesOrder() != null) {
                    if (model.getSidelinesOrder().size() != 0) {
                        for (int i = 0; i < model.getSidelinesOrder().size(); i++) {
                            price += (Integer.parseInt(model.getSidelinesOrder().get(i).getPrice()) * model.getSidelinesOrder().get(i).getQuantity());
                            if (i == 0) {
                                sidelinesDetails += model.getSidelinesOrder().get(i).getSidelineName();
                            } else {
                                sidelinesDetails = sidelinesDetails + ", " + model.getSidelinesOrder().get(i).getSidelineName();
                            }
                        }
                    } else {
                        sideline_title.setVisibility(View.GONE);
                        sidelineFlavours.setVisibility(View.GONE);
                    }
                }else {
                    sideline_title.setVisibility(View.GONE);
                    sidelineFlavours.setVisibility(View.GONE);
                }

                if (model.getChineseOrder() != null) {
                    if (model.getChineseOrder().size() != 0) {
                        sideline_title.setVisibility(View.VISIBLE);
                        sidelineFlavours.setVisibility(View.VISIBLE);
                        for (int i = 0; i < model.getChineseOrder().size(); i++) {
                            price += model.getChineseOrder().get(i).getSinglePrice() * model.getChineseOrder().get(i).getQuantity();
                            if (i == 0) {
                                sidelinesDetails += model.getChineseOrder().get(i).getName();
                            } else {
                                sidelinesDetails = sidelinesDetails + ", " + model.getChineseOrder().get(i).getName();
                            }
                        }
                    } else {
//                        sideline_title.setVisibility(View.GONE);
//                        sidelineFlavours.setVisibility(View.GONE);
                    }
                }else {
//                    sideline_title.setVisibility(View.GONE);
//                    sidelineFlavours.setVisibility(View.GONE);
                }

                if (model.getFriedOrder() != null) {
                    if (model.getFriedOrder().size() != 0) {
                        sideline_title.setVisibility(View.VISIBLE);
                        sidelineFlavours.setVisibility(View.VISIBLE);
                        for (int i = 0; i < model.getFriedOrder().size(); i++) {
                            price += model.getFriedOrder().get(i).getSinglePrice() * model.getFriedOrder().get(i).getQuantity();
                            if (i == 0) {
                                sidelinesDetails += model.getFriedOrder().get(i).getItemName();
                            } else {
                                sidelinesDetails = sidelinesDetails + ", " + model.getFriedOrder().get(i).getItemName();
                            }
                        }
                    } else {
//                        sideline_title.setVisibility(View.GONE);
//                        sidelineFlavours.setVisibility(View.GONE);
                    }
                }else {
//                    sideline_title.setVisibility(View.GONE);
//                    sidelineFlavours.setVisibility(View.GONE);
                }

                if (model.getMealOrder() != null) {
                    if (model.getMealOrder().size() != 0) {
                        sideline_title.setVisibility(View.VISIBLE);
                        sidelineFlavours.setVisibility(View.VISIBLE);
                        for (int i = 0; i < model.getMealOrder().size(); i++) {
                            price += Integer.parseInt(model.getMealOrder().get(i).getPrice()) * model.getMealOrder().get(i).getQuantity();
                            if (i == 0) {
                                sidelinesDetails += model.getMealOrder().get(i).getItemName();
                            } else {
                                sidelinesDetails = sidelinesDetails + ", " + model.getMealOrder().get(i).getItemName();
                            }
                        }
                    } else {
//                        sideline_title.setVisibility(View.GONE);
//                        sidelineFlavours.setVisibility(View.GONE);
                    }
                }else {
//                    sideline_title.setVisibility(View.GONE);
//                    sidelineFlavours.setVisibility(View.GONE);
                }

                if (model.getDealOrder() != null) {
                    if (model.getDealOrder().size() != 0) {
                        sideline_title.setVisibility(View.VISIBLE);
                        sidelineFlavours.setVisibility(View.VISIBLE);
                        for (int i = 0; i < model.getDealOrder().size(); i++) {
                            price += Integer.parseInt(model.getDealOrder().get(i).getPrice()) * model.getDealOrder().get(i).getQuantity();
                            if (i == 0) {
                                sidelinesDetails += model.getDealOrder().get(i).getItemName();
                            } else {
                                sidelinesDetails = sidelinesDetails + ", " + model.getDealOrder().get(i).getItemName();
                            }
                        }
                    } else {
//                        sideline_title.setVisibility(View.GONE);
//                        sidelineFlavours.setVisibility(View.GONE);
                    }
                }else {
//                    sideline_title.setVisibility(View.GONE);
//                    sidelineFlavours.setVisibility(View.GONE);
                }

                if (!sidelinesDetails.equals("")){
                    sidelineFlavours.setText(sidelinesDetails);
                }

                TextView totalPrice = holder.itemView.findViewById(R.id.totalPrice);
                totalPrice.setText("Rs."+price+"");





                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                        //   showActonDialog(adapter,model.getId(),model);
                        //Sending food_id to FoodDetailActivity
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("orderId", model.getId());
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    private void setNextFragment(String s,  String houseNo, String street, String area) {

    }

}