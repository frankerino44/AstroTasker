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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    EditText emailET;
    EditText passwordET;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
    }

    public void logIn(View view) {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkForPhoto();
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LogIn.this, "Account not found.",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "Authentication failed", e);
                            } else {
                                Toast.makeText(LogIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "Authentication failed", e);
                            }
                        }
                    }
                });
    }

    private void checkForPhoto() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        reference.child("Users").child(uid).child("profilePhoto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    redirectToMainActivity();
                } else {
                    redirectToCreateProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.i("CANCELLED", "CANCELLED");
            }
        });
    }

    public void signUpRedirect(View view) {
        redirectToSignUp();
    }

    private void redirectToSignUp() {
        Intent redirectToSignUp = new Intent(this, SignUp.class);
        startActivity(redirectToSignUp);
    }

    private void redirectToCreateProfile() {
        Intent redirectToCreateProfile = new Intent(this, CreateProfile.class);
        startActivity(redirectToCreateProfile);
    }

    private void redirectToMainActivity() {
        Intent redirectToMainActivity = new Intent(this, MainActivity.class);
        startActivity(redirectToMainActivity);
    }
}