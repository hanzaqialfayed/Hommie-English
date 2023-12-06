package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "achievement",
        indices = {
                @Index(
                        value = {"user_id", "unit_id"}
                )
        }
)
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "unit_id")
    private int unitId;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "score")
    private Double score;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
