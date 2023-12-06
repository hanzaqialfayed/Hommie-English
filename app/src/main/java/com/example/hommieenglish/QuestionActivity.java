package com.example.hommieenglish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hommieenglish.dao.AchievementDao;
import com.example.hommieenglish.dao.AnswerDao;
import com.example.hommieenglish.dao.QuestionsDao;
import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.entity.Achievement;
import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.QuestionAndAnswers;
import com.example.hommieenglish.entity.Questions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QuestionActivity extends Activity {
    public static final LinearLayout.LayoutParams matchParentWrapContent = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    );

    public static final String backgroundColor = "#00000000";

    public static final LinearLayout.LayoutParams matchParentMatchParent = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
    );

    private HommieEnglish db;
    private QuestionsDao questionsDao;
    private AnswerDao answerDao;
    private AchievementDao achievementDao;
    private int userId;

    private int unitId;
    private List<Integer> radioGroupIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id",0);
        LinearLayout layout = findViewById(R.id.question_layout);

        db = HommieEnglish.getInstance(this);
        questionsDao = db.questionsDao();
        answerDao = db.answerDao();
        achievementDao = db.achievementDao();

        CompletableFuture<List<QuestionAndAnswers>> completableFutureQnA = CompletableFuture.supplyAsync(
                () -> {
                    List<Questions> questions = questionsDao.getByUnit(intent.getIntExtra("unit_id", 0));
                    List<QuestionAndAnswers> result = new ArrayList<>();
                    for (Questions q:questions) {
                        QuestionAndAnswers qna = new QuestionAndAnswers();
                        qna.setUnitId(q.getUnitId());
                        qna.setQuestion(q.getQuestion());
                        qna.setParentQuestion(q.getParentQuestion());
                        qna.setType(q.getType());
                        qna.setContent(q.getContent());
                        qna.setSequence(q.getSequence());
                        qna.setId(q.getId());
                        qna.setAnswers(answerDao.getByQuestionId(q.getId()));
                        result.add(qna);
                        unitId = q.getUnitId();
                    }
                    return result;
                }
        );
        completableFutureQnA.thenAcceptAsync(dataList -> {
                   runOnUiThread(() -> {
                       for (QuestionAndAnswers q : dataList) {
                           // Membuat layout untuk pertanyaan
                           LinearLayout questionLayout = new LinearLayout(this);
                           questionLayout.setLayoutParams(matchParentMatchParent);
                           questionLayout.setGravity(Gravity.CENTER);
                           questionLayout.setPadding(30, 30, 30, 0);
                           questionLayout.setOrientation(LinearLayout.VERTICAL);

                           if (q.getParentQuestion()) {
                               if (q.getType().equals("image")) {
                                   // Membuat gambar pada layout pertanyaan
                                   ImageView questionImage = new ImageView(this);
                                   String imageName = q.getContent();

                                   int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                                   questionImage.setImageResource(resID);
                                   int heightInSp = 300;

                                   int heightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInSp, getResources().getDisplayMetrics());

                                   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightInPx);
                                   questionImage.setLayoutParams(layoutParams);
                                   questionImage.setScaleType(ImageView.ScaleType.FIT_XY);
                                   questionLayout.addView(questionImage);
                               } else if (q.getType().equals("text")) {
                                   TextView textView = new TextView(this);
                                   textView.setLayoutParams(matchParentWrapContent);
                                   textView.setText(q.getContent());
                                   textView.setTextSize(16);
                                   textView.setTypeface(Typeface.SANS_SERIF);
                                   textView.setPadding(10,10,10,0);
                                   textView.setBackgroundColor(Color.parseColor(backgroundColor));
                                   textView.setTextColor(Color.WHITE);
                                   questionLayout.addView(textView);
                               } else if (q.getType().equals("audio")) {
                                   RelativeLayout relativeLayout = new RelativeLayout(this);
                                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                           RelativeLayout.LayoutParams.MATCH_PARENT,
                                           RelativeLayout.LayoutParams.WRAP_CONTENT
                                   );
                                   relativeLayout.setLayoutParams(layoutParams);
                                   relativeLayout.setPadding(16, 16, 16, 16);
                                   relativeLayout.setBackgroundColor(Color.parseColor(backgroundColor));

                                   ImageButton btnPlayPause = new ImageButton(this);
                                   RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(
                                           ViewGroup.LayoutParams.WRAP_CONTENT,
                                           ViewGroup.LayoutParams.WRAP_CONTENT
                                   );
                                   btnLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                   btnPlayPause.setLayoutParams(layoutParams);
                                   btnPlayPause.setBackgroundColor(Color.TRANSPARENT);
                                   btnPlayPause.setId(View.generateViewId());
                                   int resID = getResources().getIdentifier("ic_play", "drawable", getPackageName());
                                   btnPlayPause.setImageResource(resID);

                                   int audioId = getResources().getIdentifier(q.getContent(), "raw", getPackageName());
                                   AssetFileDescriptor afd = getResources().openRawResourceFd(audioId);
                                   MediaPlayer md = new MediaPlayer();
                                   if (afd != null) {
                                       try {
                                           md.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                           afd.close();
                                           md.prepare();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   final Boolean[] isPlaying = {false};
                                   btnPlayPause.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if (isPlaying[0]) {
                                               isPlaying[0] = pauseAudio(md, btnPlayPause);
                                           } else {
                                               isPlaying[0] = playAudio(md, btnPlayPause);
                                           }
                                       }
                                   });

                                   relativeLayout.addView(btnPlayPause);
                                   questionLayout.addView(relativeLayout);
                               }
                           }

                           // Membuat text view untuk pertanyaan
                           TextView questionText = new TextView(this);
                           questionText.setLayoutParams(matchParentWrapContent);
                           questionText.setText(q.getSequence() + ". " + q.getQuestion());
                           questionText.setTextSize(16);
                           questionText.setTypeface(Typeface.DEFAULT_BOLD);
                           questionText.setTextColor(Color.WHITE);
                           questionText.setBackgroundColor(Color.parseColor(backgroundColor));
                           questionText.setForegroundGravity(Gravity.BOTTOM);
                           questionText.setPadding(16,16,16,16);

                           // Menambahkan image dan text view ke layout
                           questionLayout.addView(questionText);

                           // Membuat Layout untuk jawaban
                           LinearLayout answerLayout = new LinearLayout(this);
                           answerLayout.setLayoutParams(matchParentWrapContent);
                           answerLayout.setGravity(Gravity.TOP);
                           answerLayout.setPadding(16,16,16,16);
                           answerLayout.setOrientation(LinearLayout.VERTICAL);

                           // Membuat radio group untuk pilihan ganda
                           RadioGroup radioGroup = new RadioGroup(this);
                           radioGroup.setLayoutParams(matchParentWrapContent);
                           radioGroup.setBackgroundColor(Color.parseColor(backgroundColor));
                           radioGroup.setOrientation(LinearLayout.VERTICAL);
                           radioGroup.setId(View.generateViewId());

                           for (Answer a : q.getAnswers()) {
                               RadioButton rb1 = new RadioButton(this);
                               LinearLayout.LayoutParams lprb = matchParentWrapContent;
                               lprb.setMargins(10,10,10,10);
                               rb1.setLayoutParams(lprb);
                               rb1.setTextSize(16);
                               rb1.setTextColor(Color.WHITE);
                               rb1.setText(a.getAnswerText());
                               rb1.setId(View.generateViewId());
                               rb1.setTag(a.getCorrectAnswer());
                               radioGroup.addView(rb1);
                           }
                           radioGroupIds.add(radioGroup.getId());
                           answerLayout.addView(radioGroup);
                           layout.addView(questionLayout);
                           layout.addView(answerLayout);
                       }
                   });
                });
        Button submitButton = findViewById(R.id.submit_answer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double finalResult = 0;
                for (Integer radioGroupId : radioGroupIds) {

                    RadioGroup radioGroup = findViewById(radioGroupId);
                    int idRadioButton = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(idRadioButton);
                    if (selectedRadioButton == null) {
                        continue;
                    }
                    Boolean isCorrectAnswer = (Boolean) selectedRadioButton.getTag();
                    if (isCorrectAnswer){
                        finalResult += 12.5;
                    }
                    Log.d("DEBUG", "correct answer " + String.valueOf(isCorrectAnswer));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                double finalResult1 = finalResult;
                View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView)
                        .setCancelable(false);

                TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                AlertDialog dialog = builder.create();

                // Menetapkan pesan dialog
                messageTextView.setText("Congratulations, You have completed the questions in Unit " + unitId + " with a score of " + finalResult + "!");
                // Menetapkan aksi saat tombol di klik
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveScore(finalResult1);
                        Intent intentLearning = new Intent(getBaseContext(), LearningActivity.class);
                        intentLearning.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentLearning.putExtra("user_id", userId);
                        startActivity(intentLearning);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    // Fungsi untuk memainkan audio yang diputar
    public static Boolean playAudio(MediaPlayer mediaPlayer, ImageButton btnPlayPause) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        }
        return true;
    }

    // Fungsi untuk mem-pause audio yang diputar
    public static Boolean pauseAudio(MediaPlayer mediaPlayer, ImageButton btnPlayPause) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }
        return false;
    }

    private void saveScore(double finalScore) {
        CompletableFuture.supplyAsync(() -> {
            try {
                // Ambil  data dari table achievement by userId & unitId
                Achievement existingData = achievementDao.getByUserIdAndUnitId(userId, unitId);
                // kalau belum ada di table, maka buat baru
                if (existingData == null) {
                    Achievement achievement = new Achievement();
                    achievement.setScore(finalScore);
                    achievement.setUnitId(unitId);
                    achievement.setUserId(userId);
                    achievementDao.insert(achievement);
                    return null;
                }
                // kalau sudah ada, maka update score nya untuk unitId & userId yg sama
                if (existingData.getScore() < finalScore) {
                    existingData.setScore(finalScore);
                    achievementDao.update(existingData);
                }
                return null;
            } catch (Exception e){
                Log.d("DEBUG", "Failed save score" + e.getStackTrace());
                return null;
            }
        });
    }
}
