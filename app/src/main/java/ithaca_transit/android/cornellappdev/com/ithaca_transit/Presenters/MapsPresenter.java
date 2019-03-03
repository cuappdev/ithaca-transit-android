package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.OptionsFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public final class MapsPresenter implements FavoritesListAdapter.ListAdapterOnClickHandler, GoogleMap.OnPolylineClickListener {
    public static final PatternItem DOT = new Dot();
    public static final List<PatternItem> PATTERN_DOT_LIST = Arrays.asList(DOT);

    // Hardcoded places for favorites
    private static final Place place1 = new Place("To Goldwin Smith - Ithaca Commons");
    private static final Place place2 = new Place("To Duffield - The Johnson Museum");
    private static final Place place3 = new Place("To The Lux - Gates Hall");
    private static final Place[] placeList = new Place[]{place1, place2, place3};

    public RecyclerView mRecView;
    public SearchView mSearchView;
    private FavoritesListAdapter listAdapter;
    private FragmentManager mManager;

    // Maps a polyline to the route it is a component of
    private HashMap<Polyline, Route> polylineMap = new HashMap<Polyline, Route>();;

    // Maps a route to all polylines that represents its path
    private HashMap<Route, List<Polyline>> routeMap = new HashMap<Route, List<Polyline>>();


    public MapsPresenter(@NotNull FragmentManager manager) {
        super();
        this.mManager = manager;
    }

    public final void setDynamicRecyclerView(@NotNull Context context) {
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((LayoutManager) layoutManager);
        listAdapter = new FavoritesListAdapter(context, (FavoritesListAdapter.ListAdapterOnClickHandler) this, placeList);
        mRecView.setAdapter(listAdapter);
        mRecView.setVisibility(View.VISIBLE);
        listAdapter.notifyDataSetChanged();
    }

    public void onClick(int position, @NotNull Place[] list) {
        OptionsFragment optionsFragment = new OptionsFragment();
        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, optionsFragment);
        fragmentTransaction.addToBackStack((String) null);
        fragmentTransaction.commit();
    }

    public void removeAllRoutes() {
        // Remove all lines from map when new route gets selected
        polylineMap.clear();
        Repository.getInstance().getMap().clear();
    }

    // Removes bus route currently displayed on screen
    // Called when a user selects a route to display from route options
    public void removeSelectdRoute() {
        Route previousRoute = Repository.getInstance().getSelectedRoute();
        List<Polyline> polylines = routeMap.get(previousRoute);

        for(Polyline polyline:polylines){
            polyline.remove();
        }
        routeMap.remove(previousRoute);
    }


    public void drawSelectedRoute() {
        Route route = Repository.getInstance().getSelectedRoute();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.clickable(true);

        List<Direction> directionList = Arrays.asList(Repository.getInstance().getSelectedRoute().getDirections());
        ArrayList<Polyline> polylineList = new ArrayList<Polyline>();

        for (Direction direction : directionList) {
            if (direction.getType() == Direction.DirectionType.WALK) {
                //Make all elements into Dots
                polylineOptions.pattern(PATTERN_DOT_LIST);
            }
            else{
                polylineOptions.pattern(null);
            }

            polylineOptions.addAll(Arrays.asList(direction.getPath()));

            // Drawing sub-polyline on map
            Polyline polyline = Repository.getInstance().getMap().addPolyline(polylineOptions);
            polylineList.add(polyline);
            polylineMap.put(polyline, route);
        }
    }

    public void drawWalkRoute() {
        List<Route> routesList = Repository.getInstance().getRoutesList();
        for (Route route : routesList) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.pattern(PATTERN_DOT_LIST);
            polylineOptions.color(Color.GRAY);
            polylineOptions.clickable(true);

            // Walk route is always last element in routes list
            List<Direction> directionList = Arrays.asList(Repository.getInstance().getRoutesList().get(0).getDirections());

            for (Direction direction : directionList) {
                polylineOptions.addAll(Arrays.asList(direction.getPath()));
            }

            // Drawing route on map
            Polyline polyline = Repository.getInstance().getMap().addPolyline(polylineOptions);
            polylineMap.put(polyline, route);
            routeMap.put(route, Collections.singletonList(polyline));
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        // Change color of previously selected route
        Route previousRoute = Repository.getInstance().getSelectedRoute();
        for(Polyline sub_polyline: routeMap.get(previousRoute)){
            sub_polyline.setColor(Color.GRAY);
        }

        // Update value, color of selected route
        Route newRoute = polylineMap.get(polyline);
        Repository.getInstance().setSelectedRoute(newRoute);

        for(Polyline sub_polyline: routeMap.get(newRoute)){
            sub_polyline.setColor(Color.BLUE);
        }
    }
}
