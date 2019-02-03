package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers.MapsController;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MapsController mController;
    private GoogleMap mMap;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView mRecView;
    private android.widget.SearchView mSearchView;

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.app_name))
                        .snippet(snippet));
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        final FragmentManager manager = getFragmentManager();
        mController = new MapsController(manager);
        Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRecView = findViewById(R.id.recycler_view_maps);
        mController.mRecView = mRecView;
        mController.setDynamicRecyclerView(this);
        mSearchView = findViewById(R.id.tb_toolbarsearch);
        mController.mSearchView = mSearchView;
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsFragment optionsFragment = new OptionsFragment();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, optionsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //mSearchView.getChildAt(R.id.allRoutes).setOnClickListener{
//            val extendedFragment = ExtendedFragment
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.container, extendedFragment.newInstance())
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }    }
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapLongClick(mMap);
        setUpMap();
    }


    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.isMyLocationEnabled = true;
        mMap.uiSettings.isMyLocationButtonEnabled = false;
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
            location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f));
            }
        }
    }
}
