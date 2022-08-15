package com.orderpizaonline.pizzabella.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.orderpizaonline.pizzabella.adapter.FreeToppingsAdapter;
import com.orderpizaonline.pizzabella.adapter.PremiumToppingsAdapter;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;

public class PizzaToppingFlavorFrag extends Fragment implements View.OnClickListener, View.OnDragListener {

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference  pizzaToppings;
    RecyclerView toppingRecyclerView, premiumToppingRecyclerView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    FirebaseRecyclerAdapter adapterFirebase;
    TextView premium_price;
     String pri;
    TextView tv_dragtopping;
    ImageView iv_backward, iv_forward, destination, destination_full, destination_left_half_circle, destination_right_half_circle
            ,destination_right_full, destination_left_full;

    ImageView  semi_circle_destination, semi_destination_full, semi_left_circle_sel, semi_right_circle_sel, semi_full_circle_sel
            ,semi_destination_right_full, semi_destination_left_full;

    ImageView  square_destination/*, destination_full_square*//*, destination_left_half_square, destination_right_half_square*/
            ,square_destination_right_full, square_destination_left_full, square_destination_left_top_full,square_destination_right_top_full , square_destination_left_bottom_full
            , square_destination_right_bottom_full, left_top_square_sel, right_top_square_sel, left_mid_square_sel, full_mid_square_sel, right_mid_square_sel, left_bottom_square_sel,right_bottom_square_sel;
    ImageView slice_destination, slice_destination_full;
    List<PizzaToppingInfo> premiumToppings = new ArrayList<>();
    List<PizzaToppingInfo> freeToppings = new ArrayList<>();
    public static PizzaToppingInfo selectedFullCircleModel = new PizzaToppingInfo(), selectedLeftHalfCircleModel= new PizzaToppingInfo()
            , selectedRightHalfCircleModel = new PizzaToppingInfo(), selectedTopLeftModel = new PizzaToppingInfo(), selectedTopRightModel = new PizzaToppingInfo(),
            selectedBottomLeftModel = new PizzaToppingInfo(), selectedBottomRightModel = new PizzaToppingInfo();
    PremiumToppingsAdapter adapter;
    FreeToppingsAdapter freeToppingsAdapter;
    int orderPrice = 0;
    int cnt = 0;
    int cnts = 0;
    TextView tv_price;
    int tmpPrice = 0;
    private ProgressDialog dialog;
    private Bitmap selectedFull, selectedLeftHalf, selectedRightHalf, selectedTopLeft, selectedTopRight, selectedBottomLeft, selectedBottomRight;
    private RelativeLayout rl_full_circle, rl_full_square, rl_semi_circle, rl_slice, rl_full_half_circle, rl_semi_textviews, rl_square_textviews;
    RelativeLayout rl_below;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_your_pizza_topping, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzaToppings = firebaseDatabase.getReference(Common.PIZZA_TOPPING);
        square_destination=v.findViewById(R.id.square_destination);

        rl_below =v.findViewById(R.id.rl_below);
        rl_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rl_full_half_circle =v.findViewById(R.id.rl_full_half_circle);
        rl_semi_textviews =v.findViewById(R.id.rl_semi_textviews);
        rl_square_textviews =v.findViewById(R.id.rl_square_textviews);

        semi_circle_destination =v.findViewById(R.id.semi_circle_destination);
        semi_destination_full =v.findViewById(R.id.semi_destination_full);
        semi_left_circle_sel=v.findViewById(R.id.semi_left_circle_sel);
        semi_right_circle_sel=v.findViewById(R.id.semi_right_circle_sel);
        semi_full_circle_sel=v.findViewById(R.id.semi_full_circle_sel);
        semi_destination_right_full=v.findViewById(R.id.semi_destination_right_full);
        semi_destination_left_full=v.findViewById(R.id.semi_destination_left_full);
        rl_semi_circle=v.findViewById(R.id.rl_semi_circle);
        semi_right_circle_sel.setOnDragListener(this);
        semi_left_circle_sel.setOnDragListener(this);
        semi_full_circle_sel.setOnDragListener(this);

        rl_slice=v.findViewById(R.id.rl_slice);
        slice_destination=v.findViewById(R.id.slice_destination);
        slice_destination_full=v.findViewById(R.id.slice_destination_full);
        slice_destination.setOnDragListener(this);

        square_destination_right_full =v.findViewById(R.id.square_destination_right_full);
        square_destination_left_full =v.findViewById(R.id.square_destination_left_full);
        square_destination_left_top_full=v.findViewById(R.id.square_destination_left_top_full);
        square_destination_right_top_full=v.findViewById(R.id.square_destination_right_top_full);
        square_destination_left_bottom_full=v.findViewById(R.id.square_destination_left_bottom_full);
        square_destination_right_bottom_full=v.findViewById(R.id.square_destination_right_bottom_full);
        left_top_square_sel=v.findViewById(R.id.left_top_square_sel);
        right_top_square_sel=v.findViewById(R.id.right_top_square_sel);
        left_mid_square_sel=v.findViewById(R.id.left_mid_square_sel);
        full_mid_square_sel=v.findViewById(R.id.full_mid_square_sel);
        right_mid_square_sel=v.findViewById(R.id.right_mid_square_sel);
        left_bottom_square_sel=v.findViewById(R.id.left_bottom_square_sel);
        right_bottom_square_sel=v.findViewById(R.id.right_bottom_square_sel);
        rl_full_square =v.findViewById(R.id.rl_full_square);

        left_top_square_sel.setOnDragListener(this);
        right_top_square_sel.setOnDragListener(this);
        left_mid_square_sel.setOnDragListener(this);
        full_mid_square_sel.setOnDragListener(this);
        right_mid_square_sel.setOnDragListener(this);
        left_bottom_square_sel.setOnDragListener(this);
        right_bottom_square_sel.setOnDragListener(this);

        rl_full_circle =v.findViewById(R.id.rl_full_circle);
        tv_dragtopping =v.findViewById(R.id.tv_dragtopping);
        destination_right_full =v.findViewById(R.id.destination_right_full);
        destination_left_full =v.findViewById(R.id.destination_left_full);
        destination_full =v.findViewById(R.id.destination_full);
        destination_left_half_circle =v.findViewById(R.id.destination_left_half_circle);
        destination_right_half_circle =v.findViewById(R.id.destination_right_half_circle);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        //dialog.show();

        premium_price =v.findViewById(R.id.premium_price);
        destination =v.findViewById(R.id.destination);
        tv_price =v.findViewById(R.id.tv_price);
        iv_forward =v.findViewById(R.id.iv_forward);
        iv_forward.setOnClickListener(this);
        iv_backward =v.findViewById(R.id.iv_backward);
        iv_backward.setOnClickListener(this);

        toppingRecyclerView =v.findViewById(R.id.flavoursRecyclerview);
        premiumToppingRecyclerView =v.findViewById(R.id.premiumFlavoursREcyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        toppingRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        premiumToppingRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);

        showFlavors();

