package com.orderpizaonline.pizzabella.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.ChineseOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.adapter.DealOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.adapter.FriedOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.adapter.MealOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.adapter.PizzaOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.adapter.SidelinesOrderDetailsAdapter;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaSidelinesDBModel;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_order_now;
    static TextView tv_total_order_price;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines, ordersReference;
    RecyclerView recyclerView, pizzasRecyclerview;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SidelinesOrderDetailsAdapter mAdapter;
    PizzaOrderDetailsAdapter pizzaAdapter;
    ChineseOrderDetailsAdapter chineseAdapter;
    FriedOrderDetailsAdapter friedAdapter;
    MealOrderDetailsAdapter mealAdapter;
    DealOrderDetailsAdapter dealAdapter;
    public List<SidelineOrderInfo> orders = new ArrayList<>();
    public List<PizzaOrderCartModel> orderPizzas = new ArrayList<>();
    public List<ChineseCornerOrderInfo> orderChinese = new ArrayList<>();
    public List<FriedRollOrderInfo> orderFried = new ArrayList<>();
    public List<ComboOrderInfo> orderMeal = new ArrayList<>();
    public List<ComboOrderInfo> orderDeal = new ArrayList<>();
    PizzaSidelinesDBModel order;
    Button btn_checkout, btn_order;
    TextView tv_pizza,tv_sidelines, tv_empty;
    LinearLayout ll_price;
    public static int priceTot = 0;

    Dialog rateus_dialog;
    TextView rateus_noText, dialog_ok_txt;
    String orderId = "";
    ImageView iv_backward;
    TextView status, contact, name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        context = this;
        iv_backward = findViewById(R.id.iv_backward);
        status = findViewById(R.id.status);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        iv_backward.setOnClickListener(this);

        recyclerView = findViewById(R.id.sidelinesRecyclerview);
        pizzasRecyclerview = findViewById(R.id.pizzasRecyclerview);
        tv_total_order_price = findViewById(R.id.order_price);
        tv_pizza = findViewById(R.id.tv_pizza);
        tv_sidelines = findViewById(R.id.tv_sidelines);
        ll_price = findViewById(R.id.ll_price);
        btn_order = findViewById(R.id.btn_order);
        tv_empty = findViewById(R.id.textView5);

        btn_checkout = findViewById(R.id.btn_reorder);
        btn_checkout.setOnClickListener(this);
        btn_order.setOnClickListener(this);


        orderId = getIntent().getStringExtra("orderId");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        ordersReference = firebaseDatabase.getReference(Common.ORDERS);

        getOrder();

       /* if (orders.size() == 0 && orderPizzas.size() == 0){
            ll_price.setVisibility(View.GONE);
            tv_pizza.setVisibility(View.GONE);
            tv_sidelines.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            pizzasRecyclerview.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.GONE);
            btn_order.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

        }*/


        rateus_dialog = new Dialog(context, R.style.CustomDialog);
        rateus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateus_dialog.setCancelable(false);
        rateus_dialog.setContentView(R.layout.cart_dialog);

        rateus_dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                return true;
            }
            return false;
        });

        rateus_noText = rateus_dialog.findViewById(R.id.dismiss_txt);
        dialog_ok_txt = rateus_dialog.findViewById(R.id.ok_txt);

        rateus_noText.setOnClickListener(vi -> {
            rateus_dialog.dismiss();
        });
        dialog_ok_txt.setOnClickListener(vi -> {
            rateus_dialog.dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_checkout){
            new DBClass(context).DeletePizzaCart();
            new DBClass(context).DeleteCart();
            new DBClass(context).DeleteChineseCart();
            new DBClass(context).DeleteFriedRollCart();
            new DBClass(context).DeleteMealCart();
            if (orders.size() > 0) {
                for (SidelineOrderInfo soi : orders) {
                    new DBClass(context).InsertSidelines(soi);
                }
            }
            if (orderPizzas.size() > 0) {
                for (PizzaOrderCartModel soi : orderPizzas) {
                    new DBClass(context).InsertPizzas(soi);
                }
            }

            if (orderChinese.size() > 0) {
                for (ChineseCornerOrderInfo soi : orderChinese) {
                    new DBClass(context).InsertChinese(soi);
                }
            }

            if (orderFried.size() > 0) {
                for (FriedRollOrderInfo soi : orderFried) {
                    new DBClass(context).InsertFriedRoll(soi);
                }
            }

            if (orderMeal.size() > 0) {
                for (ComboOrderInfo soi : orderMeal) {
                    new DBClass(context).InsertMeal(soi);
                }
            }
            if (orderDeal.size() > 0) {
                for (ComboOrderInfo soi : orderDeal) {
                    new DBClass(context).InsertDeal(soi);
                }
            }


            
            this.finish();
            Intent intent = new Intent(OrderDetailsActivity.this, MainActivity.class);
            intent.putExtra("showCart", true);
            finishAffinity();
            startActivity(intent);
        }else if (v == iv_backward){
            this.finish();
        }

    }

    private void getOrder() {
        order = new PizzaSidelinesDBModel();
        ordersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaSidelinesDBModel chat = snapshot.getValue(PizzaSidelinesDBModel.class);
                    if (chat.getId().equals(orderId)){
                        order = chat;
                    }
                    /*messageAdapter = new MessageAdapter(context, coldrinksList);
                    recyclerView.setAdapter(messageAdapter);*/
                }
                if (order!=null){
                    name.setText(order.getName());
                    contact.setText(order.getUserPhone());
                    address.setText(order.getUserAddress());
                    if (order.getOrderStatus() == 0) {
                        status.setText(R.string.pending);
                    }else if (order.getOrderStatus() == 1) {
                        status.setText("Preparing");
                    }else  if (order.getOrderStatus() == 2) {
                        status.setText("Dispatched");
                    } else if (order.getOrderStatus() == 3) {
                        status.setText("Cancelled");
                    }

                    if (order.getSidelinesOrder() != null) {
                        orders.addAll(order.getSidelinesOrder());
                        loadCartSidelines();
                    }
                    if (order.getPizzaOrder()!= null) {
                        orderPizzas.addAll(order.getPizzaOrder());
                        loadCartPizzas();
                    }

                    if (order.getChineseOrder()!= null) {
                        orderChinese.addAll(order.getChineseOrder());
                        loadCartChinese();
                    }

                    if (order.getFriedOrder()!= null) {
                        orderFried.addAll(order.getFriedOrder());
                        loadCartFried();
                    }

                    if (order.getMealOrder()!= null) {
                        orderMeal.addAll(order.getMealOrder());
                        loadCartMeal();
                    }
                    if (order.getDealOrder()!= null) {
                        orderDeal.addAll(order.getDealOrder());
                        loadCartDeal();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showOurSidelines() {
        /*FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                sidelines.orderByChild(Common.SIDELINE_CATEGORY)*//*.equalTo(SidelineCategoryENum.Sidelines.toString())*//*, SidelineInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final SidelineInfo model) {

                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getSidelineName());

                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                itemPrice.setText(model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
                Picasso.get().load(model.getImageURL()).into(itemImage);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };*/
        //adapter.startListening();


    }


    private void setFirstFragment() {

        try {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() >= 1) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            }/*
            FragmentTransaction ft = fm.beginTransaction();
            ShowPizzasFragment hf = (ShowPizzasFragment) fm.findFragmentByTag("ShowPizzasFragment");
            if (hf == null) {
                hf = new ShowPizzasFragment();
                ft.replace(R.id.main_frame, hf, "ShowPizzasFragment");
                ft.addToBackStack("ShowPizzasFragment");
            }else{

                ft.replace(R.id.main_frame, hf, "ShowPizzasFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();*/
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }
    }

    private void loadCartPizzas() {
        //orderPizzas = new DBClass(context).getPizzas();

        pizzaAdapter = new PizzaOrderDetailsAdapter(context, orderPizzas);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        pizzasRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        pizzasRecyclerview.setAdapter(pizzaAdapter);
        setTotalCartPrice();
    }

    private void loadCartSidelines() {
        //orders = new DBClass(context).getSideslines();

        mAdapter = new SidelinesOrderDetailsAdapter(context, orders);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        setTotalCartPrice();
    }

    private void loadCartChinese() {
        //orders = new DBClass(context).getSideslines();

        chineseAdapter = new ChineseOrderDetailsAdapter(context, orderChinese);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(chineseAdapter);
        setTotalCartPrice();
    }

    private void loadCartFried() {
        //orders = new DBClass(context).getSideslines();

        friedAdapter = new FriedOrderDetailsAdapter(context, orderFried);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(friedAdapter);
        setTotalCartPrice();
    }
    private void loadCartMeal() {
        //orders = new DBClass(context).getSideslines();

        mealAdapter = new MealOrderDetailsAdapter(context, orderMeal);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mealAdapter);
        setTotalCartPrice();
    }
    private void loadCartDeal() {
        //orders = new DBClass(context).getSideslines();

        dealAdapter = new DealOrderDetailsAdapter(context, orderDeal);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(dealAdapter);
        setTotalCartPrice();
    }

    public void setTotalCartPrice() {
        int total = 0;
        if (orders!=null) {
            if (orders.size() > 0) {
                for (SidelineOrderInfo order : orders) {
                    total += (Integer.parseInt(order.getPrice()) * order.getQuantity()
                            - order.getDiscount() * order.getQuantity());
                }
            }
            //mAdapter.notifyDataSetChanged();
        }
        if (orderPizzas!=null) {
            if (orderPizzas.size() > 0) {
                for (PizzaOrderCartModel order : orderPizzas) {
                    total += (Integer.parseInt(order.getPrice()) * order.getQuantity());
                }
            }
            //pizzaAdapter.notifyDataSetChanged();
        }

        if (orderChinese!=null) {
            if (orderChinese.size() > 0) {
                for (ChineseCornerOrderInfo order : orderChinese) {
                    total += order.getSinglePrice() * order.getQuantity();
                }
            }
            //pizzaAdapter.notifyDataSetChanged();
        }

        if (orderFried!=null) {
            if (orderFried.size() > 0) {
                for (FriedRollOrderInfo order : orderFried) {
                    total += order.getSinglePrice() * order.getQuantity();
                }
            }
            //pizzaAdapter.notifyDataSetChanged();
        }

        if (orderMeal!=null) {
            if (orderMeal.size() > 0) {
                for (ComboOrderInfo order : orderMeal) {
                    total += Integer.parseInt(order.getPrice()) * order.getQuantity();
                }
            }
            //pizzaAdapter.notifyDataSetChanged();
        }

        if (orderDeal!=null) {
            if (orderDeal.size() > 0) {
                for (ComboOrderInfo order : orderDeal) {
                    total += Integer.parseInt(order.getPrice()) * order.getQuantity();
                }
            }
            //pizzaAdapter.notifyDataSetChanged();
        }
        //textViewPrice.setText(String.format(" $%s", total));
        priceTot = total;
        tv_total_order_price.setText("Rs."+total);

    }


}