package ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;


import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;

public class Repository {
    private static final Repository ourInstance = new Repository();

    private Context context;

    private List<Route> routesList;
    private Repository() {
    }

    public List<Route> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Route getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(Route selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

}
