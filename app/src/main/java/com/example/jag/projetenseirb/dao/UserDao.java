package com.example.jag.projetenseirb.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jag.projetenseirb.domain.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("select * from User ")
    LiveData< List<User>> getAllUsers();

    @Query("select * from User where User.id == :id")
    User findUserById(int id);

}
