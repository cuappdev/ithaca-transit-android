package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class DirectionsListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Direction> mDirections;

    public DirectionsListAdapter(@NonNull Context context, ArrayList<Direction> directions) {
        mContext = context;
        mDirections = directions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_direction;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        RecyclerView.ViewHolder viewHolder =
                (RecyclerView.ViewHolder) (new DirectionsListAdapter.TextAdapterViewHolder(view));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            throw new ClassCastException(
                    "null cannot be cast to non-null type com.cornellappdev.android.eatery"
                            + ".DirectionsListAdapter.TextAdapterViewHolder");
        } else {
            DirectionsListAdapter.TextAdapterViewHolder holder2 =
                    (DirectionsListAdapter.TextAdapterViewHolder) holder;
            Direction direction = mDirections.get(position);
            if (mDirections.get(position).getType().equals("walk")) {
                holder2.directionDistance.setText(direction.getDistance().toString());
            }
            holder2.directionDestination.setText(direction.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mDirections.size();
    }

    public final class TextAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView directionDestination;
        private TextView directionDistance;
        private View rootView;

        public View getRootView() {
            return rootView;
        }

        public TextView getDirectionDestination() {
            return directionDestination;
        }

        public TextView getDirectionDistance() {
            return directionDistance;
        }

        public TextAdapterViewHolder(@NotNull View itemView) {
            super(itemView);
            this.directionDestination = (TextView) itemView.findViewById(
                    R.id.direction_destination);
            this.directionDistance = (TextView) itemView.findViewById(R.id.direction_distance);
            this.rootView = itemView;
        }
    }
}
