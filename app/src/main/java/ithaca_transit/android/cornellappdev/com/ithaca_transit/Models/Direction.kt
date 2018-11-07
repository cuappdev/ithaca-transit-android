package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import java.util.*

enum class DirectionType {
    WALK, DEPART, ARRIVE, TRANSFER
}

class Direction(type: DirectionType, name: String, startLocation: LocationObject, endLocation: LocationObject,
                startTime: Date, endTime: Date, routeNumer: Int, stops: Array<LocationObject>,
                stayOnBusForTransfer: Boolean, tripIdentifiers: Array<String>, delay: Int) {

    private var type: DirectionType? = null;
    private var name: String? = null;
    private var startLocation: LocationObject = null;
    private var endLocation: LocationObject? = null;
    private var startTime: Date? = null;
    private var endTime: Date? = null;
    private var routeNumer: Int? = null;
    private var stayOnBusForTransfer: Boolean? = null;
    private var tripIdentifiers: Array<String>? = null;
    private var delay: Int? = null;

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
    }

    //secondary constructor for when no arguments are passed
    val blankLocation = LocationObject();
    val blankTime = Date();

    constructor(name: String) : this(DirectionType.ARRIVE, if (name != null) name else "", blankLocation,
            blankLocation, blankTime, blankTime, 0, false, emptyArray<String>(), null)

}

}