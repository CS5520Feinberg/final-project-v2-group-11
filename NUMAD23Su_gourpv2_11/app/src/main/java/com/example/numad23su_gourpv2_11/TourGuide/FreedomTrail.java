package com.example.numad23su_gourpv2_11.TourGuide;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numad23su_gourpv2_11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreedomTrail extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocationAdapter myAdapter;
    private ArrayList<LocationClass> locations = new ArrayList<LocationClass>();

    private Button freedomtrailFullMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freedom_trail);

        freedomtrailFullMapButton = findViewById(R.id.freedomTrailFullMapBtn);

        recyclerView = findViewById(R.id.freedomtrailRecyclerView);
        SearchView searchView = findViewById(R.id.freedomtrailSearchView);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Locations").child("Freedom_Trail");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LocationClass location = postSnapshot.getValue(LocationClass.class);

                    locations.add(location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });

        // Add some locations to your list
        //LocationClass lc = new LocationClass("Old State House", "42.3586300392819", "-71.05750988813851", "6177201713", "https://www.thefreedomtrail.org/trail-sites/old-state-house", "10");
        //locations.add(lc);

        // Instantiate the adapter with your list of locations and set it on the RecyclerView
        myAdapter = new LocationAdapter(locations);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        freedomtrailFullMapButton.setOnClickListener(view -> {

        });

    }
}