        getPremiumToppings();
        if (pizzaOrderInfoModel.getPizzaType() != null && !pizzaOrderInfoModel.getPizzaType().equals("")) {
            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                rl_full_circle.setVisibility(View.VISIBLE);
                if (pizzaOrderInfoModel.getToppingInfoList() != null) {
                    if (pizzaOrderInfoModel.getToppingInfoList().size() != 0) {
                        if (pizzaOrderInfoModel.getToppingInfoList().size() == 1) {
                            //orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(0).getPrice());
                            destination.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                            selectedFullCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(0);
                            tv_dragtopping.setVisibility(View.GONE);
                        } else if (pizzaOrderInfoModel.getToppingInfoList().size() == 2) {
                            for (int i = 0; i < pizzaOrderInfoModel.getToppingInfoList().size(); i++) {
                                //SelectedSidelines.add(sidelinesOrderList.get(i));

                                if (i == 0) {
                                    destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage())));
                                    //destination_left_full.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage());
                                    selectedLeftHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                } else {
                                    destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage())));
                                    // destination_right_full.setImageBitmap(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage());
                                    selectedRightHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                }
                                // orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(i).getPrice());
                            }
                            tv_dragtopping.setVisibility(View.GONE);

                        }
                    }
                }
            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {
                rl_full_square.setVisibility(View.VISIBLE);
                if (pizzaOrderInfoModel.getToppingInfoList() != null) {
                    if (pizzaOrderInfoModel.getToppingInfoList().size() != 0) {
                        if (pizzaOrderInfoModel.getToppingInfoList().size() == 1) {
                            //orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(0).getPrice());
                            square_destination.setImageBitmap(Common.cropToSquare(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                            selectedFullCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(0);
                            tv_dragtopping.setVisibility(View.GONE);
                        } else if (pizzaOrderInfoModel.getToppingInfoList().size() == 2) {
                            for (int i = 0; i < pizzaOrderInfoModel.getToppingInfoList().size(); i++) {
                                //SelectedSidelines.add(sidelinesOrderList.get(i));

                                if (i == 0) {
                                    square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage()));
                                    selectedLeftHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                } else {
                                    square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage()));
                                    selectedRightHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                }
                                // orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(i).getPrice());
                            }
                            tv_dragtopping.setVisibility(View.GONE);

                        } else if (pizzaOrderInfoModel.getToppingInfoList().size() == 3) {
                            for (int i = 0; i < pizzaOrderInfoModel.getToppingInfoList().size(); i++) {

                                Bitmap s = pizzaOrderInfoModel.getToppingInfoList().get(i).getImage();
                                if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftHalf() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftHalf().equals(Common.LeftHalf)) {
                                        square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage()));
                                        selectedLeftHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                } else if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightHalf() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightHalf().equals(Common.RightHalf)) {
                                        square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(pizzaOrderInfoModel.getToppingInfoList().get(i).getImage()));
                                        selectedRightHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                } else if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightTop() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightTop().equals(Common.RightTop)) {
                                        square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2));
                                        selectedTopRightModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                } else if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightBottom() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightBottom().equals(Common.RightBottom)) {
                                        square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                        selectedBottomRightModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                } else if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftBottom() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftBottom().equals(Common.LeftBottom)) {
                                        square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                        selectedBottomLeftModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                } else if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftTop() != null) {
                                    if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftTop().equals(Common.LeftTop)) {
                                        square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                                        selectedTopLeftModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                    }
                                }

                            }
                            tv_dragtopping.setVisibility(View.GONE);

                        } else if (pizzaOrderInfoModel.getToppingInfoList().size() == 4) {
                            for (int i = 0; i < pizzaOrderInfoModel.getToppingInfoList().size(); i++) {

                                Bitmap s = pizzaOrderInfoModel.getToppingInfoList().get(i).getImage();
                                if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightTop() != null) {
                                    if (i == 1) {
                                        if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightTop().equals(Common.RightTop)) {
                                            square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2));
                                            selectedTopRightModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                        }
                                    }
                                }
                                if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightBottom() != null) {
                                    if (i == 3) {
                                        if (pizzaOrderInfoModel.getToppingInfoList().get(i).getRightBottom().equals(Common.RightBottom)) {
                                            square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                            selectedBottomRightModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                        }
                                    }
                                }
                                if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftBottom() != null) {
                                    if (i == 2) {
                                        if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftBottom().equals(Common.LeftBottom)) {
                                            square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                            selectedBottomLeftModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                        }
                                    }
                                }
                                if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftTop() != null) {
                                    if (i == 0) {
                                        if (pizzaOrderInfoModel.getToppingInfoList().get(i).getLeftTop().equals(Common.LeftTop)) {
                                            square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                                            selectedTopLeftModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                        }
                                    }
                                }

                            }
                            tv_dragtopping.setVisibility(View.GONE);

                        }
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                rl_semi_circle.setVisibility(View.VISIBLE);
                if (pizzaOrderInfoModel.getToppingInfoList() != null) {
                    if (pizzaOrderInfoModel.getToppingInfoList().size() != 0) {
                        if (pizzaOrderInfoModel.getToppingInfoList().size() == 1) {
                            //orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(0).getPrice());
                           // semi_circle_destination.setImageBitmap(Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage()));
                            Bitmap selectedFull = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                            semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(selectedFull,0,selectedFull.getHeight()/2,selectedFull.getWidth(),selectedFull.getHeight()/2));;
                           // semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage(),0,pizzaOrderInfoModel.getToppingInfoList().get(0).getImage().getHeight()/2,pizzaOrderInfoModel.getToppingInfoList().get(0).getImage().getWidth(),pizzaOrderInfoModel.getToppingInfoList().get(0).getImage().getHeight()/2));

                            selectedFullCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(0);
                            tv_dragtopping.setVisibility(View.GONE);
                        } else if (pizzaOrderInfoModel.getToppingInfoList().size() == 2) {
                            for (int i = 0; i < pizzaOrderInfoModel.getToppingInfoList().size(); i++) {
                                //SelectedSidelines.add(sidelinesOrderList.get(i));

                                if (i == 0) {
                                    Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                                    semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s, 0, s.getHeight() / 2, s.getWidth() / 2, s.getHeight() / 2));
                                    selectedLeftHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                } else {

                                    Bitmap s1 = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(1).getImage());
                                    semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, s1.getHeight() / 2, s1.getWidth() / 2, s1.getHeight() / 2));  // for bottom right quater
                                    selectedRightHalfCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(i);
                                }
                                // orderPrice = orderPrice + Integer.parseInt(pizzaOrderInfoModel.getToppingInfoList().get(i).getPrice());
                            }
                            tv_dragtopping.setVisibility(View.GONE);

                        }
                    }
                }
            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                rl_slice.setVisibility(View.VISIBLE);
                tv_dragtopping.setVisibility(View.GONE);
                if (pizzaOrderInfoModel.getToppingInfoList() != null) {
                    if (pizzaOrderInfoModel.getToppingInfoList().size() != 0) {
                        if (pizzaOrderInfoModel.getToppingInfoList().size() == 1) {
                            Bitmap s = Common.getCircleBitmap(pizzaOrderInfoModel.getToppingInfoList().get(0).getImage());
                            slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s, s.getWidth() / 2, 0, s.getWidth() / 2, s.getHeight() / 2), -45));  // for top right quater  rotate bitmap
                            slice_destination.setRotation(0);
                            selectedFullCircleModel = pizzaOrderInfoModel.getToppingInfoList().get(0);

                        }
                    }
                }
            }
        }
        if (pizzaOrderInfoModel.getTotalPrice() != null && !pizzaOrderInfoModel.getTotalPrice().equals("")){
            tmpPrice = Integer.parseInt(pizzaOrderInfoModel.getTotalPrice());
            tv_price.setText(convertToString(tmpPrice));
        }
        destination_left_half_circle.setOnDragListener(this);
        destination_right_half_circle.setOnDragListener(this);
        destination_full.setOnDragListener(this);

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            Log.i("POOT", "onClick: TOPP");
            setNextPhoneFragment();
        }else if (v == iv_backward) {
            Log.i("POOT", "onClick: Topp back");
            ((MainActivity)context).onBackPressed();
            orderPrice = 0;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<String> str) {
        String type = "";
        String clipData = "";
        if (str.size()==2){
            type = str.get(0);
            clipData = str.get(1);
        }
        if (type.equals("free")) {
            rl_full_half_circle.setVisibility(View.GONE);
            rl_semi_textviews.setVisibility(View.GONE);
            rl_square_textviews.setVisibility(View.GONE);
            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));

                for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {
                            setSelectedImage(pizzaToppingInfo.getImage(), destination, Common.FULL_CIRCLE);
                        } catch (Exception e) {

                        }

                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }
                        tv_price.setText(convertToString(orderPrice + tmpPrice));

                        selectedFullCircleModel = pizzaToppingInfo;
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();
                        freeToppingsAdapter.notifyDataSetChanged();

                        //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                        destination_left_full.setVisibility(View.INVISIBLE);
                        destination_right_full.setVisibility(View.INVISIBLE);
                        destination_full.setVisibility(View.INVISIBLE);
                        destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle));
                        destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle));
                        destination_left_half_circle.setVisibility(View.INVISIBLE);
                        destination_right_half_circle.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                full_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));

                for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {
                            setSelectedImage(pizzaToppingInfo.getImage(), square_destination, Common.FullSquare);
                        } catch (Exception e) {
                        }

                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                            }
                        }
                        tv_price.setText(convertToString(orderPrice + tmpPrice));

                        selectedFullCircleModel = pizzaToppingInfo;
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();
                        selectedBottomLeftModel = null;
                        selectedBottomLeftModel = new PizzaToppingInfo();
                        selectedBottomRightModel = null;
                        selectedBottomRightModel = new PizzaToppingInfo();
                        selectedTopLeftModel = null;
                        selectedTopLeftModel = new PizzaToppingInfo();
                        selectedTopRightModel = null;
                        selectedTopRightModel = new PizzaToppingInfo();
                        freeToppingsAdapter.notifyDataSetChanged();

                        square_destination_left_full.setVisibility(View.INVISIBLE);
                        square_destination_right_full.setVisibility(View.INVISIBLE);
                        square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                        square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                        square_destination_left_top_full.setVisibility(View.INVISIBLE);
                        square_destination_right_top_full.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));

                for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {

                            setSelectedImage(pizzaToppingInfo.getImage(), semi_circle_destination, Common.FULL_CIRCLE);

                        } catch (Exception e) {
                        }

                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }
                        tv_price.setText(convertToString(orderPrice + tmpPrice));

                        selectedFullCircleModel = pizzaToppingInfo;
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();
                        freeToppingsAdapter.notifyDataSetChanged();

                        //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                        semi_destination_left_full.setVisibility(View.INVISIBLE);
                        semi_destination_right_full.setVisibility(View.INVISIBLE);
                        semi_destination_full.setVisibility(View.INVISIBLE);
                        semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle));
                        semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle));
                        semi_left_circle_sel.setVisibility(View.INVISIBLE);
                        semi_right_circle_sel.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                slice_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_slice_simple));
                for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {
                        try {
                            setSelectedImage(pizzaToppingInfo.getImage(), slice_destination, Common.FULL_CIRCLE);
                        } catch (Exception e) {
                        }

                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        selectedFullCircleModel = pizzaToppingInfo;
                        tv_price.setText(convertToString(orderPrice + tmpPrice));

                        freeToppingsAdapter.notifyDataSetChanged();
                        slice_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_slice_simple));

                    }
                }
            }
        }else if (type.equals("premium")){


            rl_full_half_circle.setVisibility(View.GONE);
            rl_semi_textviews.setVisibility(View.GONE);
            rl_square_textviews.setVisibility(View.GONE);
            if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));

                for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {
                            setSelectedImage(pizzaToppingInfo.getImage(), destination, Common.FULL_CIRCLE);
                        } catch (Exception e) {

                        }
                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }
                        selectedFullCircleModel = pizzaToppingInfo;
                        orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                        tv_price.setText(convertToString(orderPrice + tmpPrice));
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();
                        adapter.notifyDataSetChanged();


                        //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                        destination_left_full.setVisibility(View.INVISIBLE);
                        destination_right_full.setVisibility(View.INVISIBLE);
                        destination_full.setVisibility(View.INVISIBLE);
                        destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle));
                        destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle));
                        destination_left_half_circle.setVisibility(View.INVISIBLE);
                        destination_right_half_circle.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                full_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));
                for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {
                            setSelectedImage(pizzaToppingInfo.getImage(), square_destination, Common.FullSquare);
                        } catch (Exception e) {
                        }
                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                            }
                        }
                        selectedFullCircleModel = pizzaToppingInfo;
                        orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                        tv_price.setText(convertToString(orderPrice + tmpPrice));
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();
                        selectedBottomLeftModel = null;
                        selectedBottomLeftModel = new PizzaToppingInfo();
                        selectedBottomRightModel = null;
                        selectedBottomRightModel = new PizzaToppingInfo();
                        selectedTopLeftModel = null;
                        selectedTopLeftModel = new PizzaToppingInfo();
                        selectedTopRightModel = null;
                        selectedTopRightModel = new PizzaToppingInfo();


                        adapter.notifyDataSetChanged();
                        square_destination_left_full.setVisibility(View.INVISIBLE);
                        square_destination_right_full.setVisibility(View.INVISIBLE);
                        square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                        square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                        square_destination_left_top_full.setVisibility(View.INVISIBLE);
                        square_destination_right_top_full.setVisibility(View.INVISIBLE);

                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));

                for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {

                            setSelectedImage(pizzaToppingInfo.getImage(), semi_circle_destination, Common.FULL_CIRCLE);

                        } catch (Exception e) {
                        }
                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        if (orderPrice != 0) {
                            if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                            }
                        }
                        if (orderPrice != 0) {
                            if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                            }
                        }
                        selectedFullCircleModel = pizzaToppingInfo;
                        orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                        tv_price.setText(convertToString(orderPrice + tmpPrice));
                        selectedRightHalfCircleModel = null;
                        selectedRightHalfCircleModel = new PizzaToppingInfo();
                        selectedLeftHalfCircleModel = null;
                        selectedLeftHalfCircleModel = new PizzaToppingInfo();

                        adapter.notifyDataSetChanged();

                        //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                        semi_destination_left_full.setVisibility(View.INVISIBLE);
                        semi_destination_right_full.setVisibility(View.INVISIBLE);
                        semi_destination_full.setVisibility(View.INVISIBLE);
                        semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle));
                        semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle));
                        semi_left_circle_sel.setVisibility(View.INVISIBLE);
                        semi_right_circle_sel.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                slice_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_slice_simple));
                for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                    if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                        try {

                            setSelectedImage(pizzaToppingInfo.getImage(), slice_destination, Common.FULL_CIRCLE);

                        } catch (Exception e) {
                        }
                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                            if (orderPrice != 0) {
                                orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                            }
                        }

                        selectedFullCircleModel = pizzaToppingInfo;
                        orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                        tv_price.setText(convertToString(orderPrice + tmpPrice));

                        adapter.notifyDataSetChanged();
                        slice_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_slice_simple));

                    }
                }
            }


        }

    }

    private void showFlavors() {

        freeToppings = new ArrayList<>();
        pizzaToppings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                freeToppings.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaToppingInfo chat = snapshot.getValue(PizzaToppingInfo.class);
                    if (chat.getPrice().equals("")){
                        freeToppings.add(chat);
                        try {
                            URL url = new URL(chat.getImageURL());
                            GetBitmapTaskFree getBitmapTask = new GetBitmapTaskFree(context, url,freeToppings.size() -1 );
                            getBitmapTask.execute();
                        }catch (Exception e){}
                        pri = chat.getPrice();
                    }
                }

                freeToppingsAdapter = new FreeToppingsAdapter(freeToppings , context);
                toppingRecyclerView.setAdapter(freeToppingsAdapter);
                Log.d("TAG", "onDataChange: drinks List :"+ Utils.getGsonParser().toJson(freeToppings));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPremiumToppings() {

        premiumToppings = new ArrayList<>();
        pizzaToppings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                premiumToppings.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PizzaToppingInfo chat = snapshot.getValue(PizzaToppingInfo.class);
                    if (!chat.getPrice().equals("")){
                        premiumToppings.add(chat);
                        try {
                            URL url = new URL(chat.getImageURL());
                            GetBitmapTask getBitmapTask = new GetBitmapTask(context, url,premiumToppings.size() -1 );
                            getBitmapTask.execute();
                        }catch (Exception e){}
                        pri = chat.getPrice();
                    }
                    /*messageAdapter = new MessageAdapter(context, coldrinksList);
                    recyclerView.setAdapter(messageAdapter);*/
                }

                premium_price.setText("Rs."+pri);
                adapter = new PremiumToppingsAdapter(premiumToppings , context);
                premiumToppingRecyclerView.setAdapter(adapter);
                Log.d("TAG", "onDataChange: drinks List :"+ Utils.getGsonParser().toJson(premiumToppings));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNextPhoneFragment() {
        pizzaOrderInfoModel.setTotalPrice(tmpPrice+orderPrice+"");
        if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
            if (selectedFull == null) {
                if (selectedLeftHalf != null && selectedRightHalf != null) {


                    selectedRightHalfCircleModel.setImage(selectedRightHalf);
                    selectedLeftHalfCircleModel.setImage(selectedLeftHalf);
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    tmp.add(selectedLeftHalfCircleModel);
                    tmp.add(selectedRightHalfCircleModel);
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                } else {
                    Toast.makeText(context, "Please Select Toppings", Toast.LENGTH_SHORT).show();

                }
            } else {
                selectedFullCircleModel.setImage(selectedFull);
                List<PizzaToppingInfo> tmp = new ArrayList<>();
                tmp.add(selectedFullCircleModel);
                pizzaOrderInfoModel.setToppingInfoList(tmp);
                showFrag();
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

            if (selectedFull == null) {
                if (selectedLeftHalf != null && selectedRightHalf != null) {
                    selectedRightHalfCircleModel.setImage(selectedRightHalf);
                    selectedRightHalfCircleModel.setRightHalf(Common.RightHalf);
                    selectedLeftHalfCircleModel.setImage(selectedLeftHalf);
                    selectedLeftHalfCircleModel.setLeftHalf(Common.LeftHalf);
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    tmp.add(selectedLeftHalfCircleModel);
                    tmp.add(selectedRightHalfCircleModel);
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                } else if (selectedTopLeft!=null && selectedTopRight!=null && selectedBottomLeft!=null && selectedBottomRight!=null){
                    selectedTopLeftModel.setImage(selectedTopLeft);
                    selectedTopLeftModel.setLeftTop(Common.LeftTop);
                    selectedTopRightModel.setImage(selectedTopRight);
                    selectedTopRightModel.setRightTop(Common.RightTop);
                    selectedBottomLeftModel.setImage(selectedBottomLeft);
                    selectedBottomLeftModel.setLeftBottom(Common.LeftBottom);
                    selectedBottomRightModel.setImage(selectedBottomRight);
                    selectedBottomRightModel.setRightBottom(Common.RightBottom);
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    tmp.add(selectedTopLeftModel);
                    tmp.add(selectedTopRightModel);
                    tmp.add(selectedBottomLeftModel);
                    tmp.add(selectedBottomRightModel);
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                    Toast.makeText(context, "Please Select Toppings", Toast.LENGTH_SHORT).show();
                }else if (selectedLeftHalf != null){
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    selectedLeftHalfCircleModel.setLeftHalf(Common.LeftHalf);
                    selectedLeftHalfCircleModel.setImage(selectedLeftHalf);
                    tmp.add(selectedLeftHalfCircleModel);
                    if (selectedTopRight!=null && selectedBottomRight!=null){
                        selectedTopRightModel.setImage(selectedTopRight);
                        selectedTopRightModel.setRightTop(Common.RightTop);
                        selectedBottomRightModel.setImage(selectedBottomRight);
                        selectedBottomRightModel.setRightBottom(Common.RightBottom);
                        tmp.add(selectedTopRightModel);
                        tmp.add(selectedBottomRightModel);

                    }
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                }else if (selectedRightHalf != null){
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    selectedRightHalfCircleModel.setImage(selectedRightHalf);
                    selectedRightHalfCircleModel.setRightHalf(Common.RightHalf);
                    if (selectedTopLeft!=null && selectedBottomLeft!=null){
                        selectedTopLeftModel.setImage(selectedTopLeft);
                        selectedTopLeftModel.setLeftTop(Common.LeftTop);
                        selectedBottomLeftModel.setImage(selectedBottomLeft);
                        selectedBottomLeftModel.setLeftBottom(Common.LeftBottom);
                        tmp.add(selectedTopLeftModel);
                        tmp.add(selectedBottomLeftModel);
                    }
                    tmp.add(selectedRightHalfCircleModel);
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                }else {
                    Log.i("PRTAG", "setNextPhoneFragment: "+pizzaOrderInfoModel.getTotalPrice());
                }
            } else {
                selectedFullCircleModel.setImage(selectedFull);
                List<PizzaToppingInfo> tmp = new ArrayList<>();
                tmp.add(selectedFullCircleModel);
                pizzaOrderInfoModel.setToppingInfoList(tmp);
                showFrag();
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
            if (selectedFull == null) {
                if (selectedLeftHalf != null && selectedRightHalf != null) {

                    selectedRightHalfCircleModel.setImage(selectedRightHalf);
                    selectedLeftHalfCircleModel.setImage(selectedLeftHalf);
                    List<PizzaToppingInfo> tmp = new ArrayList<>();
                    tmp.add(selectedLeftHalfCircleModel);
                    tmp.add(selectedRightHalfCircleModel);
                    pizzaOrderInfoModel.setToppingInfoList(tmp);
                    showFrag();
                } else {
                    Toast.makeText(context, "Please Select Toppings", Toast.LENGTH_SHORT).show();

                }
            } else {
                selectedFullCircleModel.setImage(selectedFull);
                List<PizzaToppingInfo> tmp = new ArrayList<>();
                tmp.add(selectedFullCircleModel);
                pizzaOrderInfoModel.setToppingInfoList(tmp);
                showFrag();
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
            if (selectedFull == null) {
                Toast.makeText(context, "Please Select Toppings", Toast.LENGTH_SHORT).show();

            } else {
                selectedFullCircleModel.setImage(selectedFull);
                List<PizzaToppingInfo> tmp = new ArrayList<>();
                tmp.add(selectedFullCircleModel);
                pizzaOrderInfoModel.setToppingInfoList(tmp);
                showFrag();
            }
        }


        Log.i("PRTAG", "setNextPhoneFragment: "+pizzaOrderInfoModel.getTotalPrice());

    }

    private void showFrag() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectedSidelinesFragment hf = (SelectedSidelinesFragment) fm.findFragmentByTag("PizzaToppingVegetablesFrag");
            if (hf == null) {
                hf = new SelectedSidelinesFragment();
                ft.replace(R.id.main_frame, hf, "PizzaToppingVegetablesFrag");
                ft.addToBackStack("PizzaToppingVegetablesFrag");
            } else{
                ft.replace(R.id.main_frame, hf, "PizzaToppingVegetablesFrag");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            ft.commit();

        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }

        cnt = 0;
        cnts = 0;
        orderPrice = 0;
        selectedRightHalfCircleModel = null;
        selectedRightHalfCircleModel = new PizzaToppingInfo();
        selectedLeftHalfCircleModel = null;
        selectedLeftHalfCircleModel = new PizzaToppingInfo();
        selectedFullCircleModel = null;
        selectedFullCircleModel = new PizzaToppingInfo();
        selectedTopRightModel = null;
        selectedTopRightModel = new PizzaToppingInfo();
        selectedTopLeftModel = null;
        selectedTopLeftModel = new PizzaToppingInfo();
        selectedBottomLeftModel = null;
        selectedBottomLeftModel = new PizzaToppingInfo();
        selectedBottomRightModel = null;
        selectedBottomRightModel = new PizzaToppingInfo();
    }

    public String convertToString(int price){
        String tmp = "Rs."+price;
        return tmp;
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        try {


            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    //((ImageView) v).setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                    if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                        destination.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                        //destination.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_circle_background));
                        destination_right_full.setVisibility(View.GONE);
                        destination_left_full.setVisibility(View.GONE);
                        destination.setVisibility(View.VISIBLE);
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {
                        square_destination.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square_background));
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                        semi_circle_destination.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_background));
                        semi_destination_right_full.setVisibility(View.GONE);
                        semi_destination_left_full.setVisibility(View.GONE);
                        semi_circle_destination.setVisibility(View.VISIBLE);
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                        slice_destination.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                    }
                    tv_dragtopping.setVisibility(View.GONE);
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    String clipData = event.getClipDescription().getLabel().toString();

                    if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                        rl_full_half_circle.setVisibility(View.VISIBLE);
                        //destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up_red));
                        if (v.getId() == R.id.destination_full) {
                           destination_full.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                            destination_left_half_circle.setVisibility(View.INVISIBLE);
                            destination_right_half_circle.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.destination_left_half_circle) {
                            destination_left_half_circle.setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            destination_full.setVisibility(View.VISIBLE);
                            destination_right_half_circle.setVisibility(View.VISIBLE);
                            destination_right_half_circle.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                        } else if (v.getId() == R.id.destination_right_half_circle) {
                            destination_right_half_circle.setColorFilter(ContextCompat.getColor(context, R.color.light_grey)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            destination_full.setVisibility(View.VISIBLE);
                            destination_left_half_circle.setVisibility(View.VISIBLE);
                            destination_left_half_circle.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));
                        }
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {
                        rl_square_textviews.setVisibility(View.VISIBLE);
                        if (v.getId() == R.id.full_mid_square_sel) {
                            // destination.setColorFilter(ContextCompat.getColor(context, R.color.red)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            square_destination.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.left_top_square_sel) {
                            square_destination_left_top_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.right_top_square_sel) {
                            square_destination_right_top_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.left_bottom_square_sel) {
                            square_destination_left_bottom_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.right_bottom_square_sel) {
                            square_destination_right_bottom_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.left_mid_square_sel) {
                            square_destination_left_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                            right_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.right_mid_square_sel) {
                            square_destination_right_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));//setColorFilter(ContextCompat.getColor(context, R.color.yellowColor)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            full_mid_square_sel.setVisibility(View.INVISIBLE);
                            left_top_square_sel.setVisibility(View.INVISIBLE);
                            right_bottom_square_sel.setVisibility(View.INVISIBLE);
                            left_mid_square_sel.setVisibility(View.INVISIBLE);
                            right_top_square_sel.setVisibility(View.INVISIBLE);
                            left_bottom_square_sel.setVisibility(View.INVISIBLE);
                        }

                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {

                        rl_semi_textviews.setVisibility(View.VISIBLE);
                        semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle));
                        if (v.getId() == R.id.semi_full_circle_sel) {
                            // destination.setColorFilter(ContextCompat.getColor(context, R.color.red)/*, android.graphics.PorterDuff.Mode.MULTIPLY*/);
                            semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle));
                            semi_left_circle_sel.setVisibility(View.INVISIBLE);
                            semi_right_circle_sel.setVisibility(View.INVISIBLE);
                        } else if (v.getId() == R.id.semi_left_circle_sel) {
                            semi_destination_left_full.setBackground(context.getResources().getDrawable(R.drawable.quarter_left_circle));/*, android.graphics.PorterDuff.Mode.MULTIPLY*/
                            semi_full_circle_sel.setVisibility(View.VISIBLE);
                            semi_right_circle_sel.setVisibility(View.VISIBLE);
                        } else if (v.getId() == R.id.semi_right_circle_sel) {
                            semi_destination_right_full.setBackground(context.getResources().getDrawable(R.drawable.quarter_right_circle));/*, android.graphics.PorterDuff.Mode.MULTIPLY*/
                            semi_full_circle_sel.setVisibility(View.VISIBLE);
                            semi_left_circle_sel.setVisibility(View.VISIBLE);
                        }

                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                        //slice_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_slice));
                        slice_destination.setColorFilter(ContextCompat.getColor(context, R.color.black_transparent));

                    }

                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.invalidate();
                    rl_full_half_circle.setVisibility(View.GONE);
                    rl_semi_textviews.setVisibility(View.GONE);
                    rl_square_textviews.setVisibility(View.GONE);
                    ((ImageView) v).clearColorFilter();
                    return true;

                case DragEvent.ACTION_DROP:

                    rl_full_half_circle.setVisibility(View.GONE);
                    rl_semi_textviews.setVisibility(View.GONE);
                    rl_square_textviews.setVisibility(View.GONE);
                    clipData = event.getClipDescription().getLabel().toString();
                    if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                        destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));
                        if (v.getId() == R.id.destination_full) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), destination, Common.FULL_CIRCLE);
                                    } catch (Exception e) {

                                    }
                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                                    destination_left_full.setVisibility(View.INVISIBLE);
                                    destination_right_full.setVisibility(View.INVISIBLE);
                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle));
                                    destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle));
                                    destination_left_half_circle.setVisibility(View.INVISIBLE);
                                    destination_right_half_circle.setVisibility(View.INVISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), destination, Common.FULL_CIRCLE);
                                    } catch (Exception e) {

                                    }

                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                                    destination_left_full.setVisibility(View.INVISIBLE);
                                    destination_right_full.setVisibility(View.INVISIBLE);
                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle));
                                    destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle));
                                    destination_left_half_circle.setVisibility(View.INVISIBLE);
                                    destination_right_half_circle.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else if (v.getId() == R.id.destination_left_half_circle) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {


                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), destination_left_full, Common.Left_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }


                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());


                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;

                                    orderPrice += Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    adapter.notifyDataSetChanged();

                                    selectedFullCircleModel = null;
                                    destination_left_full.setVisibility(View.VISIBLE);
                                    destination_right_full.setVisibility(View.VISIBLE);
                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination.setVisibility(View.INVISIBLE);
                                    destination_right_half_circle.setVisibility(View.VISIBLE);
                                    destination_left_half_circle.setVisibility(View.VISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {


                                    try {

                                        setSelectedImage(pizzaToppingInfo.getImage(), destination_left_full, Common.Left_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }

                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());


                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    destination_left_full.setVisibility(View.VISIBLE);
                                    destination_right_full.setVisibility(View.VISIBLE);
                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination.setVisibility(View.INVISIBLE);
                                    destination_right_half_circle.setVisibility(View.VISIBLE);
                                    destination_left_half_circle.setVisibility(View.VISIBLE);
                                }
                            }
                        } else if (v.getId() == R.id.destination_right_half_circle) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), destination_right_full, Common.Right_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }
                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;

                                    orderPrice += Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    adapter.notifyDataSetChanged();
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_right_half_circle);
                                    destination_right_full.setVisibility(View.VISIBLE);
                                    destination_left_full.setVisibility(View.VISIBLE);

                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination.setVisibility(View.INVISIBLE);
                                    //destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_circle));
                                    destination_right_half_circle.setVisibility(View.VISIBLE);
                                    destination_left_half_circle.setVisibility(View.VISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {

                                        setSelectedImage(pizzaToppingInfo.getImage(), destination_right_full, Common.Right_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }

                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;
                                    freeToppingsAdapter.notifyDataSetChanged();
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_right_half_circle);
                                    destination_right_full.setVisibility(View.VISIBLE);
                                    destination_left_full.setVisibility(View.VISIBLE);

                                    destination_full.setVisibility(View.INVISIBLE);
                                    destination.setVisibility(View.INVISIBLE);
                                    //destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_circle));
                                    destination_right_half_circle.setVisibility(View.VISIBLE);
                                    destination_left_half_circle.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                        full_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_full_square));
                        if (v.getId() == R.id.full_mid_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination, Common.FullSquare);
                                    } catch (Exception e) {
                                    }
                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);

                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination, Common.FullSquare);
                                    } catch (Exception e) {
                                    }

                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else if (v.getId() == R.id.left_mid_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_full, Common.LeftHalf);
                                    } catch (Exception e) {
                                    }
                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.VISIBLE);
                                    square_destination_right_full.setVisibility(View.VISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);


                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_full, Common.LeftHalf);
                                    } catch (Exception e) {
                                    }

                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.VISIBLE);
                                    square_destination_right_full.setVisibility(View.VISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);


                                }
                            }
                        } else if (v.getId() == R.id.right_mid_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_full, Common.RightHalf);
                                    } catch (Exception e) {
                                    }
                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.VISIBLE);
                                    square_destination_right_full.setVisibility(View.VISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);


                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_full, Common.RightHalf);
                                    } catch (Exception e) {
                                    }

                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedBottomLeftModel = null;
                                    selectedBottomLeftModel = new PizzaToppingInfo();
                                    selectedBottomRightModel = null;
                                    selectedBottomRightModel = new PizzaToppingInfo();
                                    selectedTopLeftModel = null;
                                    selectedTopLeftModel = new PizzaToppingInfo();
                                    selectedTopRightModel = null;
                                    selectedTopRightModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.VISIBLE);
                                    square_destination_right_full.setVisibility(View.VISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_top_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_top_full.setVisibility(View.INVISIBLE);


                                }
                            }
                        } else if (v.getId() == R.id.left_top_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_top_full, Common.LeftTop);
                                    } catch (Exception e) {
                                    }
                                    if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedTopLeftModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedTopLeftModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_top_full, Common.LeftTop);
                                    } catch (Exception e) {
                                    }

                                    if (selectedTopLeftModel.getPrice() != null && !selectedTopLeftModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedTopLeftModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedTopLeftModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                        } else if (v.getId() == R.id.right_top_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_top_full, Common.RightTop);
                                    } catch (Exception e) {
                                    }
                                    if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedTopRightModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedTopRightModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_top_full, Common.RightTop);
                                    } catch (Exception e) {
                                    }

                                    if (selectedTopRightModel.getPrice() != null && !selectedTopRightModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedTopRightModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedTopRightModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                        } else if (v.getId() == R.id.left_bottom_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_bottom_full, Common.LeftBottom);
                                    } catch (Exception e) {
                                    }
                                    if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedBottomLeftModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedBottomLeftModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_left_bottom_full, Common.LeftBottom);
                                    } catch (Exception e) {
                                    }

                                    if (selectedBottomLeftModel.getPrice() != null && !selectedBottomLeftModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedBottomLeftModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }
                                    selectedBottomLeftModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);


                                }
                            }
                        } else if (v.getId() == R.id.right_bottom_square_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_bottom_full, Common.RightBottom);
                                    } catch (Exception e) {
                                    }
                                    if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedBottomRightModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedBottomRightModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);

                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), square_destination_right_bottom_full, Common.RightBottom);
                                    } catch (Exception e) {
                                    }

                                    if (selectedBottomRightModel.getPrice() != null && !selectedBottomRightModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedBottomRightModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {

                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    selectedBottomRightModel = pizzaToppingInfo;
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    square_destination_left_full.setVisibility(View.INVISIBLE);
                                    square_destination_right_full.setVisibility(View.INVISIBLE);
                                    square_destination_left_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_right_bottom_full.setVisibility(View.VISIBLE);
                                    square_destination_left_top_full.setVisibility(View.VISIBLE);
                                    square_destination_right_top_full.setVisibility(View.VISIBLE);

                                }
                            }
                        }
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                        semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));
                        if (v.getId() == R.id.semi_full_circle_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {

                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_circle_destination, Common.FULL_CIRCLE);

                                    } catch (Exception e) {
                                    }
                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    adapter.notifyDataSetChanged();

                                    //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                                    semi_destination_left_full.setVisibility(View.INVISIBLE);
                                    semi_destination_right_full.setVisibility(View.INVISIBLE);
                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle));
                                    semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle));
                                    semi_left_circle_sel.setVisibility(View.INVISIBLE);
                                    semi_right_circle_sel.setVisibility(View.INVISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {

                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_circle_destination, Common.FULL_CIRCLE);

                                    } catch (Exception e) {
                                    }

                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }
                                    selectedFullCircleModel = pizzaToppingInfo;
                                    selectedRightHalfCircleModel = null;
                                    selectedRightHalfCircleModel = new PizzaToppingInfo();
                                    selectedLeftHalfCircleModel = null;
                                    selectedLeftHalfCircleModel = new PizzaToppingInfo();
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    //Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination);
                                    semi_destination_left_full.setVisibility(View.INVISIBLE);
                                    semi_destination_right_full.setVisibility(View.INVISIBLE);
                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_left_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_left_circle));
                                    semi_right_circle_sel.setImageDrawable(context.getResources().getDrawable(R.drawable.quarter_right_circle));
                                    semi_left_circle_sel.setVisibility(View.INVISIBLE);
                                    semi_right_circle_sel.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else if (v.getId() == R.id.semi_left_circle_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {
                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_destination_left_full, Common.Left_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }

                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;

                                    orderPrice += Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    adapter.notifyDataSetChanged();

                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_left_half_circle);
                                    semi_circle_destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_circle));

                                    semi_destination_left_full.setVisibility(View.VISIBLE);
                                    semi_destination_right_full.setVisibility(View.VISIBLE);
                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setVisibility(View.INVISIBLE);
                                    semi_right_circle_sel.setVisibility(View.VISIBLE);
                                    semi_left_circle_sel.setVisibility(View.VISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {
                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_destination_left_full, Common.Left_HALF_CIRCLE);

                                    } catch (Exception e) {

                                    }

                                    if (selectedLeftHalfCircleModel.getPrice() != null && !selectedLeftHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedLeftHalfCircleModel.getPrice());
                                        }
                                    }
                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedLeftHalfCircleModel = pizzaToppingInfo;

                                    freeToppingsAdapter.notifyDataSetChanged();

                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_left_half_circle);
                                    semi_circle_destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_circle));

                                    semi_destination_left_full.setVisibility(View.VISIBLE);
                                    semi_destination_right_full.setVisibility(View.VISIBLE);
                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setVisibility(View.INVISIBLE);
                                    semi_right_circle_sel.setVisibility(View.VISIBLE);
                                    semi_left_circle_sel.setVisibility(View.VISIBLE);
                                }
                            }
                        } else if (v.getId() == R.id.semi_right_circle_sel) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_destination_right_full, Common.Right_HALF_CIRCLE);

                                    } catch (Exception e) {

                                    }
                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;

                                    orderPrice += Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    adapter.notifyDataSetChanged();
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_right_half_circle);
                                    semi_destination_right_full.setVisibility(View.VISIBLE);
                                    semi_destination_left_full.setVisibility(View.VISIBLE);

                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_circle));

                                    //destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_circle));
                                    semi_right_circle_sel.setVisibility(View.VISIBLE);
                                    semi_left_circle_sel.setVisibility(View.VISIBLE);
                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), semi_destination_right_full, Common.Right_HALF_CIRCLE);
                                    } catch (Exception e) {

                                    }

                                    if (selectedRightHalfCircleModel.getPrice() != null && !selectedRightHalfCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedRightHalfCircleModel.getPrice());
                                        }
                                    }

                                    if (orderPrice != 0) {
                                        if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());
                                        }
                                    }
                                    selectedRightHalfCircleModel = pizzaToppingInfo;
                                    freeToppingsAdapter.notifyDataSetChanged();
                                    selectedFullCircleModel = null;
                                    selectedFullCircleModel = new PizzaToppingInfo();
