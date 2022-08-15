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
import com.orderpizaonline.pizzabella.model.ChineseCornerInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Utils;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import java.util.Objects;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class ChineseCornerFragment extends Fragment implements View.OnClickListener {
    TextView continueBtn;
    Context context;
    RecyclerView recyclerView;
    RelativeLayout rl_order_now;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference pizzas;
    FirebaseRecyclerAdapter adapter;
    TextView productImage;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pizza_fragment_screen, container, false);
        context = Objects.requireNonNull(container).getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        pizzas=firebaseDatabase.getReference(Common.CHINESE_CORNER);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        rl_order_now = view.findViewById(R.id.rl_order_now);
        rl_order_now.setOnClickListener(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        showOurOffers();

    }

    private void showOurOffers() {

        FirebaseRecyclerOptions<ChineseCornerInfo> options = new FirebaseRecyclerOptions.Builder<ChineseCornerInfo>().setQuery(
                pizzas.orderByChild("id"), ChineseCornerInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<ChineseCornerInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final ChineseCornerInfo model) {
                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.name);
                itemName.setText(model.getItemName());
                TextView itemPrice = holder.itemView.findViewById(R.id.price);

                TextView priceWas = holder.itemView.findViewById(R.id.priceWas);
                RelativeLayout relativeLayout=holder.itemView.findViewById(R.id.wasPriceRL);
                itemPrice.setVisibility(View.VISIBLE);
                ImageView discountIcon=holder.itemView.findViewById(R.id.discountImg);

                if (model.getDiscount()>0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    itemPrice.setText("RS."+model.getDiscount());
                    priceWas.setText("RS."+model.getPrice());
                    discountIcon.setVisibility(View.VISIBLE);
                }else {
                    relativeLayout.setVisibility(View.GONE);
                    itemPrice.setText("RS."+model.getPrice());
                    discountIcon.setVisibility(View.GONE);

                }

                ImageView itemImage=holder.itemView.findViewById(R.id.image);
                if (model.getDiscount()>0){
                    discountIcon.setVisibility(View.VISIBLE);
                }
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);




                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //showActonDialog(adapter,model.getId());
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

    @Override
    public void onClick(View v) {
        if (v == rl_order_now){
            Log.i("PPPP", "onClick: Order now");
            setNextPhoneFragment();
        }
    }

    private void setNextPhoneFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ShowChineseCornerFragment hf = (ShowChineseCornerFragment) fm.findFragmentByTag("ShowChineseCornerFragment");
            if (hf == null) {
                hf = new ShowChineseCornerFragment();
                ft.replace(R.id.main_frame, hf, "ShowChineseCornerFragment");
                ft.addToBackStack("ShowChineseCornerFragment");
            }else{

                ft.replace(R.id.main_frame, hf, "ShowChineseCornerFragment");
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