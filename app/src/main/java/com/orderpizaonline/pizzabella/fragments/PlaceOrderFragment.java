package com.orderpizaonline.pizzabella.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.GooglePayment;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.orderpizaonline.pizzabella.MainActivity;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.db.DBClass;
import com.orderpizaonline.pizzabella.functions.ICloudFunctions;
import com.orderpizaonline.pizzabella.functions.RetrofitICloudClient;
import com.orderpizaonline.pizzabella.model.BrainTreeTransaction;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaSidelinesDBModel;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.model.TransactionModel;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.model.addOrder;
import com.orderpizaonline.pizzabella.ui.ThankYouActivity;
import com.orderpizaonline.pizzabella.utils.Common;
import com.orderpizaonline.pizzabella.utils.Constants;
import com.orderpizaonline.pizzabella.utils.SendNotificationTask;
import com.orderpizaonline.pizzabella.utils.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;
import static com.orderpizaonline.pizzabella.MainActivity.orderTotalPrice;
import static com.orderpizaonline.pizzabella.MainActivity.selectionModel;


public class PlaceOrderFragment extends Fragment implements View.OnClickListener, PaymentMethodNonceCreatedListener, BraintreeErrorListener {
    Context context;
    FirebaseDatabase firebaseDatabase;
    FirebaseRecyclerAdapter adapter;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    LinearLayout ll_select_outlet, ll_add_new_address;
    EditText et_userName, et_userPhone, et_orderNotes, et_discountedCode;
    Button placeOrderBtn;
    TextView outletAddress, createNewAddress;

    RadioGroup radioGroup;
    RadioButton btnCashOnDelivery, btnOnlinePayment;
    ICloudFunctions cloudFunctions;
    PizzaSidelinesDBModel brainTreeOrder;
    BraintreeFragment brainTreeFragment;
    DropInRequest dropInRequest;
    private static final int REQUEST_BRAINTREE_CODE = 1221;
    private static final int GOOGLE_PAYMENT_REQUEST_CODE = 1222;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public List<SidelineOrderInfo> orders;
    public List<PizzaOrderCartModel> orderPizzas;
    public List<ChineseCornerOrderInfo> orderChinese;
    public List<FriedRollOrderInfo> orderFried;
    public List<ComboOrderInfo> orderMeal;
    public List<ComboOrderInfo> orderDeal;

