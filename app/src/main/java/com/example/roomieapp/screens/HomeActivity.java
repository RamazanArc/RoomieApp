package com.example.roomieapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.roomieapp.R;
import com.example.roomieapp.fragments.AccountFragment;
import com.example.roomieapp.fragments.ChatFragment;
import com.example.roomieapp.fragments.AddDealFragment;
import com.example.roomieapp.fragments.HomeFragment;
import com.example.roomieapp.fragments.MyDealsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.menu_fav) {
            fragment = new AddDealFragment();
        } else if (itemId == R.id.menu_chat) {
            fragment = new ChatFragment();
        } else if (itemId == R.id.menu_account) {
            fragment = new AccountFragment();
//        }else if (itemId == R.id.logaout_account){
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
        } else if (itemId == R.id.my_deals) {
            fragment = new MyDealsFragment();
        }

        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }
}