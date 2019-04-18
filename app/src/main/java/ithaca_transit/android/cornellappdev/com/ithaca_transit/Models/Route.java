package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Route {

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
                }
                description = description + append + direction.getName();
            }
        } else {
            description = "Board";
            Calendar calendar = Calendar.getInstance(
                    TimeZone.getTimeZone("America/New_York"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            Date now = Calendar.getInstance(
                    TimeZone.getTimeZone("America/New_York")).getTime();

            calendar.setTime(now);
            Date currentDate = calendar.getTime();

            try {
                Date departureDate = format.parse(departureTime);

                if (departureDate.getDay() != currentDate.getDay()) {
                    description = description + " on " + (departureDate.getMonth() + 1) + "/" +
                            departureDate.getDate();
                } else if (departureDate.getHours() - currentDate.getHours() > 1) {
                    description = description + " in " + (departureDate.getHours()
                            - currentDate.getHours()) + " hours";
                } else {
                    description = description + " in " + (departureDate.getMinutes()
                            - currentDate.getMinutes()) + " minutes";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return description;
    }

    public String getDuration() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            calendar.setTimeZone(TimeZone.getTimeZone("America/NewYork"));
            calendar.setTime(format.parse(arrivalTime));
            int arrivalHour = calendar.get(Calendar.HOUR_OF_DAY);
            int arrivalMintues = calendar.get(Calendar.MINUTE);

            calendar.setTime(format.parse(departureTime));
            int departureHour = calendar.get(Calendar.HOUR_OF_DAY);
            int departureMinutes = calendar.get(Calendar.MINUTE);

            String arrivalAppend;
            String departureAppend;

            if (arrivalHour > 11) {
                arrivalAppend = "PM";
                arrivalHour = arrivalHour - 12;

            } else {
                arrivalAppend = "AM";
                if (arrivalHour == 0) {
                    arrivalHour = 12;
                }
            }

            if (departureHour > 11) {
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

            String duration = String.format("%s:%s%s %s--%s:%s%s %s", departureHour, departureZero,
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
            Direction start = new Direction(directions[0].getDistance(), "walk", "Current Location");
            Direction destination = new Direction(0.0, "walk", directions[0].getName());
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
                    direction.setDistance(directions[count].getDistance());
                    truncatedDirections.add(direction);
                    count = count + 2;
                } else {
                    truncatedDirections.add(directions[count]);
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
    public ArrayList<Direction> getBusInfo(){
        ArrayList<Direction> directionArrayList = new ArrayList<>();

        int count = 0;
        while(count < directions.length){
            if(directions[count].getType().equals("depart")){
                directionArrayList.add(directions[count]);
            }
        }
        return directionArrayList;
    }

}