package com.example.roomieapp.fragments;

import static android.app.Activity.RESULT_OK;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private EditText userName,userEmail;
    private AppCompatButton updateButton;
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
        updateButton = view.findViewById(R.id.update_button);

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
                uploadImage();
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

    private void uploadImage() {

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
                        reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                      FirebaseAuth.getInstance().getCurrentUser()
                                       .updateEmail(userEmail.getText().toString().trim())
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isComplete()){

                                                    Toast.makeText(getContext(), "Başarılı", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
                                                }
                                           }
                                       });
                            }
                        });
                    }
                });
            }
        });

    }
}