//                            Picasso.get().load(pizzaToppingInfo.getImageURL()).into(destination_right_half_circle);
                                    semi_destination_right_full.setVisibility(View.VISIBLE);
                                    semi_destination_left_full.setVisibility(View.VISIBLE);

                                    semi_destination_full.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setVisibility(View.INVISIBLE);
                                    semi_circle_destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_half_circle));

                                    //destination.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_full_circle));
                                    semi_right_circle_sel.setVisibility(View.VISIBLE);
                                    semi_left_circle_sel.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                        slice_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_slice_simple));
                        if (v.getId() == R.id.slice_destination) {
                            for (PizzaToppingInfo pizzaToppingInfo : premiumToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {

                                        setSelectedImage(pizzaToppingInfo.getImage(), slice_destination, Common.FULL_CIRCLE);

                                    } catch (Exception e) {
                                    }
                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    selectedFullCircleModel = pizzaToppingInfo;
                                    orderPrice += Integer.parseInt(selectedFullCircleModel.getPrice());
                                    tv_price.setText(convertToString(orderPrice + tmpPrice));
                                    adapter.notifyDataSetChanged();

                                    slice_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_slice_simple));

                                }
                            }
                            for (PizzaToppingInfo pizzaToppingInfo : freeToppings) {
                                if (pizzaToppingInfo.getItemName().equalsIgnoreCase(clipData)) {

                                    try {
                                        setSelectedImage(pizzaToppingInfo.getImage(), slice_destination, Common.FULL_CIRCLE);
                                    } catch (Exception e) {
                                    }

                                    if (selectedFullCircleModel.getPrice() != null && !selectedFullCircleModel.getPrice().equals("")) {
                                        if (orderPrice != 0) {
                                            orderPrice -= Integer.parseInt(selectedFullCircleModel.getPrice());

                                        }
                                    }

                                    selectedFullCircleModel = pizzaToppingInfo;
                                    freeToppingsAdapter.notifyDataSetChanged();

                                    slice_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_slice_simple));

                                }
                            }
                        }
                    }

                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
                        destination_full.setVisibility(View.VISIBLE);
                        // destination.setVisibility(View.VISIBLE);
                        destination_right_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_right_half_circle_background));
                        destination_left_half_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_left_half_circle_background));

                        destination_right_half_circle.setVisibility(View.VISIBLE);
                        destination_left_half_circle.setVisibility(View.VISIBLE);
                        destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle_up));
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                        semi_destination_full.setVisibility(View.VISIBLE);
                        // destination.setVisibility(View.VISIBLE);
                        semi_right_circle_sel.setVisibility(View.VISIBLE);
                        semi_left_circle_sel.setVisibility(View.VISIBLE);
                        semi_destination_full.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_circle));
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {

                        full_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        left_top_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        right_top_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        left_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        right_mid_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        left_bottom_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));
                        right_bottom_square_sel.setBackground(context.getResources().getDrawable(R.drawable.pizza_half_square_simple));

                        full_mid_square_sel.setVisibility(View.VISIBLE);
                        left_top_square_sel.setVisibility(View.VISIBLE);
                        right_top_square_sel.setVisibility(View.VISIBLE);
                        left_mid_square_sel.setVisibility(View.VISIBLE);
                        right_mid_square_sel.setVisibility(View.VISIBLE);
                        left_bottom_square_sel.setVisibility(View.VISIBLE);
                        right_bottom_square_sel.setVisibility(View.VISIBLE);
                    } else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                        // slice_destination_full.setImageDrawable(context.getResources().getDrawable(R.drawable.pizza_slice_simple));
                        slice_destination.setVisibility(View.VISIBLE);
                    }
                    ((ImageView) v).clearColorFilter();
                    if (event.getResult()) {
                        //Toast.makeText(context, "Awesome!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(context, "Aw Snap! Try dropping it again", Toast.LENGTH_SHORT).show();
                    }
                    return true;

                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public class GetBitmapTask extends AsyncTask<Bitmap, Void, Bitmap> {
        URL Url;
        Context context;
        int position;

        public GetBitmapTask(Context context, URL Url, int position) {
            this.context = context;
            this.Url = Url;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Address", "ADDRESS: ");

            if (cnt == 0) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Bitmap... strings) {
            Bitmap bitmap1 = null;
            try {
                bitmap1 = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
            } catch (IOException e) {

            }
            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            if (s != null) {
                premiumToppings.get(position).setImage(s);
                Log.i("Address", "ADDED IMAGE: ");
               cnt++;
               if (cnt == premiumToppings.size()) {
                   if (dialog.isShowing()) {
                       dialog.dismiss();
                   }
               }
            } else {
                Log.i("TTAG", " In get AddressTask ..... onPostExecute: ");
            }
        }

    }

    public class GetBitmapTaskFree extends AsyncTask<Bitmap, Void, Bitmap> {
        URL Url;
        Context context;
        int position;

        public GetBitmapTaskFree(Context context, URL Url, int position) {
            this.context = context;
            this.Url = Url;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Address", "ADDRESS: ");

            if (cnts == 0) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Bitmap... strings) {
            Bitmap bitmap1 = null;
            try {
              bitmap1 = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
            } catch (IOException e) {

            }
            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            if (s != null) {
                freeToppings.get(position).setImage(s);
                Log.i("Address", "ADDED IMAGE: ");
               cnts++;
               if (cnts == freeToppings.size()) {
                   if (dialog.isShowing()) {
                       dialog.dismiss();
                   }
               }
            } else {
                Log.i("TTAG", " In get AddressTask ..... onPostExecute: ");
            }
        }

    }

    private void setSelectedImage(Bitmap s, ImageView imageView, String imageType){

        if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {

            if (imageType.equals(Common.FULL_CIRCLE)) {
                selectedFull = getCircleBitmap(s);
                imageView.setImageBitmap(selectedFull);
            } else if (imageType.equals(Common.Left_HALF_CIRCLE)) {
                selectedLeftHalf = getCircleBitmap(s);
                selectedFull = null;
                //new CropImageTask(context,s,-90, -180, imageView).execute();
                imageView.setImageBitmap(cropToLeftCircleFunction(selectedLeftHalf)); //for right half circle
                //imageView.setImageBitmap(applyPieMask(s, -90, -180)); //for left half circle
            } else if (imageType.equals(Common.Right_HALF_CIRCLE)) {
                selectedFull = null;
                selectedRightHalf = getCircleBitmap(s);
                //new CropImageTask(context,s,-90, 180, imageView).execute();
                imageView.setImageBitmap(cropToRightCircleImageFunction(selectedRightHalf)); //for right half circle

            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {

            if (imageType.equals(Common.FULL_CIRCLE)) {
                selectedFull = getCircleBitmap(s);
                //imageView.setImageBitmap(selectedFull);
                imageView.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(selectedFull,selectedFull.getWidth()/2,0,selectedFull.getWidth()/2,selectedFull.getHeight()/2),-45));  // for top right quater  rotate bitmap
                slice_destination.setRotation(0);
            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
            if (imageType.equals(Common.FULL_CIRCLE)) {
                selectedFull = getCircleBitmap(s);
                imageView.setImageBitmap(Common.cropBitmapFunction(selectedFull,0,selectedFull.getHeight()/2,selectedFull.getWidth(),selectedFull.getHeight()/2));;
            } else if (imageType.equals(Common.Left_HALF_CIRCLE)) {
                selectedLeftHalf = getCircleBitmap(s);
                selectedFull = null;
                //new CropImageTask(context,s,-90, -180, imageView).execute();
                imageView.setImageBitmap(Common.cropBitmapFunction(selectedLeftHalf,0,selectedLeftHalf.getHeight()/2,selectedLeftHalf.getWidth()/2,selectedLeftHalf.getHeight()/2));
                //imageView.setImageBitmap(applyPieMask(s, -90, -180)); //for left half circle
            } else if (imageType.equals(Common.Right_HALF_CIRCLE)) {
                selectedFull = null;
                selectedRightHalf = getCircleBitmap(s);
                //new CropImageTask(context,s,-90, 180, imageView).execute();
                imageView.setImageBitmap(Common.cropBitmapFunction(selectedRightHalf,selectedRightHalf.getWidth()/2,selectedRightHalf.getHeight()/2,selectedRightHalf.getWidth()/2,selectedRightHalf.getHeight()/2));  // for bottom right quater

            }
        }else if (pizzaOrderInfoModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {
            if (imageType.equals(Common.FullSquare)) {
                selectedFull = Common.cropToSquare(s);
                imageView.setImageBitmap(selectedFull);
            }else if (imageType.equals(Common.LeftHalf)){
                selectedLeftHalf = s;
                imageView.setImageBitmap(cropToLeftCircleFunction(s));

            }else if (imageType.equals(Common.RightHalf)){
                selectedRightHalf = s;
                imageView.setImageBitmap(cropToRightCircleImageFunction(s));

            }else if (imageType.equals(Common.LeftTop)){
                selectedTopLeft = s;
                //imageView.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));

                imageView.setImageBitmap(Common.cropBitmapFunction(s,0,0,s.getWidth()/2,s.getHeight()/2));

            }else if (imageType.equals(Common.RightTop)){
                selectedTopRight= s;
                imageView.setImageBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,0,s.getWidth()/2,s.getHeight()/2));

            }else if (imageType.equals(Common.LeftBottom)){
                selectedBottomLeft = s;
                imageView.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));

            }else if (imageType.equals(Common.RightBottom)){
                selectedBottomRight = s;
                imageView.setImageBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));

            }

        }
    }

    public Bitmap applyPieMask(Bitmap src, float startAngle, float sweepAngle) {
        Log.d("TOAG",  "close it");

        Bitmap result=null;
        try {
            int width = src.getWidth();
            int height = src.getHeight();
            Log.d("TAG", "________________++++++: width "+width+ " height "+height);
            Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mask);
            canvas.drawColor(0x00000000);//fill mask bitmap with transparent black!

            Paint maskPaint = new Paint();
            maskPaint.setColor(0xFFFFFFFF);//pick highest value for bitwise AND operation
            maskPaint.setAntiAlias(true);

            RectF rect = new RectF(0, 0, width, height);
            canvas.drawArc(rect, startAngle, sweepAngle, true, maskPaint);//mask the pie


            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {
                    int color = mask.getPixel(j,i) & src.getPixel(j,i);
                    result.setPixel(j, i, color);
                    Log.d("TAG", "applyPieMask: width "+result.getWidth()+ " height "+result.getHeight() +" "+i+" "+j);

                }
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }catch (Exception e){
            Log.d("TAG", "applyPieMask: "+e.toString());
        }
        Log.d("TOAG",  "close it");

        return result;
    }

    public Bitmap getCircleBitmap(Bitmap bm) {

        int sice = Math.min((bm.getWidth()), (bm.getHeight()));

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap cropToRightCircleImageFunction(Bitmap bitmap){
        int side=0;
        if (bitmap.getHeight()<bitmap.getWidth()){
            side = bitmap.getHeight();

        }else {
            side = bitmap.getWidth();
        }
        return Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2,0,bitmap.getWidth()/2, side);
    }

    public Bitmap cropToLeftCircleFunction(Bitmap bitmap){
        int side=0;
        if (bitmap.getHeight()<bitmap.getWidth()){
            side = bitmap.getHeight();

        }else {
            side = bitmap.getWidth();
        }

        return Bitmap.createBitmap(
                bitmap, // source bitmap
                0, // x coordinate of the first pixel in source
                0, // y coordinate of the first pixel in source
                bitmap.getWidth()/2, // width
                side // height
        );
    }

}
