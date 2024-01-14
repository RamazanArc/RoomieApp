package com.example.roomieapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.model.User;
import com.example.roomieapp.screens.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class AccountFragment extends Fragment {


    private CircleImageView userProfile;
    private EditText userName,userEmail,newPassword,currentPassword;
    private AppCompatButton updateButton, logOutButton;
    private DatabaseReference ref;
    private int Pick_Image = 1;
    private Uri uri;
    private String id;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfile = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        newPassword = view.findViewById(R.id.user_password);
        currentPassword = view.findViewById(R.id.current_password);
        updateButton = view.findViewById(R.id.update_button);
        logOutButton = view.findViewById(R.id.logout_button);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());
                if (user.getImage().equals("default")){
                    userProfile.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getActivity().getApplicationContext()).load(user.getImage()).into(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userProfile.setOnClickListener(new View.OnClickListener() {
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
                uploadImage(currentPassword.getText().toString(),newPassword.getText().toString());
               // updatePassword(currentPassword.getText().toString(),newPassword.getText().toString());
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = createDialog();
                alertDialog.show();
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
                userProfile.setImageURI(uri);
            }
        }
    }
    AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Çıkış Yapmak İstiyor Musunuz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    private void uploadImage(String currentPassWord, String newPassword) {
       // updateEmail(currentPassWord,newEmail);
        updatePassword(currentPassWord,newPassword);

        StorageReference ref = FirebaseStorage.getInstance()
                .getReference().child("images/"+ UUID.randomUUID().toString());
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URL","onSuccess: "+uri);

                        // Database de veriyi update ediyor
                        Map<String,String> map = new HashMap<>();
                        map.put("name",userName.getText().toString().trim());
                        map.put("email",userEmail.getText().toString().trim());
                        map.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if (uri != null){
                            map.put("image",uri.toString());
                        }
                        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        reference.setValue(map);
                    }
                });
            }
        });

    }

    private void updatePassword(String currentPassword, String newPassword){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && !newPassword.isEmpty()){
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),currentPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Şifre güncellendi", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getContext(), "Şifre güncelleme hatası", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(getContext(), "Mevcut Şifre doğru değil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getContext(), "Yeni şifre belirlenmemiş veye kullanıcı oturumu kapalı", Toast.LENGTH_SHORT).show();
        }
    }
//    private void updateEmail(String currentPassword, String newEmail) {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//
//        if (user != null && !newEmail.isEmpty()) {
//            // Kullanıcının mevcut şifresini doğrula
//            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
//            user.reauthenticate(credential)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                // Yeni e-posta adresini güncelle
//                                user.updateEmail(newEmail)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(getContext(), "E-posta adresi güncellendi.", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(getContext(), "E-posta adresi güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            } else {
//                                Toast.makeText(getContext(), "Mevcut şifrenizi doğrulayamıyoruz.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(getContext(), "Yeni e-posta belirlenmemiş veya kullanıcı oturumu açık değil.", Toast.LENGTH_SHORT).show();
//        }
//    }
}