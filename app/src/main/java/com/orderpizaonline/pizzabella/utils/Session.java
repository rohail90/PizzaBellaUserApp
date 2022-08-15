package com.orderpizaonline.pizzabella.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.orderpizaonline.pizzabella.model.UserInfo;

public class Session {
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;
    static Context ctx;

    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public  static  void setTimeToStart(boolean loginFlag) {
        editor.putBoolean("TimeToStart", loginFlag);
        editor.commit();
    }
    public  static  Boolean getTimeToStart() {
        return  prefs.getBoolean("TimeToStart", false);
    }
    public  static  void setArriveAtStore(boolean loginFlag) {
        editor.putBoolean("ArrivedAtStore", loginFlag);
        editor.commit();
    }
    public  static  Boolean getArriveAtStore() {
        return  prefs.getBoolean("ArrivedAtStore", false);
    }

    public  static  void setLoginState(boolean loginFlag) {
        editor.putBoolean("isLoggedIn", loginFlag);
        editor.commit();
    }
    public  static  Boolean getHasCard() {
        return  prefs.getBoolean("hasCard", false);
    }
    public  static  void setHasCard(boolean loginFlag) {
        editor.putBoolean("hasCard", loginFlag);
        editor.commit();
    }
    public  static  int getLOcationIndex() {
        return  prefs.getInt("locationIndex", 0);
    }
    public  static  void setLOcationIndex(int index) {
        editor.putInt("locationIndex", index);
        editor.commit();
    }
    public  static  Boolean getJobStarted() {
        return  prefs.getBoolean("jobStarted", false);
    }
    public  static  void setJobStarted(boolean loginFlag) {
        editor.putBoolean("jobStarted", loginFlag);
        editor.commit();
    }
    public  static  String getCurrentState() {
        return  prefs.getString("CurrentState", null);
    }
    public  static  void setCurrentState(String currentState) {
        editor.putString("CurrentState", currentState);
        editor.commit();
    }
    public  static  void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }
    public  static void setUserInfo (UserInfo userInfo) {

        editor.putString("userInfo", Utils.getGsonParser().toJson(userInfo));
        editor.commit();
    }
    public static UserInfo getUserInfo(){
        String str = prefs.getString("userInfo", null);
        UserInfo userInfo = Utils.getGsonParser().fromJson(str, UserInfo.class);
        return userInfo;
    }


    public  static  void setUserType(String userType) {
        editor.putString("userType", userType);
        editor.commit();
    }
    public  static  void setToken(String password) {
        editor.putString("tokenNo", password);
        editor.commit();
    }
    public  static  void setFCMToken(String FCMtoken) {
        editor.putString("FCMtokenNo", FCMtoken);
        editor.commit();
    }
    public  static  String getFCMToken() {
        return  prefs.getString("FCMtokenNo", null);
    }
    public  static  void setAddress(String address) {
        editor.putString("address", address);
        editor.commit();
    }
    public  static  void setFirstName(String name) {
        editor.putString("f_name", name);
        editor.commit();
    }
    public  static  void setLastName(String name) {
        editor.putString("l_name", name);
        editor.commit();
    }
    public  static  void setLat(String lat) {
        editor.putString("lat", lat);
        editor.commit();
    }
    public  static  void setLng(String lng) {
        editor.putString("lng", lng);
        editor.commit();
    }
    public  static  void setPickupLat(String lat) {
        editor.putString("latPickup", lat);
        editor.commit();
    }
    public  static  void setDestinationLatLng(String lat,String lng) {
        editor.putString("latDestination", lat);
        editor.putString("lngDestination", lng);
        editor.commit();
    }
    public  static  void setCurrentDestinationLatLng(String lat,String lng) {
        Log.d("LOOC", "setCurrentDestinationLatLng: ================================= ");
        editor.putString("currentDesLat", lat);
        editor.putString("currentDesLng", lng);
        editor.commit();
    }
    public  static  void setPickupLng(String lng) {
        editor.putString("lngPickup", lng);
        editor.commit();
    }

    public static String getDestinationLat(){
        return   prefs.getString("latDestination", null);
    }
    public static String getDestinationLng(){
        return   prefs.getString("lngDestination", null);
    }
    public static String getBuyerLatFromSeller(){
        return   prefs.getString("b_lat", null);
    }
    public static String getBuyerLngFromSeller(){
        return   prefs.getString("b_lng", null);
    }

    public  static  void setBuyerLatFromSeller(String lat) {
        editor.putString("b_lat", lat);
        editor.commit();
    }
    public  static  void setBuyerLngFromSeller(String lng) {
        editor.putString("b_lng", lng);
        editor.commit();
    }

    public static String getPickupLat(){
        return   prefs.getString("latPickup", null);
    }
    public static String getPickupLng(){
        return   prefs.getString("lngPickup", null);
    }
    public static String getLat(){
        return   prefs.getString("lat", null);
    }
    public static String getLng(){
        return   prefs.getString("lng", null);
    }
    public  static  void setPhoneNo(String phoneNo) {
        editor.putString("phone_no", phoneNo);
        editor.commit();
    }
    public  static  void setEmail(String email) {
        editor.putString("u_email", email);
        editor.commit();
    }
    public  static  void setServiceTag(String tag) {
        editor.putString("serviceTag", tag);
        editor.commit();
    }
    public  static  String getServiceTag() {
        return  prefs.getString("serviceTag", null);
    }
    public  static  void setMessageCount(int count) {
        editor.putInt("messageCount", count);
        editor.commit();
    }
    public  static  int getMessageCount() {
        return  prefs.getInt("messageCount", 0);
    }

    public  static  void setId(String id) {
        editor.putString("u_id", id);
        editor.commit();
    }
    public  static  void setEmailVerified(boolean loginFlag) {
        editor.putBoolean("emailVerified", loginFlag);
        editor.commit();
    }
    public  static  void setPhoneNoVerified(boolean loginFlag) {
        editor.putBoolean("phoneNoVerified", loginFlag);
        editor.commit();
    }
    public  static  void setUserID(String userID) {
        editor.putString("userID", userID);
        editor.commit();
    }
    public  static  void setStartTime(String time) {
        editor.putString("startTime", time);
        editor.commit();
    }
    public  static  String getStartTime() {
        return  prefs.getString("startTime", null);
    }
    public  static  void setSellerStartPosition(String lat,String lng) {
        editor.putString("sStartPositionLat", lat);
        editor.putString("sStartPositionLng", lng);
        editor.commit();
    }
    public  static  String getSellerStartPositionLat() {
        return  prefs.getString("sStartPositionLat", null);
    }
    public  static  String getSellerStartPositionLng() {
        return  prefs.getString("sStartPositionLng", null);
    }
    public  static  void setSellerUserID(String userID) {
        editor.putString("sellerID", userID);
        editor.commit();
    }
    public  static  String getSellerUserID() {
        return  prefs.getString("sellerID", null);
    }
    public  static  void setBuyerUserID(String userID) {
        editor.putString("buyerID", userID);
        editor.commit();
    }
    public  static  String getBuyerUserID() {
        return  prefs.getString("buyerID", null);
    }
    public  static  void setJObId(String userID) {
        editor.putString("JObId", userID);
        editor.commit();
    }
    public  static  String getJObId() {
        return  prefs.getString("JObId", null);
    }
    public  static  String getUserID() {
        return  prefs.getString("userID", null);
    }
    public  static  Boolean getLoginState() {
        return  prefs.getBoolean("isLoggedIn", false);
    }
    public  static  Boolean getEmailVerified() {
        return  prefs.getBoolean("emailVerified", false);
    }
    public  static  Boolean getPhoneNoVerified() {
        return  prefs.getBoolean("phoneNoVerified", false);
    }
    public  static  String getUserType() {
        return  prefs.getString("userType", null);
    }
    public  static  String getFirstName() {
        return  prefs.getString("f_name", null);
    }
    public  static  String getLastName() {
        return  prefs.getString("l_name", null);
    }
    public  static  String getPhoneNo() {
        return  prefs.getString("phone_no", null);
    }
    public  static  String getEmail() {
        return  prefs.getString("u_email", null);
    }
    public  static  String getPassword() {
        return  prefs.getString("password", null);
    }
    public  static  String getAddress() {
        return  prefs.getString("address", null);
    }
    public  static  String getId() {
        return  prefs.getString("u_id", null);
    }
    public  static  String getToken() {
        return  prefs.getString("tokenNo", null);
    }
    public static void setJobAssignedBuyerNoti(String jobAssignedBuyerNoti){
        Log.d("JOBASS", "setJobAssignedBuyerNoti: "+jobAssignedBuyerNoti);
        editor.putString("JobAssignedBuyerNoti", jobAssignedBuyerNoti);
        editor.commit();
    }

    public static void setJobAssignedSellerNoti(String jobAssignedBuyerNoti){
        editor.putString("JobAssignedSellerNoti", jobAssignedBuyerNoti);
        editor.commit();
    }
    public static void setReachedAtPickup(Boolean reachedAtPickup){
        editor.putBoolean("reachedAtPickup", reachedAtPickup);
        editor.commit();
    }
    public  static  Boolean getReachedAtPickup() {
        return  prefs.getBoolean("reachedAtPickup", false);
    }

    public static void setLocationUpdated(Boolean reachedAtPickup){
        editor.putBoolean("locationUpdated", reachedAtPickup);
        editor.commit();
    }
    public  static  Boolean getLocationUpdated() {
        return  prefs.getBoolean("locationUpdated", false);
    }



    public static void setSignINRespone(String jobAssignedBuyerNoti){
        editor.putString("signInResponse", jobAssignedBuyerNoti);
        editor.commit();
    }
    public static void setNotiGuid(String notiGuid){
        editor.putString("notiGuid", notiGuid);
        editor.commit();
    }

    public static void setPromoCode(String promoCode){
        editor.putString("promoCode", promoCode);
        editor.commit();
    }
    public  static String getPromoCode() {

        return  prefs.getString("promoCode", null);
    }

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert manager != null;
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        } catch (Exception ignored) {
            return false;
        }

    }


    public static void clearSharedPrefrences(){
    /* setLoginState(false);
     setUserID(null);
     setBuyerUserID(null);
     setSellerUserID(null);
     setFCMToken(null);
        setUserType(null);*/
        setLOcationIndex(0);
     setBuyerLngFromSeller(null);
     setBuyerLatFromSeller(null);
     setPickupLat(null);
     setPickupLng(null);
     setId(null);
     setServiceTag(null);
     setLocationUpdated(false);

     setJobAssignedSellerNoti(null);
     setJobAssignedBuyerNoti(null);
     setStartTime(null);
     setSellerStartPosition(null,null);
     setDestinationLatLng(null,null);
     setReachedAtPickup(false);
     setTimeToStart(false);
     setCurrentDestinationLatLng(null,null);
     setHasCard(false);
     setPromoCode(null);
     setNotiGuid(null);
        setSellerUserID("");
     setCurrentState(null);
     setJobStarted(false);
     setArriveAtStore(false);
     setLOcationIndex(0);
        editor.commit();

    }


}
