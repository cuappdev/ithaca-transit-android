package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.android.gms.maps.model.LatLng;

public final class LocationObject {
    public static final double BLANK_COORD = 0.0D;

    private String id;
    private Double latitude;
    private Double longitude;
    private String name;

    public final String getId() {
        return this.id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final Double getLatitude() {
        return this.latitude;
    }

    public final void setLatitude(Double var1) {
        this.latitude = var1;
    }

    public final Double getLongitude() {
        return this.longitude;
    }

    public final void setLongitude(Double var1) {
        this.longitude = var1;
    }

    public final LatLng coordinates() {
        return new LatLng(latitude, longitude);
    }

    public LocationObject(String name, String id, double latitude, double longitude) {
        super();
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationObject() {
        this("", "", BLANK_COORD, BLANK_COORD);
    }

    public LocationObject(String name) {
        this(name, "", BLANK_COORD, BLANK_COORD);
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
}
