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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.ComboInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;


public class DealsFragment extends Fragment implements View.OnClickListener {
    TextView tv_order_now, tv_title;
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
        sidelines = firebaseDatabase.getReference(Common.COMBO);


        tv_title =v.findViewById(R.id.tv_title);
        tv_title.setText("Deals");
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
        FirebaseRecyclerOptions<ComboInfo> options = new FirebaseRecyclerOptions.Builder<ComboInfo>().setQuery(
                sidelines, ComboInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<ComboInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final ComboInfo model) {

                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());

                TextView itemPrice = holder.itemView.findViewById(R.id.price);
                //itemPrice.setText("Rs. "+model.getPrice());

                ImageView itemImage = holder.itemView.findViewById(R.id.image);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);
                itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.soft_drink));
                ImageView discountIcon=holder.itemView.findViewById(R.id.discountImg);

                TextView priceWas = holder.itemView.findViewById(R.id.priceWas);
                RelativeLayout relativeLayout=holder.itemView.findViewById(R.id.wasPriceRL);
                itemPrice.setVisibility(View.VISIBLE);

                if (model.getDiscount()>0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    itemPrice.setText(""+model.getDiscount()+ " % OFF");
                    priceWas.setText("RS."+ Utils.getOriginalPrice(Integer.parseInt(model.getPrice()), model.getDiscount()));
                    discountIcon.setVisibility(View.VISIBLE);
                }else {
                    relativeLayout.setVisibility(View.GONE);
                    itemPrice.setText("RS."+model.getPrice());
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
            ShowDealsFragment hf = (ShowDealsFragment) fm.findFragmentByTag("ShowDealsFragment");
            if (hf == null) {
                hf = new ShowDealsFragment();
                ft.replace(R.id.main_frame, hf, "ShowDealsFragment");
                ft.addToBackStack("ShowDealsFragment");
            }else{
                ft.replace(R.id.main_frame, hf, "ShowDealsFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
        }


    }

    @Override
    public void onResume() {

        ((MainActivity) context).setToolbarTitle("Pizza Menu");
        super.onResume();
    }
}