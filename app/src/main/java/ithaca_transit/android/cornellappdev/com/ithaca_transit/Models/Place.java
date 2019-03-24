package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.gson.annotations.SerializedName;


public final class Place {
    private String detail;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("long")
    private Double longitude;
    private String name;
    private String placeId;
    private String type;

    public Place(String detail,
            Double latitude,
            Double longitude,
            String name,
            String placeId,
            String type) {
        this.type = type;
        this.name = name;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.type = type;
    }

    public Place(Double latitude, Double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
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
        return placeId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return (latitude != null) ? getLatitude() + "," + getLongitude() : getPlaceID();
    }
}
