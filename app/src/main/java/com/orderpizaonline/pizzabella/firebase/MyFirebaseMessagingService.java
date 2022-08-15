package com.orderpizaonline.pizzabella.firebase;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orderpizaonline.pizzabella.R;
import com.orderpizaonline.pizzabella.model.UserInfo;
import com.orderpizaonline.pizzabella.ui.SplashActivity;
import com.orderpizaonline.pizzabella.utils.Session;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Session session;
    Intent intent;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAGG", " MyFirebaseMessagingService   Token is:" + s);
        session=new Session(getApplicationContext());
        if (s==null || s.equals("")){
            Log.d("TOKEN", " MyFirebaseMessagingService   Token is:" + s);
        }else {
            Session.setFCMToken(s);
            updateFCM(s);
        }


    }
    public void showNotification(String title, String message) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Start without a delay
// Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

// The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        v.vibrate(pattern, -1);
        //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/ring");
        final Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_pizza)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(contentIntent)
                .setContentText(message);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("TAG", remoteMessage.getNotification().getTitle());
        Log.d("TAG", remoteMessage.getNotification().getBody());
        Log.d("TAG", remoteMessage.getData().toString());
        Log.i("DATA", remoteMessage.getData().toString());
        // get a list of running processes and iterate through them
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
// get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        Log.i("current task :", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());

    }
    public void updateFCM(String token) {
        UserInfo vegeInfo = null;
        if (Session.getUserInfo() != null) {
            vegeInfo = Session.getUserInfo();
        }
        if (vegeInfo!=null){
            vegeInfo.setFcmToken(token);
            Session.setUserInfo(vegeInfo);
        }
        Log.i("FCM", "updateFCM of class MyFirebaseMessagingSevice: token is:" + token);

    }
  /*  public void startAlert(){
        int i =60*60;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Log.i("onMessage", "Alarm set in " + i + " seconds");
       // Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }*/
}
