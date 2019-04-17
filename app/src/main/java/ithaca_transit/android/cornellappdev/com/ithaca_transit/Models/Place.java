package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.gson.annotations.SerializedName;


public class Place {
    private String detail;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("long")
    private Double longitude;
    private String name;
    private String placeID;
    private String type;

     public Place(Double latitude, Double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public Place(String detail, Double latitude, Double longitude, String name, String placeID, String type) {
        this.type = type;
        this.name = name;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeID = placeID;
        this.type = type;
    }


    public Place(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getPlaceID() {
        return placeID;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return (latitude != null) ? getLatitude() + "," + getLongitude() : getPlaceID();
    }
}
