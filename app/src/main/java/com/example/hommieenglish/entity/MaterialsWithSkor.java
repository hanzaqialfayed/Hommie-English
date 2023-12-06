package com.example.hommieenglish.entity;

public class MaterialsWithSkor {
    public int id;
    public String videoUrl;
    public String title;
    public String description;
    public String image_button_name;
    public int unit;
    public Boolean isEnable;

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

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }
}
