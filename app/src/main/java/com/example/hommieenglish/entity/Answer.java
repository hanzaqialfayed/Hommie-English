package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "answer",
        indices = {
                @Index(value = {"question_id"})
        }
)
public class Answer {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "question_id")
    private int questionId;
    @ColumnInfo(name = "answer_text")
    private String answerText;
    @ColumnInfo(name = "is_correct_answer")
    private Boolean isCorrectAnswer;
    @ColumnInfo(name = "explanation")
    private String explanation;

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
