package com.example.mylostdevice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;

import android.content.Context;

import android.widget.ProgressBar;
import android.widget.Toast;
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
public class ActivityLogin extends Activity{

    private EditText loginemail;
    private EditText loginpass;

    private ProgressBar pb;

    int userid_g;
    int devid_g;
    int exist_dev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginemail = (EditText) findViewById(R.id.loginemail);
        loginpass = (EditText) findViewById(R.id.loginpass);

        pb=(ProgressBar)findViewById(R.id.progressBarLogin);
        pb.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void actionLogin(View v){

        pb.setVisibility(View.VISIBLE);

        Thread TLoginUsu = new Thread(new CheckLoginRunnable(loginemail.getText().toString(),loginpass.getText().toString()));
        TLoginUsu.start();

        while(TLoginUsu.isAlive());

        SharedPreferences preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(userid_g == 0){
            tToast("Incorrect Data");
            return;
        }
        else{
            editor.putInt("userid",  userid_g);
            editor.commit();
        }

        //DEVICE nuevo???

        exist_dev = -1;

        if(preferences.contains("devid")){

            devid_g= preferences.getInt("devid",0);

            Thread TSearchDev = new Thread(new CheckLoginRunnable(String.valueOf(userid_g), String.valueOf(devid_g)));
            TSearchDev.start();

            while(TSearchDev.isAlive());
        }

        if(exist_dev != 1){

            String devType = android.os.Build.MANUFACTURER +" - "+ System.getProperty("os.version");
            String devLocation = "0";

            Thread TRegistroDev = new Thread(new AddDeviceRunnable(String.valueOf(userid_g),devType,devLocation));
            TRegistroDev.start();

            while(TRegistroDev.isAlive());

            editor.putInt("devid",  devid_g);
            editor.commit();
        }

        pb.setVisibility(View.GONE);

        Intent i=new Intent(this,ActivityTabs.class);
        startActivity(i);

        tToast("Login Successful");

    }

    public void actionGoRegister(View v){

        Intent i=new Intent(this,ActivityRegister.class);
        startActivity(i);

        tToast("Going to register page");
    }

    private class CheckLoginRunnable implements Runnable {

        String email,pass;

        CheckLoginRunnable(String email, String pass)
        {
            this.email=email;
            this.pass=pass;
        }

        @Override
        public void run() {

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost =new HttpPost("http://relguzman.com/MyLostDevice/controller_client/corUserCheck.php");

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


                userid_g =  Integer.parseInt(obj.getString("resp"));


                Log.e("addReg", obj.getString("resp"));
                //Log.e("addReg", String.valueOf(preferences.getInt("userid",0)));

            } catch (Exception e){

                Log.e("addReg", "Exception " + e.toString());
            }
        }
    }



    private class SearchDevRunnable implements Runnable {

        String userid,devid;

        SearchDevRunnable(String userid, String devid)
        {
            this.userid=userid;
            this.devid=devid;
        }

        @Override
        public void run() {

            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost =new HttpPost("http://relguzman.com/MyLostDevice/controller_client/corDeviceSearch.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("userid", userid));
                nameValuePairs.add(new BasicNameValuePair("devid", devid));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                ByteArrayOutputStream out=new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String payload=out.toString();

                JSONObject obj=new JSONObject(payload);


                exist_dev =  Integer.parseInt(obj.getString("resp"));


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


                devid_g =  Integer.parseInt(obj.getString("resp"));


                Log.e("addReg", "D - "+ obj.getString("resp"));
                //Log.e("addReg", String.valueOf(preferences.getInt("userid",0)));

            } catch (Exception e){

                Log.e("actReg", "Exception " + e.toString());
            }
        }
    }

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

}
