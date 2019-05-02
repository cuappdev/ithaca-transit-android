package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import static android.support.v4.content.res.ResourcesCompat.getColor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BoundingBox;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusLocation;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Coordinate;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements GoogleMap.OnPolylineClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private Location lastLocation;
    private Marker mBusLocationMarker;
    private GoogleMap mMap;
    private Context context;
    private MapsPresenter mMapsPresenter = new MapsPresenter();
    private BusStop[] mStopsList;
    // Maps a polyline to its parent route
    private HashMap<ArrayList<Polyline>, Route>
            polylineMap = new HashMap<ArrayList<Polyline>, Route>();

    public static final PatternItem DOT = new Dot();
    public static final List<PatternItem> PATTERN_DOT_LIST = Arrays.asList(DOT);

    // Maps a route to all polylines that represent its path
    private HashMap<Route, List<ArrayList<Polyline>>>
            routeMap = new HashMap<Route, List<ArrayList<Polyline>>>();

    private ArrayList<Polyline> mPathLastSelected;
    private ArrayList<Polyline> mBorderLastSelected;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        context = rootView.getContext();

        Endpoint.Config config = new Endpoint.Config();
        config.scheme = Optional.of("https");
        config.host = Optional.of("transit-backend.cornellappdev.com");

        // Versions may vary, so version type is not appended to common path
        config.commonPath = Optional.of("/api/");
        Endpoint.config = config;

        //Initializes Google Places, Location Identifier, and the Google Maps Controller
        Places.initialize(rootView.getContext(),
                getResources().getString(R.string.google_maps_key));
        placesClient = Places.createClient(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                setUpMap();
                makeStopsMarkers(mMap);
            }
        });
        return rootView;
    }

    //Retrieves Route info from backend, sends it to MapPresenter
    public void launchRoute(String start, String end, String name) {

        Calendar calendar = Calendar.getInstance(
                TimeZone.getTimeZone("\"America/NewYork\""));
        int secondsEpoch = (int) (calendar.getTimeInMillis() / 1000L);

        Map<String, String> mapString = new HashMap<String, String>();
        mapString.put("Content-Type", "application/json");
        JSONObject searchJSON = new JSONObject();

        try {
            searchJSON.put("start", start);
            searchJSON.put("end", end);
            searchJSON.put("arriveBy", String.valueOf(false));
            searchJSON.put("destinationName", name);
            searchJSON.put("time", String.valueOf(secondsEpoch));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody =
                RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                        searchJSON.toString());

        Endpoint searchEndpoint = new Endpoint()
                .path("v2/route")
                .body(Optional.of(requestBody))
                .headers(mapString)
                .method(Endpoint.Method.POST);

        FutureNovaRequest.make(
                SectionedRoutes.class, searchEndpoint).thenAccept(
                (APIResponse<SectionedRoutes> response) -> {

                    SectionedRoutes sectionedRoutes = response.getData();
                    Route optRoute = sectionedRoutes.getOptRoute();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (optRoute != null) {
                                drawRoutes(optRoute, sectionedRoutes);
                            } else {
                                Toast.makeText(context,
                                        "Something went wrong, we can't provide a route.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });
    }

    private final void setUpMap() {
        if (ActivityCompat.checkSelfPermission(context,
                "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lastLocation = location;
                                mMapsPresenter.setLastLocation(lastLocation);
                                LatLng currentLatLng = new LatLng(lastLocation.getLatitude(),
                                        lastLocation.getLongitude());
                                mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0F));
                            }
                        }
                    });
        }
    }

    public void drawRoutes(Route route, SectionedRoutes routeList) {
        removeAllRoutes();
        Repository.getInstance().setSelectedRoute(route);
        Repository.getInstance().setRoutesList(routeList);
        drawSelectedRoute();
        drawWalkRoute();
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
            polylineOptionsCenter.color(getColor(context.getResources(), R.color.tcatBlue, null));
            polylineOptionsCenter.clickable(true);
            polylineOptionsCenter.width(25F);

            // Creating larger polyline under path to provide border
            PolylineOptions polylineOptionsBorder = new PolylineOptions();
            polylineOptionsBorder.color(getColor(context.getResources(), R.color.tcatBlue, null));
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
            Bitmap walkMarker = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.walkmarker);
            walkMarker = Bitmap.createScaledBitmap(walkMarker, 50, 50, false);

            Bitmap busMarker = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.busmarker);
            busMarker = Bitmap.createScaledBitmap(walkMarker, 50, 50, false);

            Bitmap endMarker = BitmapFactory.decodeResource(context.getResources(),
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

        // Code below causes the app to stop responding (without giving clear error)

        // Check to see if route invovles buses, if so, track the first bus
//        if (!Repository.getInstance().getSelectedRoute().isWalkOnlyRoute()) {
//            ArrayList<Direction> directionArrayList =
//                    Repository.getInstance().getSelectedRoute().getBusInfo();
//
//            // For now, only track first bus
//            int dirCount = 0;
//            Direction direction = directionArrayList.get(dirCount);
//            ScheduledExecutorService scheduledExecutorService =
//                    Executors.newSingleThreadScheduledExecutor();
//            Runnable task = new Runnable() {
//                @Override
//                public void run() {
//                    updateBusLocation(direction);
//                }
//            };
//
//            int initDelay = 0;
//            int updatePeriod = 5;
//            scheduledExecutorService.scheduleAtFixedRate(task, initDelay, updatePeriod,
//                    TimeUnit.SECONDS);
//        }

//        ((MainActivity) context).makeDetailViewFragment();
    }

    /* Place markers on map
      Right now, the method displays all the bus stops (which gets messy)
    */
    public void makeStopsMarkers(GoogleMap mMap) {

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_bus_marker);
                Bitmap resized_bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, false);

                Endpoint allStopsEndpoint = new Endpoint().path("v1/allstops").method(
                        Endpoint.Method.GET);
                FutureNovaRequest.make(BusStop[].class, allStopsEndpoint).thenAccept(
                        (APIResponse<BusStop[]> response) -> {
                            mStopsList = response.getData();

                            ((MapsActivity) context).runOnUiThread(new Runnable() {
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


}
