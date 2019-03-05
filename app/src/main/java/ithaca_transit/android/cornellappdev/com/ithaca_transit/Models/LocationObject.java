package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.android.gms.maps.model.LatLng;

public class LocationObject {
    public static final double BLANK_COORD = 0.0D;

    private Double latitude;
    private Double longitude;
    private String name;

    public LocationObject(String name, String id, double latitude, double longitude) {
        super();

        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationObject() {
        this("", "", BLANK_COORD, BLANK_COORD);
    }

    public LocationObject(String name, String id) {
        this(name, id, BLANK_COORD, BLANK_COORD);
    }

    public LocationObject(Double latitude, Double longitude) {
        this("", "", latitude, longitude);
    }

    public LocationObject(String name, double latitude, double longitude) {
        this(name, "", latitude, longitude);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double var1) {
        this.latitude = var1;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double var1) {
        this.longitude = var1;
    }

    public LatLng coordinates() {
        return new LatLng(latitude, longitude);
    }

}
