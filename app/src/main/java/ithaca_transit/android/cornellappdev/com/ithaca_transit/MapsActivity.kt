package ithaca_transit.android.cornellappdev.com.ithaca_transit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers.MapsController
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mController =  MapsController()
    private lateinit var mMap: GoogleMap
    private lateinit var mRecView: RecyclerView

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latLng.latitude,
                    latLng.longitude)

            map.addMarker(MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.app_name))
                    .snippet(snippet))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mRecView = findViewById(R.id.recycler_view_maps);
        mController.mRecView = mRecView
        mController.
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Ithaca and move the camera
        val ithaca = LatLng(42.4, -76.5)
        mMap.addMarker(MarkerOptions().position(ithaca).title("Marker in Ithaca"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ithaca))
        setMapLongClick(mMap)
    }
}
