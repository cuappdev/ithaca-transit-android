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
import com.cornellappdev.android.eatery.MainListAdapter;
import com.cornellappdev.android.eatery.MainListAdapter.ListAdapterOnClickHandler;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.OptionsFragment;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public final class MapsController implements ListAdapterOnClickHandler {
    @NotNull
    public RecyclerView mRecView;
    @NotNull
    public SearchView mSearchView;
    private MainListAdapter listAdapter;
    private FragmentManager mManager;
    @NotNull
    private static final Place place1 = new Place("To Goldwin Smith - Ithaca Commons");
    @NotNull
    private static final Place place2 = new Place("To Duffield - The Johnson Museum");
    @NotNull
    private static final Place place3 = new Place("To The Lux - Gates Hall");
    @NotNull
    private static final Place[] placeList;

    @NotNull
    public final SearchView getMSearchView() {
        SearchView var10000 = this.mSearchView;
        if (this.mSearchView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSearchView");
        }

        return var10000;
    }

    public final void setMSearchView(@NotNull SearchView var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.mSearchView = var1;
    }

    public final void setDynamicRecyclerView(@NotNull Context context) {
        mRecView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, 0, false);
        mRecView.setLayoutManager((LayoutManager)layoutManager);
        listAdapter = new MainListAdapter(context, (ListAdapterOnClickHandler)this, placeList);
        mRecView.setAdapter(listAdapter);
        mRecView.setVisibility(View.GONE);
        listAdapter.notifyDataSetChanged();
    }

    public void onClick(int position, @NotNull Place[] list) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        ithaca_transit.android.cornellappdev.com.ithaca_transit.OptionsFragment.Companion optionsFragment = OptionsFragment.Companion;
        FragmentTransaction fragmentTransaction = this.mManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, optionsFragment.newInstance());
        fragmentTransaction.addToBackStack((String)null);
        fragmentTransaction.commit();
    }

    public MapsController(@NotNull FragmentManager manager) {
        super();
        Intrinsics.checkParameterIsNotNull(manager, "manager");
        this.mManager = manager;
    }

    static {
        placeList = new Place[]{place1, place2, place3};
    }

    public static final class Companion {
        @NotNull
        public final Place getPlace1() {
            return MapsController.place1;
        }

        @NotNull
        public final Place getPlace2() {
            return MapsController.place2;
        }

        @NotNull
        public final Place getPlace3() {
            return MapsController.place3;
        }

        @NotNull
        public final Place[] getPlaceList() {
            return MapsController.placeList;
        }

        private Companion() {
        }
        }
}
