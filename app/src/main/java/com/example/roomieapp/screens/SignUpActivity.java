package com.example.roomieapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomieapp.R;
import com.example.roomieapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextView haveAccount;
    private FirebaseAuth mAuth;
    private EditText userName, userEmail, userPassword, confirmPassword;
    private AppCompatButton signUpButton;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        haveAccount = findViewById(R.id.have_account);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.user_confirm_password);
        signUpButton = findViewById(R.id.sign_up_button);

        mAuth = FirebaseAuth.getInstance();

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Adınızı Giriniz", Toast.LENGTH_SHORT).show();
                } else if (userEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Geçerli Bir Mail Giriniz", Toast.LENGTH_SHORT).show();
                } else if (userPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Şifre Giriniz", Toast.LENGTH_SHORT).show();
                } else if (!userPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Şifreler Uyuşmuyor", Toast.LENGTH_SHORT).show();
                } else {
                    if (emailChecker(userEmail.getText().toString().trim())) {
                        createUser(userEmail.getText().toString().trim(),
                                userPassword.getText().toString().trim(),
                                userName.getText().toString().trim());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Geçerli Bir Mail Giriniz", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            mRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userID", userId);
                            hashMap.put("name", name);
                            hashMap.put("image","default");
                            hashMap.put("email",email);

                            mRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(SignUpActivity.this, "Kayıt olma başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}