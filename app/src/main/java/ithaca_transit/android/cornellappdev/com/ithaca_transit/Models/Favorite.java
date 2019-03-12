package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

public class Favorite {
    private Place startPlace;
    private Place endPlace;

    public Favorite(Place startPlace, Place endPlace) {
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public Place getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(Place endPlace) {
        this.endPlace = endPlace;
    }
}
