package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class LearningMenuActivity extends Activity {

    private MediaPlayer buttonSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_menu);

        Intent incomingIntent = getIntent();

        ImageButton learningBtn = (ImageButton) findViewById(R.id.learning_btn);
        ImageButton practiceBtn = (ImageButton) findViewById(R.id.practice_btn);
        buttonSound = MediaPlayer.create(this, R.raw.button_sound);

        learningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButtonSound();
                Intent i = new Intent(getBaseContext(), VideoActivity.class);
                i.putExtra("video_url", incomingIntent.getStringExtra("video_url"));
                startActivity(i);
            }
        });

        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButtonSound();
                Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
                intent.putExtra("unit_id", incomingIntent.getIntExtra("unit_id", 0));
                intent.putExtra("user_id", incomingIntent.getIntExtra("user_id", 0));
                startActivity(intent);
            }
        });
    }


    private void playButtonSound() {
        // Memulai memutar backsound tombol
        buttonSound.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bebaskan MediaPlayer saat activity dihancurkan
        if (buttonSound != null) {
            buttonSound.release();
            buttonSound = null;
        }
    }
}