package com.orderpizaonline.pizzabella.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.CartCheckoutAdapter;
import com.orderpizaonline.pizzabella.adapter.CartChineseAdapter;
import com.orderpizaonline.pizzabella.adapter.CartDealAdapter;
import com.orderpizaonline.pizzabella.adapter.CartFriedRollAdapter;
import com.orderpizaonline.pizzabella.adapter.CartMealAdapter;
import com.orderpizaonline.pizzabella.adapter.CartPizzaCheckoutAdapter;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.ui.LoginActivity;
import com.orderpizaonline.pizzabella.utils.Session;

import java.util.List;
import java.util.Objects;

import static com.orderpizaonline.pizzabella.MainActivity.orderTotalPrice;

public class CartCheckoutFragment extends Fragment implements View.OnClickListener {
    TextView tv_order_now;
    static TextView tv_total_order_price;
    Context context;
    public static Context staticContext;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines;
    RecyclerView recyclerView, pizzasRecyclerview, chineseRecyclerView, friedRecyclerView, mealsRecyclerView,
            dealsRecyclerView, summerRecyclerView, winterRecyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    static CartCheckoutAdapter mAdapter;
    static CartChineseAdapter chineseAdapter;
    static CartFriedRollAdapter friedRollAdapter;
    static CartMealAdapter mealAdapter;
    static CartDealAdapter dealAdapter;
    static CartPizzaCheckoutAdapter pizzaAdapter;
    public static List<SidelineOrderInfo> orders;
    public static List<ChineseCornerOrderInfo> ordersChinese;
    public static List<FriedRollOrderInfo> ordersFried;
    public static List<PizzaOrderCartModel> orderPizzas;
    public static List<ComboOrderInfo> ordersMeal;
    public static List<ComboOrderInfo> ordersDeal;
    Button btn_checkout, btn_order;
    TextView tv_pizza,tv_sidelines, tv_empty;
    LinearLayout ll_price;
    public static int priceTot = 0;

