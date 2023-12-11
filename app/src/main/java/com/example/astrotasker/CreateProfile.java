package com.example.astrotasker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfile extends AppCompatActivity {

    ImageView profileSelectIV;
    int imageID;
    int imageNum;
    String uid;

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

        profileSelectIV = findViewById(R.id.profileSelectIV);
        imageNum = 1;

        imageID = this.getResources().getIdentifier("alien_" + imageNum, "drawable", this.getPackageName());
        profileSelectIV.setImageResource(imageID);
    }

    public void left(View view) {
        if (imageNum == 1) {
            imageNum = 6;
        } else {
            imageNum--;
        }

        imageID = this.getResources().getIdentifier("alien_" + imageNum, "drawable", this.getPackageName());
        profileSelectIV.setImageResource(imageID);
    }

    public void right(View view) {
        if (imageNum == 6) {
            imageNum = 1;
        } else {
            imageNum++;
        }

        imageID = this.getResources().getIdentifier("alien_" + imageNum, "drawable", this.getPackageName());
        profileSelectIV.setImageResource(imageID);
    }

    public void accept(View view) {
        reference.child("Users").child(uid).child("profilePhoto").setValue(imageNum);
        redirectToMainActivity();
    }

    private void redirectToMainActivity() {
        Intent redirectToMainActivity = new Intent(this, MainActivity.class);
        startActivity(redirectToMainActivity);
    }
}