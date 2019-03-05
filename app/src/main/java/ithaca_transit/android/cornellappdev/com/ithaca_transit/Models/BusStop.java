package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

public class BusStop{

    private String name;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("long")
    private double longitude;

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
