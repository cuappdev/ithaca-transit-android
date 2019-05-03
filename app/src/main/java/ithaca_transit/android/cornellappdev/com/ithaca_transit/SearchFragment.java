package ithaca_transit.android.cornellappdev.com.ithaca_transit;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.RouteSwitcherAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.LocationAutocomplete;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private FloatingSearchView mSearchView;
    private DrawerLayout mHomeView;
    private NavigationView mHomeMenu;


    private Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    private Runnable workRunnable;
    private PlacesClient placesClient;
    private Context mContext;
    private View rootView;
    private OnSearchFragmentListener mCallback;
    private MapsPresenter mMapsPresenter = new MapsPresenter();

    private Button mRouteIndicator;
    private ConstraintLayout mRouteSwitchLayout;
    private EditText mRouteStartInput;
    private EditText mRouteEndInput;
    private ImageButton mRouteSwitchButton;
    private ListView mRouteSwitchList;
    private RouteSwitcherAdapter mRouteSwitcherAdapter;
    private Place startLoc;
    private Place endLoc;
    private int focusedInput;
    private boolean routeSwitcherOpen;

<<<<<<< HEAD
=======
    public SearchFragment() {
    }

    public void setEndLoc(Place end) {
        endLoc = end;
    }

>>>>>>> 6c3926a5857bf5dadf3b88efffd14f8a73544bf6
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mContext = rootView.getContext();
        Places.initialize(mContext, getResources().getString(R.string.google_maps_key));
        placesClient = Places.createClient(mContext);

        mSearchView = (FloatingSearchView) rootView.findViewById(R.id.search_view);
//        mHomeView = rootView.findViewById(R.id.home_view);
//        mHomeMenu = rootView.findViewById(R.id.home_menu);

        setUpSearch();
        //TODO: removed drawer for now bc it was overlaying on top of the entire map...need to
        // rethink architecture and hierarchy of views
//        setUpMenu();
        setUpRouteSwitcher();
        return rootView;
    }

    private void setUpSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> autoCompleteRequest(newQuery);
                handler.postDelayed(workRunnable, 250 /*delay*/);
            }
        });

        //TODO: removed the drawer for now and commented out these lines for debugging purposes
