package com.rgap.mylostdevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.*;
import android.view.Menu;
import android.view.View;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

    private int registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regemail = (EditText) findViewById(R.id.regemail);
        regpass = (EditText) findViewById(R.id.regpass);


        pb=(ProgressBar)findViewById(R.id.progressBarRegister);

        pb.setVisibility(View.GONE);

        registerUser = 1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean notFilledData(){
        if(regemail.getText().toString().equals("") || regpass.getText().toString().equals(""))
            return true;

        return false;
    }

    public void actionRegister(View v){

        //Check spaces
        if(notFilledData()){
            tToast("You must fill all spaces");
            return;
        }


        pb.setVisibility(View.VISIBLE);


        Thread TRegistroUsu = new Thread(new AddUserRunnable(regemail.getText().toString(),regpass.getText().toString()));
        TRegistroUsu.start();

        while(TRegistroUsu.isAlive());


        if(registerUser == 0){
            pb.setVisibility(View.GONE);

            tToast("User already registered");

            return;
        }

        SharedPreferences preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userid",  userid);
        editor.commit();


        //INSERTAR DEVICE

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String h = Integer.toString(metrics.heightPixels);
        String w = Integer.toString(metrics.widthPixels);

        String devType = android.os.Build.MANUFACTURER + ", Dimension: "+ h + "x" + w + ", Android: "+ System.getProperty("os.version");
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
            HttpPost httppost =new HttpPost("http://relguzman.com/MyLostDevice/controller_client/corUserInsert.php");

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

                if(obj.getString("resp").equals("0")) registerUser = 0;
                else{
                    userid =  Integer.parseInt(obj.getString("resp"));
                    registerUser = 1;
                }


                //Log.e("addReg", obj.getString("resp"));
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
            HttpPost httppost =new HttpPost("http://relguzman.com/MyLostDevice/controller_client/corDeviceInsert.php");

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


                //Log.e("addReg", "D - "+ obj.getString("resp"));
                //Log.e("addReg", String.valueOf(preferences.getInt("userid",0)));

            } catch (Exception e){

                Log.e("actReg", "Exception " + e.toString());
            }
        }
    }

}

