package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import com.google.android.gms.maps.model.LatLng
import java.util.Date

enum class DirectionType {
    ARRIVE, DEPART, TRANSFER, WALK
}

class Direction(delay: Int, endLocation: LocationObject, endTime: Date,
                name: String, path: Array<LatLng>, routeNumber: Int, startLocation: LocationObject,
                startTime: Date, stayOnBusForTransfer: Boolean, stops: Array<LocationObject>,
                travelDistance: Double, tripIdentifiers: Array<String>, type: DirectionType) {

    private var delay: Int? = null
    private var endLocation: LocationObject? = null
    private var endTime: Date? = null
    private var name: String? = null
    private var path: Array<LatLng>? = null
    private var routeNumber: Int? = null
    private var startLocation: LocationObject? = null
    private var startTime: Date? = null
    private var stayOnBusForTransfer: Boolean? = null
    private var stops: Array<LocationObject>? = null
    private var travelDistance: Double? = null
    private var tripIdentifiers: Array<String>? = null
    private var type: DirectionType? = null


    init {
        this.delay = delay
        this.endLocation = endLocation
        this.endTime = endTime
        this.name = name
        this.path = path
        this.routeNumber = routeNumber
        this.startLocation = startLocation
        this.startTime = startTime
        this.stayOnBusForTransfer = stayOnBusForTransfer
        this.stops = stops
        this.travelDistance = travelDistance
        this.tripIdentifiers = tripIdentifiers
        this.type = type
    }

    //static variables
    companion object {
        val blankLocation = LocationObject()
        val blankTime = Date()
    }

    constructor(name: String) : this(0, blankLocation, blankTime, if (name != null) name else "",
            emptyArray<LatLng>(), 0, blankLocation, blankTime, false,
            emptyArray<LocationObject>(), 0.0, emptyArray<String>(), DirectionType.ARRIVE)

    fun locationDescription(): String {
        var instruct = ""
        when (type) {
            DirectionType.WALK -> instruct = "Walk to " + name
            DirectionType.ARRIVE -> instruct = "Get off at " + name
            DirectionType.DEPART -> instruct = "at " + name
            DirectionType.TRANSFER -> instruct = "at " + name + ". Stay on bus"
        }
        return instruct
    }

}