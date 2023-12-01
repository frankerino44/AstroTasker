package com.example.astrotasker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FragmentContainerView fragmentContainerView;
    Fragment profileFragment;
    Fragment homeFragment;
    Fragment socialFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
        socialFragment = new SocialFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, homeFragment)
                .commit();
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