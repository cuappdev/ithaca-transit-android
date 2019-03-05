package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import org.json.JSONException;
import org.json.JSONObject;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

public class BusStop {
    private Double latitude;
    private Double longitude;
    private String stopName;

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

    public static BusStop fromJSON(JSONObject busStopJSON)
            throws JSONException {
        BusStop model = new BusStop();
        model.parseJSONObject(busStopJSON);
        return model;
    }

    private void parseJSONObject(JSONObject busStop) {
        try {
            stopName = busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_name));
            latitude = Double.parseDouble(busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude)));
            longitude = Double.parseDouble(busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude)));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}