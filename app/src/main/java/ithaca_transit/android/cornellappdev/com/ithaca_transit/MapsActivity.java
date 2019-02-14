package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

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

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers.MapsController;

import java.util.Locale;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import kotlin.TypeCastException;


public final class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    public MapsController mController;
    private GoogleMap mMap;
    private RecyclerView mRecView;
    private SearchView mSearchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        FragmentManager manager = this.getFragmentManager();
        Repository.getInstance().setContext(this);
        mController = new MapsController(manager);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        RecyclerView mRecView = this.findViewById(R.id.recycler_view_maps);
        mController.mRecView = mRecView;
        mController.setDynamicRecyclerView(this);
        mSearchView = this.findViewById(R.id.tb_toolbarsearch);
        mController.mSearchView = mSearchView;
    }

    private final void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener((new OnMapLongClickListener() {
            public final void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f", latLng.latitude, latLng.longitude);
                ;
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
        if (ActivityCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) this, (OnSuccessListener) (new OnSuccessListener() {
                // $FF: synthetic method
                // $FF: bridge method
                public void onSuccess(Object var1) {
                    this.onSuccess((Location) var1);
                }

                public final void onSuccess(Location location) {
                    if (location != null) {
                        MapsActivity.this.lastLocation = location;
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0F));
                    }

                }
            }));
        }
    }

}
