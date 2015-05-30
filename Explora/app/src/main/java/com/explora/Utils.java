package com.explora;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chirag.agrawal on 5/29/2015.
 */
public class Utils {
    public static String TAG = "Utils";
    static CountDownTimer countDownTimer;
    public static void sendSMS(String phoneNumber, String message, final Context context) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        Log.e(phoneNumber, message);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


    public static void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }

    /**
     * try to get the 'best' location selected from all providers
     */
    public static Location getBestLocation(Context context) {
        Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER, context);
        Location networkLocation =
                getLocationByProvider(LocationManager.NETWORK_PROVIDER, context);
        // if we have only one location available, the choice is easy
        if (gpslocation == null) {
            Log.d(TAG, "No GPS Location available.");
            return networkLocation;
        }
        if (networkLocation == null) {
            Log.d(TAG, "No Network Location available");
            return gpslocation;
        }

//        }
        return networkLocation;
    }

    /**
     * get the last known location from a specific provider (network/gps)
     */
    public static Location getLocationByProvider(String provider, Context context) {
        Location location = null;
//        if (!isProviderSupported(provider)) {
//            return null;
//        }
        LocationManager locationManager = (LocationManager) context.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Cannot acces Provider " + provider);
        }
        return location;
    }

    public static void fetchDataFromUrl(final String url, final HttpListener listener) {

        if (listener == null) {
            return;
        }

        final String LOGTAG = "Url called";
        new Thread(new Runnable() {

            @Override
            public void run() {
                String data = "";

                {

                    Log.d("Url called", "Final URL = " + url);

                    try {
                        HttpGet get = new HttpGet(url);

                        HttpClient client = new DefaultHttpClient();
                        HttpResponse response = client.execute(get);
                        int status = response.getStatusLine().getStatusCode();
                        InputStream dataStream = response.getEntity().getContent();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataStream));
                        String responseLine;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((responseLine = bufferedReader.readLine()) != null) {
                            responseBuilder.append(responseLine);
                        }

                        data = responseBuilder.toString();

                        if (data != null) {
                            listener.success(data);
                        } else {
                            listener.failed("Null data received");
                        }

                        if (status == HttpStatus.SC_UNAUTHORIZED) {
                            Log.e(LOGTAG, "Server said Unauthorized. Url :" + url);
                            listener.failed("Unauthorized");
                        } else if (status != HttpStatus.SC_OK) {
                            Log.e(LOGTAG, "Error " + status + " " + org.apache.commons.httpclient.HttpStatus.getStatusText(status) + " data : " + data);
                            Log.e(LOGTAG, "Url was : " + url);
                            listener.failed("Error " + status + " " + org.apache.commons.httpclient.HttpStatus.getStatusText(status) + " data : " + data);
                        }
                    } catch (ClientProtocolException e) {
                        Log.e(LOGTAG, "ClientProtocolException", e);
                        listener.failed("ClientProtocolException ");
                    } catch (IOException e) {
                        Log.e(LOGTAG, "IOException ", e);
                        listener.failed("IOException ");
                    } catch (StackOverflowError e) {
                        Log.e(LOGTAG, "StackOverflowError ", e);
                        listener.failed("StackOverflowError ");
                    } catch (IllegalArgumentException e) {
                        Log.e(LOGTAG, "IllegalArgumentException ", e);
                        listener.failed("IllegalArgumentException ");
                    } catch (IllegalStateException e) {
                        Log.e(LOGTAG, "IllegalStateException ", e);
                        listener.failed("IllegalStateException ");
                    } catch (Exception e) {
                        Log.e(LOGTAG, "IllegalStateException ", e);
                        listener.failed("IllegalStateException ");
                    }
                }
            }
        }).start();

    }

    public interface HttpListener {
        public void success(String data);

        public void failed(String reason);
    }
}
