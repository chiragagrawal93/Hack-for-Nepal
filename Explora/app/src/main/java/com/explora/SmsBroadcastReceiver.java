package com.explora;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    Context context;
    static Timer timer;
    static Runnable runnable;
    static Handler handler;

    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String address = null;
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                address = smsMessage.getOriginatingAddress();
                if (address.equals("+918197823176")) {
                    String smsBody = smsMessage.getMessageBody().toString();
                    smsMessageStr += smsBody;
                }
            }
            if (address != null && address.equals("+918197823176")) {
                if (smsMessageStr.equals("DISASTERALERT")) {
                    Log.e("message", smsMessageStr);
                    App.mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.abc_btn_radio_material)
                                    .setContentTitle("Disaster Alert")
                                    .setContentText("Tap here to mark yourself safe.");
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(context, MarkedSafeService.class);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getService(context, 1354, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    App.mBuilder.setContentIntent(pendingIntent);
                    // mId allows you to update the notification later on.
                    mNotificationManager.notify(App.mId, App.mBuilder.build());
//                    Intent countdown = new Intent(context, CountDownService.class);
//                    context.getApplicationContext().startService(countdown);
//                    Utils.startTimer(App.mId,context,App.mBuilder);
//                    timer = new Timer();
//                    timer.schedule(doAsynchronousTask, 30000);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            /* do what you need to do */
                            Log.e("timer", "timertask called");
                            String ns = Context.NOTIFICATION_SERVICE;
                            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                            {
                                App.state = "HELP ";
                                nMgr.cancel(App.mId);


                                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                                if (!wifi.isWifiEnabled()) {
                                    final IntentFilter filters = new IntentFilter();
                                    filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                                    filters.addAction("android.net.wifi.STATE_CHANGE");
                                    context.getApplicationContext().registerReceiver(new WifiStateChangedReceiver(), filters);
                                    wifi.setWifiEnabled(true);
                                } else {
                                    Utils.getBestLocation(context);
                                }

//                                final IntentFilter filtersMobileData = new IntentFilter();
//                                filtersMobileData.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//                                filtersMobileData.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//                                context.getApplicationContext().registerReceiver(new ConnectivityBroadcastReceiver(), filtersMobileData);
//
//                                try {
//                                    Utils.setMobileDataEnabled(context, true);
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                } catch (NoSuchFieldException e) {
//                                    e.printStackTrace();
//                                } catch (IllegalAccessException e) {
//                                    e.printStackTrace();
//                                } catch (NoSuchMethodException e) {
//
//                                    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                                    if (!wifi.isWifiEnabled()) {
//                                        final IntentFilter filters = new IntentFilter();
//                                        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//                                        filters.addAction("android.net.wifi.STATE_CHANGE");
//                                        context.getApplicationContext().registerReceiver(new WifiStateChangedReceiver(), filters);
//                                        wifi.setWifiEnabled(true);
//                                    } else {
//                                        Utils.getBestLocation(context);
//                                    }
//                                    e.printStackTrace();
//                                } catch (InvocationTargetException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }
                    };
                    handler = new Handler();
                    handler.postDelayed(runnable, 30000);
                }
            }
        }
    }

    public static void stopTimer() {
        handler.removeCallbacks(runnable);
    }
}

//TimerTask doAsynchronousTask = new TimerTask() {
//    @Override
//    public void run() {
//
//    }
//
//    ;
//
//    public static void stopTimer() {
//        timer.cancel();
//    }
//}