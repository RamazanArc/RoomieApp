package com.example.roomieapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.example.roomieapp.screens.DetailsActivity;
import com.example.roomieapp.screens.LoginActivity;
import com.example.roomieapp.screens.MessageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder>{
    private Context context;
    private List<Item> itemList;
    private ItemListener itemListener;
    private DatabaseReference databaseReference;

    public DealAdapter(Context context, List<Item> itemList, ItemListener itemListener) {
        this.context = context;
        this.itemList = itemList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public DealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_deals,parent,false);


        return new DealAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.price.setText("İlanı Kaldır");
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
                AlertDialog alertDialog = alertOfDealDelete(itemList.get(position).getIlanID());
                alertDialog.show();
            }
        });
    }
    private void deleteDeal(String id){
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("images").child(id);

        Task<Void> mTask = dbref.removeValue();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "silinemedi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    AlertDialog alertOfDealDelete(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("İlanı Silme İstiyor Musunuz ?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteDeal(id);

            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
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
