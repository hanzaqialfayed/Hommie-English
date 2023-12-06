package com.example.hommieenglish.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.Questions;

import java.util.List;

@Dao
public interface AnswerDao {
    @Insert
    void insert(Answer q);

    @Query("SELECT * FROM answer WHERE id = :id")
    Answer getById(int id);

    @Update
    void update(Answer answer);

    @Delete
    void delete(Answer answer);

    @Query("SELECT * FROM answer WHERE question_id = :questionId")
    List<Answer> getByQuestionId(int questionId);

    @Query("SELECT * FROM answer WHERE question_id = :unitId and is_correct_answer = :isCorrectAnswer")
    Answer getCorrectAnswer(Integer unitId, Boolean isCorrectAnswer);
}
