package com.example.jag.projetenseirb.domain;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String lastname;
    public String alias;

    public User(String name, String lastname, String alias) {
        this.name = name;
        this.lastname = lastname;
        this.alias = alias;
    }

}
