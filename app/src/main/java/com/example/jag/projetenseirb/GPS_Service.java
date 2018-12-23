package com.example.jag.projetenseirb;

import android.annotation.SuppressLint;
import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.example.jag.projetenseirb.dao.AppDatabase;
import com.example.jag.projetenseirb.dao.PositionDao;
import com.example.jag.projetenseirb.dao.SessionDao;
import com.example.jag.projetenseirb.domain.Position;
import com.example.jag.projetenseirb.domain.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private IBinder mBinder = new MyBinder();
    private MutableLiveData<Session> session;
    private SessionDao sessionDao;
    private PositionDao positionDao;
    private int sessionId; // new session
    private int positionOrder = 0;


    // bind
    class MyBinder extends Binder {
        GPS_Service getService() {
            return GPS_Service.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }




    // location
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            Session curr = session.getValue();
            Location prevLocation = curr.currentLocation;
            if(prevLocation == null){
                // first capture
                curr.currentLocation = location;
            }else{
                float distance = prevLocation.distanceTo(location);
                long currentTime = (new Date()).getTime() / 1000;
                long time = currentTime - curr.currentTime;

                curr.totalDistance += distance;
                curr.duration += currentTime  -  curr.currentTime;
                curr.currentTime = currentTime;
                curr.averageSpeed = curr.totalDistance/curr.duration;
                curr.currentLocation = location;
                double currentSpeed = distance/time;
                if(currentSpeed > curr.maxSpeed)
                    curr.maxSpeed = currentSpeed;
            }
            Location l = curr.currentLocation;
            Position p = new Position(l.getLatitude(),l.getLongitude(),positionOrder++,sessionId);
            positionDao.insert(p);
            sessionDao.update(curr);
            session.setValue(curr);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    };


    // service ..
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sessionDao = (AppDatabase.getInstance(getApplication())).sessionDao();
        positionDao = (AppDatabase.getInstance(getApplication())).positionDao();
        int userId = intent.getExtras().getInt("userId");
        String startDatetime = new SimpleDateFormat("YYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Session s = new Session(startDatetime,userId);
        sessionId = (int) sessionDao.insert(s);
        s.id = sessionId;
        session = new MutableLiveData<>();
        session.setValue(s);
        listener = new MyLocationListener();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        session.getValue().endDate = new SimpleDateFormat("YYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        sessionDao.update(session.getValue());
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(listener);
        }
    }

    public LiveData<Session> getSessionInfo(){
        return session;
    }

}