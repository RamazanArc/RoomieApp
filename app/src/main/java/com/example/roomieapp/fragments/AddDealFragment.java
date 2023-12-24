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
    //    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//    FirebaseUser firebaseUser;
//
//    public FavouriteFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        description = view.findViewById(R.id.description);
//        shortDescription = view.findViewById(R.id.short_description);
//        price = view.findViewById(R.id.price);
//        location = view.findViewById(R.id.location);
//        dealAddButton = view.findViewById(R.id.deal_add_button);
//        imageView2 = view.findViewById(R.id.imageView2);
//
//        mRef = FirebaseDatabase.getInstance().getReference();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, 2);
//            }
//        });
//
//        dealAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (imageUri != null){
//                    createDeal(location.getText().toString().trim(),price.getText().toString().trim(),description.getText().toString().trim(),shortDescription.getText().toString().trim(),imageUri.toString().trim(),firebaseUser.getUid().toString().trim());
//                }else {
//                    Toast.makeText(getActivity().getApplicationContext(), "Fotoğaraf Seçiniz", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
//
//            imageUri = data.getData();
//            imageView2.setImageURI(imageUri);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favourite, container, false);
//    }
//
//    void createDeal(String location, String price, String description, String shortDescription, String imageUrl, String currentUserId){
//        Item item = new Item(description,shortDescription,price,location,imageUrl,currentUserId);
//        Random random = new Random();
//        int randomNumber = random.nextInt(1000000);
//        mRef.child("images").child("image"+randomNumber).setValue(item);
//    }
//
//    private void uploadToFirebase(Uri uri){
//        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Başarıyla yüklendi", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity().getApplicationContext(), "Yükleme hatası", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private String getFileExtension(Uri mUri) {
//        ContentResolver cr = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(mUri));
//    }
}