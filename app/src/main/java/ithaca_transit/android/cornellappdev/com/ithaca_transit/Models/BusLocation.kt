package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.widget.ImageView
import com.google.android.gms.maps.model.Marker
import java.util.*

enum class BusDataType {
    noData, validData, invalidData
}

class BusLocation(dataType: BusDataType, destination: String, deviation: Int, delay: Int,
                  direction: String, displayStatus: String, gpsStatus: Int, heading: Int,
                  lastStop: String, lastUpdated: Date, latitude: Double, longitude: Double,
                  name: Int, opStatus: String, routeID: String, runID: Int, speed: Int, tripID: String,
                  vehicleID: Int) {

    private var dataType: BusDataType? = null
    private var destination: String? = null
    private var deviation: Int? = null
    private var delay: Int? = null
    private var displayStatus: String? = null
    private var gpsStatus: Int? = null
    private var heading: Int? = null
    private var lastStop: String? = null
    private var lastUpdated: Date? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: Int? = null
    private var opStatus: String? = null
    private var routeID: String? = null
    private var runID: Int? = null
    private var speed: Int? = null
    private var tripID: String? = null
    private var vehicleID: Int? = null

    private var icon: Marker? = null

    init {
        this.dataType = dataType
        this.destination = destination
        this.deviation = deviation
        this.delay = delay
        this.displayStatus = displayStatus
        this.gpsStatus = gpsStatus
        this.heading = heading
        this.lastStop = lastStop
        this.lastUpdated = lastUpdated
        this.latitude = latitude
        this.longitude = longitude
        this.name = name
        this.opStatus = opStatus
        this.routeID = routeID
        this.runID = runID
        this.speed = speed
        this.tripID = tripID
        this.vehicleID = vehicleID
    }

    fun routeNumer(): Int {
        return if (routeID != null) Integer.parseInt(routeID) else 0

    }
}