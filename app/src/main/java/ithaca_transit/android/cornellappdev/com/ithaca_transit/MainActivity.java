package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
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

    public SearchFragment getSearchFragment() {
        return mSearchFragment;
    }

    //TODO: move to presenter
    // Hardcoded data for favorites
    private Place goldwin = new Place(42.4491, -76.4835, "Goldwin");
    private Place duffield = new Place(42.4446, -76.4823, "Duffield");
    private Place dickson = new Place(42.4547, -76.4794, "Clara Dickson");
    private Favorite favorite1 = new Favorite(goldwin, duffield);
    private Favorite favorite2 = new Favorite(dickson, duffield);
    private Favorite favorite3 = new Favorite(duffield, dickson);
    private static ArrayList<Favorite> favoriteList = new ArrayList<Favorite>();
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
        favoriteList.add(favorite1);
        favoriteList.add(favorite2);
        favoriteList.add(favorite3);

        mRecView = this.findViewById(R.id.recycler_view_maps);
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((RecyclerView.LayoutManager) layoutManager);
        favoriteListAdapter = new FavoritesListAdapter(getApplicationContext(),
                (FavoritesListAdapter.TextAdapterOnClickHandler) this,
                favoriteList);
        mRecView.setAdapter(favoriteListAdapter);
        mRecView.setVisibility(View.VISIBLE);
        favoriteListAdapter.notifyDataSetChanged();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRecView.getVisibility() == View.GONE) {
            mRecView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavoriteClick(int position, ArrayList<Favorite> list) {
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
        fragmentTransaction.addToBackStack((String) null);
        fragmentTransaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
        mDetailViewFragment.setUpList();
        mSlidingPanel.setPanelHeight(600);
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }


}
