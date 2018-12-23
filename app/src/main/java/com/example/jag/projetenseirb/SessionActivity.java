package com.example.jag.projetenseirb;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.jag.projetenseirb.domain.Session;


public class SessionActivity extends AppCompatActivity {

    GPS_Service gps_service;
    private int userId;
    private ServiceConnection mServiceConnection = new MyServiceConnection(this);

    class MyServiceConnection implements ServiceConnection {

        private AppCompatActivity activity;

        public MyServiceConnection(AppCompatActivity activity){
            this.activity = activity;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPS_Service.MyBinder myBinder = (GPS_Service.MyBinder) service;
            gps_service = myBinder.getService();
            setUpListener(activity);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    public void setUpListener(AppCompatActivity activity){

        class MyObserver implements Observer<Session> {
            private AppCompatActivity activity;

            public MyObserver(AppCompatActivity activity){
                this.activity = activity;
            }

            @Override
            public void onChanged(@Nullable Session session) {
                StringBuilder output = new StringBuilder();
                if(session.currentLocation != null)
                    output.append("current location: " + session.currentLocation.getLatitude() + "-" + session.currentLocation.getLongitude() + "\n");
                output.append("average speed: " + session.averageSpeed + " (m/s)\n");
                output.append("max speed: " + session.maxSpeed + " (m/s)\n");
                output.append("duration: " + session.duration + " (s)\n");
                output.append("total distance: " + session.totalDistance + " (m)\n");
                output.append("current time: " + session.currentTime + " (s)\n");
                ((TextView) activity.findViewById(R.id.output)).setText(output.toString());
            }
        }

        gps_service.getSessionInfo().observe(activity,new MyObserver(activity));

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        Intent intent = new Intent(SessionActivity.this, GPS_Service.class);
        stopService(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        userId = getIntent().getExtras().getInt("userId");

        if(!runtime_permissions())
            start_service();
    }


    private void start_service(){
        Intent intent =new Intent(getApplicationContext(),GPS_Service.class);
        intent.putExtra("userId",userId);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                start_service();
            }else {
                runtime_permissions();
            }
        }
    }

    public void stopTracking(View view ) {
        unbindService(mServiceConnection);
        Intent intent = new Intent(SessionActivity.this, GPS_Service.class);
        stopService(intent);
        Intent i  = new Intent(this,ListSessionsActivity.class);
        i.putExtra("userId",userId);
        startActivity(i);
    }

}