    Dialog rateus_dialog;
    TextView rateus_noText, dialog_ok_txt;
    ImageView iv_empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.car_checkoutt_fragment, container, false);
        context = Objects.requireNonNull(container).getContext();
        staticContext = Objects.requireNonNull(container).getContext();
        ((MainActivity) context).setToolbarTitle("Cart");
        recyclerView = v.findViewById(R.id.sidelinesRecyclerview);
        pizzasRecyclerview = v.findViewById(R.id.pizzasRecyclerview);
        chineseRecyclerView = v.findViewById(R.id.chineseRecyclerview);
        mealsRecyclerView = v.findViewById(R.id.mealsRecyclerview);
        dealsRecyclerView = v.findViewById(R.id.dealsRecyclerview);
        summerRecyclerView = v.findViewById(R.id.summerDealsRecyclerview);
        winterRecyclerView = v.findViewById(R.id.winterDealsRecyclerview);
        dealsRecyclerView = v.findViewById(R.id.dealsRecyclerview);
        friedRecyclerView = v.findViewById(R.id.friedRollRecyclerview);
        tv_total_order_price = v.findViewById(R.id.order_price);
        tv_pizza = v.findViewById(R.id.tv_pizza);
        tv_sidelines = v.findViewById(R.id.tv_sidelines);
        ll_price = v.findViewById(R.id.ll_price);
        btn_order = v.findViewById(R.id.btn_order);
        iv_empty = v.findViewById(R.id.iv_empty);
        tv_empty = v.findViewById(R.id.textView5);

        btn_checkout = v.findViewById(R.id.btn_checkout);
        btn_checkout.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        /*firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        sidelines = firebaseDatabase.getReference(Common.SIDE_LINES);*/

        loadCartPizzas();
        loadCartSidelines();
        loadCartChinese();
        loadCartFried();
        loadCartMeal();
        loadCartDeal();
        if (ordersDeal.size() == 0 && ordersMeal.size() == 0 && ordersChinese.size() == 0 && ordersFried.size() == 0 && orders.size() == 0 && orderPizzas.size() == 0){
            ll_price.setVisibility(View.GONE);
            tv_pizza.setVisibility(View.GONE);
            tv_sidelines.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            pizzasRecyclerview.setVisibility(View.GONE);
            chineseRecyclerView.setVisibility(View.GONE);
            dealsRecyclerView.setVisibility(View.GONE);
            mealsRecyclerView.setVisibility(View.GONE);
            summerRecyclerView.setVisibility(View.GONE);
            winterRecyclerView.setVisibility(View.GONE);
            friedRecyclerView.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.GONE);
            btn_order.setVisibility(View.VISIBLE);
            iv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

        }


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

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_checkout){
            UserInfo userInfo = Session.getUserInfo();
            Session s = new Session(context);

            if (s.getLoginState()) {
                if (priceTot >= 0) {
                    orderTotalPrice = priceTot;
                    setNextPhoneFragment();
                }else {
                    rateus_dialog.show();
                }
            }else {

                Dialog rateus_dialog = new Dialog(context, R.style.CustomDialog);
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

                TextView rateus_noText = rateus_dialog.findViewById(R.id.dismiss_txt);
                TextView dialog_ok_txt = rateus_dialog.findViewById(R.id.ok_txt);
                TextView title_dialog = rateus_dialog.findViewById(R.id.title_dialog);
                TextView title_rating = rateus_dialog.findViewById(R.id.title_rating);
                title_rating.setText("Please Login/Signup to continue");
                title_dialog.setText("Warning");
                rateus_noText.setOnClickListener(vi -> {
                    rateus_dialog.dismiss();
                });
                dialog_ok_txt.setOnClickListener(vi -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    rateus_dialog.dismiss();
                    ((MainActivity)context).finish();
                });
                rateus_dialog.show();
            }
        }else if (v == btn_order){
            setFirstFragment();
        }

    }

    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PlaceOrderFragment hf = (PlaceOrderFragment) fm.findFragmentByTag("PlaceOrderFragment");
            if (hf == null) {
                hf = new PlaceOrderFragment();
                ft.replace(R.id.main_frame, hf, "PlaceOrderFragment");
                ft.addToBackStack("PlaceOrderFragment");
            }else {
                ft.replace(R.id.main_frame, hf, "PlaceOrderFragment");
            }

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }


    }
    private void setFirstFragment() {

        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
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
        orderPizzas = new DBClass(context).getPizzas();

        pizzaAdapter = new CartPizzaCheckoutAdapter(context, orderPizzas);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        pizzasRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        pizzasRecyclerview.setAdapter(pizzaAdapter);
        setTotalCartPrice();
    }

    private void loadCartSidelines() {
        orders = new DBClass(context).getSideslines();

        mAdapter = new CartCheckoutAdapter(context, orders);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        setTotalCartPrice();
    }

    private void loadCartChinese() {
        ordersChinese = new DBClass(context).getChinese();

        chineseAdapter = new CartChineseAdapter(context, ordersChinese);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        chineseRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        chineseRecyclerView.setAdapter(chineseAdapter);
        setTotalCartPrice();
    }

    private void loadCartFried() {
        ordersFried = new DBClass(context).getFriedRoll();

        friedRollAdapter = new CartFriedRollAdapter(context, ordersFried);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        friedRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        friedRecyclerView.setAdapter(friedRollAdapter);
        setTotalCartPrice();
    }

    private void loadCartMeal() {
        ordersMeal = new DBClass(context).getMeal();
        mealAdapter = new CartMealAdapter(context, ordersMeal);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        mealsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mealsRecyclerView.setAdapter(mealAdapter);
        setTotalCartPrice();
    }

    private void loadCartDeal() {
        ordersDeal = new DBClass(context).getDeal();
        dealAdapter = new CartDealAdapter(context, ordersDeal);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        dealsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        dealsRecyclerView.setAdapter(dealAdapter);
        setTotalCartPrice();
    }

    public static void setTotalCartPrice() {
        int total = 0;
        int cartSize = 0;
        if (orders!=null) {
            if (orders.size() > 0) {
                cartSize += orders.size();
                for (SidelineOrderInfo order : orders) {
                    if (order.getPrice() !=null && !order.getPrice().equals("")) {
                        total += (Integer.parseInt(order.getPrice()) * order.getQuantity()
                                - order.getDiscount() * order.getQuantity());
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        if (orderPizzas!=null) {
            if (orderPizzas.size() > 0) {
                cartSize += orderPizzas.size();
                for (PizzaOrderCartModel order : orderPizzas) {
                    if (order.getPrice() !=null && !order.getPrice().equals("")) {
                        total += (Integer.parseInt(order.getPrice()) * order.getQuantity());
                    }
                }
            }
            pizzaAdapter.notifyDataSetChanged();
        }

        if (ordersChinese!=null) {
            if (ordersChinese.size() > 0) {
                cartSize += ordersChinese.size();
                for (ChineseCornerOrderInfo order : ordersChinese) {
                    if (order.getSinglePrice() !=0) {
                        total += order.getSinglePrice() * order.getQuantity();
                    }
                }
            }
            chineseAdapter.notifyDataSetChanged();
        }

        if (ordersFried!=null) {
            if (ordersFried.size() > 0) {
                cartSize += ordersFried.size();
                for (FriedRollOrderInfo order : ordersFried) {
                    if (order.getSinglePrice() !=0) {
                        total += order.getSinglePrice() * order.getQuantity();
                    }
                }
            }
            friedRollAdapter.notifyDataSetChanged();
        }

        if (ordersMeal!=null) {
            if (ordersMeal.size() > 0) {
                cartSize += ordersMeal.size();
                for (ComboOrderInfo order : ordersMeal) {
                    if (order.getPrice() !=null && !order.getPrice().equals("")) {
                        total += Integer.parseInt(order.getPrice()) * order.getQuantity();
                    }
                }
            }
            mealAdapter.notifyDataSetChanged();
        }

        if (ordersDeal!=null) {
            if (ordersDeal.size() > 0) {
                cartSize += ordersDeal.size();
                for (ComboOrderInfo order : ordersDeal) {
                    if (order.getPrice() !=null && !order.getPrice().equals("")) {
                        total += Integer.parseInt(order.getPrice()) * order.getQuantity();

                    }
                }
            }
            dealAdapter.notifyDataSetChanged();
        }
        ((MainActivity)staticContext).cartSize(cartSize);
        //textViewPrice.setText(String.format(" $%s", total));
        priceTot = total;
        tv_total_order_price.setText("Rs."+total);

    }


}