package com.example.hommieenglish.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hommieenglish.dao.AnswerDao;
import com.example.hommieenglish.dao.LearningMaterialsDao;
import com.example.hommieenglish.dao.QuestionsDao;
import com.example.hommieenglish.dao.UserDao;
import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.DataModel;
import com.example.hommieenglish.entity.LearningMaterials;
import com.example.hommieenglish.entity.QuestionAndAnswers;
import com.example.hommieenglish.entity.Questions;
import com.example.hommieenglish.entity.User;
import com.example.hommieenglish.utils.Helper;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

class RoomCallback extends RoomDatabase.Callback {
    private Context context;
    public RoomCallback(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        Log.d("DEBUG", "room callback on create invoked");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                importData(context);
            }
        });
    }

    public static void importData(Context context) {
        try {
            HommieEnglish hommieEnglish = HommieEnglish.getInstance(context);
            LearningMaterialsDao learningMaterialsDao = hommieEnglish.learningMaterialsDao();
            QuestionsDao questionsDao = hommieEnglish.questionsDao();
            AnswerDao answerDao = hommieEnglish.answerDao();
            UserDao userDao = hommieEnglish.userDao();

            InputStream inputStream = context.getAssets().open("data-model.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            DataModel dataModel = gson.fromJson(json, DataModel.class);
            User user = dataModel.user;
            user.setPassword(Helper.encodePassword(user.getPassword()));
            userDao.insertUser(user);
            for (LearningMaterials materials : dataModel.learningMaterials) {
                learningMaterialsDao.insertMaterials(materials);
            }

            for (QuestionAndAnswers qna : dataModel.questions) {
                Questions q = new Questions();
                q.setUnitId(qna.getUnitId());
                q.setType(qna.getType());
                q.setQuestion(qna.getQuestion());
                q.setId(qna.getId());
                q.setContent(qna.getContent());
                q.setSequence(qna.getSequence());
                q.setParentQuestion(qna.getParentQuestion());
                q.setBankQuestion(qna.getBankQuestion());
                q.setLevel(qna.getLevel());
                questionsDao.insert(q);

                for (Answer a : qna.getAnswers()) {
                    a.setQuestionId(q.getId());
                    answerDao.insert(a);
                }
            }
            Log.d("DataImporter", "Data imported successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
