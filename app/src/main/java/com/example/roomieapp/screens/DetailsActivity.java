package com.example.roomieapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.model.User;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price,shortDescription,description;
    private Button chatButton;

    String des, sDes, pri, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Item item = new Item();
                Intent intent = new Intent(getApplicationContext(),MessageActivity.class);
//                intent.putExtra("currentUserID",item.getCurrentUserID());
                startActivity(intent);
            }
        });


    }
}