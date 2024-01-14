package com.example.roomieapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DetailsActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView price,shortDescription,description;
    private Button chatButton;
    private String ilanID;
    private List<Item> itemList;
    private DatabaseReference databaseReference;
    GoogleMap mGoogleMap;
    MapView mMapView;

    String des, sDes, pri, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        databaseReference = FirebaseDatabase.getInstance().getReference("images");

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        shortDescription = findViewById(R.id.short_description);
        description = findViewById(R.id.description);
//        mMapView = findViewById(R.id.map);
//        if (mMapView != null){
//            mMapView.onCreate(null);
//            mMapView.onResume();
//            mMapView.getMapAsync(this);
//        }

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

    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        MapsInitializer.initialize(getApplicationContext());
//
//        Double latitude = 38.6075536;
//        Double longitude = 27.0915555;
//
//
//        mGoogleMap = googleMap;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("yerIsim").snippet("Here"));
//
//        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(45).build();
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
//    }
}