package com.example.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class HardwareButtonReceiver extends BroadcastReceiver {
    NotificationManagerCompat managerCompat;
    NotificationCompat.Builder builder;

    private static final int NOTIFICATION_PERMISSION_CODE = 2;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            Log.d("Battery", "Battery Low");
            builder = new NotificationCompat.Builder(context, "Wifi warning");
            builder.setContentTitle("Warning");
            builder.setContentText("Battery low. Be sure to save your draft before loosing power!");
        /*
        if (checkInternet(context)) {
            //NOTIFICATION
            Log.d("Wifi", "Connected");
            builder.setContentTitle("Connected");
            builder.setContentText("Application is ready to send emails");
        }
        else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
            Log.d("Wifi", "Disconnected");
            builder.setContentTitle("Lost Connection");
            builder.setContentText("All emails will be queued and send when connection is available");
        }
         */
            builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
            builder.setAutoCancel(true);

            managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }
    }

    //private boolean checkInternet(Context context) {
    //2 line bellow is for the using serviceManager
        /*
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
         */
        //3 line bellow is second alternative
        /*
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
         */
        //4 line bellow is fourth alternative
        /*
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return ((wifi != null && wifi.isAvailable()) || (mobile != null && mobile.isAvailable()));
         */
    //}

}
