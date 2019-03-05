package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import java.util.Date;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Enums.DirectionType;

import org.jetbrains.annotations.NotNull;

public final class Direction {

    private static final LocationObject BLANK_LOCATION = new LocationObject();
    private static final Date BLANK_TIME = new Date();

    private Integer delay;
    private Double distance;
    private Coordinate endLocation;
    private Date endTime;
    private String name;
    private Coordinate[] path;
    private Integer routeNumber;
    private Coordinate startLocation;
    private Date startTime;
    private Boolean stayOnBusForTransfer;
    private LocationObject[] stops;
    private Double travelDistance;
    private String[] tripIdentifiers;
    private DirectionType type;

    @NotNull
    public final String locationDescription() {
        if (type != null) {
            switch (type) {
                case WALK:
                    return "Walk to " + this.name;
                case ARRIVE:
                    return "Get off at " + this.name;
                case DEPART:
                    return "at " + this.name;
                case TRANSFER:
                    return "at " + this.name + ". Stay on bus";
            }
        }
        return "";
    }

    public DirectionType getType() {
        return type;
    }

    public void setType(DirectionType type) {
        this.type = type;
    }

    public Integer getDelay() {
        return delay;
    }

    public Double getDistance() {
        return distance;
    }

    public Coordinate getEndLocation() {
        return endLocation;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Coordinate[] getPath() {
        return path;
    }

    public Integer getRouteNumber() {
        return routeNumber;
    }

    public Coordinate getStartLocation() {
        return startLocation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Boolean getStayOnBusForTransfer() {
        return stayOnBusForTransfer;
    }

    public LocationObject[] getStops() {
        return stops;
    }

    public Double getTravelDistance() {
        return travelDistance;
    }

    public String[] getTripIdentifiers() {
        return tripIdentifiers;
    }

    public String getName() {
        return name;
    }
}

