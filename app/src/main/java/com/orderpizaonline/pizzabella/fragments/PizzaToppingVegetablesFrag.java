package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.adapter.PizzaVegetablesAdapter;
import com.orderpizaonline.pizzabella.enums.PizzaIngredientCategoryENum;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;

public class PizzaToppingVegetablesFrag extends Fragment implements View.OnClickListener {

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference  pizzaToppings;
    FirebaseRecyclerAdapter toppingAdapter,premiumToppingAdapter;
    RecyclerView toppingRecyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    TextView premium_price;
     String pri;
    int counter = 0;
    List<PizzaToppingInfo> selectedVegeList = new ArrayList<>();
    ImageView iv_backward, iv_forward, destination, destination_full,  destination_left_half_circle, destination_right_half_circle
            ,destination_right_full, destination_left_full;

    ImageView  square_destination, destination_full_square, destination_left_half_square, destination_right_half_square
            ,square_destination_right_full, square_destination_left_full, square_destination_left_top_full,square_destination_right_top_full , square_destination_left_bottom_full
            , square_destination_right_bottom_full, left_top_square_sel, right_top_square_sel, left_bottom_square_sel,right_bottom_square_sel;

    ImageView  semi_circle_destination, semi_destination_full, semi_left_circle_sel, semi_right_circle_sel
            ,semi_destination_right_full, semi_destination_left_full;
    ImageView slice_destination;

