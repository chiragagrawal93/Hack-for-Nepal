package com.javapapers.androidreceivesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    String MESSAGE_URL = "http://192.168.2.3:8000/exploraapp/data/";
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String address = null;
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
                smsMessageStr += smsBody;
            }
            Log.e("message", smsMessageStr);
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            String latitude;
            String longitude;
            String[] array = smsMessageStr.split(" ");
            Log.e("array[0]",array[0]);
            if(array[0].equals("HELP") || array[0].equals("SAFE"))
            {
                String[] location = array[1].split(",");
                latitude = location[0];
                longitude = location[1];
                if(address.startsWith("+"))
                    address = address.substring(3);
                MESSAGE_URL += address + "/" + latitude + "/" + longitude + "/" + array[0];
                Log.e("url",MESSAGE_URL);
                Utils.fetchDataFromUrl(MESSAGE_URL, new Utils.HttpListener() {
                    @Override
                    public void success(String data) {
                        Log.e("data sent",data);
                    }

                    @Override
                    public void failed(String reason) {
                        Log.e("data sent failed",reason);
                    }
                });
            }
        }
    }
}