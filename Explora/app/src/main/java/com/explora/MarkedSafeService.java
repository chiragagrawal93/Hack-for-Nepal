package com.explora;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chirag.agrawal on 5/30/2015.
 */
public class MarkedSafeService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public MarkedSafeService() {
        super("MarkedSafeService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        App.state = "SAFE ";
        App.markedSafe = true;

        if(App.mBuilder !=null)
        {
            Log.e("markedsafe","onHandle");
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getSystemService(ns);
            nMgr.cancel(App.mId);
 //           SmsBroadcastReceiver.stopTimer();
              SmsBroadcastReceiver.stopTimer();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String locationString = String.valueOf(latitude) + "," + String.valueOf(longitude);
            Log.e("location",locationString);
            Utils.sendSMS("+918197823176",App.state + locationString,this);
        }
    }
}
