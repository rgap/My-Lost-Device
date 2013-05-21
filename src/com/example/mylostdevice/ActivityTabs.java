package com.example.mylostdevice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.TabHost.TabSpec;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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


    public volatile UpdateDeviceThread TUpdateDev;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

        tab_host = (TabHost) findViewById(android.R.id.tabhost);
        tab_host.setup();

        TabSpec tspec;

        tspec = tab_host.newTabSpec("tag2");
        tspec.setIndicator("Configuration");
        tspec.setContent(R.layout.tab);
        tab_host.addTab(tspec);

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

        //Log.e("addReg", "userid ----> " + String.valueOf(userid_g));
	}

    @Override
    public void onBackPressed() {
    }

    public void updateExitDevice(){
        state_g = "2";

        TUpdateDev = new UpdateDeviceThread(true);
        TUpdateDev.start();

    }

    @Override
    protected void onStop() {

        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        updateExitDevice();
        super.onPause();
        locationManager.removeUpdates(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
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

    public synchronized void actionChangeState(View v){

        boolean on = ((ToggleButton) v).isChecked();

        if (on) {
            state_g = "1";

            //ACTUALIZAR

            TUpdateDev = new UpdateDeviceThread();
            TUpdateDev.start();

            tToast("Device enabled");

        } else {
            state_g = "0";

            TUpdateDev.requestStop();

            TUpdateDev = new UpdateDeviceThread(true);
            TUpdateDev.start();

            tToast("Device disabled");
        }

    }

    public void actionUnlink(View v){;

        tToast("Device unlinked");

        Intent i=new Intent(this,ActivityLogin.class);
        startActivity(i);
    }


    private class UpdateDeviceThread extends Thread {

        String userid,devid,devlocation,devstate;

        boolean unciclo = false;

        private volatile boolean stop = false;

        UpdateDeviceThread()
        {
            this.userid=String.valueOf(userid_g);
            this.devid= String.valueOf(devid_g);
            this.devlocation= (String.valueOf(lat) + ", " + String.valueOf(lng));
            this.devstate=state_g;
        }

        UpdateDeviceThread(boolean unciclo)
        {
            this.unciclo = true;

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

            while(!stop){

                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost =new HttpPost("http://relguzman.com/MyLostDevice/controller_client/corDeviceUpdate.php");

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

                    //Log.d("addReg","ACTUALIZADO " + userid +" "+ devid);


                } catch (Exception e){

                    Log.e("addReg", "Exception " + e.toString());
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                updateValues();

                //Log.d("addReg",devlocation +" "+ devid + " " + devstate);

                if(unciclo) break;

            }
        }

        public synchronized void requestStop() {
            stop = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:

                showAlert(getString(R.string.created_by));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAlert(String s){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("About This");
        builder1.setMessage(s);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
