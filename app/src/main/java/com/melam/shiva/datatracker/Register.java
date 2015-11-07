package com.melam.shiva.datatracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Register extends Activity {

    EditText inputFirstName;
    EditText inputLastName;
    EditText inputPhone;
    EditText inputPassword, planlimit, limit, startdate;
    Button btnRegister;
    String first_st,last_st, phone_st,password_st, limit_st, start_date_st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstName = (EditText) findViewById(R.id.register_firstname);
        inputLastName = (EditText) findViewById(R.id.register_lastname);
        inputPhone = (EditText) findViewById(R.id.register_phone);
        inputPassword = (EditText) findViewById(R.id.register_password);
        limit = (EditText) findViewById(R.id.register_limit);
        startdate = (EditText) findViewById(R.id.register_startdate);

        btnRegister = (Button) findViewById(R.id.register);

        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivity(myIntent);
                finish();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputPhone.getText().toString().equals(""))
                        && ( !inputPassword.getText().toString().equals(""))
                        && ( !inputFirstName.getText().toString().equals(""))
                        && ( !inputLastName.getText().toString().equals(""))
                        && ( !limit.getText().toString().equals(""))
                        && ( !startdate.getText().toString().equals(""))) {

                    Log.v("Register","Register Pressed!");
                    first_st = inputFirstName.getText().toString();
                    last_st = inputLastName.getText().toString();
                    phone_st = inputPhone.getText().toString();
                    password_st = inputPassword.getText().toString();
                    limit_st = limit.getText().toString();
                    start_date_st = startdate.getText().toString();
                    new RegSync().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    private class RegSync extends AsyncTask<URL, Integer, Long> {

        HttpURLConnection httpsURLConnection;

        @Override
        protected Long doInBackground(URL... params) {

            URL url = null;

            try {
                url = new URL(new IpAddress().getIPAddress()+ "SignIn/signup/");
            }catch (MalformedURLException e){

                Log.v("Login Async Task", "Malformed URL" + e.getMessage());
            }

            Log.v("URL",""+ url);

             httpsURLConnection = null;

            try{

                httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.connect();

                /*
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("UserPh", phone_st)
                        .appendQueryParameter("Fname", first_st)
                        .appendQueryParameter("Lname", last_st)
                        .appendQueryParameter("Passcode", password_st)
                        .appendQueryParameter("Ownerlimit", limit_st)
                        .appendQueryParameter("CycleDate", start_date_st);

                String query = builder.build().getEncodedQuery();

                OutputStream os = httpsURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                Log.v("URL", "" + url);

                httpsURLConnection.connect();
                */


                JSONObject data = new JSONObject();

                data.put("Fname", first_st);
                data.put("Lname", last_st);
                data.put("UserPh", phone_st);
                data.put("Passcode", password_st);
                data.put("Ownerlimit", limit_st);
                data.put("CycleDate", start_date_st);

                Log.v("Json",""+data);


                OutputStreamWriter output_writer = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                output_writer.write(data.toString());
                output_writer.flush();

                Log.v("Register",""+httpsURLConnection.getResponseCode());

            }catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            try {
                if (httpsURLConnection.getResponseCode() == 200) {
                    Intent in=new Intent(Register.this,Registered.class);
                    startActivity(in);
                }
            }catch (Exception e){
                    e.printStackTrace();
            }
        }
    }



}
