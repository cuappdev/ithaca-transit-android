package ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.SearchView;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.OptionsFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public final class MapsController implements MainListAdapter.ListAdapterOnClickHandler {
    public RecyclerView mRecView;
    public SearchView mSearchView;
    private MainListAdapter listAdapter;
    private FragmentManager mManager;
    private static final Place place1 = new Place("To Goldwin Smith - Ithaca Commons");
    private static final Place place2 = new Place("To Duffield - The Johnson Museum");
    private static final Place place3 = new Place("To The Lux - Gates Hall");
    private static final Place[] placeList;

    public final void setDynamicRecyclerView(@NotNull Context context) {
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecView.setLayoutManager((LayoutManager)layoutManager);
        listAdapter = new MainListAdapter(context, (MainListAdapter.ListAdapterOnClickHandler)this, placeList);
        mRecView.setAdapter(listAdapter);
        mRecView.setVisibility(View.VISIBLE);
        listAdapter.notifyDataSetChanged();
    }

    public void onClick(int position, @NotNull Place[] list) {
        OptionsFragment optionsFragment = new OptionsFragment();
        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, optionsFragment);
        fragmentTransaction.addToBackStack((String)null);
        fragmentTransaction.commit();
    }

    public MapsController(@NotNull FragmentManager manager) {
        super();
        this.mManager = manager;
    }

    static {
        placeList = new Place[]{place1, place2, place3};
    }

}
