package com.example.astrotasker;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchRVAdapter extends RecyclerView.Adapter<SearchRVAdapter.MyViewHolder> {
    private ArrayList<User> searchUsers;

    // Constructor to initialize the list
    public SearchRVAdapter(ArrayList<User> searchUsers) {
        this.searchUsers = searchUsers;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView searchUsernameTV;
        ImageView searchProfilePhotoIV;
        TextView searchCurrentLevelTV;
        TextView searchNextLevelTV;
        ProgressBar searchProgressBar;
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseUser currentUser;
        String uid;

        public MyViewHolder(View view) {
            super(view);
            searchUsernameTV = view.findViewById(R.id.searchUsernameTV);
            searchProfilePhotoIV = view.findViewById(R.id.searchProfilePhotoIV);
            searchCurrentLevelTV = view.findViewById(R.id.searchCurrentLevelTV);
            searchNextLevelTV = view.findViewById(R.id.searchNextLevelTV);
            searchProgressBar = view.findViewById(R.id.searchProgressBar);

            database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = searchUsers.get(position);

        holder.searchUsernameTV.setText(user.getUsername());

        int imageNum = user.getProfilePhoto();
        int imageID = holder.itemView.getContext().getResources().getIdentifier("alien_" + imageNum, "drawable", holder.itemView.getContext().getPackageName());
        holder.searchProfilePhotoIV.setImageResource(imageID);

        int level = user.getLevel();
        holder.searchCurrentLevelTV.setText(String.valueOf(level));
        int nextLevel = level+1;
        holder.searchNextLevelTV.setText(String.valueOf(nextLevel));

        double requiredXP = 10; // XP required for the first level
        double tempXP = user.getXp();

        while (tempXP >= requiredXP) {
            tempXP -= requiredXP;
            requiredXP *= 2;
        }

        holder.searchProgressBar.setProgress((int)(100*(tempXP/requiredXP)));
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }
}