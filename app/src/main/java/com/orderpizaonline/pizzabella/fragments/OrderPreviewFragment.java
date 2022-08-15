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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.SelectedChineseAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectedFriedRollAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectedMealAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectedSidelinesAdapter;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.BurgerOrSandwichInfo;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfoBella;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.chineseCornerInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.comboOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.friedRollOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;

public class OrderPreviewFragment extends Fragment implements View.OnClickListener {
    TextView order_price;
    Context context;
    Button btn_add_to_cart, btn_change_selection;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines;
    RecyclerView recyclerView, pizzassRecyclerview;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SelectedSidelinesAdapter mAdapter;
    SelectedMealAdapter mealdapter;
    SelectedChineseAdapter mCAdapter;
    SelectedFriedRollAdapter fRAdapter;
    private TextView tv_piiza_name, tv_crust_name, tv_spice_name, tv_toppings_name,
            tv_vegetables_name, tv_total_piiza_price, tv_piiza_price, tv_crust_price, tv_sliceTwo_toppings_vegeis, tv_sliceOne_toppings_vegeis
            ,tv_sliceTwo_toppings_name, tv_sliceOne_toppings_name, tv_sliceTwo_toppings_price, tv_sliceOne_toppings_price, tv_sliceTwo_name
            , tv_sliceOne_name, tv_pizza_top_name;
    private TextView tv_sliceThree_toppings_vegeis, tv_slicefour_toppings_vegeis
            ,tv_sliceThree_toppings_name, tv_slicefour_toppings_name, tv_sliceThree_toppings_price, tv_slicefour_toppings_price,
            tv_sliceThree_name, tv_slicefour_name;

    ImageView  square_destination,square_destination_right_full, square_destination_left_full, square_destination_left_top_full,
            square_destination_right_top_full , square_destination_left_bottom_full, square_destination_right_bottom_full;
    ImageView  semi_circle_destination, semi_destination_right_full, semi_destination_left_full;
    ImageView slice_destination, slice_destination_full;
    private ImageView iv_delete, iv_minus, iv_plus  ,destination_right_full, destination_left_full, destination;
    private LinearLayout  ll_extrass, slice_four, slice_one, slice_two, slice_three, ll_d
            , ll_a, ll_b, ll_c, ll_toppings, ll_vagetables, ll_top, ll_veg, ll_ext;
    List<PizzaToppingInfo> toppingsList = new ArrayList<>();
    private RelativeLayout rl_full_circle, rl_full_square, rl_semi_circle, rl_slice;
    ListView lv_extras;
    List<SidelineOrderInfo> tmpSidelines;
    LinearLayout ll_piiza_info_item;
    TextView tv_extras_price, tv_extras_name;
    List<FriedRollOrderInfo> lst;
    List<ComboOrderInfo> comboLst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.selected_sidelines_fragment, container, false);
        context = container.getContext();

        tv_extras_price =v.findViewById(R.id.tv_extras_price);
        tv_extras_name =v.findViewById(R.id.tv_extras_name);

        rl_slice =v.findViewById(R.id.rl_slice);
        slice_destination =v.findViewById(R.id.slice_destination);
        slice_destination_full =v.findViewById(R.id.slice_destination_full);

        semi_circle_destination =v.findViewById(R.id.semi_circle_destination);
        semi_destination_right_full=v.findViewById(R.id.semi_destination_right_full);
        semi_destination_left_full=v.findViewById(R.id.semi_destination_left_full);
        rl_semi_circle=v.findViewById(R.id.rl_semi_circle);

        // for Square Selection
        square_destination=v.findViewById(R.id.square_destination);
        square_destination_right_full =v.findViewById(R.id.square_destination_right_full);
        square_destination_left_full =v.findViewById(R.id.square_destination_left_full);
        square_destination_left_top_full=v.findViewById(R.id.square_destination_left_top_full);
        square_destination_right_top_full=v.findViewById(R.id.square_destination_right_top_full);
        square_destination_left_bottom_full=v.findViewById(R.id.square_destination_left_bottom_full);
        square_destination_right_bottom_full=v.findViewById(R.id.square_destination_right_bottom_full);
        rl_full_square =v.findViewById(R.id.rl_full_square);
        rl_full_circle =v.findViewById(R.id.rl_full_circle);


        destination= v.findViewById(R.id.destination);
        destination_right_full= v.findViewById(R.id.destination_right_full);
        destination_left_full= v.findViewById(R.id.destination_left_full);

        tv_slicefour_toppings_price= v.findViewById(R.id.tv_sliceFour_toppings_price);
        tv_sliceThree_toppings_price= v.findViewById(R.id.tv_sliceThree_topping_price);
        tv_slicefour_toppings_name= v.findViewById(R.id.tv_sliceFour_toppings);
        tv_sliceThree_toppings_name= v.findViewById(R.id.tv_sliceThree_toppings_name);
        tv_slicefour_toppings_vegeis= v.findViewById(R.id.tv_sliceFour_veggies);
        tv_sliceThree_toppings_vegeis= v.findViewById(R.id.tv_sliceThree_toppings_vegeis);
        tv_sliceThree_name= v.findViewById(R.id.tv_sliceThree_name);
        tv_slicefour_name= v.findViewById(R.id.tv_sliceFour_name);

        tv_sliceOne_toppings_price= v.findViewById(R.id.tv_sliceOne_topping_price);
        tv_sliceTwo_toppings_price= v.findViewById(R.id.tv_sliceTwo_toppings_price);
        tv_sliceOne_toppings_name= v.findViewById(R.id.tv_sliceOne_toppings_name);
        tv_sliceTwo_toppings_name= v.findViewById(R.id.tv_sliceTwo_toppings_name);
        tv_sliceOne_toppings_vegeis= v.findViewById(R.id.tv_sliceOne_toppings_vegeis);
        tv_sliceTwo_toppings_vegeis= v.findViewById(R.id.tv_sliceTwo_toppings_vegeis);
        tv_sliceTwo_name= v.findViewById(R.id.tv_sliceTwo_name);
        tv_sliceOne_name= v.findViewById(R.id.tv_sliceOne_name);

        tv_pizza_top_name= v.findViewById(R.id.tv_pizza_name);
        ll_toppings= v.findViewById(R.id.ll_toppings);
        ll_vagetables= v.findViewById(R.id.ll_vagetables);
        ll_top= v.findViewById(R.id.ll_top);
        ll_veg= v.findViewById(R.id.ll_veg);
        ll_ext= v.findViewById(R.id.ll_ext);
        ll_a= v.findViewById(R.id.ll_a);
        ll_b= v.findViewById(R.id.ll_b);
        ll_c= v.findViewById(R.id.ll_c);
        ll_d= v.findViewById(R.id.ll_d);
