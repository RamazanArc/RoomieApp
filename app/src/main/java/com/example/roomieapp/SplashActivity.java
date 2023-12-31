package com.example.roomieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.*;

import com.example.roomieapp.model.User;
import com.example.roomieapp.screens.HomeActivity;
import com.example.roomieapp.screens.LoginActivity;
import com.example.roomieapp.screens.MessageActivity;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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