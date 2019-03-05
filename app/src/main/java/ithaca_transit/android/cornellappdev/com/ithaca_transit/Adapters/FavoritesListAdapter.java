package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.os.health.SystemHealthManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.BusStop;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.FutureUtilities;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public final class FavoritesListAdapter extends Adapter {

    // stores all routes for favorite at the position represented by the key
    private HashMap<Integer, Route[]> mAllRoutesToFavorites;

    private Endpoint.Config mConfig;
    private Context mContext;
    private Favorite[] mFavList;
    private final FavoritesListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler;

    // stores most optimal route for favorite
    private Route[] mOptimalRoutes;
    private static Repository REPOSITORY;

    public FavoritesListAdapter(@NotNull Context context, @NotNull ListAdapterOnClickHandler listAdapterOnClickHandler,
                                @NotNull Favorite[] favorites, Endpoint.Config config, Repository repository) {
        super();
        mConfig = config;
        mContext = context;
        mFavList = favorites;
        mListAdapterOnClickHandler = listAdapterOnClickHandler;
        mOptimalRoutes = new Route[favorites.length];
        REPOSITORY = repository;
    }


    public final void setList(@NotNull Favorite[] list, @NotNull String query) {
        mFavList = list;
        mOptimalRoutes = new Route[list.length];
        notifyDataSetChanged();
    }

    @Nullable
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.card_item_maps;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        ViewHolder viewHolder = (ViewHolder)(new FavoritesListAdapter.TextAdapterViewHolder(view));
        return viewHolder;
    }

    public int getItemCount() {
        return this.mFavList.length;
    }

    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {
        if (holder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.cornellappdev.android.eatery.FavoritesFavoritesListAdapter.TextAdapterViewHolder");
        } else {
            FavoritesListAdapter.TextAdapterViewHolder holder2 = (FavoritesListAdapter.TextAdapterViewHolder)holder;
            holder2.getFavoriteName().setText(mFavList[position].getStartPlace().getName() + "--" +
                    mFavList[position].getEndPlace().getName());


            // Grabbing route associated with favorite
//            mAllRoutesToFavorites = FutureUtilities.getRoute(mConfig, mFavList[position].getStartPlace(),
//                    mFavList[position].getEndPlace(), mAllRoutesToFavorites, position);

            Map<String, String> mapString = new HashMap<String, String>();
            mapString.put("start", mFavList[position].getStartPlace().toString());
            mapString.put("end", mFavList[position].getEndPlace().toString());
            mapString.put("arriveBy", String.valueOf(false));
            mapString.put("destinationName", mFavList[position].getEndPlace().getName());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("\"America/NewYork\""));
            int secondsEpoch = (int) (calendar.getTimeInMillis()/1000L);
            mapString.put("time", String.valueOf(secondsEpoch));

            Endpoint searchEndpoint = new Endpoint()
                    .queryItems(mapString)
                    .path("route")
                    .method(Endpoint.Method.GET);

            System.out.print("About to look at routes list");
            FutureNovaRequest.make(Route[].class, searchEndpoint).thenAccept((APIResponse<Route[]> response) -> {

                for(Route r:  response.getData()){
                    System.out.println("Broadcasr");

                    System.out.println("Duration " + r.getDuration());
                }
                mAllRoutesToFavorites.put(position, response.getData());
                mOptimalRoutes[position] = response.getData()[0];

            });
        }
    }

    public interface ListAdapterOnClickHandler {
        void onFavoriteClick(int var1, @NotNull Favorite[] var2);
    }

    public final class TextAdapterViewHolder extends ViewHolder implements OnClickListener {
        @NotNull
        private TextView favoriteName;

        @NotNull
        public final TextView getFavoriteName() {
            return this.favoriteName;
        }

        public void onClick(@NotNull View v) {
            int adapterPosition = this.getAdapterPosition();
            FavoritesListAdapter.this.mListAdapterOnClickHandler.onFavoriteClick(adapterPosition,
                    FavoritesListAdapter.this.mFavList);
        }

        public TextAdapterViewHolder(@NotNull View itemView) {
            super(itemView);
            this.favoriteName = (TextView) itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener((OnClickListener)this);
        }
    }

    public Route[] getOptimalRoutes() {
        return mOptimalRoutes;
    }

    public HashMap<Integer, Route[]> getmAllRoutesToFavorites() {
        return mAllRoutesToFavorites;
    }
}
