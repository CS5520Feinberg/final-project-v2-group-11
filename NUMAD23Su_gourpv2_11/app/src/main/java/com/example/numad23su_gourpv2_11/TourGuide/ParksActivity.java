package com.example.numad23su_gourpv2_11.TourGuide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class ParksActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private LocationAdapter myAdapter;
    private final ArrayList<LocationClass> locations = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<JSONObject> listOfParks;
    private double currentLatitude;
    private double currentLongitude;
    private double longitudeValue;
    private double latitudeValue;

    private Location lastLocation;

    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                longitudeValue = location.getLongitude();
                latitudeValue = location.getLatitude();
                listOfParks = new ArrayList<JSONObject>();
                try {
                    setListOfParks();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parks);

        RecyclerView recyclerView = findViewById(R.id.parkRecyclerView);
        SearchView searchView = findViewById(R.id.parkSearchView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500).build();
        initializeData(savedInstanceState);

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

    private void initializeData(Bundle savedInstanceState) {
        if (savedInstanceState != null && (savedInstanceState.containsKey("longitude") && savedInstanceState.containsKey("latitude"))) {
            double longitudeSaved= savedInstanceState.getDouble("longitude");
            double latitudeSaved= savedInstanceState.getDouble("latitude");
            lastLocation = new Location("");
            lastLocation.setLatitude(latitudeSaved);
            lastLocation.setLongitude(longitudeSaved);
        }
        else {
            lastLocation = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkAndStart();
        } else {
            askPermission();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void checkAndStart() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            if(location != null){
                lastLocation = location;
            }
        });
    }

    private void askPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkAndStart();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(lastLocation != null) {
            outState.putDouble("longitude", lastLocation.getLongitude());
            outState.putDouble("latitude", lastLocation.getLatitude());
        }
    }

    protected void setListOfParks() throws MalformedURLException {
        try {
            Log.d("long", ("long: " + longitudeValue));
            Log.d("lat", ("lat: " + latitudeValue));
            String urlBuilder = String.format("https://api.yelp.com/v3/businesses/search?latitude=%s&longitude=%s&open_now=%s&sort_by=%s&term=%s", latitudeValue, longitudeValue, "true", "distance", "meal");
            URL url = new URL(urlBuilder);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d("responseCode", String.valueOf(responseCode));
            if(responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder stringBuilt = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()){
                    stringBuilt.append(scanner.nextLine());
                }
                scanner.close();
                Log.w("tag", "HERE");
                Log.d("tag", String.valueOf(stringBuilt));
            }
            connection.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
