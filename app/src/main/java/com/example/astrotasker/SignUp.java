package com.example.astrotasker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    EditText firstNameET;
    EditText lastNameET;
    EditText usernameET;
    EditText emailET;
    EditText passwordET;

    ArrayList<String> usernameList;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        usernameET = findViewById(R.id.usernameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        usernameList = new ArrayList<>();
    }

    public void signUp(View view) {
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        reference.child("Usernames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    ArrayList<String> usernameList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        usernameList.add(value);
                    }

                    if (usernameList.contains(username)) {
                        Toast.makeText(SignUp.this, "This username is already in use by another account.", Toast.LENGTH_SHORT).show();
                    } else {
                        createUser(email, password, firstName, lastName, username);
                    }
                } else {
                    createUser(email, password, firstName, lastName, username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignUp.this, "Error checking database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String email, String password, String firstName, String lastName, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, save new user in Freebase
                            uid = auth.getUid();
                            reference.child("Users").child(uid).push().getKey();
                            User user = new User();

                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setUsername(username);
                            user.setEmail(email);
                            user.setXp(0);
                            user.setLevel(1);

                            reference.child("Users").child(uid).setValue(user);
                            reference.child("Usernames").push().setValue(username);

                            redirectToCreateProfile();
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(SignUp.this, "Your password must be at least 6 characters long.",
                                        Toast.LENGTH_SHORT).show();
                            } else if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUp.this, "This email address is already in use by another account.",
                                        Toast.LENGTH_SHORT).show();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUp.this, "Please use a properly formatted email.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "Authentication failed", e);
                            }
                        }
                    }
                });
    }

    public void backToLogIn(View view) {
        redirectToLogIn();
    }

    private void redirectToLogIn() {
        Intent redirectToLogIn = new Intent(this, LogIn.class);
        startActivity(redirectToLogIn);
    }

    private void redirectToCreateProfile() {
        Intent redirectToCreateProfile = new Intent(this, ChooseAvatar.class);
        startActivity(redirectToCreateProfile);
    }
}