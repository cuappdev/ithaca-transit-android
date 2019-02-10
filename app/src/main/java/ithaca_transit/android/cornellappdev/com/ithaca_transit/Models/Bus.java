// BusDataType.java
package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import com.google.android.gms.maps.model.Marker;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public final class Bus {
    private BusDataType dataType;
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
    private int name;
    private String opStatus;
    private String routeID;
    private int runID;
    private int speed;
    private String tripID;
    private int vehicleID;

    public final int routeNumber() {
        return this.routeID != null ? Integer.parseInt(this.routeID) : 0;
    }

    public Bus(@NotNull BusDataType dataType, int delay, @NotNull String destination,
                       int deviation, @NotNull String direction, @NotNull String displayStatus,
                       int gpsStatus, int heading, @NotNull String lastStop, @NotNull Date lastUpdated,
                       double latitude, double longitude, int name, @NotNull String opStatus, @NotNull String routeID,
                       int runID, int speed, @NotNull String tripID, int vehicleID) {

        super();
        this.direction = direction;
        this.dataType = dataType;
        this.destination = destination;
        this.deviation = deviation;
        this.delay = delay;
        this.displayStatus = displayStatus;
        this.gpsStatus = gpsStatus;
        this.heading = heading;
        this.lastStop = lastStop;
        this.lastUpdated = lastUpdated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.opStatus = opStatus;
        this.routeID = routeID;
        this.runID = runID;
        this.speed = speed;
        this.tripID = tripID;
        this.vehicleID = vehicleID;
    }

    public enum BusDataType {
        invalidData,
        noData,
        validData;
    }
}
