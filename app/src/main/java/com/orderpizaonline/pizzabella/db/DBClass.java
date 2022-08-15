package com.orderpizaonline.pizzabella.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.orderpizaonline.pizzabella.model.BurgerOrSandwichInfo;
import com.orderpizaonline.pizzabella.model.ChineseCornerOrderInfo;
import com.orderpizaonline.pizzabella.model.ComboOrderInfo;
import com.orderpizaonline.pizzabella.model.FriedRollInfo;
import com.orderpizaonline.pizzabella.model.FriedRollOrderInfo;
import com.orderpizaonline.pizzabella.model.PairValuesModel;
import com.orderpizaonline.pizzabella.model.PizzaInfoInCombo;
import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
import com.orderpizaonline.pizzabella.model.QuantityInfo;
import com.orderpizaonline.pizzabella.model.SidelineInfo;
import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.orderpizaonline.pizzabella.model.SizeInfo;
import com.orderpizaonline.pizzabella.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DBClass extends SQLiteOpenHelper {

    public DBClass(Context context) {

        super(context, "Pizza User app", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String[] sqlColumns = {"ID", "Name", "ImageUrl", "Price", "Category", "Quantity", "Discount"};
        db.execSQL("create table CartTable (ID text,Name text,ImageUrl text,Price text,Type text ,Quantity integer,Coldrink text, SizeInfo text)");
        db.execSQL("create table ChineseTable (ID text,Name text,ImageUrl text,SinglePrice integer,TotalPrice integer ,Quantity integer)");
        db.execSQL("create table MealTable (ID text,Name text,ImageUrl text,SinglePrice text,TotalPrice text ,Quantity integer," +
                " Mealdescription text, ColdrinkList text)");
        db.execSQL("create table FriedRollTable (ID text,Name text,ImageUrl text,SinglePrice integer,TotalPrice integer ,Quantity integer, Variation text)");
        db.execSQL("create table PizzaCartTable (ID text,Name text,ImageUrl text,Price text,TotalPrice text ,Quantity integer, SizePrice text, SizeName text, Crust text, " +
                "ToppingsList text, PizzaExtraToppingList text )");

        db.execSQL("create table DealTable (ID text,Name text,ImageUrl text,SinglePrice text,TotalPrice text ,Quantity integer," +
                " Mealdescription text, ColdrinkList text,Pizza text, FriedRollList text, BugerSandwichList text, Flavours text )");
    }


    public long InsertDeal(ComboOrderInfo sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getItemName());
        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("SinglePrice", sidelineOrderInfo.getPrice());
        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        values.put("Mealdescription", sidelineOrderInfo.getDealDescription());
        values.put("ColdrinkList", Utils.getGsonParser().toJson(sidelineOrderInfo.getColdPizList()));
        values.put("Pizza", Utils.getGsonParser().toJson(sidelineOrderInfo.getPizzaInfo()));
        values.put("FriedRollList", Utils.getGsonParser().toJson(sidelineOrderInfo.getFriedRollInfoList()));
        values.put("BugerSandwichList", Utils.getGsonParser().toJson(sidelineOrderInfo.getFriedRollInfoList()));
        values.put("Flavours", Utils.getGsonParser().toJson(sidelineOrderInfo.getPizzaFlavours()));
        a = db.insert("DealTable", null, values);
        db.close();

        return a;
    }

    public long InsertMeal(ComboOrderInfo sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        Log.i("TAG", "InsertProducts: true");

        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getItemName());
        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("SinglePrice", sidelineOrderInfo.getPrice());
        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        values.put("Mealdescription", sidelineOrderInfo.getDealDescription());
        values.put("ColdrinkList", Utils.getGsonParser().toJson(sidelineOrderInfo.getColdPizList()));
        a = db.insert("MealTable", null, values);
        db.close();

        return a;
    }

    public long InsertPizzas(PizzaOrderCartModel sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        Log.i("TAG", "InsertProducts: true");

        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getPizzaName());
        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("Price", sidelineOrderInfo.getPrice());
        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        values.put("SizePrice", sidelineOrderInfo.getSizePrice());
        values.put("SizeName", sidelineOrderInfo.getSizeName());
        values.put("Crust", Utils.getGsonParser().toJson(sidelineOrderInfo.getCrust()));
        values.put("ToppingsList", Utils.getGsonParser().toJson(sidelineOrderInfo.getPizzaToppingInfo()));
        values.put("PizzaExtraToppingList", Utils.getGsonParser().toJson(sidelineOrderInfo.getExtraTopping()));
        //values.put("promotion",infoList.get(i).p_promotion);
        a = db.insert("PizzaCartTable", null, values);

        db.close();
        return a;
    }

    public long InsertSidelines(SidelineOrderInfo sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        Log.i("TAG", "InsertProducts: true");

        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getSidelineName());
        // values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("Price", sidelineOrderInfo.getPrice());
        values.put("Type", sidelineOrderInfo.getBeveragesType());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        //values.put("p_Details",infoList.get(i).p_Details);
        values.put("Coldrink", sidelineOrderInfo.getColdDrinksInfo());
        values.put("SizeInfo", Utils.getGsonParser().toJson(sidelineOrderInfo.getSizeInfo()));
        a = db.insert("CartTable", null, values);
        db.close();

        return a;
    }

    public long InsertChinese(ChineseCornerOrderInfo sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        Log.i("TAG", "InsertProducts: true");

        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getName());
        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("SinglePrice", sidelineOrderInfo.getSinglePrice());
        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        a = db.insert("ChineseTable", null, values);
        db.close();

        return a;
    }

    public long InsertFriedRoll(FriedRollOrderInfo sidelineOrderInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        long a = 0;
        Log.i("TAG", "InsertProducts: true");

        values = new ContentValues();
        values.put("ID", sidelineOrderInfo.getId());
        values.put("Name", sidelineOrderInfo.getItemName());
        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
        values.put("SinglePrice", sidelineOrderInfo.getSinglePrice());
        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
        values.put("Quantity", sidelineOrderInfo.getQuantity());
        values.put("Variation", Utils.getGsonParser().toJson(sidelineOrderInfo.getVariationList()));
        a = db.insert("FriedRollTable", null, values);
        db.close();

        return a;
    }

    public int CountFolders() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM foldertable";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        db.close();
        return icount;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CartTable");
        db.execSQL("DROP TABLE IF EXISTS PizzaCartTable");
        db.execSQL("DROP TABLE IF EXISTS ChineseTable");
        db.execSQL("DROP TABLE IF EXISTS MealTable");
        db.execSQL("DROP TABLE IF EXISTS DealTable");
        onCreate(db);
    }

    public List<PizzaOrderCartModel> getPizzas() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PizzaOrderCartModel> productArrayList = new ArrayList<>();
        PizzaOrderCartModel sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from PizzaCartTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new PizzaOrderCartModel();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setPizzaName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
            sidelineOrderInfo.setSizePrice(cursor.getString(cursor.getColumnIndex("SizePrice")));
            sidelineOrderInfo.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
            sidelineOrderInfo.setTotalPrice(cursor.getString(cursor.getColumnIndex("TotalPrice")));
