package com.melam.shiva.datatracker;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.provider.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ManageNetwork extends ActionBarActivity {


    private long rbytes = 0;
    private long tbytes = 0;
    private long sum_bytes = 0;

    private long mrbytes = 0;
    private long mtbytes = 0;
    private long msum_bytes = 0;

    String display_bytes = "";
    String mdisplay_bytes = "";


    ConnectivityManager cm1;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_network);

        Button settingIntent = (Button) findViewById(R.id.manageconnection_settingsintent_btn);

        Button netbutton = (Button)findViewById(R.id.button1);
        final AlertDialog.Builder build = new AlertDialog.Builder(this);

        cm1 = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);



        netbutton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v)
                    {
                        NetworkInfo ni1 = cm1.getActiveNetworkInfo();
                        build.setTitle("Network Connection");
                        if(ni1 != null)
                        {
                            build.setMessage(ni1.getTypeName());
                            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert11 = build.create();
                            alert11.show();
                        }
                        else{
                            build.setMessage("NO DATA CONNECTION");
                            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert11 = build.create();
                            alert11.show();
                        }
                    }//onclick
                }//button onclick
        );//netbutton listener






        settingIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_network, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
