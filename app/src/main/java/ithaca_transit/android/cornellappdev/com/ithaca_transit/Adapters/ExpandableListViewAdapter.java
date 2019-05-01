package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;


public final class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Direction> mDirections;
    private LayoutInflater mLayoutInflater;


    public ExpandableListViewAdapter(Context context, ArrayList<Direction> directions) {
        mContext = context;
        mDirections = directions;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mDirections.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mDirections.get(i).getStops().length;
    }

    @Override
    public Object getGroup(int i) {
        return mDirections.get(i);
    }

    @Override
    public Object getChild(int directionIndex, int stopIndex) {
        return mDirections.get(directionIndex).getStops()[stopIndex];
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int directionIdx, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.detail_list_item, null);
        }

        Direction direction = mDirections.get(directionIdx);

        TextView numStops = view.findViewById(R.id.num_stops);
        TextView duration = view.findViewById(R.id.direction_duration);
        TextView distance = view.findViewById(R.id.distance);
        ImageView dropArrow = view.findViewById(R.id.drop_down_arrow);
        ImageView dotPartition = view.findViewById(R.id.dot_partition);

        ImageView busStopDot = view.findViewById(R.id.bus_stop_dot);
        ImageView busStopPath = view.findViewById(R.id.bus_stop_path);

        ImageView walkPath1 = view.findViewById(R.id.walk_path_1);
        ImageView walkPath2 = view.findViewById(R.id.walk_path_2);
        ImageView walkPath3 = view.findViewById(R.id.walk_path_3);

        ImageView walkStartDot = view.findViewById(R.id.walk_start_dot);
        ImageView walkEndDot = view.findViewById(R.id.walk_end_dot);

        TextView directionType = view.findViewById(R.id.direction_type);
        TextView directionDestination = view.findViewById(R.id.direction_destination);
        RelativeLayout busContainer = view.findViewById(R.id.bus_container);
        TextView time = view.findViewById(R.id.time);
        View bottomDivider = view.findViewById(R.id.bottom_divider);
        View stopsDivider = view.findViewById(R.id.stops_divider);

        if (direction.getType().equals("depart")) {

            // Setting duration of the route
            duration.setVisibility(View.VISIBLE);
            long difference = TimeUnit.MILLISECONDS.toMinutes(
                    direction.getEndTime().getTime() - direction.getStartTime().getTime());
            duration.setText(difference + "min");

            dotPartition.setVisibility(View.VISIBLE);
            dropArrow.setVisibility(View.VISIBLE);
            stopsDivider.setVisibility(View.VISIBLE);
            busStopDot.setVisibility(View.VISIBLE);
            busStopPath.setVisibility(View.VISIBLE);

            numStops.setVisibility(View.VISIBLE);

            // Include last stop in count (last stop isn't in Stops[])
            numStops.setText((direction.getStops().length + 1) + "stops");

            directionType.setText("Board");
            TextView busNumber = busContainer.findViewById(R.id.tv_bus_number_detail);
            busNumber.setText(direction.getRouteNumber());

            time.setText(getTime(direction.getStartTime()));
        } else if (direction.getType().equals("walk")) {
            bottomDivider.setVisibility(View.VISIBLE);

            if (directionIdx != mDirections.size() - 1) {
                // Walking to bus
                String walkString = "Walk to " + "<b>" + direction.getName() + "</b>";
                directionType.setText(Html.fromHtml(walkString));
                distance.setText(direction.getDistance().intValue() + " ft away");
                time.setText(getTime(direction.getStartTime()));

                walkStartDot.setVisibility(View.VISIBLE);
                walkPath1.setVisibility(View.VISIBLE);
                walkPath2.setVisibility(View.VISIBLE);
                walkPath3.setVisibility(View.VISIBLE);

            } else {
                // Walking to final destination
                directionType.setText(direction.getName());
                distance.setText(direction.getDistance().intValue() + " ft away");
                time.setText(getTime(direction.getEndTime()));

                walkEndDot.setVisibility(View.VISIBLE);
            }
        }
        // Arriving to final stop on bus path (not necessarily final destination)
        else if (direction.getType().equals("arrive")) {
            bottomDivider.setVisibility(View.VISIBLE);

            Direction previousDirection = mDirections.get(directionIdx - 1);
            time.setText(getTime(previousDirection.getEndTime()));

            busStopDot.setVisibility(View.VISIBLE);
            if (directionIdx != mDirections.size() - 1) {
                String walkString = "Get off at " + "<b>" + direction.getName() + "</b>";
                directionType.setText(Html.fromHtml(walkString));
                if (mDirections.get(directionIdx + 1).getType().equals("walk")) {
                    walkPath1.setVisibility(View.VISIBLE);
                    walkPath2.setVisibility(View.VISIBLE);
                    walkPath3.setVisibility(View.VISIBLE);
                } else {
                    busStopPath.setVisibility(View.VISIBLE);
                }
            } else {
                directionType.setText(direction.getName());
            }
        }
        return view;
    }

    @Override
    public View getChildView(int directionIdx, int stopIdx, boolean b, View view,
            ViewGroup viewGroup) {
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.detail_view_child, null);
        }
        Place stop = mDirections.get(directionIdx).getStops()[stopIdx];
        TextView textView = view.findViewById(R.id.stop_name);
        textView.setText(stop.getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public String getTime(Date date) {
        String time;
        if (date.getHours() > 12) {
            time = (date.getHours() - 12) + ":"
                    + date.getMinutes() + "PM";
        } else {
            time = date.getHours() + ":" + date.getMinutes() + "AM";
        }
        return time;
    }
}
