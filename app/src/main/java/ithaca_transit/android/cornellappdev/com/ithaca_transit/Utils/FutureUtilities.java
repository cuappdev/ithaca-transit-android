package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Bus;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Coordinate;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.LocationObject;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    public static Route[] getRoute(Endpoint.Config config, Place start, Place end){
        Map<String, String> mapString = new HashMap<String, String>();
        mapString.put("start", start.getLatitude() + "," + start.getLongitude());
        mapString.put("end", end.getLatitude() + "," + end.getLongitude());
        mapString.put("arriveBy", String.valueOf(false));
        mapString.put("destinationName", end.getName());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("\"America/Nassau\""));
        int secondsEpoch = (int) (calendar.getTimeInMillis()/1000L);
        mapString.put("time", String.valueOf(secondsEpoch));

        final Route[][] routes = new Route[1][1];
        Endpoint searchEndpoint = new Endpoint()
                .headers(mapString)
                .path("route")
                .method(Endpoint.Method.GET);

        System.out.print("About to look at routes list");
        FutureNovaRequest.make(Route[].class, searchEndpoint).thenAccept(response -> {
            Route[] routeList = response.getData();

            if(routeList != null){
                routes[0] = routeList;
                System.out.println(routes[0]);
            }
        });
        return routes[0];
    }
}
