package com.example.roomieapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomieapp.model.Item;

import com.example.roomieapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddDealFragment extends Fragment {

    private EditText description, shortDescription, price, location, imageUrl;
    private AppCompatButton dealAddButton;
    private String currentUserId;
    private Button  btnResimSec;
    private static final int REQUEST_IMAGE_PICK = 1;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite,container,false);
        description = view.findViewById(R.id.description);
        shortDescription = view.findViewById(R.id.short_description);
        price = view.findViewById(R.id.price);
        location = view.findViewById(R.id.location);
        dealAddButton = view.findViewById(R.id.deal_add_button);
        btnResimSec = view.findViewById(R.id.btn_resim);

        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_IMAGE_PICK);
            }
        });

        dealAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (selectedImageUri != null){
                  String desc = description.getText().toString();
                  String shortDsc = shortDescription.getText().toString();
                  String prc = price.getText().toString();
                  String loc = location.getText().toString();
                  String currentUserId = firebaseUser.getUid();

                  StorageReference imageRef = storageReference.child("images/" +selectedImageUri.getLastPathSegment());
                  imageRef.putFile(selectedImageUri)
                          .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                              @Override
                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        String dealKey = databaseReference.push().getKey();
                                        Item item = new Item(desc,shortDsc,prc,loc,currentUserId,imageUrl,dealKey);
                                        databaseReference.child(dealKey).setValue(item);
                                    }
                                });
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
                              }
                          });
              }

            }
        });
            return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null){
            selectedImageUri = data.getData();
        }
    }
}