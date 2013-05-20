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

/**
 * Created by rgap on 18/05/13.
 */
public class ActivityLogin extends Activity{

    private EditText loginemail;
    private EditText loginpass;

    private ProgressBar pb;

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


        Intent i=new Intent(this,ActivityTabs.class);
        startActivity(i);

        tToast("Login Successful");
        //tToast("Device Registered");
    }

    public void actionGoRegister(View v){

        Intent i=new Intent(this,ActivityRegister.class);
        startActivity(i);

        tToast("Going to register page");
    }

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

}
