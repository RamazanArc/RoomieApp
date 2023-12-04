package com.example.roomieapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price,shortDescription,description;

    String des, sDes, pri, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        sDes= getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");

        price.setText(pri+"₺");
        description.setText(des);
        shortDescription.setText(sDes);
        Glide.with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(imageView);
    }
}