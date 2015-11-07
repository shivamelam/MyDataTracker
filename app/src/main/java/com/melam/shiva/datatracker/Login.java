package com.melam.shiva.datatracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class Login extends ActionBarActivity{

    Button btnLogin;
    Button Btnregister;
    EditText inputPhone;
    EditText inputPassword;
    String phone, password;
    HttpURLConnection httpsURLConnection;
    TelephonyManager telephonyManager;
    String currentDeviceNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhone = (EditText) findViewById(R.id.phno);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        inputPhone.setText("" + new PhoneNumber(getApplicationContext()).getPhonenumber());


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (  ( !inputPhone.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {

                    phone = inputPhone.getText().toString();
                    password = inputPassword.getText().toString();


                    new LoginAsync().execute();
                    Log.v("onclick", "User name and Password" + phone + "  " + password);

                }
                else if ( ( !inputPhone.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Phone field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Phone and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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



    private class LoginAsync extends AsyncTask <URL, Integer, Long>{


        @Override
        protected Long doInBackground(URL... params) {

            Log.v("Async task","User name and Password"+ phone + "  "+ password);
            currentDeviceNumber = new PhoneNumber(getApplicationContext()).getPhonenumber();
            URL url = null;


            try {
                url = new URL(new IpAddress().getIPAddress()+ "SignIn/signin/?UserPh="+phone+"&Passcode="+password+"&LocalPh="+currentDeviceNumber);
            }catch (MalformedURLException e){

                Log.v("Login Async Task", "Malformed URL" + e.getMessage());
            }
            Log.v("Login Async Task", ""+url);


             httpsURLConnection = null;

            try{
                httpsURLConnection = (HttpURLConnection) url.openConnection();
                httpsURLConnection.connect();
                Log.v("Login",""+httpsURLConnection.getResponseCode());
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

                    if(phone.equalsIgnoreCase(currentDeviceNumber.substring(currentDeviceNumber.length() - 10))){
                        // owner screen

                        startActivity(new Intent(Login.this, Settings.class));
                        Log.v("Login","Owner Screen");
                    }else
                    {
                        startActivity(new Intent(Login.this, UserHome.class));

                        // user screen
                        Log.v("Login","User Screen");

                    }

                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
