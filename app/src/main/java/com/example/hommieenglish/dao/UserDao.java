package com.example.hommieenglish.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hommieenglish.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    User getById(int id);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE user_name = :userName")
    User getByUsername(String userName);

    @Query("SELECT * FROM users WHERE user_name = :userName AND user_password = :password")
    User getByUsernameAndPassword(String userName, String password);
}
