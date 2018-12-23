package com.example.jag.projetenseirb.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.jag.projetenseirb.domain.Position;

import java.util.List;

@Dao
public interface PositionDao {
    @Insert
    void insert(Position position);

    @Update
    void update(Position position);

    @Delete
    void delete(Position position);

    @Query("select * from Position where sessionId=:sessionId order by orderInSession asc")
    LiveData< List<Position>> getAllPositions(int sessionId);

}