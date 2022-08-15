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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.enums.SidelineCategoryENum;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class PizzaExtraSidelinesFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas, pizzaToppings;
    FirebaseRecyclerAdapter toppingAdapter,dipsAdapter, drinksdapter;
    RecyclerView toppingRecyclerView, dipsRecyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    int counter = 0;
    ImageView iv_backward, iv_forward;

    List<SidelineOrderInfo> selectedsidelines = new ArrayList<>();
    List<SidelineOrderInfo> selectedDesserts = new ArrayList<>();

    int orderPrice = 0;
    TextView tv_price;

    private SidelineInfo doughModel = new SidelineInfo();
    private SidelineInfo crustModel = new SidelineInfo();;
    private SidelineInfo sauceModel = new SidelineInfo();;
    private SidelineInfo spiceModel = new SidelineInfo();;
    int index = -1;
    int index2 = -1;
    int index3 = -1;
    int index4 = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_extra_sidelines, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_TOPPING);
        pizzas = firebaseDatabase.getReference(Common.SIDE_LINES);

        tv_price =v.findViewById(R.id.tv_price);
        iv_forward =v.findViewById(R.id.iv_forward);
        iv_forward.setOnClickListener(this);
        iv_backward =v.findViewById(R.id.iv_backward);
        iv_backward.setOnClickListener(this);

        toppingRecyclerView =v.findViewById(R.id.extraToppingsRecyclerview);
        dipsRecyclerView =v.findViewById(R.id.extraDipsRecyclerview);

        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        toppingRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        dipsRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);

        showSidelines();
        showDesserts();

        if (pizzaOrderInfoModel.getTotalPrice() != null){
            orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getTotalPrice());
            tv_price.setText(convertToString(orderPrice));
        }
        if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
            if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().size() != 0) {
                for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraSidelinesList().size(); i++) {
                    //SelectedSidelines.add(sidelinesOrderList.get(i));
                    orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getPrice());
                    tv_price.setText(convertToString(orderPrice));
                }
            }
        }
        if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
            if (pizzaOrderInfoModel.getPizzaExtraDessertsList().size() != 0) {
                for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDessertsList().size(); i++) {
                    //SelectedSidelines.add(sidelinesOrderList.get(i));
                    orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getPrice());
                    tv_price.setText(convertToString(orderPrice));
                }
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            setNextPhoneFragment();
        }else if (v == iv_backward) {
            orderPrice = 0;
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }

    }

    @Override
    public void onResume() {

        super.onResume();
    }
    private void showSidelines() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                pizzas.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Sideline.toString()), SidelineInfo.class).build();
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
                Picasso.get().load(model.getImageURL()).into(itemImage);

                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);

                if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
                    if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().size() != 0) {
                        for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraSidelinesList().size(); i++) {
                            if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getId().equals(model.getId())) {
                                selectedsidelines.add(pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i));

                                tv_count.setText(pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getQuantity()+"");
                                ll_add_minus.setVisibility(View.VISIBLE);
                                // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                //  tv_price.setText(convertToString(orderPrice));
                                selection.setChecked(true);
                                ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                            }
                        }
                    }
                }

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraSidelinesList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getId().equals(model.getId())) {
                                    int temp = pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getQuantity() + 1;
                                    selectedsidelines.get(i).setQuantity(temp);
                                    int pr = Integer.parseInt(model.getPrice());
                                    pr += Integer.parseInt(selectedsidelines.get(i).getPrice());
                                    orderPrice += Integer.parseInt(model.getPrice());
                                    selectedsidelines.get(i).setPrice(String.valueOf(pr));
                                    pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                    tv_price.setText(convertToString(orderPrice));
                                    tv_count.setText("" + temp);
                                }
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraSidelinesList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getId().equals(model.getId())) {
                                    if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getQuantity() > 1) {
                                        int temp = selectedsidelines.get(i).getQuantity() - 1;
                                        selectedsidelines.get(i).setQuantity(temp);

                                        int pr = Integer.parseInt(selectedsidelines.get(i).getPrice());

                                        pr -= Integer.parseInt(model.getPrice());
                                        orderPrice -= Integer.parseInt(model.getPrice());
                                        selectedsidelines.get(i).setPrice(String.valueOf(pr));
                                        tv_price.setText(convertToString(orderPrice));

                                        pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                        tv_count.setText("" + temp);
                                    } else {

                                        orderPrice -= Integer.parseInt(selectedsidelines.get(i).getPrice());
                                        tv_price.setText(convertToString(orderPrice));
                                        ll_add_minus.setVisibility(View.GONE);
                                        selectedsidelines.remove(i);
                                        pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
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

                        if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraSidelinesList().size(); i++) {
                                if (model.getId().equals(pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getId())) {
                                    isFOund = true;
                                    orderPrice -= Integer.parseInt(pizzaOrderInfoModel.getPizzaExtraSidelinesList().get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice));
                                    selectedsidelines.remove(i);
                                    ll_add_minus.setVisibility(View.GONE);
                                    pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                                }
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice));
                            selectedsidelines.add(orderModel);
                            pizzaOrderInfoModel.setPizzaExtraSidelinesList(selectedsidelines);
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        toppingAdapter.startListening();
        toppingRecyclerView.setAdapter(toppingAdapter);
    }


    private void showDesserts() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                pizzas.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Desserts.toString()), SidelineInfo.class).build();
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
                Picasso.get().load(model.getImageURL()).into(itemImage);


                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);

                if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
                    if (pizzaOrderInfoModel.getPizzaExtraDessertsList().size() != 0) {
                        for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDessertsList().size(); i++) {
                            if (pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getId().equals(model.getId())) {
                                selectedDesserts.add(pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i));
                                tv_count.setText(pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getQuantity()+"");
                                ll_add_minus.setVisibility(View.VISIBLE);
                                // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                //  tv_price.setText(convertToString(orderPrice));
                                selection.setChecked(true);
                                ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                            }
                        }
                    }
                }


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDessertsList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getId().equals(model.getId())) {
                                    int temp = pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getQuantity() + 1;
                                    selectedDesserts.get(i).setQuantity(temp);
                                    int pr = Integer.parseInt(model.getPrice());
                                    pr += Integer.parseInt(selectedDesserts.get(i).getPrice());
                                    orderPrice += Integer.parseInt(model.getPrice());
                                    selectedDesserts.get(i).setPrice(String.valueOf(pr));
                                    pizzaOrderInfoModel.setPizzaExtraDessertsList(selectedDesserts);
                                    tv_price.setText(convertToString(orderPrice));
                                    tv_count.setText("" + temp);
                                }
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDessertsList().size(); i++) {
                                if (pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getId().equals(model.getId())) {
                                    if (pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getQuantity() > 1) {
                                        int temp = selectedDesserts.get(i).getQuantity() - 1;
                                        selectedDesserts.get(i).setQuantity(temp);

                                        int pr = Integer.parseInt(selectedDesserts.get(i).getPrice());

                                        pr -= Integer.parseInt(model.getPrice());
                                        orderPrice -= Integer.parseInt(model.getPrice());
                                        selectedDesserts.get(i).setPrice(String.valueOf(pr));
                                        tv_price.setText(convertToString(orderPrice));

                                        pizzaOrderInfoModel.setPizzaExtraDessertsList(selectedDesserts);
                                        tv_count.setText("" + temp);
                                    } else {

                                        orderPrice -= Integer.parseInt(selectedDesserts.get(i).getPrice());
                                        tv_price.setText(convertToString(orderPrice));

                                        selectedDesserts.remove(i);
                                        pizzaOrderInfoModel.setPizzaExtraDessertsList(selectedDesserts);
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

                        if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
                            for (int i = 0; i < pizzaOrderInfoModel.getPizzaExtraDessertsList().size(); i++) {
                                if (model.getId().equals(pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getId())) {
                                    isFOund = true;
                                    orderPrice -= Integer.parseInt(pizzaOrderInfoModel.getPizzaExtraDessertsList().get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice));
                                    selectedDesserts.remove(i);
                                    ll_add_minus.setVisibility(View.GONE);
                                    pizzaOrderInfoModel.setPizzaExtraDessertsList(selectedDesserts);
                                }
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice));
                            selectedDesserts.add(orderModel);
                            pizzaOrderInfoModel.setPizzaExtraDessertsList(selectedDesserts);
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


    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectedSidelinesFragment hf = (SelectedSidelinesFragment) fm.findFragmentByTag("SelectedSidelinesFragment");
            if (hf == null) {
                hf = new SelectedSidelinesFragment();
                ft.replace(R.id.main_frame, hf, "SelectedSidelinesFragment");
                ft.addToBackStack("SelectedSidelinesFragment");
            } else{
                ft.replace(R.id.main_frame, hf, "SelectedSidelinesFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            ft.commit();
            orderPrice = 0;
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }

        pizzaOrderInfoModel.setTotalPrice(orderPrice+"");
        orderPrice = 0;
        selectedsidelines.clear();
        selectedDesserts.clear();

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