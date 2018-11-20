package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R
import java.util.*

enum class DirectionType {
    WALK, DEPART, ARRIVE, TRANSFER
}

class Direction(type: DirectionType, name: String, startLocation: LocationObject, endLocation: LocationObject,
                startTime: Date, endTime: Date, routeNumer: Int, stops: Array<LocationObject>,
                stayOnBusForTransfer: Boolean, tripIdentifiers: Array<String>, delay: Int, path: Array<LatLng>,
                travelDistance: Double) {

    private var type: DirectionType? = null;
    private var name: String? = null;
    private var startLocation: LocationObject? = null;
    private var endLocation: LocationObject? = null;
    private var startTime: Date? = null;
    private var endTime: Date? = null;
    private var routeNumer: Int? = null;
    private var stayOnBusForTransfer: Boolean? = null;
    private var tripIdentifiers: Array<String>? = null;
    private var delay: Int? = null;
    private var path: Array<LatLng>? = null;
    private var stops: Array<LocationObject>? = null;
    private var travelDistance: Double? = null;

    init {
        this.type = type;
        this.name = name;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.routeNumer = routeNumer;
        this.stayOnBusForTransfer = stayOnBusForTransfer;
        this.tripIdentifiers = tripIdentifiers;
        this.delay = delay;
        this.path = path;
        this.stops = stops;
        this.travelDistance = travelDistance;
    }

    //static variables
    companion object {
        val blankLocation = LocationObject();
        val blankTime = Date();
    }

    constructor(name: String) : this(DirectionType.ARRIVE, if (name != null) name else "", blankLocation,
            blankLocation, blankTime, blankTime, 0, emptyArray<LocationObject>(), false, emptyArray<String>(),
            0, emptyArray<LatLng>(), 0.0)

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