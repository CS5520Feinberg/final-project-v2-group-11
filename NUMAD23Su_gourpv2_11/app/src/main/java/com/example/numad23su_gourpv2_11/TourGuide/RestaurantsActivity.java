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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.BuildConfig;
import com.example.numad23su_gourpv2_11.R;
import com.example.numad23su_gourpv2_11.TourGuide.YelpAPIAccess.Business;
import com.example.numad23su_gourpv2_11.TourGuide.YelpAPIAccess.YelpLocationData;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class RestaurantsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private static final String YelpAPISecKey = BuildConfig.YELP_API_KEY;
    private LocationAdapter myAdapter;
    private ArrayList<LocationClass> mylocations = new ArrayList<>();
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude;
    private double currentLongitude;

    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                setLongitude(location.getLongitude());
                setLatitude(location.getLatitude());
                querySearch();
            }
        }
    };

    interface RequestRestaurant{
        @GET("/v3/businesses/search")
        Call<YelpLocationData> getLocation(@Header("Authorization") String authHeader, @Query("latitude") double latitude, @Query("longitude")double longitude, @Query("open_now") String open_now, @Query("sort_by") String sort_by, @Query("term") String term);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        RecyclerView recyclerView = findViewById(R.id.restaurantRecyclerView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build();
        initializeData(savedInstanceState);
        getLastLocation();

        // Instantiate the adapter with your list of locations and set it on the RecyclerView
        myAdapter = new LocationAdapter(mylocations);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    public void querySearch(){
        ArrayList<LocationClass> tempLocations = this.mylocations;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestaurantsActivity.RequestRestaurant requestRestaurant = retrofit.create(RestaurantsActivity.RequestRestaurant.class);
        Log.d("Location", String.format("Lat: %s, Long: %s", Double.toString(this.currentLatitude), Double.toString(this.currentLongitude)));
        requestRestaurant.getLocation("Bearer " + YelpAPISecKey, this.currentLatitude, this.currentLongitude, "true", "distance", "restaurant" ).enqueue(new Callback<YelpLocationData>() {
            @Override
            public void onResponse(Call<YelpLocationData> call, Response<YelpLocationData> response) {
                List<Business> responseData = response.body().getBusinesses();
                for (int i = 0; i < responseData.size(); i++) {
                    Long phoneNumber = 0L;
                    if(responseData.get(i).getPhone().length() != 0){
                        phoneNumber = Long.parseLong(responseData.get(i).getPhone().substring(1));
                    }
                    LocationClass temp = new LocationClass(
                            responseData.get(i).getLocation().getAddress1(),
                            String.format("Restaurant with a rating of %f", responseData.get(i).getRating()),
                            Double.toString(responseData.get(i).getCoordinates().getLatitude()),
                            Double.toString(responseData.get(i).getCoordinates().getLongitude()),
                            responseData.get(i).getName(),
                            phoneNumber,
                            "-1",
                            responseData.get(i).getUrl()
                            );
                    temp.setDistance(responseData.get(i).getDistance());
                    tempLocations.add(temp);
                }
                RecyclerView recyclerView = findViewById(R.id.restaurantRecyclerView);
                myAdapter = new LocationAdapter(tempLocations);
                recyclerView.setAdapter(myAdapter);
                Log.d("array",String.format("tempLocations size: %d", tempLocations.size()));
            }

            @Override
            public void onFailure(Call<YelpLocationData> call, Throwable t) {
                Log.d("Fail", "query for new restaurants failed");
            }
        });
    }

    private void recyclerView(){

    }
    private void setLongitude(double longitudeValue){
        this.currentLongitude = longitudeValue;
    }
    private void setLatitude(double latitudeValue){
        this.currentLatitude = latitudeValue;
    }

}
