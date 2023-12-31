package com.example.roomieapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomieapp.R;
import com.example.roomieapp.adapters.DealAdapter;
import com.example.roomieapp.adapters.HomeAdapter;
import com.example.roomieapp.listeners.ItemListener;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.screens.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyDealsFragment extends Fragment implements ItemListener {
    private RecyclerView recyclerView;
    private DealAdapter dealAdapter;
    private List<Item> dealList;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_deals,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.my_deals_RV);

        dealList = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("images").orderByChild("currentUserID").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dealList.add(new Item(
                            Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("currentUserID").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("ilanID").getValue()).toString()
                    ));
                    dealAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dealAdapter = new DealAdapter(getContext(),dealList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dealAdapter);

    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("price",dealList.get(position).getPrice());
        intent.putExtra("location",dealList.get(position).getLocation());
        intent.putExtra("description",dealList.get(position).getDescription());
        intent.putExtra("shortDescription",dealList.get(position).getShortDescription());
        intent.putExtra("imageUrl",dealList.get(position).getImageUrl());
        intent.putExtra("ilanID",dealList.get(position).getIlanID());

        startActivity(intent);
    }
}