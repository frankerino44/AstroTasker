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
import android.widget.Toast;

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
public class SocialFragment extends Fragment implements SearchRVAdapter.OnItemClickListener{

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    EditText searchET;
    RecyclerView searchRV;
    RecyclerView friendsRV;
    ImageButton searchButton;

    String uid;
    String currentUsername;
    String addedUsername;
    ArrayList<User> searchUsers;
    ArrayList<User> friendsUsers;
    ArrayList<String> friendsUsernames;
    SearchRVAdapter searchRVAdapter;
    FriendsRVAdapter friendsRVAdapter;

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

        loadCurrentUsername();

        searchET = view.findViewById(R.id.searchET);
        searchRV = view.findViewById(R.id.searchRV);
        friendsRV = view.findViewById(R.id.friendsRV);
        searchUsers = new ArrayList<>();
        friendsUsers = new ArrayList<>();
        searchRVAdapter = new SearchRVAdapter(searchUsers, this);
        friendsRVAdapter = new FriendsRVAdapter(friendsUsers);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        searchRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRV.setAdapter(searchRVAdapter);
        friendsRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        friendsRV.setAdapter(friendsRVAdapter);

        friendsUsernames = new ArrayList<>();
        loadFriends();
        friendsRVAdapter.notifyDataSetChanged();

        return view;
    }

    private void loadCurrentUsername() {
        reference.child("Users").child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUsername = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadFriends() {
        reference.child("Users").child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> friends = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {
                        friends.add(friendsSnapshot.getValue(String.class));
                    }

                    if (friends != null) {
                        friendsUsernames.addAll(friends);
                        usernamesToFriendsList();
                    }
                } else {
                    usernamesToFriendsList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void usernamesToFriendsList() {
        for (String username : friendsUsernames) {
            reference.child("Users").orderByChild("username").equalTo(username).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        friendsUsers.add(user);
                    }

                    friendsRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }




    @Override
    public void add(String text) {
        addedUsername = text;

        reference.child("Users").child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> friends = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        friends.add(friendSnapshot.getValue(String.class));
                    }

                    if (friends == null || friends.isEmpty() || !(friends.contains(addedUsername))) {
                        addFriend();
                    } else {
                        Toast.makeText(requireContext(), "You've already added this user!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addFriend();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void addFriend() {
        reference.child("Users").orderByChild("username").equalTo(addedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    friendsUsers.add(user);
                    friendsRVAdapter.notifyDataSetChanged();
                    reference.child("Users").child(uid).child("friends").push().setValue(addedUsername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
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

                    String username = user.getUsername();

                    if (!(currentUsername.equals(username))) {
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
}