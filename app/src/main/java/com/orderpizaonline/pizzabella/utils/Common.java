package com.orderpizaonline.pizzabella.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Common {

    public final static String VEGETABLES = "Vegetables";
    public final static String COLD_DRINKS = "Cold_Drinks";
    public final static String SIDE_LINES = "Sidelines";
    public final static String DIPS = "Dips";
    public final static String DRINKS = "Drinks";
    public final static String DOUGH = "Dough";
    public final static String CRUST = "Crust";
    public final static String Souce = "Souces";
    public final static String SPICE = "Spice";
    public final static String PIZZA = "Pizza";
    public final static String USERS = "Users";
    public final static String D_Coke = "Coke";
    public final static String D_Sprite = "Sprite";
    public final static String D_Fanta = "Fanta";

    public final static String HALF_SQUARE = "Half_Square";
    public final static String FULL_SQUARE = "Full_Square";
    public final static String FULL_CIRCLE = "Full_Circle";
    public final static String Right_HALF_CIRCLE = "Right_Half_Circle";
    public final static String Left_HALF_CIRCLE = "Left_Half_Circle";

    public final static String PIZZA_TYPE_SLICE = "Slice";
    public final static String PIZZA_TYPE_HALF_CIRCLE = "Half Circle";
    public final static String PIZZA_TYPE_FULL_CIRCLE = "Full Circle";
    public final static String PIZZA_TYPE_FULL_Square = "Full Square";
    public final static String PIZZA_TYPE_HALF_SQUARE = "Half Square";

    public final static String Add_VEGETABLES_F_TAG = "AddVegeFragment";
    public final static String Add_ITEMS_F_TAG = "AddItemsFragment";
    public final static String VEGE_F_TAG = "VegeFragment";
    public final static String Add_PIZZA_F_TAG = "AddPizzaFragment";
    public final static String SIDELINE_F_TAG = "SidelinesFragment";

    public final static String DELETE = "Delete";
    public final static String SIDELINE_CATEGORY = "category";


    public final static String COMBO = "COMBO";
    public final static String ORDERS = "ORDERS";
    public final static String PIZZA_INGRDIENT = "Pizza_Ingrdient";
    public final static String UPDATE = "Update";
    public final static String PIZZA_TOPPING = "PizzaToppings";

    public final static String FullSquare = "FullSquare";
    public final static String LeftTop = "LeftTop";
    public final static String LeftBottom = "LeftBottom";
    public final static String RightTop = "RightTop";
    public final static String RightBottom = "RightBottom";
    public final static String RightHalf = "RightHalf";
    public final static String LeftHalf = "LeftHalf";
    public final static String UserAddresses = "User_addresses";
    public final static String StoreAddresses = "Store_addresses";
    public final static String AdminEmail = "admin@gmail.com";
    public final static String Complete_TITLE = "New Order";
    public final static String Complete_MSG = "A new order is placed";

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null){

                for(int i=0; i<info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm"
                , calendar).toString());
        return date.toString();
    }

    public static Bitmap getCircleBitmap(Bitmap bm) {

        int sice = Math.min((bm.getWidth()), (bm.getHeight()));

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
       /* if (output!=null){
            selectedFull = output;
            selectedRightHalf = null;
            selectedLeftHalf = null;

        }*/
        return output;
    }

    public static Bitmap cropToRightCircleImageFunction(Bitmap bitmap){
        int side=0;
        if (bitmap.getHeight()<bitmap.getWidth()){
            side = bitmap.getHeight();

        }else {
            side = bitmap.getWidth();
        }

// calculate the x and y offset
        int xOffset = (bitmap.getWidth() - side) /2;
        int yOffset = (bitmap.getHeight() - side)/2;

// create a square bitmap
// a square is closed, two dimensional shape with 4 equal sides
/*return Bitmap.createBitmap(
bitmap, // source bitmap
xOffset, // x coordinate of the first pixel in source
yOffset, // y coordinate of the first pixel in source
200, // width
side // height
);*/

        return Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2,0,bitmap.getWidth()/2, side);
    }

    public static Bitmap cropToLeftCircleFunction(Bitmap bitmap){
        int side=0;
        if (bitmap.getHeight()<bitmap.getWidth()){
            side = bitmap.getHeight();

        }else {
            side = bitmap.getWidth();
        }

// calculate the x and y offset
        int xOffset = (bitmap.getWidth() - side) /2;
        int yOffset = (bitmap.getHeight() - side)/2;

// create a square bitmap
// a square is closed, two dimensional shape with 4 equal sides
        return Bitmap.createBitmap(
                bitmap, // source bitmap
                0, // x coordinate of the first pixel in source
                0, // y coordinate of the first pixel in source
                bitmap.getWidth()/2, // width
                side // height
        );
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }
    public static String getImagePath(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File file = new File("storage/emulated/0/Pictures");
        if (file.exists()) {

            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return path;
        }else {
            new File("/storage/emulated/0/Pictures").mkdirs();
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return path;
        }


    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    public static Bitmap cropBitmapFunction(Bitmap bitmap,int x,int y,int mWidth,int mHeight){

        return Bitmap.createBitmap(
                bitmap, // source bitmap
                x, // x coordinate of the first pixel in source
                y, // y coordinate of the first pixel in source
                mWidth, // width
                mHeight // height
        );
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String bitmapToUriConverter(Context context, Bitmap mBitmap) {
        Uri uri = null;

        String realPath = "";
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".png");
            FileOutputStream out = context.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            realPath = file.getAbsolutePath();
            // File f = new File(realPath);
           // uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return realPath;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }






    public final static String DrinkHalf = "Half Ltr";
    public final static String DrinkQuater = "325 ml";
    public final static String DrinkLtr = "1 Ltr";
    public final static String Drink1_5 = "1.5 Ltr";
    public final static String Drink2_25 = "2.25 Ltr";
    public final static String Drink_Can = "Can";
    public final static String Small = "Small";
    public final static String Large = "Large";
    public final static String NONE = "None";

    public final static String SOFT_DRINK = "Soft Drink";
    public final static String MINERAL_WATER = "Mineral Water";
    public final static String JUICES = "Juices";
    public final static String FRIED_ROLL = "Fried_Roll";
    public final static String BURGER_SANDWICH = "BurgerAndSandwich";
    public final static String CHINESE_CORNER = "Kids Mania";
    public final static String BEVERAGES = "Beverages";
    public final static String MEAL = "Meal";

    public final static String SUMMER_SPECIAL = "Summer Special";
    public final static String WINTER_SPECIAL = "Winter Special";

    public final static String PERSONAL = "Personal";
    public final static String SMALLL = "Small";
    public final static String MEDIUM = "Medium";
    public final static String LARGE = "Large";
    public final static String EXTRA_LARGE = "Extra Large";
    public final static String FIESTA = "Fiesta";
}
