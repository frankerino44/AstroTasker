package com.example.astrotasker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextView firstNameTV;
    TextView lastNameTV;
    TextView usernameTV;
    TextView emailTV;
    ImageView profilePhotoIV;

    String uid;
    String firstName;
    String lastName;
    String username;
    String email;
    int imageNum;
    int imageID;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firstNameTV = view.findViewById(R.id.firstNameTV);
        lastNameTV = view.findViewById(R.id.lastNameTV);
        usernameTV = view.findViewById(R.id.usernameTV);
        emailTV = view.findViewById(R.id.emailTV);
        profilePhotoIV = view.findViewById(R.id.profilePhotoIV);

        getUserInfo();
        /*setFirstNameTV();
        setLastNameTV();
        setUsernameTV();
        setEmailTV();*/

        return view;
    }

    private void getUserInfo() {
        reference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    username = user.getUsername();
                    email = user.getEmail();
                    imageNum = user.getProfilePhoto();

                    imageID = requireContext().getResources().getIdentifier("alien_" + imageNum, "drawable", requireContext().getPackageName());

                    setFields();
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
    }

    private void setFields() {
        firstNameTV.setText(firstName);
        lastNameTV.setText(lastName);
        usernameTV.setText(username);
        emailTV.setText(email);
        profilePhotoIV.setImageResource(imageID);
    }

    public void setFirstNameTV() {
        reference.child("Users").child(uid).child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.getValue(String.class);
                    firstNameTV.setText(firstName);
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
    }

    public void setLastNameTV() {
        reference.child("Users").child(uid).child("lastName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String lastName = dataSnapshot.getValue(String.class);
                    lastNameTV.setText(lastName);
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
    }

    public void setUsernameTV() {
        reference.child("Users").child(uid).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.getValue(String.class);
                    usernameTV.setText(username);
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
    }

    public void setEmailTV() {
        reference.child("Users").child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.getValue(String.class);
                    emailTV.setText(email);
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
    }
}