    DatabaseReference ordersReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.add_address_info_screen, container, false);

        context = container.getContext();
        ((MainActivity) context).setToolbarTitle("Checkout");

        cloudFunctions = RetrofitICloudClient.getInstance().create(ICloudFunctions.class);
        initView(v);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        ordersReference = firebaseDatabase.getReference(Common.ORDERS);

        outletAddress = v.findViewById(R.id.outletAddress);
        createNewAddress = v.findViewById(R.id.createNewAddress);
        if (selectionModel != null) {
            if (selectionModel.getMyAddress() != null && !selectionModel.getMyAddress().equals("")) {
                createNewAddress.setText(selectionModel.getMyAddress());
            }
            if (selectionModel.getOutletAddress() != null && !selectionModel.getOutletAddress().equals("")) {
                outletAddress.setText(selectionModel.getOutletAddress());
            }
        }

        ll_select_outlet = v.findViewById(R.id.selectOutletAddressLL);
        ll_add_new_address = v.findViewById(R.id.createNewAddressLL);
        et_discountedCode = v.findViewById(R.id.discountCode);
        placeOrderBtn = v.findViewById(R.id.placeOrderBtn);
        et_orderNotes = v.findViewById(R.id.et_orderNotes);
        et_userName = v.findViewById(R.id.userName);
        et_userPhone = v.findViewById(R.id.userPhone);
        placeOrderBtn.setOnClickListener(this);
        ll_add_new_address.setOnClickListener(this);
        ll_select_outlet.setOnClickListener(this);

        UserInfo userInfo = Session.getUserInfo();
        et_userName.setText(userInfo.getName());
        et_userPhone.setText(userInfo.getPhoneNumber());


        return v;
    }

    private void initView(View v) {
        Paper.init(context);
        radioGroup = v.findViewById(R.id.radioGroup);
        btnCashOnDelivery = v.findViewById(R.id.btnCashOnDelivery);
        btnOnlinePayment = v.findViewById(R.id.btnOnlinePayment);

        try {
            brainTreeFragment = BraintreeFragment.newInstance(this, Paper.book().read(Constants.BrainTreeToken));
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
    }


    @Override
    public void onClick(View v) {
        if (v == ll_select_outlet) {
            setSelectOutletFragment();

        } else if (v == ll_add_new_address) {
            setMyAddressFragment();

        } else if (v == placeOrderBtn) {

            if (outletAddress.getText().toString().equals("")) {
                Toast.makeText(context, "Please Select Outlet", Toast.LENGTH_SHORT).show();
                outletAddress.setFocusable(true);
            } else if (et_userName == null && et_userName.equals("")) {
                Toast.makeText(context, "Enter Your Name", Toast.LENGTH_SHORT).show();
            } else if (et_userPhone == null && et_userPhone.equals("")) {
                Toast.makeText(context, "Enter Your Contact Number", Toast.LENGTH_SHORT).show();
            } else if (et_userPhone.getText().length() != 13) {
                Toast.makeText(context, "Incorrect Contact Number", Toast.LENGTH_SHORT).show();
            } else if (createNewAddress.getText().toString().equals("")) {
                Toast.makeText(context, "Please Select Address", Toast.LENGTH_SHORT).show();
                createNewAddress.setFocusable(true);
            } else {

                String contact = "", name = "", orderNotes = "", discountedCode = "";

                PizzaSidelinesDBModel model = new PizzaSidelinesDBModel();
                name = et_userName.getText().toString();
                contact = et_userPhone.getText().toString();
                model.setName(name);
                model.setOrderStatus(0);
                model.setUserPhone(contact);
                if (et_orderNotes != null && !et_orderNotes.equals("")) {
                    orderNotes = et_orderNotes.getText().toString();
                    model.setOrderNotes(orderNotes);
                }

              /*  if (et_discountedCode != null && !et_discountedCode.equals("")) {
                    discountedCode = et_discountedCode.getText().toString();
                    model.setDiscountedCode(discountedCode);
                }*/

                orderPizzas = new DBClass(context).getPizzas();
                orderChinese = new DBClass(context).getChinese();
                orders = new DBClass(context).getSideslines();
                orderFried = new DBClass(context).getFriedRoll();
                orderMeal = new DBClass(context).getMeal();
                orderDeal = new DBClass(context).getDeal();

                model.setSidelinesOrder(orders);
                model.setPizzaOrder(orderPizzas);
                model.setChineseOrder(orderChinese);
                model.setFriedOrder(orderFried);
                model.setMealOrder(orderMeal);
                model.setDealOrder(orderDeal);
                model.setOutletId(selectionModel.getOutletId());
                model.setUserAddress(createNewAddress.getText().toString());
                model.setUserId(Session.getUserInfo().getId());
                model.setOutletFCMToken(selectionModel.getOutletToken());
                model.setOutletAddress(outletAddress.getText().toString());
                model.setOrderType("App");
                model.setOrderPrice(orderTotalPrice + "");

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aa");
                SimpleDateFormat dateformat2 = new SimpleDateFormat("dd/MM/yyyy");
                String datetime2 = dateformat2.format(c.getTime());
                String datetime = dateformat.format(c.getTime());
                System.out.println(datetime);

                model.setDate(datetime2);
                model.setOrderDate(datetime);
                if (btnCashOnDelivery.isChecked()) {
                    model.setPaymentMethod("Cash on Delivery");
                    placeOrder(model);
                }
                else if (btnOnlinePayment.isChecked()) {
                    //brainTreePayment(model);
                    model.setPaymentMethod("Card Payment");
                    brainTreePayment(model);
                }
                else
                    Toast.makeText(context, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void brainTreePayment(PizzaSidelinesDBModel model) {
        brainTreeOrder = model;
        dropInRequest = new DropInRequest().clientToken(Paper.book().read(Constants.BrainTreeToken, "InvalidToken"));
        enableGooglePay(dropInRequest);
        GooglePayment.isReadyToPay(brainTreeFragment, new BraintreeResponseListener<Boolean>() {
            @Override
            public void onResponse(Boolean isReadyToPay) {
                if (isReadyToPay) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivityForResult(dropInRequest.getIntent(context), REQUEST_BRAINTREE_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BRAINTREE_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();


                compositeDisposable.add(cloudFunctions.submit(Double.parseDouble(brainTreeOrder.getOrderPrice()), nonce.getNonce())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BrainTreeTransaction>() {
                            @Override
                            public void accept(BrainTreeTransaction brainTreeTransaction) throws Exception {
                                if (brainTreeTransaction.isSuccess()) {
                                    //Toast.makeText(context, "Transaction Successful", Toast.LENGTH_SHORT).show();
                                    TransactionModel transactionModel = new TransactionModel();
                                    transactionModel.setTransactionId(brainTreeTransaction.getTransaction().getId());
                                    transactionModel.setCreateAt(brainTreeTransaction.getTransaction().getCreateAt());
                                    transactionModel.setCurrencyIsoCode(brainTreeTransaction.getTransaction().getCurrencyIsoCode());
                                    transactionModel.setMarchentAccountId(brainTreeTransaction.getTransaction().getMerchantAccountId());
                                    transactionModel.setMasterMarchentAccountId(brainTreeTransaction.getTransaction().getMasterMerchantAccountId());
                                    transactionModel.setStatus(brainTreeTransaction.getTransaction().getStatus());
                                    transactionModel.setSubMarchentAccountId(brainTreeTransaction.getTransaction().getSubMerchantAccountId());
                                    transactionModel.setType(brainTreeTransaction.getTransaction().getType());

                                    addOrder transactionOrderModel = new addOrder(brainTreeOrder, transactionModel);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(transactionOrderModel);
                                    Log.e("xOrderModelx", json);
                                    //saveOrder(transactionOrderModel);
                                    placeOrder(brainTreeOrder);
                                } else {

                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));

            }
        }
        if (requestCode == GOOGLE_PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            GooglePayment.tokenize(brainTreeFragment, PaymentData.getFromIntent(data));
        }
    }

    private void saveOrder(addOrder transactionOrderModel) {
        String path = ordersReference.push().getKey();
        transactionOrderModel.getOrder().setId(path);

        ordersReference.child(path).setValue(transactionOrderModel);
        new DBClass(context).DeletePizzaCart();
        new DBClass(context).DeleteCart();
        new DBClass(context).DeleteChineseCart();
        new DBClass(context).DeleteFriedRollCart();
        new DBClass(context).DeleteMealCart();
        new DBClass(context).DeleteDealCart();
        ((MainActivity) context).cartSize(0);

        SendNotificationTask sendNotificationTask = new SendNotificationTask(context, selectionModel.getOutletToken(), Common.Complete_TITLE, Common.Complete_MSG);
        sendNotificationTask.execute();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() >= 1) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
        }
        //setNextFragment();
        Intent intent = new Intent(context, ThankYouActivity.class);
        startActivity(intent);
    }

    private void enableGooglePay(DropInRequest dropInRequest) {
        GooglePaymentRequest googlePaymentRequest = new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setTotalPrice(brainTreeOrder.getOrderPrice())
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setCurrencyCode("PKR")
                        .build())
                .billingAddressRequired(true);
        dropInRequest.googlePaymentRequest(googlePaymentRequest);
    }

    private void placeOrder(PizzaSidelinesDBModel model) {

        String path = ordersReference.push().getKey();
        model.setId(path);
        ordersReference.child(path).setValue(model);
        new DBClass(context).DeletePizzaCart();
        new DBClass(context).DeleteCart();
        new DBClass(context).DeleteChineseCart();
        new DBClass(context).DeleteFriedRollCart();
        new DBClass(context).DeleteMealCart();
        new DBClass(context).DeleteDealCart();
        ((MainActivity) context).cartSize(0);

        SendNotificationTask sendNotificationTask = new SendNotificationTask(context, selectionModel.getOutletToken(), Common.Complete_TITLE, Common.Complete_MSG);
        sendNotificationTask.execute();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() >= 1) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
        }
        //setNextFragment();
        Intent intent = new Intent(context, ThankYouActivity.class);
        startActivity(intent);
    }


    private void setNextFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
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

    private void setMyAddressFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectMyAddressFragment hf = (SelectMyAddressFragment) fm.findFragmentByTag("SelectMyAddressFragment");
            if (hf == null) {
                hf = new SelectMyAddressFragment();
                ft.replace(R.id.main_frame, hf, "SelectMyAddressFragment");
                ft.addToBackStack("SelectMyAddressFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "SelectMyAddressFragment");
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

    private void setSelectOutletFragment() {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectOutletFragment hf = (SelectOutletFragment) fm.findFragmentByTag("SelectOutletFragment");
            if (hf == null) {
                hf = new SelectOutletFragment();
                ft.replace(R.id.main_frame, hf, "SelectOutletFragment");
                ft.addToBackStack("SelectOutletFragment");
            } else {
                ft.replace(R.id.main_frame, hf, "SelectOutletFragment");
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.d("EXC", " exception AddComboFragment: " + e.getMessage());
        }
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) e;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    //setErrorMessage(expirationMonthError.getMessage());
                }
            }
        }
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        if (brainTreeOrder != null)
        {
            compositeDisposable.add(cloudFunctions.submit( Double.parseDouble(brainTreeOrder.getOrderPrice()), paymentMethodNonce.getNonce())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BrainTreeTransaction>() {
                        @Override
                        public void accept(BrainTreeTransaction brainTreeTransaction) throws Exception {
                            if (brainTreeTransaction.isSuccess()) {
                                TransactionModel transactionModel = new TransactionModel();
                                transactionModel.setTransactionId(brainTreeTransaction.getTransaction().getId());
                                transactionModel.setCreateAt(brainTreeTransaction.getTransaction().getCreateAt());
                                transactionModel.setCurrencyIsoCode(brainTreeTransaction.getTransaction().getCurrencyIsoCode());
                                transactionModel.setMarchentAccountId(brainTreeTransaction.getTransaction().getMerchantAccountId());
                                transactionModel.setMasterMarchentAccountId(brainTreeTransaction.getTransaction().getMasterMerchantAccountId());
                                transactionModel.setStatus(brainTreeTransaction.getTransaction().getStatus());
                                transactionModel.setSubMarchentAccountId(brainTreeTransaction.getTransaction().getSubMerchantAccountId());
                                transactionModel.setType(brainTreeTransaction.getTransaction().getType());
                                addOrder transactionOrderModel = new addOrder(brainTreeOrder,transactionModel);
                                Gson gson = new Gson();
                                String json = gson.toJson(transactionOrderModel);
                                Log.e("xOrderModelx", json);
                                saveOrder(transactionOrderModel);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }));
        }
        else
        { }
    }
}