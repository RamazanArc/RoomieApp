package com.example.roomieapp.fragments;

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
import com.example.roomieapp.adapters.HomeAdapter;
import com.example.roomieapp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView topDealRV;
    private HomeAdapter adapter;
    private List<Item> itemList;

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

        itemList = new ArrayList<>();
        itemList.add(new Item("Yılmazlar Mah.", "1500₺ ", "2+1 Daire"));
        itemList.add(new Item("Yılmazlar Mah.", "1500₺", "2+1 Daire"));
        itemList.add(new Item("Yılmazlar Mah.", "1500₺", "2+1 Daire"));
        itemList.add(new Item("Yılmazlar Mah.", "1500₺", "2+1 Daire"));

        adapter = new HomeAdapter(getContext(),itemList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);
    }
}