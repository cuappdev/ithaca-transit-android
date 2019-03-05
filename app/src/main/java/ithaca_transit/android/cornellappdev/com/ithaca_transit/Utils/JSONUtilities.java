package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Bus;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class JSONUtilities {

    public static List<BusStop> getAllStops(Context mainContext) {
        List<BusStop> busStopsList = new ArrayList<>();

        try {
            JSONObject parentJSON = Networking.getJSON(mainContext.getString(R.string.get_method_allstops));
            JSONArray stops = parentJSON.getJSONArray(mainContext.getString(R.string.field_data));

            for (int i = 0; i < stops.length(); i++) {
                JSONObject obj = stops.getJSONObject(i);
                BusStop busStop;
                busStop = BusStop.fromJSON(obj);
                busStopsList.add(busStop);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return busStopsList;
    }



    public static int getDelay(int stopID, int tripID, Context mainContext) {
        int delay = 0;
        try {
            String append = String.format(mainContext.getString(R.string.query_delay),
                    stopID, tripID);
            JSONObject parentJSON = Networking.getJSON(mainContext.getString(R.string.field_delay));
            delay = parentJSON.getInt((mainContext.getString(R.string.field_data)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return delay;
    }
}
