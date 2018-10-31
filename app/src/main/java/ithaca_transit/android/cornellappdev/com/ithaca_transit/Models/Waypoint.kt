package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.widget.ImageView
import com.google.android.gms.maps.model.LatLng

class Waypoint(longitude: Double, latitude: Double, wpType: WaypointType, busNumber:Int,
               isStop:Boolean = false){

    enum class WaypointType{
        origin, destination, stop, bus, walk, walking, bussing, none
    }

    val largeDiameter = 12F
    val smallDiameter = 12F


    private var busNumber = 0
    private var icon:ImageView ?= null
    private var latitude = 0.0
    private var longitude = 0.0
    private var wpType = WaypointType.origin


    init {
        this.busNumber = busNumber
        this.latitude = latitude
        this.longitude = longitude
        this.wpType = wpType
    }

    fun coordinates(): LatLng {
        return LatLng(latitude, longitude)
    }


}