package com.orderpizaonline.pizzabella.functions;

import com.orderpizaonline.pizzabella.model.BrainTreeTransaction;
import com.orderpizaonline.pizzabella.model.BraintreeToken;
import com.orderpizaonline.pizzabella.model.Transaction;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ICloudFunctions {
    @GET("token")
    Observable<BraintreeToken> getToken();

    @FormUrlEncoded
    @POST("checkout")
    Observable<BrainTreeTransaction> submit(@Field("amount")double amount,
                                            @Field("payment_method_nonce") String nonce);
    @FormUrlEncoded
    @POST("refund")
    Observable<BrainTreeTransaction> refund(@Field("transactionId") String transactionId);
    @FormUrlEncoded
    @POST("cancel")
    Observable<BrainTreeTransaction> cancel(@Field("transactionId") String transactionId);
    @FormUrlEncoded
    @POST("find")
    Observable<Transaction> find(@Field("transactionId") String transactionId);
}
