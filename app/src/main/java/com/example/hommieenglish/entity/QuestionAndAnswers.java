package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.List;

public class QuestionAndAnswers {
    private int id;
    private int unitId;
    private String type;
    private String question;
    private String content;
    private String sequence;
    private Boolean isParentQuestion;
    private Boolean isBankQuestion;
    private String level;
    private List<Answer> answers;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getBankQuestion() {
        return isBankQuestion;
    }

    public void setBankQuestion(Boolean bankQuestion) {
        isBankQuestion = bankQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Boolean getParentQuestion() {
        return isParentQuestion;
    }

    public void setParentQuestion(Boolean parentQuestion) {
        isParentQuestion = parentQuestion;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
