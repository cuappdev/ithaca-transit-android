package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

public class Route {

    private Date arrivalTime;
    private Date departureTime;
    private Direction[] directions;
    private LocationObject endLocation;

    // Used to center the camera on the selected route
    private Double maxLatBound;
    private Double maxLongBound;
    private Double minLatBound;
    private Double minLongBound;

    private int numTransfers;

    // To be drawn on the map, is an ordered list of paths from start to end
    private LatLng[] pathsList;
    private LocationObject startLocation;


    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public void setDirections(Direction[] directions) {
        this.directions = directions;
    }

    public Double getMaxLatBound() {
        return maxLatBound;
    }

    public void setMaxLatBound(Double maxLatBound) {
        this.maxLatBound = maxLatBound;
    }

    public Double getMaxLongBound() {
        return maxLongBound;
    }

    public void setMaxLongBound(Double maxLongBound) {
        this.maxLongBound = maxLongBound;
    }

    public Double getMinLatBound() {
        return minLatBound;
    }

    public void setMinLatBound(Double minLatBound) {
        this.minLatBound = minLatBound;
    }

    public Double getMinLongBound() {
        return minLongBound;
    }

    public void setMinLongBound(Double minLongBound) {
        this.minLongBound = minLongBound;
    }

    public int getNumTransfers() {
        return numTransfers;
    }

    public void setNumTransfers(int numTransfers) {
        this.numTransfers = numTransfers;
    }

    public LocationObject getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LocationObject endLocation) {
        this.endLocation = endLocation;
    }

    public LocationObject getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LocationObject startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng[] getPathsList() {
        return pathsList;
    }

    public void setPathsList(LatLng[] pathsList) {
        this.pathsList = pathsList;
    }


    public static Route fromJSON(JSONObject routeJSON)
            throws JSONException {
        Route route = new Route();
        route.parseJSONObject(routeJSON);
        return route;
    }

    private void parseJSONObject(JSONObject route) {
        try {
            // Getting arrival time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ");
            arrivalTime = dateFormat.parse(route.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_arrival_time)));
            departureTime = dateFormat.parse(route.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_departure_time)));

            // Getting start and end coords of route
            JSONObject startCoordsObj = route.getJSONObject(Repository.getInstance().getContext()
                    .getString(R.string.field_start_coords));
            Double startLat = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude));
            Double startLong = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude));
            startLocation = new LocationObject(startLat, startLong);

            JSONObject endCoordsObj = route.getJSONObject(Repository.getInstance().getContext()
                    .getString(R.string.field_end_coords));
            Double endLat = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude));
            Double endLong = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude));
            endLocation = new LocationObject(endLat, endLong);

            // Getting bounds for camera
            JSONArray boundsArray = route.getJSONArray(Repository.getInstance().getContext()
                    .getString(R.string.field_bounding_box));
            maxLatBound = boundsArray.getDouble(0);
            maxLongBound = boundsArray.getDouble(1);
            minLatBound = boundsArray.getDouble(2);
            minLongBound = boundsArray.getDouble(3);

            // Getting number of transfers
            numTransfers = route.getInt(Repository.getInstance().getContext()
                    .getString(R.string.field_number_of_transfers));

            // Getting directions
            JSONArray arrayDirections = route.getJSONArray(Repository.getInstance().getContext()
                    .getString(R.string.field_directions));
            for (int i = 0; i < arrayDirections.length(); i++) {
                JSONObject obj = arrayDirections.getJSONObject(i);
                Direction direction;
                direction = Direction.fromJSON(obj);
                directions[i] = direction;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}