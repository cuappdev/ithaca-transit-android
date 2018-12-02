package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

import com.google.android.gms.maps.model.LatLng

class LocationObject(name: String, id: String, latitude: Double, longitude: Double){
    var id: String ?= null
    var name: String ?= null
    var latitude: Double ?= null
    var longitude: Double ?= null

    init {
        this.id = id
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(): this("", "", 0.0, 0.0)

    constructor(name: String): this(name, "", 0.0, 0.0)

    constructor(name: String, latitude: Double, longitude: Double):this(name,"",latitude,
            longitude)

    fun coordinates(): LatLng {
        return LatLng(latitude!!, longitude!!)
    }
}