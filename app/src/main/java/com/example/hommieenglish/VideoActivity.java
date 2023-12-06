package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_learning_activity);

        Intent intent = getIntent();

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        String videoId = "e_04ZrNroTo"; // Ganti dengan ID video YouTube yang ingin Anda tampilkan
        String videoUrl = intent.getStringExtra("video_url");
        webView.loadUrl(videoUrl);
    }
}
