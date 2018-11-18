package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.security.AccessController.getContext

enum class PathType {
    driving, walking
}

open class Path(waypoints: Array<Waypoint>) {
    private var waypoints: Array<Waypoint> ?= null;
    private var traveledPolyline: PolylineOptions ?= null;
    var color: Int ?= null;

    init {
        this.waypoints = waypoints;
        this.traveledPolyline = PolylineOptions();
        this.color = Color.rgb(0,0,0);
    }
}

class BusPath(waypoints: Array<Waypoint>) : Path(waypoints) {

    private var dashLengths : Array<Int> = arrayOf(6, 4);
    private var dashColors : Array<Int> = emptyArray();
    private var polylineWidth : Float = 0.0F;
    private var traveledPath: Array<LatLng> ?= null;
    private var untraveledPath: Array<LatLng> ?= null;

    init{
        //this.color = ContextCompat.getColor(R.colors.blue);
        this.dashColors = arrayOf(color!!, Color.rgb(0,0,0))
        this.polylineWidth = 0.0F;
    }


}