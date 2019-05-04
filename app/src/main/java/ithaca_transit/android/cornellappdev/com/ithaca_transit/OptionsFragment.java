package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;


import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

import java.util.Date;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.SectionAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;


public class OptionsFragment extends Fragment {
    private TextView allRoutesText;
    private RecyclerView recyclerView;
    private Context mContext;
    private SectionedRecyclerViewAdapter routeOptionsListAdapter;

    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_options_extended_fragment, container, false);
        mContext = view.getContext();

        return view;
    }

    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

    public void setUpRecView() {
        // Setting up Extended Fragment recycler view
        RecyclerView recyclerView = ((MainActivity) mContext).findViewById(
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
                "Boarding Soon from Nearby Stops");
        SectionAdapter walkingSection = new SectionAdapter(mContext,
                Repository.getInstance().getRoutesList().getWalking(), "walking", "By Walking");

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

        // Adding dialogue
        ImageView clock = ((MainActivity) mContext).findViewById(R.id.ic_clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogFragment = new Dialog(mContext);
                dialogFragment.setContentView(R.layout.dialog_leave_now);

                DatePicker datePicker = dialogFragment.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogFragment.findViewById(R.id.time_picker);

                Date date = new Date();
                datePicker.init(date.getYear(), date.getMonth(), date.getDay(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker datePicker, int i, int i1,
                                    int i2) {
                                Date leaveDate = new Date();
                                leaveDate.setYear(leaveDate.getYear());
                                leaveDate.setYear(leaveDate.getMonth());
                                leaveDate.setYear(leaveDate.getDay());
                            }
                        });

                dialogFragment.show();
            }
        });
    }

}
