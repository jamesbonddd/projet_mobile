package com.example.jag.projetenseirb.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jag.projetenseirb.domain.Position;
import com.example.jag.projetenseirb.domain.Session;
import com.example.jag.projetenseirb.domain.User;


@Database(entities = {User.class,Session.class,Position.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract  UserDao userDao();
    public abstract SessionDao sessionDao();
    public abstract PositionDao positionDao();

    public static synchronized  AppDatabase getInstance(Context context){
        if(instance == null ){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
