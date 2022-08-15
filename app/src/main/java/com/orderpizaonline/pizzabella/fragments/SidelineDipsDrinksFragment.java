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
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.enums.SidelineCategoryENum;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class SidelineDipsDrinksFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines;
    FirebaseRecyclerAdapter adapter,dipsAdapter;
    RecyclerView dipsRecyclerView, drinksRecyclerView;
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
    RelativeLayout rl_below;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.sideline_dips_drinks_fragment, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
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

        dipsRecyclerView =v.findViewById(R.id.sidelinesRecyclerview);
        drinksRecyclerView =v.findViewById(R.id.dessertsRecyclerview);
        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        dipsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        drinksRecyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);
        if (sidelinesOrderList.size()!=0){
            for (int i = 0; i<sidelinesOrderList.size(); i++){
                //SelectedSidelines.add(sidelinesOrderList.get(i));
                orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                tv_price.setText(convertToString(orderPrice));
            }
        }
        return v;
    }

    @Override
    public void onClick(View v) {

        if (v == iv_forward) {
            setNextPhoneFragment();
        }else if (v == iv_backward) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }

    }

    @Override
    public void onResume() {

        showOurSidelines();
        showDesserts();
        super.onResume();
    }

    private void showOurSidelines() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                sidelines.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Dips.toString()), SidelineInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<SidelineInfo, VegetablesViewHolder>(options) {
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
                itemPrice.setText(model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);

                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);

                if (sidelinesOrderList.size()!=0){
                    for (int i = 0; i<sidelinesOrderList.size(); i++){
                        if (sidelinesOrderList.get(i).getId().equals(model.getId()) ){
                            tv_count.setText(sidelinesOrderList.get(i).getQuantity()+"");
                            //SelectedSidelines.add(sidelinesOrderList.get(i));
                           // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                          //  tv_price.setText(convertToString(orderPrice));
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }
                    }
                }
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i=0;i<sidelinesOrderList.size();i++){
                            if (sidelinesOrderList.get(i).getId().equals(model.getId())){
                                int temp=sidelinesOrderList.get(i).getQuantity()+1;
                                sidelinesOrderList.get(i).setQuantity(temp);
                                int pr = Integer.parseInt(model.getPrice());
                                pr += Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                orderPrice += Integer.parseInt(model.getPrice());
                                sidelinesOrderList.get(i).setPrice(String.valueOf(pr));
                                tv_price.setText(convertToString(orderPrice));
                                tv_count.setText(""+temp);
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i=0;i<sidelinesOrderList.size();i++){
                            if (sidelinesOrderList.get(i).getId().equals(model.getId())){
                                if (sidelinesOrderList.get(i).getQuantity()>1){
                                    int temp=sidelinesOrderList.get(i).getQuantity()-1;
                                    sidelinesOrderList.get(i).setQuantity(temp);

                                    int pr = Integer.parseInt(sidelinesOrderList.get(i).getPrice());

                                    pr -=Integer.parseInt(model.getPrice());
                                    orderPrice -= Integer.parseInt(model.getPrice());
                                    sidelinesOrderList.get(i).setPrice(String.valueOf(pr));
                                    tv_price.setText(convertToString(orderPrice));

                                    tv_count.setText(""+temp);
                                }else {

                                    orderPrice -=  Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice));

                                    sidelinesOrderList.remove(i);
                                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                                    ll_add_minus.setVisibility(View.GONE);
                                    selection.setChecked(false);
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

                        for (int i = 0; i < sidelinesOrderList.size(); i++) {
                            if (model.getId().equals(sidelinesOrderList.get(i).getId()) ) {
                                isFOund = true;
                                orderPrice -= Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                tv_price.setText(convertToString(orderPrice));
                                sidelinesOrderList.remove(i);
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            ll_add_minus.setVisibility(View.GONE);
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice));
                            tv_count.setText(""+1);
                            sidelinesOrderList.add(orderModel);
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        adapter.startListening();
        dipsRecyclerView.setAdapter(adapter);

    }

    private void showDesserts() {
        FirebaseRecyclerOptions<SidelineInfo> options = new FirebaseRecyclerOptions.Builder<SidelineInfo>().setQuery(
                sidelines.orderByChild(Common.SIDELINE_CATEGORY).equalTo(SidelineCategoryENum.Drinks.toString()), SidelineInfo.class).build();

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
                itemPrice.setText(model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);

                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                final LinearLayout ll_add_minus =holder.itemView.findViewById(R.id.ll_add_minus);
                final LinearLayout ll_sideline_item =holder.itemView.findViewById(R.id.ll_sideline_item);
                ImageView add =holder.itemView.findViewById(R.id.add);
                ImageView  minus =holder.itemView.findViewById(R.id.minus);

                final RadioButton selection =holder.itemView.findViewById(R.id.selection);
                final TextView tv_count =holder.itemView.findViewById(R.id.tv_count);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i=0;i<sidelinesOrderList.size();i++){
                            if (sidelinesOrderList.get(i).getId().equals(model.getId())){
                                int temp=sidelinesOrderList.get(i).getQuantity()+1;
                                sidelinesOrderList.get(i).setQuantity(temp);
                                int pr = Integer.parseInt(model.getPrice());
                                pr += Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                orderPrice += Integer.parseInt(model.getPrice());
                                sidelinesOrderList.get(i).setPrice(String.valueOf(pr));
                                tv_price.setText(convertToString(orderPrice));
                                tv_count.setText(""+temp);
                            }
                        }
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i=0;i<sidelinesOrderList.size();i++){
                            if (sidelinesOrderList.get(i).getId().equals(model.getId())){
                                if (sidelinesOrderList.get(i).getQuantity()>1){
                                    int temp=sidelinesOrderList.get(i).getQuantity()-1;
                                    sidelinesOrderList.get(i).setQuantity(temp);

                                    int pr = Integer.parseInt(sidelinesOrderList.get(i).getPrice());

                                    pr -=Integer.parseInt(model.getPrice());
                                    orderPrice -= Integer.parseInt(model.getPrice());
                                    sidelinesOrderList.get(i).setPrice(String.valueOf(pr));
                                    tv_price.setText(convertToString(orderPrice));

                                    tv_count.setText(""+temp);
                                }else {

                                    orderPrice -=  Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                    tv_price.setText(convertToString(orderPrice));

                                    sidelinesOrderList.remove(i);
                                    ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                                    ll_add_minus.setVisibility(View.GONE);
                                    selection.setChecked(false);
                                }
                            }
                        }
                    }
                });
                if (sidelinesOrderList.size()!=0){
                    for (int i = 0; i<sidelinesOrderList.size(); i++){
                        if (sidelinesOrderList.get(i).getId().equals( model.getId())){
                            tv_count.setText(sidelinesOrderList.get(i).getQuantity()+"");
                           // SelectedSidelines.add(sidelinesOrderList.get(i));
                           // orderPrice = orderPrice + Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                           // tv_price.setText(convertToString(orderPrice));
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
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

                        for (int i = 0; i < sidelinesOrderList.size(); i++) {
                            if (model.getId().equals(sidelinesOrderList.get(i).getId())) {
                                isFOund = true;
                                orderPrice -= Integer.parseInt(sidelinesOrderList.get(i).getPrice());
                                tv_price.setText(convertToString(orderPrice));
                                sidelinesOrderList.remove(i);
                            }
                        }
                        if (isFOund) {
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.simple_border_shape));
                            ll_add_minus.setVisibility(View.GONE);
                            selection.setChecked(false);
                        } else {
                            orderPrice += Integer.parseInt(orderModel.getPrice());
                            tv_price.setText(convertToString(orderPrice));
                            tv_count.setText(""+1);
                            sidelinesOrderList.add(orderModel);
                            selection.setChecked(true);
                            ll_add_minus.setVisibility(View.VISIBLE);
                            ll_sideline_item.setBackground(getResources().getDrawable(R.drawable.red_border_shape));
                        }

                    }
                });
            }
        };
        dipsAdapter.startListening();
        drinksRecyclerView.setAdapter(dipsAdapter);

    }

    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectedSidelinesFragment hf = (SelectedSidelinesFragment) fm.findFragmentByTag("SelectedSidelinesFragment");
            if (hf == null) {
                hf = new SelectedSidelinesFragment();
            }
            ft.replace(R.id.main_frame, hf, "SelectedSidelinesFragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack("SelectedSidelinesFragment");
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }

        //sidelinesOrderList.addAll(SelectedSidelines);
        orderPrice = 0;
       // SelectedSidelines.clear();

    }

    private void setPreviousPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SidelinesDessertsFragment hf = (SidelinesDessertsFragment) fm.findFragmentByTag("SidelineDessertFragment");
            if (hf == null) {
                hf = new SidelinesDessertsFragment();
                ft.replace(R.id.main_frame, hf, "SidelineDessertFragment");
                ft.addToBackStack("SidelineDessertFragment");
            }else{

                ft.replace(R.id.main_frame, hf, "SidelineDessertFragment");
            }
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