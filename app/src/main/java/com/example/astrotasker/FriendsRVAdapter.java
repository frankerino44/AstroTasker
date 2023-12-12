package com.example.astrotasker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendsRVAdapter extends RecyclerView.Adapter<FriendsRVAdapter.MyViewHolder> {
    private ArrayList<User> friendsUsers;

    // Constructor to initialize the list
    public FriendsRVAdapter(ArrayList<User> friendsUsers) {
        this.friendsUsers = friendsUsers;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView friendsUsernameTV;
        ImageView friendsProfilePhotoIV;
        TextView friendsCurrentLevelTV;
        TextView friendsNextLevelTV;
        ProgressBar friendsProgressBar;
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseUser currentUser;
        String uid;

        public MyViewHolder(View view) {
            super(view);
            friendsUsernameTV = view.findViewById(R.id.friendsUsernameTV);
            friendsProfilePhotoIV = view.findViewById(R.id.friendsProfilePhotoIV);
            friendsCurrentLevelTV = view.findViewById(R.id.friendsCurrentLevelTV);
            friendsNextLevelTV = view.findViewById(R.id.friendsNextLevelTV);
            friendsProgressBar = view.findViewById(R.id.friendsProgressBar);

            database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = friendsUsers.get(position);

        Log.i("USER USERNAME", user.getUsername());

        holder.friendsUsernameTV.setText(user.getUsername());

        int imageNum = user.getProfilePhoto();
        int imageID = holder.itemView.getContext().getResources().getIdentifier("alien_" + imageNum, "drawable", holder.itemView.getContext().getPackageName());
        holder.friendsProfilePhotoIV.setImageResource(imageID);

        int level = user.getLevel();
        holder.friendsCurrentLevelTV.setText(String.valueOf(level));
        int nextLevel = level+1;
        holder.friendsNextLevelTV.setText(String.valueOf(nextLevel));

        double requiredXP = 10; // XP required for the first level
        double tempXP = user.getXp();

        while (tempXP >= requiredXP) {
            tempXP -= requiredXP;
            requiredXP *= 2;
        }

        holder.friendsProgressBar.setProgress((int)(100*(tempXP/requiredXP)));
    }

    @Override
    public int getItemCount() {
        return friendsUsers.size();
    }
}