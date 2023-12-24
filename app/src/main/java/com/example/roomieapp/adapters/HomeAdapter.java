package com.example.roomieapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.roomieapp.R;
import com.example.roomieapp.listeners.ItemListener;
import com.example.roomieapp.model.Item;
import com.example.roomieapp.screens.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private ItemListener itemListener;

    public HomeAdapter(Context context, List<Item> itemList, ItemListener itemListener) {
        this.context = context;
        this.itemList = itemList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_deals,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.price.setText("İletişime Geç");
        holder.location.setText(itemList.get(position).getLocation());
        holder.shortDescription.setText(itemList.get(position).getShortDescription());
        Glide.with(context)
                .load(itemList.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.relativeLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (itemList.get(position).getCurrentUserID().equals(firebaseUser.getUid())){
                    Toast.makeText(context, "Kendinize mesaj atamazsınız", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("currentUserID",itemList.get(position).getCurrentUserID());
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView price;
        private TextView location;
        private TextView shortDescription;
        private RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            shortDescription = itemView.findViewById(R.id.short_description);
            relativeLayout = itemView.findViewById(R.id.relative_layout);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}
