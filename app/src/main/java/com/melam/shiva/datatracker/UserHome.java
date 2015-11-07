package com.melam.shiva.datatracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class UserHome extends ActionBarActivity {

    ArrayList<String> graphstats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Button graph = (Button) findViewById(R.id.userhome_getgraph);
        Button manageconnection =(Button) findViewById(R.id.userhome_manageconnection);


        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new getGraphAsync().execute();
            }
        });


        manageconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
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

    private class getGraphAsync extends AsyncTask<URL, Integer, Long> {
        HttpURLConnection http1;
        @Override
        protected Long doInBackground(URL... params) {

            String currentselected = new PhoneNumber(UserHome.this).getPhonenumber();
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
                    String result = "";
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