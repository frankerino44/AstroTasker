package com.example.astrotasker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUpRedirect(View view) {
        redirectToSignUp();
    }

    private void redirectToSignUp() {
        Intent redirectToSignUp = new Intent(this, SignUp.class);
        startActivity(redirectToSignUp);
    }
}