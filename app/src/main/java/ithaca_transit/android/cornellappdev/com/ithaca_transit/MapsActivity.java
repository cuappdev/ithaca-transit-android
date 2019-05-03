package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.LocationAutocomplete;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public final class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private Location lastLocation;
    public MapsPresenter mController;
    private GoogleMap mMap;
    private RecyclerView mRecView;
    private DrawerLayout mHomeView;
    private FloatingSearchView mSearchView;
    private NavigationView mHomeMenu;

    private Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    private Runnable workRunnable;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRecView.getVisibility() == View.GONE) {
            mRecView.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        Endpoint.Config config = new Endpoint.Config();
        config.scheme = Optional.of("https");
        config.host = Optional.of("transit-backend.cornellappdev.com");

        // Versions may vary, so version type is not appended to common path
        config.commonPath = Optional.of("/api/");
        Endpoint.config = config;

        //Initializes Google Places, Location Identifier, and the Google Maps Controller
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Places.initialize(getApplicationContext(),
                getResources().getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        FragmentManager manager = this.getFragmentManager();
//        mController = new MapsPresenter(this, config);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        mRecView = this.findViewById(R.id.recycler_view_maps);
    }

    private void setUpMenu() {
        mHomeView.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset < 0.4) {
                            mSearchView.setLeftMenuOpen(false);
                        }
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        mSearchView.setLeftMenuOpen(true);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                }
        );

        mHomeMenu.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        mHomeView.closeDrawer(mHomeMenu);
                        return true;
                    }
                }
        );
    }

    private MapsPresenter getController() {
        return mController;
    }

    private FloatingSearchView getSearchView() {
        return mSearchView;
    }

    //TODO: what is the point of this method
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
    }

    private final void setUpMap() {
        if (ActivityCompat.checkSelfPermission((Context) this,
                "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) this,
                    (OnSuccessListener) (new OnSuccessListener() {
                        public void onSuccess(Object var1) {
                            this.onSuccess((Location) var1);
                        }

                        public final void onSuccess(Location location) {
                            if (location != null) {
                                MapsActivity.this.lastLocation = location;
                                LatLng currentLatLng = new LatLng(lastLocation.getLatitude(),
                                        lastLocation.getLongitude());
                                mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0F));
                            }
                        }
                    }));
        }
    }
}