//            sidelineOrderInfo.setPizzaType(cursor.getString(cursor.getColumnIndex("Type")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
//            sidelineOrderInfo.setDough(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Dough")), SidelineInfo.class));
            sidelineOrderInfo.setCrust(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Crust")), SidelineInfo.class));
            sidelineOrderInfo.setPizzaToppingInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), PizzaToppingInfo.class));
            sidelineOrderInfo.setExtraTopping(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("PizzaExtraToppingList")), PairValuesModel.class));
//            sidelineOrderInfo.setSpice(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Spice")), SidelineInfo.class));
//            sidelineOrderInfo.setComboInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Combo")), ComboInfo.class));
//            sidelineOrderInfo.setToppingInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), new TypeToken<List<PizzaToppingInfo>>() {
//            }.getType()));
//            sidelineOrderInfo.setPizzaExtraToppingList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("PizzaExtraToppingList")), new TypeToken<List<SidelineOrderInfo>>() {
//            }.getType()));
            /*
            sidelineOrderInfo.setDough(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Dough")), SidelineInfo.class));
            sidelineOrderInfo.setCrust(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Crust")), SidelineInfo.class));
            sidelineOrderInfo.setSauce(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Sauce")), SidelineInfo.class));
            sidelineOrderInfo.setSpice(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Spice")), SidelineInfo.class));
            sidelineOrderInfo.setToppingInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), List.class));*/

            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public List<SidelineOrderInfo> getSideslines() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<SidelineOrderInfo> productArrayList = new ArrayList<>();
        SidelineOrderInfo sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from CartTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new SidelineOrderInfo();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setSidelineName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setBeveragesType(cursor.getString(cursor.getColumnIndex("Type")));
            sidelineOrderInfo.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
            sidelineOrderInfo.setColdDrinksInfo(cursor.getString(cursor.getColumnIndex("Coldrink")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
            sidelineOrderInfo.setSizeInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("SizeInfo")), SizeInfo.class));

            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public List<ChineseCornerOrderInfo> getChinese() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ChineseCornerOrderInfo> productArrayList = new ArrayList<>();
        ChineseCornerOrderInfo sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from ChineseTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new ChineseCornerOrderInfo();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setSinglePrice(cursor.getInt(cursor.getColumnIndex("SinglePrice")));
            sidelineOrderInfo.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TotalPrice")));
            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));

            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public List<FriedRollOrderInfo> getFriedRoll() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<FriedRollOrderInfo> productArrayList = new ArrayList<>();
        FriedRollOrderInfo sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from FriedRollTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new FriedRollOrderInfo();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setItemName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setSinglePrice(cursor.getInt(cursor.getColumnIndex("SinglePrice")));
            sidelineOrderInfo.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TotalPrice")));
            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
            sidelineOrderInfo.setVariationList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Variation")), QuantityInfo.class));

            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public List<ComboOrderInfo> getMeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ComboOrderInfo> productArrayList = new ArrayList<>();
        ComboOrderInfo sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from MealTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new ComboOrderInfo();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setItemName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setPrice(""+cursor.getInt(cursor.getColumnIndex("SinglePrice")));
            sidelineOrderInfo.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TotalPrice")));
            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
            sidelineOrderInfo.setDealDescription(cursor.getString(cursor.getColumnIndex("Mealdescription")));
            sidelineOrderInfo.setColdPizList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ColdrinkList")), new TypeToken<List<PairValuesModel>>() {
            }.getType()));
            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public List<ComboOrderInfo> getDeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ComboOrderInfo> productArrayList = new ArrayList<>();
        ComboOrderInfo sidelineOrderInfo;
        Cursor cursor = db.rawQuery("select * from DealTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sidelineOrderInfo = new ComboOrderInfo();
            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
            sidelineOrderInfo.setItemName(cursor.getString(cursor.getColumnIndex("Name")));
            sidelineOrderInfo.setPrice(""+cursor.getInt(cursor.getColumnIndex("SinglePrice")));
            sidelineOrderInfo.setTotalPrice(cursor.getInt(cursor.getColumnIndex("TotalPrice")));
            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
            sidelineOrderInfo.setDealDescription(cursor.getString(cursor.getColumnIndex("Mealdescription")));
            sidelineOrderInfo.setPizzaInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Pizza")), PizzaInfoInCombo.class));
            sidelineOrderInfo.setColdPizList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ColdrinkList")), new TypeToken<List<PairValuesModel>>() {
            }.getType()));
            sidelineOrderInfo.setFriedRollInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("FriedRollList")), new TypeToken<List<FriedRollInfo>>() {
            }.getType()));
            sidelineOrderInfo.setBurgerOrSandwichInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("BugerSandwichList")), new TypeToken<List<BurgerOrSandwichInfo>>() {
            }.getType()));
            sidelineOrderInfo.setPizzaFlavours(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Flavours")), new TypeToken<List<PizzaToppingInfo>>() {
            }.getType()));

            productArrayList.add(sidelineOrderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return productArrayList;
    }

    public void UpdatePizzaQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("PizzaCartTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }

    public void UpdateMealQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("MealTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }
    public void UpdateDealQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("DealTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }

    public void UpdateQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("CartTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }

    public void UpdateChineseQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("ChineseTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }

    public void UpdateFriedRollQuantity(String pId, int pQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        // values.put("id",imageID);
        values.put("Quantity", pQuantity);
        //values.put("price",price);
        Log.i("TAG", "PRODUCT quantity: " + pQuantity);

        db.update("FriedRollTable", values, "ID" + " = ? ", new String[]{pId});
        db.close();
    }

    public int DeleteSideline(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("CartTable", "ID" + " = ? ", new String[]{productId});

        db.close();
        return a;
    }
    public int DeleteMeal(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("MealTable", "ID" + " = ? ", new String[]{productId});

        db.close();
        return a;
    }
    public int DeleteDeal(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("DealTable", "ID" + " = ? ", new String[]{productId});

        db.close();
        return a;
    }

    public int DeleteFriedRoll(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("FriedRollTable", "ID" + " = ? ", new String[]{productId});

        db.close();
        return a;
    }

    public int DeleteChinese(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("ChineseTable", "ID" + " = ? ", new String[]{productId});

        db.close();
        return a;
    }

    public int DeletePizza(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a =  db.delete("PizzaCartTable", "ID" + " = ? ", new String[]{productId});
        db.close();
        return a;
    }

    public void DeletePizzaCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from PizzaCartTable");
        db.close();

    }
    public void DeleteMealCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from MealTable");
        db.close();

    }
    public void DeleteDealCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from DealTable");
        db.close();

    }

    public void DeleteCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from CartTable");
        db.close();

    }

    public void DeleteChineseCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ChineseTable");
        db.close();

    }

    public void DeleteFriedRollCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from FriedRollTable");
        db.close();

    }

}










