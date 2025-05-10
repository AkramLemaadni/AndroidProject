package com.example.gamingstore.data.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    LiveData<userEntity> getAll();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    userEntity login(String username, String password);

    @Insert
    void insert(userEntity user);

    @Delete
    void delete(userEntity user);

    @Update
    void update(userEntity user);

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    LiveData<userEntity> getUserById(int userId);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    userEntity getUserByUsername(String username);
} 