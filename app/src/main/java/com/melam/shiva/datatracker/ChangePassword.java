package com.melam.shiva.datatracker;

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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ChangePassword extends ActionBarActivity {

    String realpassword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText password = (EditText) findViewById(R.id.changepass_et1);

        final EditText repassword = (EditText) findViewById(R.id.changepass_et2);
        Button change = (Button) findViewById(R.id.change_changepassword);



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().equalsIgnoreCase(repassword.getText().toString())){

                    realpassword = password.getText().toString();
                    new changepass().execute();
                }

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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


    private class changepass extends AsyncTask<URL, Integer, Long> {

        HttpURLConnection http;
        @Override
        protected Long doInBackground(URL... params) {

            String phonenumber = new PhoneNumber(ChangePassword.this).getPhonenumber();
            URL url = null;
            try{

                url = new URL (new IpAddress().getIPAddress()+"EditDevice/change_passcode/?UserPh="+phonenumber+"&Passcode="+realpassword);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            Log.v("URl", "" + url);
             http = null;
            try {
                http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                /* While using HTTP url connection use setOutput(true) for "POST" verb, for other verbs use setRequestMethod(Verb)*/
                http.connect();
                Log.v("","Responsecode is"+ http.getResponseCode());

            }catch (Exception e){
                e.printStackTrace();
            }
                return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            try {
                if (http.getResponseCode() == 200)
                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