    private RelativeLayout rl_full_circle, rl_full_square, rl_semi_circle, rl_slice;
    List<SidelineInfo> vegetablesList = new ArrayList();
    PizzaVegetablesAdapter adapter;
    int orderPrice = 0;
    TextView tv_price;
    public static ArrayList<SidelineInfo> toppingVegeList = new ArrayList<>();
    RelativeLayout rl_below;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_your_vegetable, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);

        rl_below =v.findViewById(R.id.rl_below);
        rl_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        slice_destination =v.findViewById(R.id.slice_destination);
        rl_slice =v.findViewById(R.id.rl_slice);

        semi_circle_destination =v.findViewById(R.id.semi_circle_destination);
        semi_destination_full =v.findViewById(R.id.semi_destination_full);
        semi_left_circle_sel=v.findViewById(R.id.semi_left_circle_sel);
        semi_right_circle_sel=v.findViewById(R.id.semi_right_circle_sel);
        semi_destination_right_full=v.findViewById(R.id.semi_destination_right_full);
        semi_destination_left_full=v.findViewById(R.id.semi_destination_left_full);
        rl_semi_circle=v.findViewById(R.id.rl_semi_circle);

        semi_left_circle_sel.setOnClickListener(this);
        semi_right_circle_sel.setOnClickListener(this);

        // for Square Selection
        square_destination=v.findViewById(R.id.square_destination);
        destination_full_square=v.findViewById(R.id.destination_full_square);
        destination_left_half_square = v.findViewById(R.id.destination_left_half_square);
        destination_right_half_square =v.findViewById(R.id.destination_right_half_square);
        square_destination_right_full =v.findViewById(R.id.square_destination_right_full);
        square_destination_left_full =v.findViewById(R.id.square_destination_left_full);
        square_destination_left_top_full=v.findViewById(R.id.square_destination_left_top_full);
        square_destination_right_top_full=v.findViewById(R.id.square_destination_right_top_full);
        square_destination_left_bottom_full=v.findViewById(R.id.square_destination_left_bottom_full);
        square_destination_right_bottom_full=v.findViewById(R.id.square_destination_right_bottom_full);
        left_top_square_sel=v.findViewById(R.id.square_left_top_sel);
        right_top_square_sel=v.findViewById(R.id.square_right_top_sel);
        left_bottom_square_sel=v.findViewById(R.id.square_left_bottom_sel);
        right_bottom_square_sel=v.findViewById(R.id.square_right_bottom_sel);
        rl_full_square =v.findViewById(R.id.rl_full_square);
        rl_full_circle =v.findViewById(R.id.rl_full_circle);

        destination_full =v.findViewById(R.id.destination_full);
        destination_right_full =v.findViewById(R.id.destination_right_full);
        destination_left_full =v.findViewById(R.id.destination_left_full);
        destination_left_half_circle =v.findViewById(R.id.destination_left_half_circle);
        destination_right_half_circle =v.findViewById(R.id.destination_right_half_circle);

        premium_price =v.findViewById(R.id.premium_price);
        destination =v.findViewById(R.id.destination);
        tv_price =v.findViewById(R.id.tv_price);
        iv_forward =v.findViewById(R.id.iv_forward);
        iv_forward.setOnClickListener(this);
        iv_backward =v.findViewById(R.id.iv_backward);
        iv_backward.setOnClickListener(this);
        destination_left_half_circle.setOnClickListener(this);
        destination_right_half_circle.setOnClickListener(this);

        destination_left_half_square.setOnClickListener(this);
        destination_right_half_square.setOnClickListener(this);
        left_top_square_sel.setOnClickListener(this);
        right_top_square_sel.setOnClickListener(this);
        left_bottom_square_sel.setOnClickListener(this);
        right_bottom_square_sel.setOnClickListener(this);

        toppingRecyclerView =v.findViewById(R.id.pizzaVegeRecyclerview);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        toppingRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        getVegetables();
        if (pizzaOrderInfoModel.getTotalPrice() != null){
            tv_price.setText("Rs."+pizzaOrderInfoModel.getTotalPrice());
        }
        Log.i("PRTAG", "setNextPhoneFragment: "+pizzaOrderInfoModel.getTotalPrice());
        selectedVegeList = pizzaOrderInfoModel.getToppingInfoList();
        if (pizzaOrderInfoModel.getPizzaType() != null && !pizzaOrderInfoModel.getPizzaType().equals("")) {

            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                rl_full_circle.setVisibility(View.VISIBLE);
                if (selectedVegeList.size() == 1) {
                    //Picasso.get().load(selectedVegeList.get(0).getImageURL()).into(destination);
                    destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_circle_background));

                    destination.setImageBitmap(selectedVegeList.get(0).getImage());
                    destination_left_half_circle.setVisibility(View.INVISIBLE);
                    destination_right_half_circle.setVisibility(View.INVISIBLE);

                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
           /* for (int i = 0; i< vegetablesList.size(); i++) {
                for (int j = 0; j < toppingVegeList.size(); j++) {
                    if (vegetablesList.get(i).getId().equals(toppingVegeList.get(j).getId())){

                    }
                }
            }*/

                } else if (selectedVegeList.size() == 2) {
                    //Picasso.get().load(selectedVegeList.get(0).getImageURL()).into(destination_left_full);
                    //Picasso.get().load(selectedVegeList.get(1).getImageURL()).into(destination_right_full);
                    destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(selectedVegeList.get(0).getImage()));
                    destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(selectedVegeList.get(1).getImage()));
                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();

                    destination.setVisibility(View.INVISIBLE);
                }
            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                rl_slice.setVisibility(View.VISIBLE);
                if (selectedVegeList.size() == 1) {
                    Bitmap s = Common.getCircleBitmap(selectedVegeList.get(0).getImage());
                    slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2), -45));  // for top right quater  rotate bitmap

                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();

                }
            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                rl_semi_circle.setVisibility(View.VISIBLE);
                if (selectedVegeList.size() == 1) {
                    //Picasso.get().load(selectedVegeList.get(0).getImageURL()).into(destination);
                    semi_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_circle_background));
                    Bitmap s = Common.getCircleBitmap(selectedVegeList.get(0).getImage());
                    semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth(), s.getHeight() / 2));

                    semi_left_circle_sel.setVisibility(View.INVISIBLE);
                    semi_right_circle_sel.setVisibility(View.INVISIBLE);

                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
           /* for (int i = 0; i< vegetablesList.size(); i++) {
                for (int j = 0; j < toppingVegeList.size(); j++) {
                    if (vegetablesList.get(i).getId().equals(toppingVegeList.get(j).getId())){

                    }
                }
            }*/

                } else if (selectedVegeList.size() == 2) {
                    //Picasso.get().load(selectedVegeList.get(0).getImageURL()).into(destination_left_full);
                    //Picasso.get().load(selectedVegeList.get(1).getImageURL()).into(destination_right_full);
                    Bitmap s = Common.getCircleBitmap(selectedVegeList.get(0).getImage());
                    Bitmap s1 = Common.getCircleBitmap(selectedVegeList.get(1).getImage());
                    semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                    semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, s1.getHeight() / 2, s1.getWidth() / 2, s1.getHeight() / 2));  // for bottom right quater
                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();

                    semi_circle_destination.setVisibility(View.INVISIBLE);
                }
            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                rl_full_square.setVisibility(View.VISIBLE);
                if (selectedVegeList != null) {
                    if (selectedVegeList.size() != 0) {
                        if (selectedVegeList.size() == 1) {
                            //orderPrice = orderPrice + Integer.parseInt(selectedVegeList.get(0).getPrice());
                            destination_full_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                            square_destination.setImageBitmap(selectedVegeList.get(0).getImage());
                            destination_left_half_square.setVisibility(View.INVISIBLE);
                            destination_right_half_square.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                            toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
                        } else if (selectedVegeList.size() == 2) {
                            for (int i = 0; i < selectedVegeList.size(); i++) {
                                //SelectedSidelines.add(sidelinesOrderList.get(i));
                                if (i == 0) {
                                    toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
                                    square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(selectedVegeList.get(i).getImage()));

                                } else {
                                    square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(selectedVegeList.get(i).getImage()));
                                }
                                destination_left_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                destination_left_half_square.setVisibility(View.VISIBLE);
                                destination_right_half_square.setVisibility(View.VISIBLE);
                                left_top_square_sel.setVisibility(View.INVISIBLE);
                                right_top_square_sel.setVisibility(View.INVISIBLE);
                                left_bottom_square_sel.setVisibility(View.INVISIBLE);
                                right_bottom_square_sel.setVisibility(View.INVISIBLE);
                                square_destination.setVisibility(View.INVISIBLE);
                                // orderPrice = orderPrice + Integer.parseInt(selectedVegeList.get(i).getPrice());
                            }

                        } else if (selectedVegeList.size() == 3) {
                            boolean temp = true;
                            for (int i = 0; i < selectedVegeList.size(); i++) {

                                Bitmap s = selectedVegeList.get(i).getImage();
                                if (selectedVegeList.get(i).getLeftHalf() != null) {
                                    if (selectedVegeList.get(i).getLeftHalf().equals(Common.LeftHalf)) {
                                        if (temp) {
                                            temp = false;
                                            destination_left_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                        }
                                        square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(selectedVegeList.get(i).getImage()));
                                    }
                                } else if (selectedVegeList.get(i).getRightHalf() != null) {
                                    if (selectedVegeList.get(i).getRightHalf().equals(Common.RightHalf)) {
                                        if (temp) {
                                            temp = false;
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                            destination_right_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));

                                        }
                                        square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(selectedVegeList.get(i).getImage()));

                                    }
                                } else if (selectedVegeList.get(i).getRightTop() != null) {
                                    if (selectedVegeList.get(i).getRightTop().equals(Common.RightTop)) {
                                        if (temp) {
                                            temp = false;
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                            right_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                        }
                                        square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2));

                                    }
                                } else if (selectedVegeList.get(i).getRightBottom() != null) {
                                    if (selectedVegeList.get(i).getRightBottom().equals(Common.RightBottom)) {
                                        if (temp) {
                                            temp = false;
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                            right_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                        }
                                        square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));

                                    }
                                } else if (selectedVegeList.get(i).getLeftBottom() != null) {
                                    if (selectedVegeList.get(i).getLeftBottom().equals(Common.LeftBottom)) {
                                        if (temp) {
                                            temp = false;
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                            left_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                        }
                                        square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));

                                    }
                                } else if (selectedVegeList.get(i).getLeftTop() != null) {
                                    if (selectedVegeList.get(i).getLeftTop().equals(Common.LeftTop)) {
                                        if (temp) {
                                            temp = false;
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                            left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                        }
                                        square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                                    }
                                }
                            }
                        } else if (selectedVegeList.size() == 4) {
                            for (int i = 0; i < selectedVegeList.size(); i++) {

                                Bitmap s = selectedVegeList.get(i).getImage();
                                if (selectedVegeList.get(i).getRightTop() != null) {
                                    if (i == 1) {
                                        if (selectedVegeList.get(i).getRightTop().equals(Common.RightTop)) {
                                            square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2));

                                        }
                                    }
                                }
                                if (selectedVegeList.get(i).getRightBottom() != null) {
                                    if (i == 3) {
                                        if (selectedVegeList.get(i).getRightBottom().equals(Common.RightBottom)) {
                                            square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                        }
                                    }
                                }
                                if (selectedVegeList.get(i).getLeftBottom() != null) {
                                    if (i == 2) {
                                        if (selectedVegeList.get(i).getLeftBottom().equals(Common.LeftBottom)) {
                                            square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));

                                        }
                                    }
                                }
                                if (selectedVegeList.get(i).getLeftTop() != null) {
                                    if (i == 0) {
                                        if (selectedVegeList.get(i).getLeftTop().equals(Common.LeftTop)) {
                                            square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                                            toppingVegeList = selectedVegeList.get(i).getVegeInfoArrayList();
                                        }
                                    }
                                }

                                left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                                destination_left_half_square.setVisibility(View.INVISIBLE);
                                destination_right_half_square.setVisibility(View.INVISIBLE);
                                left_top_square_sel.setVisibility(View.VISIBLE);
                                right_top_square_sel.setVisibility(View.VISIBLE);
                                left_bottom_square_sel.setVisibility(View.VISIBLE);
                                right_bottom_square_sel.setVisibility(View.VISIBLE);
                                square_destination.setVisibility(View.VISIBLE);


                            }

                        }
                    }
                }
            }
        }

    /*    if (pizzaOrderInfoModel.getPizzaExtraSidelinesList() != null) {
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
        }*/

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            Log.i("POOT", "onClick: vege");

            setNextPhoneFragment();
        }else if (v == iv_backward) {
            Log.i("POOT", "onClick: vege back");
            ((MainActivity)context).onBackPressed();
            orderPrice = 0;
        }else if (v == destination_left_half_circle) {
            toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
            destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle_background));
            destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle));
            adapter.notifyDataSetChanged();

        }else if (v == destination_right_half_circle) {
            toppingVegeList = selectedVegeList.get(1).getVegeInfoArrayList();
            destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle_background));
            destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle));
            adapter.notifyDataSetChanged();

        }else if (v == semi_left_circle_sel) {
            toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
            semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle_background));
            semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle));
            adapter.notifyDataSetChanged();

        }else if (v == semi_right_circle_sel) {
            toppingVegeList = selectedVegeList.get(1).getVegeInfoArrayList();
            semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle_background));
            semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle));
            adapter.notifyDataSetChanged();

        }
        else if (v == destination_left_half_square) {
            toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
            destination_left_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            destination_right_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }
        else if (v == destination_right_half_square) {
            toppingVegeList = selectedVegeList.get(1).getVegeInfoArrayList();
            destination_right_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            destination_left_half_square.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }
        else if (v == left_top_square_sel) {
            toppingVegeList = selectedVegeList.get(0).getVegeInfoArrayList();
            left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            right_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            left_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            right_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }
        else if (v == right_top_square_sel) {
            toppingVegeList = selectedVegeList.get(1).getVegeInfoArrayList();
            right_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            left_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            right_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }
        else if (v == left_bottom_square_sel) {
            toppingVegeList = selectedVegeList.get(2).getVegeInfoArrayList();
            left_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            right_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            right_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }
        else if (v == right_bottom_square_sel) {
            toppingVegeList = selectedVegeList.get(3).getVegeInfoArrayList();
            right_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
            left_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            right_top_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            left_bottom_square_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
            adapter.notifyDataSetChanged();

        }



    }

    private void getVegetables() {

        vegetablesList = new ArrayList<>();
        pizzaToppings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vegetablesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SidelineInfo chat = snapshot.getValue(SidelineInfo.class);
                    if (chat.getCategory().equals(PizzaIngredientCategoryENum.Vegetable.toString())){
                        vegetablesList.add(chat);
                    }
                }

                adapter = new PizzaVegetablesAdapter(vegetablesList , context);
                toppingRecyclerView.setAdapter(adapter);
                Log.d("TAG", "onDataChange: drinks List :"+ Utils.getGsonParser().toJson(vegetablesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PizzaExtraToppingFragment hf = (PizzaExtraToppingFragment) fm.findFragmentByTag("PizzaExtraToppingFragment");
            if (hf == null) {
                hf = new PizzaExtraToppingFragment();
                ft.replace(R.id.main_frame, hf, "PizzaExtraToppingFragment");
                ft.addToBackStack("PizzaExtraToppingFragment");
            } else{
                ft.replace(R.id.main_frame, hf, "PizzaExtraToppingFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            ft.commit();

        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }

        orderPrice = 0;

    }


    public String convertToString(int price){
        String tmp = "Rs."+price;
        return tmp;
    }

}
