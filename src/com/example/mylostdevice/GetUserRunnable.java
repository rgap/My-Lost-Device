package com.example.mylostdevice;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by rgap on 19/05/13.
 */

public class GetUserRunnable implements Runnable {

    CharSequence id;

    GetUserRunnable(CharSequence id)
    {
        this.id=id;
    }

    @Override
    public void run() {

        HttpClient cli=new DefaultHttpClient();

        HttpUriRequest req=new HttpGet("http://192.168.1.34/MyLostDevice/controller/corUser.php");

        try {
            HttpResponse r=cli.execute(req);

            ByteArrayOutputStream out=new ByteArrayOutputStream();
            r.getEntity().writeTo(out);
            String payload=out.toString();

            JSONObject obj=new JSONObject(payload);



            //Log.d("ttt","name="+obj.getString("name"));
            //Log.d("ttt","age="+obj.getString("age"));

        } catch (Exception e){
            Log.e("ttt","Exception "+e.toString());
        }
    }
}