package com.example.jag.projetenseirb.domain;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Position {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double latitude;
    public double longitude;
    public int orderInSession;
    public int sessionId;

    public Position(double latitude, double longitude, int orderInSession, int sessionId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderInSession = orderInSession;
        this.sessionId = sessionId;
    }

}
