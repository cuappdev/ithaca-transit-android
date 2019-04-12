package ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;

public class Repository {
    private static Repository sRepoInstance = new Repository();

    private Context context;
    private GoogleMap map;
    private Route selectedRoute;
    private SectionedRoutes routesList;

    public static Repository getInstance() {
        return sRepoInstance;
    }

    public SectionedRoutes getRoutesList() {
        return routesList;
    }

    public void setRoutesList(SectionedRoutes routesList) {
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