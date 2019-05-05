package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;

public class MainActivity extends AppCompatActivity implements
        FavoritesListAdapter.TextAdapterOnClickHandler,
        SearchFragment.OnSearchFragmentListener {
    private RecyclerView mRecView;
    private SlidingUpPanelLayout mSlidingPanel;
    private MapFragment mapFragment;
    private DetailViewFragment mDetailViewFragment;
    private FavoritesListAdapter favoriteListAdapter;
    private SearchFragment mSearchFragment;
    public static MapsPresenter mMapsPresenter;
    private OptionsFragment mOptionsFragment;

    private FloatingSearchView mSearchView;
    private DrawerLayout mHomeView;
    private NavigationView mHomeMenu;

    //TODO: move to presenter
    // Hardcoded data for favorites
    private Place ctb = new Place(42.4383786067072, -76.4633538315693, "Collegetown Bagels");
    private Place duffield = new Place(42.4446, -76.4823, "Duffield");
    private Place goldwin = new Place(42.4491, -76.4835, "Goldwin Smith Hall");
    private Place noyes = new Place(42.4465,  -76.4880, "Noyes Recreation Center");
    private Place rpcc = new Place( 42.4559, -76.4775, "Robert Purcell Community Center");
    private Place commons = new Place(42.4405, -76.4965, "Ithaca Commons - Seneca Street");

    private static ArrayList<Place> favoriteList = new ArrayList<Place>();
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();
        mMapsPresenter = new MapsPresenter();
        mSearchFragment = new SearchFragment();
        mSlidingPanel = findViewById(R.id.sliding_panel);

        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.map_container, mapFragment, "");
        ft.replace(R.id.search_view_container, mSearchFragment, "");
        ft.commitAllowingStateLoss();

        mHomeView = findViewById(R.id.home_view);
        mHomeMenu = findViewById(R.id.home_menu);
        mSearchFragment.setUpMenu(mHomeView, mHomeMenu);

        //TODO: move to presenter
        // Adding hardcoded favorites
        favoriteList.add(ctb);
        favoriteList.add(duffield);
        favoriteList.add(goldwin);
        favoriteList.add(noyes);
        favoriteList.add(rpcc);
        favoriteList.add(commons);

        mRecView = this.findViewById(R.id.recycler_view_maps);
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((RecyclerView.LayoutManager) layoutManager);

        // Only showing favorites if we get user's current location
        Context context = this;
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                favoriteListAdapter = new FavoritesListAdapter(getApplicationContext(),
                                        (FavoritesListAdapter.TextAdapterOnClickHandler) context,
                                        favoriteList, location);
                                mRecView.setAdapter(favoriteListAdapter);
                                mRecView.setVisibility(View.VISIBLE);
                                favoriteListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRecView.getVisibility() == View.GONE) {
            mRecView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavoriteClick(int position, ArrayList<Place> list) {
        mapFragment.drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
                favoriteListAdapter.getmAllRoutesToFavorites().get(position));
        mSlidingPanel.setPanelHeight(600);

        mOptionsFragment = new OptionsFragment();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.options_container, mOptionsFragment, "");
        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();

        mOptionsFragment.setUpRecView();
    }

    @Override
    public void changeRoutes(String start, String end, String name) {
        mapFragment.launchRoute(start, end, name, this);
    }


    public void makeOptionsFragment() {
        mSlidingPanel.setPanelHeight(600);

        mOptionsFragment = new OptionsFragment();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.options_container, mOptionsFragment, "");
        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();
        mOptionsFragment.setUpRecView();
    }

    public void makeDetailViewFragment() {
        mDetailViewFragment = new DetailViewFragment();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.options_container, mDetailViewFragment, "");

        fragmentTransaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
        mDetailViewFragment.setUpList();
        mSlidingPanel.setPanelHeight(300);
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }

    public SearchFragment getSearchFragment() {
        return mSearchFragment;
    }
}
