package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.RouteSwitcherAdapter;
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

    private Button mRouteIndicator;
    private ConstraintLayout mRouteSwitchLayout;
    private EditText mRouteStartInput;
    private EditText mRouteEndInput;
    private ImageButton mRouteSwitchButton;
    private ListView mRouteSwitchList;
    private RouteSwitcherAdapter mRouteSwitcherAdapter;
    private int focusedView;
    private Place startLoc;
    private Place endLoc;

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
        mController = new MapsPresenter(manager, this, config);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        mRecView = this.findViewById(R.id.recycler_view_maps);
        mController.mRecView = mRecView;

        mController.setDynamicRecyclerView();

        mHomeView = this.findViewById(R.id.home_view);
        mHomeMenu = this.findViewById(R.id.home_menu);

        setUpRouteSwitcher();
        setUpSearch();
        setUpMenu();
    }

    public void onHeaderClick(View view) {
//        Intent goHome = new Intent(getBaseContext(), MapsActivity.class);
//        startActivity(goHome);
    }

    private void setUpRouteSwitcher() {
        mRouteIndicator = this.findViewById(R.id.route_indicator);
        mRouteSwitchLayout = this.findViewById(R.id.route_switcher_layout);
        mRouteStartInput = this.findViewById(R.id.startloc_input);
        mRouteEndInput = this.findViewById(R.id.endloc_input);
        mRouteSwitchButton = this.findViewById(R.id.swap_destination);
        mRouteSwitchList = this.findViewById(R.id.route_switcher_list);

        mRouteSwitcherAdapter = new RouteSwitcherAdapter(this, new ArrayList<Place>());

        mRouteSwitchList.setAdapter(mRouteSwitcherAdapter);
        mRouteSwitchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (focusedView) {
                    case 1: {
                        startLoc = (Place) adapterView.getItemAtPosition(i);
                        mRouteStartInput.setText("");
                        mRouteStartInput.setHint(startLoc.getName());
                        mRouteSwitcherAdapter.clear();
                    }
                    break;
                    case 2: {
                        endLoc = (Place) adapterView.getItemAtPosition(i);
                        mRouteEndInput.setText("");
                        mRouteEndInput.setHint(endLoc.getName());
                        mRouteSwitcherAdapter.clear();
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        mRouteSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place temp = startLoc;
                startLoc = endLoc;
                endLoc = temp;
                mRouteStartInput.setHint(startLoc.getName());
                mRouteEndInput.setHint(endLoc.getName());
            }
        });
        mRouteIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRouteStartInput.setHint(startLoc.getName());
                mRouteEndInput.setHint(endLoc.getName());
                mRouteIndicator.setVisibility(View.GONE);
                mRouteSwitchLayout.setVisibility(View.VISIBLE);
            }
        });
        mRouteStartInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                focusedView = 1;
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> routeSwitcherAutocomplete(charSequence.toString(),
                        mRouteSwitcherAdapter);
                handler.postDelayed(workRunnable, 250 /*delay*/);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mRouteEndInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                focusedView = 2;
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> routeSwitcherAutocomplete(charSequence.toString(),
                        mRouteSwitcherAdapter);
                handler.postDelayed(workRunnable, 250 /*delay*/);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void routeSwitcherAutocomplete(String query, RouteSwitcherAdapter rsAdapter) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("Content-Type", "application/json");

        JSONObject searchJSON = new JSONObject();
        try {
            searchJSON.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody =
                RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                        searchJSON.toString());

        Endpoint searchEndpoint = new Endpoint()
                .path("v1/search")
                .body(Optional.of(requestBody))
                .headers(map)
                .method(Endpoint.Method.POST);

        FutureNovaRequest.make(Place[].class, searchEndpoint).thenAccept(response -> {
            Place[] searchResults = response.getData();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rsAdapter.clear();
                    if (searchResults != null) {
                        int i = 0;
                        for (Place p : searchResults) {
                            rsAdapter.add(p);
                            //Limit number of displayed results to 12
                            i++;
                            if (i > 12) break;
                        }
                    }
                }
            });
        });
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

        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                mHomeView.openDrawer(mHomeMenu);
            }

            @Override
            public void onMenuClosed() {
                mHomeView.closeDrawer(mHomeMenu);
            }
        });

        mSearchView.setOnBindSuggestionCallback(
                new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
                    @Override
                    public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                            TextView textView, SearchSuggestion item, int itemPosition) {
                        LocationAutocomplete suggestion = (LocationAutocomplete) item;

                        if (suggestion.getPlace().getPlaceID() == null) {
                            leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_bus_stop, null));
                            textView.setTextColor(Color.parseColor("#111111"));
                            textView.setTextSize(15f);
                        } else {
                            leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_loc_stop, null));
                            Util.setIconColor(leftIcon, Color.parseColor("#cacaca"));
                            String text = "<font color='black'>" + suggestion.getPlace().getName()
                                    + "</font>"
                                    + "<br/>" + suggestion.getPlace().getDetail();
                            textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                            textView.setTextSize(15f);
                        }
                    }
                });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                final Place dest = ((LocationAutocomplete) searchSuggestion).getPlace();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startLoc = new Place(MapsActivity.this.lastLocation.getLatitude(),
                                MapsActivity.this.lastLocation.getLongitude(),
                                "Current Location");
                        endLoc = dest;
                        if (dest.getPlaceID() == null) {
                            launchRoute(MapsActivity.this.lastLocation.getLatitude() +
                                            ", " + MapsActivity.this.lastLocation.getLongitude(),
                                    dest.toString(), dest.getName());
                        } else {
                            FetchPlaceRequest request = FetchPlaceRequest.builder(dest.toString(),
                                    Arrays.asList(
                                            com.google.android.libraries.places.api.model.Place
                                                    .Field.LAT_LNG)).build();

                            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                                launchRoute(MapsActivity.this.lastLocation.getLatitude() +
                                                ", " + MapsActivity.this.lastLocation
                                                .getLongitude(),
                                        response.getPlace().getLatLng().latitude + ", "
                                                + response.getPlace().getLatLng().longitude,
                                        dest.getName());
                            });
                        }
                    }
                }).start();
            }

            @Override
            public void onSearchAction(String currentQuery) {
            }
        });
    }

    //Retrieves Route info from backend, sends it to MapPresenter
    private void launchRoute(String start, String end, String name) {

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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (optRoute != null) {
                                getController().drawRoutes(optRoute, sectionedRoutes);
                                getSearchView().setSearchText(name);
                                getSearchView().clearSearchFocus();
                                getSearchView().setVisibility(View.GONE);
                                mRouteIndicator.setVisibility(View.VISIBLE);
                                String s = "Current Location  >  " + (name.length() > 20 ?
                                        name.substring(0, 17) + "..." : name);
                                SpannableString route =
                                        new SpannableString(
                                                s.substring(0, Math.min(s.length(), 42)));
                                route.setSpan(new ForegroundColorSpan(Color.parseColor("#08a0e0")),
                                        0, 19, 0);
                                // make "Here" (characters 6 to 10) Blue
                                route.setSpan(new ForegroundColorSpan(Color.BLACK), 20,
                                        route.length(), 0);
                                mRouteIndicator.setText(route, TextView.BufferType.SPANNABLE);
                                mRouteIndicator.bringToFront();
                            } else {
                                Toast.makeText(MapsActivity.this,
                                        "Something went wrong, we can't provide a route.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });
    }

    private MapsPresenter getController() {
        return mController;
    }

    private FloatingSearchView getSearchView() {
        return mSearchView;
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
                RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                        searchJSON.toString());

        Endpoint searchEndpoint = new Endpoint()
                .path("v1/search")
                .body(Optional.of(requestBody))
                .headers(map)
                .method(Endpoint.Method.POST);

        FutureNovaRequest.make(Place[].class, searchEndpoint).thenAccept(response -> {
            Place[] searchResults = response.getData();

            final List<LocationAutocomplete> wrappedResults = new ArrayList<>();
            if (searchResults != null) {
                int i = 0;
                for (Place p : searchResults) {
                    wrappedResults.add(new LocationAutocomplete(p));

                    //Limit number of displayed results to 8
                    i++;
                    if (i > 8) break;
                }
            }
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
