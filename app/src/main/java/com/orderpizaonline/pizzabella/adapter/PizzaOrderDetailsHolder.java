package com.orderpizaonline.pizzabella.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;

import java.util.ArrayList;
import java.util.List;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoBella;


public class PizzaOrderDetailsHolder extends RecyclerView.ViewHolder {

    TextView  tv_dough_name, tv_crust_name, tv_sauce_name, tv_spice_name, tv_toppings_name,
    tv_vegetables_name, tv_total_piiza_price, tv_piiza_price, tv_crust_price, tv_sliceTwo_toppings_vegeis, tv_sliceOne_toppings_vegeis
    ,tv_sliceTwo_toppings_name, tv_sliceOne_toppings_name, tv_sliceTwo_name
            , tv_sliceOne_name, tv_name_description;
    private TextView tv_sliceThree_toppings_vegeis, tv_slicefour_toppings_vegeis
            ,tv_sliceThree_toppings_name, tv_slicefour_toppings_name,
            tv_sliceThree_name, tv_slicefour_name;

    ImageView  square_destination,square_destination_right_full, square_destination_left_full, square_destination_left_top_full,
            square_destination_right_top_full , square_destination_left_bottom_full, square_destination_right_bottom_full;
    ImageView  semi_circle_destination, semi_destination_right_full, semi_destination_left_full;

    private ImageView  plus, minus, delete,destination_right_full, destination_left_full, destination;
    private TextView name, price, quantity, totalPrice, tv_pizza_price;
    private List<PizzaOrderCartModel> mList;
    Context context;

    private ImageView iv_delete, iv_minus, iv_plus;
    private LinearLayout ll_pizza_info_item, ll_extrass, slice_four, slice_one, slice_two, slice_three, ll_d
            , ll_a, ll_b, ll_c, ll_toppings, ll_vagetables, ll_top, ll_veg, ll_ext , ll_price;
    List<PizzaToppingInfo> toppingsList = new ArrayList<>();
    private RelativeLayout rl_full_circle, rl_full_square, rl_semi_circle, rl_slice;
    ImageView slice_destination, slice_destination_full;
    ListView lv_extras;
    List<SidelineOrderInfo> tmpSidelines;

    TextView tv_extras_price, tv_extras_name;

