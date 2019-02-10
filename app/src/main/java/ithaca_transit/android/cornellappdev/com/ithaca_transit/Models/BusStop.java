package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class BusStop {
    private String stopName;
    private Double latitude;
    private Double longitude;

    public BusStop() {

    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static BusStop fromJSON(JSONObject eatery)
            throws JSONException {
        BusStop model = new BusStop();
        model.parseJSONObject(eatery);
        return model;
    }

    private void parseJSONObject(JSONObject busStop) {
        try {
            stopName = busStop.getString("name");
            latitude = Double.parseDouble(busStop.getString("lat"));
            longitude = Double.parseDouble(busStop.getString("long"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
