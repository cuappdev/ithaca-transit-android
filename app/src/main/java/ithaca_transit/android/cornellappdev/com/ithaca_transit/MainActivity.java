package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;



import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.FavoritesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;

public class MainActivity extends AppCompatActivity implements FavoritesListAdapter.TextAdapterOnClickHandler{
    private RecyclerView mRecView;
    private MapFragment mapFragment;
    private FavoritesListAdapter favoriteListAdapter;

    //TODO: move to presenter
    // Hardcoded data for favorites
    private Place goldwin = new Place(42.4491, -76.4835, "Goldwin");
    private Place duffield = new Place(42.4446, -76.4823, "Duffield");
    private Place dickson = new Place(42.4547, -76.4794, "Clara Dickson");
    private Favorite favorite1 = new Favorite(goldwin, duffield);
    private Favorite favorite2 = new Favorite(dickson, duffield);
    private Favorite favorite3 = new Favorite(duffield, dickson);
    private static ArrayList<Favorite> favoriteList = new ArrayList<Favorite>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.map_container, mapFragment, "");
        ft.commitAllowingStateLoss();

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
    public void onFavoriteClick(int position, @NotNull ArrayList<Favorite> list) {
        mapFragment.drawRoutes(favoriteListAdapter.getOptimalRoutes()[position],
                favoriteListAdapter.getmAllRoutesToFavorites().get(position));
    }
}
