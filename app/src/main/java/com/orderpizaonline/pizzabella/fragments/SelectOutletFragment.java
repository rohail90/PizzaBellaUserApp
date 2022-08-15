package com.orderpizaonline.pizzabella.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.interfaces.ItemClickListener;
import com.orderpizaonline.pizzabella.model.StoreInfo;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.viewholders.VegetablesViewHolder;

import static com.orderpizaonline.pizzabella.MainActivity.selectionModel;


public class SelectOutletFragment extends Fragment implements View.OnClickListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference storeAddresses;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView titleTv;
    static Location currentLocation;
    public static String uriForNavigation="";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest request;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.i("LOC", "onLocationResult: ");
            if (locationResult == null) {
                Log.i("LOC", "onLocationResult: NUll so getLocationFromNetwork calling");
                return;
            }
            currentLocation = locationResult.getLastLocation();
            /*storeInfo.setLat(currentLocation.getLatitude());
            storeInfo.setLng(currentLocation.getLongitude());*/
            Log.i("LOC", "getLastLocation  " + locationResult.getLastLocation().getLongitude() + "  " + locationResult.getLastLocation().getLatitude());
            Log.i("TAG", "onLocationResult: lat " + currentLocation.getLongitude() + " lng" + currentLocation.getLatitude());

            //if (buyerMarker == null) {
            Log.i("LOC", "yes stratMarker is null: ");

            if (currentLocation != null) {

              /*  String uri = "http://maps.google.com/maps?saddr=" + currentLocation.getLatitude()+","+currentLocation.getLongitude();
                uriForNavigation = uri;*/
                Log.d("ADDRESS", "getting Address onLocationResult: ");
               /* BranchesFragment.GetAddressTask async = new BranchesFragment.GetAddressTask(context, currentLocation.getLatitude(), currentLocation.getLongitude());
                async.execute();*/
            }


            /*if (mMap != null) {
                Log.i("LOC", "yes mMap is Not null: ");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), 15));
                //buyerMarker = mMap.addMarker(new MarkerOptions().position(startLocation).title("Buyer location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                if (storeMarker==null){
                    storeMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(storeInfo.getLat(), storeInfo.getLng()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                            .title("Your Store Location"));
                }else {
                    storeMarker.setPosition(new LatLng(storeInfo.getLat(), storeInfo.getLng()));
                }
            }*/

           /* String latEiffelTower = "48.858235";
            String lngEiffelTower = "2.294571";
            String url = "http://maps.google.com/maps/api/staticmap?center=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&zoom=15&size=200x200&sensor=false&key=AIzaSyAArAN6PAtMJ68K-vkWqoq9Csog-m87Tic";
            Picasso.get().load(url).into(staticMAp);*/

            Location loc = locationResult.getLastLocation();
            Log.i("TAG", loc.getLatitude() + "==abc== " + loc.getLongitude());
            try {
                stopLocationUpdates();

            } catch (Exception e) {

            }

            //  }
        }
    };
    private void startLocationUpdate() {
      /*  if ( operationRegionsResponseArrayList==null){
            progressDialog.show();
        }*/

        Log.i("LOC", "in startLocationUpdate: ");
        //address.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(request, mLocationCallback, null);


    }

    public void stopLocationUpdates() {

        //address.setVisibility(View.VISIBLE);
        //send.setOnClickListener(this);
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.select_outlet_activity, container, false);
        context = container.getContext();
