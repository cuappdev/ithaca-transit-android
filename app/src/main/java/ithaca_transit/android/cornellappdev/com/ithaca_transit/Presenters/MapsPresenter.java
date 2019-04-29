package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import static android.support.v4.content.res.ResourcesCompat.getColor;

import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.ExpandableListViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.DetailViewFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.ExtendedFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.MapsActivity;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BoundingBox;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusLocation;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Coordinate;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;


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
    private SlidingUpPanelLayout slideView;
    public FloatingSearchView mSearchView;
    private BusStop[] mStopsList;

    private ArrayList<Polyline> mPathLastSelected;
    private ArrayList<Polyline> mBorderLastSelected;

    private Marker mBusLocationMarker;

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
        ((MapsActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
                        favoriteListAdapter.getmAllRoutesToFavorites().get(position));
            }
        });
    }

    public void drawRoutes(Route route, SectionedRoutes routeList) {
        removeAllRoutes();
        Repository.getInstance().setSelectedRoute(route);
        Repository.getInstance().setRoutesList(routeList);
        drawSelectedRoute();
        drawWalkRoute();

        makeExtendedOptionsFragment();
        slideView.setPanelHeight(900);
        mSearchView.bringToFront();
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
        Route walkRoute = Repository.getInstance().getRoutesList().getWalking()[0];

        if (walkRoute != route) {
            PolylineOptions polylineOptionsCenter = new PolylineOptions();
            polylineOptionsCenter.color(getColor(mContext.getResources(), R.color.tcatBlue, null));
            polylineOptionsCenter.clickable(true);
            polylineOptionsCenter.width(25F);

            // Creating larger polyline under path to provide border
            PolylineOptions polylineOptionsBorder = new PolylineOptions();
            polylineOptionsBorder.color(getColor(mContext.getResources(), R.color.tcatBlue, null));
            polylineOptionsBorder.width(30F);


            ArrayList<Polyline> polylinePathList = new ArrayList<Polyline>();
            ArrayList<Polyline> polylineBorderList = new ArrayList<Polyline>();
            List<Direction> directionList = Arrays.asList(
                    Repository.getInstance().getSelectedRoute().getDirections());

            for (Direction direction : directionList) {
                if (direction.getType().equals("walk")) {
                    polylineOptionsCenter.pattern(PATTERN_DOT_LIST);
                    polylineOptionsBorder.pattern(PATTERN_DOT_LIST);
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

            // Setting bounds
            BoundingBox boundingBox = route.getBoundingBox();
            LatLng northeast = new LatLng(boundingBox.getMaxLat(), boundingBox.getMaxLong());
            LatLng southwest = new LatLng(boundingBox.getMinLat(), boundingBox.getMinLong());
            LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));

            // Draw dot for start, end
            Bitmap walkMarker = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.walkmarker);
            walkMarker = Bitmap.createScaledBitmap(walkMarker, 50, 50, false);

            Bitmap busMarker = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.busmarker);
            busMarker = Bitmap.createScaledBitmap(walkMarker, 50, 50, false);

            Bitmap endMarker = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.endmarker);
            busMarker = Bitmap.createScaledBitmap(walkMarker, 50, 50, false);

            Bitmap chosenBitmap;
            for (Direction direction : directionList) {

                Coordinate coord = direction.getPath()[0];
                if (direction.getType().equals("walk")) {
                    chosenBitmap = walkMarker;
                } else {
                    chosenBitmap = busMarker;
                }

                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(chosenBitmap))
                        .position(new LatLng(coord.getLatitude(), coord.getLongitude()))
                        .title(direction.getName());

                mMap.addMarker(markerOptions);
            }

            Coordinate endCoords = route.getEndCoords();
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(endMarker))
                    .position(new LatLng(endCoords.getLatitude(), endCoords.getLongitude()))
                    .title(null);
            mMap.addMarker(markerOptions);
        }


    }

    public void drawWalkRoute() {

        Route route = Repository.getInstance().getRoutesList().getWalking()[0];

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
                sub_polyline.setColor(R.color.tcatBlue);
            }
        }
    }

    public void onRouteClick(int position, Route[] routeList) {
        // Remove currently selected route from map
        removeSelectdRoute();
        Route newRoute = routeList[position];
        // Getting position within section
        Repository.getInstance().setSelectedRoute(newRoute);

        drawSelectedRoute();
        drawWalkRoute();

        // Check to see if route invovles buses, if so, track the first bus
        if (!Repository.getInstance().getSelectedRoute().isWalkOnlyRoute()) {
            ArrayList<Direction> directionArrayList =
                    Repository.getInstance().getSelectedRoute().getBusInfo();

            // For now, only track first bus
            int dirCount = 0;
            Direction direction = directionArrayList.get(dirCount);
            ScheduledExecutorService scheduledExecutorService =
                    Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    updateBusLocation(direction);
                }
            };

            int initDelay = 0;
            int updatePeriod = 5;
            scheduledExecutorService.scheduleAtFixedRate(task, initDelay, updatePeriod,
                    TimeUnit.SECONDS);
        }
        makeDetailViewFragment();
    }

    public void makeDetailViewFragment() {
        DetailViewFragment detailViewFragment = new DetailViewFragment();

        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detailViewFragment);
        fragmentTransaction.addToBackStack((String)null);
        fragmentTransaction.commit();
        mManager.executePendingTransactions();

        Route route = Repository.getInstance().getSelectedRoute();
        ArrayList<Direction> detailDirections = route.getDetailDirections();

        ExpandableListView listView = ((MapsActivity) mContext).findViewById(
                R.id.directions_list_view);

        ExpandableListViewAdapter listViewAdapter = new ExpandableListViewAdapter(mContext,
                detailDirections);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i,
                    long l) {
                return detailDirections.get(i).getStops().length > 0;
            }
        });
        listView.setAdapter(listViewAdapter);
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

        // Adding dialogue
        ImageView clock = ((MapsActivity) mContext).findViewById(R.id.ic_clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogFragment = new Dialog(mContext);
                dialogFragment.setContentView(R.layout.dialog_leave_now);

                DatePicker datePicker = dialogFragment.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogFragment.findViewById(R.id.time_picker);

                Date date = new Date();
                datePicker.init(date.getYear(), date.getMonth(), date.getDay(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker datePicker, int i, int i1,
                                    int i2) {
                                Date leaveDate = new Date();
                                leaveDate.setYear(leaveDate.getYear());
                                leaveDate.setYear(leaveDate.getMonth());
                                leaveDate.setYear(leaveDate.getDay());
                            }
                        });

                dialogFragment.show();
            }
        });

        slideView = ((MapsActivity) mContext).findViewById(R.id.maps_activity);
        mSearchView.bringToFront();
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

        ((MapsActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.ic_bus_marker);
                Bitmap resized_bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

                Endpoint allStopsEndpoint = new Endpoint().path("v1/allstops").method(
                        Endpoint.Method.GET);
                FutureNovaRequest.make(BusStop[].class, allStopsEndpoint).thenAccept(
                        (APIResponse<BusStop[]> response) -> {
                            mStopsList = response.getData();

                            ((MapsActivity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (BusStop stop : mStopsList) {

                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.fromBitmap(
                                                        resized_bitmap))
                                                .position(
                                                        new LatLng(stop.getLatitude(),
                                                                stop.getLongitude()))
                                                .title(stop.getName());

                                        mMap.addMarker(markerOptions);
                                    }
                                }
                            });
                        });
            }
        });
    }

    public void updateBusLocation(Direction direction) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");

        JSONObject trackingJSON = new JSONObject();
        try {
            trackingJSON.put("routeID", direction.getRouteNumber());
            trackingJSON.put("stopID", direction.getStops()[0].getName());
            trackingJSON.put("tripID", direction.getTripIdentifiers());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody =
                RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                        trackingJSON.toString());

        Endpoint searchEndpoint = new Endpoint()
                .path("v1/tracking")
                .body(Optional.of(requestBody))
                .headers(map)
                .method(Endpoint.Method.POST);

        FutureNovaRequest.make(BusLocation.class, searchEndpoint).thenAccept(response -> {
            BusLocation busLocation = response.getData();
            // remomove old marker, add new one

            if (mBusLocationMarker != null) {
                mBusLocationMarker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.buslocationmarker))
                    .position(
                            new LatLng(busLocation.getLatitude(), busLocation.getLongitude()))
                    .title("Bus " + busLocation.getName());
            mBusLocationMarker = mMap.addMarker(markerOptions);
        });
    }

    public void setSearchView(FloatingSearchView searchView) {
        mSearchView = searchView;
    }
}
