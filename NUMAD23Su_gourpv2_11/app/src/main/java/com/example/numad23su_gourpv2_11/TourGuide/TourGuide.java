package com.example.numad23su_gourpv2_11.TourGuide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.numad23su_gourpv2_11.R;

public class TourGuide extends AppCompatActivity {

    Button freedomTrailButton;
    Button restaurantButton;
    Button parksButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tour_guide_home_screen);

        freedomTrailButton = findViewById(R.id.freedom_trail_button);
        freedomTrailButton.setOnClickListener(view -> freedomTrail());

        restaurantButton = findViewById(R.id.restaurants_button);
        restaurantButton.setOnClickListener(view -> restaurants());

        parksButton = findViewById(R.id.parks_button);
        parksButton.setOnClickListener(view -> parks());
    }

    private void freedomTrail() {
        Intent intent = new Intent(this, FreedomTrail.class);
        startActivity(intent);
    }

    private void restaurants() {
        Intent intent = new Intent(this, Restaurants.class);
        startActivity(intent);
    }

    private void parks() {
        Intent intent = new Intent(this, Parks.class);
        startActivity(intent);
    }


}
