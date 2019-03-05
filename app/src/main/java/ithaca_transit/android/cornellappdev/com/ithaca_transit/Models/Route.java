package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Enums.DirectionType;

import static java.time.LocalDate.now;

public class Route {

    private Date arrivalTime;
    private BoundingBox boundingBox;
    private Date departureTime;
    private Direction[] directions;
    private Coordinate endCoords;
    private int numberOfTransfers;
    private Coordinate startCoords;

    public Direction[] getDirections() {
        return directions;
    }

    // Determine if a route only contains walk directions
    public boolean isWalkOnlyRoute(){
        boolean walkOnlyRoute = true;
        int count = 0;

        while(walkOnlyRoute){
            if(directions[count].getType().equals(DirectionType.DEPART)){
                walkOnlyRoute = false;
            }
            count++;
        }
        return walkOnlyRoute;
    }

    public String getDescription(){
        String description = "";

        if(isWalkOnlyRoute()){
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
        else{
            description = "Board";
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ssz");
            Date now = new Date(System.currentTimeMillis());
            calendar.setTime(now);
            Date currentDate = calendar.getTime();

            if(departureTime.getDay() - currentDate.getDay() > 1){
                description = description + " on " + currentDate.getMonth() + "/" + departureTime.getDay();
            }
            else if(departureTime.getHours() - currentDate.getHours() > 1){
                description = description + " in " + (currentDate.getHours() - departureTime.getHours());
            }
            else{
                description = description + " in " + (departureTime.getMinutes() - currentDate.getMinutes());
            }
        }

        return description;
    }

    public String getDuration(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arrivalTime);
        int arrivalHour = calendar.get(Calendar.HOUR_OF_DAY);
        int arrivalMintues = calendar.get(Calendar.MINUTE);

        calendar.setTime(departureTime);
        int departureHour = calendar.get(Calendar.HOUR_OF_DAY);
        int departureMinutes = calendar.get(Calendar.MINUTE);

        String arrivalAppend;
        String departureAppend;

        if(arrivalHour > 11){
            arrivalAppend = "PM";
        }
        else{
            arrivalAppend = "AM";
        }

        if(departureHour > 11){
            departureAppend = "PM";
        }
        else{
            departureAppend = "AM";
        }

        return arrivalHour + ":" + arrivalMintues + arrivalAppend + "-" +
                departureHour + departureAppend +":" + departureMinutes;
    }
}