//
//
//package com.orderpizaonline.pizzabella.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.orderpizaonline.pizzabella.model.PairValuesModel;
//import com.orderpizaonline.pizzabella.model.PizzaOrderCartModel;
//import com.orderpizaonline.pizzabella.model.PizzaToppingInfo;
//import com.orderpizaonline.pizzabella.model.SidelineInfo;
//import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
//import com.orderpizaonline.pizzabella.model.SizeInfo;
//import com.orderpizaonline.pizzabella.utils.Utils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class DBClass extends SQLiteOpenHelper {
//
//    public DBClass(Context context) {
//
//        super(context, "Pizza User app", null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        String[] sqlColumns = {"ID", "Name", "ImageUrl", "Price", "Category", "Quantity", "Discount"};
//        db.execSQL("create table CartTable (ID text,Name text,ImageUrl text,Price text,Type text ,Quantity integer,Coldrink text, SizeInfo text)");
//        db.execSQL("create table PizzaCartTable (ID text,Name text,ImageUrl text,Price text,TotalPrice text ,Quantity integer, SizePrice text, SizeName text, Crust text, " +
//                "ToppingsList text, PizzaExtraToppingList text )");
//    }
//
//
//    public long InsertFriedRoll(SidelineOrderInfo sidelineOrderInfo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values;
//        long a = 0;
//        Log.i("TAG", "InsertProducts: true");
//        values = new ContentValues();
//        values.put("ID", sidelineOrderInfo.getId());
//        values.put("Name", sidelineOrderInfo.getName());
//        // values.put("ImageUrl", sidelineOrderInfo.getImageURL());
//        values.put("Price", sidelineOrderInfo.getTotalPrice());
//        values.put("Type", sidelineOrderInfo.getBeveragesType());
//        values.put("Quantity", sidelineOrderInfo.getQuantity());
//        //values.put("p_Details",infoList.get(i).p_Details);
//        values.put("Coldrink", sidelineOrderInfo.getColdDrinksInfo());
//        values.put("SizeInfo",Utils.getGsonParser().toJson(sidelineOrderInfo.getSizeInfo()));
//        a = db.insert("CartTable", null, values);
//        db.close();
//
//        return a;
//    }
//
//
//    public long InsertPizzas(PizzaOrderCartModel sidelineOrderInfo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values;
//        long a = 0;
//        Log.i("TAG", "InsertProducts: true");
//
//        values = new ContentValues();
//        values.put("ID", sidelineOrderInfo.getId());
//        values.put("Name", sidelineOrderInfo.getPizzaName());
//        values.put("ImageUrl", sidelineOrderInfo.getImageURL());
//        values.put("Price", sidelineOrderInfo.getPrice());
//        values.put("TotalPrice", sidelineOrderInfo.getTotalPrice());
//        values.put("Quantity", sidelineOrderInfo.getQuantity());
//        values.put("SizePrice", sidelineOrderInfo.getSizePrice());
//        values.put("SizeName", sidelineOrderInfo.getSizeName());
//        values.put("Crust", Utils.getGsonParser().toJson(sidelineOrderInfo.getCrust()));
//        values.put("ToppingsList", Utils.getGsonParser().toJson(sidelineOrderInfo.getPizzaToppingInfo()));
//        values.put("PizzaExtraToppingList", Utils.getGsonParser().toJson(sidelineOrderInfo.getExtraTopping()));
//        //values.put("promotion",infoList.get(i).p_promotion);
//        a = db.insert("PizzaCartTable", null, values);
//
//        db.close();
//        return a;
//    }
//
//    public long InsertSidelines(SidelineOrderInfo sidelineOrderInfo) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values;
//        long a = 0;
//        Log.i("TAG", "InsertProducts: true");
//
//        values = new ContentValues();
//        values.put("ID", sidelineOrderInfo.getId());
//        values.put("Name", sidelineOrderInfo.getName());
//       // values.put("ImageUrl", sidelineOrderInfo.getImageURL());
//        values.put("Price", sidelineOrderInfo.getTotalPrice());
//        values.put("Type", sidelineOrderInfo.getBeveragesType());
//        values.put("Quantity", sidelineOrderInfo.getQuantity());
//        //values.put("p_Details",infoList.get(i).p_Details);
//        values.put("Coldrink", sidelineOrderInfo.getColdDrinksInfo());
//        values.put("SizeInfo",Utils.getGsonParser().toJson(sidelineOrderInfo.getSizeInfo()));
//        a = db.insert("CartTable", null, values);
//        db.close();
//
//        return a;
//    }
//
//    public int CountFolders() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String count = "SELECT count(*) FROM foldertable";
//        Cursor mcursor = db.rawQuery(count, null);
//        mcursor.moveToFirst();
//        int icount = mcursor.getInt(0);
//        db.close();
//        return icount;
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS CartTable");
//        db.execSQL("DROP TABLE IF EXISTS PizzaCartTable");
//        onCreate(db);
//    }
//
//    public List<PizzaOrderCartModel> getPizzas() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<PizzaOrderCartModel> productArrayList = new ArrayList<>();
//        PizzaOrderCartModel sidelineOrderInfo;
//        Cursor cursor = db.rawQuery("select * from PizzaCartTable", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            sidelineOrderInfo = new PizzaOrderCartModel();
//            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
//            sidelineOrderInfo.setPizzaName(cursor.getString(cursor.getColumnIndex("Name")));
//            sidelineOrderInfo.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
//            sidelineOrderInfo.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUrl")));
//            sidelineOrderInfo.setSizePrice(cursor.getString(cursor.getColumnIndex("SizePrice")));
//            sidelineOrderInfo.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
//            sidelineOrderInfo.setTotalPrice(cursor.getString(cursor.getColumnIndex("TotalPrice")));
////            sidelineOrderInfo.setPizzaType(cursor.getString(cursor.getColumnIndex("Type")));
//            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
////            sidelineOrderInfo.setDough(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Dough")), SidelineInfo.class));
//            sidelineOrderInfo.setCrust(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Crust")), SidelineInfo.class));
//            sidelineOrderInfo.setPizzaToppingInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), PizzaToppingInfo.class));
//            sidelineOrderInfo.setExtraTopping(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("PizzaExtraToppingList")), PairValuesModel.class));
////            sidelineOrderInfo.setSpice(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Spice")), SidelineInfo.class));
////            sidelineOrderInfo.setComboInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Combo")), ComboInfo.class));
////            sidelineOrderInfo.setToppingInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), new TypeToken<List<PizzaToppingInfo>>() {
////            }.getType()));
////            sidelineOrderInfo.setPizzaExtraToppingList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("PizzaExtraToppingList")), new TypeToken<List<SidelineOrderInfo>>() {
////            }.getType()));
//            /*
//            sidelineOrderInfo.setDough(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Dough")), SidelineInfo.class));
//            sidelineOrderInfo.setCrust(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Crust")), SidelineInfo.class));
//            sidelineOrderInfo.setSauce(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Sauce")), SidelineInfo.class));
//            sidelineOrderInfo.setSpice(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("Spice")), SidelineInfo.class));
//            sidelineOrderInfo.setToppingInfoList(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("ToppingsList")), List.class));*/
//
//            productArrayList.add(sidelineOrderInfo);
//
//            cursor.moveToNext();
//        }
//        cursor.close();
//        db.close();
//        return productArrayList;
//    }
//
//    public List<SidelineOrderInfo> getSideslines() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<SidelineOrderInfo> productArrayList = new ArrayList<>();
//        SidelineOrderInfo sidelineOrderInfo;
//        Cursor cursor = db.rawQuery("select * from CartTable", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            sidelineOrderInfo = new SidelineOrderInfo();
//            sidelineOrderInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
//            sidelineOrderInfo.setSidelineName(cursor.getString(cursor.getColumnIndex("Name")));
//            sidelineOrderInfo.setBeveragesType(cursor.getString(cursor.getColumnIndex("Type")));
//            sidelineOrderInfo.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
//            sidelineOrderInfo.setColdDrinksInfo(cursor.getString(cursor.getColumnIndex("Coldrink")));
//            sidelineOrderInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
//            sidelineOrderInfo.setSizeInfo(Utils.getGsonParser().fromJson(cursor.getString(cursor.getColumnIndex("SizeInfo")), SizeInfo.class));
//
//            productArrayList.add(sidelineOrderInfo);
//
//            cursor.moveToNext();
//        }
//        cursor.close();
//        db.close();
//        return productArrayList;
//    }
//
//    public void UpdatePizzaQuantity(String pId, int pQuantity) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values;
//        values = new ContentValues();
//        // values.put("id",imageID);
//        values.put("Quantity", pQuantity);
//        //values.put("price",price);
//        Log.i("TAG", "PRODUCT quantity: " + pQuantity);
//
//        db.update("PizzaCartTable", values, "ID" + " = ? ", new String[]{pId});
//        db.close();
//    }
//
//    public void UpdateQuantity(String pId, int pQuantity) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values;
//        values = new ContentValues();
//        // values.put("id",imageID);
//        values.put("Quantity", pQuantity);
//        //values.put("price",price);
//        Log.i("TAG", "PRODUCT quantity: " + pQuantity);
//
//        db.update("CartTable", values, "ID" + " = ? ", new String[]{pId});
//        db.close();
//    }
//
//    public int DeleteSideline(String productId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int a = db.delete("CartTable", "ID" + " = ? ", new String[]{productId});
//
//        db.close();
//        return a;
//    }
//
//    public int DeletePizza(String productId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int a =  db.delete("PizzaCartTable", "ID" + " = ? ", new String[]{productId});
//        db.close();
//        return a;
//    }
//
//    public void DeletePizzaCart() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from PizzaCartTable");
//        db.close();
//
//    }
//
//    public void DeleteCart() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from CartTable");
//        db.close();
//
//    }
//
//}
