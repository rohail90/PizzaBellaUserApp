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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.enums.PizzaIngredientCategoryENum;
import com.orderpizaonline.pizzabella.enums.SidelineCategoryENum;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class PizzaExtraToppingFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines, pizzaToppings;
    FirebaseRecyclerAdapter toppingAdapter,dipsAdapter, drinksdapter;
    RecyclerView toppingRecyclerView, dipsRecyclerView, drinksRecyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
   /* LinearLayout ll_add_minus, ll_sideline_item;
    ImageView add, minus;
    RadioButton selection;
    TextView tv_count;*/
    int counter = 0;
    ImageView iv_backward, iv_forward;

    List<SidelineOrderInfo> selectedTopping = new ArrayList<>();
    List<SidelineOrderInfo> selectedDips = new ArrayList<>();
    List<SidelineOrderInfo> selectedDrinks = new ArrayList<>();

    int orderPrice = 0;
    int tmpPrice = 0;
    TextView tv_price;

    private SidelineInfo doughModel = new SidelineInfo();
    private SidelineInfo crustModel = new SidelineInfo();;
    private SidelineInfo sauceModel = new SidelineInfo();;
    private SidelineInfo spiceModel = new SidelineInfo();;
    int index = -1;
    int index2 = -1;
    int index3 = -1;
    int index4 = -1;
    RelativeLayout rl_below;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_your_pizza_extra_topping, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);
        sidelines = firebaseDatabase.getReference(Common.SIDE_LINES);

        rl_below =v.findViewById(R.id.rl_below);
        rl_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_price =v.findViewById(R.id.tv_price);
        iv_forward =v.findViewById(R.id.iv_forward);
        iv_forward.setOnClickListener(this);
        iv_backward =v.findViewById(R.id.iv_backward);
        iv_backward.setOnClickListener(this);

        toppingRecyclerView =v.findViewById(R.id.extraToppingsRecyclerview);
        dipsRecyclerView =v.findViewById(R.id.extraDipsRecyclerview);
        drinksRecyclerView =v.findViewById(R.id.extraDrinksRecyclerview);

        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        toppingRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        dipsRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);
        StaggeredGridLayoutManager staggeredGridLayoutManagerSpice = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        drinksRecyclerView.setLayoutManager(staggeredGridLayoutManagerSpice);

        showToppings();
        showDips();
        showDrinks();

        if (pizzaOrderInfoModel.getTotalPrice() != null && !pizzaOrderInfoModel.getTotalPrice().equals("")){
            tmpPrice =   Integer.parseInt(pizzaOrderInfoModel.getTotalPrice());
            tv_price.setText(convertToString(tmpPrice));
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            Log.i("POOT", "onClick: ext Top");
            setNextPhoneFragment();

        }else if (v == iv_backward) {
            Log.i("POOT", "onClick: ext Top back");
            orderPrice = 0;
            ((MainActivity)context).onBackPressed();
        }

    }

    @Override
    public void onResume() {

        super.onResume();
    }
    private void showToppings() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                pizzaToppings.orderByChild("category").equalTo(PizzaIngredientCategoryENum.Extra_Topping.toString()), SidelineInfo.class).build();
        toppingAdapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                itemPrice.setText("Rs."+model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);

                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);

                if (pizzaOrderInfoModel.getPizzaExtraToppingList() != null) {
                    if (pizzaOrderInfoModel.getPizzaExtraToppingList().size() != 0) {
                        for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraToppingList().size(); i++) {
                            if (pizzaOrderInfoModel.getPizzaExtraToppingList().get(i).getId().equals(model.getId())) {
                                selectedTopping.add(pizzaOrderInfoModel.getPizzaExtraToppingList().get(i));
                                selection.setChecked(true);
                                ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                            }
                        }
                    }
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        SidelineOrderInfo orderModel = new SidelineOrderInfo();
                        orderModel.setId(model.getId());
                        orderModel.setCategory(model.getCategory());
                        orderModel.setImageURL(model.getImageURL());
                        orderModel.setPrice(model.getPrice());
                        orderModel.setSidelineName(model.getSidelineName());
                        orderModel.setQuantity(1);
                        boolean isFOund = false;

                        if (selectedTopping != null) {
                            for (int i = 0; i < selectedTopping.size(); i++) {
                                if (model.getId().equals(selectedTopping.get(i).getId())) {
                                    isFOund = true;
                                    orderPrice -= Integer.parseInt(selectedTopping.get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice+tmpPrice));
                                    selectedTopping.remove(i);
                                    pizzaOrderInfoModel.getPizzaExtraToppingList().clear();
                                    pizzaOrderInfoModel.getPizzaExtraToppingList().addAll(selectedTopping);
                                }
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice+tmpPrice));
                            selectedTopping.add(orderModel);
                            List<SidelineOrderInfo> tmp = new ArrayList<>();
                            pizzaOrderInfoModel.setPizzaExtraToppingList(tmp);
                            pizzaOrderInfoModel.getPizzaExtraToppingList().addAll(selectedTopping);
                            selection.setChecked(true);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        toppingAdapter.startListening();
        toppingRecyclerView.setAdapter(toppingAdapter);
    }


    private void showDips() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                sidelines.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Dips.toString()), SidelineInfo.class).build();
        dipsAdapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                itemPrice.setText("Rs."+model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);
                final RadioButton selection =holder.itemView.findViewById(R.id.selection);


                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);

                if (pizzaOrderInfoModel.getPizzaExtraDipsList() != null) {
                    if (pizzaOrderInfoModel.getPizzaExtraDipsList().size() != 0) {
                        for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDipsList().size(); i++) {
                            if (pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getId().equals(model.getId())) {
                                selectedDips.add(pizzaOrderInfoModel.getPizzaExtraDipsList().get(i));
                                // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                //  tv_price.setText(convertToString(orderPrice));
                                tv_count.setText(pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getQuantity()+"");
                                ll_add_minus.setVisibility(View.VISIBLE);

                                selection.setChecked(true);
                                ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                            }
                        }
                    }
                }


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDipsList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDipsList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getId().equals(model.getId())) {
                                    int temp = pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getQuantity() + 1;
                                    selectedDips.get(i).setQuantity(temp);
                                    int pr = Integer.parseInt(model.getPrice());
                                    pr += Integer.parseInt(selectedDips.get(i).getPrice());
                                    orderPrice += Integer.parseInt(model.getPrice());
                                    selectedDips.get(i).setPrice(String.valueOf(pr));
                                    //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);

                                    List<SidelineOrderInfo> tmp = new ArrayList<>();
                                    pizzaOrderInfoModel.setPizzaExtraDipsList(tmp);
                                    pizzaOrderInfoModel.getPizzaExtraDipsList().addAll(selectedDips);

                                    tv_price.setText(convertToString(orderPrice+tmpPrice));
                                    tv_count.setText("" + temp);
                                }
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDipsList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDipsList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getId().equals(model.getId())) {
                                    if (pizzaOrderInfoModel.getPizzaExtraDipsList().get(i).getQuantity() > 1) {
                                        int temp = selectedDips.get(i).getQuantity() - 1;
                                        selectedDips.get(i).setQuantity(temp);

                                        int pr = Integer.parseInt(selectedDips.get(i).getPrice());

                                        pr -= Integer.parseInt(model.getPrice());
                                        orderPrice -= Integer.parseInt(model.getPrice());
                                        selectedDips.get(i).setPrice(String.valueOf(pr));
                                        tv_price.setText(convertToString(orderPrice+tmpPrice));

                                        //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                        pizzaOrderInfoModel.getPizzaExtraDipsList().clear();
                                        pizzaOrderInfoModel.getPizzaExtraDipsList().addAll(selectedDips);

                                        tv_count.setText("" + temp);
                                    } else {

                                        orderPrice -= Integer.parseInt(selectedDips.get(i).getPrice());
                                        tv_price.setText(convertToString(orderPrice+tmpPrice));
                                        ll_add_minus.setVisibility(View.GONE);
                                        selectedDips.remove(i);
                                        //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                        pizzaOrderInfoModel.getPizzaExtraDipsList().clear();
                                        pizzaOrderInfoModel.getPizzaExtraDipsList().addAll(selectedDips);
                                        ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                                        ll_add_minus.setVisibility(View.GONE);
                                        selection.setChecked(false);
                                    }
                                }
                            }
                        }
                    }
                });


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        SidelineOrderInfo orderModel = new SidelineOrderInfo();
                        orderModel.setId(model.getId());
                        orderModel.setCategory(model.getCategory());
                        orderModel.setImageURL(model.getImageURL());
                        orderModel.setPrice(model.getPrice());
                        orderModel.setSidelineName(model.getSidelineName());
                        orderModel.setQuantity(1);
                        boolean isFOund = false;

                        if (selectedDips != null) {
                            for (int i = 0; i < selectedDips.size(); i++) {
                                if (model.getId().equals(selectedDips.get(i).getId())) {
                                    isFOund = true;
                                    orderPrice -= Integer.parseInt(selectedDips.get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice+tmpPrice));
                                    selectedDips.remove(i);
                                    ll_add_minus.setVisibility(View.GONE);
                                    pizzaOrderInfoModel.getPizzaExtraDipsList().clear();
                                    pizzaOrderInfoModel.getPizzaExtraDipsList().addAll(selectedDips);
                                }
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice+tmpPrice));
                            selectedDips.add(orderModel);
                            List<SidelineOrderInfo> tmp = new ArrayList<>();
                            pizzaOrderInfoModel.setPizzaExtraDipsList(tmp);
                            pizzaOrderInfoModel.getPizzaExtraDipsList().addAll(selectedDips);
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        dipsAdapter.startListening();
        dipsRecyclerView.setAdapter(dipsAdapter);

    }

    private void showDrinks() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                sidelines.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Drinks.toString()), SidelineInfo.class).build();
        drinksdapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                itemPrice.setText("Rs."+model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);

                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);
                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);

                if (pizzaOrderInfoModel.getPizzaExtraDrinksList() != null) {
                    if (pizzaOrderInfoModel.getPizzaExtraDrinksList().size() != 0) {
                        for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDrinksList().size(); i++) {
                            if (pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getId().equals(model.getId())) {
                                selectedDrinks.add(pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i));
                                // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                //  tv_price.setText(convertToString(orderPrice));
                                tv_count.setText(pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getQuantity()+"");
                                ll_add_minus.setVisibility(View.VISIBLE);
                                selection.setChecked(true);
                                ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                            }
                        }
                    }
                }



                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDrinksList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDrinksList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getId().equals(model.getId())) {
                                    int temp = pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getQuantity() + 1;
                                    selectedDrinks.get(i).setQuantity(temp);
                                    int pr = Integer.parseInt(model.getPrice());
                                    pr += Integer.parseInt(selectedDrinks.get(i).getPrice());
                                    orderPrice += Integer.parseInt(model.getPrice());
                                    selectedDrinks.get(i).setPrice(String.valueOf(pr));
                                    //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);

                                    List<SidelineOrderInfo> tmp = new ArrayList<>();
                                    pizzaOrderInfoModel.setPizzaExtraDrinksList(tmp);
                                    pizzaOrderInfoModel.getPizzaExtraDrinksList().addAll(selectedDrinks);

                                    tv_price.setText(convertToString(orderPrice+tmpPrice));
                                    tv_count.setText("" + temp);
                                }
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDrinksList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDrinksList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getId().equals(model.getId())) {
                                    if (pizzaOrderInfoModel.getPizzaExtraDrinksList().get(i).getQuantity() > 1) {
                                        int temp = selectedDrinks.get(i).getQuantity() - 1;
                                        selectedDrinks.get(i).setQuantity(temp);

                                        int pr = Integer.parseInt(selectedDrinks.get(i).getPrice());

                                        pr -= Integer.parseInt(model.getPrice());
                                        orderPrice -= Integer.parseInt(model.getPrice());
                                        selectedDrinks.get(i).setPrice(String.valueOf(pr));
                                        tv_price.setText(convertToString(orderPrice+tmpPrice));

                                        //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                        pizzaOrderInfoModel.getPizzaExtraDrinksList().clear();
                                        pizzaOrderInfoModel.getPizzaExtraDrinksList().addAll(selectedDrinks);

                                        tv_count.setText("" + temp);
                                    } else {

                                        orderPrice -= Integer.parseInt(selectedDrinks.get(i).getPrice());
                                        tv_price.setText(convertToString(orderPrice+tmpPrice));
                                        ll_add_minus.setVisibility(View.GONE);
                                        selectedDrinks.remove(i);
                                        //pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                        pizzaOrderInfoModel.getPizzaExtraDrinksList().clear();
                                        pizzaOrderInfoModel.getPizzaExtraDrinksList().addAll(selectedDrinks);
                                        ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                                        ll_add_minus.setVisibility(View.GONE);
                                        selection.setChecked(false);
                                    }
                                }
                            }
                        }
                    }
                });



                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        SidelineOrderInfo orderModel = new SidelineOrderInfo();
                        orderModel.setId(model.getId());
                        orderModel.setCategory(model.getCategory());
                        orderModel.setImageURL(model.getImageURL());
                        orderModel.setPrice(model.getPrice());
                        orderModel.setSidelineName(model.getSidelineName());
                        orderModel.setQuantity(1);
                        boolean isFOund = false;

                        if (selectedDrinks != null) {
                            for (int i = 0; i < selectedDrinks.size(); i++) {
                                if (model.getId().equals(selectedDrinks.get(i).getId())) {
                                    isFOund = true;
                                    orderPrice -= Integer.parseInt(selectedDrinks.get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice+tmpPrice));
                                    selectedDrinks.remove(i);
                                    ll_add_minus.setVisibility(View.GONE);
                                    pizzaOrderInfoModel.getPizzaExtraDrinksList().clear();
                                    pizzaOrderInfoModel.getPizzaExtraDrinksList().addAll(selectedDrinks);
                                }
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice+tmpPrice));
                            selectedDrinks.add(orderModel);
                            List<SidelineOrderInfo> tmp = new ArrayList<>();
                            pizzaOrderInfoModel.setPizzaExtraDrinksList(tmp);
                            pizzaOrderInfoModel.getPizzaExtraDrinksList().addAll(selectedDrinks);
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        drinksdapter.startListening();
        drinksRecyclerView.setAdapter(drinksdapter);

    }
    private void setNextPhoneFragment() {
        try {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PizzaExtraSidelineFrag hf = (PizzaExtraSidelineFrag) fm.findFragmentByTag("PizzaExtraSidelineFrag");
            if (hf == null) {
                hf = new PizzaExtraSidelineFrag();
                ft.replace(R.id.main_frame, hf, "PizzaExtraSidelineFrag");
                ft.addToBackStack("PizzaExtraSidelineFrag");
            }else{
                ft.replace(R.id.main_frame, hf, "PizzaExtraSidelineFrag");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            pizzaOrderInfoModel.setTotalPrice(orderPrice+tmpPrice+"");
            orderPrice = 0;
            selectedTopping.clear();
            selectedDips.clear();
            selectedDrinks.clear();
            for (SidelineOrderInfo soi : pizzaOrderInfoModel.getPizzaExtraToppingList()){
                Log.i("OTG", pizzaOrderInfoModel.getPizzaExtraToppingList().size()+"setNextPhoneFragment: "+ soi.getSidelineName());
            }
        } catch (Exception e) {
            Log.d("EXC", "PizzaExtraSidelineFrag: " + e.getMessage());
        }


    }


    private void setPreviousPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SidelinesDessertsFragment hf = (SidelinesDessertsFragment) fm.findFragmentByTag("SidelineDessertFragment");
            if (hf == null) {
                hf = new SidelinesDessertsFragment();
            }
            ft.replace(R.id.main_frame, hf, "SidelineDessertFragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }


    }


    public String convertToString(int price){
        String tmp = "Rs."+price;
        return tmp;
    }
}