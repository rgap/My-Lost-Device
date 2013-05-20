package com.example.mylostdevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.*;
import android.view.Menu;
import android.view.View;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgap on 18/05/13.
 */
public class ActivityRegister extends Activity {

    private EditText regemail;
    private EditText regpass;

    private ProgressBar pb;

    //Variables globales

    private int userid;
    private int devid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regemail = (EditText) findViewById(R.id.regemail);
        regpass = (EditText) findViewById(R.id.regpass);


        pb=(ProgressBar)findViewById(R.id.progressBarRegister);

        pb.setVisibility(View.GONE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void actionRegister(View v){

        pb.setVisibility(View.VISIBLE);

        Thread TRegistroUsu = new Thread(new AddUserRunnable(regemail.getText().toString(),regpass.getText().toString()));
        TRegistroUsu.start();


        while(TRegistroUsu.isAlive());

        SharedPreferences preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userid",  userid);
        editor.commit();


        //INSERTAR DEVICE

        String devType = android.os.Build.MANUFACTURER +" - "+ System.getProperty("os.version");
        String devLocation = "0";

        Thread TRegistroDev = new Thread(new AddDeviceRunnable(String.valueOf(userid),devType,devLocation));
        TRegistroDev.start();

        while(TRegistroDev.isAlive());

        editor.putInt("devid",  devid);
        editor.commit();


        pb.setVisibility(View.GONE);

        tToast("Device and User Registered");

        Intent i=new Intent(this,ActivityTabs.class);
        startActivity(i);

        tToast("TABS");

    }

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    private class AddUserRunnable implements Runnable {

        String email,pass;

        AddUserRunnable(String email, String pass)
        {
            this.email=email;
            this.pass=pass;
        }

        @Override
        public void run() {

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost =new HttpPost("http://192.168.1.34/MyLostDevice/controller_client/corUserInsert.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", email));
                nameValuePairs.add(new BasicNameValuePair("password", pass));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                ByteArrayOutputStream out=new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String payload=out.toString();

                JSONObject obj=new JSONObject(payload);


                userid =  Integer.parseInt(obj.getString("resp"));


                Log.e("addReg", obj.getString("resp"));
                //Log.e("addReg", String.valueOf(preferences.getInt("userid",0)));

            } catch (Exception e){

                Log.e("addReg", "Exception " + e.toString());
            }
        }
    }


    private class AddDeviceRunnable implements Runnable {

        String userid,devlocation,devtype;

        AddDeviceRunnable(String userid, String devtype, String devlocation)
        {
            this.userid=userid;
            this.devtype=devtype;
            this.devlocation=devlocation;

        }

        @Override
        public void run() {

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost =new HttpPost("http://192.168.1.34/MyLostDevice/controller_client/corDeviceInsert.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("userid", userid));
                nameValuePairs.add(new BasicNameValuePair("devtype", devtype));
                nameValuePairs.add(new BasicNameValuePair("devlocation", devlocation));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                ByteArrayOutputStream out=new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String payload=out.toString();

                JSONObject obj=new JSONObject(payload);


                devid =  Integer.parseInt(obj.getString("resp"));


                Log.e("addReg", "D - "+ obj.getString("resp"));
                //Log.e("addReg", String.valueOf(preferences.getInt("userid",0)));

            } catch (Exception e){

                Log.e("actReg", "Exception " + e.toString());
            }
        }
    }
}