    public PizzaOrderDetailsHolder(@NonNull View itemView, List<PizzaOrderCartModel> mList, Context context) {
        super(itemView);
        this.context = context;


        ll_price =itemView.findViewById(R.id.ll_price);

        tv_extras_price =itemView.findViewById(R.id.tv_extras_price);
        tv_extras_name =itemView.findViewById(R.id.tv_extras_name);

        rl_slice =itemView.findViewById(R.id.rl_slice);
        slice_destination =itemView.findViewById(R.id.slice_destination);
        slice_destination_full =itemView.findViewById(R.id.slice_destination_full);

        semi_circle_destination =itemView.findViewById(R.id.semi_circle_destination);
        semi_destination_right_full=itemView.findViewById(R.id.semi_destination_right_full);
        semi_destination_left_full=itemView.findViewById(R.id.semi_destination_left_full);
        rl_semi_circle=itemView.findViewById(R.id.rl_semi_circle);

        // for Square Selection

        tv_slicefour_toppings_name= itemView.findViewById(R.id.tv_sliceFour_toppings);
        tv_sliceThree_toppings_name= itemView.findViewById(R.id.tv_sliceThree_toppings_name);
        tv_slicefour_toppings_vegeis= itemView.findViewById(R.id.tv_sliceFour_veggies);
        tv_sliceThree_toppings_vegeis= itemView.findViewById(R.id.tv_sliceThree_toppings_vegeis);
        tv_sliceThree_name= itemView.findViewById(R.id.tv_sliceThree_name);
        tv_slicefour_name= itemView.findViewById(R.id.tv_sliceFour_name);

        square_destination=itemView.findViewById(R.id.square_destination);
        square_destination_right_full =itemView.findViewById(R.id.square_destination_right_full);
        square_destination_left_full =itemView.findViewById(R.id.square_destination_left_full);
        square_destination_left_top_full=itemView.findViewById(R.id.square_destination_left_top_full);
        square_destination_right_top_full=itemView.findViewById(R.id.square_destination_right_top_full);
        square_destination_left_bottom_full=itemView.findViewById(R.id.square_destination_left_bottom_full);
        square_destination_right_bottom_full=itemView.findViewById(R.id.square_destination_right_bottom_full);
        rl_full_square =itemView.findViewById(R.id.rl_full_square);
        rl_full_circle =itemView.findViewById(R.id.rl_full_circle);

        destination= itemView.findViewById(R.id.destination);
        destination_right_full= itemView.findViewById(R.id.destination_right_full);
        destination_left_full= itemView.findViewById(R.id.destination_left_full);

        tv_sliceOne_toppings_name= itemView.findViewById(R.id.tv_sliceOne_toppings_name);
        tv_sliceTwo_toppings_name= itemView.findViewById(R.id.tv_sliceTwo_toppings_name);
        tv_sliceOne_toppings_vegeis= itemView.findViewById(R.id.tv_sliceOne_toppings_vegeis);
        tv_sliceTwo_toppings_vegeis= itemView.findViewById(R.id.tv_sliceTwo_toppings_vegeis);
        tv_sliceTwo_name= itemView.findViewById(R.id.tv_sliceTwo_name);
        tv_sliceOne_name= itemView.findViewById(R.id.tv_sliceOne_name);

        tv_name_description= itemView.findViewById(R.id.tv_piiza_name);
        ll_toppings= itemView.findViewById(R.id.ll_toppings);
        ll_vagetables= itemView.findViewById(R.id.ll_vagetables);
        ll_top= itemView.findViewById(R.id.ll_top);
        ll_veg= itemView.findViewById(R.id.ll_veg);
        ll_ext= itemView.findViewById(R.id.ll_ext);
        ll_a= itemView.findViewById(R.id.ll_a);
        ll_b= itemView.findViewById(R.id.ll_b);
        ll_c= itemView.findViewById(R.id.ll_c);
        ll_d= itemView.findViewById(R.id.ll_d);
//        lv_extras = itemView.findViewById(R.id.lv_extras);
        slice_four= itemView.findViewById(R.id.slice_four);
        slice_one= itemView.findViewById(R.id.slice_one);
        slice_three= itemView.findViewById(R.id.slice_three);
        slice_two= itemView.findViewById(R.id.slice_two);
        ll_extrass= itemView.findViewById(R.id.ll_extrass);
        tv_dough_name= itemView.findViewById(R.id.tv_dough_name);
        tv_crust_name= itemView.findViewById(R.id.tv_crust_name);
        tv_sauce_name = itemView.findViewById(R.id.tv_sauce_name);
        tv_spice_name = itemView.findViewById(R.id.tv_spice_name);
        tv_toppings_name= itemView.findViewById(R.id.tv_topping_name);
        tv_vegetables_name = itemView.findViewById(R.id.tv_vegetables_name);
        ll_pizza_info_item = itemView.findViewById(R.id.ll_piiza_info_item);


        delete = itemView.findViewById(R.id.iv_delete);
        plus = itemView.findViewById(R.id.iv_plus);
        minus = itemView.findViewById(R.id.iv_minus);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.tv_quantity);
        totalPrice = itemView.findViewById(R.id.totalPrice);
        tv_pizza_price = itemView.findViewById(R.id.tv_pizza_price);
        this.mList = mList;
    }


    public void bindData(final PizzaOrderCartModel achievementModel, final int position, int size) {

        plus.setVisibility(View.GONE);
        minus.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        ll_price.setVisibility(View.GONE);
        List<PizzaToppingInfo> imgUriList = new ArrayList<>();

        imgUriList.add(achievementModel.getPizzaToppingInfo());
       /* if (achievementModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_CIRCLE)) {
            rl_full_circle.setVisibility(View.VISIBLE);

            if (imgUriList.size() == 1) {
                try {
                    slice_three.setVisibility(View.GONE);
                    slice_two.setVisibility(View.GONE);
                    slice_four.setVisibility(View.GONE);
                    slice_one.setVisibility(View.GONE);
                    ll_a.setVisibility(View.GONE);
                    ll_b.setVisibility(View.GONE);
                    ll_c.setVisibility(View.GONE);
                    ll_d.setVisibility(View.GONE);
                    slice_one.setVisibility(View.GONE);
                    slice_two.setVisibility(View.GONE);
                    slice_three.setVisibility(View.GONE);
                    slice_four.setVisibility(View.GONE);

                    tv_toppings_name.setText(imgUriList.get(0).getItemName());
                    String vegeis = "";
                    for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                        if (i == 0) {
                            vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                        } else {
                            vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                        }
                    }
                    tv_vegetables_name.setText(vegeis);

                    if (achievementModel.getComboInfo() != null) {
                        if (achievementModel.getComboInfo().getItemName() != null) {
//                            Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(destination);
                            Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(destination);

                        } else {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                                destination.setImageBitmap(Common.getCircleBitmap(bitmap));
                            } catch (Exception e) {
                            }
                        }
                    } else {

                        String path = imgUriList.get(0).getImageUri();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(path));
                 *//*   ParcelFileDescriptor parcelFileDescriptor =
                            context.getContentResolver().openFileDescriptor(imgUriList.get(0), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();*//*
                        destination.setImageBitmap(Common.getCircleBitmap(bitmap));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (imgUriList.size() == 2) {
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

                for (int j = 0; j < imgUriList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(imgUriList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);
                   *//* if (achievementModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceOne_name.setText("Slice 1");
                    }else {
                        tv_sliceOne_name.setText("Half 1");
                    }*//*
                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(imgUriList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);
                    *//*if (achievementModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceTwo_name.setText("Slice 2");
                    }else {
                        tv_sliceTwo_name.setText("Half 2");
                    }*//*
                    }
                }


                if (achievementModel.getComboInfo() != null) {
                    if (achievementModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(destination);
                        Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(destination);

                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                            destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(Common.getCircleBitmap(bitmap)));
                            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri()));
                            destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(Common.getCircleBitmap(bitmap2)));
                        } catch (Exception e) {
                        }
                    }
                } else {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                        destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(Common.getCircleBitmap(bitmap)));
                        Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri()));
                        destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(Common.getCircleBitmap(bitmap2)));
                    } catch (Exception e) {
                    }
                }
            }
        }
        else if (achievementModel.getPizzaType().equals(Common.PIZZA_TYPE_SLICE)) {
                rl_slice.setVisibility(View.VISIBLE);

                if (imgUriList.size() == 1) {
                    try {
                        slice_three.setVisibility(View.GONE);
                        slice_two.setVisibility(View.GONE);
                        slice_four.setVisibility(View.GONE);
                        slice_one.setVisibility(View.GONE);
                        ll_a.setVisibility(View.GONE);
                        ll_b.setVisibility(View.GONE);
                        ll_c.setVisibility(View.GONE);
                        ll_d.setVisibility(View.GONE);
                        slice_one.setVisibility(View.GONE);
                        slice_two.setVisibility(View.GONE);
                        slice_three.setVisibility(View.GONE);
                        slice_four.setVisibility(View.GONE);

                        tv_toppings_name.setText(imgUriList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_vegetables_name.setText(vegeis);

                        if (achievementModel.getComboInfo() != null) {
                            if (achievementModel.getComboInfo().getItemName() != null) {
//                            Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(slice_destination_full);
                                Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(slice_destination_full);


                            } else {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
//                                slice_destination.setImageBitmap(Common.getCircleBitmap(bitmap));
                                    Bitmap s = Common.getCircleBitmap(bitmap);
                                    slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,0,s.getWidth()/2,s.getHeight()/2),-45));  // for top right quater  rotate bitmap

                                } catch (Exception e) {
                                }
                            }
                        } else {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
//                                slice_destination.setImageBitmap(Common.getCircleBitmap(bitmap));
                            Bitmap s = Common.getCircleBitmap(bitmap);
                            slice_destination.setImageBitmap(Common.RotateBitmap(Common.cropBitmapFunction(s,s.getWidth()/2,0,s.getWidth()/2,s.getHeight()/2),-45));  // for top right quater  rotate bitmap


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (achievementModel.getPizzaType().equals(Common.PIZZA_TYPE_HALF_CIRCLE)) {
                rl_semi_circle.setVisibility(View.VISIBLE);

                if (imgUriList.size() == 1) {
                    try {
                        slice_three.setVisibility(View.GONE);
                        slice_two.setVisibility(View.GONE);
                        slice_four.setVisibility(View.GONE);
                        slice_one.setVisibility(View.GONE);
                        ll_a.setVisibility(View.GONE);
                        ll_b.setVisibility(View.GONE);
                        ll_c.setVisibility(View.GONE);
                        ll_d.setVisibility(View.GONE);
                        slice_one.setVisibility(View.GONE);
                        slice_two.setVisibility(View.GONE);
                        slice_three.setVisibility(View.GONE);
                        slice_four.setVisibility(View.GONE);

                        tv_toppings_name.setText(imgUriList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_vegetables_name.setText(vegeis);

                        if (achievementModel.getComboInfo() != null) {
                            if (achievementModel.getComboInfo().getItemName() != null) {
//                            Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(semi_circle_destination);
                                Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(semi_circle_destination);

                            } else {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                                    Bitmap s = Common.getCircleBitmap(bitmap);
                                    semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth(),s.getHeight()/2));

                                } catch (Exception e) {
                                }
                            }
                        } else {

                            String path = imgUriList.get(0).getImageUri();

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(path));
                 *//*   ParcelFileDescriptor parcelFileDescriptor =
                            context.getContentResolver().openFileDescriptor(imgUriList.get(0), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();*//*
                            Bitmap s = Common.getCircleBitmap(bitmap);
                            semi_circle_destination.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth(),s.getHeight()/2));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (imgUriList.size() == 2) {
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

                    for (int j = 0; j < imgUriList.size(); j++) {
                        if (j == 0) {
                            tv_sliceOne_toppings_name.setText(imgUriList.get(0).getItemName());
                            String vegeis = "";
                            for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                                if (i == 0) {
                                    vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                                } else {
                                    vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                                }
                            }
                            tv_sliceOne_toppings_vegeis.setText(vegeis);
                   *//* if (achievementModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceOne_name.setText("Slice 1");
                    }else {
                        tv_sliceOne_name.setText("Half 1");
                    }*//*
                        }
                        if (j == 1) {
                            tv_sliceTwo_toppings_name.setText(imgUriList.get(1).getItemName());
                            String vegeis = "";
                            for (int i = 0; i < imgUriList.get(1).getVegeInfoArrayList().size(); i++) {
                                if (i == 0) {
                                    vegeis = imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                                } else {
                                    vegeis = vegeis + "," + imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                                }
                            }
                            tv_sliceTwo_toppings_vegeis.setText(vegeis);
                    *//*if (achievementModel.getPizzaType().equals(Common.PIZZA_SINGLE_SLICE)){
                        tv_sliceTwo_name.setText("Slice 2");
                    }else {
                        tv_sliceTwo_name.setText("Half 2");
                    }*//*
                        }
                    }


                    if (achievementModel.getComboInfo() != null) {
                        if (achievementModel.getComboInfo().getItemName() != null) {
//                        Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(semi_circle_destination);
                            Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(semi_circle_destination);

                        } else {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri()));

                                Bitmap s = Common.getCircleBitmap(bitmap);
                                Bitmap s1 = Common.getCircleBitmap(bitmap2);
                                semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));
                                semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1,s1.getWidth()/2,s1.getHeight()/2,s1.getWidth()/2,s1.getHeight()/2));  // for bottom right quater

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));
                            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri()));

                            Bitmap s = Common.getCircleBitmap(bitmap);`
                            Bitmap s1 = Common.getCircleBitmap(bitmap2);
                            semi_destination_left_full.setImageBitmap(Common.cropBitmapFunction(s,0,s.getHeight()/2,s.getWidth()/2,s.getHeight()/2));
                            semi_destination_right_full.setImageBitmap(Common.cropBitmapFunction(s1,s1.getWidth()/2,s1.getHeight()/2,s1.getWidth()/2,s1.getHeight()/2));  // for bottom right quater

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else if (achievementModel.getPizzaType().equals(Common.PIZZA_TYPE_FULL_Square)) {*/
            rl_full_square.setVisibility(View.VISIBLE);
            /*if (imgUriList.size() == 1) {*/
                /*if (pizzaOrderInfoModel.getComboInfo() != null) {
                    if (pizzaOrderInfoModel.getComboInfo().getItemName() != null) {*/
//                        Picasso.get().load(pizzaOrderInfoModel.getComboInfo().getImageURL()).into(square_destination);
                        Glide.with(context).load(achievementModel.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

/*
                    } else {
                        try {
                            Bitmap s = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));

                            square_destination.setImageBitmap(s);
                        }catch (Exception e){}
                    }
                } else {
                    try {
                        Bitmap s = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri()));

                        square_destination.setImageBitmap(s);
                    }catch (Exception e){}
                }*/

                slice_three.setVisibility(View.GONE);
                slice_two.setVisibility(View.GONE);
                slice_four.setVisibility(View.GONE);
                slice_one.setVisibility(View.GONE);

                tv_toppings_name.setText(imgUriList.get(0).getItemName());
                String vegeis = "";
                for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                    if (i == 0) {
                        vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                    } else {
                        vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
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


           /* }
            else if (imgUriList.size() == 2) {
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

                try {
                    Bitmap s = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri())),
                            s1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri()));

                    if (achievementModel.getComboInfo() != null) {
                        if (achievementModel.getComboInfo().getItemName() != null) {
//                            Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(square_destination);
                            Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

                        } else {
                            square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(s));
                            square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(s1));
                        }
                    } else {

                        square_destination_left_full.setImageBitmap(Common.cropToLeftCircleFunction(s));
                        square_destination_right_full.setImageBitmap(Common.cropToRightCircleImageFunction(s1));
                    }
                } catch (Exception e) {
                }


                for (int j = 0; j < imgUriList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(imgUriList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);


                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(imgUriList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);


                    }
                }
            }
            else if (imgUriList.size() == 4) {

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
                tv_sliceThree_name.setText("Quarter 2");
                tv_slicefour_name.setText("Quarter 2");

                try {

                    Bitmap s = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(0).getImageUri())),
                            s1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(1).getImageUri())),
                            s2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(2).getImageUri())),
                            s3 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imgUriList.get(3).getImageUri()));


                    if (achievementModel.getComboInfo() != null) {
                        if (achievementModel.getComboInfo().getItemName() != null) {
//                            Picasso.get().load(achievementModel.getComboInfo().getImageURL()).into(square_destination);
                            Glide.with(context).load(achievementModel.getComboInfo().getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(square_destination);

                        } else {
                            square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                            square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, 0, s1.getWidth() / 2, s1.getHeight() / 2));
                            square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s2, 0, s2.getHeight() / 2, s2.getWidth() / 2, s2.getHeight() / 2));
                            square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s3, s3.getWidth() / 2, s3.getHeight() / 2, s3.getWidth() / 2, s3.getHeight() / 2));
                        }
                    } else {
                        square_destination_left_top_full.setImageBitmap(Common.cropBitmapFunction(s, 0, 0, s.getWidth() / 2, s.getHeight() / 2));
                        square_destination_right_top_full.setImageBitmap(Common.cropBitmapFunction(s1, s1.getWidth() / 2, 0, s1.getWidth() / 2, s1.getHeight() / 2));
                        square_destination_left_bottom_full.setImageBitmap(Common.cropBitmapFunction(s2, 0, s2.getHeight() / 2, s2.getWidth() / 2, s2.getHeight() / 2));
                        square_destination_right_bottom_full.setImageBitmap(Common.cropBitmapFunction(s3, s3.getWidth() / 2, s3.getHeight() / 2, s3.getWidth() / 2, s3.getHeight() / 2));
                    }
                } catch (Exception e) {
                }

                for (int j = 0; j < imgUriList.size(); j++) {
                    if (j == 0) {
                        tv_sliceOne_toppings_name.setText(imgUriList.get(0).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(0).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(0).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceOne_toppings_vegeis.setText(vegeis);


                    }
                    if (j == 1) {
                        tv_sliceTwo_toppings_name.setText(imgUriList.get(1).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(1).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(1).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceTwo_toppings_vegeis.setText(vegeis);


                    }
                    if (j == 2) {
                        tv_sliceThree_toppings_name.setText(imgUriList.get(2).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(2).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(2).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(2).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_sliceThree_toppings_vegeis.setText(vegeis);

                    }
                    if (j == 3) {
                        tv_slicefour_toppings_name.setText(imgUriList.get(3).getItemName());
                        String vegeis = "";
                        for (int i = 0; i < imgUriList.get(3).getVegeInfoArrayList().size(); i++) {
                            if (i == 0) {
                                vegeis = imgUriList.get(3).getVegeInfoArrayList().get(i).getSidelineName();

                            } else {
                                vegeis = vegeis + "," + imgUriList.get(3).getVegeInfoArrayList().get(i).getSidelineName();
                            }
                        }
                        tv_slicefour_toppings_vegeis.setText(vegeis);

                    }
                }
            }*/
        /*}*/

        if (achievementModel.getPizzaExtraToppingList() != null) {
            if (achievementModel.getPizzaExtraToppingList() .size() > 0) {

                tv_extras_name.setText(pizzaOrderInfoBella.getExtraTopping().getName());
                tv_extras_price.setText(pizzaOrderInfoBella.getExtraTopping().getValue());

            } else {
                ll_extrass.setVisibility(View.GONE);
                ll_ext.setVisibility(View.GONE);
            }

        }else{
            ll_extrass.setVisibility(View.GONE);
            ll_ext.setVisibility(View.GONE);
        }


        final int p = Integer.parseInt(achievementModel.getPrice());
        name.setText(achievementModel.getPizzaName());
        /*if (achievementModel.getComboInfo()!=null) {
            if (achievementModel.getComboInfo().getDescription() != null) {
                tv_name_description.setText(achievementModel.getComboInfo().getDescription());
            }else {
                tv_name_description.setText(achievementModel.getPizzaName());
            }
        }else {*/
            tv_name_description.setText(achievementModel.getPizzaName());

       /* }*/
        price.setText(achievementModel.getPrice());
        quantity.setText(achievementModel.getQuantity()+"");
        totalPrice.setText(achievementModel.getQuantity() * p+"");
        tv_pizza_price.setText(achievementModel.getQuantity() * p+"");

   /*     plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()+1;
                mList.get(position).setQuantity(q);
                orderPizzas.get(position).setQuantity(q);
                int pr= Integer.parseInt(mList.get(position).getPrice());
                new DBClass(context).UpdatePizzaQuantity(achievementModel.getId(),q);
                quantity.setText(q+"");
                totalPrice.setText( (q * pr) +"");
                setTotalCartPrice();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=mList.get(position).getQuantity()-1;
                if (q != 0) {
                    mList.get(position).setQuantity(q);
                    orderPizzas.get(position).setQuantity(q);
                    int pr = Integer.parseInt(mList.get(position).getPrice());
                    new DBClass(context).UpdatePizzaQuantity(achievementModel.getId(), q);
                    quantity.setText(q + "");
                    totalPrice.setText((q * pr) + "");
                    setTotalCartPrice();

                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mList.remove(position);
                   //orderPizzas.remove(position);
                    new DBClass(context).DeletePizza(achievementModel.getId());
                    setTotalCartPrice();

            }
        });*/

    }



}
