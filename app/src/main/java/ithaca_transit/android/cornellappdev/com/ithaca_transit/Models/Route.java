package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class Route {

    private Date arrivalTime;
    private Date departureTime;
    private Direction[] directions;

    // Used to center the camera on the selected route
    private Double maxLatBound;
    private Double maxLongBound;
    private Double minLatBound;
    private Double minLongBound;

    private int numTransfers;

    private LocationObject endLocation;
    private LocationObject startLocation;

    // To be drawn on the map, is an ordered list of paths from start to end
    private LatLng[] pathsList;


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

    private void parseJSONObject(JSONObject routeJSON) {
        try {
            arrivalTime = routeJSON.get("arrivalTime");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
