package com.example.hommieenglish.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hommieenglish.entity.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {
    @Insert
    void insert(Achievement a);

    @Update
    void update(Achievement a);

    @Delete
    void delete(Achievement a);

    @Query("Select * from achievement where unit_id=:unitId")
    Achievement getByUnitId(int unitId);

    @Query("Select * from achievement where user_id =:userId")
    List<Achievement> getByUserId(int userId);

    @Query("Select * from achievement where user_id =:userId AND unit_id =:unitId")
    Achievement getByUserIdAndUnitId(int userId, int unitId);
}