//        lv_extras = v.findViewById(R.id.lv_extras);
        slice_four= v.findViewById(R.id.slice_four);
        slice_one= v.findViewById(R.id.slice_one);
        slice_three= v.findViewById(R.id.slice_three);
        slice_two= v.findViewById(R.id.slice_two);
        ll_extrass= v.findViewById(R.id.ll_extrass);
        tv_crust_price= v.findViewById(R.id.tv_crust_price);
        tv_piiza_price= v.findViewById(R.id.tv_piiza_price);
        tv_piiza_name= v.findViewById(R.id.tv_piiza_name);
        tv_crust_name= v.findViewById(R.id.tv_crust_name);
        tv_spice_name = v.findViewById(R.id.tv_spice_name);
        tv_toppings_name= v.findViewById(R.id.tv_topping_name);
        tv_vegetables_name = v.findViewById(R.id.tv_vegetables_name);
        tv_total_piiza_price = v.findViewById(R.id.tv_pizza_price);
        iv_delete = v.findViewById(R.id.iv_delete);
        iv_minus = v.findViewById(R.id.iv_minus);
        iv_plus = v.findViewById(R.id.iv_plus);
        ll_piiza_info_item = v.findViewById(R.id.ll_piiza_info_item);

        recyclerView = v.findViewById(R.id.sidelinesRecyclerview);
        // pizzassRecyclerview = v.findViewById(R.id.pizzassRecyclerview);
        order_price = v.findViewById(R.id.order_price);
        btn_add_to_cart = v.findViewById(R.id.btn_add_to_cart);
        btn_add_to_cart.setOnClickListener(this);
        btn_change_selection = v.findViewById(R.id.btn_change_selection);
        btn_change_selection.setOnClickListener(this);


        if (pizzaOrderInfoBella != null) {
            if (pizzaOrderInfoBella.getItemName() != null && !pizzaOrderInfoBella.getItemName().equals("")) {
                order_price.setText("Rs." + pizzaOrderInfoBella.getTotalPrice());
                showPizzas();
            }else {
                ll_piiza_info_item.setVisibility(View.GONE);
                if (chineseCornerInfoBella!=null){
                    if (chineseCornerInfoBella.getName() != null && !chineseCornerInfoBella.getName().equals("")){
                        ll_piiza_info_item.setVisibility(View.GONE);
                        order_price.setText("Rs." + chineseCornerInfoBella.getTotalPrice());
                        List<ChineseCornerOrderInfo> lst = new ArrayList<>();
                        lst.add(chineseCornerInfoBella);
                        showChineseAdapter(lst);
                    }
                }/*else {

                    sidelinesOrderList.clear();
                    order_price.setText("Rs." + sidelinesOrderInfoBella.getTotalPrice());
                    sidelinesOrderList.add(sidelinesOrderInfoBella);
                    showSidelineAdapter(sidelinesOrderList);
                    ll_piiza_info_item.setVisibility(View.GONE);

                }*/

            }
        }else {
            ll_piiza_info_item.setVisibility(View.GONE);
            if (chineseCornerInfoBella!=null){
                if (chineseCornerInfoBella.getName() != null && !chineseCornerInfoBella.getName().equals("")){
                    ll_piiza_info_item.setVisibility(View.GONE);
                    order_price.setText("Rs." + chineseCornerInfoBella.getTotalPrice());
                    List<ChineseCornerOrderInfo> lst = new ArrayList<>();
                    lst.add(chineseCornerInfoBella);
                    showChineseAdapter(lst);
                }
            }/*else {

                sidelinesOrderList.clear();
                order_price.setText("Rs." + sidelinesOrderInfoBella.getTotalPrice());
                sidelinesOrderList.add(sidelinesOrderInfoBella);
                showSidelineAdapter(sidelinesOrderList);
                ll_piiza_info_item.setVisibility(View.GONE);

            }*/
        }


        if (sidelinesOrderInfoBella != null) {
            if (sidelinesOrderInfoBella.getSidelineName() != null && !sidelinesOrderInfoBella.getSidelineName().equals("")) {
                sidelinesOrderList.clear();
                order_price.setText("Rs." + sidelinesOrderInfoBella.getTotalPrice());
                sidelinesOrderList.add(sidelinesOrderInfoBella);
                showSidelineAdapter(sidelinesOrderList);
                ll_piiza_info_item.setVisibility(View.GONE);
            }
        }

        if (friedRollOrderInfoBella != null) {
            if (friedRollOrderInfoBella.getType() != null && !friedRollOrderInfoBella.getType().equals("")) {
                if (friedRollOrderInfoBella.getType().equals(Common.FRIED_ROLL)) {
                    ll_piiza_info_item.setVisibility(View.GONE);
                    order_price.setText("Rs." + friedRollOrderInfoBella.getTotalPrice());

                    lst = new ArrayList<>();
                    lst.add(friedRollOrderInfoBella);
                    showFriedRollAdapter(lst);
                }
            }
        }

        if (comboOrderInfoBella != null) {
            if (comboOrderInfoBella.getType() != null && !comboOrderInfoBella.getType().equals("")) {
                if (comboOrderInfoBella.getType().equals(Common.MEAL)) {
                    ll_piiza_info_item.setVisibility(View.GONE);
                    order_price.setText("Rs." + comboOrderInfoBella.getTotalPrice());

                    comboLst = new ArrayList<>();
                    comboLst.add(comboOrderInfoBella);
                    showMealAdapter(comboLst);
                }else if (comboOrderInfoBella.getType().equals(Common.COMBO)) {
                    ll_piiza_info_item.setVisibility(View.GONE);
                    order_price.setText("Rs." + comboOrderInfoBella.getTotalPrice());

                    comboLst = new ArrayList<>();
                    comboLst.add(comboOrderInfoBella);
                    showMealAdapter(comboLst);
                }else if (comboOrderInfoBella.getType().equals(Common.SUMMER_SPECIAL) || comboOrderInfoBella.getType().equals(Common.WINTER_SPECIAL)) {
                    ll_piiza_info_item.setVisibility(View.GONE);
                    order_price.setText("Rs." + comboOrderInfoBella.getTotalPrice());

                    comboLst = new ArrayList<>();
                    comboLst.add(comboOrderInfoBella);
                    showMealAdapter(comboLst);
                }
            }
        }

        return v;
    }

    private void showFriedRollAdapter(List<FriedRollOrderInfo> list) {
        fRAdapter = new SelectedFriedRollAdapter(list,context);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(fRAdapter);
    }

    private void showChineseAdapter(List<ChineseCornerOrderInfo> list) {
        mCAdapter = new SelectedChineseAdapter(list,context);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mCAdapter);
    }

    private void showSidelineAdapter(List<SidelineOrderInfo> list) {
        mAdapter = new SelectedSidelinesAdapter(list,context);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void showMealAdapter(List<ComboOrderInfo> list) {
        mealdapter = new SelectedMealAdapter(list,context);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mealdapter);
    }

    private void showPizzas(){

        tv_pizza_top_name.setText(pizzaOrderInfoBella.getItemName());
        /*if (pizzaOrderInfoModel.getComboInfo()!=null) {
            if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
                tv_piiza_name.setText(pizzaOrderInfoModel.getComboInfo().getDescription());
            }else {
                tv_piiza_name.setText(pizzaOrderInfoModel.getPizzaName());
            }
        }else {
            tv_piiza_name.setText(pizzaOrderInfoModel.getPizzaName());
        }*/
        tv_piiza_name.setText(pizzaOrderInfoBella.getItemName());
        tv_crust_name.setText(pizzaOrderInfoBella.getCrust().getSidelineName());
        if (pizzaOrderInfoBella.getCrust().getPrice() != null && !pizzaOrderInfoBella.getCrust().getPrice().equals("")){
            tv_crust_price.setText("Rs."+pizzaOrderInfoBella.getCrust().getPrice());
        }else {
            tv_crust_price.setVisibility(View.INVISIBLE);
        }

        //tv_spice_name.setText(pizzaOrderInfoModel.getSpice().getSidelineName());

        if (pizzaOrderInfoBella.getPizzaToppingInfo() != null) {
            toppingsList.add(pizzaOrderInfoBella.getPizzaToppingInfo());
        }

        /*if (pizzaOrderInfoModel.getComboInfo()!=null) {
            if (pizzaOrderInfoModel.getComboInfo().getPrice()!=null && !pizzaOrderInfoModel.getComboInfo().getPrice().equals("")){
                tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getComboInfo().getPrice());
            }else {
                tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getPrice());
            }
        }else {
            tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getPrice());

        }*/
        tv_piiza_price.setText("Rs." + pizzaOrderInfoBella.getSizePrice());
        tv_total_piiza_price.setText("Rs." + pizzaOrderInfoBella.getTotalPrice());

        rl_full_square.setVisibility(View.VISIBLE);
        Glide.with(context).load(pizzaOrderInfoBella.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

        slice_three.setVisibility(View.GONE);
        slice_two.setVisibility(View.GONE);
        slice_four.setVisibility(View.GONE);
        slice_one.setVisibility(View.GONE);
        String vegeis = "";
        if (toppingsList != null) {
            if (toppingsList.size() > 0) {
                tv_toppings_name.setText(toppingsList.get(0).getItemName());

                for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                    } else {
                        vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                    }
                }
            }
        }
        tv_vegetables_name.setText(vegeis);
        ll_a.setVisibility(View.GONE);
        ll_b.setVisibility(View.GONE);
        ll_c.setVisibility(View.GONE);
        ll_d.setVisibility(View.GONE);
        slice_one.setVisibility(View.GONE);
        slice_two.setVisibility(View.GONE);
        slice_three.setVisibility(View.GONE);
        slice_four.setVisibility(View.GONE);


        //decide later
        if (pizzaOrderInfoBella.getExtraTopping() != null) {
            tv_extras_name.setText(pizzaOrderInfoBella.getExtraTopping().getName());
            tv_extras_price.setText(pizzaOrderInfoBella.getExtraTopping().getValue());

        }else{
            ll_extrass.setVisibility(View.GONE);
            ll_ext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btn_add_to_cart){
            setNextPhoneFragment();
        }else if (v == btn_change_selection){
            setFirstFragment();

            friedRollOrderInfoBella = null;
            friedRollOrderInfoBella = new FriedRollOrderInfo();
            comboOrderInfoBella = null;
            comboOrderInfoBella = new ComboOrderInfo();
            chineseCornerInfoBella = null;
            chineseCornerInfoBella = new ChineseCornerOrderInfo();
            sidelinesOrderInfoBella = null;
            sidelinesOrderInfoBella = new SidelineOrderInfo();
            sidelinesOrderList.clear();
            pizzaOrderInfoBella = null;
            pizzaOrderInfoBella = new PizzaOrderInfoBella();
        }

    }

    private void setFirstFragment() {
        ((MainActivity)context).onBackPressed();
    }

    private void setNextPhoneFragment() {
        if (lst != null){
            if (lst.size() > 0){
                for (FriedRollOrderInfo soi : lst) {
                    soi.setSinglePrice((soi.getSinglePrice()));
                    long a = new DBClass(context).InsertFriedRoll(soi);
                    Log.i("TAG", "setNextPhoneFragment: "+a);
                }
                friedRollOrderInfoBella = null;
                friedRollOrderInfoBella = new FriedRollOrderInfo();
            }
        }


        if (chineseCornerInfoBella!= null){
            if (chineseCornerInfoBella.getName()!=null && !chineseCornerInfoBella.getName().equals("")){
                long a = new DBClass(context).InsertChinese(chineseCornerInfoBella);
                chineseCornerInfoBella = null;
                chineseCornerInfoBella = new ChineseCornerOrderInfo();

            }
        }

        if (comboOrderInfoBella!= null){
            if (comboOrderInfoBella.getItemName()!=null && !comboOrderInfoBella.getItemName().equals("")){
                if (comboOrderInfoBella.getType().equals(Common.MEAL)) {
                    comboOrderInfoBella.setDealDescription(getDescription(comboOrderInfoBella,comboOrderInfoBella.getDealDescription()));
                    long a = new DBClass(context).InsertMeal(comboOrderInfoBella);
                    comboOrderInfoBella = null;
                    comboOrderInfoBella = new ComboOrderInfo();
                }else if (comboOrderInfoBella.getType().equals(Common.COMBO)) {
                    comboOrderInfoBella.setDealDescription(getDescription(comboOrderInfoBella,comboOrderInfoBella.getDealDescription()));
                    long a = new DBClass(context).InsertDeal(comboOrderInfoBella);
                    comboOrderInfoBella = null;
                    comboOrderInfoBella = new ComboOrderInfo();
                }else if (comboOrderInfoBella.getType().equals(Common.SUMMER_SPECIAL) || comboOrderInfoBella.getType().equals(Common.WINTER_SPECIAL)) {
                    comboOrderInfoBella.setDealDescription(getDescription(comboOrderInfoBella,comboOrderInfoBella.getDealDescription()));

                    long a = new DBClass(context).InsertDeal(comboOrderInfoBella);
                    comboOrderInfoBella = null;
                    comboOrderInfoBella = new ComboOrderInfo();
                }

                comboOrderInfoBella = null;
                comboOrderInfoBella = new ComboOrderInfo();
            }
        }

        if (sidelinesOrderList != null){
            if (sidelinesOrderList.size() > 0){
                for (SidelineOrderInfo soi : sidelinesOrderList) {
                   /* String tmp = "";
                    tmp = soi.getSidelineName() + " " + soi.getSizeInfo().getSize()*/
                    int q = soi.getQuantity();
                    int pri = Integer.parseInt(soi.getTotalPrice());
                    soi.setPrice((pri / q)+"");
                    long a = new DBClass(context).InsertSidelines(soi);
                    Log.i("TAG", "setNextPhoneFragment: "+a);

                }
                sidelinesOrderInfoBella = null;
                sidelinesOrderInfoBella = new SidelineOrderInfo();
                sidelinesOrderList.clear();
            }
        }

       /* if (tmpSidelines != null){
            if (tmpSidelines.size() > 0){
                for (SidelineOrderInfo soi : tmpSidelines) {
                    int q = soi.getQuantity();
                    int pri = Integer.parseInt(soi.getPrice());
                    soi.setPrice((pri / q)+"");
                    long a = new DBClass(context).InsertSidelines(soi);

                    Log.i("PIZTAG", "Sidelines: "+ a);
                }
            }
        }*/

        //new DBClass(context).DeletePizzaCart();

        if (pizzaOrderInfoBella.getItemName() != null) {

            PizzaOrderCartModel pocm = new PizzaOrderCartModel();
            pocm.setId(pizzaOrderInfoBella.getId());
            pocm.setPizzaName(pizzaOrderInfoBella.getItemName());
            pocm.setPrice(pizzaOrderInfoBella.getSinglePrice()+"");
            pocm.setQuantity(pizzaOrderInfoBella.getQuantity());
            pocm.setImageURL(pizzaOrderInfoBella.getImageURL());
//                pocm.setFlavourId(pizzaOrderInfoBella.getFlavourId());
            pocm.setExtraTopping(pizzaOrderInfoBella.getExtraTopping());
            pocm.setSizeName(pizzaOrderInfoBella.getSizeName());
            pocm.setSizePrice(pizzaOrderInfoBella.getSizePrice());
            pocm.setPizzaToppingInfo(pizzaOrderInfoBella.getPizzaToppingInfo());
            // pocm.setPizzaType(pizzaOrderInfoBella.getPizzaType());
            // pocm.setDough(pizzaOrderInfoBella.getDough());
            pocm.setCrust(pizzaOrderInfoBella.getCrust());
            //pocm.setSauce(pizzaOrderInfoBella.getSauce());
            //pocm.setSpice(pizzaOrderInfoBella.getSpice());
            // pocm.setComboInfo(pizzaOrderInfoBella.getComboInfo());

            // pocm.setToppingInfoList(uriList);
            // pocm.setPizzaExtraToppingList(pizzaOrderInfoModel.getPizzaExtraToppingList());
            long a = new DBClass(context).InsertPizzas(pocm);
            Log.i("PIZTAG", "Pizzas: " + a);

            pizzaOrderInfoBella = null;
            pizzaOrderInfoBella = new PizzaOrderInfoBella();

        }


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
            }else{
                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

            friedRollOrderInfoBella = null;
            friedRollOrderInfoBella = new FriedRollOrderInfo();
            comboOrderInfoBella = null;
            comboOrderInfoBella = new ComboOrderInfo();
            chineseCornerInfoBella = null;
            chineseCornerInfoBella = new ChineseCornerOrderInfo();
            sidelinesOrderInfoBella = null;
            sidelinesOrderInfoBella = new SidelineOrderInfo();
            pizzaOrderInfoBella = null;
            pizzaOrderInfoBella = new PizzaOrderInfoBella();
            sidelinesOrderList.clear();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }




    }

    private String getDescription(@NonNull ComboOrderInfo model, String descrptn) {
        String des = "";

        /*if (model.getPizzaInfo()!= null){
            if (model.getPizzaInfo().getQuantiy() > 0) {
                des = model.getPizzaInfo().getQuantiy() + " " +  model.getPizzaInfo().getSize()+ " Pizza(s)";
            }
        }*/

        if (model.getPizzaFlavours()!= null){
            if (model.getPizzaFlavours().size() > 0){
                for (PizzaToppingInfo i:model.getPizzaFlavours()) {
                    if (des.equals("")){
                        des += " "+i.getItemName();
                    }else {
                        des += ", " + i.getItemName();
                    }
                }
            }
        }

        if (model.getBurgerOrSandwichInfoList()!= null){
            if (model.getBurgerOrSandwichInfoList().size() > 0){
                if (des.equals("")){
                    des += /*" "+ model.getBurgerSandwichQuantity() +*/ " "+model.getBurgerOrSandwichInfoList().get(0).getItemName();

                }else {
                    des += ", "/*+ model.getBurgerSandwichQuantity() + " "*/+model.getBurgerOrSandwichInfoList().get(0).getItemName();
                }
            }
        }

        if (model.getFriedRollInfoList()!= null) {
            if (model.getFriedRollInfoList().size() > 0) {
                if (des != null && !des.equals("")){
                    des += ", "/*+ model.getFriedRollQuantity() + " "*/+model.getFriedRollInfoList().get(0).getItemName();

                }else {
                    des += /*" "+ model.getFriedRollQuantity() +*/ " "+model.getFriedRollInfoList().get(0).getItemName();
                }

            }
        }

       /* if (model.getBeveragesInfoInCombo()!= null) {
            des += ", " + model.getBeveragesInfoInCombo().getQuantiy() + " " + model.getBeveragesInfoInCombo().getDrinksSize()
                    + " soft drink(s)";
        }*/
        if (model.getColdPizList()!= null){
            if (model.getColdPizList().size() > 0){
                for (PairValuesModel i:model.getColdPizList()) {
                    if (des.equals("")){
                        des += " "+i.getName();
                    }else {
                        des += ", " + i.getName();
                    }
                }
            }
        }

        if (descrptn != null && !descrptn.equals("")){
            des = descrptn + " ("+des+")" ;
        }
        return des;
    }


}