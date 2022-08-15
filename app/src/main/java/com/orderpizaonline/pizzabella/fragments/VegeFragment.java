package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.VegeInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;


public class VegeFragment extends Fragment implements View.OnClickListener {
    TextView continueBtn;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference vegetables;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    private Button btnUpload;
    TextView productImage;
    private Button btnSelect;
    private static final int IMAGE_REQUEST_CODE = 100;
    private Uri saveUri;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pizza_fragment_screen, container, false);
        context = container.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        vegetables = firebaseDatabase.getReference(Common.VEGETABLES);

        recyclerView =v.findViewById(R.id.recycler_view);
        /*recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextFragment();
            }
        });
        showOurOffers();

        return v;
    }

    @Override
    public void onClick(View v) {

    }
    private void showOurOffers() {
        FirebaseRecyclerOptions<VegeInfo> options = new FirebaseRecyclerOptions.Builder<VegeInfo>().setQuery(
                vegetables.orderByChild("id"), VegeInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<VegeInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vege_spinner_items, parent, false);
                return new VegetablesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final VegeInfo model) {

                final String mealID=adapter.getRef(position).getKey();

                TextView itemName = holder.itemView.findViewById(R.id.vegeName);
                itemName.setText(model.getVegeName());

                ImageView itemImage=holder.itemView.findViewById(R.id.vegeImage);
//                Picasso.get().load(model.getImageURL()).into(itemImage);
                Glide.with(context).load(model.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.loading).into(itemImage);

                RadioButton itemSelectionImage=holder.itemView.findViewById(R.id.selectionImage);
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

    private void setNextFragment() {
        try {
          /*  FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            AddVegeFragment hf = (AddVegeFragment) fm.findFragmentByTag(Common.Add_VEGETABLES_F_TAG);
            if (hf == null) {
                hf = new AddVegeFragment();
                ft.replace(R.id.main_frame, hf, Common.Add_VEGETABLES_F_TAG);
                ft.addToBackStack(Common.Add_VEGETABLES_F_TAG);
            }else {
                ft.replace(R.id.main_frame, hf, Common.Add_VEGETABLES_F_TAG);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();*/
        } catch (Exception e) {
            Log.d("EXC", "AddVegeFragment: " + e.getMessage());
        }


    }


}