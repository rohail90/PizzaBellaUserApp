
package com.orderpizaonline.pizzabella;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.facebook.AccessToken;
//import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.orderpizaonline.pizzabella.adapter.HomeAdapter;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.fragments.BaveragesFragment;
import com.orderpizaonline.pizzabella.fragments.BurgSandFragment;
import com.orderpizaonline.pizzabella.fragments.CartCheckoutFragment;
import com.orderpizaonline.pizzabella.fragments.ChangePasswordFragment;
import com.orderpizaonline.pizzabella.fragments.ChineseCornerFragment;
import com.orderpizaonline.pizzabella.fragments.ContactUsFragment;
import com.orderpizaonline.pizzabella.fragments.DealsFragment;
import com.orderpizaonline.pizzabella.fragments.FriedRollFragment;
import com.orderpizaonline.pizzabella.fragments.MealsFragment;
import com.orderpizaonline.pizzabella.fragments.MyAddressFragment;
import com.orderpizaonline.pizzabella.fragments.OrdersFragment;
import com.orderpizaonline.pizzabella.fragments.PizzaFragment;
import com.orderpizaonline.pizzabella.fragments.ProfileFragment;
import com.orderpizaonline.pizzabella.fragments.ShowComboFragment;
import com.orderpizaonline.pizzabella.fragments.ShowPizzasFragment;
import com.orderpizaonline.pizzabella.fragments.SidelinesDessertsFragment;
import com.orderpizaonline.pizzabella.fragments.StoreBranchesFragment;
import com.orderpizaonline.pizzabella.fragments.SummerFragment;
import com.orderpizaonline.pizzabella.fragments.VegeFragment;
import com.orderpizaonline.pizzabella.fragments.WinterFragment;
import com.orderpizaonline.pizzabella.functions.ICloudFunctions;
import com.orderpizaonline.pizzabella.functions.RetrofitICloudClient;
import com.orderpizaonline.pizzabella.model.BraintreeToken;
import com.orderpizaonline.pizzabella.model.BurgSandOrderInfo;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ColdPizModel;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfoBella;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.SelectionModel;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.ui.FinishThankYouActivity;
import com.orderpizaonline.pizzabella.ui.LoginActivity;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Constants;
import com.orderpizaonline.pizzabella.utils.Session;

