package ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton;

import android.content.Context;

import java.util.List;


import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;

public class Repository {
    private static final Repository ourInstance = new Repository();

    private Context context;

<<<<<<< HEAD
    private List<Route> routesList;
=======
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
}
