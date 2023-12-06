package com.example.hommieenglish.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "users",
    indices = {
        @Index(value = {"user_name"}, unique = true),
        @Index(value = {"user_email"}, unique = true)
    }
)
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_name")
    public String name;

    @ColumnInfo(name = "user_password")
    public String password;

    @ColumnInfo(name = "user_email")
    public String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