//        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
//            @Override
//            public void onMenuOpened() {
//                mHomeView.openDrawer(mHomeMenu);
//            }
//
//            @Override
//            public void onMenuClosed() {
//                mHomeView.closeDrawer(mHomeMenu);
//            }
//        });

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
                        if (dest.getPlaceID() == null) {
                            Location lastLocation = mMapsPresenter.getLastLocation();
                            startLoc = new Place(lastLocation.getLatitude(),
                                    lastLocation.getLongitude(), "Current Location");
                            endLoc = dest;
                            launchRoute(startLoc,endLoc);
                        } else {
                            FetchPlaceRequest request = FetchPlaceRequest.builder(dest.toString(),
                                    Arrays.asList(
                                            com.google.android.libraries.places.api.model.Place
                                                    .Field.LAT_LNG)).build();

                            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                                Location lastLocation = mMapsPresenter.getLastLocation();
                                startLoc = new Place(lastLocation.getLatitude(),
                                        lastLocation.getLongitude(), "Current Location");
                                endLoc = new Place(response.getPlace().getLatLng().latitude,
                                        response.getPlace().getLatLng().longitude,
                                        dest.getName());
                                launchRoute(startLoc,endLoc);
                            });
                        }
                        //TODO: change slider height to 0dp, must add method to interface and
                        // implement in mainactivity
                    }
                }).start();
            }

            @Override
            public void onSearchAction(String currentQuery) {
            }
        });
    }

    public void clickRouteIndicator() {
        Location lastLocation = mMapsPresenter.getLastLocation();
        startLoc = new Place(lastLocation.getLatitude(),
                lastLocation.getLongitude(), "Current Location");

        mRouteStartInput.setHint(startLoc.getName());
        mRouteEndInput.setHint(endLoc.getName());
        mRouteIndicator.setVisibility(View.GONE);
        mRouteSwitchLayout.setVisibility(View.VISIBLE);
        mRouteStartInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
        handler.removeCallbacks(workRunnable);
        workRunnable = () -> routeSwitcherOpen = true;
        //Delay Setting Boolean
        handler.postDelayed(workRunnable, 100);
    }

    private void setUpRouteSwitcher() {
        mRouteIndicator = rootView.findViewById(R.id.route_indicator); //The Route Display Button
        mRouteSwitchLayout = rootView.findViewById(R.id.route_switcher_layout);
        mRouteStartInput = rootView.findViewById(R.id.startloc_input);
        mRouteEndInput = rootView.findViewById(R.id.endloc_input);
        mRouteSwitchButton = rootView.findViewById(
                R.id.swap_destination); //The Button to Swap Route Directions
        mRouteSwitchList = rootView.findViewById(R.id.route_switcher_list);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        rootView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootView.getRootView().getHeight();

                        int keypadHeight = screenHeight - r.bottom;

                        if (keypadHeight < screenHeight * 0.15 && routeSwitcherOpen) {
                            routeSwitcherOpen = false;
                            if (startLoc.getPlaceID() == null) {
                                if (endLoc.getPlaceID() == null) {
                                    launchRoute(startLoc,endLoc);
                                } else {
                                    FetchPlaceRequest request = FetchPlaceRequest.builder(
                                            endLoc.toString(),
                                            Arrays.asList(
                                                    com.google.android.libraries.places.api.model.Place
                                                            .Field.LAT_LNG)).build();

                                    placesClient.fetchPlace(request).addOnSuccessListener(
                                            (response) -> {
                                                endLoc = new Place(
                                                        response.getPlace().getLatLng().latitude,
                                                        response.getPlace().getLatLng().longitude,
                                                        endLoc.getName());
                                                launchRoute(startLoc,endLoc);
                                            });
                                }
                            } else {
                                FetchPlaceRequest requestStart = FetchPlaceRequest.builder(
                                        startLoc.toString(),
                                        Arrays.asList(
                                                com.google.android.libraries.places.api.model.Place
                                                        .Field.LAT_LNG)).build();

                                placesClient.fetchPlace(requestStart).addOnSuccessListener(
                                        (responseStart) -> {
                                            if (endLoc.getPlaceID() == null) {
                                                startLoc = new Place(
                                                        responseStart.getPlace().getLatLng().latitude,
                                                        responseStart.getPlace().getLatLng().longitude,
                                                        startLoc.getName());
                                                launchRoute(startLoc, endLoc);
                                            } else {
                                                FetchPlaceRequest requestEnd =
                                                        FetchPlaceRequest.builder(endLoc.toString(),
                                                                Arrays.asList(
                                                                        com.google.android.libraries.places.api.model.Place
                                                                                .Field.LAT_LNG)).build();

                                                placesClient.fetchPlace(
                                                        requestEnd).addOnSuccessListener(
                                                        (responseEnd) -> {
                                                            startLoc = new Place(
                                                                            responseStart.getPlace().getLatLng().latitude,
                                                                            responseStart.getPlace().getLatLng().longitude,
                                                                            startLoc.getName());
                                                            endLoc = new Place(
                                                                    responseEnd.getPlace().getLatLng().latitude,
                                                                    responseEnd.getPlace().getLatLng().longitude,
                                                                    endLoc.getName());
                                                            launchRoute(startLoc, endLoc);
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });

        mRouteSwitcherAdapter = new RouteSwitcherAdapter(getContext(), new ArrayList<Place>());

        mRouteSwitchList.setAdapter(mRouteSwitcherAdapter);
        mRouteSwitchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Checks whether the user is focused on the Start or End inputs
                switch (focusedInput) {
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
                clickRouteIndicator();
            }
        });
        mRouteStartInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                focusedInput = 1;
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> routeSwitcherAutocomplete(charSequence.toString(),
                        mRouteSwitcherAdapter);
                //Delay Requesting Autocomplete
                handler.postDelayed(workRunnable, 250);
                mRouteStartInput.requestFocus();
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
                focusedInput = 2;
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> routeSwitcherAutocomplete(charSequence.toString(),
                        mRouteSwitcherAdapter);
                //Delay Requesting Autocomplete
                handler.postDelayed(workRunnable, 250);
                mRouteEndInput.requestFocus();
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

            getActivity().runOnUiThread(new Runnable() {
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

    private void launchRoute(Place startRoute, Place endRoute) {
        startLoc = startRoute;
        endLoc = endRoute;
        mCallback.changeRoutes(startRoute.toString(),
                endRoute.toString(), endRoute.getName());
        mSearchView.clearSearchFocus();
        mSearchView.setVisibility(View.GONE);
        String s = (startRoute.getName().length() > 20 ?
                startRoute.getName().substring(0, 17) + "..." : startRoute.getName()) + "  >  "
                + (endRoute.getName().length() > 20 ?
                endRoute.getName().substring(0, 17) + "..." : endRoute.getName());
        SpannableString route =
                new SpannableString(
                        s.substring(0, Math.min(s.length(), 42)));
        route.setSpan(new ForegroundColorSpan(Color.parseColor("#08a0e0")),
                0, 19, 0);
        route.setSpan(new ForegroundColorSpan(Color.BLACK),
                Math.min(startRoute.getName().length(), 20),
                route.length(), 0);
        mRouteIndicator.setText(route, TextView.BufferType.SPANNABLE);
        mRouteIndicator.setVisibility(View.VISIBLE);
        mRouteSwitchLayout.setVisibility(View.GONE);

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
                for (Place p : searchResults) {
                    wrappedResults.add(new LocationAutocomplete(p));
                }
            }
            mSearchView.post(new Runnable() {
                public void run() {
                    mSearchView.swapSuggestions(wrappedResults);
                }
            });
        });
    }

    private FloatingSearchView getSearchView() {
        return mSearchView;
    }

    public interface OnSearchFragmentListener {
        void changeRoutes(String start, String end, String name);
    }

    private void resizeSelf(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentListener) {
            mCallback = (OnSearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}
