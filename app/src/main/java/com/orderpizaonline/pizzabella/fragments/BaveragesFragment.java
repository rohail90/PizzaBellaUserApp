package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.BeveragesInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class BaveragesFragment extends Fragment implements View.OnClickListener {
    TextView tv_order_now;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference sidelines;
    FirebaseRecyclerAdapter adapter, desAdapter;
    RecyclerView recyclerView, desserts_recyclerView;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.sidelines_fragment_screen, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        sidelines = firebaseDatabase.getReference(Common.BEVERAGES);


        tv_order_now =v.findViewById(R.id.tv_order_now);
        tv_order_now.setOnClickListener(this);
        recyclerView =v.findViewById(R.id.sidelines_recycler_view);
        //desserts_recyclerView =v.findViewById(R.id.desserts_recycler_view);
        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        StaggeredGridLayoutManager staggeredGridLayoutManagerDesserts = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
//        desserts_recyclerView.setLayoutManager(staggeredGridLayoutManagerDesserts);
        showOurSidelines();
       // showDesserts();
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_order_now){
            sidelinesOrderList.clear();
            setNextPhoneFragment();
        }

    }

    private void showOurSidelines() {
        FirebaseRecyclerOptions<BeveragesInfo> options = new FirebaseRecyclerOptions.Builder<BeveragesInfo>().setQuery(
                sidelines, BeveragesInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<BeveragesInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final BeveragesInfo model) {

                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getName());

                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                //itemPrice.setText("Rs. "+model.getSizesArrayList().get(0).getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
              //  Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.soft_drink));
                TextView priceWas = holder.itemView.findViewById(R.id.priceWas);
                RelativeLayout relativeLayout=holder.itemView.findViewById(R.id.wasPriceRL);
                ImageView discountIcon=holder.itemView.findViewById(R.id.discountImg);

                if (model.getDiscount()>0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    itemPrice.setText(""+model.getDiscount()+" % OFF");
                    priceWas.setText("RS."+ Utils.getOriginalPrice(model.getSizesArrayList().get(0).getPrice(), model.getDiscount()));
                    discountIcon.setVisibility(View.VISIBLE);
                }else {
                    relativeLayout.setVisibility(View.GONE);
                    itemPrice.setText("RS."+model.getSizesArrayList().get(0).getPrice());
                    discountIcon.setVisibility(View.GONE);

                }

                // totalAmount.setText(model.getTotal());

                /*itemSelectionImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "onClick: ");
                        //editOrDeleteOfferDialog(mealID,model);
                    }
                });*/
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Sending food_id to FoodDetailActivity
//                        Intent intent = new Intent(FoodActivityServer.this, FoodDetailActivity.class);
//                        intent.putExtra("foodId", adapter.getRef(position).getKey());
//                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ShowBeveragesFragment hf = (ShowBeveragesFragment) fm.findFragmentByTag("ShowBeveragesFragment");
            if (hf == null) {
                hf = new ShowBeveragesFragment();
                ft.replace(R.id.main_frame, hf, "ShowBeveragesFragment");
                ft.addToBackStack("ShowBeveragesFragment");
            }else{
                ft.replace(R.id.main_frame, hf, "ShowBeveragesFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }


    }

    @Override
    public void onResume() {

        pizzaOrderInfoModel = null;
        pizzaOrderInfoModel = new PizzaOrderInfo();
        ((MainActivity) context).setToolbarTitle("Pizza Menu");
        super.onResume();
    }
}