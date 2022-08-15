package com.orderpizaonline.pizzabella.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.enums.PizzaIngredientCategoryENum;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class PizzaDoughFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas;
    FirebaseRecyclerAdapter crustAdapter, spicedapter;
    RecyclerView spiceRecyclerView, crustRecyclerView;
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
    //List<SidelineOrderInfo> SelectedSidelines = new ArrayList<>();

    int orderPrice = 0;
    TextView tv_price;
    private SidelineInfo crustModel = new SidelineInfo();
    private SidelineInfo spiceModel = new SidelineInfo();
    int index2 = -1;
    int index4 = -1;
    Dialog rateus_dialog;
    TextView rateus_noText;
    RelativeLayout rl_below;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_your_pizza_dough, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas = firebaseDatabase.getReference(Common.PIZZA_INGRDIENT);


        rl_below = v.findViewById(R.id.rl_below);
        rl_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_price = v.findViewById(R.id.totalPrice);
        iv_forward = v.findViewById(R.id.iv_forward);
        iv_forward.setOnClickListener(this);
        iv_backward = v.findViewById(R.id.iv_backward);
        iv_backward.setOnClickListener(this);
        crustRecyclerView = v.findViewById(R.id.pizzaCrustREcyclerview);
        spiceRecyclerView = v.findViewById(R.id.pizzaSpiceRecyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        crustRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);
        StaggeredGridLayoutManager staggeredGridLayoutManagerSpice = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        spiceRecyclerView.setLayoutManager(staggeredGridLayoutManagerSpice);

        if (pizzaOrderInfoModel != null) {

            if (pizzaOrderInfoModel.getCrust() != null) {
                crustModel = pizzaOrderInfoModel.getCrust();
            }
            if (pizzaOrderInfoModel.getSpice() != null) {

                spiceModel = pizzaOrderInfoModel.getSpice();
            }
        }

        try {
            if (pizzaOrderInfoModel != null) {
                if (pizzaOrderInfoModel.getTotalPrice() != null && !pizzaOrderInfoModel.getTotalPrice().equals("")) {
                    orderPrice = Integer.parseInt(pizzaOrderInfoModel.getTotalPrice());
                    tv_price.setText(convertToString(orderPrice));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        showSpice();
        showCrust();


        rateus_dialog = new Dialog(context, R.style.CustomDialog);
        rateus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateus_dialog.setCancelable(false);
        rateus_dialog.setContentView(R.layout.custome_dialog);

        rateus_dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                return true;
            }
            return false;
        });

        rateus_noText = rateus_dialog.findViewById(R.id.dismiss_txt);

        rateus_noText.setOnClickListener(vi -> {
            rateus_dialog.dismiss();
        });
        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            Log.i("POOT", "onClick: dough");
            setNextPhoneFragment();
        } else if (v == iv_backward) {
            Log.i("POOT", "onClick: dough back");
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void showSpice() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                pizzas.orderByChild("category").equalTo(PizzaIngredientCategoryENum.Spice.toString()), SidelineInfo.class).build();
        spicedapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                if (model.getDiscount() != 0) {
                    itemPrice.setText(model.getDiscount());
                } else {
                    itemPrice.setText(model.getPrice());
                }

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);


                //final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item = holder.itemView.findViewById(R.id.ll_sideline_item);

                final RadioButton selection = holder.itemView.findViewById(R.id.selection);

                if (index2 == position) {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                    selection.setChecked(true);
                } else {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                    selection.setChecked(false);
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        index2 = position;
                        notifyDataSetChanged();


                        if (spiceModel != null) {
                            if (spiceModel.getDiscount() != 0) {
                                if (orderPrice != 0) {
                                    orderPrice = orderPrice - spiceModel.getDiscount();
                                }
                            } else {
                                if (orderPrice != 0) {
                                    if (spiceModel.getPrice() != null && !spiceModel.getPrice().equals("")) {
                                        orderPrice = orderPrice - Integer.parseInt(spiceModel.getPrice());
                                    }
                                }
                            }
                        }
                        spiceModel = model;
                        if (model.getDiscount() != 0) {
                            orderPrice = orderPrice + model.getDiscount();
                        } else {
                            if (model.getPrice() != null && !model.getPrice().equals("")) {
                                orderPrice = orderPrice + Integer.parseInt(model.getPrice());
                            }
                        }
                        pizzaOrderInfoModel.setSpice(spiceModel);
                        tv_price.setText(convertToString(orderPrice));

                    }
                });
            }
        };
        spicedapter.startListening();
        spiceRecyclerView.setAdapter(spicedapter);

    }

    private void showCrust() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                pizzas.orderByChild("category").equalTo(PizzaIngredientCategoryENum.Crust.toString()), SidelineInfo.class).build();
        crustAdapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                if (model.getDiscount() != 0) {
                    itemPrice.setText(model.getDiscount());
                } else {
                    itemPrice.setText(model.getPrice());
                }

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);

                //final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item = holder.itemView.findViewById(R.id.ll_sideline_item);

                final RadioButton selection = holder.itemView.findViewById(R.id.selection);

                if (index4 == position) {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                    selection.setChecked(true);
                } else {
                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                    selection.setChecked(false);
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        index4 = position;
                        notifyDataSetChanged();


                        if (crustModel != null) {
                            if (crustModel.getDiscount() != 0) {
                                if (orderPrice != 0) {
                                    orderPrice = orderPrice - crustModel.getDiscount();
                                }
                            } else {
                                if (orderPrice != 0) {
                                    if (crustModel.getPrice() != null && !crustModel.getPrice().equals("")) {
                                        orderPrice = orderPrice - Integer.parseInt(crustModel.getPrice());
                                    }
                                }
                            }
                        }
                        crustModel = model;
                        if (model.getDiscount() != 0) {
                            orderPrice = orderPrice + model.getDiscount();
                        } else {
                            if (model.getPrice() != null && !model.getPrice().equals("")) {
                                orderPrice = orderPrice + Integer.parseInt(model.getPrice());
                            }
                        }
                        pizzaOrderInfoModel.setCrust(crustModel);
                        tv_price.setText(convertToString(orderPrice));


                    }
                });
            }
        };
        crustAdapter.startListening();
        crustRecyclerView.setAdapter(crustAdapter);

    }

    private void setNextPhoneFragment() {
        if (spiceModel != null && crustModel != null) {
            if (spiceModel.getId() != null && crustModel.getId() != null) {

                pizzaOrderInfoModel.setTotalPrice(orderPrice + "");
                try {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    PizzaToppingFlavorFrag hf = (PizzaToppingFlavorFrag) fm.findFragmentByTag("PizzaToppingFlavorFrag");
                    if (hf == null) {
                        hf = new PizzaToppingFlavorFrag();
                        ft.replace(R.id.main_frame, hf, "PizzaToppingFlavorFrag");
                        ft.addToBackStack("PizzaToppingFlavorFrag");

                    } else {
                        ft.replace(R.id.main_frame, hf, "PizzaToppingFlavorFrag");
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    spiceModel = null;
                    crustModel = null;
                    orderPrice = 0;
                } catch (Exception e) {
                    Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
                }
                //sidelinesOrderList.addAll(SelectedSidelines);
            } else {
                rateus_dialog.show();
            }
        } else {
            rateus_dialog.show();
        }

        // SelectedSidelines.clear();

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


    public String convertToString(int price) {
        String tmp = "Rs." + price;
        return tmp;
    }
}