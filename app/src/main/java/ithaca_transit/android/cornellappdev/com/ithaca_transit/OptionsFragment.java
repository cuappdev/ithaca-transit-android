package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public class OptionsFragment extends Fragment {
    private TextView allRoutesText;
    private RecyclerView recyclerView;
    private Context mContext;
    private SectionedRecyclerViewAdapter routeOptionsListAdapter;

    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_options_holder, container, false);
        mContext = view.getContext();
//        ((TextView)view.findViewById(R.id.allRoutes)).setOnClickListener((new OnClickListener() {
//            public final void onClick(View view) {
//                ExtendedFragment extendedFragment = new ExtendedFragment();
//                FragmentTransaction fragmentTransaction = OptionsFragment.this.getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container, (Fragment)extendedFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        }));

        // Setting up Extended Fragment recycler view
        RecyclerView recyclerView = view.findViewById(
                R.id.nearby_stops_routes);
        routeOptionsListAdapter = new SectionedRecyclerViewAdapter();

        Route optimalAsList[] = new Route[1];
        optimalAsList[0] = Repository.getInstance().getRoutesList().getOptRoute();
        // creating routes list adapter for each section
        SectionAdapter optimalSection = new SectionAdapter(mContext,
                optimalAsList, "optimal", null);
        SectionAdapter fromStopsSection = new SectionAdapter(mContext,
                Repository.getInstance().getRoutesList().getFromStop(), "fromStops", "From Stops");
        SectionAdapter boardingSoonSection = new SectionAdapter(mContext,
                Repository.getInstance().getRoutesList().getBoardingSoon(), "boardingSoon",
                "Boarding Soon");
        SectionAdapter walkingSection = new SectionAdapter(mContext,
                Repository.getInstance().getRoutesList().getWalking(), "walking", "By Walking");

        // Adding sections -- check if some of them are null before adding (optimal will never be null)
        routeOptionsListAdapter.addSection(optimalSection);
        if (Repository.getInstance().getRoutesList().getFromStop() != null &&
                Repository.getInstance().getRoutesList().getFromStop().length != 0) {
            routeOptionsListAdapter.addSection(fromStopsSection);
        }
        if (Repository.getInstance().getRoutesList().getBoardingSoon() != null &&
                Repository.getInstance().getRoutesList().getBoardingSoon().length != 0) {
            routeOptionsListAdapter.addSection(boardingSoonSection);
        }
        // If only route is walk route, set optimal to walk and leave walk section empty
        if (optimalAsList[0] !=
                Repository.getInstance().getRoutesList().getWalking()[0]) {
            routeOptionsListAdapter.addSection(walkingSection);
        }

        recyclerView.setLayoutManager((new LinearLayoutManager(mContext, 1, false)));

        recyclerView.setAdapter(routeOptionsListAdapter);

        return view;
    }

    @NotNull
    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

}
