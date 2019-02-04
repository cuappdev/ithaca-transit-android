package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Path {
    private final int BLANK_COLOR;
    private final int DASH_LENGTHS_X;
    private final int DASH_LENGTHS_Y;
    private final float LINE_WIDTH;
    @Nullable
    private Waypoint[] waypoints;
    @Nullable
    private PolylineOptions traveledPolyline;
    private int color;

    public final int getBLANK_COLOR() {
        return this.BLANK_COLOR;
    }

    public final int getDASH_LENGTHS_X() {
        return this.DASH_LENGTHS_X;
    }

    public final int getDASH_LENGTHS_Y() {
        return this.DASH_LENGTHS_Y;
    }

    public final float getLINE_WIDTH() {
        return this.LINE_WIDTH;
    }

    @Nullable
    public final Waypoint[] getWaypoints() {
        return this.waypoints;
    }

    public final void setWaypoints(@Nullable Waypoint[] var1) {
        this.waypoints = var1;
    }

    @Nullable
    public final PolylineOptions getTraveledPolyline() {
        return this.traveledPolyline;
    }

    public final void setTraveledPolyline(@Nullable PolylineOptions var1) {
        this.traveledPolyline = var1;
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int var1) {
        this.color = var1;
    }

    public Path(@NotNull Waypoint[] waypoints) {
        super();
        this.BLANK_COLOR = Color.rgb(0, 0, 0);
        this.DASH_LENGTHS_X = 6;
        this.DASH_LENGTHS_Y = 4;
        this.LINE_WIDTH = 8.0F;
        this.waypoints = waypoints;
        this.traveledPolyline = new PolylineOptions();
        this.color = this.BLANK_COLOR;
    }

    public enum PathType {
        driving,
        walking;
    }
}
