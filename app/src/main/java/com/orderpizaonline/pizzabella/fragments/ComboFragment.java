package com.orderpizaonline.pizzabella.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.orderpizaonline.pizzabella.model.PizzaOrderInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoModel;


public class ComboFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference combos;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    RelativeLayout rl_order_now;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pizza_fragment_screen, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        combos = firebaseDatabase.getReference(Common.COMBO);
        recyclerView =v.findViewById(R.id.recycler_view);
        rl_order_now =v.findViewById(R.id.rl_order_now);
        rl_order_now.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
      /*  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);*/
        showCombos();

        return v;
    }

    private void deleteFood(String key) {
        combos.child(key).removeValue();
    }

    @Override
    public void onClick(View v) {
        if (v == rl_order_now){
            setNextFragment();
        }
    }
    private void showActonDialog(final FirebaseRecyclerAdapter adapter, final String key) {
        try {
            final View dialogView = View.inflate(context, R.layout.show_combo_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button noBtn = dialogView.findViewById(R.id.button);
            Button yesBtn = dialogView.findViewById(R.id.btn_yes);
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.dismiss();

                }
            });
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // add to list
                    alertDialog.dismiss();

                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        }catch (Exception e){
            Log.d("TAG", "exception in showActonDialog: "+e.getMessage());
        }

    }

    private void showCombos() {
        FirebaseRecyclerOptions<ComboInfo> options = new FirebaseRecyclerOptions.Builder<ComboInfo>().setQuery(
                combos.orderByChild("id"), ComboInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<ComboInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_list_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final ComboInfo model) {

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
    private void setNextFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ShowComboFragment hf = (ShowComboFragment) fm.findFragmentByTag("ShowComboFragment");
            if (hf == null) {
                hf = new ShowComboFragment();
                ft.replace(R.id.main_frame, hf, "ShowComboFragment");
                ft.addToBackStack("ShowComboFragment");
            }else {
                ft.replace(R.id.main_frame, hf, "ShowComboFragment");
            }
            /*if (s.equals("")){

            }else {
                Bundle bundle=new Bundle();
                bundle.putString("item_key",s);
                hf.setArguments(bundle);
            }*/
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
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