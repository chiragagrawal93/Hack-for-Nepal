package com.explora;

import android.app.Activity;
import android.support.v4.app.NotificationCompat;

/**
 * Created by chirag.agrawal on 5/29/2015.
 */
public class App {

    public static Activity currentActivity;
    public static String state;
    public static NotificationCompat.Builder mBuilder;
    public static int mId = 1;
    public static boolean markedSafe = false;
}