import com.orderpizaonline.pizzabella.utils.BuildConfig;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    ImageView iv_drawer, iv_notification_orange;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout tabLayout;
    LinearLayout nav_logout;
    TextView toolbarTitle, tv_cart_count, tv_contact, tv_name;
    LinearLayout nav_my_address, privacy_policy, nav_share_app, nav_my_profile, nav_contactus, nav_mapview, nav_create_pizza, nav_order_combos, nav_order_sidelines, nav_change_password, nav_my_orders;
    RelativeLayout nav_pizza_menu;
    public static PizzaOrderInfo pizzaOrderInfoModel = new PizzaOrderInfo();
    public static int orderTotalPrice = 0;
    public static PizzaOrderInfoBella pizzaOrderInfoBella = new PizzaOrderInfoBella();
    public static List<SidelineOrderInfo> sidelinesOrderList = new ArrayList<>();
    public static SidelineOrderInfo sidelinesOrderInfoBella = new SidelineOrderInfo();
    public static ChineseCornerOrderInfo chineseCornerInfoBella = new ChineseCornerOrderInfo();
    public static BurgSandOrderInfo burgSandOrderInfoBella = new BurgSandOrderInfo();
    public static SelectionModel selectionModel = new SelectionModel();
    public static FriedRollOrderInfo friedRollOrderInfoBella = new FriedRollOrderInfo();
    public static ComboOrderInfo comboOrderInfoBella = new ComboOrderInfo();

    public static PairValuesModel selectColdPizItm = new PairValuesModel();
    public static PairValuesModel selectPizItm = new PairValuesModel();
    public static List<ColdPizModel> finalColdPizList = new ArrayList<>();
    public static List<ColdPizModel> finalPizList = new ArrayList<>();
    public static List<PizzaToppingInfo> finalPizFlavour = new ArrayList<>();

    //new
    Dialog rateus_dialog;
    TextView rateus_noText;
    RelativeLayout rl_below;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ICloudFunctions iCloudFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        iCloudFunctions = RetrofitICloudClient.getInstance().create(ICloudFunctions.class);
        updateBrainTreeToken();

        Date currentTime = Calendar.getInstance().getTime();
        int hours = currentTime.getHours();
        int minutes = currentTime.getMinutes();
        if (hours == 23 && minutes >= 45) {
            showScheduleDialog();
        } else if (hours == 0 && minutes <= 5) {
            showScheduleDialog();
        } else {

            new Session(this);
            setToolbarTitle("Pizza Menu");
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.red));
            }
            tv_name = findViewById(R.id.tv_name);
            tv_contact = findViewById(R.id.tv_contact);
            if (Session.getUserInfo() != null) {
                tv_name.setText(Session.getUserInfo().getName());
                tv_contact.setText(Session.getUserInfo().getPhoneNumber());
            }

            if (!haveNetworkConnection()) {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }

            nav_pizza_menu = findViewById(R.id.nav_pizza_menu);
            nav_create_pizza = findViewById(R.id.nav_create_pizza);
            nav_order_combos = findViewById(R.id.nav_order_combos);
            nav_order_sidelines = findViewById(R.id.nav_order_sidelines);
            nav_change_password = findViewById(R.id.nav_change_password);
            nav_my_orders = findViewById(R.id.nav_my_orders);
            nav_share_app = findViewById(R.id.nav_share_app);
            privacy_policy = findViewById(R.id.privacy_policy);
            nav_pizza_menu.setOnClickListener(this);
            nav_create_pizza.setOnClickListener(this);
            nav_order_combos.setOnClickListener(this);
            nav_order_sidelines.setOnClickListener(this);
            nav_change_password.setOnClickListener(this);
            nav_my_orders.setOnClickListener(this);
            nav_share_app.setOnClickListener(this);
            privacy_policy.setOnClickListener(this);


            nav_my_address = findViewById(R.id.nav_my_address);

            nav_my_address.setOnClickListener(this);
            nav_my_profile = findViewById(R.id.nav_my_profile);
            nav_my_profile.setOnClickListener(this);
            nav_contactus = findViewById(R.id.nav_contactus);
            nav_contactus.setOnClickListener(this);
            nav_mapview = findViewById(R.id.nav_mapview);
            nav_mapview.setOnClickListener(this);


            drawer = findViewById(R.id.drawer_layout);
            iv_drawer = findViewById(R.id.side_menu);
            navigationView = findViewById(R.id.nav_view);
            nav_logout = findViewById(R.id.nav_logout);
            nav_logout.setOnClickListener(this);
            iv_notification_orange = findViewById(R.id.iv_notification_orange);
            iv_notification_orange.setOnClickListener(this);

            iv_drawer.setOnClickListener(this);
            pager = findViewById(R.id.pager);
            HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());
            pager.setAdapter(adapter);
            tabLayout = findViewById(R.id.sliding_tabs);
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setupWithViewPager(pager);
            setupViewPager(pager);


            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    if (getIntent().getBooleanExtra("showCart", false)) {
                        setCartFragment();
                    }
                }
            }
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    if (getIntent().getBooleanExtra("showPizza", false)) {
                        setCreatePizzaFragment();
                    }
                }
            }
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    if (getIntent().getBooleanExtra("showOrders", false)) {
                        setOrdersFragment();
                    }
                }
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel =
                        new NotificationChannel("MyNotification", "MyNotification",
                                NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);

            }

            //setNextFragment();
        }


    }

    private void updateBrainTreeToken() {
        compositeDisposable.add(iCloudFunctions.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BraintreeToken>() {
                    @Override
                    public void accept(BraintreeToken braintreeToken) throws Exception {
                        Paper.book().write(Constants.BrainTreeToken, braintreeToken.getToken());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void showScheduleDialog() {
        rateus_dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar);
        rateus_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateus_dialog.setCancelable(false);
        rateus_dialog.setContentView(R.layout.schedule_dialog);

        rateus_dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
            }
            return false;
        });
        rateus_dialog.show();
    }

    public void cartSize(int count) {
        tv_cart_count = findViewById(R.id.tv_cart_count);
        tv_cart_count.setText(count + "");
    }

    private void setupViewPager(ViewPager viewPager) {
        HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());
        adapter.addFragment(new PizzaFragment(), "PIZZA");
        adapter.addFragment(new DealsFragment(), "DEALS");
        adapter.addFragment(new MealsFragment(), "MEALS");
        adapter.addFragment(new BurgSandFragment(), "BURGER/SANDWICHES");
        adapter.addFragment(new FriedRollFragment(), "FRIES/ROLL");
        adapter.addFragment(new ChineseCornerFragment(), "KIDS MANIA");
        adapter.addFragment(new BaveragesFragment(), "BEVERAGES");
        viewPager.setAdapter(adapter);
    }

    private void setNextFragment() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            VegeFragment hf = (VegeFragment) fm.findFragmentByTag(Common.VEGE_F_TAG);
            if (hf == null) {
                hf = new VegeFragment();
                ft.replace(R.id.main_frame, hf, Common.VEGE_F_TAG);
                ft.addToBackStack(Common.VEGE_F_TAG);
            } else {
                ft.replace(R.id.main_frame, hf, Common.VEGE_F_TAG);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " " + Common.VEGE_F_TAG + e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        if (v == iv_drawer) {
            drawer.openDrawer(Gravity.LEFT);
        } else if (v == nav_logout) {
            Session.setLoginState(false);
            try {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut();
//                AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//                if (isLoggedIn){
//                    LoginManager.getInstance().logOut();
//                }
            } catch (Exception e) {
                Log.d("USER", "exception onClick: logoutLL ");
            }
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v == nav_my_address) {
            setMyAddressFragment();
            closeDrawer();
        } else if (v == nav_my_profile) {
            setMyProfileFragment();
            closeDrawer();
        } else if (v == nav_contactus) {
            setContactUsFragment();
            closeDrawer();
        } else if (v == nav_change_password) {
            setChangePasswordFragment();
            closeDrawer();
        } else if (v == nav_my_orders) {
            setOrdersFragment();
            closeDrawer();
        } else if (v == nav_mapview) {
            setStoreBranchesFragment();
            closeDrawer();
        } else if (v == nav_create_pizza) {
            setCreatePizzaFragment();
            closeDrawer();
        } else if (v == nav_order_combos) {
            setOrderComboFragment();
            closeDrawer();
        } else if (v == nav_share_app) {

            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("text/plain");
            intent2.putExtra("android.intent.extra.SUBJECT", MainActivity.this.getString(R.string.app_name));
            intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + BuildConfig.applicationId);
            MainActivity.this.startActivity(Intent.createChooser(intent2, "Choose"));
            closeDrawer();


        } else if (v == privacy_policy) {

            try {
                MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.privacy_policy_url))));
            } catch (Exception ignored) {
            }

        } else if (v == nav_order_sidelines) {
            setOrderSidelinesFragment();
            closeDrawer();
        } else if (v == nav_pizza_menu) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() >= 1) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            }
            closeDrawer();
        } else if (v == iv_notification_orange) {
            try {
                sidelinesOrderList.clear();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                CartCheckoutFragment hf = (CartCheckoutFragment) fm.findFragmentByTag("CartCheckoutFragment");
                if (hf == null) {
                    hf = new CartCheckoutFragment();
                }
                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack("CartCheckoutFragment");
                ft.commit();
            } catch (Exception e) {
                Log.d("EXC", "setNextPhoneFragment: " + e.getMessage());
            }
        }
    }

    private void closeDrawer() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawers();
        }
    }

    private void setMyAddressFragment() {
        try {

            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "MyAddressFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("MyAddressFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            MyAddressFragment hf = (MyAddressFragment) fm.findFragmentByTag("MyAddressFragment");
            if (hf == null) {
                hf = new MyAddressFragment();
                ft.replace(R.id.main_frame, hf, "MyAddressFragment");
                ft.addToBackStack("MyAddressFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "MyAddressFragment");
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

    private void setMyProfileFragment() {
        try {

            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "ProfileFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("ProfileFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            ProfileFragment hf = (ProfileFragment) fm.findFragmentByTag("ProfileFragment");
            if (hf == null) {
                hf = new ProfileFragment();
                ft.replace(R.id.main_frame, hf, "ProfileFragment");
                ft.addToBackStack("ProfileFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "ProfileFragment");
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

    private void setContactUsFragment() {
        try {

            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "ContactUsFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("ContactUsFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            ContactUsFragment hf = (ContactUsFragment) fm.findFragmentByTag("ContactUsFragment");
            if (hf == null) {
                hf = new ContactUsFragment();
                ft.replace(R.id.main_frame, hf, "ContactUsFragment");
                ft.addToBackStack("ContactUsFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "ContactUsFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }
    }

    private void setChangePasswordFragment() {
        try {

            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "ChangePasswordFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("ChangePasswordFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            ChangePasswordFragment hf = (ChangePasswordFragment) fm.findFragmentByTag("ChangePasswordFragment");
            if (hf == null) {
                hf = new ChangePasswordFragment();
                ft.replace(R.id.main_frame, hf, "ChangePasswordFragment");
                ft.addToBackStack("ChangePasswordFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "ChangePasswordFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }
    }

    private void setStoreBranchesFragment() {
        try {

            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "StoreBranchesFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("StoreBranchesFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            StoreBranchesFragment hf = (StoreBranchesFragment) fm.findFragmentByTag("StoreBranchesFragment");
            if (hf == null) {
                hf = new StoreBranchesFragment();
                ft.replace(R.id.main_frame, hf, "StoreBranchesFragment");
                ft.addToBackStack("StoreBranchesFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "StoreBranchesFragment");
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

    private void setCreatePizzaFragment() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "ShowPizzasFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("ShowPizzasFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            ShowPizzasFragment hf = (ShowPizzasFragment) fm.findFragmentByTag("ShowPizzasFragment");
            if (hf == null) {
                hf = new ShowPizzasFragment();
                ft.replace(R.id.main_frame, hf, "ShowPizzasFragment");
                ft.addToBackStack("ShowPizzasFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "ShowPizzasFragment");
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

    private void setOrderComboFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (isFragmentInBackstack(fm, "ShowComboFragment")) {
            // Fragment exists, go back to that fragment
            //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
            fm.popBackStackImmediate("ShowComboFragment", 0);

        }
        try {

            FragmentTransaction ft = fm.beginTransaction();
            ShowComboFragment hf = (ShowComboFragment) fm.findFragmentByTag("ShowComboFragment");
            if (hf == null) {
                hf = new ShowComboFragment();
                ft.replace(R.id.main_frame, hf, "ShowComboFragment");
                ft.addToBackStack("ShowComboFragment");
            } else {
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

    private void setOrderSidelinesFragment() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "SidelinesDessertsFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("SidelinesDessertsFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            SidelinesDessertsFragment hf = (SidelinesDessertsFragment) fm.findFragmentByTag("SidelinesDessertsFragment");
            if (hf == null) {
                hf = new SidelinesDessertsFragment();
                ft.replace(R.id.main_frame, hf, "SidelinesDessertsFragment");
                ft.addToBackStack("SidelinesDessertsFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "SidelinesDessertsFragment");
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

    private void setOrdersFragment() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "OrdersFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("OrdersFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            OrdersFragment hf = (OrdersFragment) fm.findFragmentByTag("OrdersFragment");
            if (hf == null) {
                hf = new OrdersFragment();
                ft.replace(R.id.main_frame, hf, "OrdersFragment");
                ft.addToBackStack("OrdersFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "OrdersFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }
    }

    public static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }

    public void setCartFragment() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (isFragmentInBackstack(fm, "CartCheckoutFragment")) {
                // Fragment exists, go back to that fragment
                //// you can also use POP_BACK_STACK_INCLUSIVE flag, depending on flow
                fm.popBackStackImmediate("CartCheckoutFragment", 0);

            }
            FragmentTransaction ft = fm.beginTransaction();
            CartCheckoutFragment hf = (CartCheckoutFragment) fm.findFragmentByTag("CartCheckoutFragment");
            if (hf == null) {
                hf = new CartCheckoutFragment();
                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
                ft.addToBackStack("CartCheckoutFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "CartCheckoutFragment");
            }

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }


    }

    @Override
    public void onBackPressed() {
        closeDrawer();

        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
            //super.onBackPressed();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are You Sure To Exit App?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showGoodBye();
                    finishAffinity();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void showGoodBye() {
        MainActivity.this.startActivity(new Intent(MainActivity.this, FinishThankYouActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        sidelinesOrderList.clear();
        setToolbarTitle("Pizza Menu");
        int tmp = 0;
        List<PizzaOrderCartModel> poi = new DBClass(MainActivity.this).getPizzas();
        List<SidelineOrderInfo> soi = new DBClass(MainActivity.this).getSideslines();
        if (poi != null) {
            if (poi.size() > 0) {
                tmp += poi.size();
            }
        }
        if (soi != null) {
            if (soi.size() > 0) {
                tmp += soi.size();
            }
        }
        cartSize(tmp);

        pizzaOrderInfoModel = null;
        pizzaOrderInfoModel = new PizzaOrderInfo();

        pizzaOrderInfoBella = null;
        pizzaOrderInfoBella = new PizzaOrderInfoBella();
        super.onResume();
    }

    public void setToolbarTitle(String s) {
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("" + s);
    }
}