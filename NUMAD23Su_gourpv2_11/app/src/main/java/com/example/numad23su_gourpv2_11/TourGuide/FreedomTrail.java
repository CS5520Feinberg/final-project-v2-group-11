package com.example.numad23su_gourpv2_11.TourGuide;

import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numad23su_gourpv2_11.R;
import java.util.ArrayList;

public class FreedomTrail extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocationAdapter myAdapter;
    private ArrayList<LocationClass> locations = new ArrayList<LocationClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freedom_trail);

        recyclerView = findViewById(R.id.freedomtrailRecyclerView);
        SearchView searchView = findViewById(R.id.freedomtrailSearchView);

        // Add some locations to your list
        LocationClass lc = new LocationClass("Old State House", "42.3586300392819", "-71.05750988813851", "6177201713", "https://www.thefreedomtrail.org/trail-sites/old-state-house", "10");
        locations.add(lc);

        // Instantiate the adapter with your list of locations and set it on the RecyclerView
        myAdapter = new LocationAdapter(locations);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
