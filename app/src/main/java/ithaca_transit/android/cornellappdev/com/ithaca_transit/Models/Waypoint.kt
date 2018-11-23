package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.widget.ImageView
import com.google.android.gms.maps.model.LatLng

class Waypoint(longitude: Double, latitude: Double, wpType: WaypointType, busNumber:Int,
               isStop:Boolean = false){

    enum class WaypointType{
        origin, destination, stop, bus, walk, walking, bussing, none
    }

    val smallDiameter = 12F
    val largeDiameter = 12F

    private var latitude = 0.0
    private var longitude = 0.0

    private var wpType = WaypointType.origin
    private var busNumber = 0
    private var icon:ImageView ?= null

    init {
        this.latitude = latitude
        this.longitude = longitude
        this.wpType = wpType
        this.busNumber = busNumber
    }

    fun coordinates(): LatLng {
        return LatLng(latitude, longitude)
    }


}