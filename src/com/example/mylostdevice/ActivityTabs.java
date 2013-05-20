package com.example.mylostdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TabHost.TabSpec;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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


public class ActivityTabs extends Activity implements LocationListener {

	private Button btAddDev, btActDev;
	
	private EditText value;

    private TabHost tab_host;


    private LocationManager locationManager;
    private String provider;

    int userid_g;
    int devid_g;
    String state_g;
    private double lat;
    private double lng;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

        tab_host = (TabHost) findViewById(android.R.id.tabhost);
        tab_host.setup();

        TabSpec tspec_t1;

        tspec_t1 = tab_host.newTabSpec("tag1");
        tspec_t1.setIndicator("Devices List");
        tspec_t1.setContent(R.layout.tab1);
        tab_host.addTab(tspec_t1);

        TabSpec tspec_t2;

        tspec_t2 = tab_host.newTabSpec("tag2");
        tspec_t2.setIndicator("Configuration");
        tspec_t2.setContent(R.layout.tab2);
        tab_host.addTab(tspec_t2);

        //LOCATION

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            tToast("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            tToast("Location not available");
        }

        state_g = "0";

        SharedPreferences preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);

        userid_g = preferences.getInt("userid",0);

        devid_g = preferences.getInt("devid",0);

        Log.e("addReg", "userid ----> " + String.valueOf(userid_g));

        //LISTA

        /*
        Device[] devices={
                new Device("afas","sfa","fa"),
                new Device("afas","sfa","fa"),
                new Device("afas","sfa","fa"),
                new Device("afas","sfa","fa"),
                new Device("afas","sfa","fa"),
                new Device("afas","sfa","fa"),
        };
        */

        //devices.

        /*
        AdapterDevices adapter=new AdapterDevices(devices);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        */

        //ACTUALIZAR

        Thread TUpdateDev = new Thread(new UpdateDeviceRunnable());
        TUpdateDev.start();

	}


    class AdapterDevices extends ArrayAdapter<Device> {

        public AdapterDevices(Device[]device) {
            super(getApplicationContext(),0,device);
        }


        private class Holder {

            public TextView txdevType;
            public TextView txdevLoc;
            public TextView txdevState;

            public Holder(View l) {
                txdevType=(TextView)l.findViewById(R.id.devType);
                txdevLoc=(TextView)l.findViewById(R.id.devLoc);
                txdevState=(TextView)l.findViewById(R.id.devState);
            }
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            Device p=getItem(position);
            if(convertView!=null) {
                Log.d("ttt","not null");
            }
            RelativeLayout l;
            Holder h;
            if(convertView==null) {
                l=(RelativeLayout)getLayoutInflater().inflate(R.layout.list_row_layout, null);
                h=new Holder(l);
                l.setTag(h);
            }
            else {
                l=(RelativeLayout)convertView;
                h=(Holder)l.getTag();
            }

            h.txdevType.setText(p.devtype);
            h.txdevLoc.setText(p.devlocation);
            h.txdevState.setText(p.devstate);

            return l;
        }
    }

    private void tToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (location.getLatitude());
        lng = (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    public void actionChangeState(View v){

        boolean on = ((ToggleButton) v).isChecked();

        if (on) {
            state_g = "1";
        } else {
            state_g = "0";
        }
    }

    public void actionUnlink(View v){
    }

    private class UpdateDeviceListRunnable implements Runnable {

        String userid;

        UpdateDeviceListRunnable()
        {
            this.userid=String.valueOf(userid_g);
        }

        @Override
        public void run() {

            while(true){

                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost =new HttpPost("http://192.168.1.34/MyLostDevice/controller_client/corDeviceSelect.php");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid", userid));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    //LISTA
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    JSONTokener tokener = new JSONTokener(json);
                    JSONArray finalResult = new JSONArray(tokener);



                    Log.d("addReg","LISTA ACTUALIZADA");


                } catch (Exception e){

                    Log.e("addReg", "Exception " + e.toString());
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private class UpdateDeviceRunnable implements Runnable {

        String userid,devid,devlocation,devstate;

        UpdateDeviceRunnable()
        {
            this.userid=String.valueOf(userid_g);
            this.devid= String.valueOf(devid_g);
            this.devlocation= (String.valueOf(lat) + ", " + String.valueOf(lng));
            this.devstate=state_g;
        }

        public void updateValues(){
            this.userid=String.valueOf(userid_g);
            this.devid= String.valueOf(devid_g);
            this.devlocation= (String.valueOf(lat) + ", " + String.valueOf(lng));
            this.devstate=state_g;
        }

        @Override
        public void run() {

            while(true){

                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost =new HttpPost("http://192.168.1.34/MyLostDevice/controller_client/corDeviceUpdate.php");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid", userid));
                    nameValuePairs.add(new BasicNameValuePair("devid", devid));
                    nameValuePairs.add(new BasicNameValuePair("devlocation", devlocation));
                    nameValuePairs.add(new BasicNameValuePair("devstate", devstate));


                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    Log.d("addReg","ACTUALIZADO " + userid +" "+ devid);


                } catch (Exception e){

                    Log.e("addReg", "Exception " + e.toString());
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                updateValues();

                Log.d("addReg",devlocation +" "+ devid + " " + devstate);

            }
        }
    }


}
