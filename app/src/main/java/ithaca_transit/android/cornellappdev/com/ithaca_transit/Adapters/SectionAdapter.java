package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class SectionAdapter extends StatelessSection {
    private final Context mContext;
    private int mCount;
    private Route[] mRoutesList;
    private String mTitle; // Boarding Soon, From Stops, By Walking
    private String mType; //either optimal, fromStops, boardingSoon, or byWalking section


    public SectionAdapter(Context context,
            Route[] list, String type, String title) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.route_card)
                .headerResourceId(R.layout.header_section)
                .build());
        mContext = context;
        mCount = list.length;
        mRoutesList = list;
        mTitle = title;
        mType = type;
    }

    public void setList(Route[] list, int count, String query) {
        mCount = list.length;
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

        ItemHolder holder2 = (ItemHolder) holder;
        holder2.duration.setText(routeModel.getDuration());
        holder2.route_description.setText(routeModel.getDescription());
//        holder2.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMapsPresenter.onRouteClick(position, mRoutesList);
//            }
//        });
        holder2.directions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        holder2.directions.setLayoutManager((RecyclerView.LayoutManager) layoutManager);

        // Remove doubles from route directions list
        ArrayList<Direction> routeDirections = routeModel.getTruncatedDirections();

        holder2.directions.setAdapter(
                new DirectionsListAdapter(mContext, routeDirections));

        // Remove last element from route directions when we add icons, as we don't need icon for
        // arrival
        LinearLayoutManager iconsLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        holder2.icons.setLayoutManager((RecyclerView.LayoutManager) iconsLayoutManager);
        holder2.icons.setAdapter(new IconsListAdapter(mContext, routeDirections));

        LinearLayoutManager dotsLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        holder2.dots.setLayoutManager((RecyclerView.LayoutManager) dotsLayoutManager);
        holder2.dots.setAdapter(new DotsListAdapter(mContext, routeDirections));

        if (routeModel.getTotalDelay() > 0) {
            if (routeModel.getTotalDelay() < 60) {
                holder2.delay.setText(routeModel.getTotalDelay() + " minutes late");
            } else {
                holder2.delay.setText(((int) routeModel.getTotalDelay() / 60) + " hours late");
            }
            holder2.delay.setTextColor(Color.RED);

        } else {
            holder2.delay.setText("On Time");
        }
    }

    public interface ListAdapterOnClickHandler {
        void onRouteClick(int position, Route[] routesList);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView delay;
        private TextView duration;
        private RecyclerView directions;
        private RecyclerView icons;
        private RecyclerView dots;
        private View rootView;
        private TextView route_description;

        ItemHolder(View itemView) {
            super(itemView);
            delay =  itemView.findViewById(R.id.delay);
            duration = itemView.findViewById(R.id.duration);
            directions = itemView.findViewById(R.id.directions);
            dots = itemView.findViewById(R.id.path_dots);
            icons = itemView.findViewById(R.id.icons);
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
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderHolder(view);
    }
}
