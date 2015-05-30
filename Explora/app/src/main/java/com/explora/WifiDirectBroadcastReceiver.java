package com.explora;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 5/29/2015.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    Context context;

    List peers = new ArrayList();

    public WifiDirectBroadcastReceiver(WifiP2pManager m, WifiP2pManager.Channel c)
    {
        super();
        manager=m;
        channel=c;
    }
    @Override
    public void onReceive(final Context context, Intent intent) {

        String action= intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if (state==WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {

            }
            else
            {
//                Method method1 = null;
//                try {
//                    method1 = manager.getClass().getMethod("enableP2p", WifiP2pManager.Channel.class);
//                    method1.invoke(manager, channel);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
            }

        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

            // The peer list has changed!  We should probably do something about
            // that.
            if(manager!=null)
            {
                manager.requestPeers(channel,new WifiP2pManager.PeerListListener() {
                    WifiP2pDevice actual_device;
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        Log.e("Device List here","Now");
                        Log.e("Devices",""+peers.getDeviceList().size());
                        for(WifiP2pDevice device:peers.getDeviceList())
                        {
                            String name=device.deviceName;
                            Log.e("Device Name",name);
                            if(name.equals("XT1033_47a2"))
                            {
                                actual_device=device;
                                break;
                            }
                        }
                        Log.e("Device",actual_device.deviceName);
                        WifiP2pConfig config=new WifiP2pConfig();
                        config.deviceAddress=actual_device.deviceAddress;
                        manager.connect(channel,config,new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context,"Connection Successful",Toast.LENGTH_LONG).show();
                                Log.e("Connection","Success");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.e("Connection","Failure");
                            }
                        });

                    }
                });

            }

        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.

        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {

        }
    }
}
