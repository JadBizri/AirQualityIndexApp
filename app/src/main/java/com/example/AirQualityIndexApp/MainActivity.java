package com.example.AirQualityIndexApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //open main fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    //called when moving to next fragment and taking selected city to it
    @Override
    public void sendSelectedCity(DataServices.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, AqiFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }
}