package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.orderpizaonline.pizzabella.adapter.ExtraToppingAdapter;
import com.orderpizaonline.pizzabella.adapter.SelectedSidelinesAdapter;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;

public class SelectedSidelinesFragment extends Fragment implements View.OnClickListener {
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
    private LinearLayout ll_pizza_info_item, ll_extrass, slice_four, slice_one, slice_two, slice_three, ll_d
            , ll_a, ll_b, ll_c, ll_toppings, ll_vagetables, ll_top, ll_veg, ll_ext;
    List<PizzaToppingInfo> toppingsList = new ArrayList<>();
    private RelativeLayout rl_full_circle, rl_full_square, rl_semi_circle, rl_slice;
    ListView lv_extras;
    List<SidelineOrderInfo> tmpSidelines;
    LinearLayout ll_piiza_info_item;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.selected_sidelines_fragment, container, false);
        context = container.getContext();

        ll_piiza_info_item =v.findViewById(R.id.ll_piiza_info_item);

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
        ll_pizza_info_item = v.findViewById(R.id.ll_piiza_info_item);

        recyclerView = v.findViewById(R.id.sidelinesRecyclerview);
       // pizzassRecyclerview = v.findViewById(R.id.pizzassRecyclerview);
        order_price = v.findViewById(R.id.order_price);
        btn_add_to_cart = v.findViewById(R.id.btn_add_to_cart);
        btn_add_to_cart.setOnClickListener(this);
        btn_change_selection = v.findViewById(R.id.btn_change_selection);
        btn_change_selection.setOnClickListener(this);


        if (pizzaOrderInfoModel != null) {
            if (pizzaOrderInfoModel.getPizzaName() != null && !pizzaOrderInfoModel.getPizzaName().equals("")) {
                order_price.setText("Rs." + pizzaOrderInfoModel.getTotalPrice());
                showPizzas();
            }else {
                int total = 0;
                for (SidelineOrderInfo oi : sidelinesOrderList) {
                    total += Integer.parseInt(oi.getPrice());
                }
                order_price.setText("Rs."+total);

                showSidelineAdapter(sidelinesOrderList);
                ll_piiza_info_item.setVisibility(View.GONE);


            }
        }else {
            int total = 0;
            for (SidelineOrderInfo oi : sidelinesOrderList) {
                total += Integer.parseInt(oi.getPrice());
            }
            order_price.setText("Rs."+total);

            showSidelineAdapter(sidelinesOrderList);
            ll_piiza_info_item.setVisibility(View.GONE);

        }

        return v;
    }

    private void showSidelineAdapter(List<SidelineOrderInfo> list) {
        mAdapter = new SelectedSidelinesAdapter(list,context);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void showPizzas(){


        tv_pizza_top_name.setText(pizzaOrderInfoModel.getPizzaName());
        if (pizzaOrderInfoModel.getComboInfo()!=null) {
            if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
                tv_piiza_name.setText(pizzaOrderInfoModel.getComboInfo().getDescription());
            }else {
                tv_piiza_name.setText(pizzaOrderInfoModel.getPizzaName());
            }
        }else {
            tv_piiza_name.setText(pizzaOrderInfoModel.getPizzaName());
        }
        tv_crust_name.setText(pizzaOrderInfoModel.getCrust().getSidelineName());
        if (pizzaOrderInfoModel.getCrust().getPrice() != null && !pizzaOrderInfoModel.getCrust().getPrice().equals("")){
            tv_crust_price.setText("Rs."+pizzaOrderInfoModel.getCrust().getPrice());
        }else {
            tv_crust_price.setVisibility(View.INVISIBLE);
        }
        tv_spice_name.setText(pizzaOrderInfoModel.getSpice().getSidelineName());

        if (pizzaOrderInfoModel.getToppingInfoList().size() > 0) {
            toppingsList.addAll(pizzaOrderInfoModel.getToppingInfoList());
        }

        if (pizzaOrderInfoModel.getComboInfo()!=null) {
            if (pizzaOrderInfoModel.getComboInfo().getPrice()!=null && !pizzaOrderInfoModel.getComboInfo().getPrice().equals("")){
                tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getComboInfo().getPrice());
            }else {
                tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getPrice());
            }
        }else {
            tv_piiza_price.setText("Rs." + pizzaOrderInfoModel.getPrice());

        }

        if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
            rl_full_circle.setVisibility(View.VISIBLE);
            if (toppingsList.size() == 1) {
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(destination);

                    } else {
                        destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    }
                } else {
                    destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                }

                slice_three.setVisibility(View.GONE);
                slice_two.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                slice_one.setVisibility(View.GONE);

                tv_toppings_name.setText(toppingsList.get(0).getItemName());
                String vegeis = "";
                for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                    } else {
                        vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
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


            } else if (toppingsList.size() == 2) {

                slice_three.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                ll_toppings.setVisibility(View.GONE);
                ll_vagetables.setVisibility(View.GONE);
                ll_c.setVisibility(View.GONE);
                ll_d.setVisibility(View.GONE);
                ll_top.setVisibility(View.GONE);
                ll_veg.setVisibility(View.GONE);
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(destination);
                    } else {
                        destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                        destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage()));
                    }
                } else {

                    destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                    destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage()));
                }

                for (int j = 0; j < toppingsList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(toppingsList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(0).getPrice() != null && !toppingsList.get(0).getPrice().equals(""))) {
                            tv_sliceOne_toppings_price.setText("Rs." + toppingsList.get(0).getPrice());
                        } else {
                            tv_sliceOne_toppings_price.setVisibility(View.GONE);
                        }
                   /* if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceOne_name.setText("Slice 1");
                    }else {
                        tv_sliceOne_name.setText("Half 1");
                    }*/
                    }
                    if (j == 1) {

                        tv_sliceOne_name.setText("Half 2");
                        tv_sliceTwo_name.setText("Half 2");
                        tv_sliceTwo_toppings_name.setText(toppingsList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(1).getPrice() != null && !toppingsList.get(1).getPrice().equals(""))) {
                            tv_sliceTwo_toppings_price.setText("Rs." + toppingsList.get(1).getPrice());
                        } else {
                            tv_sliceTwo_toppings_price.setVisibility(View.GONE);
                        }
                    /*if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceTwo_name.setText("Slice 2");
                    }else {
                        tv_sliceTwo_name.setText("Half 2");
                    }*/
                    }
                }
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
            rl_slice.setVisibility(View.VISIBLE);
            if (toppingsList.size() == 1) {
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(slice_destination_full);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(slice_destination_full);


                    } else {
                        Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                        slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,0,s.getWidth()/2,s.getHeight()/2),-45));  // for top right quater  rotate bitmap

                       // slice_destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    }
                } else {
                    Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,0,s.getWidth()/2,s.getHeight()/2),-45));  // for top right quater  rotate bitmap

                }

                slice_three.setVisibility(View.GONE);
                slice_two.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                slice_one.setVisibility(View.GONE);


                tv_toppings_name.setText(toppingsList.get(0).getItemName());
                String vegeis = "";
                for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                    } else {
                        vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
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


            }
        } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
            rl_semi_circle.setVisibility(View.VISIBLE);
            if (toppingsList.size() == 1) {
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(semi_circle_destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(semi_circle_destination);

                    } else {
                        Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                        semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth(),s.getHeight()/2));

                    }
                } else {

                    Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth(),s.getHeight()/2));
                }

                slice_three.setVisibility(View.GONE);
                slice_two.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                slice_one.setVisibility(View.GONE);


                tv_toppings_name.setText(toppingsList.get(0).getItemName());
                String vegeis = "";
                for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                    } else {
                        vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
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


            } else if (toppingsList.size() == 2) {
                slice_three.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                ll_toppings.setVisibility(View.GONE);
                ll_vagetables.setVisibility(View.GONE);
                ll_c.setVisibility(View.GONE);
                ll_d.setVisibility(View.GONE);
                ll_top.setVisibility(View.GONE);
                ll_veg.setVisibility(View.GONE);
                tv_sliceOne_name.setText("Half 2");
                tv_sliceTwo_name.setText("Half 2");

                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(semi_circle_destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(semi_circle_destination);

                    } else {
                        Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                        Bitmap s1 = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage());
                        semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));
                        semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1,s1.getWidth()/2,s1.getHeight()/2,s1.getWidth()/2,s1.getHeight()/2));  // for bottom right quater

                    }
                } else {



                    Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    Bitmap s1 = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage());
                    semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));
                    semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1,s1.getWidth()/2,s1.getHeight()/2,s1.getWidth()/2,s1.getHeight()/2));  // for bottom right quater
                }

                for (int j = 0; j < toppingsList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(toppingsList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(0).getPrice() != null && !toppingsList.get(0).getPrice().equals(""))) {
                            tv_sliceOne_toppings_price.setText("Rs." + toppingsList.get(0).getPrice());
                        } else {
                            tv_sliceOne_toppings_price.setVisibility(View.GONE);
                        }
                   /* if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceOne_name.setText("Slice 1");
                    }else {
                        tv_sliceOne_name.setText("Half 1");
                    }*/
                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(toppingsList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(1).getPrice() != null && !toppingsList.get(1).getPrice().equals(""))) {
                            tv_sliceTwo_toppings_price.setText("Rs." + toppingsList.get(1).getPrice());
                        } else {
                            tv_sliceTwo_toppings_price.setVisibility(View.GONE);
                        }
                    /*if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceTwo_name.setText("Slice 2");
                    }else {
                        tv_sliceTwo_name.setText("Half 2");
                    }*/
                    }
                }
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {
            rl_full_square.setVisibility(View.VISIBLE);

            if (toppingsList.size() == 1) {
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(square_destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);


                    } else {
                        square_destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                    }
                } else {
                    square_destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                }

                slice_three.setVisibility(View.GONE);
                slice_two.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                slice_one.setVisibility(View.GONE);

                tv_toppings_name.setText(toppingsList.get(0).getItemName());
                String vegeis = "";
                for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                    } else {
                        vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
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


            } else if (toppingsList.size() == 2) {
                slice_three.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                ll_toppings.setVisibility(View.GONE);
                ll_vagetables.setVisibility(View.GONE);
                ll_c.setVisibility(View.GONE);
                ll_d.setVisibility(View.GONE);
                ll_top.setVisibility(View.GONE);
                ll_veg.setVisibility(View.GONE);


                tv_sliceOne_name.setText("Half 1");
                tv_sliceTwo_name.setText("Half 2");
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(square_destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

                    } else {
                        square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                        square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage()));
                    }
                } else {

                    square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                    square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage()));
                }

                for (int j = 0; j < toppingsList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(toppingsList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(0).getPrice() != null && !toppingsList.get(0).getPrice().equals(""))) {
                            tv_sliceOne_toppings_price.setText("Rs." + toppingsList.get(0).getPrice());
                        } else {
                            tv_sliceOne_toppings_price.setVisibility(View.GONE);
                        }

                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(toppingsList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(1).getPrice() != null && !toppingsList.get(1).getPrice().equals(""))) {
                            tv_sliceTwo_toppings_price.setText("Rs." + toppingsList.get(1).getPrice());
                        } else {
                            tv_sliceTwo_toppings_price.setVisibility(View.GONE);
                        }



                    }
                }
            } else if (toppingsList.size() == 4) {
               // slice_three.setVisibility(View.GONE);
               // slice_four.setVisibility(View.GONE);
                ll_toppings.setVisibility(View.GONE);
                ll_vagetables.setVisibility(View.GONE);
               // ll_c.setVisibility(View.GONE);
               // ll_d.setVisibility(View.GONE);
                ll_top.setVisibility(View.GONE);
                ll_veg.setVisibility(View.GONE);


                tv_sliceOne_name.setText("Quarter 1");
                tv_sliceTwo_name.setText("Quarter 2");
                tv_sliceThree_name.setText("Quarter 3");
                tv_slicefour_name.setText("Quarter 4");
                Bitmap s = pizzaOrderInfoModel.getToppingInfoList().get(0).getImage(),s1 = pizzaOrderInfoModel.getToppingInfoList().get(1).getImage(),
                        s2 = pizzaOrderInfoModel.getToppingInfoList().get(2).getImage(),s3 = pizzaOrderInfoModel.getToppingInfoList().get(3).getImage();
                if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(square_destination);
                        Glide.with(context).load(pizzaOrderInfoModel.getComboInfo()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

                    } else {
                        square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s,0,0,s.getWidth()/2,s.getHeight()/2));
                        square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, 0, s1.getWidth() / 2, s1.getHeight() / 2));
                        square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s2, 0, s2.getHeight() / 2, s2.getWidth() / 2, s2.getHeight() / 2));
                        square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s3, s3.getWidth() / 2, s3.getHeight() / 2, s3.getWidth() / 2, s3.getHeight() / 2));
                    }
                }else {
                    square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s,0,0,s.getWidth()/2,s.getHeight()/2));
                    square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, 0, s1.getWidth() / 2, s1.getHeight() / 2));
                    square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s2, 0, s2.getHeight() / 2, s2.getWidth() / 2, s2.getHeight() / 2));
                    square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s3, s3.getWidth() / 2, s3.getHeight() / 2, s3.getWidth() / 2, s3.getHeight() / 2));
                }

                for (int j = 0; j < toppingsList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(toppingsList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(0).getPrice() != null && !toppingsList.get(0).getPrice().equals(""))) {
                            tv_sliceOne_toppings_price.setText("Rs." + toppingsList.get(0).getPrice());
                        } else {
                            tv_sliceOne_toppings_price.setVisibility(View.GONE);
                        }

                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(toppingsList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(1).getPrice() != null && !toppingsList.get(1).getPrice().equals(""))) {
                            tv_sliceTwo_toppings_price.setText("Rs." + toppingsList.get(1).getPrice());
                        } else {
                            tv_sliceTwo_toppings_price.setVisibility(View.GONE);
                        }

                    }
                    if (j == 2) {
                        tv_sliceThree_toppings_name.setText(toppingsList.get(2).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(2).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(2).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(2).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceThree_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(2).getPrice() != null && !toppingsList.get(2).getPrice().equals(""))) {
                            tv_sliceThree_toppings_price.setText("Rs." + toppingsList.get(2).getPrice());
                        } else {
                            tv_sliceThree_toppings_price.setVisibility(View.GONE);
                        }
                    }
                    if (j == 3) {
                        tv_slicefour_toppings_name.setText(toppingsList.get(3).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < toppingsList.get(3).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = toppingsList.get(3).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + toppingsList.get(3).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_slicefour_toppings_vegeis.setText(vegeis);
                        if ((toppingsList.get(3).getPrice() != null && !toppingsList.get(3).getPrice().equals(""))) {
                            tv_slicefour_toppings_price.setText("Rs." + toppingsList.get(3).getPrice());
                        } else {
                            tv_slicefour_toppings_price.setVisibility(View.GONE);
                        }
                    }
                }
            }

        }



        tmpSidelines = new ArrayList<>();
        if (pizzaOrderInfoModel.getPizzaExtraDipsList()!=null) {
            if (pizzaOrderInfoModel.getPizzaExtraDipsList().size() > 0) {
                tmpSidelines.addAll(pizzaOrderInfoModel.getPizzaExtraDipsList());
            }
        }
        if (pizzaOrderInfoModel.getPizzaExtraDrinksList()!=null) {
            if (pizzaOrderInfoModel.getPizzaExtraDrinksList().size() > 0) {
                tmpSidelines.addAll(pizzaOrderInfoModel.getPizzaExtraDrinksList());
            }
        }
        if (pizzaOrderInfoModel.getPizzaExtraSidelinesList()!=null) {
            if (pizzaOrderInfoModel.getPizzaExtraSidelinesList().size() > 0) {
                tmpSidelines.addAll(pizzaOrderInfoModel.getPizzaExtraSidelinesList());
            }
        }
        if (pizzaOrderInfoModel.getPizzaExtraDessertsList()!=null) {
            if (pizzaOrderInfoModel.getPizzaExtraDessertsList().size() > 0) {
                tmpSidelines.addAll(pizzaOrderInfoModel.getPizzaExtraDessertsList());
            }
        }

        if (tmpSidelines.size() > 0){
            showSidelineAdapter(tmpSidelines);
        }


        if (pizzaOrderInfoModel.getPizzaExtraToppingList() != null) {
            if (pizzaOrderInfoModel.getPizzaExtraToppingList().size() > 0) {
                ExtraToppingAdapter extraToppingAdapter = new ExtraToppingAdapter(context, pizzaOrderInfoModel.getPizzaExtraToppingList(), "showPrice");
                lv_extras.setAdapter(extraToppingAdapter);

            } else {
                ll_extrass.setVisibility(View.GONE);
                ll_ext.setVisibility(View.GONE);
            }

        }else{
            ll_extrass.setVisibility(View.GONE);
            ll_ext.setVisibility(View.GONE);
        }

        int total = 0;

        if (sidelinesOrderList != null) {
            if (sidelinesOrderList.size() > 0) {
                for (SidelineOrderInfo oi : sidelinesOrderList) {
                    total += Integer.parseInt(oi.getPrice());
                }
            }
        }

        if (pizzaOrderInfoModel!= null) {

            List<SidelineOrderInfo> pizzaExtraDipsList = new ArrayList<>();
            List<SidelineOrderInfo> pizzaExtraDrinksList = new ArrayList<>();
            List<SidelineOrderInfo> pizzaExtraSidelinesList = new ArrayList<>();
            List<SidelineOrderInfo> pizzaExtraDessertsList = new ArrayList<>();
            if (pizzaOrderInfoModel.getPizzaExtraDipsList() != null) {
                pizzaExtraDipsList = new ArrayList<>(pizzaOrderInfoModel.getPizzaExtraDipsList());
            }

            if (pizzaOrderInfoModel.getPizzaExtraDrinksList() != null) {
                pizzaExtraDrinksList = new ArrayList<>(pizzaOrderInfoModel.getPizzaExtraDrinksList());
            }
            if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
                pizzaExtraSidelinesList = new ArrayList<>(pizzaOrderInfoModel.getPizzaExtraSidelinesList());
            }
            if (pizzaOrderInfoModel.getPizzaExtraDessertsList() != null) {
                pizzaExtraDessertsList = new ArrayList<>(pizzaOrderInfoModel.getPizzaExtraDessertsList());
            }

            if (pizzaExtraDessertsList.size() > 0) {
                for (SidelineOrderInfo oi : pizzaExtraDessertsList) {
                    total += Integer.parseInt(oi.getPrice());
                }
            }

            if (pizzaExtraSidelinesList.size() > 0) {
                for (SidelineOrderInfo oi : pizzaExtraSidelinesList) {
                    total += Integer.parseInt(oi.getPrice());
                }
            }

            if (pizzaExtraDipsList.size() > 0) {
                for (SidelineOrderInfo oi : pizzaExtraDipsList) {
                    total += Integer.parseInt(oi.getPrice());
                }
            }

            if (pizzaExtraDrinksList.size() > 0) {
                for (SidelineOrderInfo oi : pizzaExtraDrinksList) {
                    total += Integer.parseInt(oi.getPrice());
                }
            }

            tv_total_piiza_price.setText("Rs." + (Integer.parseInt(pizzaOrderInfoModel.getTotalPrice()) - total));
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btn_add_to_cart){
            setNextPhoneFragment();
        }else if (v == btn_change_selection){
            setFirstFragment();
        }

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
        ((MainActivity)context).onBackPressed();
        /*if (pizzaOrderInfoModel.getPizzaName() == null) {
            try {
                FragmentManager fm = getActivity().getSupportFragmentManager();

                if (fm.getBackStackEntryCount() >= 1) {
                    for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                        fm.popBackStack();
                    }
                }

                FragmentTransaction ft = fm.beginTransaction();
                SidelinesDessertsFragment hf = (SidelinesDessertsFragment) fm.findFragmentByTag("SidelinesDessertsFragment");
                if (hf == null) {
                    hf = new SidelinesDessertsFragment();
                    ft.replace(R.id.main_frame, hf, "SidelinesDessertsFragment");
                    ft.addToBackStack("SidelinesDessertsFragment");
                }else {
                    ft.replace(R.id.main_frame, hf, "SidelinesDessertsFragment");
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            } catch (Exception e) {
                Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
            }
        }else {

            try {
                FragmentManager fm = getActivity().getSupportFragmentManager();

                if (fm.getBackStackEntryCount() > 0) {
                    for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                        fm.popBackStack();
                    }
                }
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

        }*/

    }
    private void setNextPhoneFragment() {

        if (sidelinesOrderList != null){
            if (sidelinesOrderList.size() > 0){
                for (SidelineOrderInfo soi : sidelinesOrderList) {
                    int q = soi.getQuantity();
                    int pri = Integer.parseInt(soi.getPrice());
                    soi.setPrice((pri / q)+"");
                    new DBClass(context).InsertSidelines(soi);
                }
            }
        }

        if (tmpSidelines != null){
            if (tmpSidelines.size() > 0){
                for (SidelineOrderInfo soi : tmpSidelines) {
                    int q = soi.getQuantity();
                    int pri = Integer.parseInt(soi.getPrice());
                    soi.setPrice((pri / q)+"");
                    long a = new DBClass(context).InsertSidelines(soi);

                    Log.i("PIZTAG", "Sidelines: "+ a);
                }
            }
        }

        //new DBClass(context).DeletePizzaCart();

            if (pizzaOrderInfoModel.getPizzaName() != null) {

                List<PizzaToppingInfo> uriList = new ArrayList<>();
                uriList.addAll(pizzaOrderInfoModel.getToppingInfoList());
                List<SidelineOrderInfo> pizzaExtraToppingList = new ArrayList<>();
                if (pizzaOrderInfoModel.getPizzaExtraToppingList()!= null) {
                    pizzaExtraToppingList = pizzaOrderInfoModel.getPizzaExtraToppingList();
                }
                int totPrice = 0;
                totPrice = pizzaOrderInfoModel.getQuantity() * Integer.parseInt(pizzaOrderInfoModel.getPrice());
                for (int i = 0; i < uriList.size(); i++) {
                    if (uriList.get(i).getPrice() != null && !uriList.get(i).getPrice().equals("")) {
                        totPrice += Integer.parseInt(uriList.get(i).getPrice());
                    }
                }
                if (pizzaExtraToppingList.size() > 0){
                    for (int i = 0; i < pizzaExtraToppingList.size(); i++) {
                        totPrice +=  Integer.parseInt(pizzaExtraToppingList.get(i).getPrice());
                    }
                }
                if (pizzaOrderInfoModel.getComboInfo() != null){
                    if (pizzaOrderInfoModel.getComboInfo().getPrice() != null && !pizzaOrderInfoModel.getComboInfo().getPrice().equals("")) {
                        totPrice += Integer.parseInt(pizzaOrderInfoModel.getComboInfo().getPrice());
                    }
                }
                if (pizzaOrderInfoModel.getSauce() != null) {
                    if (pizzaOrderInfoModel.getSauce().getPrice() != null && !pizzaOrderInfoModel.getSauce().getPrice().equals("")) {
                        totPrice += Integer.parseInt(pizzaOrderInfoModel.getSauce().getPrice());
                    }
                }
                if (pizzaOrderInfoModel.getCrust() != null) {
                    if (pizzaOrderInfoModel.getCrust().getPrice() != null  && !pizzaOrderInfoModel.getCrust().getPrice().equals("")) {
                        totPrice += Integer.parseInt(pizzaOrderInfoModel.getCrust().getPrice());
                    }
                }
                if (pizzaOrderInfoModel.getSpice() != null) {
                    if (pizzaOrderInfoModel.getSpice().getPrice() != null  && !pizzaOrderInfoModel.getSpice().getPrice().equals("")) {
                        totPrice += Integer.parseInt(pizzaOrderInfoModel.getSpice().getPrice());
                    }
                }
                if (pizzaOrderInfoModel.getDough() != null) {
                    if (pizzaOrderInfoModel.getDough().getPrice() != null && !pizzaOrderInfoModel.getDough().getPrice().equals("")) {
                        totPrice += Integer.parseInt(pizzaOrderInfoModel.getDough().getPrice());
                    }
                }

                PizzaOrderCartModel pocm = new PizzaOrderCartModel();
                pocm.setId(pizzaOrderInfoModel.getId());
                pocm.setPizzaName(pizzaOrderInfoModel.getPizzaName());
                pocm.setPrice(totPrice+"");
                pocm.setQuantity(pizzaOrderInfoModel.getQuantity());
                pocm.setImageURL(pizzaOrderInfoModel.getImageURL());
                pocm.setPizzaType(pizzaOrderInfoModel.getPizzaType());
                pocm.setDough(pizzaOrderInfoModel.getDough());
                pocm.setCrust(pizzaOrderInfoModel.getCrust());
                pocm.setSauce(pizzaOrderInfoModel.getSauce());
                pocm.setSpice(pizzaOrderInfoModel.getSpice());
                pocm.setComboInfo(pizzaOrderInfoModel.getComboInfo());


                if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                    for (int i = 0; i < uriList.size(); i++) {
                        if  (uriList.size() == 1) {
                            String uri = Common.getImagePath(context, uriList.get(i).getImage());
                            uriList.get(i).setImageUri(uri);
                            uriList.get(i).setImage(null);
                        }else if (uriList.size() == 2) {
                            if (i == 1) {
                                if (!uriList.get(0).getId().equals(uriList.get(1).getId())) {
                                    String uri = Common.getImagePath(context, uriList.get(i).getImage());
                                    uriList.get(i).setImageUri(uri);
                                    uriList.get(i).setImage(null);
                                } else {
                                    uriList.get(i).setImage(null);
                                }

                            } else {
                                String uri = Common.getImagePath(context, uriList.get(i).getImage());
                                uriList.get(i).setImageUri(uri);
                                uriList.get(i).setImage(null);
                            }
                        }else if (uriList.size() == 4){
                            String uri = Common.getImagePath(context, uriList.get(i).getImage());
                            uriList.get(i).setImageUri(uri);
                        }

                    }
                    for (int i = 0 ; i<uriList.size(); i++){
                        uriList.get(i).setImage(null);
                    }

                }
                if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE) || pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)|| pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)){
                    for (int i = 0; i < uriList.size(); i++) {
                        if (i == 1) {
                            if (!uriList.get(0).getId().equals(uriList.get(1).getId())) {
                                String uri = Common.getImagePath(context, uriList.get(i).getImage());
                                uriList.get(i).setImageUri(uri);
                                uriList.get(i).setImage(null);
                            } else {

                                uriList.get(i).setImage(null);
                            }

                        } else {
                            String uri = Common.getImagePath(context, uriList.get(i).getImage());
                            uriList.get(i).setImageUri(uri);
                            uriList.get(i).setImage(null);
                        }
                    }
                }

                pocm.setToppingInfoList(uriList);
                pocm.setPizzaExtraToppingList(pizzaOrderInfoModel.getPizzaExtraToppingList());
                long a = new DBClass(context).InsertPizzas(pocm);
                Log.i("PIZTAG", "Pizzas: " + a);
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
            pizzaOrderInfoModel = null;
            pizzaOrderInfoModel = new PizzaOrderInfo();
            sidelinesOrderList.clear();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }




    }


}