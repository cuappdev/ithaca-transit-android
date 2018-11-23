package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R
import java.security.AccessController.getContext

enum class PathType {
    driving, walking
}

open class Path(waypoints: Array<Waypoint>) {
    var waypoints: Array<Waypoint>? = null
    var traveledPolyline: PolylineOptions? = null
    var color: Int

    init {
        this.waypoints = waypoints
        this.traveledPolyline = PolylineOptions()
        this.color = Color.rgb(0, 0, 0)
    }
}

class BusPath(waypoints: Array<Waypoint>) : Path(waypoints) {

    var dashLengths: Array<Int> = arrayOf(6, 4)
    private var dashColors: Array<Int> = emptyArray()
    private var polylineWidth: Float?
    private var traveledPath: List<LatLng>? = null
    private var untraveledPath: List<LatLng>? = null

    init {
        //super.color = ContextCompat.getColor(R.colors.blue);
        this.dashColors = arrayOf(color!!, Color.rgb(0, 0, 0))
        this.polylineWidth = 8.0F
        createPath(waypoints)
        this.untraveledPath = super.traveledPolyline!!.points
        this.traveledPath = untraveledPath
        super.traveledPolyline!!.color(super.color)
        super.traveledPolyline!!.width(polylineWidth!!)
    }

    fun createPath(waypoints: Array<Waypoint>) {
        var count = 0
        for (point in waypoints) {
            this.traveledPolyline!!.add(point.coordinates())
            count = count + 1
        }
    }


}