package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models

class Place(name: String) {

    var name: String? = null
    val nameKey = "name"

    init {
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Place
        return (name.equals(other.name))
    }


}