package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.MainActivity;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
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

        holder2.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).getMapFragment().onRouteClick(position, mRoutesList);
            }
        });

        // Remove doubles from route directions list
        ArrayList<Direction> routeDirections = routeModel.getTruncatedDirections();
        int size = routeDirections.size();

        // Set direction section
        holder2.direction_destination_one.setText(routeDirections.get(0).getName());

        // Want to set distance in middle of card if only walk route
        if (size == 2 && routeDirections.get(0).getType().equals("walk") && routeDirections.get(
                1).getType().equals("walk")) {
            holder2.direction_destination_two.setText(routeDirections.get(0).getDistance() + " ft");

            holder2.direction_destination_two.setTextColor(Color.GRAY);
            holder2.direction_destination_three.setText(routeDirections.get(1).getName());
        } else {
            holder2.direction_destination_two.setText(routeDirections.get(1).getName());

            if (size >= 3) {
                holder2.direction_destination_three.setText(routeDirections.get(2).getName());
                if (routeDirections.get(2).getType().equals("walk")) {
                    holder2.direction_distance_three.setText(
                            routeDirections.get(2).getDistance().intValue() + " ft");

                }
            }
            if (size >= 4) {
                holder2.direction_destination_four.setText(routeDirections.get(size - 1).getName());
                if (routeDirections.get(size - 1).getType().equals("walk")) {
                    holder2.direction_distance_four.setText(
                            routeDirections.get(size - 1).getDistance().intValue() + " ft");
                }
            }
        }

        // Set images
        if (size == 2 && routeDirections.get(0).getType().equals("walk") && routeDirections.get(
                1).getType().equals("walk")) {
            holder2.icon_image_four.setVisibility(View.VISIBLE);
        } else {
            if (routeDirections.get(0).getType().equals("walk")) {
                holder2.icon_image_one.setVisibility(View.VISIBLE);
            } else {
                holder2.container_bus_one.setVisibility(View.VISIBLE);
                TextView number = holder2.container_bus_one.findViewById(R.id.tv_bus_number_one);
                number.setText(routeDirections.get(0).getRouteNumber().toString());
            }

            if (size >= 3) {
                if (routeDirections.get(1).getType().equals("walk")) {
                    holder2.icon_image_two.setVisibility(View.VISIBLE);
                } else {
                    holder2.container_bus_two.setVisibility(View.VISIBLE);
                    TextView number = holder2.container_bus_two.findViewById(
                            R.id.tv_bus_number_two);
                    number.setText(routeDirections.get(1).getRouteNumber().toString());
                }
            }

            if (size >= 4) {
                if (routeDirections.get(size - 1).getType().equals("walk")) {
                    holder2.icon_image_three.setVisibility(View.VISIBLE);
                } else {
                    // If we took consecutive buses, just show final bus route
                    holder2.container_bus_three.setVisibility(View.VISIBLE);
                    TextView number = holder2.container_bus_three.findViewById(
                            R.id.tv_bus_number_three);
                    number.setText(routeDirections.get(size - 1).getRouteNumber().toString());
                }
            }
        }

        // Set dots
        if (size == 2 && routeDirections.get(0).getType().equals("walk") && routeDirections.get(
                1).getType().equals("walk")) {
            holder2.dots_walk_only_start.setVisibility(View.VISIBLE);
            holder2.dots_walk_only_end.setVisibility(View.VISIBLE);
        }

        // if not walk only and only start/end, we must be taking bus path
        else if (size < 3) {
            holder2.dots_one_bus.setVisibility(View.VISIBLE);
        } else if (size < 4) {
            // if walking at end, take one bus then walk
            if (routeDirections.get(size - 1).getType().equals("walk")) {
                holder2.dots_one_bus.setVisibility(View.VISIBLE);
                holder2.dots_walk_from_stop.setVisibility(View.VISIBLE);
            }
            // must be taking transfer bus
            else {
                holder2.dots_transfer_bus.setVisibility(View.VISIBLE);
            }
        } else {
            holder2.dots_transfer_bus.setVisibility(View.VISIBLE);
            holder2.dots_transfer_walk.setVisibility(View.VISIBLE);
        }


        // Set delay
        if (routeModel.getTotalDelay() > 0) {
            if (routeModel.getTotalDelay() < 60) {
                holder2.delay.setText(routeModel.getTotalDelay() + " minutes late");
            } else {
                holder2.delay.setText(((int) routeModel.getTotalDelay() / 60) + " hour(s) late");
            }
            holder2.delay.setTextColor(Color.RED);

        } else if (!routeModel.isWalkOnlyRoute()) {
            holder2.delay.setText("On Time");
            holder2.delay_image.setVisibility(View.VISIBLE);
        }
    }

    public interface ListAdapterOnClickHandler {
        void onRouteClick(int position, Route[] routesList);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView delay_image;

        private TextView delay;
        private TextView duration;

        private TextView direction_destination_one;
        private TextView direction_destination_two;
        private TextView direction_destination_three;
        private TextView direction_destination_four;

        private TextView direction_distance_one;
        private TextView direction_distance_two;
        private TextView direction_distance_three;
        private TextView direction_distance_four;

        // Walk icons
        private ImageView icon_image_one;
        private ImageView icon_image_two;
        private ImageView icon_image_three;
        private ImageView icon_image_four;

        // Bus icons
        private RelativeLayout container_bus_one;
        private RelativeLayout container_bus_two;
        private RelativeLayout container_bus_three;

        // Dots
        private ImageView dots_one_bus;
        private ImageView dots_walk_from_stop;
        private ImageView dots_transfer_bus;
        private ImageView dots_transfer_walk;
        private ImageView dots_walk_only_start;
        private ImageView dots_walk_only_end;

        private View rootView;
        private TextView route_description;

        ItemHolder(View itemView) {
            super(itemView);
            delay = itemView.findViewById(R.id.delay);
            delay_image = itemView.findViewById(R.id.delay_image);

            duration = itemView.findViewById(R.id.duration);
            rootView = itemView;
            route_description = itemView.findViewById(R.id.route_description);

            // Getting direction text views
            direction_destination_one = itemView.findViewById(R.id.direction_destination_one);
            direction_destination_two = itemView.findViewById(R.id.direction_destination_two);
            direction_destination_three = itemView.findViewById(R.id.direction_destination_three);
            direction_destination_four = itemView.findViewById(R.id.direction_destination_four);

            // Getting distance text views
            direction_distance_one = itemView.findViewById(R.id.direction_destination_one);
            direction_distance_two = itemView.findViewById(R.id.direction_distance_two);
            direction_distance_three = itemView.findViewById(R.id.direction_distance_three);
            direction_distance_four = itemView.findViewById(R.id.direction_distance_four);

            // Getting walk icon images
            icon_image_one = itemView.findViewById(R.id.ic_walk_one);
            icon_image_two = itemView.findViewById(R.id.ic_walk_two);
            icon_image_three = itemView.findViewById(R.id.ic_walk_three);
            icon_image_four = itemView.findViewById(R.id.ic_walk_four);

            // Only showing at most three buses
            container_bus_one = itemView.findViewById(R.id.container_bus_one);
            container_bus_two = itemView.findViewById(R.id.container_bus_two);
            container_bus_three = itemView.findViewById(R.id.container_bus_three);

            // Dots
            dots_one_bus = itemView.findViewById(R.id.dots_one_bus);
            dots_walk_from_stop = itemView.findViewById(R.id.dots_walk_from_stop);
            dots_transfer_bus = itemView.findViewById(R.id.dots_transfer_bus);
            dots_transfer_walk = itemView.findViewById(R.id.dots_transfer_walk);
            dots_walk_only_start = itemView.findViewById(R.id.dots_walk_only_start);
            dots_walk_only_end = itemView.findViewById(R.id.dots_walk_only_end);

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
