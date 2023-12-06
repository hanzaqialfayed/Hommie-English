package com.example.hommieenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hommieenglish.entity.User;
import com.example.hommieenglish.utils.Helper;

import java.util.concurrent.CompletableFuture;

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Button registerBtn = findViewById(R.id.btn_register);

        UserManager userManager = new UserManager(getApplicationContext());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView email = (TextView) findViewById(R.id.et_register_email);
                TextView password = (TextView) findViewById(R.id.et_register_password);
                TextView name = (TextView) findViewById(R.id.et_register_name);
                if (!Helper.isValidEmail(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    return;
                } else if (password == null || password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                    return;
                } else if (name == null || name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                    return;
                }

                User newUser = new User();
                newUser.setEmail(email.getText().toString());
                newUser.setName(name.getText().toString());
                newUser.setPassword(Helper.encodePassword(password.getText().toString()));
                // Menjalankan operasi login secara asynchronous menggunakan CompletableFuture
                CompletableFuture.supplyAsync(() -> userManager.createUser(newUser))
                    .thenAccept(isSuccess -> {
                    if (isSuccess) {
                        // Registrasi berhasil
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Registration Success, Lets Login", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);
                        });
                    } else {
                        // Registrasi gagal
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Registration Failed, Please Try Again", Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        TextView linkToRegister = findViewById(R.id.already_have_account);
        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
