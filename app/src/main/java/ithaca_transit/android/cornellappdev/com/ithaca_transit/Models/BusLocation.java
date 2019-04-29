package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public final class BusLocation {

    private String commStatus;

    @SerializedName("case")
    private String dataStatus;

    private String destination;
    private int delay;
    private int deviation;
    private String direction;
    private String displayStatus;
    private int gpsStatus;
    private int heading;
    private String lastStop;
    private Date lastUpdated;
    private Double latitude;
    private Double longitude;
    private String name;
    private String opStatus;
    private String routeID;
    private int runID;
    private int speed;
    private String tripID;
    private int vehicleID;

    public String getCommStatus() {
        return commStatus;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public String getDestination() {
        return destination;
    }

    public int getDelay() {
        return delay;
    }

    public int getDeviation() {
        return deviation;
    }

    public String getDirection() {
        return direction;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public int getHeading() {
        return heading;
    }

    public String getLastStop() {
        return lastStop;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getOpStatus() {
        return opStatus;
    }

    public String getRouteID() {
        return routeID;
    }

    public int getRunID() {
        return runID;
    }

    public int getSpeed() {
        return speed;
    }

    public String getTripID() {
        return tripID;
    }

    public int getVehicleID() {
        return vehicleID;
    }
}

