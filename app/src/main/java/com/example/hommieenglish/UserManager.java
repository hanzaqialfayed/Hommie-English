package com.example.hommieenglish;

import android.content.Context;
import android.util.Log;

import com.example.hommieenglish.dao.UserDao;
import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.entity.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserManager {
    private UserDao userDao;
    private Executor executor = Executors.newSingleThreadExecutor();


    public UserManager(Context context) {
        HommieEnglish db = HommieEnglish.getInstance(context);
        userDao = db.userDao();
    }

    public Boolean createUser(User user) {
            User existUser = userDao.getByUsername(user.getName());
            if (existUser == null) {
                userDao.insertUser(user);
                return true;
            }
            return false;
    }

    public User login(String username, String password) {
        try {
            return userDao.getByUsernameAndPassword(username, password);
        } catch (Exception e) {
            Log.d("ERROR", "Failed login " +e.getMessage());
            return null;
        }
    }
}
