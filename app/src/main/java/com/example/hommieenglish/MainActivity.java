package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.utils.Helper;
import com.facebook.stetho.Stetho;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Initiate database
        HommieEnglish.getInstance(getApplicationContext());
        UserManager userManager = new UserManager(getApplicationContext());

        // Fungsi untuk pindah ke halaman register
        TextView linkToRegister = findViewById(R.id.dont_have_account);
        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        // Fungsi untuk login
        Button loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = (TextView) findViewById(R.id.et_username );
                TextView password = (TextView) findViewById(R.id.et_password);
                if (password == null || password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                } else if (name == null || name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                }

                CompletableFuture.supplyAsync(() -> userManager.login(name.getText().toString(), Helper.encodePassword(password.getText().toString())))
                        .thenAccept(user -> {
                            if (user != null) {
                                // Login berhasil
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getBaseContext(), MainMenu.class);
                                    i.putExtra("user_id", user.id);
                                    startActivity(i);
                                });
                            } else {
                                // Login gagal
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login Failed, Please Try Again", Toast.LENGTH_SHORT).show());
                            }
                        });

            }
        });
    }
}