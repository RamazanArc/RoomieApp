package com.example.roomieapp.screens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.model.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateDealActivity extends AppCompatActivity {
    private CircleImageView dealImage;
    private EditText description,shortDescription,location,price;
    private AppCompatButton updateButton;
    private DatabaseReference ref;
    private int Pick_Image = 1;
    private Uri uri;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_deal);
        dealImage = findViewById(R.id.deal_image);
        description = findViewById(R.id.description);
        shortDescription = findViewById(R.id.short_description);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
        updateButton = findViewById(R.id.update_button);

        Intent intent = getIntent();
        String ilanid = intent.getStringExtra("ilanID");
        String first_image = intent.getStringExtra("imageUrl");
        String first_description = intent.getStringExtra("description");
        String first_shortDescription = intent.getStringExtra("shortDescription");
        String first_price = intent.getStringExtra("price");
        String first_location = intent.getStringExtra("location");
        String first_currentUserID = intent.getStringExtra("currentUserID");
        String ilanID = ilanid;
        String currentUserID = first_currentUserID;

        ref = FirebaseDatabase.getInstance().getReference("images").child(ilanid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                description.setText(first_description);
                shortDescription.setText(first_shortDescription);
                location.setText(first_location);
                price.setText(first_price);
                Glide.with(getApplicationContext().getApplicationContext()).load(first_image).into(dealImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seçilen Resim"),Pick_Image);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference().child("images/"+ UUID.randomUUID().toString());
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("URL","onSuccess: "+uri);

                                // Database de veriyi update ediyor
                                Map<String,String> map = new HashMap<>();
                                map.put("description",description.getText().toString().trim());
                                map.put("shortDescription",shortDescription.getText().toString().trim());
                                map.put("ilanID",ilanID.toString().trim());
                                map.put("currentUserID",currentUserID.toString().trim());
                                map.put("location",location.getText().toString().trim());
                                map.put("price",price.getText().toString().trim());

                                if (uri != null){
                                    map.put("imageUrl",uri.toString());
                                }
                                reference = FirebaseDatabase.getInstance().getReference("images").child(ilanid);
                                reference.setValue(map);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Pick_Image && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {

                uri = data.getData();
                dealImage.setImageURI(uri);
            }
        }
    }
}