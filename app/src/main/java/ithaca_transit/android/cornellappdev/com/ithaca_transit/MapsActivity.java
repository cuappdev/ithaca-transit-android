package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;

import java8.util.Optional;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.LocationAutocomplete;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public final class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    public MapsPresenter mController;
    private GoogleMap mMap;
    private RecyclerView mRecView;
    private FloatingSearchView mSearchView;

    private Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    private Runnable workRunnable;

    protected void onCreate(Bundle savedInstanceState) {

        Endpoint.Config config = new Endpoint.Config();
        config.scheme = Optional.of("http");
        config.host = Optional.of("transit-backend.cornellappdev.com");
        config.commonPath = Optional.of("/api/v1");
        Endpoint.config = config;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        FragmentManager manager = this.getFragmentManager();
        Repository.ourInstance.setContext(this);
        mController = new MapsPresenter(manager, this, config);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        RecyclerView mRecView = this.findViewById(R.id.recycler_view_maps);
        mController.mRecView = mRecView;

        mController.setDynamicRecyclerView();

        setUpSearch();
    }

    private void setUpSearch() {
        mSearchView = (FloatingSearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> autoCompleteRequest(newQuery);
                handler.postDelayed(workRunnable, 250 /*delay*/);
            }
        });
    }

    private void autoCompleteRequest(String query) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("Content-Type", "application/json");

        JSONObject searchJSON = new JSONObject();
        try {
            searchJSON.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody = 
            RequestBody.create(MediaType.get("application/json; charset=utf-8"), searchJSON.toString());

        Endpoint searchEndpoint = new Endpoint()
                .path("search")
                .body(Optional.of(requestBody))
                .headers(map)
                .method(Endpoint.Method.POST);

        FutureNovaRequest.make(Place[].class, searchEndpoint).thenAccept(response -> {
            Place[] searchResults = response.getData();

            final List<LocationAutocomplete> wrappedResults = new ArrayList<>();
            if (searchResults != null)
                for (Place p : searchResults)
                    wrappedResults.add(new LocationAutocomplete(p));
            mSearchView.post(new Runnable() {
                public void run() {
                    mSearchView.swapSuggestions(wrappedResults);
                }
            });
        });
    }

    private final void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener((new OnMapLongClickListener() {
            public final void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f", latLng.latitude, latLng.longitude);
                map.addMarker((new MarkerOptions()).position(latLng)
                        .title(MapsActivity.this.getString(R.string.app_name)).snippet(snippet));
            }
        }));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapLongClick(mMap);
        setUpMap();
        mController.setmMap(mMap);
    }

    private final void setUpMap() {
        if (ActivityCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) this, (OnSuccessListener) (new OnSuccessListener() {
                public void onSuccess(Object var1) {
                    this.onSuccess((Location) var1);
                }

                public final void onSuccess(Location location) {
                    if (location != null) {
                        MapsActivity.this.lastLocation = location;
                        LatLng currentLatLng = new LatLng(42.4446, -76.4823);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0F));
                    }
                }
            }));
        }
    }
}
