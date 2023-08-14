package com.example.numad23su_gourpv2_11.TourGuide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ParksActivity extends AppCompatActivity {
    private LocationAdapter myAdapter;
    private final ArrayList<LocationClass> locations = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude = 0;
    private double currentLongitude = 0;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parks);

        RecyclerView recyclerView = findViewById(R.id.parkRecyclerView);
        SearchView searchView = findViewById(R.id.parkSearchView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateCurrentLocation();
        Spinner sortSpinner = findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0) {
                    sortByName();
                } else if (position == 1) {
                    // assuming you've some way to get current user's location
                    sortByDistance(currentLatitude, currentLongitude);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Locations").child("Freedom_Trail");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LocationClass location = postSnapshot.getValue(LocationClass.class);

                    locations.add(location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });

        // Instantiate the adapter with your list of locations and set it on the RecyclerView
        myAdapter = new LocationAdapter(locations);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Pass the search text to your adapter
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void sortByName() {
        Collections.sort(locations, Comparator.comparing(LocationClass::getName));
    }

    private void sortByDistance(final double userLat, final double userLng) {
        Collections.sort(locations, (location1, location2) -> {
            double lat1 = Double.parseDouble(location1.getLatitude());
            double lng1 = Double.parseDouble(location1.getLongitude());

            double lat2 = Double.parseDouble(location2.getLatitude());
            double lng2 = Double.parseDouble(location2.getLongitude());

            double distanceToLoc1 = distance(userLat, userLng, lat1, lng1);
            double distanceToLoc2 = distance(userLat, userLng, lat2, lng2);

            return Double.compare(distanceToLoc1, distanceToLoc2);
        });
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        // compute the distance between two lat-lng pairs. You can use the Haversine formula or any library method
        // for simplicity, you can use direct distance calculation without accounting for Earth's curvature
        double a = Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2);
        return Math.sqrt(a);
    }

    @SuppressLint("MissingPermission")
    private void updateCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    }
                });
    }




}
