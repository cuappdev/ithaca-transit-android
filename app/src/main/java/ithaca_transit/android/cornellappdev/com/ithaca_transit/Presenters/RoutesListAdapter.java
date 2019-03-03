package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class RoutesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final ListAdapterOnClickHandler mListAdapterOnClickHandler;
    private int mCount;
    private String mQuery;
    private ArrayList<Route> mRoutesList;


    public interface ListAdapterOnClickHandler {
        void onClick(int position, ArrayList<Route> list);
    }

    RoutesListAdapter(
            Context context,
            ListAdapterOnClickHandler clickHandler,
            int count,
            ArrayList<Route> list) {
        mContext = context;
        mListAdapterOnClickHandler = clickHandler;
        mCount = count;
        mRoutesList = list;
    }

    void setList(ArrayList<Route> list, int count, String query) {
        mQuery = query;
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
        final Route routeModel = mRoutesList.get(position);

        ListAdapterViewHolder holder2 = (ListAdapterViewHolder) input_holder;
        //holder2.duration = routeModel.getArrivalTime()

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(routeModel.getArrivalTime());
        int arrivalHour = calendar.get(Calendar.HOUR_OF_DAY);
        int arrivalMintues = calendar.get(Calendar.MINUTE);

        calendar.setTime(routeModel.getDepartureTime());
        int departureHour = calendar.get(Calendar.HOUR_OF_DAY);
        int departureMinutes = calendar.get(Calendar.MINUTE);


        String arrivalAppend;
        String departureAppend;

        if(arrivalHour > 11){
            arrivalAppend = "PM";
        }
        else{
            arrivalAppend = "AM";
        }

        if(departureHour > 11){
            departureAppend = "PM";
        }
        else{
            departureAppend = "AM";
        }
        holder2.duration.setText(arrivalHour + ":" + arrivalMintues + arrivalAppend + "-" +
                departureHour + departureAppend +":" + departureMinutes);

    }

    @Override
    public int getItemCount() {
        return mCount;
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
            mListAdapterOnClickHandler.onClick(adapterPosition, mRoutesList);
        }
    }
}
