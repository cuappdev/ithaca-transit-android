package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import android.content.Context;

import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class FutureUtilities {


    public static int getDelay(int stopID, int tripID, Context mainContext) {
        int delay = 0;
        try {
            String append = String.format(mainContext.getString(R.string.query_delay),
                    stopID, tripID);
            JSONObject parentJSON = Networking.getJSON(append);
            delay = parentJSON.getInt((mainContext.getString(R.string.field_data)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return delay;
    }

    public static HashMap<Integer, Route[]> getRoute(Endpoint.Config config, Place start, Place end, HashMap<Integer, Route[]> mAllRoutesToFavorites, int position){
        Map<String, String> mapString = new HashMap<String, String>();
        mapString.put("start", start.getLatitude() + "," + start.getLongitude());
        mapString.put("end", end.getLatitude() + "," + end.getLongitude());
        mapString.put("arriveBy", String.valueOf(false));
        mapString.put("destinationName", end.getName());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("\"America/NewYork\""));
        int secondsEpoch = (int) (calendar.getTimeInMillis()/1000L);
        System.out.println("TIME:   "  + String.valueOf(secondsEpoch));
        mapString.put("time", String.valueOf(secondsEpoch));

        final Route[][] routes = new Route[1][1];
        Endpoint searchEndpoint = new Endpoint()
                .queryItems(mapString)
                .path("route")
                .method(Endpoint.Method.GET);

      //  System.out.print("About to look at routes list");
        FutureNovaRequest.make(Route[].class, searchEndpoint).thenAccept(response -> {
            Route[] routeList = response.getData();

            if(routeList != null){
                mAllRoutesToFavorites.put(position, routeList);
            }
        });
        return mAllRoutesToFavorites;
    }
}
