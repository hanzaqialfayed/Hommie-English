package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class QuestionBankMenu extends Activity {
    private MediaPlayer mediaPlayer;
    private Boolean playMusicBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank_menu);
        Intent intent = getIntent();
        playMusicBackground = intent.getBooleanExtra("play_back_sound", false);
        startBackgroundMusic();

        Button beginnerBtn = findViewById(R.id.beginner);
        Button intermediateBtn = findViewById(R.id.intermediate);
        Button advancedBtn = findViewById(R.id.advanced);

        beginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BankQuestions.class);
                intent.putExtra("level", "easy");
                startActivity(intent);
            }
        });

        intermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BankQuestions.class);
                intent.putExtra("level", "medium");
                startActivity(intent);
            }
        });

        advancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BankQuestions.class);
                intent.putExtra("level", "hard");
                startActivity(intent);
            }
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
