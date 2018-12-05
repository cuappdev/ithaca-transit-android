package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

enum class PathType {
    driving, walking
}

open class Path(waypoints: Array<Waypoint>) {
    val BLANK_COLOR = Color.rgb(0, 0, 0)
    val DASH_LENGTHS_X = 6
    val DASH_LENGTHS_Y = 4
    val LINE_WIDTH = 8.0F

    var waypoints: Array<Waypoint>? = null
    var traveledPolyline: PolylineOptions? = null
    var color: Int

    init {
        this.waypoints = waypoints
        this.traveledPolyline = PolylineOptions()
        this.color = BLANK_COLOR
    }
}

class BusPath(waypoints: Array<Waypoint>) : Path(waypoints) {

    var dashLengths: Array<Int> = arrayOf(DASH_LENGTHS_X, DASH_LENGTHS_Y)
    private var dashColors: Array<Int> = emptyArray()
    private var polylineWidth: Float?
    private var traveledPath: List<LatLng>? = null
    private var untraveledPath: List<LatLng>? = null

    init {
//        super.color = ContextCompat.getColor(R.colors.blue);
        this.dashColors = arrayOf(color!!, BLANK_COLOR)
        this.polylineWidth = LINE_WIDTH
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