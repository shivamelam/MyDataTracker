package com.melam.shiva.datatracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BillingCycle extends ActionBarActivity {

    DatePicker pickdate;
    Button update;
    int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_cycle);

        pickdate = (DatePicker) findViewById(R.id.datePicker);
        update = (Button) findViewById(R.id.billingcycle_button);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), pickdate.getDayOfMonth() + "/" + pickdate.getMonth() + "/" + pickdate.getYear(), Toast.LENGTH_SHORT).show();
                new BillingCycleAsync().execute();
            }


        });

    }

    private void Dialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setMessage("Billing Day Changed!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(BillingCycle.this, Settings.class));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_billing_cycle, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class BillingCycleAsync extends AsyncTask<URL, Integer, Long> {


        HttpURLConnection httpURLConnection;

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;
            try{
                url = new URL ("http://www.google.com");
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

             httpURLConnection = null;
            try{
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                JSONObject date = new JSONObject();
               // date.put("Day", dayOfMonth);

                OutputStreamWriter  output_writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                output_writer.write(date.toString());
                output_writer.flush();


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {


            try{
                    if(httpURLConnection.getResponseCode() == 200)
                    {
                        Dialog();
                    }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}