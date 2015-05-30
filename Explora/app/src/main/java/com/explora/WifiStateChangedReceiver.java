package com.explora;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chirag.agrawal on 5/29/2015.
 */
public class WifiStateChangedReceiver extends BroadcastReceiver  {
    GoogleApiClient mGoogleApiClient;
    @Override
    public void onReceive(final Context context, Intent intent) {

        int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
                WifiManager.WIFI_STATE_UNKNOWN);

        switch(extraWifiState){
            case WifiManager.WIFI_STATE_DISABLED:
                Log.e("wifi receiver", "disabled");
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                Log.e("wifi receiver", "enabled");
                Location locationObject = Utils.getBestLocation(context);
                double latitude=locationObject.getLatitude();
                double longitude=locationObject.getLongitude();
                String location = String.valueOf(latitude) + "," + String.valueOf(longitude);
                Log.e("location",location);
                Utils.sendSMS("+918197823176",App.state + location,context);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }

}
