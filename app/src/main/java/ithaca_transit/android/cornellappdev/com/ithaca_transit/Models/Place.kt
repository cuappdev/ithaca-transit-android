package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

class Place {

    var name: String? = null
    var detail: String? = null
    var placeID: String? = null

    val nameKey = "name"
    val detailKey = "name"
    val placeIDKey = "name"

    constructor(name:String) {
        this.name = name
    }

    constructor(name:String, detail:String, placeID: String) {
        this.name = name
        this.detail = detail
        this.placeID = placeID
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Place
        return (placeID.equals(other.placeID))
    }

}