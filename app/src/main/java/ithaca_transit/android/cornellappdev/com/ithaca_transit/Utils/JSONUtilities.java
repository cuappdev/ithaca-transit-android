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

public class JSONUtilities {

    public static List<BusStop> getAllStops(Context mainContext) {
        List<BusStop> busStopsList = new ArrayList<>();

        try {
            JSONObject parentJSON = Networking.getJSON("allstops");
            JSONArray stops = parentJSON.getJSONArray("data");

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


    public static List<Route> getRoutes(LatLng coords, LatLng end, int time, Context mainContext) {
        List<Route> routesList = new ArrayList<>();

        try {
            JSONObject parentJSON = Networking.getJSON("route");
            JSONArray stops = parentJSON.getJSONArray("data");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routesList;
    }
}
