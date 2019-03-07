package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Enums.DirectionType;

import static java.time.LocalDate.now;

public class Route {

    private String arrivalTime;
    private BoundingBox boundingBox;
    private String departureTime;
    private Direction[] directions;
    private Coordinate endCoords;
    private int numberOfTransfers;
    private Coordinate startCoords;


    // Determine if a route only contains walk directions
    public boolean isWalkOnlyRoute(){
        boolean walkOnlyRoute = true;
        int count = 0;

        while(walkOnlyRoute && count < directions.length){
            if(directions[count].getType().equals("depart")){
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

    public String getDescription(){
        String description = "";

        if (isWalkOnlyRoute()) {
            String append = " , ";
            boolean firstItem = true;
            for(Direction direction: directions){
                if(firstItem){
                    description = direction.getName();
                    firstItem = false;
                }
                description = description + append + direction.getName();
            }
        }
        else {
            description = "Board";
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date now = new Date(System.currentTimeMillis());
            calendar.setTime(now);
            Date currentDate = calendar.getTime();

            try {
                Date departureDate = format.parse(departureTime);

                if (departureDate.getDay() - currentDate.getDay() > 1) {
                    description = description + " on " + currentDate.getMonth() + "/" + departureDate.getDay();
                }
                else if (departureDate.getHours() - currentDate.getHours() > 1) {
                    description = description + " in " + (departureDate.getHours() - currentDate.getHours()) + " hours";
                }
                else{
                    description = description + " in " + (departureDate.getMinutes() - currentDate.getMinutes()) + " minutes";
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return description;
    }

    public String getDuration(){
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

            }
            else {
                arrivalAppend = "AM";
                if (arrivalHour == 0) {
                    arrivalHour = 12;
                }
            }

            if (departureHour > 11) {
                departureAppend = "PM";
                departureHour = departureHour - 12;
            }
            else {
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
                    departureMinutes, departureAppend, arrivalHour, arrivalZero, arrivalMintues, arrivalAppend);

            return duration;
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return "No description available";
    }


}