//        titleTv=v.findViewById(R.id.title);
//        titleTv.setText("Manage Your Branches");
        ((MainActivity) context).setToolbarTitle("Outlets");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        storeAddresses = firebaseDatabase.getReference(Common.StoreAddresses);
        request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(3000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        startLocationUpdate();
       // swipeRefreshLayout = v.findViewById(R.id.refreshFood);
        recyclerView =v.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
      /*  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);*/

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
        storeAddresses.child(key).removeValue();
    }

    @Override
    public void onClick(View v) {

    }
    private void showAddresses() {
        FirebaseRecyclerOptions<StoreInfo> options = new FirebaseRecyclerOptions.Builder<StoreInfo>().setQuery(
                storeAddresses, StoreInfo.class).build();

        adapter = new FirebaseRecyclerAdapter<StoreInfo, VegetablesViewHolder>(options) {
            @NonNull
            @Override
            public VegetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_outlet_item, parent, false);
                return new VegetablesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull VegetablesViewHolder holder, int position, @NonNull final StoreInfo model) {

                final String mealID=adapter.getRef(position).getKey();
                TextView address = holder.itemView.findViewById(R.id.address);
                RadioButton selection = holder.itemView.findViewById(R.id.selectAddressRb);
                address.setText(model.getLongAddress());
                TextView outletTiming = holder.itemView.findViewById(R.id.outletTimings);
                TextView outletStatus = holder.itemView.findViewById(R.id.outletStatus);
                outletTiming.setText(model.getStoreTiming());
                if (model.getStoreStatus()) {
                    outletStatus.setText("Open");
                    outletStatus.setBackground(context.getResources().getDrawable(R.drawable.green_filled_shape));
                }else {
                    outletStatus.setText("Closed");
                    outletStatus.setBackground(context.getResources().getDrawable(R.drawable.red_filled_shape));
                }

                selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (model.getStoreStatus()) {
                            selectionModel.setOutletAddress(model.getLongAddress());
                            selectionModel.setOutletAddressSelected(true);
                            selectionModel.setOutletId(model.getId());
                            selectionModel.setOutletToken(model.getStoreFCM());
                            FragmentManager fm = getActivity().getSupportFragmentManager();

                            if (fm.getBackStackEntryCount() >= 1) {
                                fm.popBackStack();
                            }
                        }else {
                            Toast.makeText(context, "Outlet Closed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        if (model.getStoreStatus()) {
                            selectionModel.setOutletAddress(model.getLongAddress());
                            selectionModel.setOutletAddressSelected(true);
                            selectionModel.setOutletId(model.getId());
                            selectionModel.setOutletToken(model.getStoreFCM());
                            FragmentManager fm = getActivity().getSupportFragmentManager();

                            if (fm.getBackStackEntryCount() >= 1) {
                                fm.popBackStack();
                            }
                        }else {
                            Toast.makeText(context, "Outlet Closed", Toast.LENGTH_SHORT).show();
                        }
                        /*selectionModel.setOutletAddress(model.getLongAddress());
                        selectionModel.setOutletAddressSelected(true);
                        selectionModel.setOutletId(model.getId());
                        selectionModel.setOutletToken(model.getStoreFCM());

                        FragmentManager fm = getActivity().getSupportFragmentManager();

                        if (fm.getBackStackEntryCount() >= 1) {
                            fm.popBackStack();
                        }*/
                       //showActonDialog(model.getId());
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
    private void makeCallWithStore(String phoneNo) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        String p = "tel:" + phoneNo;
        Log.d("PHONE", "Seller Phone no: "+p);
        i.setData(Uri.parse(p));
        startActivity(i);
    }
    private void setNextFragment(String s) {
     /*   try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            AddBranchAddressFragment hf = (AddBranchAddressFragment) fm.findFragmentByTag(Common.Add_Branches_F_TAG);
            if (hf == null) {
                hf = new AddBranchAddressFragment();
                ft.replace(R.id.main_frame, hf, Common.Add_Branches_F_TAG);
                ft.addToBackStack(Common.Add_Branches_F_TAG);
            }else {
                ft.replace(R.id.main_frame, hf, Common.Add_Branches_F_TAG);
            }
            if (s.equals("")){

            }else {
                Bundle bundle=new Bundle();
                bundle.putString("item_key",s);
                hf.setArguments(bundle);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception Add_BranchesFragment: " + e.getMessage());
        }*/
    }

    private void showActonDialog( final String key) {
        /*try {
            final View dialogView = View.inflate(context, R.layout.select_option_dialog, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView update = dialogView.findViewById(R.id.updateItem);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((MainActivity)context).setToolbarTitle("Update Store Address");
                        Log.d("TAG", "onContextItemSelected: "+key);
                        setNextFragment(key);
                    }catch (Exception e){
                        Log.d("TAG", "exception: "+e.getMessage());
                    }
                    alertDialog.dismiss();

                }
            });
            dialogView.findViewById(R.id.deleteItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteFood(key);
                    alertDialog.dismiss();

                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        }catch (Exception e){
            Log.d("TAG", "exception in showActonDialog: "+e.getMessage());
        }
*/
    }
}