package ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton;

import java.util.List;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;

class Repo {
    private static final Repo ourInstance = new Repo();

    private List<Route> routesList;

    private Repo() {
    }

    public static Repo getInstance() {
        return ourInstance;
    }

    public List<Route> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
    }
}
