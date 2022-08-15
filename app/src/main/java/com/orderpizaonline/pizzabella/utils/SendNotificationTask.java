package com.orderpizaonline.pizzabella.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendNotificationTask extends AsyncTask<String, Void, String> {
    static String response;
    Context context;
    String deviceId,title,message;
    public final static String AUTH_KEY_FCM = "AAAAT21G630:APA91bFcDFSFmMgjmYApN6d3O55n7ocs7uV3D4CVYAXFzGmuBddq2ztxkbimIbL0HwZeldXNFN7k4C64dHdcEy7px8psG99937I986S9mFezb03AuKrjHHSPdr7xJSM9twL8kILXtwqr";

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public SendNotificationTask(Context context,String userDeviceIdKey, String title, String message) {
        this.context = context;
      this.deviceId=userDeviceIdKey;
        Log.d("TOKEN", "SendNotificationTask: "+userDeviceIdKey);
      this.title=title;
      this.message=message;
    }

    @Override
    protected void onPreExecute() {
        Log.d("TAG", "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String address=null;
        try {
            String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
            String FMCurl = API_URL_FCM;

            URL url;
            url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            Log.d("TOKEN", "deviceId: "+deviceId+"   token:"+ Session.getToken());
            JSONObject json = new JSONObject();
            json.put("to", deviceId);
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
            info.put("text", message); // Notification body
            json.put("notification", info);
            System.out.println(json.toString());

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
        } catch (Exception e) {
            Log.d("TAG", "Exception in pushFCMNotification: " + e.getMessage());
        }

        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s!=null){

        }else {
            Log.d("TTAG", " In   ..... onPostExecute: ");
        }
    }
}
