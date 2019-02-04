// Path.java
package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import android.graphics.Color;
import com.google.android.gms.maps.model.PolylineOptions;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public final class BusPath extends Path {
    @NotNull
    private Integer[] dashLengths;
    private Integer[] dashColors;
    private Float polylineWidth;
    private List traveledPath;
    private List untraveledPath;


    public BusPath(@NotNull Waypoint[] waypoints) {
        super(waypoints);
        this.dashLengths = new Integer[]{this.getDASH_LENGTHS_X(), this.getDASH_LENGTHS_Y()};
        this.dashColors = new Integer[]{this.getColor(), this.getBLANK_COLOR()};
        this.polylineWidth = this.getLINE_WIDTH();
        this.createPath(waypoints);

        this.untraveledPath = super.getTraveledPolyline().getPoints();
        this.traveledPath = this.untraveledPath;

        super.getTraveledPolyline().color(super.getColor());
        super.getTraveledPolyline().width(polylineWidth);
    }


    public final void createPath(@NotNull Waypoint[] waypoints) {
        Intrinsics.checkParameterIsNotNull(waypoints, "waypoints");

        for(int k = 0; k < waypoints.length; ++k) {
            Waypoint point = waypoints[k];
            getTraveledPolyline().add(point.coordinates());
        }
    }
}
