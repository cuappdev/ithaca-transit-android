package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.ExtendedFragment;
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


public final class MapsPresenter implements FavoritesListAdapter.TextAdapterOnClickHandler,
        GoogleMap.OnPolylineClickListener, SectionAdapter.ListAdapterOnClickHandler {

    public static final PatternItem DOT = new Dot();
    public static final List<PatternItem> PATTERN_DOT_LIST = Arrays.asList(DOT);

    // Hardcoded data for favorites
    private Place goldwin = new Place(42.4491, -76.4835, "Goldwin");
    private Place duffield = new Place(42.4446, -76.4823, "Duffield");
    private Place dickson = new Place(42.4547, -76.4794, "Clara Dickson");
    private Favorite favorite1 = new Favorite(goldwin, duffield);
    private Favorite favorite2 = new Favorite(dickson, duffield);
    private Favorite favorite3 = new Favorite(duffield, dickson);
    private static ArrayList<Favorite> favoriteList = new ArrayList<Favorite>();

    private FavoritesListAdapter favoriteListAdapter;
    private SectionedRecyclerViewAdapter routeOptionsListAdapter;

    private Endpoint.Config mConfig;
    private Context mContext;
    private FragmentManager mManager;
    private GoogleMap mMap;
    public RecyclerView mRecView;
    private View slideView;
    public SearchView mSearchView;
    private BusStop[] mStopsList;

    private ArrayList<Polyline> mPathLastSelected;
    private ArrayList<Polyline> mBorderLastSelected;

    // Maps a polyline to its parent route
    private HashMap<ArrayList<Polyline>, Route>
            polylineMap = new HashMap<ArrayList<Polyline>, Route>();

    // Maps a route to all polylines that represent its path
    private HashMap<Route, List<ArrayList<Polyline>>>
            routeMap = new HashMap<Route, List<ArrayList<Polyline>>>();

    // Maps a polyline path to its border
    private HashMap<ArrayList<Polyline>, ArrayList<Polyline>>
            borderMap = new HashMap<ArrayList<Polyline>, ArrayList<Polyline>>();


    public MapsPresenter(@NotNull FragmentManager manager, Context context,
            Endpoint.Config config) {
        super();
        mContext = context;
        mManager = manager;
        mConfig = config;

        // Adding hardcoded favorites
        favoriteList.add(favorite1);
        favoriteList.add(favorite2);
        favoriteList.add(favorite3);
    }

    public final void setDynamicRecyclerView() {
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((LayoutManager) layoutManager);
        favoriteListAdapter = new FavoritesListAdapter(mContext,
                (FavoritesListAdapter.TextAdapterOnClickHandler) this,
                favoriteList);
        mRecView.setAdapter(favoriteListAdapter);
        mRecView.setVisibility(View.VISIBLE);
        favoriteListAdapter.notifyDataSetChanged();
    }

    public void onFavoriteClick(int position, @NotNull ArrayList<Favorite> list) {

        drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
                favoriteListAdapter.getmAllRoutesToFavorites().get(position));
    }

    public void drawRoutes(Route route, SectionedRoutes routeList) {
        removeAllRoutes();
        Repository.getInstance().setSelectedRoute(route);
        Repository.getInstance().setRoutesList(routeList);
        drawSelectedRoute();
        drawWalkRoute();

        makeExtendedOptionsFragment();
        mRecView.setVisibility(View.GONE);
    }

    public void removeAllRoutes() {
        // Remove all lines from map when new route gets selected
        polylineMap.clear();
        routeMap.clear();
        mMap.clear();
        makeStopsMarkers(mMap);
    }

    /* Removes bus route currently displayed on screen
       Called when a user selects a route to display from route options fragment
    */
    public void removeSelectdRoute() {
        Route previousRoute = Repository.getInstance().getSelectedRoute();

        // Removing paths of selected route from map
        for (Polyline polyline : mPathLastSelected) {
            polyline.remove();
        }

        // Removing borders from map
        for (Polyline border : mBorderLastSelected) {
            border.remove();
        }

        // Removing mappings
        polylineMap.remove(mPathLastSelected);
        routeMap.remove(previousRoute);
    }

    public void drawSelectedRoute() {

        Route route = Repository.getInstance().getSelectedRoute();
        PolylineOptions polylineOptionsCenter = new PolylineOptions();
        polylineOptionsCenter.color(R.color.tcat_blue);
        polylineOptionsCenter.clickable(true);
        polylineOptionsCenter.width(25F);

        // Creating larger polyline under path to provide border
        PolylineOptions polylineOptionsBorder = new PolylineOptions();
        polylineOptionsBorder.color(Color.RED);
        polylineOptionsBorder.width(30F);

        List<Direction> directionList = Arrays.asList(
                Repository.getInstance().getSelectedRoute().getDirections());
        ArrayList<Polyline> polylinePathList = new ArrayList<Polyline>();
        ArrayList<Polyline> polylineBorderList = new ArrayList<Polyline>();

        for (Direction direction : directionList) {
            if (direction.getType().equals("walk")) {
                polylineOptionsCenter.pattern(PATTERN_DOT_LIST);
                polylineOptionsBorder.pattern(PATTERN_DOT_LIST);
                polylineOptionsCenter.width(30f);
            } else {
                polylineOptionsCenter.pattern(null);
                polylineOptionsBorder.pattern(null);

            }

            for (Coordinate coordinate : direction.getPath()) {
                LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                polylineOptionsCenter.add(latLng);
                polylineOptionsBorder.add(latLng);
            }

            // Drawing sub-polyline on map
            Polyline polylineBorder = mMap.addPolyline(polylineOptionsBorder);
            Polyline polylineCenter = mMap.addPolyline(polylineOptionsCenter);

            // Adding polyline representing path to polyline, route hash map
            polylinePathList.add(polylineCenter);
            polylineBorderList.add(polylineBorder);
        }

        polylineMap.put(polylinePathList, route);
        mBorderLastSelected = polylineBorderList;
        mPathLastSelected = polylinePathList;

        LatLng startLatLng = new LatLng(route.getStartCoords().getLatitude(),
                route.getStartCoords().getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15.0F));
    }

    public void drawWalkRoute() {

        Route route = Repository.getInstance().getRoutesList().getWalking()[0];

        // Check if this route is equal to selected route. If so, don't draw, as that means that
        // it's already been drawn
        if (route != Repository.getInstance().getSelectedRoute()) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.pattern(PATTERN_DOT_LIST);
            polylineOptions.color(Color.GRAY);
            polylineOptions.clickable(true);
            polylineOptions.width(30f);

            Direction[] directionList = route.getDirections();
            for (Direction direction : directionList) {
                for (Coordinate coordinate : direction.getPath()) {
                    LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                    polylineOptions.add(latLng);
                }
            }

            // Drawing route on map
            Polyline polyline = mMap.addPolyline(polylineOptions);
            ArrayList<Polyline> polyineList = new ArrayList<Polyline>(Arrays.asList(polyline));

            polylineMap.put(polyineList, route);
            routeMap.put(route, Collections.singletonList(polyineList));
        }

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        // Change color of previously selected route
        Route previousRoute = Repository.getInstance().getSelectedRoute();
        for (ArrayList<Polyline> sub_polyline_list : routeMap.get(previousRoute)) {
            for (Polyline sub_polyline : sub_polyline_list) {
                sub_polyline.setColor(Color.GRAY);
            }
        }

        // Update value, color of selected route
        Route newRoute = polylineMap.get(polyline);
        Repository.getInstance().setSelectedRoute(newRoute);

        for (ArrayList<Polyline> sub_polyline_list : routeMap.get(previousRoute)) {
            for (Polyline sub_polyline : sub_polyline_list) {
                sub_polyline.setColor(R.color.tcat_blue);
            }
        }
    }

    public void onRouteClick(int position, Route[] routeList) {
        // Remove currently selected route from map
        removeSelectdRoute();

        // Getting position within section
        Repository.getInstance().setSelectedRoute(routeList[position]);

        drawSelectedRoute();
        drawWalkRoute();
        slideView.setVisibility(View.GONE);

        //TODO: Detail view gets created and inflated here
    }

    public void makeExtendedOptionsFragment() {

        ExtendedFragment optionsFragment = new ExtendedFragment();
        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.add(R.id.container, optionsFragment);
        fragmentTransaction.addToBackStack((String) null);
        fragmentTransaction.commit();
        mManager.executePendingTransactions();

        // Setting up Extended Fragment recycler view
        RecyclerView recyclerView = ((MapsActivity) mContext).findViewById(
                R.id.nearby_stops_routes);
        routeOptionsListAdapter = new SectionedRecyclerViewAdapter();

        Route optimalAsList[] = new Route[1];
        optimalAsList[0] = Repository.getInstance().getRoutesList().getOptRoute();
        // creating routes list adapter for each section
        SectionAdapter optimalSection = new SectionAdapter(mContext, this,
                optimalAsList, "optimal", null);
        SectionAdapter fromStopsSection = new SectionAdapter(mContext, this,
                Repository.getInstance().getRoutesList().getFromStop(), "fromStops", "From Stops");
        SectionAdapter boardingSoonSection = new SectionAdapter(mContext, this,
                Repository.getInstance().getRoutesList().getBoardingSoon(), "boardingSoon",
                "Boarding Soon");
        SectionAdapter walkingSection = new SectionAdapter(mContext, this,
                Repository.getInstance().getRoutesList().getWalking(), "walking", "By Walking");

        // Adding sections -- check if some of them are null before adding (optimal will never be
        // null)
        routeOptionsListAdapter.addSection(optimalSection);
        if (Repository.getInstance().getRoutesList().getFromStop() != null &&
                Repository.getInstance().getRoutesList().getFromStop().length != 0) {
            routeOptionsListAdapter.addSection(fromStopsSection);
        }
        if (Repository.getInstance().getRoutesList().getBoardingSoon() != null &&
                Repository.getInstance().getRoutesList().getBoardingSoon().length != 0) {
            routeOptionsListAdapter.addSection(boardingSoonSection);
        }
        // If only route is walk route, set optimal to walk and leave walk section empty
        if (optimalAsList[0] !=
                Repository.getInstance().getRoutesList().getWalking()[0]) {
            routeOptionsListAdapter.addSection(walkingSection);
        }

        recyclerView.setLayoutManager((new LinearLayoutManager(mContext, 1, false)));

        recyclerView.setAdapter(routeOptionsListAdapter);

        slideView = ((MapsActivity) mContext).findViewById(R.id.slide_panel);

    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
        makeStopsMarkers(mMap);
    }

    /* Place markers on map
       Right now, the method displays all the bus stops (which gets messy)
     */
    public void makeStopsMarkers(GoogleMap mMap) {
        // Change icon size
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_bus_marker);
        Bitmap resized_bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

        Endpoint allStopsEndpoint = new Endpoint().path("v1/allstops").method(Endpoint.Method.GET);
        FutureNovaRequest.make(BusStop[].class, allStopsEndpoint).thenAccept(
                (APIResponse<BusStop[]> response) -> {
                    mStopsList = response.getData();

                    ((MapsActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (BusStop stop : mStopsList) {

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromBitmap(resized_bitmap))
                                        .position(
                                                new LatLng(stop.getLatitude(), stop.getLongitude()))
                                        .title(stop.getName());

                                mMap.addMarker(markerOptions);
                            }
                        }
                    });
                });

    }
}
