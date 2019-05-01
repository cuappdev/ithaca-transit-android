package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.SearchView;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.ExtendedFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.MapFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.MapsActivity;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Coordinate;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
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



    //TODO: implement
//    public void onFavoriteClick(int position, @NotNull ArrayList<Favorite> list) {
//        drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
//                favoriteListAdapter.getmAllRoutesToFavorites().get(position));
//    }

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



//    public void makeExtendedOptionsFragment() {
//
////        ExtendedFragment optionsFragment = new ExtendedFragment();
////        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
////        fragmentTransaction.add(R.id.container, optionsFragment);
////        fragmentTransaction.addToBackStack((String) null);
////        fragmentTransaction.commit();
////        mManager.executePendingTransactions();
//
//        // Setting up Extended Fragment recycler view
//        RecyclerView recyclerView = ((MapsActivity) mContext).findViewById(
//                R.id.nearby_stops_routes);
//        routeOptionsListAdapter = new SectionedRecyclerViewAdapter();
//
//        Route optimalAsList[] = new Route[1];
//        optimalAsList[0] = Repository.getInstance().getRoutesList().getOptRoute();
//        // creating routes list adapter for each section
//        SectionAdapter optimalSection = new SectionAdapter(mContext, this,
//                optimalAsList, "optimal", null);
//        SectionAdapter fromStopsSection = new SectionAdapter(mContext, this,
//                Repository.getInstance().getRoutesList().getFromStop(), "fromStops", "From Stops");
//        SectionAdapter boardingSoonSection = new SectionAdapter(mContext, this,
//                Repository.getInstance().getRoutesList().getBoardingSoon(), "boardingSoon",
//                "Boarding Soon");
//        SectionAdapter walkingSection = new SectionAdapter(mContext, this,
//                Repository.getInstance().getRoutesList().getWalking(), "walking", "By Walking");
//
//        // Adding sections -- check if some of them are null before adding (optimal will never be null)
//        routeOptionsListAdapter.addSection(optimalSection);
//        if (Repository.getInstance().getRoutesList().getFromStop() != null &&
//                Repository.getInstance().getRoutesList().getFromStop().length != 0) {
//            routeOptionsListAdapter.addSection(fromStopsSection);
//        }
//        if (Repository.getInstance().getRoutesList().getBoardingSoon() != null &&
//                Repository.getInstance().getRoutesList().getBoardingSoon().length != 0) {
//            routeOptionsListAdapter.addSection(boardingSoonSection);
//        }
//        // If only route is walk route, set optimal to walk and leave walk section empty
//        if (optimalAsList[0] !=
//                Repository.getInstance().getRoutesList().getWalking()[0]) {
//            routeOptionsListAdapter.addSection(walkingSection);
//        }
//
//        recyclerView.setLayoutManager((new LinearLayoutManager(mContext, 1, false)));
//
//        recyclerView.setAdapter(routeOptionsListAdapter);
////
////        slideView = ((MapsActivity) mContext).findViewById(R.id.slide_panel);
//
//    }

}
