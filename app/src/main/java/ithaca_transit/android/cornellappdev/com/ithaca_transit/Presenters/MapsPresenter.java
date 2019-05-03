package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.location.Location;

import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.PatternItem;

import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;


public final class MapsPresenter {

    public static final PatternItem DOT = new Dot();
    public static final List<PatternItem> PATTERN_DOT_LIST = Arrays.asList(DOT);

    private SectionedRecyclerViewAdapter routeOptionsListAdapter;

//    private Endpoint.Config mConfig;
//    private Context mContext;
//    public RecyclerView mRecView;
//    public GoogleMap mMap;


    //    public MapFragment mMapFragment;
    public Repository mRepo = Repository.getInstance();

    public MapsPresenter() {
        super();
//        mMap = map;
//        mContext = context;
//        mConfig = config;
//        mMapFragment = mapFragment;
    }


    public void drawRoutes(Route route, SectionedRoutes routeList) {
        Repository.getInstance().setSelectedRoute(route);
        Repository.getInstance().setRoutesList(routeList);
     }

//    public void changeRoutes(String start, String end, String name) {
//        mMapFragment.launchRoute(start, end, name);
//    }

    public void setLastLocation(Location loc) {
        mRepo.setLastLocation(loc);

    }

    public Location getLastLocation() {
        return mRepo.getLastLocation();
    }
}
