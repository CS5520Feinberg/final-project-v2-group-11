package com.example.numad23su_gourpv2_11.TourGuide.YelpAPIAccess;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YelpLocationData {

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

}