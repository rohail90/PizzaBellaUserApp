package com.orderpizaonline.pizzabella.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.orderpizaonline.pizzabella.model.SidelineOrderInfo;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

//import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "Orders.db";
    private static final int DB_VERSION = 1;

    public Database(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<SidelineOrderInfo> getCarts()
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String sqlTable = "OrderDetail";
        String[] sqlColumns = {"ID", "Name", "ImageUrl", "Price", "Category", "Quantity", "Discount"};

        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlColumns, null, null, null, null, null);

        final List<SidelineOrderInfo> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                result.add(new SidelineOrderInfo(cursor.getString(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("ImageUrl")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Category")),
                        cursor.getInt(cursor.getColumnIndex("Quantity")),
                        cursor.getInt(cursor.getColumnIndex("Discount"))
                ));
            }
            while (cursor.moveToNext());
        }
        return result;
    }
    public boolean checkItemExists(String foodId, String userPhone){

        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        //String SQLQuery = String.format("SELECT * From OrderDetail WHERE ID='%s' AND ProductId='%s'", userPhone,foodId);
        String SQLQuery = String.format("SELECT * From OrderDetail WHERE ID='%s'", foodId);
        cursor = db.rawQuery(SQLQuery, null);
        if (cursor.getCount()>0)
            flag= true;
        else
            flag= false;
        cursor.close();
        return flag;
    }

    public void addToCart(SidelineOrderInfo order)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("INSERT INTO OrderDetail('ID', 'Name', 'ImageUrl', 'Price', 'Category' , 'Quantity', 'Discount')" +
                        "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                order.getId(), order.getSidelineName(), order.getImageURL(), order.getPrice(), order.getCategory(), order.getQuantity(), order.getDiscount());
        sqLiteDatabase.execSQL(query);
    }

    public void increaseCart(String Id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String price  = "";
        int quantity = 0, singlePrice = 0;
        String SQLQuery = String.format("SELECT * From OrderDetail WHERE ID='%s'", Id);
        cursor = db.rawQuery(SQLQuery, null);
        if (cursor.getCount()>0){
            price = cursor.getString(cursor.getColumnIndex("Price"));
            quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
            singlePrice = Integer.parseInt(price) * (quantity+1);
        }

        String query = String.format("UPDATE OrderDetail SET Quantity= Quantity+1 AND Price ="+singlePrice+"' WHERE ID = '%s'",Id);
       db.execSQL(query);

    }

    public void decreaseCart(String Id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String price  = "";
        int quantity = 0, singlePrice = 0;
        String SQLQuery = String.format("SELECT * From OrderDetail WHERE ID='%s'", Id);
        cursor = db.rawQuery(SQLQuery, null);
        if (cursor.getCount()>0){
            price = cursor.getString(cursor.getColumnIndex("Price"));
            quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
            singlePrice = Integer.parseInt(price) * (quantity-1);
        }

        String query = String.format("UPDATE OrderDetail SET Quantity= Quantity-1 AND Price = "+singlePrice+"' WHERE ID = '%s'",Id);
       db.execSQL(query);

    }

    public void cleanCart()
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);
    }


  /*  public void addToFavorite(String foodId)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("Insert INTO favorites VALUES('%s')", foodId);
        sqLiteDatabase.execSQL(query);
    }
    public List<Favorites> getAllFavorites(String userPhone) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","FoodId", "FoodName", "FoodPrice", "FoodMenuId", "FoodImage", "FoodDescription"};
        String sqlTable = "Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null);

        final List<Favorites> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Favorites(
                        c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("FoodMenuId")),
                        c.getString(c.getColumnIndex("FoodImage")),
                        c.getString(c.getColumnIndex("FoodDescription")),
                        c.getString(c.getColumnIndex("UserPhone"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void removeFromFavourites(String foodId, String userPhone) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId = '%s' and UserPhone = '%s' ;", foodId, userPhone);
        db.execSQL(query);
    }

    //Favourites
    public void addToFavourites(Favorites food) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(" +
                        "FoodId,FoodName,FoodPrice,FoodMenuId,FoodImage,FoodDescription,UserPhone)" +
                        "VALUES('%s','%s','%s','%s','%s','%s','%s');",
                food.getFoodId(),
                food.getFoodName(),
                food.getFoodPrice(),
                food.getFoodMenuId(),
                food.getFoodImage(),
                food.getFoodDescription(),
                food.getUserPhone());
        db.execSQL(query);
    }
    public void removeFromFavorites(String foodId)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format("DELETE FROM favorites WHERE foodId = '%s'", foodId);
        sqLiteDatabase.execSQL(query);
    }


    public boolean isFavorite(String foodId)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT * FROM favorites WHERE foodId = '%s'", foodId);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }
*/

    public int getOrderCount() {
        int count = 0;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do{
                count = cursor.getInt(0);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return count;
    }

 /*   public void updateCart(SidelineOrderInfo order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        @SuppressLint("DefaultLocale")
        String query = String.format("UPDATE OrderDetail SET Quantity = %s WHERE ID = %d", order.getQuantity(), order.getID());
        sqLiteDatabase.execSQL(query);
    }*/
}//class ends