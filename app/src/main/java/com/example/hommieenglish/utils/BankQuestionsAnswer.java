package com.example.hommieenglish.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BankQuestionsAnswer implements Serializable {
    private String currentAnswer;
    private Integer unitId;
    private String question;
    private String correctAnwer;
    private String explanation;
    public BankQuestionsAnswer(String currentAnswer, Integer unitId, String question) {
        this.currentAnswer = currentAnswer;
        this.unitId = unitId;
        this.question = question;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnwer() {
        return correctAnwer;
    }

    public void setCorrectAnwer(String correctAnwer) {
        this.correctAnwer = correctAnwer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

}
