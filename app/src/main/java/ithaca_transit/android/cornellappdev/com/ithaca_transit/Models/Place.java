package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

public final class Place {
    private String detail;

    @SerializedName("lat")
    private Double latitude;

    @SerializedName("long")
    private Double longitude;
    private String name;
    private String placeID;
    private String type;

    public Place(String detail, Double latitude, Double longitude, String name, String placeID, String type) {

        this.type = type;
        this.name = name;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeID = placeID;
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
}
