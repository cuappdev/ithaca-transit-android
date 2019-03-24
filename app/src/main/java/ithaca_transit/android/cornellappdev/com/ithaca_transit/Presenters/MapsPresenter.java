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

import com.appdev.futurenovajava.Endpoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.RoutesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.ExtendedFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.MapsActivity;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Coordinate;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;


public final class MapsPresenter implements FavoritesListAdapter.ListAdapterOnClickHandler,
        GoogleMap.OnPolylineClickListener {
    public static final PatternItem DOT = new Dot();
    public static final List<PatternItem> PATTERN_DOT_LIST = Arrays.asList(DOT);

    // Hardcoded data for favorites
    private static final Place goldwin = new Place(42.4491, -76.4835, "Goldwin");
    private static final Place duffield = new Place(42.4446, -76.4823, "Duffield");
    private static final Place dickson = new Place(42.4547, -76.4794, "Clara Dickson");
    private static final Favorite favorite1 = new Favorite(goldwin, duffield);
    private static final Favorite favorite2 = new Favorite(dickson, duffield);
    private static final Favorite favorite3 = new Favorite(duffield, dickson);
    private static final Favorite[] favoriteList = new Favorite[]{favorite1, favorite2, favorite3};
    private FavoritesListAdapter favoriteListAdapter;
    private RoutesListAdapter routeOptionsListAdapter;

    private Endpoint.Config mConfig;
    private Context mContext;
    private FragmentManager mManager;
    private GoogleMap mMap;
    public RecyclerView mRecView;
    public SearchView mSearchView;

    // Maps a polyline to its parent route
    private HashMap<Polyline, Route> polylineMap = new HashMap<Polyline, Route>();
    ;

    // Maps a route to all polylines that represent its path
    private HashMap<Route, List<Polyline>> routeMap = new HashMap<Route, List<Polyline>>();

    public MapsPresenter(@NotNull FragmentManager manager, Context context,
            Endpoint.Config config) {
        super();
        mContext = context;
        mManager = manager;
        mConfig = config;

    }

    public final void setDynamicRecyclerView() {
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((LayoutManager) layoutManager);
        favoriteListAdapter = new FavoritesListAdapter(mContext,
                (FavoritesListAdapter.ListAdapterOnClickHandler) this,
                favoriteList, mConfig);
        mRecView.setAdapter(favoriteListAdapter);
        mRecView.setVisibility(View.VISIBLE);
        favoriteListAdapter.notifyDataSetChanged();

    }

    public void onFavoriteClick(int position, @NotNull Favorite[] list) {

        drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
                favoriteListAdapter.getmAllRoutesToFavorites().get(position));
    }

    public void drawRoutes(Route route, ArrayList<Route> routeList) {
        removeAllRoutes();
        Repository.getInstance().setSelectedRoute(route);
        Repository.getInstance().setRoutesList(routeList);
        drawSelectedRoute();
        drawWalkRoute();
        makeExtendedOptionsFragment();
    }

    public void removeAllRoutes() {
        // Remove all lines from map when new route gets selected
        polylineMap.clear();
        mMap.clear();
    }

    /* Removes bus route currently displayed on screen
       Called when a user selects a route to display from route options
    */
    public void removeSelectdRoute() {
        Route previousRoute = Repository.getInstance().getSelectedRoute();
        List<Polyline> polylines = routeMap.get(previousRoute);

        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        routeMap.remove(previousRoute);
    }

    public void drawSelectedRoute() {
        //removeAllRoutes();

        Route route = Repository.getInstance().getSelectedRoute();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.clickable(true);

        List<Direction> directionList = Arrays.asList(
                Repository.getInstance().getSelectedRoute().getDirections());
        ArrayList<Polyline> polylineList = new ArrayList<Polyline>();

        for (Direction direction : directionList) {
            if (direction.getType().equals("walk")) {
                polylineOptions.pattern(PATTERN_DOT_LIST);
                polylineOptions.width(30f);
            } else {
                polylineOptions.pattern(null);
            }

            for (Coordinate coordinate : direction.getPath()) {
                LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                polylineOptions.add(latLng);
            }

            // Drawing sub-polyline on map
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polylineList.add(polyline);
            polylineMap.put(polyline, route);
        }

        LatLng startLatLng = new LatLng(route.getStartCoords().getLatitude(),
                route.getStartCoords().getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15.0F));
    }

    public void drawWalkRoute() {
        int idxLast = Repository.getInstance().getRoutesList().size();
        Route route = Repository.getInstance().getRoutesList().get(idxLast - 1);

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
        polylineMap.put(polyline, route);
        routeMap.put(route, Collections.singletonList(polyline));

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        // Change color of previously selected route
        Route previousRoute = Repository.getInstance().getSelectedRoute();
        for (Polyline sub_polyline : routeMap.get(previousRoute)) {
            sub_polyline.setColor(Color.GRAY);
        }

        // Update value, color of selected route
        Route newRoute = polylineMap.get(polyline);
        Repository.getInstance().setSelectedRoute(newRoute);

        for (Polyline sub_polyline : routeMap.get(newRoute)) {
            sub_polyline.setColor(Color.BLUE);
        }
    }

    public void onRouteClick(int adapterPosition, ArrayList<Route> routeArrayList) {
        Repository.getInstance().setSelectedRoute(routeArrayList.get(adapterPosition));
        drawSelectedRoute();
        drawWalkRoute();
    }

    public void makeExtendedOptionsFragment() {

        ExtendedFragment optionsFragment = new ExtendedFragment();
        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.add(R.id.container, optionsFragment);
        fragmentTransaction.addToBackStack((String) null);
        fragmentTransaction.commit();
        mManager.executePendingTransactions();

        // Setting up Extended Fragment recycler view
        RecyclerView recyclerView = ((MapsActivity) mContext).findViewById(R.id.routes);
        recyclerView.setLayoutManager((new LinearLayoutManager(mContext, 1, false)));
        routeOptionsListAdapter = new RoutesListAdapter(mContext, this,
                Repository.getInstance().getRoutesList().size(),
                Repository.getInstance().getRoutesList());
        recyclerView.setAdapter(routeOptionsListAdapter);
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }
}
