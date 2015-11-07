package com.melam.shiva.datatracker;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class DataGathering extends Service {

    long previous, new1, temporary;
    String currentnumber;
    Timer timer;
    Runnable runnable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.v("Service","onCreate");
        previous = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        currentnumber = new PhoneNumber(getApplicationContext()).getPhonenumber();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.v("Service","Onstart command");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runnable.run();
            }
        }, 1000,60000);



        runnable = new Runnable() {
            public void run() {

                new1 = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
                temporary = new1  - previous;

                Log.v("values","new1"+new1 +" previous "+ previous);
                //to convert to MB
                temporary = temporary/(1048576);

                Log.v("Service", "Inside loop , Temp value is " + temporary);

                if(temporary > 0) {

                    Log.v("Service","greater than 0");

                    URL url = null;
                    try {
                        url = new URL(new IpAddress().getIPAddress() + "Plan/sendingdata/?UserPh="+currentnumber+"&UsedData="+temporary);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    Log.v("URL",""+url);
                    HttpURLConnection httpURLConnection;
                    httpURLConnection = null;
                    try {
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        httpURLConnection.connect();

                       /* JSONObject data = new JSONObject();
                        data.put("UserPh", currentnumber);
                        data.put("UsedData", temporary);
                        Log.v("MyService", "" + currentnumber + "data used in gap " + temporary);


                        Log.v("Json ", "" + data.toString());

                        OutputStreamWriter output_writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                        output_writer.write(data.toString());
                        Log.v("URL",""+url);
                        output_writer.flush();
                        */

                        Log.v("Service response", "" + httpURLConnection.getResponseCode());

                    /* Response */
                        if(httpURLConnection.getResponseCode() == 200)
                        {
                            Log.v("Respones 200", "http connect works" + temporary + " added");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    /* Closing the HTTP URL CONNECTION */
                        httpURLConnection.disconnect();
                    }

                }
                previous = new1;
            }
        };

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
        new Handler().removeCallbacks(runnable);
        super.onDestroy();
    }
}


