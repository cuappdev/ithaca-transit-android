package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import java.util.Date;

public class Direction {

    private static final Date BLANK_TIME = new Date();

    private int delay;
    private Double distance;
    private Coordinate endLocation;
    private Date endTime;
    private String name;
    private Coordinate[] path;
    private int routeNumber;
    private Coordinate startLocation;
    private Date startTime;
    private Boolean stayOnBusForTransfer;
    private Place[] stops;
    private String[] tripIdentifiers;
    private String type;

    public Direction(int delay, Double distance,
            Coordinate endLocation, Date endTime, String name,
            Coordinate[] path, int routeNumber,
            Coordinate startLocation, Date startTime, Boolean stayOnBusForTransfer,
            Place[] stops, String[] tripIdentifiers, String type) {
        this.delay = delay;
        this.distance = distance;
        this.endLocation = endLocation;
        this.endTime = endTime;
        this.name = name;
        this.path = path;
        this.routeNumber = routeNumber;
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.stayOnBusForTransfer = stayOnBusForTransfer;
        this.stops = stops;
        this.tripIdentifiers = tripIdentifiers;
        this.type = type;
    }

    public Direction(Double distance, String name, String type) {
        this.distance = distance;
        this.name = name;
        this.type = type;
    }

    public String locationDescription() {
        if (type != null) {
            switch (type) {
                case "walk":
                    return "Walk to " + this.name;
                case "arrive":
                    return "Get off at " + this.name;
                case "depart":
                    return "at " + this.name;
                case "transfer":
                    return "at " + this.name + ". Stay on bus";
            }
        }
        return "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public Place[] getStops() {
        return stops;
    }

    public String[] getTripIdentifiers() {
        return tripIdentifiers;
    }

    public String getName() {
        return name;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /* Delete last element of stops array, as we want to depict the last stop as a direction in detail view
     * For example, if the last stop is Duffield, we want to show "Get off at Duffield" instead of
     * Duffield only showing up as a stop in the detail view
     */
    public void updateStops(){
        int numStops = stops.length;
        Place[] newStops = new Place[numStops-1];

        for(int count=0;count<numStops-1;count++){
            newStops[count] = stops[count];
        }
        stops = newStops;
    }
}

