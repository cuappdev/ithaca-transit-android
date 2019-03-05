package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters.MapsPresenter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class RoutesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private int mCount;
    private final MapsPresenter mMapsPresenter;
    private Route[] mRoutesList;
    

   public RoutesListAdapter( Context context, MapsPresenter clickHandler,
            int count, Route[] list) {
        mContext = context;
        mMapsPresenter = clickHandler;
        mCount = count;
        mRoutesList = list;
    }

    public void setList(Route[] list, int count, String query) {
        mCount = count;
        mRoutesList = list;
        notifyDataSetChanged();
    }

    /**
     * Set view to layout of CardView
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        final int layoutId;
        RecyclerView.ViewHolder viewHolder = null;
        layoutId = R.layout.route_card;
        view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        viewHolder = new ListAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder input_holder, int position) {
        final Route routeModel = mRoutesList[position];

        ListAdapterViewHolder holder2 = (ListAdapterViewHolder) input_holder;
        //holder2.duration = routeModel.getArrivalTime()

        holder2.duration.setText(routeModel.getDuration());
        holder2.route_description.setText(routeModel.getDescription());


        //if direction is walking from start to end, do this
        //if bus then walking, do this


        //holder2.directions.setAdapter();

    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public interface ListAdapterOnClickHandler {
        void onRouteClick(int position, @NotNull Route[] routesList);
    }

    class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView duration;
        TextView route_description;
        ListView directions;

        ListAdapterViewHolder(View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            route_description = itemView.findViewById(R.id.route_description);
            directions = itemView.findViewById(R.id.directions);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mMapsPresenter.onRouteClick(adapterPosition, mRoutesList);
        }
    }

}