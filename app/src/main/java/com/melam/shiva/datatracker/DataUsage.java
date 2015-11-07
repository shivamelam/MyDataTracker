package com.melam.shiva.datatracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DataUsage extends ListActivity {

    String result = "", currentselected="";
    ListView listView;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> quotaset = new ArrayList<>();
    ArrayList<String> quotaused = new ArrayList<>();
    ArrayList<String> phonenumber = new ArrayList<>();
    ArrayList<String> graphstats = new ArrayList<>();

    ArrayAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        listView = getListView();
        new ListOfDevicesAsync().execute();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(DataUsage.this);
                builder.setCancelable(true)
                        .setTitle("Stastics")
                        .setMessage("Data Limt"+ quotaset.get(position) + "  Data Used"+ quotaused.get(position))
                        .setPositiveButton("ok!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }

                        }).setNegativeButton("Get Montly Graph", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        currentselected = phonenumber.get(position);

                        new getGraphAsync().execute();


                    }

                });


                AlertDialog dialog = builder.create();
                dialog.show();


                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_usage, menu);
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


    private class ListOfDevicesAsync extends AsyncTask<URL, Integer, Long> {

        HttpURLConnection http = null;

        @Override
        protected Long doInBackground(URL... params) {

            String phonenum = new PhoneNumber(getApplicationContext()).getPhonenumber();

            Log.v("Inside Async","Inside Async");
            URL url = null;
            try {

                url = new URL(new IpAddress().getIPAddress() + "Owner/familydetails/?LocalPh=" + phonenum);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection http = null;
            try {

                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                Log.v("Inside Async", ""+url);

                if (http.getResponseCode() == 200) {

                    Log.v("Post Execute", "Response message" + http.getResponseCode());

                    InputStream in = http.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((result = reader.readLine()) != null) {

                        buffer.append(result + "\n");
                    }
                    result = buffer.toString();


                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject;


                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        name.add(jsonObject.getString("Fname"));
                        quotaset.add(jsonObject.getString("Ownerlimit"));
                        quotaused.add(jsonObject.getString("UsedData"));
                        phonenumber.add(jsonObject.getString("UserPh"));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            itemsAdapter = new ArrayAdapter<String>(DataUsage.this, android.R.layout.simple_list_item_1, name);
            listView.setAdapter(itemsAdapter);
        }
    }

    private class getGraphAsync extends AsyncTask<URL, Integer, Long> {
        HttpURLConnection http1;
        @Override
        protected Long doInBackground(URL... params) {

            Log.v("Inside Async","Inside Async");
            URL url = null;
            try {

                url = new URL(new IpAddress().getIPAddress() + "Owner/usagedetails/?LocalPh="+currentselected +"&Duration=Monthly");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.v("Url",""+url);
             http1 = null;
            try {


                http1 = (HttpURLConnection) url.openConnection();
                http1.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http1.connect();

                Log.v("Inside Async", "" + url);
                Log.v("Inside Async", "" + http1.getResponseCode());

                if (http1.getResponseCode() == 200) {

                    Log.v("Post Execute", "Response message" + http1.getResponseCode());

                    InputStream in = http1.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((result = reader.readLine()) != null) {

                        buffer.append(result + "\n");
                    }
                    result = buffer.toString();

                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject;


                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        graphstats.add(jsonObject.getString("UsedData"));
                    }

                }

            }catch (Exception e){

                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Long aLong) {

            try{
            if(http1.getResponseCode()==200)
            {
                Log.v("Inside post execute", "Inside post execute");
                BarGraph barGraph = new BarGraph();

                Intent intent = barGraph.getIntent(getApplicationContext(),graphstats);
                startActivity(intent);
                graphstats.clear();

            }
            }catch (Exception e){
                e.getMessage();

            }
        }
    }


}