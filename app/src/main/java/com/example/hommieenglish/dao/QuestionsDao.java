package com.example.hommieenglish.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hommieenglish.entity.Questions;

import java.util.List;

@Dao
public interface QuestionsDao {
    @Insert
    void insert(Questions q);

    @Query("SELECT * FROM questions WHERE id = :id")
    Questions getById(int id);

    @Update
    void updateUser(Questions questions);

    @Delete
    void deleteUser(Questions questions);

    @Query("SELECT * FROM questions WHERE unit = :unit")
    List<Questions> getByUnit(int unit);

    @Query("SELECT * FROM questions WHERE is_bank_question = :isBankQuestions AND level = :level")
    List<Questions> getBankQuestions(Boolean isBankQuestions, String level);
}
