package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import org.json.JSONException;
import org.json.JSONObject;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

public class BusStop extends LocationObject{

    public static BusStop fromJSON(JSONObject busStopJSON)
            throws JSONException {
        BusStop model = new BusStop();
        model.parseJSONObject(busStopJSON);
        return model;
    }

    private void parseJSONObject(JSONObject busStop) {
        try {
            super.setName(busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_name)));
            super.setLatitude(Double.parseDouble(busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude))));
            super.setLongitude(Double.parseDouble(busStop.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude))));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
