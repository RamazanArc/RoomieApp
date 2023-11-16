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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView haveAccount;
    private FirebaseAuth mAuth;
    private EditText userName,userEmail,userPassword,confirmPassword;
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
        mRef = FirebaseDatabase.getInstance().getReference();

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
                if(userName.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Adınızı Giriniz", Toast.LENGTH_SHORT).show();
                } else if (userEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this,"Geçerli Bir Mail Giriniz", Toast.LENGTH_SHORT).show();
                } else if (userPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this,"Şifre Giriniz", Toast.LENGTH_SHORT).show();
                } else if (!userPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this,"Şifreler Uyuşmuyor", Toast.LENGTH_SHORT).show();
                }else {
                    if (emailChecker(userEmail.getText().toString().trim())){
                        createUser(userEmail.getText().toString().trim(),
                                userPassword.getText().toString().trim(),
                                userName.getText().toString().trim());
                    }else {
                        Toast.makeText(SignUpActivity.this,"Geçerli Bir Mail Giriniz", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    boolean emailChecker(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                User user = new User(name,email);

                if (task.isSuccessful()){

                    //Veriyi otomatik oluşturulan key ile database'e kaydediyor
                    //push() fonksiyonu otomatik key oluşumu için kullanılıyor
                    mRef.child("users").push().setValue(user);
                    startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                    finish();
                    Toast.makeText(SignUpActivity.this,"Kullanıcı Başarılı Bir Şekilde Oluşturuldu ",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUpActivity.this,"Hata",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,"Hata",Toast.LENGTH_SHORT).show();
            }
        });
    }

}