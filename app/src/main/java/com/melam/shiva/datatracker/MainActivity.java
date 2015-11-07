package com.melam.shiva.datatracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);


                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);

                }
            }
        };

        // start thread
        background.start();
    }


}
