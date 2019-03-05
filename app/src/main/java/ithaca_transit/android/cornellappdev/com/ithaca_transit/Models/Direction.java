package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Direction {

    private static final LocationObject BLANK_LOCATION = new LocationObject();
    private static final Date BLANK_TIME = new Date();

    private Integer delay;
    private LocationObject endLocation;
    private Date endTime;
    private String name;
    private LatLng[] path;
    private Integer routeNumber;
    private LocationObject startLocation;
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

    public Direction() {
    }

    public Direction(int delay, LocationObject endLocation, Date endTime, String name, LatLng[] path,
                     int routeNumber, LocationObject startLocation, Date startTime, boolean stayOnBusForTransfer,
                     LocationObject[] stops, double travelDistance, String[] tripIdentifiers, DirectionType type) {
        this.delay = delay;
        this.endLocation = endLocation;
        this.endTime = endTime;
        this.name = name;
        this.path = path;
        this.routeNumber = routeNumber;
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.stayOnBusForTransfer = stayOnBusForTransfer;
        this.stops = stops;
        this.travelDistance = travelDistance;
        this.tripIdentifiers = tripIdentifiers;
        this.type = type;
    }

    public Direction(@NotNull String name) {
        this(0, BLANK_LOCATION, BLANK_TIME, name, new LatLng[0], 0, BLANK_LOCATION,
                BLANK_TIME, false, new LocationObject[0], 0.0D,
                new String[0], DirectionType.ARRIVE);
    }

    public enum DirectionType {
        ARRIVE,
        DEPART,
        TRANSFER,
        WALK;
    }

    public static Direction fromJSON(JSONObject directionJSON)
            throws JSONException {
        Direction direction = new Direction();
        direction.parseJSONObject(directionJSON);
        return direction;
    }

    private void parseJSONObject(JSONObject direction) {
        try {
            // Getting arrival time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ");
            startTime = dateFormat.parse(direction.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_arrival_time)));
            endTime = dateFormat.parse(direction.getString(Repository.getInstance().getContext()
                    .getString(R.string.field_departure_time)));

            // Getting start and end coords of route
            JSONObject startCoordsObj = direction.getJSONObject(Repository.getInstance().getContext()
                    .getString(R.string.field_start_coords));
            Double startLat = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude));
            Double startLong = startCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude));
            startLocation = new LocationObject(startLat, startLong);

            JSONObject endCoordsObj = direction.getJSONObject(Repository.getInstance().getContext()
                    .getString(R.string.field_end_coords));
            Double endLat = endCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_latitude));
            Double endLong = endCoordsObj.getDouble(Repository.getInstance().getContext()
                    .getString(R.string.field_longitude));
            endLocation = new LocationObject(endLat, endLong);

            delay = direction.getInt(Repository.getInstance().getContext().getString(R.string.field_delay));
            name = direction.getString(Repository.getInstance().getContext().getString(R.string.field_name));
            routeNumber = direction.getInt(Repository.getInstance().getContext().getString(R.string.field_route_number));
            stayOnBusForTransfer = direction.getBoolean(Repository.getInstance().getContext().getString(R.string.field_stay_on_bus));
            travelDistance = direction.getDouble(Repository.getInstance().getContext().getString(R.string.field_distance));

            String directionType = direction.getString(Repository.getInstance().getContext().getString(R.string.field_type));
            switch (directionType) {
                case "walk":
                    type = DirectionType.WALK;
                case "depart":
                    type = DirectionType.DEPART;
            }

            // tripIdentifiers field only exists when type is depart
            if (type == DirectionType.DEPART) {
                JSONArray arrayType = direction.getJSONArray(Repository.getInstance().getContext()
                        .getString(R.string.field_trip_identifiers));

                for (int i = 0; i < arrayType.length(); i++) {
                    String id = arrayType.getJSONObject(i).toString();
                    tripIdentifiers[i] = id;
                }
            }

            JSONArray arrayPath = direction.getJSONArray(Repository.getInstance().getContext()
                    .getString(R.string.field_path));
            for (int i = 0; i < arrayPath.length(); i++) {
                JSONObject object = arrayPath.getJSONObject(i);
                Double pathLatitude = object.getDouble(Repository.getInstance().getContext()
                        .getString(R.string.field_latitude));
                Double pathLongitude = object.getDouble(Repository.getInstance().getContext()
                        .getString(R.string.field_longitude));
                path[i] = new LatLng(pathLatitude, pathLongitude);
            }

            JSONArray arrayStops = direction.getJSONArray(Repository.getInstance().getContext()
                    .getString(R.string.field_path));
            for (int i = 0; i < arrayPath.length(); i++) {
                JSONObject object = arrayStops.getJSONObject(i);

                String name = object.getString(Repository.getInstance().getContext()
                        .getString(R.string.field_name));
                String id = object.getString(Repository.getInstance().getContext()
                        .getString(R.string.field_id));
                stops[i] = new LocationObject(name, id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}


