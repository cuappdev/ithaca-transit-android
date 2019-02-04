package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLng;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public final class Waypoint {
    private final float LARGE_DIAMETER = 12.0F;
    private final float SMALL_DIAMETER = 12.0F;
    private int busNumber;
    private ImageView icon;
    private double latitude;
    private double longitude;
    private Waypoint.WaypointType wpType;

    @NotNull
    public final LatLng coordinates() {
        return new LatLng(this.latitude, this.longitude);
    }

    public Waypoint(double longitude, double latitude, Waypoint.WaypointType wpType, int busNumber, boolean isStop) {
        super();

        this.wpType = Waypoint.WaypointType.origin;
        this.busNumber = busNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wpType = wpType;
    }

    public static enum WaypointType {
        origin,
        destination,
        stop,
        bus,
        walk,
        walking,
        bussing,
        none;
    }
}
