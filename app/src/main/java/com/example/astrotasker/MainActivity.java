package com.example.astrotasker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FragmentContainerView fragmentContainerView;
    Fragment profileFragment;
    Fragment homeFragment;
    Fragment socialFragment;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String email;
    String value;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
        socialFragment = new SocialFragment();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, homeFragment)
                .commit();
    }

    /*private void getValue(String key) {
        reference.child("Users").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    String value;
                    switch (key) {
                        case "firstName":
                            value = user.getFirstName();
                            break;
                        case "lastName":
                            value = user.getLastName();
                            break;
                        case "username":
                            value = user.getUsername();
                            break;
                        case "email":
                            value = user.getEmail();
                            break;
                        default:
                            value = "";
                            break;
                    }
                    Log.i("Value", value);
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
    }*/

    public String getFirstName() {
        reference.child("Users").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    value = user.getFirstName();;
                } else {
                    // User data does not exist
                    value = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
                value = "";
            }
        });

        return value;
    }

    public String getLastName() {
        reference.child("Users").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    value = user.getLastName();;
                } else {
                    // User data does not exist
                    value = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
                value = "";
            }
        });

        return value;
    }

    public String getUsername() {
        reference.child("Users").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    value = user.getUsername();;
                } else {
                    // User data does not exist
                    value = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
                value = "";
            }
        });

        return value;
    }

    public String getEmail() {
        reference.child("Users").orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    value = user.getEmail();;
                } else {
                    // User data does not exist
                    value = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
                value = "";
            }
        });

        return value;
    }

    public void profile(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .commit();
    }

    public void home(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, homeFragment)
                .commit();
    }

    public void social(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, socialFragment)
                .commit();
    }
}