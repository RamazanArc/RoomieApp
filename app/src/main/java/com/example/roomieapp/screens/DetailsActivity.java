package com.example.roomieapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price,shortDescription,description;
    private Button chatButton;
    private String ilanID;
    private DatabaseReference databaseReference;

    String des, sDes, pri, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        databaseReference = FirebaseDatabase.getInstance().getReference("images");

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);
        chatButton = findViewById(R.id.chat_button);

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        sDes= getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("imageUrl");

        price.setText(pri+"₺");
        description.setText(des);
        shortDescription.setText(sDes);
        Glide.with(this)
                .load(img)
                .centerCrop()
                .into(imageView);
    }
}