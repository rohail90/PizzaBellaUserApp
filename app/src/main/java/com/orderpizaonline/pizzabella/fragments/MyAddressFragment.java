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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.AddressInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Session;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;


public class MyAddressFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userAddresses;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SwipeRefreshLayout swipeRefreshLayout;
    AddressInfo addressInfo=new AddressInfo();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.my_addresses, container, false);
        context = container.getContext();

        ((MainActivity) context).setToolbarTitle("My Addresses");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if (Session.getUserInfo() != null) {
            userAddresses = firebaseDatabase.getReference(Common.USERS).child(Session.getUserInfo().getId()).child(Common.UserAddresses);
        }
        //userAddresses.child(Session.getUserInfo().getId()).child(Common.UserAddresses)
       // swipeRefreshLayout = v.findViewById(R.id.refreshFood);
        recyclerView =v.findViewById(R.id.myAddresses);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
      /*  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);*/
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextFragment("","","","");
            }
        });
       /* swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    adapter.stopListening();
                    showPizzas();
                    adapter.startListening();
                    swipeRefreshLayout.setRefreshing(false);
            }
        });*/
        showAddresses();

        return v;
    }
    private void deleteFood(String key) {
        userAddresses.child(key).removeValue();
    }

    @Override
    public void onClick(View v) {

    }

    private void showAddresses() {
        FirebaseRecyclerOptions<AddressInfo> options = new FirebaseRecyclerOptions.Builder<AddressInfo>().setQuery(
                userAddresses, AddressInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<AddressInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final AddressInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView itemName = holder.itemView.findViewById(R.id.address);
                itemName.setText(model.getHouseNo()+","+model.getStreet()+","+model.getArea());
                ImageView editBn = holder.itemView.findViewById(R.id.editBtn);
                editBn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setNextFragment(model.getId(),model.getHouseNo(),model.getStreet(),model.getArea());
                    }
                });
                ImageView deleteBtn=holder.itemView.findViewById(R.id.deleteBtn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFood(model.getId());
                    }
                });



                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                     //   showActonDialog(adapter,model.getId(),model);
                        //Sending food_id to FoodDetailActivity
//                        Intent intent = new Intent(FoodActivityServer.this, FoodDetailActivity.class);
//                        intent.putExtra("foodId", adapter.getRef(position).getKey());
//                        startActivity(intent);;

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    private void setNextFragment(String s,  String houseNo, String street, String area) {
        try {
          addAddressDialog(s,houseNo,street,area);
        } catch (Exception e) {
            Log.d("EXC", "AddSidelinesFragment: " + e.getMessage());
        }
    }
    private void addAddressDialog(final String key, final String houseNo, String street, String area) {
        try {
            final View dialogView = View.inflate(context, R.layout.add_new_addresses, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Button update = dialogView.findViewById(R.id.saveAddressBtn);
            final EditText houseNoEt=dialogView.findViewById(R.id.houseNo);
            final EditText streetEt=dialogView.findViewById(R.id.street);
            final EditText areaEt=dialogView.findViewById(R.id.area);
            if (houseNo.equals("")||street.equals("") || area.equals("") || key==null || key.equals("")){
            }else {
                update.setText("Update Address");
                houseNoEt.setText(houseNo);
                streetEt.setText(street);
                areaEt.setText(area);
               /* addressInfo.setArea(areaEt.getText().toString());
                addressInfo.setHouseNo(houseNoEt.getText().toString());
                addressInfo.setStreet(streetEt.getText().toString());
                addressInfo.setId(key);*/
            }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (houseNoEt.getText().toString().trim().equals("") || streetEt.getText().toString().trim().equals("") || areaEt.getText().toString().trim().equals("")){
                            Toast.makeText(context,"PLease provide all info",Toast.LENGTH_SHORT).show();
                        }else {
                            addressInfo.setArea(areaEt.getText().toString());
                            addressInfo.setHouseNo(houseNoEt.getText().toString());
                            addressInfo.setStreet(streetEt.getText().toString());

                            if (key==null ||key.equals("")){
                                String path = userAddresses.push().getKey();

                                //String path = userAddresses.push().getKey();
                                addressInfo.setId(path);
                                userAddresses.child(path).setValue(addressInfo);
                                Snackbar.make(recyclerView, "Item Added successfully", Snackbar.LENGTH_SHORT).show();
                            }else {
                                userAddresses.child(key).setValue(addressInfo);
                                Snackbar.make(recyclerView, "Item Updated successfully", Snackbar.LENGTH_SHORT).show();

                            }
                            alertDialog.dismiss();

                        }
                        Log.d("TAG", "onContextItemSelected: "+key);

                    }catch (Exception e){
                        Log.d("TAG", "exception: "+e.getMessage());
                    }


                }
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
        }catch (Exception e){
            Log.d("TAG", "exception in addAddressDialog: "+e.getMessage());
        }

    }
}