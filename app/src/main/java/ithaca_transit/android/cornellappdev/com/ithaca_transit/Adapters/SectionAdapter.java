package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class SectionAdapter extends StatelessSection {
    private final Context mContext;
    private final MapsPresenter mMapsPresenter;
    private int mCount;
    private Route[] mRoutesList;
    private String mTitle; // Boarding Soon, From Stops, By Walking
    private String mType; //either optimal, fromStops, boardingSoon, or byWalking section


    public SectionAdapter(Context context, MapsPresenter clickHandler,
            int count, Route[] list, String type, String title) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.route_card)
                .headerResourceId(R.layout.header_section)
                .build());
        mContext = context;
        mMapsPresenter = clickHandler;
        mCount = count;
        mRoutesList = list;
        mTitle = title;
        mType = type;
    }

    public void setList(Route[] list, int count, String query) {
        mCount = count;
        mRoutesList = list;
    }

    @Override
    public int getContentItemsTotal() {
        return mCount;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder,
            int position) {
        Route routeModel = mRoutesList[position];
//        switch (mType) {
//            case "optimal":
//                routeModel = mRoutesList[0];
//            case "fromStops":
//                routeModel = mRoutesList[position];
//            case "boardingSoon":
//                routeModel = mRoutesList[position];
//            case "walking":
//                routeModel = mRoutesList[position];
//        }

        ItemHolder holder2 = (ItemHolder) holder;
        holder2.duration.setText(routeModel.getDuration());
        holder2.route_description.setText(routeModel.getDescription());
        holder2.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapsPresenter.onRouteClick(position, mRoutesList);
            }
        });

    }

    public interface ListAdapterOnClickHandler {
        void onRouteClick(int position, Route[] routesList);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView duration;
        private ListView directions;
        private View rootView;
        private TextView route_description;


        ItemHolder(View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            directions = itemView.findViewById(R.id.directions);
            rootView = itemView;
            route_description = itemView.findViewById(R.id.route_description);
         }

    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private View rootView;


        HeaderHolder(View headerView) {
            super(headerView);
            rootView = headerView;
            header = headerView.findViewById(R.id.header_holder);
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderHolder headerHolder = (HeaderHolder) holder;

        headerHolder.header.setText(mTitle);


        //TODO: If section is the second section, it's title should be "See all route options" until slide up

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderHolder(view);
    }



}
