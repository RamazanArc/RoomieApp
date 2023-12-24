package com.example.roomieapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roomieapp.R;
import com.example.roomieapp.adapters.HomeAdapter;
import com.example.roomieapp.listeners.ItemListener;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.model.User;
import com.example.roomieapp.screens.DetailsActivity;
import com.example.roomieapp.screens.LoginActivity;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements ItemListener {

    private RecyclerView topDealRV;
    private HomeAdapter adapter;
    private List<Item> itemList;
    private CircleImageView profileImage;
    private TextView username;
    private DatabaseReference ref;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topDealRV = view.findViewById(R.id.top_deal_RV);
        profileImage = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.user_name);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText("Merhaba "+user.getName());
                if (user.getImage().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getActivity().getApplicationContext()).load(user.getImage()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        itemList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            itemList.add(new Item(
                                    Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("currentUserID").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("ilanID").getValue()).toString()
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        adapter = new HomeAdapter(getContext(),itemList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);

    }
    @Override
    public void OnItemPosition(int position){
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("price",itemList.get(position).getPrice());
        intent.putExtra("location",itemList.get(position).getLocation());
        intent.putExtra("description",itemList.get(position).getDescription());
        intent.putExtra("shortDescription",itemList.get(position).getShortDescription());
        intent.putExtra("imageUrl",itemList.get(position).getImageUrl());
        intent.putExtra("ilanID",itemList.get(position).getIlanID());

        startActivity(intent);
    }

}