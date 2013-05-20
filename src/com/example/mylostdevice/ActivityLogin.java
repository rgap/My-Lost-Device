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

    int userid;

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

        Thread TLoginUsu = new Thread(new CheckLoginRunnable(loginemail.getText().toString(),loginpass.getText().toString()));
        TLoginUsu.start();

        while(TLoginUsu.isAlive());

        if(userid == 0){
            tToast("Incorrect Data");
            return;
        }
        else{

            SharedPreferences preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userid",  userid);
            editor.commit();

            Intent i=new Intent(this,ActivityTabs.class);
            startActivity(i);

            tToast("Login Successful");
        }



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
            HttpPost httppost =new HttpPost("http://192.168.1.34/MyLostDevice/controller_client/corUserCheck.php");

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

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

}
