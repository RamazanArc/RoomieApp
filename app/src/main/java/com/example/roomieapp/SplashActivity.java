package com.example.roomieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.roomieapp.screens.HomeActivity;
import com.example.roomieapp.screens.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        // Splash ekranı 2 saniyeliğine bekleyecek sonra login ekranı yüklenecek
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null){
                    // Eğer kullanıcı zaten giriş yapmışşsa Home ekranınan yönlendirecek bir daha girş yapmsına gerek kalmayacak
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }, 5000);
    }
}