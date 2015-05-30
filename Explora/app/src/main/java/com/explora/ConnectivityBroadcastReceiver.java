package com.explora;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by chirag.agrawal on 5/29/2015.
 */
public class ConnectivityBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = new NetworkUtil().getConnectivityStatusString(context);

        if (status.equals("Mobile data enabled"))
        {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if(!wifi.isWifiEnabled())
            {
                final IntentFilter filters = new IntentFilter();
                filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                filters.addAction("android.net.wifi.STATE_CHANGE");
                context.getApplicationContext().registerReceiver(new WifiStateChangedReceiver(), filters);
                wifi.setWifiEnabled(true);
            }
            else
            {
                Location locationObject = Utils.getBestLocation(context);
                double latitude=locationObject.getLatitude();
                double longitude=locationObject.getLongitude();
                String location = String.valueOf(latitude) + "," + String.valueOf(longitude);
                Log.e("location", location);
                Utils.sendSMS("+918197823176", App.state + location, context);
            }
        }
    }

    private class NetworkUtil {

        public  int TYPE_WIFI = 1;
        public  int TYPE_MOBILE = 2;
        public  int TYPE_NOT_CONNECTED = 0;


        public  int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }

        public  String getConnectivityStatusString(Context context) {
            int conn = getConnectivityStatus(context);
            String status = null;
            if (conn == TYPE_WIFI) {
                status = "Wifi enabled";
            } else if (conn == TYPE_MOBILE) {
                status = "Mobile data enabled";
            } else if (conn == TYPE_NOT_CONNECTED) {
                status = "Not connected to Internet";
            }
            return status;
        }
    }
}
