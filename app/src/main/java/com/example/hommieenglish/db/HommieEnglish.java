package com.example.hommieenglish.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hommieenglish.dao.AchievementDao;
import com.example.hommieenglish.dao.AnswerDao;
import com.example.hommieenglish.dao.LearningMaterialsDao;
import com.example.hommieenglish.dao.QuestionsDao;
import com.example.hommieenglish.dao.UserDao;
import com.example.hommieenglish.entity.Achievement;
import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.LearningMaterials;
import com.example.hommieenglish.entity.Questions;
import com.example.hommieenglish.entity.User;

@Database(entities = {
        User.class,
        LearningMaterials.class,
        Questions.class,
        Answer.class,
        Achievement.class
}, version = 9)
public abstract class HommieEnglish extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract LearningMaterialsDao learningMaterialsDao();
    public abstract QuestionsDao questionsDao();
    public abstract AnswerDao answerDao();
    public abstract AchievementDao achievementDao();
    private static volatile HommieEnglish INSTANCE;
    public static HommieEnglish getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HommieEnglish.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    HommieEnglish.class, "hommie_english")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomCallback(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
