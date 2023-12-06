package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "learning_materials",
        indices = {
                @Index(value = {"unit"})
        }
)
public class LearningMaterials {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "video_url")
    public String videoUrl;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "button_name")
    public String image_button_name;

    @ColumnInfo(name = "unit")
    public int unit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_button_name() {
        return image_button_name;
    }

    public void setImage_button_name(String image_button_name) {
        this.image_button_name = image_button_name;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
