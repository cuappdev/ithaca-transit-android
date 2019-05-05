package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import static android.graphics.Color.GREEN;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import java8.util.Optional;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public final class FavoritesListAdapter extends Adapter {

    // stores all routes for favorite at the position represented by the key
    private HashMap<Integer, SectionedRoutes>
            mAllRoutesToFavorites = new HashMap();
    private Location mLocation;
    private Context mContext;
    private ArrayList<Place> mFavList;
    private final TextAdapterOnClickHandler mListAdapterOnClickHandler;

    // stores most optimal route for favorite
    private Route[] mOptimalRoutes;

    public FavoritesListAdapter(Context context,
            TextAdapterOnClickHandler listAdapterOnClickHandler,
            ArrayList<Place> favorites, Location location) {
        super();
        mContext = context;
        mFavList = favorites;
        mListAdapterOnClickHandler = listAdapterOnClickHandler;
        mOptimalRoutes = new Route[favorites.size()];
        mLocation = location;
    }

    public final void setList(ArrayList<Place> list, String query) {
        mFavList = list;
        mOptimalRoutes = new Route[list.size()];
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.favorites_card;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        ViewHolder viewHolder = (ViewHolder) (new FavoritesListAdapter.TextAdapterViewHolder(view));
        return viewHolder;
    }

    public int getItemCount() {
        return this.mFavList.size();
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) {
            throw new ClassCastException(
                    "null cannot be cast to non-null type com.cornellappdev.android.eatery"
                            + ".FavoritesFavoritesListAdapter.TextAdapterViewHolder");
        } else {
            FavoritesListAdapter.TextAdapterViewHolder holder2 =
                    (FavoritesListAdapter.TextAdapterViewHolder) holder;

            Calendar calendar = Calendar.getInstance(
                    TimeZone.getTimeZone("\"America/NewYork\""));
            long secondsEpoch = (int) (calendar.getTimeInMillis() / 1000L);

            // Get Place object from mLocation
            Place currentLocation = new Place(mLocation.getLatitude(), mLocation.getLongitude(),
                    "Current Location");
            Place favLocation = mFavList.get(position);

            // Getting the other route options
            HashMap<String, String> map = new HashMap();
            map.put("Content-Type", "application/json");
            JSONObject searchJSON = new JSONObject();
            try {
                searchJSON.put("start", currentLocation.toString());
                searchJSON.put("end", favLocation.toString());
                searchJSON.put("destinationName", favLocation.getName());
                searchJSON.put("arriveBy", false);
                searchJSON.put("time", String.valueOf(secondsEpoch));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final RequestBody requestBody =
                    RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                            searchJSON.toString());

            Endpoint searchEndpoint2 = new Endpoint()
                    .path("v2/route")
                    .body(Optional.of(requestBody))
                    .headers(map)
                    .method(Endpoint.Method.POST);

            FutureNovaRequest.make(SectionedRoutes.class, searchEndpoint2).thenAccept(
                    (APIResponse<SectionedRoutes> response) -> {
                        SectionedRoutes sectionedRoutes = response.getData();
                        mAllRoutesToFavorites.put(position, sectionedRoutes);
                        Route optRoute = sectionedRoutes.getOptRoute();
                        mOptimalRoutes[position] = optRoute;

                        // Populate card with response data
                        holder2.getFavoriteName().setText("To " + favLocation.getName());

                        if (!optRoute.isWalkOnlyRoute()) {
                            holder2.onTime.setVisibility(View.VISIBLE);

                            // Getting route number of first bus on route
                            Direction firstBusDirection = optRoute.getBusInfo().get(0);
                            holder2.busNumber.setText(firstBusDirection.getRouteNumber());

                            String busArrival = "<b>" + optRoute.getBusArrival() + "</b>" + " at "
                                    + firstBusDirection.getName();
                            holder2.departureText.setText(busArrival);

                            int delay = optRoute.getTotalDelay();
                            Date currentDate = new Date();
                            Date departureDate =  optRoute.getFirstBusDirection().getStartTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                            if (delay > 60) {
                                String lateBusArrival = optRoute.getDelayedBusArrival(delay);
                                holder2.lateBusTime.setText(lateBusArrival);
                                try {
                                    departureDate = dateFormat.parse(lateBusArrival);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            String boardText = "";
                            if (departureDate.getDay() != currentDate.getDay()) {
                                boardText = boardText + " on " + (departureDate.getMonth() + 1) + "/" +
                                        departureDate.getDate();
                            } else if (departureDate.getTime() < currentDate.getTime()) {
                                boardText = "Board now";
                            } else if (departureDate.getHours() - currentDate.getHours() > 1) {
                                boardText = boardText + " in " + (departureDate.getHours()
                                        - currentDate.getHours()) + " hours";
                            } else {
                                int diff = (departureDate.getMinutes()
                                        - currentDate.getMinutes());

                                // When departure's minutes are less, but still ahead of current time
                                if (diff < 0) {
                                    diff = 60 + diff;
                                }

                                if (diff > 1) {
                                    boardText = boardText + " in " + diff + " minutes";
                                } else if (diff == 1) {
                                    boardText = boardText + " in " + diff + " minute";
                                } else {
                                    boardText = boardText + " now";
                                }
                            }

                            holder2.boardText.setText(boardText);
                            if(delay > 60){
                                holder2.boardText.setTextColor(mContext.getResources().getColor(R.color.green));
                            }
                        }

                    });
        }
    }

    public interface TextAdapterOnClickHandler {
        void onFavoriteClick(int var1, ArrayList<Place> var2);
    }

    public final class TextAdapterViewHolder extends ViewHolder implements OnClickListener {
        private TextView favoriteName;
        private TextView boardText;
        private TextView lateBusTime;
        private TextView departureText;
        private TextView busNumber;
        private ImageView onTime;


        public final TextView getFavoriteName() {
            return this.favoriteName;
        }

        public void onClick(View v) {
            int adapterPosition = this.getAdapterPosition();
            FavoritesListAdapter.this.mListAdapterOnClickHandler.onFavoriteClick(adapterPosition,
                    mFavList);
        }

        public TextAdapterViewHolder(View itemView) {
            super(itemView);
            this.favoriteName = (TextView) itemView.findViewById(R.id.place_name);
            this.busNumber = (TextView) itemView.findViewById(R.id.tv_bus_number);
            this.departureText = (TextView) itemView.findViewById(R.id.text_departure);
            this.boardText = (TextView) itemView.findViewById(R.id.text_board);
            this.onTime = (ImageView) itemView.findViewById(R.id.on_time);
            this.lateBusTime = (TextView) itemView.findViewById(R.id.text_late_departure);
            itemView.setOnClickListener((OnClickListener) this);
        }
    }

    public Route[] getOptimalRoutes() {
        return mOptimalRoutes;
    }

    public HashMap<Integer, SectionedRoutes> getmAllRoutesToFavorites() {
        return mAllRoutesToFavorites;
    }
}
