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
import android.widget.ImageButton;
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
    ImageButton profileButton;
    ImageButton homeButton;
    ImageButton socialButton;
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

        profileButton = findViewById(R.id.profileButton);
        homeButton = findViewById(R.id.homeButton);
        socialButton = findViewById(R.id.socialButton);

        homeButton.setBackgroundResource(R.drawable.nav_bar_light);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, homeFragment)
                .commit();
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



    public void profile(View view) {
        homeButton.setBackgroundResource(R.drawable.nav_bar);
        socialButton.setBackgroundResource(R.drawable.nav_bar);
        profileButton.setBackgroundResource(R.drawable.nav_bar_light);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .commit();
    }

    public void home(View view) {
        profileButton.setBackgroundResource(R.drawable.nav_bar);
        socialButton.setBackgroundResource(R.drawable.nav_bar);
        homeButton.setBackgroundResource(R.drawable.nav_bar_light);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, homeFragment)
                .commit();
    }

    public void social(View view) {
        profileButton.setBackgroundResource(R.drawable.nav_bar);
        homeButton.setBackgroundResource(R.drawable.nav_bar);
        socialButton.setBackgroundResource(R.drawable.nav_bar_light);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, socialFragment)
                .commit();
    }
}