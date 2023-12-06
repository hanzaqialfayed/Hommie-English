package com.example.hommieenglish.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hommieenglish.entity.LearningMaterials;

import java.util.List;

@Dao
public interface LearningMaterialsDao {
    @Insert
    void insertMaterials(LearningMaterials learningMaterials);

    @Query("Select * FROM learning_materials where id != 0")
    List<LearningMaterials> getAllMaterials();

    @Delete
    void deleteMaterials(LearningMaterials learningMaterials);

    @Update
    void updateMaterials(LearningMaterials learningMaterials);
}
