package com.orderpizaonline.pizzabella.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PizzaOrderInfoBella;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;

import static com.orderpizaonline.pizzabella.MainActivity.chineseCornerInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.comboOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.friedRollOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.pizzaOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderInfoBella;
import static com.orderpizaonline.pizzabella.MainActivity.sidelinesOrderList;

public class Utils {
    private static Gson gson;

    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }


    public static int getDiscountedPrice(int d, int op) {
        float dp = 0, p = 0;
        dp = (d/100f) * op;
        p = (float) Math.ceil(op - dp);
        return (int)p;
    }

    public static int getOriginalPrice(int dp, int per) {
        int  op = 0;
        float f = 1 - (per/100f);
        op = (int) Math.ceil(dp/f);
        return op;
    }
    public static void clearModels() {
        friedRollOrderInfoBella = null;
        friedRollOrderInfoBella = new FriedRollOrderInfo();
        comboOrderInfoBella = null;
        comboOrderInfoBella = new ComboOrderInfo();
        chineseCornerInfoBella = null;
        chineseCornerInfoBella = new ChineseCornerOrderInfo();
        sidelinesOrderInfoBella = null;
        sidelinesOrderInfoBella = new SidelineOrderInfo();
        pizzaOrderInfoBella = null;
        pizzaOrderInfoBella = new PizzaOrderInfoBella();
        sidelinesOrderList.clear();
    }




}
