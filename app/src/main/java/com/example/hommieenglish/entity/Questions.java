package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "questions",
        indices = {
                @Index(value = {"unit"})
        }
)
public class Questions {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "unit")
    private int unitId;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "sequence")
    private String sequence;
    @ColumnInfo(name = "is_parent_question")
    private Boolean isParentQuestion;
    @ColumnInfo(name = "is_bank_question")
    private Boolean isBankQuestion;
    @ColumnInfo(name = "level")
    private String level;

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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean getParentQuestion() {
        return isParentQuestion;
    }

    public void setParentQuestion(Boolean parentQuestion) {
        isParentQuestion = parentQuestion;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
