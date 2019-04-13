package com.example.familymapclient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;

        if (Model.instance().getAuthToken() == null) {
            fragment = new LoginFragment();
        } else {
            fragment = new MapFragment();
        }

        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment();
        
    }

}