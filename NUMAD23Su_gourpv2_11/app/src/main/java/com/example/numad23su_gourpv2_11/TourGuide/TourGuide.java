package com.example.numad23su_gourpv2_11.TourGuide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.numad23su_gourpv2_11.R;

public class TourGuide extends AppCompatActivity {

    Button freedomtrailBtn, resturantsBtn, parksBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_home_screen);

        freedomtrailBtn = findViewById(R.id.freedom_trail_button);
        resturantsBtn = findViewById(R.id.restaurants_button);
        parksBtn = findViewById(R.id.parks_button);

        freedomtrailBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TourGuide.this, FreedomTrail.class);
            startActivity(intent);
        });

        resturantsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TourGuide.this, RestaurantsActivity.class);
            startActivity(intent);
        });

        parksBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TourGuide.this, ParksActivity.class);
            startActivity(intent);
        });

    }



}
