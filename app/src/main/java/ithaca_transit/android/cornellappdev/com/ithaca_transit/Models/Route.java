package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Route implements Serializable {

    private String arrivalTime;
    private BoundingBox boundingBox;
    private String departureTime;
    private Direction[] directions;
    private Coordinate endCoords;
    private int numberOfTransfers;
    private Coordinate startCoords;

    // Determine if a route only contains walk directions
    public boolean isWalkOnlyRoute() {
        boolean walkOnlyRoute = true;
        int count = 0;

        while (walkOnlyRoute && count < directions.length) {
            if (directions[count].getType().equals("depart")) {
                walkOnlyRoute = false;
            }
            count++;
        }
        return walkOnlyRoute;
    }


    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public Coordinate getStartCoords() {
        return startCoords;
    }

    public Coordinate getEndCoords() {
        return endCoords;
    }

    public String getDescription() {
        String description = "";

        if (isWalkOnlyRoute()) {
            String append = " , ";
            boolean firstItem = true;
            for (Direction direction : directions) {
                if (firstItem) {
                    description = direction.getName();
                    firstItem = false;
                } else {
                    description = "via " + description + append + direction.getName();
                }
            }
        } else {
            description = "Board";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("America/NewYork"));
            Date currentDate = new Date();

            try {
                Date departureDate = format.parse(departureTime);

                if (departureDate.getDay() != currentDate.getDay()) {
                    description = description + " on " + (departureDate.getMonth() + 1) + "/" +
                            departureDate.getDate();
                }
                else if (departureDate.getTime() < currentDate.getTime()){
                    description = "Board now";
                }
                else if (departureDate.getHours() - currentDate.getHours() > 1) {
                    description = description + " in " + (departureDate.getHours()
                            - currentDate.getHours()) + " hours";
                } else {
                    int diff =  (departureDate.getMinutes()
                            - currentDate.getMinutes());

                    // When departure's minutes are less, but still ahead of current time
                    if (diff < 0){
                        diff = 60 + diff;
                    }

                    if(diff > 1){
                        description = description + " in " + diff + " minutes";
                    }
                    else if(diff == 1){
                        description = description + " in " + diff + " minute";
                    }
                    else{
                        description = description + " now";
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return description;
    }

    public String getBusArrival() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(directions[0].getStartTime());
    }

    public String getDuration() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/NewYork"));
        try {
            Date arrivalDate = (simpleDateFormat.parse(arrivalTime));
            int arrivalHour = arrivalDate.getHours();
            int arrivalMintues = arrivalDate.getMinutes();

            Date departureDate = (simpleDateFormat.parse(departureTime));
            int departureHour = departureDate.getHours();
            int departureMinutes = departureDate.getMinutes();

            String arrivalAppend;
            String departureAppend;

            if (arrivalHour > 12) {
                arrivalAppend = "PM";
                arrivalHour = arrivalHour - 12;

            } else {
                arrivalAppend = "AM";
                if (arrivalHour == 0) {
                    arrivalHour = 12;
                }
            }

            if (departureHour > 12) {
                departureAppend = "PM";
                departureHour = departureHour - 12;
            } else {
                departureAppend = "AM";
                if (departureHour == 0) {
                    departureHour = 12;
                }
            }

            String departureZero = "";
            if (departureMinutes < 10) {
                departureZero = "0";
            }

            String arrivalZero = "";
            if (arrivalMintues < 10) {
                arrivalZero = "0";
            }

            String duration = String.format("%s:%s%s %s - %s:%s%s %s", departureHour, departureZero,
                    departureMinutes, departureAppend, arrivalHour, arrivalZero, arrivalMintues,
                    arrivalAppend);

            return duration;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "No description available";
    }

    // Used to display information about directions on route card
    public ArrayList<Direction> getTruncatedDirections() {
        ArrayList<Direction> truncatedDirections = new ArrayList<Direction>();

        if (directions.length == 1 && directions[0].getType().equals("walk")) {
            // Add Current Location
            Direction start = new Direction(directions[0].getDistance(), "Current Location",
                    "walk");
            Direction destination = new Direction(0.0, directions[0].getName(), "walk");
            truncatedDirections.add(start);
            truncatedDirections.add(destination);
        } else {
            int count = 0;
            while (count < directions.length) {
                if (count < directions.length - 1 && (directions[count].getName().equals(
                        directions[count + 1].getName())
                        && directions[count].getType().equals("walk")
                        && directions[count + 1].getType().equals("depart"))) {

                    // want to show distance walking to departure stop
                    Direction direction = directions[count + 1];
                    direction.setDistance(directions[count].getDistance().doubleValue());
                    truncatedDirections.add(direction);

                    int lastStopIdx = directions[count + 1].getStops().length - 1;
                    String lastStop = directions[count + 1].getStops()[lastStopIdx].getName();

                    String type = "";
                    if (count < directions.length - 2) {
                        type = type + directions[count + 2].getType();
                    } else {
                        type = type + "arrive";
                    }
                    Direction endBusPath = new Direction(0.0, lastStop, type,
                            directions[count].getRouteNumber());
                    truncatedDirections.add(endBusPath);

                    count = count + 2;
                } else {
                    truncatedDirections.add(directions[count]);
                    // If we have a transfer
                    if (directions[count].getType().equals("depart")
                            && count < directions.length - 2
                            && directions[count + 1].getType().equals("depart")) {
                        int lastStopIdx = directions[count].getStops().length - 1;
                        String lastStop = directions[count].getStops()[lastStopIdx].getName();
                        Direction endBusPath = new Direction(0.0, lastStop, "arrive",
                                directions[count].getRouteNumber());
                        truncatedDirections.add(endBusPath);
                    }
                    count++;
                }
            }
        }
        return truncatedDirections;
    }

    public int getTotalDelay() {
        int delay = 0;
        for (Direction direction : directions) {
            if (direction.getType().equals("depart")) {
                delay = direction.getDelay() + delay;
            }
        }
        return delay;
    }

    // Determines how many unique buses we need to track
    // Returns the first direction associated with the unique bus
    public ArrayList<Direction> getBusInfo() {
        ArrayList<Direction> directionArrayList = new ArrayList<>();

        int count = 0;
        while (count < directions.length) {
            if (directions[count].getType().equals("depart")) {
                directionArrayList.add(directions[count]);
            }
        }
        return directionArrayList;
    }

    // Check here for errors
    public ArrayList<Direction> getDetailDirections() {
        ArrayList<Direction> detailDirections = new ArrayList<Direction>();
        int count = 0;
        while (count < directions.length) {
            Direction direction = directions[count];
            if(directions.length == 1 && directions[count].getType().equals("walk")){

            }
            else if (direction.getType().equals("depart")) {
                Place[] stops = direction.getStops();
                int numStops = stops.length;
                Place lastStop = stops[numStops - 1];

                // Adding intial direction, removing last stop
                direction.updateStops();
                detailDirections.add(direction);

                // Adding last stop as its own direction
                Direction arriveDirection = new Direction(0.0, lastStop.getName(), "arrive");
                detailDirections.add(arriveDirection);
            }
            else{
                detailDirections.add(directions[count]);
            }
            count++;
        }
        return detailDirections;
    }
}