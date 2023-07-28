package com.example.numad23su_gourpv2_11.TourGuide;

import java.net.URI;

public class LocationClass {
    private String Name;
    private String Latitude;
    private String Longitude;
    private String Trail_Position;

    private Long Phone;

    private String URLlink;

    public LocationClass() {
        // Default constructor required for calls to DataSnapshot.getValue(Location.class)
    }

    public LocationClass(String Latitude, String Longitude, String Name, Long Phone, String Trail_Position, String url) {
        this.Name = Name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Trail_Position = Trail_Position;
        this.Phone = Phone;
        this.URLlink = url;
    }

    public String getName() {
        return Name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getTrail_Position() {
        return Trail_Position;
    }

    public Long getPhone() {return Phone; }

    public String getURLlink() {return URLlink; }
}
