package com.example.astrotasker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    EditText searchET;
    RecyclerView searchRV;
    RecyclerView friendsRV;
    ImageButton searchButton;

    String uid;
    String currentUsername;
    ArrayList<User> searchUsers;
    SearchRVAdapter searchRVAdapter;

    public SocialFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        reference.child("Users").child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUsername = dataSnapshot.getValue(String.class);
                    Log.i("CURRENT ONCREATE", currentUsername);
                } else {
                    // User data does not exist

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });

        searchET = view.findViewById(R.id.searchET);
        searchRV = view.findViewById(R.id.searchRV);
        friendsRV = view.findViewById(R.id.friendsRV);
        searchUsers = new ArrayList<>();
        searchRVAdapter = new SearchRVAdapter(searchUsers);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        searchRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRV.setAdapter(searchRVAdapter);

        return view;
    }

    public void search() {
        String searchText = searchET.getText().toString();
        reference.child("Users").orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the dataSnapshot to get the matching users
                searchUsers.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // You can now access user data using userSnapshot
                    // For example, userSnapshot.child("username").getValue() to get the username
                    User user = userSnapshot.getValue(User.class);

                    if (currentUsername != user.getUsername()) {
                        searchUsers.add(user);
                    }
                }

                searchRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public void add(View view) {

    }
}