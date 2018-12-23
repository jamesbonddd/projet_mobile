package com.example.jag.projetenseirb.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.jag.projetenseirb.domain.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Insert
    long insert(Session session);

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);

    @Query("select * from Session where userId=:userId")
    LiveData< List<Session>> getAllSessions(int userId);

}