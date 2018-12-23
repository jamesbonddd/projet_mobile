package com.example.jag.projetenseirb.domain;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import java.util.Date;


@Entity
public class Session {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String  startDate;
    public String  endDate;

    @Ignore
    public Location currentLocation;
    public double averageSpeed;
    public double maxSpeed;
    public long duration;
    public double totalDistance;
    @Ignore
    public long currentTime;
    public int userId;

    public Session(String startDate, int userId) {
        this.startDate = startDate;
        this.userId = userId;
        currentLocation = null;
        averageSpeed = 0;
        maxSpeed = 0;
        duration = 0;
        totalDistance = 0;
        currentTime = (new Date()).getTime()/1000;
    }
}
