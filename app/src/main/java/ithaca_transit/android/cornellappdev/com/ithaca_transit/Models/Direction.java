// Direction$WhenMappings.java
package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;


import com.google.android.gms.maps.model.LatLng;
import java.util.Date;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class Direction {
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
    private static final LocationObject blankLocation = new LocationObject();
    @NotNull
    private static final Date blankTime = new Date();

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

    public Direction(int delay, @NotNull LocationObject endLocation, @NotNull Date endTime, @NotNull String name, @NotNull LatLng[] path, int routeNumber, @NotNull LocationObject startLocation, @NotNull Date startTime, boolean stayOnBusForTransfer, @NotNull LocationObject[] stops, double travelDistance, @NotNull String[] tripIdentifiers, @NotNull DirectionType type) {
        super();
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
        this(0, blankLocation, blankTime, name, new LatLng[0], 0, blankLocation,
                blankTime, false, new LocationObject[0], 0.0D,
                new String[0], DirectionType.ARRIVE);
    }

    public enum DirectionType {
        ARRIVE,
        DEPART,
        TRANSFER,
        WALK;
    }

}


