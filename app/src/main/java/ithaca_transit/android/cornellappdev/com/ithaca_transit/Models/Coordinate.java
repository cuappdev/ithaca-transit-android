package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.gson.annotations.SerializedName;

public class Coordinate {

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("long")
    private Double longitude;

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
