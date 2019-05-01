package ithaca_transit.android.cornellappdev.com.ithaca_transit;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public SearchFragment() {
    }


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
                            mCallback.changeRoutes(lastLocation.getLatitude() +
                                            ", " + lastLocation.getLongitude(),
                                    dest.toString(), dest.getName());
//                            launchRoute(MapsActivity.this.lastLocation.getLatitude() +
//                                            ", " + MapsActivity.this.lastLocation.getLongitude(),
//                                    dest.toString(), dest.getName());
                        } else {
                            FetchPlaceRequest request = FetchPlaceRequest.builder(dest.toString(),
                                    Arrays.asList(
                                            com.google.android.libraries.places.api.model.Place
                                                    .Field.LAT_LNG)).build();

                            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                                Location lastLocation = mMapsPresenter.getLastLocation();
                                mCallback.changeRoutes(lastLocation.getLatitude() +
                                                ", " + lastLocation.getLongitude(),
                                        response.getPlace().getLatLng().latitude + ", "
                                                + response.getPlace().getLatLng().longitude,
                                        dest.getName());
//                                launchRoute(MapsActivity.this.lastLocation.getLatitude() +
//                                                ", " + MapsActivity.this.lastLocation
//                                                .getLongitude(),
//                                        response.getPlace().getLatLng().latitude + ", "
//                                                + response.getPlace().getLatLng().longitude,
//                                        dest.getName());
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

    public interface OnSearchFragmentListener {
        void changeRoutes(String start, String end, String name);
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
