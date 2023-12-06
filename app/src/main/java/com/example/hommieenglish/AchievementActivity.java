package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.example.hommieenglish.dao.AchievementDao;
import com.example.hommieenglish.dao.UserDao;
import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.entity.Achievement;
import com.example.hommieenglish.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AchievementActivity extends Activity {
    private ImageView imageViewGif;
    private HommieEnglish db;
    private AchievementDao achievementDao;
    private UserDao userDao;
    private User userData;
    private MediaPlayer mediaPlayer;
    private Boolean playMusicBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        Intent intent = getIntent();
        playMusicBackground = intent.getBooleanExtra("play_back_sound", false);
        startBackgroundMusic();
        LinearLayout l = findViewById(R.id.achievement_text);
        l.getBackground().setAlpha(128);
        db = HommieEnglish.getInstance(this);
        achievementDao = db.achievementDao();
        userDao = db.userDao();
        CompletableFuture.supplyAsync(new Supplier<List<Achievement>>() {
                    @Override
                    public List<Achievement> get() {
                        userData = userDao.getById(intent.getIntExtra("user_id", 0));
                        return achievementDao.getByUserId(intent.getIntExtra("user_id", 0));
                    }
                })
                .thenAcceptAsync(listAchievement -> {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            double score = 0;
                            String lvl = "beginner";
                            for (Achievement achievement : listAchievement) {
                                score += achievement.getScore();
                                if (achievement.getUnitId() == 3 && achievement.getScore() > 75) {
                                    lvl = "intermediate";
                                } else if (achievement.getUnitId() == 7 && achievement.getScore() > 75) {
                                    lvl = "professional";
                                }
                            }
                            int resID = getResources().getIdentifier(lvl, "raw", getPackageName());
                            imageViewGif = findViewById(R.id.imageViewGif);
                            Glide.with(getApplicationContext())
                                    .asGif()
                                    .load(resID)
                                    .into(new ImageViewTarget<GifDrawable>(imageViewGif) {
                                        @Override
                                        protected void setResource(GifDrawable resource) {
                                            if (resource == null) {
                                                Log.d("TAG", "resource is null");
                                                return;
                                            }
                                            imageViewGif.setImageDrawable(resource);
                                            resource.start();
                                        }
                                    });

                            TextView tv = findViewById(R.id.achievementTv);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Congrats ").append(userData.getName()).append("\n")
                                    .append(listAchievement.size())
                                    .append("/7 ")
                                    .append("Tasks Completed \n With a score of ")
                                    .append(score);
                            tv.setText(sb.toString());
                        }
                });
        });
    }


    private void startBackgroundMusic() {
        if (!playMusicBackground) {
            return;
        }
        // Inisialisasi MediaPlayer dengan file audio di raw folder
        mediaPlayer = MediaPlayer.create(this, R.raw.backsound);

        // Mengatur agar backsound diulang-ulang (looping)
        mediaPlayer.setLooping(true);

        // Memulai memutar backsound
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer == null) {
            return;
        }
        // Jika activity tidak lagi berada di depan (kehilangan fokus),
        // maka pause backsound agar tidak terus berlanjut di background
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer == null) {
            return;
        }
        // Ketika activity kembali aktif setelah di pause, lanjutkan pemutaran backsound
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bebaskan MediaPlayer saat activity dihancurkan
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
