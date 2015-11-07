package com.melam.shiva.datatracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class ConnectivityChange extends BroadcastReceiver {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    @Override
    public void onReceive(Context context, Intent intent) {

        connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        if(networkInfo!=null){

            //Check for which type of connection
            if(networkInfo.getTypeName().equalsIgnoreCase("wifi")){


                Intent intent1 = new Intent(context, DataGathering.class);
                context.startService(intent1);
                Log.v("Inside reciever","trying to start a serivce");
            }
        }else
        {

            Intent intent1 = new Intent (context, DataGathering.class);
            context.stopService(intent1);
        }


    }



}

